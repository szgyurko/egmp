package org.gyurko.egmp.core;

import java.net.Inet6Address;
import java.net.InetAddress;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpConfig {
    /** Implementation for node management */
    private EgmpImplementation implementation = EgmpImplementation.MULTICAST;
    /** IP address */
    private InetAddress ip;
    /** Port to use */
    private int port;
    /** Whether to start a heartbeat thread or not */
    private boolean heartBeatSchedulerEnabled = false;
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
    public InetAddress getIP() {
        return ip;
    }

    /**
     * Standard setter
     *
     * @param ip New multicast address
     */
    public void setIP(final InetAddress ip) {
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
}
