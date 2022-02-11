package com.luxoft.highperformance.repositories;

import java.util.Date;
import java.util.List;

import com.luxoft.highperformance.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookDao extends JpaRepository<Book, Integer>, BookDaoCustom {

	Book findByTitle(String title);

	@Query(nativeQuery = true,
			value = "SELECT * FROM BOOK WHERE TITLE=?1")
	Book findByTitleNative(String title);

	List<Book> findByDateReleaseBetween(Date start, Date end);
	List<Book> findAllByOrderByTitleAsc();

	@Query("select b from Book b where b.dateRelease = (select max(b1.dateRelease) from Book b1) ")
	Book getLatestBook();
}
