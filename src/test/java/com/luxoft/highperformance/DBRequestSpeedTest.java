package com.luxoft.highperformance;

import com.luxoft.highperformance.model.Book;
import com.luxoft.highperformance.repositories.BookDao;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
//@ActiveProfiles("h2-mem")
//@ActiveProfiles("h2-embedded")
//@ActiveProfiles("h2-server")
//@ActiveProfiles("hsqldb")
@ActiveProfiles("postgres")
//@ActiveProfiles("postgres-docker")
@Rollback(false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DBRequestSpeedTest {
    final int BOOKS_AMOUNT = 10_000;
    @Autowired
    BookDao bookDao;

    @Autowired
    DataSource dataSource;

    static int id = 1;

    @BeforeAll
    public static void clearDB(@Autowired BookDao bookDao) {
        bookDao.deleteAll();
    }

    @Order(1)
    @RepeatedTest(3)
    @Rollback
    public void addBooksWarmup() {
        for (int i=0; i<BOOKS_AMOUNT; i++) {
            Book book = new Book("The Book "+id);
            bookDao.save(book);
            id++;
        }
    }

    @Order(11)
    @RepeatedTest(3)
    public void addBooks() {
        long start = System.nanoTime();
        for (int i=0; i<BOOKS_AMOUNT; i++) {
            Book book = new Book("The Book "+id);
            bookDao.save(book);
            id++;
        }
        long end = System.nanoTime();
        System.out.println("addBooks time: "+((end-start)/1000_000));
    }

    @Transactional
    @Order(5)
    @RepeatedTest(3)
    public void addBooksTransactional() {
        long start = System.nanoTime();
        for (int i=0; i<BOOKS_AMOUNT; i++) {
            Book book = new Book("The Book "+id);
            bookDao.save(book);
            id++;
        }
        long end = System.nanoTime();
        System.out.println("addBooksTransactional time: "+((end-start)/1000_000));
    }

    @Order(20)
    @RepeatedTest(3)
    public void addBooksJDBC() throws ClassNotFoundException, SQLException {
        long start = System.nanoTime();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection
                .prepareStatement("INSERT INTO BOOK(ID, TITLE) VALUES(?,?)");) {

            for (int i = 1; i < BOOKS_AMOUNT; i++) {
                //Book book = new Book("The Book " + i);
                statement.setInt(1, BOOKS_AMOUNT*10+id);
                statement.setString(2, "The Book " + id);
                statement.executeUpdate();
                id++;
            }
        }
        long end = System.nanoTime();
        System.out.println("addBooksJDBC time: "+((end-start)/1000_000));
    }

    @RepeatedTest(3)
    @Order(30)
    public void testQueryJPA() {
        long start = System.nanoTime();
        for (int i=1;i<BOOKS_AMOUNT; i++) {
            Book b = bookDao.findByTitle("The Book "+i);
        }
        long end = System.nanoTime();
        System.out.println("testQueryJPA time: "+((end-start)/1000_000));
    }

    @RepeatedTest(3)
    @Order(30)
    public void testQueryJPAWarmup() {
        long start = System.nanoTime();
        for (int i=1;i<BOOKS_AMOUNT; i++) {
            Book b = bookDao.findByTitle("The Book "+i);
        }
        long end = System.nanoTime();
        System.out.println("testQueryJPA time: "+((end-start)/1000_000));
    }

    @RepeatedTest(3)
    @Order(40)
    public void testQueryNativeWarmup() {
        long start = System.nanoTime();
        for (int i=1;i<BOOKS_AMOUNT; i++) {
            Book b = bookDao.findByTitleNative("The Book "+i);
        }
        long end = System.nanoTime();
        System.out.println("testQueryNative time: "+((end-start)/1000_000));
    }

    @RepeatedTest(3)
    @Order(45)
    public void testQueryNative() {
        long start = System.nanoTime();
        for (int i=1;i<BOOKS_AMOUNT; i++) {
            Book b = bookDao.findByTitleNative("The Book "+i);
        }
        long end = System.nanoTime();
        System.out.println("testQueryNative time: "+((end-start)/1000_000));
    }

    @RepeatedTest(3)
    @Order(46)
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public void testQueryNativeTransactional() {
        long start = System.nanoTime();
        for (int i=1;i<BOOKS_AMOUNT; i++) {
            Book b = bookDao.findByTitleNative("The Book "+i);
        }
        long end = System.nanoTime();
        System.out.println("testQueryNative time: "+((end-start)/1000_000));
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Order(50)
    @RepeatedTest(3)
    public void testQuerySpringJDBC() throws ClassNotFoundException, SQLException {
        List<Integer> ids = new ArrayList<>();
        for (int i = 1; i < BOOKS_AMOUNT; i++) {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                    "SELECT * FROM BOOK WHERE TITLE=?", "The Book " + i);
            rowSet.next();
            int id = rowSet.getInt("id");
            ids.add(id);
        }
        System.out.println(ids.size());
    }

    @Order(60)
    @RepeatedTest(3)
    public void testQueryJDBC() throws ClassNotFoundException, SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM BOOK WHERE TITLE=?");) {

            long start = System.nanoTime();

            List<Integer> ids = new ArrayList<>();
            for (int i = 1; i < BOOKS_AMOUNT; i++) {
                statement.setString(1, "The Book "+i);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                int id = resultSet.getInt("ID");
                ids.add(id);
            }

            System.out.println(ids.size());
            long end = System.nanoTime();
            System.out.println("testQueryJDBC time: "+((end-start)/1000_000));
        }

    }

    @Test
    @Order(100)
    public void printBooksAmount() {
        System.out.println("********** Books amount: "+ bookDao.findAll().size());
    }

}
