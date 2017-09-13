package org.thoughts.on.java.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDateAndTime {

	Logger log = Logger.getLogger(this.getClass().getName());

	private EntityManagerFactory emf;

	@Before
	public void init() {
		emf = Persistence.createEntityManagerFactory("my-persistence-unit");
	}

	@After
	public void close() {
		emf.close();
	}

	@Test
	public void testLocalDate() {
		log.info("... testLocalDate ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Book b = new Book();
		b.setTitle("My Book");
		b.setPublishingDate(LocalDate.of(2017, 1, 1));
		em.persist(b);
		
		TypedQuery<Book> q = em.createQuery("SELECT b FROM Book b WHERE publishingDate = :pubDate", Book.class);
		q.setParameter("pubDate", LocalDate.of(2017, 1, 1));
		List<Book> books = q.getResultList();
		
		books.stream()
			.map(book -> book.getTitle() + " was published on " + book.getPublishingDate())
			.forEach(m -> log.info(m));
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testLocalDateTime() {
		log.info("... testLocalDateTime ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Review r = new Review();
		r.setBook(em.find(Book.class, 100L));
		r.setComment("This is comment ...");
		r.setRating(Rating.FIVE);
		r.setPostedAt(LocalDateTime.now());
		em.persist(r);
		
		em.getTransaction().commit();
		em.close();
	}
}
