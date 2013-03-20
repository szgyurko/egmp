package org.gyurko.egmp.core.impl;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;
import org.gyurko.egmp.core.EgmpException;
import org.gyurko.egmp.core.EgmpHeartBeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpMulticast implements Egmp {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpMulticast.class);
    /** EGMP config */
    private EgmpConfig egmpConfig;
    /** Heartbeat thread */
    private Thread heartBeatThread;
    /** The multicast socket object */
    private MulticastSocket socket;

    /**
     * Default constructor.
     *
     * @param config EGMP Config object
     */
    public EgmpMulticast(final EgmpConfig config) {
        egmpConfig = config;
    }

    @Override
    public void initEgmpNode() throws EgmpException {
        LOGGER.info("Starting up EGMP node ~ Multicast communication");
        LOGGER.info("Using multicast group {} port {}", egmpConfig.getIP().getHostAddress(),
                                                        egmpConfig.getPort());

        if (egmpConfig.isHeartBeatSchedulerEnabled()) {
            heartBeatThread = new Thread(new EgmpHeartBeat(this));
            heartBeatThread.setDaemon(true);
            heartBeatThread.start();
        }

        try {
            socket = new MulticastSocket();
        } catch (IOException ioe) {
            LOGGER.error("Could not create multicast socket");
            throw new EgmpException("Could not create multicast socket");
        }
    }

    @Override
    public void shutdownEgpmNode() {
        if (egmpConfig.isHeartBeatSchedulerEnabled() && heartBeatThread != null && heartBeatThread.isAlive()) {
            heartBeatThread.interrupt();
            try {
                heartBeatThread.join();
            } catch (InterruptedException ie) {
                LOGGER.warn("Interrupted while waiting for the heartbeat termination", ie);
            }
        }
    }

    @Override
    public boolean isElevated() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendHeartBeat() {
        DatagramPacket packet;

        packet = new DatagramPacket("NODE".getBytes(), 4, egmpConfig.getIP(), egmpConfig.getPort());
        try {
            LOGGER.debug("Sending multicast heart-beat to group {} port {}", egmpConfig.getIP().getHostName(), egmpConfig.getPort());
            socket.send(packet);
        } catch (IOException ioe) {
            LOGGER.warn("Cannot send multicast UDP packet", ioe);
        }
    }
}
