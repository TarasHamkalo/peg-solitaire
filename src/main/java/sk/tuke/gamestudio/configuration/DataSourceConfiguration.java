package sk.tuke.gamestudio.configuration;

import lombok.NonNull;

import javax.sql.ConnectionPoolDataSource;

public interface DataSourceConfiguration {
    ConnectionPoolDataSource getDataSource();

    ConnectionPoolDataSource configureNewFromResource(@NonNull String name);
}
