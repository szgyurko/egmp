package org.gyurko.egmp.core.impl;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpMulticast implements Egmp {
    @Override
    public void initEgmpNode(final EgmpConfig config) {

    }

    @Override
    public void shutdownEgpmNode() {
    }

    @Override
    public boolean isElevated() {
        return false;
    }
}
