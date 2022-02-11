package com.luxoft.highperformance;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootApplication
public class Application {

    @Bean
    public PreparedStatement getStatement(DataSource dataSource) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        return connection
                .prepareStatement("SELECT * FROM BOOK WHERE TITLE=?");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
