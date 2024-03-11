package sk.tuke.gamestudio.configuration.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.postgresql.ds.PGConnectionPoolDataSource;
import sk.tuke.gamestudio.configuration.DataSourceConfiguration;

import javax.naming.InitialContext;
import javax.sql.ConnectionPoolDataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostgresDataSourceConfiguration implements DataSourceConfiguration {

    ConnectionPoolDataSource postgresDataSource;

    @Override
    public ConnectionPoolDataSource getDataSource() {
        return this.postgresDataSource;
    }

    @Override
    public ConnectionPoolDataSource configureNewFromResource(@NonNull String name) {
        var url = ClassLoader.getSystemResource(name);
        if (url == null) {
            return null;
        }

        var postgresDataSource = new PGConnectionPoolDataSource();
        try (var reader = new InputStreamReader(url.openStream())) {
            var properties = new Properties();
            properties.load(reader);

            postgresDataSource.setURL(properties.getProperty("postgres.url"));
            postgresDataSource.setUser(properties.getProperty("postgres.password"));
            postgresDataSource.setPassword(properties.getProperty("postgres.password"));
        } catch (IOException ignore) {
            ignore.printStackTrace();
            return null;
        }

        this.postgresDataSource = postgresDataSource;
        return postgresDataSource;
    }
}
