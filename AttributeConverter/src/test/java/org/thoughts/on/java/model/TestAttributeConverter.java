package org.thoughts.on.java.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAttributeConverter {

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
	public void testRatingConverter() {
		log.info("... testRatingConverterWrite ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Review r = new Review();
		r.setBook(em.find(Book.class, 1L));
		r.setComment("My rating");
		r.setRating(Rating.ONE);
		em.persist(r);
		
		em.getTransaction().commit();
		em.close();
		
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Review> q = em.createQuery("SELECT r FROM Review r WHERE r.rating = :rating", Review.class);
		q.setParameter("rating", Rating.ONE);
		r = q.getSingleResult();
		Assert.assertEquals(Rating.ONE, r.getRating());
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testEmbeddable() {
		log.info("... testEmbeddable ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Review r = new Review();
		r.setBook(em.find(Book.class, 1L));
		r.setComment("My rating");

		ComplexRating complexRating = new ComplexRating();
		complexRating.setSummary(Rating.ONE);
		r.setComplexRating(complexRating);
		em.persist(r);
		
		em.getTransaction().commit();
		em.close();
		
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Review> q = em.createQuery("SELECT r FROM Review r WHERE r.complexRating.summary = :summary", Review.class);
		q.setParameter("summary", Rating.ONE);
		Review r2 = q.getSingleResult();
		Assert.assertEquals(Rating.ONE, r2.getComplexRating().getSummary());
		Assert.assertEquals(r.getId(), r2.getId());
		
		em.getTransaction().commit();
		em.close();
	}
}
