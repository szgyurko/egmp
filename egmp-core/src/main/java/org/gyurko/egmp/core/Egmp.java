package org.gyurko.egmp.core;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public interface Egmp {

    /**
     * Initializes EGMP node. This is the first call to be made.
     *
     * @see EgmpConfig
     */
    void initEgmpNode() throws EgmpException;

    /**
     * Destroys EGMP node.
     */
    void shutdownEgpmNode();

    /** Returns true if the EGMP node is the elevated node. False otherwise */
    boolean isElevated();

    /**
     * Send heart-beat
     */
    void sendHeartBeat();

    /**
     * Receive heart-beat message
     */
    void receiveHeartBeat();

    /**
     * Standard getter
     *
     * @return EGMP Config object
     */
    EgmpConfig getEgmpConfig();
}
