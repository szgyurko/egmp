package org.gyurko.egmp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
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
