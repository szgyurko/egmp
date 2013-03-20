package org.gyurko.egmp.core;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public interface Egmp {

    /**
     * Initializes EGMP node. This is the first call to be made.
     *
     * @param config The configuration for the EGMP node.
     * @see EgmpConfig
     */
    void initEgmpNode(final EgmpConfig config);

    /**
     * Destroys EGMP node.
     */
    void shutdownEgpmNode();

    /** Returns true if the EGMP node is the elevated node. False otherwise */
    boolean isElevated();
}
