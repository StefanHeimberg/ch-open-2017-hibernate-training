package org.thoughts.on.java.model;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestOptional {

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
	public void testOptionalAttribute() {
		log.info("... testOptionalAttribute ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Book b = em.find(Book.class, 1L);
		Assert.assertTrue(b.getPublishingDate().isPresent());
		log.info(b.getTitle() + " was published on "+b.getPublishingDate().get());
		
		b = em.find(Book.class, 2L);
		Assert.assertFalse(b.getPublishingDate().isPresent());
		log.info(b.getTitle() + " hasn't been published yet.");
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testOptionalRelationship() {
		log.info("... testOptionalRelationship ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Book b = em.find(Book.class, 1L);
		Assert.assertTrue(b.getPublisher().isPresent());
		log.info(b.getTitle() + " was published by "+b.getPublisher().get());
		
		b = em.find(Book.class, 2L);
		Assert.assertFalse(b.getPublisher().isPresent());
		log.info(b.getTitle() + " has no publisher.");
	
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testOptionalEntity() {
		log.info("... testOptionalEntity ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Session session = em.unwrap(Session.class);
		
		Optional<Book> b = session.byId(Book.class).loadOptional(1L);
		Assert.assertTrue(b.isPresent());
		log.info(b.get().getTitle());
		
		b = session.byId(Book.class).loadOptional(100L);
		Assert.assertFalse(b.isPresent());
		log.info("Book doesn't exist.");
		
		em.getTransaction().commit();
		em.close();
	}
}
