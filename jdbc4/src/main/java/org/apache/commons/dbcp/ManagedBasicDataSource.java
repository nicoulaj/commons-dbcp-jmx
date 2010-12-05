/*
 * Copyright 2010 Julien Nicoulaud <julien.nicoulaud@gmail.com>
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

import org.weakref.jmx.MBeanExporter;
import org.weakref.jmx.Managed;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Wrapper for {@link BasicDataSource} that exposes some fields and methods as a MBean.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
public class ManagedBasicDataSource extends BasicDataSource {

    /**
     * The default auto-generated unique name for the exposed MBean.
     */
    public static final String DEFAULT_MBEAN_NAME = "org.apache.commons.dbcp:ManagedBasicDataSource=ManagedBasicDataSource-" + UUID.randomUUID();

    /**
     * The name under which this object is exposed to the MBean server.
     */
    protected final String mBeanName;

    /**
     * Build a new instance of {@link org.apache.commons.dbcp.ManagedBasicDataSource} and expose it as a MBean with an auto-generated unique name.
     *
     * @see #DEFAULT_MBEAN_NAME
     * @see #exportMBean(String)
     */
    public ManagedBasicDataSource() {
        this(DEFAULT_MBEAN_NAME);
    }

    /**
     * Build a new instance of {@link org.apache.commons.dbcp.ManagedBasicDataSource} and expose it as a MBean with the specified name.
     *
     * @param mBeanName the name of the MBean to expose, should be unique accross the target application.
     * @see #DEFAULT_MBEAN_NAME
     * @see #exportMBean(String)
     */
    public ManagedBasicDataSource(String mBeanName) {
        this.mBeanName = mBeanName;
        exportMBean(mBeanName);
    }

    /**
     * Export this object as a MBean to the platform default MBean server.
     *
     * @param name the name of the MBean to expose.
     */
    protected synchronized void exportMBean(String name) {
        MBeanExporter.withPlatformMBeanServer().export(name, this);
    }

    /**
     * Get the name under which this object is exposed to the MBean server.
     *
     * @return the MBean object name.
     */
    public String getmBeanName() {
        return mBeanName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return iface.cast(this);
        }
        return super.unwrap(iface);
    }

    /**
     * Get the current number of active connections that have been allocated from this data source.
     *
     * @return the current number of active connections.
     */
    @Managed(description = "The current number of active connections that have been allocated from this data source.")
    public synchronized int getNumActive() {
        return super.getNumActive();
    }

    /**
     * Get the current number of idle connections that are waiting to be allocated from this data source.
     *
     * @return the current number of idle connections.
     */
    @Managed(description = "The current number of idle connections that are waiting to be allocated from this data source.")
    public synchronized int getNumIdle() {
        return super.getNumIdle();
    }

    /**
     * Get the maximum number of active connections that can be allocated at the same time.
     * <p/>
     * <p>A negative number means that there is no limit.</p>
     *
     * @return the maximum number of active connections.
     */
    @Managed(description = "The maximum number of active connections that can be allocated at the same time.")
    public synchronized int getMaxActive() {
        return super.getMaxActive();
    }

    /**
     * Set the maximum number of active connections that can be allocated at the same time. Use a negative value for no limit.
     *
     * @param maxActive the new value for maxActive.
     * @see #getMaxActive()
     */
    @Managed(description = "Set the maximum number of active connections that can be allocated at the same time. Use a negative value for no limit.")
    public synchronized void setMaxActive(int maxActive) {
        super.setMaxActive(maxActive);
    }

    /**
     * Get the maximum number of connections that can remain idle in the pool.
     * <p/>
     * <p>A negative value indicates that there is no limit.</p>
     *
     * @return the maximum number of idle connections.
     */
    @Managed(description = "The maximum number of connections that can remain idle in the pool.")
    public synchronized int getMaxIdle() {
        return super.getMaxIdle();
    }

    /**
     * Set the maximum number of connections that can remain idle in the pool.
     *
     * @param maxIdle the new value for maxIdle.
     * @see #getMaxIdle()
     */
    @Managed(description = "Set the maximum number of connections that can remain idle in the pool.")
    public synchronized void setMaxIdle(int maxIdle) {
        super.setMaxIdle(maxIdle);
    }

    /**
     * Get the minimum number of idle connections in the pool.
     *
     * @return the minimum number of idle connections.
     * @see org.apache.commons.pool.impl.GenericObjectPool#getMinIdle()
     */
    @Managed(description = "The minimum number of idle connections in the pool.")
    public synchronized int getMinIdle() {
        return super.getMinIdle();
    }

    /**
     * Set the minimum number of idle connections in the pool.
     *
     * @param minIdle the new value for minIdle.
     * @see org.apache.commons.pool.impl.GenericObjectPool#setMinIdle(int)
     */
    @Managed(description = "The minimum number of idle connections in the pool.")
    public synchronized void setMinIdle(int minIdle) {
        super.setMinIdle(minIdle);
    }

    /**
     * Get the maximum number of milliseconds that the pool will wait for a connection to be returned before throwing an exception.
     * <p/>
     * <p>A value less than or equal to zero means the pool is set to wait indefinitely.</p>
     *
     * @return the maxWait property value.
     */
    @Managed(description = "The maximum number of milliseconds that the pool will wait for a connection to be returned before throwing an exception.")
    public synchronized long getMaxWait() {
        return super.getMaxWait();
    }

    /**
     * Set the maxWait property.
     * <p/>
     * <p>Use -1 to make the pool wait indefinitely.</p>
     *
     * @param maxWait the new value for maxWait.
     * @see #getMaxWait()
     */
    @Managed(description = "Set the maxWait property. Use -1 to make the pool wait indefinitely.")
    public synchronized void setMaxWait(long maxWait) {
        super.setMaxWait(maxWait);
    }
}
