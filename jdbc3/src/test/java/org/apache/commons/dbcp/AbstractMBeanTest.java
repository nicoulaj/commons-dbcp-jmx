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
     * @throws IOException if the connection could not be opened.
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
     * @throws IOException if an error occured while closing the connection.
     */
    @AfterTest
    public void closeMBeanServerConnection() throws IOException {
        jmxConnector.close();
        jmxConnectorServer.stop();
    }
}
