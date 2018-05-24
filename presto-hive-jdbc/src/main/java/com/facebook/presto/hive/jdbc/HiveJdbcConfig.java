/*
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
package com.facebook.presto.hive.jdbc;

import com.facebook.presto.plugin.jdbc.BaseJdbcConfig;
import io.airlift.configuration.Config;

public class HiveJdbcConfig
        extends BaseJdbcConfig
{
    private String connectionUrl;
    private boolean kerberosEnabled;
    private String kerberosKeytab;
    private String kerberosPrincipal;

    @Override
    @Config("connection-url")
    public BaseJdbcConfig setConnectionUrl(String connectionUrl)
    {
        this.connectionUrl = connectionUrl;
        return this;
    }

    @Config("hive-jdbc.kerberos-enabled")
    public BaseJdbcConfig getKerberosEnabled(boolean kerberosEnabled)
    {
        this.kerberosEnabled = kerberosEnabled;
        return this;
    }

    @Config("hive-jdbc.kerberos-keytab")
    public BaseJdbcConfig getKerberosKeytab(String kerberosKeytab)
    {
        if (kerberosEnabled) {
            this.kerberosKeytab = kerberosKeytab;
        }

        return this;
    }

    @Config("hive-jdbc.kerberos-principal")
    public BaseJdbcConfig getKerberosPrincipal(String kerberosPrincipal)
    {
        if (kerberosEnabled) {
            this.kerberosPrincipal = ";principal=" + kerberosPrincipal;
        }

        return this;
    }
}
