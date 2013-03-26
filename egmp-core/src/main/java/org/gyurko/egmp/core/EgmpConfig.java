package org.gyurko.egmp.core;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpConfig {
    /** Multicast V6 group to use */
    private static final String MULTICAST_V6_GROUP = "ff08::be:0101";
    /** Multicast port to use */
    private static final int PORT = 5328;
    /** Implementation for node management */
    private EgmpImplementation implementation = EgmpImplementation.MULTICAST;
    /** IP address */
    private InetAddress ip;
    /** Port to use */
    private int port = PORT;
    /** Whether to start a heartbeat thread or not */
    private boolean heartBeatSchedulerEnabled = false;
    /** Limit datagram packet TTL to a certain number */
    private int datagramPacketTTL = 0;
    /** Elevation strategy to be used */
    private EgmpElevationStrategy elevationStrategy = new DummyElevationStrategy();
    /** Heartbeat sender frequency */
    private long heartBeatSendFrequency = 10000;


    /**
     * Default constructor
     */
    public EgmpConfig() {
        try {
            ip = Inet6Address.getByName(MULTICAST_V6_GROUP);
        } catch (UnknownHostException uh) {
            ip = null;
        }
    }

    /**
     * Standard getter
     *
     * @return EGMP heartbeat send frequency in ms
     */
    public long getHeartBeatSendFrequency() {
        return heartBeatSendFrequency;
    }

    /**
     * Standard setter
     *
     * @param heartBeatSendFrequency New frequency in ms
     */
    public void setHeartBeatSendFrequency(final long heartBeatSendFrequency) {
        this.heartBeatSendFrequency = heartBeatSendFrequency;
    }

    /**
     * Getter for implementation field.
     *
     * @return implementation
     */
    public EgmpImplementation getImplementation() {
        return implementation;
    }

    /**
     * Setter for implementation field.
     *
     * @param impl implementation
     */
    public void setImplementation(final EgmpImplementation impl) {
        this.implementation = impl;
    }

    /**
     * Standard getter
     *
     * @return
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * Standard setter
     *
     * @param ip IP address to use
     */
    public void setIp(final InetAddress ip) {
        this.ip = ip;
    }

    /**
     * Standard getter
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * Standard setter
     *
     * @param port IP port to use
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * Standard getter.
     *
     * @return
     */
    public boolean isHeartBeatSchedulerEnabled() {
        return heartBeatSchedulerEnabled;
    }

    /**
     * Standard setter
     *
     * @param heartBeatSchedulerEnabled true or false to enable heart-beat thread.
     */
    public void setHeartBeatSchedulerEnabled(final boolean heartBeatSchedulerEnabled) {
        this.heartBeatSchedulerEnabled = heartBeatSchedulerEnabled;
    }

    /**
     * Standard getter
     *
     * @return
     */
    public int getDatagramPacketTTL() {
        return datagramPacketTTL;
    }

    /**
     * Standard setter
     *
     * @param datagramPacketTTL TTL for the datagram packet
     */
    public void setDatagramPacketTTL(final int datagramPacketTTL) {
        this.datagramPacketTTL = datagramPacketTTL;
    }

    /**
     * Standard getter
     *
     * @return
     */
    public EgmpElevationStrategy getElevationStrategy() {
        return elevationStrategy;
    }

    /**
     * Standard setter
     *
     * @param elevationStrategy The elevation strategy to be used
     */
    public void setElevationStrategy(final EgmpElevationStrategy elevationStrategy) {
        this.elevationStrategy = elevationStrategy;
    }
}
