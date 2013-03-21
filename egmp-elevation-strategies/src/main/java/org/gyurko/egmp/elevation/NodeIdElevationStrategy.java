package org.gyurko.egmp.elevation;

import org.gyurko.egmp.core.DummyElevationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class NodeIdElevationStrategy extends DummyElevationStrategy {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeIdElevationStrategy.class);
    /** bytes in java are signed, therefore if a negative number comes out, we have to do binary complement operation */
    private static final long BYTE_OVERFLOW_VALUE = 127;

    @Override
    public long getElevationLevel() {
        long elevation = 0;

        try {
            elevation = Long.parseLong(System.getProperty("EGMP_NODE_ID"));
        } catch (NumberFormatException nfe) {
            LOGGER.error("EGMP_NODE_ID system property (JVM level) is not defined, but you are using node ID based elevation. Please define it.");
        }

        return elevation;
    }

    @Override
    public String getDescription() {
        return "Node ID Elevation Strategy";
    }
}
