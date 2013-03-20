package org.gyurko.egmp.core;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class EgmpConfig {
    /** Implementation for node management */
    private EgmpImplementation implementation = EgmpImplementation.MULTICAST;

    /**
     * Getter for implementation field.
     *
     * @return implementation
     */
    public EgmpImplementation getImplementation() {
        return implementation;
    }

    /**
     * Setter for implementation field.
     *
     * @param impl implementation
     */
    public void setImplementation(final EgmpImplementation impl) {
        this.implementation = impl;
    }
}
