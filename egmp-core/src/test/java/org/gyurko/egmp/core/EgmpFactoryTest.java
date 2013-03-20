package org.gyurko.egmp.core;

import junit.framework.Assert;
import org.gyurko.egmp.core.impl.EgmpMulticast;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Szabolcs Gyurko
 * Date: 3/20/13
 * Time: 8:09 PM
 *
 * @author szabolcs
 */
public class EgmpFactoryTest {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgmpFactoryTest.class);

    @Test
    public void testGetInstance() throws Exception {
        EgmpConfig config = new EgmpConfig();
        Egmp instance = EgmpFactory.getInstance(config);

        Assert.assertEquals(EgmpMulticast.class.getCanonicalName(), instance.getClass().getCanonicalName());
        LOGGER.debug(instance.getClass().getCanonicalName());
    }
}
