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
public enum EgmpImplementation {
    /** Dummy implementation */
    DUMMY("EgmpDummy"),

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
