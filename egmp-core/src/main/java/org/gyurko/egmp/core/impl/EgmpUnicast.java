package org.gyurko.egmp.core.impl;

import org.gyurko.egmp.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpUnicast implements Egmp {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpUnicast.class);
    /** Receive buffer length */
    private static final int RECEIVE_BUFFER_LENGTH = 1024;
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
        byte[] data = egmpConfig.getElevationStrategy().getDistributedMessage().getBytes();

        packet = new DatagramPacket(data, data.length, egmpConfig.getIp(), egmpConfig.getPort());
        try {
            broadcastSocket = new DatagramSocket();
            broadcastSocket.setBroadcast(true);
            LOGGER.debug("Sending unicast heart-beat to address {} port {}", Inet4Address.getByName("255.255.255.255"), egmpConfig.getPort());
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
        } catch (IOException ioe) {
            LOGGER.warn("Error during receiving UDP packet", ioe);
        }
    }}
