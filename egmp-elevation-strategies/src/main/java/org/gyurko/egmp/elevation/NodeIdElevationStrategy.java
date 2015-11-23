package org.gyurko.egmp.elevation;

import org.gyurko.egmp.core.DummyElevationStrategy;
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
public class NodeIdElevationStrategy extends DummyElevationStrategy {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeIdElevationStrategy.class);

    @Override
    public long getElevationLevel() {
        long elevation = 0;

        try {
            elevation = Long.parseLong(System.getProperty("EGMP_NODE_ID"));
        } catch (NumberFormatException nfe) {
            LOGGER.error("EGMP_NODE_ID system property (JVM level) is not defined, but you are using node ID based elevation. Please check your config.");
        }

        return elevation;
    }

    @Override
    public String getDescription() {
        return "Node ID Elevation Strategy";
    }
}
