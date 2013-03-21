package org.gyurko.egmp.core;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public interface EgmpElevationStrategy {
    /** Get elevation level */
    long getElevationLevel();

    /** Get elevation moudle description */
    String getDescription();

    /** Get distributed message. This message will be distributed across the nodes via heartbeat mechanism */
    String getDistributedMessage();
}
