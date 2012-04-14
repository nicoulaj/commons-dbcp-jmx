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

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import javax.management.MBeanServerConnection;
import javax.management.remote.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Helper class for MBean tests.
 * <p/>
 * Opens a connection on the platform MBean server the extending class can use with {#getMBeanServerConnection}.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.2
 */
public abstract class AbstractMBeanTest {

    /**
     * The {@link javax.management.remote.JMXConnectorServer}.
     */
    private JMXConnectorServer jmxConnectorServer;

    /**
     * The {@link javax.management.remote.JMXConnector}.
     */
    private JMXConnector jmxConnector;

    /**
     * The {@link javax.management.MBeanServerConnection} used by extending classes.
     */
    private MBeanServerConnection mBeanServerConnection;

    /**
     * Get the {@link javax.management.MBeanServerConnection} on the MBean server used for tests.
     *
     * @return an alive connection the platform MBean server.
     */
    protected MBeanServerConnection getMBeanServerConnection() {
        return mBeanServerConnection;
    }

    /**
     * Open a connection on the platform MBean server before tests.
     *
     * @throws java.io.IOException if the connection could not be opened.
     */
    @BeforeTest
    public void prepareMBeanServerConnection() throws IOException {
        jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(new JMXServiceURL("service:jmx:rmi://"),
                                                                             null,
                                                                             ManagementFactory.getPlatformMBeanServer());
        jmxConnectorServer.start();

        jmxConnector = JMXConnectorFactory.connect(jmxConnectorServer.getAddress());
        mBeanServerConnection = jmxConnector.getMBeanServerConnection();
    }

    /**
     * Close the connection on the MBean server.
     *
     * @throws java.io.IOException if an error occured while closing the connection.
     */
    @AfterTest
    public void closeMBeanServerConnection() throws IOException {
        jmxConnector.close();
        jmxConnectorServer.stop();
    }
}
