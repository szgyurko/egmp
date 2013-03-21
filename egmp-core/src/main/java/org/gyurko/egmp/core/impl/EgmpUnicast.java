package org.gyurko.egmp.core.impl;

import org.gyurko.egmp.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpUnicast implements Egmp {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpUnicast.class);
    /** Receive buffer length */
    private static final int RECEIVE_BUFFER_LENGTH = 1024;
    /** Timeout, after we restart elevation process if we don't hear an elevated message */
    private static final long ELEVATION_TIMEOUT = 10000;
    /** EGMP config */
    private EgmpConfig egmpConfig;
    /** Heartbeat sender thread */
    private Thread heartBeatSenderThread;
    /** Heartbeat receiver thread */
    private Thread heartBeatReceiverThread;
    /** The unicast socket object */
    private DatagramSocket socket;
    /** Elevation state for the EGMP object */
    private boolean isElevated = true;
    /** TS of last elevated message seen */
    private long lastElevatedMessage = System.currentTimeMillis();

    /**
     * Default constructor.
     *
     * @param config EGMP Config object
     */
    public EgmpUnicast(final EgmpConfig config) {
        egmpConfig = config;
    }

    @Override
    public void initEgmpNode() throws EgmpException {
        LOGGER.info("Starting up EGMP node ~ Unicast communication ~ {}", egmpConfig.getElevationStrategy().getDescription());
        LOGGER.info("Using unicast address {} port {}", egmpConfig.getIp().getHostAddress(),
                egmpConfig.getPort());

        if (egmpConfig.getIp() instanceof Inet6Address) {
            throw new EgmpException("IPv6 does not support unicast for broadcasting. Use Multicast instead.");
        }

        try {
            socket = new DatagramSocket(egmpConfig.getPort(), egmpConfig.getIp());
            LOGGER.info("Receiving messages from unicast address {} port {}", egmpConfig.getIp().getHostAddress(), egmpConfig.getPort());
        } catch (IOException ioe) {
            LOGGER.error("Could not create unicast socket", ioe);
            throw new EgmpException("Could not create unicast socket");
        }

        if (egmpConfig.isHeartBeatSchedulerEnabled()) {
            heartBeatSenderThread = new Thread(new EgmpHeartBeatSender(this));
            heartBeatSenderThread.setDaemon(true);
            heartBeatSenderThread.start();
        }

        heartBeatReceiverThread = new Thread(new EgmpHeartBeatReceiver(this));
        heartBeatReceiverThread.setDaemon(true);
        heartBeatReceiverThread.start();
    }

    @Override
    public void shutdownEgpmNode() {
        if (egmpConfig.isHeartBeatSchedulerEnabled() && heartBeatSenderThread != null && heartBeatSenderThread.isAlive()) {
            heartBeatSenderThread.interrupt();
            try {
                heartBeatSenderThread.join();
            } catch (InterruptedException ie) {
                LOGGER.warn("Interrupted while waiting for the heartbeat sender termination", ie);
            }
        }

        if (heartBeatReceiverThread != null && heartBeatReceiverThread.isAlive()) {
            heartBeatReceiverThread.interrupt();
            try {
                heartBeatReceiverThread.join();
            } catch (InterruptedException ie) {
                LOGGER.warn("Interrupted while waiting for the heartbeat receiver termination", ie);
            }
        }

        socket.close();
    }

    @Override
    public boolean isElevated() {
        return isElevated;
    }

    @Override
    public void sendHeartBeat() {
        DatagramSocket broadcastSocket;
        DatagramPacket packet;
        byte[] data = (egmpConfig.getElevationStrategy().getDistributedMessage() + (isElevated ? "*" : "")).getBytes();

        packet = new DatagramPacket(data, data.length, egmpConfig.getIp(), egmpConfig.getPort());
        try {
            broadcastSocket = new DatagramSocket();
            broadcastSocket.setBroadcast(true);
            LOGGER.debug("Sending unicast heart-beat to address {} port {}", getBroadcastAddress(), egmpConfig.getPort());
            broadcastSocket.send(packet);
        } catch (IOException ioe) {
            LOGGER.warn("Cannot send unicast UDP packet", ioe);
        }
    }

    @Override
    public void receiveHeartBeat() {
        if (socket == null) return;

        DatagramPacket packet;
        byte[] recvbuf = new byte[RECEIVE_BUFFER_LENGTH];

        packet = new DatagramPacket(recvbuf, recvbuf.length);
        try {
            String data;
            socket.receive(packet);

            data = new String(packet.getData()).substring(0, packet.getLength());

            LOGGER.debug("Received data: |{}|", data);
            if (data.endsWith("*")) {
                data = data.substring(0, data.length() - 1);
                lastElevatedMessage = System.currentTimeMillis();
            }

            try {
                long elevation = Long.parseLong(data);
                if (elevation > egmpConfig.getElevationStrategy().getElevationLevel()) {
                    if (isElevated) LOGGER.info("Changing new elevated node to {}", packet.getAddress().getHostAddress());
                    isElevated = false;
                } else {
                    if (!isElevated) LOGGER.info("Changing new elevated node to this node");
                    isElevated = true;
                }
                LOGGER.debug("Current elevation status is {}", (isElevated ? "ELEVATED" : "NON ELEVATED"));
            } catch (NumberFormatException nfe) {
                LOGGER.warn("Data sent by {} is invalid", packet.getAddress().getHostAddress());
            }

            if (lastElevatedMessage + ELEVATION_TIMEOUT < System.currentTimeMillis()) {
                LOGGER.info("Last elevated message timed out, resetting elevation procedure");
                isElevated = true;
                lastElevatedMessage = System.currentTimeMillis();
            }
        } catch (IOException ioe) {
            LOGGER.warn("Error during receiving UDP packet", ioe);
        }
    }

    /**
     * Tries to determine the lan local Broadcast address
     *
     * @returns The LAN local broadcast address
     */
    private InetAddress getBroadcastAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface current = interfaces.nextElement();
                LOGGER.debug("Checking: {} P2P: {}, LOOP: {}, Virtual: {}, Up: {}", current, current.isPointToPoint(), current.isLoopback(), current.isVirtual(), current.isUp());
                if (!current.isUp() || current.isLoopback() || current.isVirtual() || current.isPointToPoint()) continue;
                LOGGER.debug("Matching interface: {}", current);
                for (InterfaceAddress currentAddress : current.getInterfaceAddresses()) {
                    LOGGER.debug("Checking address: {}", currentAddress);
                    if (currentAddress.getAddress().isLoopbackAddress() || !(currentAddress.getAddress() instanceof Inet4Address)) continue;
                    return currentAddress.getBroadcast();
                }
            }
        } catch (SocketException e) {
            LOGGER.warn("Cannot get local host IP");
        }

        InetAddress broadcastAddress = null;

        try {
            broadcastAddress = Inet4Address.getByName("255.255.255.255");
        } catch (UnknownHostException uhe) {
        }

        return broadcastAddress;
    }
}
