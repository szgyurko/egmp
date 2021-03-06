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
public class DummyElevationStrategy implements EgmpElevationStrategy {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(DummyElevationStrategy.class);

    public long getElevationLevel() {
        return 0;
    }

    public String getDescription() {
        LOGGER.error("Null Elevation Strategy - Please select a valid elevation strategy");
        return "Null Elevation Strategy - Please select a valid elevation strategy";
    }

    public String getDistributedMessage() {
        return Long.toString(getElevationLevel());
    }
}
