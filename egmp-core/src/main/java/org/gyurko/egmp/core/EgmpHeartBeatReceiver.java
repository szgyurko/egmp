package org.gyurko.egmp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 EGMP - Extensible Group Management Protocol

 Copyright (C) 2013-2015  Szabolcs Gyurko <szabolcs@gyurko.org>

 This file is part of EGMP project.

 EGMP is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, version 2 of the License, but not
 any later version.

 EGMP is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with EGMP.  If not, see <http://www.gnu.org/licenses/>.
 */
public class EgmpHeartBeatReceiver implements Runnable {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpHeartBeatReceiver.class);
    /** EGMP instance */
    private Egmp egmp;
    /** Thread sleep time */
    private static final int SLEEP_TIME = 10;

    /**
     * Default constructor
     *
     * @param implementation EGMP instance object
     */
    public EgmpHeartBeatReceiver(final Egmp implementation) {
        this.egmp = implementation;
    }

    public void run() {
        LOGGER.info("EGMP heart-beat receiver thread has been started");

        if (egmp == null) {
            LOGGER.error("EGMP object passed to EgmpHeartBeatReceiver is null. This should not happen.");
            return;
        }

        while (!Thread.currentThread().interrupted()) {
            egmp.receiveHeartBeat();
            try {
                /* To ensure that we sleep a bit even if the implementation of the receiver fails to */
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ie) {
                break;
            }
        }
    }
}
