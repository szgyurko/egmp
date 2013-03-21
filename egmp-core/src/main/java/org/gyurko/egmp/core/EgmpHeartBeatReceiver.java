package org.gyurko.egmp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpHeartBeatReceiver implements Runnable {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpHeartBeatReceiver.class);
    /** EGMP instance */
    private Egmp egmp;
    /** Thread sleep time */
    private static final int SLEEP_TIME = 10000;

    /**
     * Default constructor
     *
     * @param implementation EGMP instance object
     */
    public EgmpHeartBeatReceiver(final Egmp implementation) {
        this.egmp = implementation;
    }

    @Override
    public void run() {
        LOGGER.info("EGMP heart-beat receiver thread has been started");

        if (egmp == null) {
            LOGGER.error("EGMP object passed to EgmpHeartBeatReceiver is null. This should not happen.");
            return;
        }

        while (!Thread.currentThread().interrupted()) {
            try {
                egmp.receiveHeartBeat();

                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ie) {
                break;
            }
        }
    }
}
