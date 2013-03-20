package org.gyurko.egmp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpHeartBeat implements Runnable {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpHeartBeat.class);
    /** EGMP object */
    private Egmp egmp;
    /** Thread sleep time */
    private static final int SLEEP_TIME = 10000;

    /**
     * Default constructor
     *
     * @param implementation EGMP object to make the callback on
     */
    public EgmpHeartBeat(final Egmp implementation) {
        egmp = implementation;
    }

    @Override
    public void run() {
        LOGGER.info("EGMP heart-beat thread has been started");

        if (egmp == null) {
            LOGGER.error("EGMP object passed to EgmpHeartBeat is null. This should not happen.");
            return;
        }

        while (!Thread.currentThread().interrupted()) {
            try {
                egmp.sendHeartBeat();

                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ie) {
                break;
            }
        }
    }
}