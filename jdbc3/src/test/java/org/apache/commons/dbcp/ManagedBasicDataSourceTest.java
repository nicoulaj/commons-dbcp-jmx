/*
 * Copyright 2010-2012 Julien Nicoulaud <julien.nicoulaud@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.dbcp;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import java.util.Properties;
import java.util.Random;

import static org.testng.Assert.assertTrue;

/**
 * Tests for {@link org.apache.commons.dbcp.ManagedBasicDataSource}.
 * <p/>
 * Only tests the added value to {@link org.apache.commons.dbcp.ManagedBasicDataSource}, i.e. the MBean part.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.2
 */
public class ManagedBasicDataSourceTest extends AbstractMBeanTest {

    /**
     * {@link java.util.Random} number generator used for generating dummy datasource settings.
     */
    final static Random randomGenerator = new Random();

    /**
     * Assert creating a new {@link org.apache.commons.dbcp.ManagedBasicDataSource} registers a MBean.
     *
     * @throws Exception should not happen.
     */
    @Test
    public void testMBeanRegistered() throws Exception {
        final ManagedBasicDataSource testDataSource = new ManagedBasicDataSource();
        assertTrue(getMBeanServerConnection().isRegistered(new ObjectName(testDataSource.getMBeanName())));
    }

    /**
     * Assert creating a new {@link org.apache.commons.dbcp.ManagedBasicDataSource} through the {@link org.apache.commons.dbcp.ManagedBasicDataSourceFactory}
     * registers a MBean.
     *
     * @throws Exception should not happen.
     */
    @Test
    public void testMBeanRegisteredThroughFactory() throws Exception {
        final ManagedBasicDataSource testDataSource = (ManagedBasicDataSource) ManagedBasicDataSourceFactory.createDataSource(new Properties());
        assertTrue(getMBeanServerConnection().isRegistered(new ObjectName(testDataSource.getMBeanName())));
    }

    /**
     * Data provider for {@link #testAttributeExists(String)}.
     *
     * @return an array of parameters to be passed to {@link #testAttributeExists(String)}.
     */
    @DataProvider(name = "testAttributeExistsProvider")
    public Object[][] testAttributeExistsProvider() {
        return new Object[][]{{"NumActive"}, {"NumIdle"}, {"MaxActive"}, {"MaxIdle"}, {"MinIdle"}, {"MaxWait"}, {"Url"}, {"Username"}};
    }

    /**
     * Assert an attribute exists in the MBean.
     *
     * @param attributeName the name of the attribute in the MBean.
     * @throws Exception should not happen.
     */
    @Test(dataProvider = "testAttributeExistsProvider")
    public void testAttributeExists(String attributeName) throws Exception {

        // Create a datasource.
        final ManagedBasicDataSource testDataSource = (ManagedBasicDataSource) ManagedBasicDataSourceFactory.createDataSource(new Properties());

        // Get the value of the attribute from the MBean.
        getMBeanServerConnection().getAttribute(new ObjectName(testDataSource.getMBeanName()), attributeName);
    }

    /**
     * Data provider for {@link #testConfigurableAttribute(String, Object, String, String)}.
     *
     * @return an array of parameters to be passed to {@link #testConfigurableAttribute(String, Object, String, String)}.
     */
    @DataProvider(name = "testConfigurableAttributeProvider")
    public Object[][] testConfigurableAttributeProvider() {
        return new Object[][]{
                {ManagedBasicDataSourceFactory.PROP_MAXACTIVE, randomGenerator.nextInt(1000), "MaxActive", "getMaxActive"},
                {ManagedBasicDataSourceFactory.PROP_MAXIDLE, randomGenerator.nextInt(1000), "MaxIdle", "getMaxIdle"},
                {ManagedBasicDataSourceFactory.PROP_MINIDLE, randomGenerator.nextInt(1000), "MinIdle", "getMinIdle"},
                {ManagedBasicDataSourceFactory.PROP_MAXWAIT, randomGenerator.nextLong(), "MaxWait", "getMaxWait"}
        };
    }

    /**
     * Assert configuration of an attribute is taken into account and has the same value when queried directly or through the MBean.
     *
     * @param attributeProperty      the property to use to configure the attribute.
     * @param attributePropertyValue the value to use for the attribute.
     * @param attributeName          the name of the attribute in the MBean.
     * @param attributeGetterName    the name of the getter method for the attribute.
     * @throws Exception should not happen.
     */
    @Test(dataProvider = "testConfigurableAttributeProvider")
    public void testConfigurableAttribute(String attributeProperty,
                                          Object attributePropertyValue,
                                          String attributeName,
                                          String attributeGetterName) throws Exception {

        // Create a datasource and set the attribute parameter.
        final Properties testDataSourceProperties = new Properties();
        testDataSourceProperties.put(attributeProperty, String.valueOf(attributePropertyValue));
        final ManagedBasicDataSource testDataSource = (ManagedBasicDataSource) ManagedBasicDataSourceFactory.createDataSource(testDataSourceProperties);

        // Get the value of the attribute from the MBean.
        final Object attributeValue = getMBeanServerConnection().getAttribute(new ObjectName(testDataSource.getMBeanName()), attributeName);

        // Check both values are equal.
        assertTrue((ManagedBasicDataSource.class.getMethod(attributeGetterName).invoke(testDataSource)).equals(attributeValue));
    }
}
