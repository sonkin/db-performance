package com.luxoft.highperformance.repositories;

import com.luxoft.highperformance.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookDaoJDBC {

    @Autowired
    DataSource dataSource;

    @Autowired
    PreparedStatement statement;

    public static AtomicLong sum = new AtomicLong();

    //@Transactional(readOnly = true)
    public Book findByTitle(String title) throws SQLException {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection
            .prepareStatement("SELECT * FROM BOOK WHERE TITLE=?");) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt("ID");
            return new Book(id, title);
        }
    }
}
