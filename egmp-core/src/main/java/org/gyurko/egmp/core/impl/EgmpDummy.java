package org.gyurko.egmp.core.impl;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;
import org.gyurko.egmp.core.EgmpException;

/**
 * Created by GYURKS on 23/11/2015.
 *
 * @author GYURKS
 */
public class EgmpDummy implements Egmp {
    private EgmpConfig egmpConfig;

    public EgmpDummy(final EgmpConfig egmpConfig) {
        this.egmpConfig = egmpConfig;
    }

    private EgmpDummy() {}

    public void initEgmpNode() throws EgmpException {
        throw new EgmpException("Dummy transport mechanism choosen. Check your config to use a valid transport.");
    }

    public void shutdownEgpmNode() {
    }

    public boolean isElevated() {
        return false;
    }

    public void sendHeartBeat() {
    }

    public void receiveHeartBeat() {
    }

    public EgmpConfig getEgmpConfig() {
        return egmpConfig;
    }
}
