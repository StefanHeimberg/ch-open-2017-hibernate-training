package org.thoughts.on.java.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetamodel {

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
	public void testMetamodel() {
		log.info("... testMetamodel ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		Root<Book> book = cq.from(Book.class);
		cq.select(book);
		
		ParameterExpression<String> bookTitleParam = cb.parameter(String.class, "title");
		cq.where(cb.equal(book.get(Book_.title), bookTitleParam));
		
		TypedQuery<Book> q = em.createQuery(cq);
		q.setParameter(bookTitleParam, "Effective Java");
		Book b = q.getSingleResult();
		
		Assert.assertEquals(new Long(1), b.getId());
		log.info(b);
		
		em.getTransaction().commit();
		em.close();
	}
}
