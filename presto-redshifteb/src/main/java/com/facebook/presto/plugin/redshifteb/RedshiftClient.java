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
package com.facebook.presto.plugin.redshifteb;

import com.amazon.redshift.jdbc42.Driver;

import com.facebook.presto.plugin.jdbc.BaseJdbcClient;
import com.facebook.presto.plugin.jdbc.BaseJdbcConfig;
import com.facebook.presto.plugin.jdbc.JdbcConnectorId;
import com.facebook.presto.plugin.jdbc.JdbcOutputTableHandle;
import com.facebook.presto.spi.PrestoException;

import javax.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.facebook.presto.plugin.jdbc.JdbcErrorCode.JDBC_ERROR;

public class RedshiftClient
        extends BaseJdbcClient
{
    @Inject
    public RedshiftClient(JdbcConnectorId connectorId, BaseJdbcConfig config)
            throws SQLException
    {
        super(connectorId, config, "\"", new Driver());
    }

    @Override
    public void commitCreateTable(JdbcOutputTableHandle handle)
    {
        // Redshift does not allow qualifying the target of a rename
        StringBuilder sql = new StringBuilder()
                .append("ALTER TABLE ")
                .append(quoted(handle.getCatalogName(), handle.getSchemaName(), handle.getTemporaryTableName()))
                .append(" RENAME TO ")
                .append(quoted(handle.getTableName()));

        try (Connection connection = getConnection(handle)) {
            execute(connection, sql.toString());
        }
        catch (SQLException e) {
            throw new PrestoException(JDBC_ERROR, e);
        }
    }

    @Override
    public PreparedStatement getPreparedStatement(Connection connection, String sql)
            throws SQLException
    {
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setFetchSize(1000);
        return statement;
    }
}
