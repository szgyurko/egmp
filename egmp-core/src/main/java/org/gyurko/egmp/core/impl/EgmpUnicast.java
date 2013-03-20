package org.gyurko.egmp.core.impl;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;
import org.gyurko.egmp.core.EgmpException;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpUnicast implements Egmp {
    /** EGMP config */
    private EgmpConfig egmpConfig;

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

    }

    @Override
    public void shutdownEgpmNode() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isElevated() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendHeartBeat() {

    }
}
