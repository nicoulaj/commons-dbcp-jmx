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

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.*;

/**
 * Factory for {@link ManagedBasicDataSource}.
 * <p/>
 * This is almost a copy/paste of {@link BasicDataSourceFactory}, since most of the required fields in
 * {@link BasicDataSourceFactory} are private. It only adds the {@link #PROP_MBEANNAME} property to the datasource configuration
 * properties set.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @see ManagedBasicDataSource
 * @see BasicDataSourceFactory
 * @since 0.1
 */
public class ManagedBasicDataSourceFactory extends BasicDataSourceFactory {

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_DEFAULTAUTOCOMMIT = "defaultAutoCommit";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_DEFAULTREADONLY = "defaultReadOnly";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_DEFAULTTRANSACTIONISOLATION = "defaultTransactionIsolation";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_DEFAULTCATALOG = "defaultCatalog";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_DRIVERCLASSNAME = "driverClassName";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_MAXACTIVE = "maxActive";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_MAXIDLE = "maxIdle";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_MINIDLE = "minIdle";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_INITIALSIZE = "initialSize";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_MAXWAIT = "maxWait";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_TESTONBORROW = "testOnBorrow";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_TESTONRETURN = "testOnReturn";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_NUMTESTSPEREVICTIONRUN = "numTestsPerEvictionRun";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_TESTWHILEIDLE = "testWhileIdle";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_PASSWORD = "password";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_URL = "url";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_USERNAME = "username";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_VALIDATIONQUERY = "validationQuery";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_VALIDATIONQUERY_TIMEOUT = "validationQueryTimeout";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_INITCONNECTIONSQLS = "initConnectionSqls";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED = "accessToUnderlyingConnectionAllowed";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_REMOVEABANDONED = "removeAbandoned";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_REMOVEABANDONEDTIMEOUT = "removeAbandonedTimeout";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_LOGABANDONED = "logAbandoned";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_POOLPREPAREDSTATEMENTS = "poolPreparedStatements";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_MAXOPENPREPAREDSTATEMENTS = "maxOpenPreparedStatements";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String PROP_CONNECTIONPROPERTIES = "connectionProperties";

    /**
     * The key for the property where one can specify the name of the {@link ManagedBasicDataSource} Mbean.
     *
     * @see ManagedBasicDataSource#DEFAULT_MBEAN_NAME
     */
    public final static String PROP_MBEANNAME = "mbeanName";

    /**
     * TODO Add Javadoc comment.
     */
    public final static String[] ALL_PROPERTIES = {
            PROP_DEFAULTAUTOCOMMIT,
            PROP_DEFAULTREADONLY,
            PROP_DEFAULTTRANSACTIONISOLATION,
            PROP_DEFAULTCATALOG,
            PROP_DRIVERCLASSNAME,
            PROP_MAXACTIVE,
            PROP_MAXIDLE,
            PROP_MINIDLE,
            PROP_INITIALSIZE,
            PROP_MAXWAIT,
            PROP_TESTONBORROW,
            PROP_TESTONRETURN,
            PROP_TIMEBETWEENEVICTIONRUNSMILLIS,
            PROP_NUMTESTSPEREVICTIONRUN,
            PROP_MINEVICTABLEIDLETIMEMILLIS,
            PROP_TESTWHILEIDLE,
            PROP_PASSWORD,
            PROP_URL,
            PROP_USERNAME,
            PROP_VALIDATIONQUERY,
            PROP_VALIDATIONQUERY_TIMEOUT,
            PROP_INITCONNECTIONSQLS,
            PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED,
            PROP_REMOVEABANDONED,
            PROP_REMOVEABANDONEDTIMEOUT,
            PROP_LOGABANDONED,
            PROP_POOLPREPAREDSTATEMENTS,
            PROP_MAXOPENPREPAREDSTATEMENTS,
            PROP_CONNECTIONPROPERTIES,
            PROP_MBEANNAME
    };

    /**
     * Create and return a new {@link org.apache.commons.dbcp.ManagedBasicDataSource} instance.  If no instance can be created, return <code>null</code>
     * instead.
     *
     * @param obj         The possibly null object containing location or reference information that can be used in creating an object.
     * @param name        The name of this object relative to <code>nameCtx</code>.
     * @param nameCtx     The context relative to which the <code>name</code> parameter is specified, or <code>null</code> if <code>name</code> is
     *                    relative to the default initial context.
     * @param environment The possibly null environment that is used in creating this object.
     * @throws Exception if an exception occurs creating the instance.
     */
    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {

        // We only know how to deal with <code>javax.naming.Reference</code>s
        // that specify a class name of "javax.sql.DataSource"
        if ((obj == null) || !(obj instanceof Reference)) {
            return null;
        }
        Reference ref = (Reference) obj;
        if (!"javax.sql.DataSource".equals(ref.getClassName())) {
            return null;
        }

        Properties properties = new Properties();
        for (int i = 0; i < ALL_PROPERTIES.length; i++) {
            String propertyName = ALL_PROPERTIES[i];
            RefAddr ra = ref.get(propertyName);
            if (ra != null) {
                String propertyValue = ra.getContent().toString();
                properties.setProperty(propertyName, propertyValue);
            }
        }

        return createDataSource(properties);
    }

