package sk.tuke.gamestudio.configuration.impl;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.h2.jdbcx.JdbcDataSource;
import sk.tuke.gamestudio.configuration.DataSourceConfiguration;

import javax.sql.ConnectionPoolDataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class H2DataSourceConfiguration implements DataSourceConfiguration {

    ConnectionPoolDataSource h2DataSource;

    @Override
    public ConnectionPoolDataSource getDataSource() {
        return h2DataSource;
    }

    @Override
    public ConnectionPoolDataSource configureNewFromResource(@NonNull String name) {
        var url = ClassLoader.getSystemResource(name);
        if (url == null) {
            return null;
        }

        var h2DataSource = new JdbcDataSource();
        try (var reader = new InputStreamReader(url.openStream())) {
            var properties = new Properties();
            properties.load(reader);

            h2DataSource.setURL(properties.getProperty("h2.url"));
            h2DataSource.setUser(properties.getProperty("h2.user"));
            h2DataSource.setPassword(properties.getProperty("h2.password"));
        } catch (IOException ignore) {
            ignore.printStackTrace();
            return null;
        }

        this.h2DataSource = h2DataSource;
        return h2DataSource;
    }
}
