package org.gyurko.egmp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpHeartBeatSender implements Runnable {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpHeartBeatSender.class);
    /** EGMP object */
    private Egmp egmp;

    /**
     * Default constructor
     *
     * @param implementation EGMP object to make the callback on
     */
    public EgmpHeartBeatSender(final Egmp implementation) {
        egmp = implementation;
    }

    public void run() {
        LOGGER.info("EGMP heart-beat sender thread has been started");

        if (egmp == null) {
            LOGGER.error("EGMP object passed to EgmpHeartBeatSender is null. This should not happen.");
            return;
        }

        while (!Thread.currentThread().interrupted()) {
            egmp.sendHeartBeat();
            try {
                Thread.sleep(egmp.getEgmpConfig().getHeartBeatSendFrequency());
            } catch (InterruptedException ie) {
                break;
            }
        }
    }
}