    /**
     * Create and configures a {@link org.apache.commons.dbcp.ManagedBasicDataSource} instance based on the given properties.
     *
     * @param properties the datasource configuration properties.
     * @throws Exception if an error occurs creating the data source.
     */
    public static DataSource createDataSource(Properties properties) throws Exception {

        ManagedBasicDataSource dataSource;
        String value = null;

        value = properties.getProperty(PROP_MBEANNAME);
        dataSource = (value != null) ? new ManagedBasicDataSource(value) : new ManagedBasicDataSource();

        value = properties.getProperty(PROP_DEFAULTAUTOCOMMIT);
        if (value != null) {
            dataSource.setDefaultAutoCommit(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_DEFAULTREADONLY);
        if (value != null) {
            dataSource.setDefaultReadOnly(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_DEFAULTTRANSACTIONISOLATION);
        if (value != null) {
            int level = PoolableConnectionFactory.UNKNOWN_TRANSACTIONISOLATION;
            if ("NONE".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_NONE;
            } else if ("READ_COMMITTED".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_READ_COMMITTED;
            } else if ("READ_UNCOMMITTED".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_READ_UNCOMMITTED;
            } else if ("REPEATABLE_READ".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_REPEATABLE_READ;
            } else if ("SERIALIZABLE".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_SERIALIZABLE;
            } else {
                try {
                    level = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    System.err.println("Could not parse defaultTransactionIsolation: " + value);
                    System.err.println("WARNING: defaultTransactionIsolation not set");
                    System.err.println("using default value of database driver");
                    level = PoolableConnectionFactory.UNKNOWN_TRANSACTIONISOLATION;
                }
            }
            dataSource.setDefaultTransactionIsolation(level);
        }

        value = properties.getProperty(PROP_DEFAULTCATALOG);
        if (value != null) {
            dataSource.setDefaultCatalog(value);
        }

        value = properties.getProperty(PROP_DRIVERCLASSNAME);
        if (value != null) {
            dataSource.setDriverClassName(value);
        }

        value = properties.getProperty(PROP_MAXACTIVE);
        if (value != null) {
            dataSource.setMaxActive(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_MAXIDLE);
        if (value != null) {
            dataSource.setMaxIdle(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_MINIDLE);
        if (value != null) {
            dataSource.setMinIdle(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_INITIALSIZE);
        if (value != null) {
            dataSource.setInitialSize(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_MAXWAIT);
        if (value != null) {
            dataSource.setMaxWait(Long.parseLong(value));
        }

        value = properties.getProperty(PROP_TESTONBORROW);
        if (value != null) {
            dataSource.setTestOnBorrow(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_TESTONRETURN);
        if (value != null) {
            dataSource.setTestOnReturn(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_TIMEBETWEENEVICTIONRUNSMILLIS);
        if (value != null) {
            dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(value));
        }

        value = properties.getProperty(PROP_NUMTESTSPEREVICTIONRUN);
        if (value != null) {
            dataSource.setNumTestsPerEvictionRun(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_MINEVICTABLEIDLETIMEMILLIS);
        if (value != null) {
            dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(value));
        }

        value = properties.getProperty(PROP_TESTWHILEIDLE);
        if (value != null) {
            dataSource.setTestWhileIdle(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_PASSWORD);
        if (value != null) {
            dataSource.setPassword(value);
        }

        value = properties.getProperty(PROP_URL);
        if (value != null) {
            dataSource.setUrl(value);
        }

        value = properties.getProperty(PROP_USERNAME);
        if (value != null) {
            dataSource.setUsername(value);
        }

        value = properties.getProperty(PROP_VALIDATIONQUERY);
        if (value != null) {
            dataSource.setValidationQuery(value);
        }

        value = properties.getProperty(PROP_VALIDATIONQUERY_TIMEOUT);
        if (value != null) {
            dataSource.setValidationQueryTimeout(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED);
        if (value != null) {
            dataSource.setAccessToUnderlyingConnectionAllowed(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_REMOVEABANDONED);
        if (value != null) {
            dataSource.setRemoveAbandoned(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_REMOVEABANDONEDTIMEOUT);
        if (value != null) {
            dataSource.setRemoveAbandonedTimeout(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_LOGABANDONED);
        if (value != null) {
            dataSource.setLogAbandoned(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_POOLPREPAREDSTATEMENTS);
        if (value != null) {
            dataSource.setPoolPreparedStatements(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_MAXOPENPREPAREDSTATEMENTS);
        if (value != null) {
            dataSource.setMaxOpenPreparedStatements(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_INITCONNECTIONSQLS);
        if (value != null) {
            StringTokenizer tokenizer = new StringTokenizer(value, ";");
            dataSource.setConnectionInitSqls(Collections.list(tokenizer));
        }

        value = properties.getProperty(PROP_CONNECTIONPROPERTIES);
        if (value != null) {
            Properties p = getProperties(value);
            Enumeration e = p.propertyNames();
            while (e.hasMoreElements()) {
                String propertyName = (String) e.nextElement();
                dataSource.addConnectionProperty(propertyName, p.getProperty(propertyName));
            }
        }

        // DBCP-215
        // Trick to make sure that initialSize connections are created
        if (dataSource.getInitialSize() > 0) {
            dataSource.getLogWriter();
        }

        // Return the configured DataSource instance
        return dataSource;
    }

    /**
     * Parse properties from the string. Format of the string must be [propertyName=property;]*
     *
     * @param propText
     * @return Properties
     * @throws Exception
     */
    private static Properties getProperties(String propText) throws Exception {
        Properties p = new Properties();
        if (propText != null) {
            p.load(new ByteArrayInputStream(propText.replace(';', '\n').getBytes()));
        }
        return p;
    }
}
