package org.gyurko.egmp.core.impl;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpMulticast implements Egmp {
    /** EGMP config */
    private EgmpConfig egmpConfig;

    /**
     * Default constructor.
     *
     * @param config EGMP Config object
     */
    public EgmpMulticast(final EgmpConfig config) {
        egmpConfig = config;
    }

    @Override
    public void initEgmpNode(final EgmpConfig config) {

    }

    @Override
    public void shutdownEgpmNode() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isElevated() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
