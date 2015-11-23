package org.gyurko.egmp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
public final class EgmpFactory {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpFactory.class);

    /**
     * Creates an EGMP instance.
     *
     * @param config Configuration for the EGMP node.
     * @return EGMP instance
     */
    public static Egmp getInstance(final EgmpConfig config) {
        Class<? extends Egmp> clazz;

        try {
            clazz = Class.forName(config.getImplementation().toString()).asSubclass(Egmp.class);
            Constructor<? extends Egmp> constructor = clazz.getConstructor(EgmpConfig.class);
            Egmp egmp = constructor.newInstance(config);

            return egmp;
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("Implementation class not found. Check you config.implementation value.", cnfe);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Implementation class invalid.", e);
        } catch (InstantiationException e) {
            LOGGER.error("Implementation class cannot be instantiated", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Security violation during instantiation", e);
        } catch (InvocationTargetException e) {
            LOGGER.error("Implementation class cannot be instantiated", e);
        }

        return null;
    }

    /** Default constructor disallows creating an instance of this class */
    private EgmpFactory() {}
}
