package org.gyurko.egmp.core;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public enum EgmpImplementation {
    /** Dummy implementation */
    UNKNOWN("EgmpDummy"),

    /** Unicast based node management */
    UNICAST("EgmpUnicast"),

    /** Multicast based node management */
    MULTICAST("EgmpMulticast");

    /** Implementation class name */
    private final String implementationClassname;

    /**
     * Constructor.
     *
     * @param classname The classname for the implementation.
     */
    EgmpImplementation(final String classname) {
        implementationClassname = classname;
    }

    @Override
    public String toString() {
        return EgmpImplementation.class.getPackage().getName() + ".impl." + implementationClassname;
    }
}
