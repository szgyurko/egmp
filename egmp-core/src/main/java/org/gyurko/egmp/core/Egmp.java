package org.gyurko.egmp.core;

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
