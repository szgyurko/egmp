package org.gyurko.egmp.core.impl;

import org.gyurko.egmp.core.Egmp;
import org.gyurko.egmp.core.EgmpConfig;
import org.gyurko.egmp.core.EgmpException;

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
public class EgmpDummy implements Egmp {
    private EgmpConfig egmpConfig;

    public EgmpDummy(final EgmpConfig egmpConfig) {
        this.egmpConfig = egmpConfig;
    }

    private EgmpDummy() {}

    public void initEgmpNode() throws EgmpException {
        throw new EgmpException("Dummy transport mechanism choosen. Check your config to use a valid transport.");
    }

    public void shutdownEgmpNode() {
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
