package com.luxoft.highperformance.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(indexes={
	@Index(name = "title_idx", columnList = "TITLE")
})
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String title;
	private Date dateRelease;
	private String publishingHouse;

	public Book() {
	}

	public Book(int id, String title) {
		this.id = id;
		this.title = title;
	}


	public Book(String title) {
		this.title = title;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="DATE_RELEASE")
	public Date getDateRelease() {
		return dateRelease;
	}
	
	public void setDateRelease(Date dateRelease) {
		this.dateRelease = dateRelease;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Book book = (Book) o;

		if (dateRelease != null ? !dateRelease.equals(book.dateRelease)
				: book.dateRelease != null)
			return false;
		if (title != null ? !title.equals(book.title) : book.title != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (dateRelease != null ? dateRelease.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", dateRelease=" + dateRelease + ", publishingHouse="
				+ publishingHouse + "]";
	}

	public String getPublishingHouse() {
		return publishingHouse;
	}

	public void setPublishingHouse(String publishingHouse) {
		this.publishingHouse = publishingHouse;
	}
}
