package org.gyurko.egmp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
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
