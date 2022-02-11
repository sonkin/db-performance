package com.luxoft.highperformance.controllers;

import com.luxoft.highperformance.model.Book;
import com.luxoft.highperformance.repositories.BookDao;
import com.luxoft.highperformance.repositories.BookDaoJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class BookController {

    @Autowired
    BookDao bookDao;

    @Autowired
    BookDaoJDBC bookDaoJDBC;

    AtomicLong counterJPA = new AtomicLong();
    AtomicLong counterJPANative = new AtomicLong();
    AtomicLong counterJDBC = new AtomicLong();

    @PostConstruct
    public void init() {
        for (int i=0; i<10_000; i++) {
            Book book = new Book("The Book "+i);
            bookDao.save(book);
        }
    }

    @GetMapping("/book-jpa")
    public Book getBookByTitle(String title) {
        long start = System.nanoTime();
        Book book = bookDao.findByTitle(title);
        long time = System.nanoTime()-start;
        counterJPA.addAndGet(time/1000_000);
        return book;
    }

    @GetMapping("/book-jpa-native")
    public Book getBookByTitleNative(String title) {
        long start = System.nanoTime();
        Book book = bookDao.findByTitleNative(title);
        long time = System.nanoTime()-start;
        counterJPANative.addAndGet(time/1000_000);
        return book;
    }

    @GetMapping("/book-jdbc")
    public Book getBookByTitleJDBC(String title) throws SQLException {
        long start = System.nanoTime();
        Book book = bookDaoJDBC.findByTitle(title);
        long time = System.nanoTime()-start;
        counterJDBC.addAndGet(time/1000_000);
        return book;
    }

    @GetMapping("clear")
    public String clearStats() {
        counterJPA.set(0);
        counterJPANative.set(0);
        counterJDBC.set(0);
        return "Stats is reset!";
    }

    @GetMapping("stats")
    public String stats() {
        return counterJPA.get()+"\n"+
                counterJPANative.get()+"\n"+
                counterJDBC.get()+"\n\n";
    }

}
