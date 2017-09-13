package org.thoughts.on.java.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestUserType {

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
	public void testJsonType() {
		log.info("... testJsonType ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Book b = new Book();
		MyJson json = new MyJson();
		json.setLongProp(12345L);
		json.setStringProp("My book description");
		b.setBookDescription(json);
		em.persist(b);
		
		em.getTransaction().commit();
		em.close();
		
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Book b2 = (Book) em.createNativeQuery("SELECT * FROM Book b WHERE b.bookdescription->'longProp' = '12345'", Book.class).getSingleResult();
		Assert.assertEquals(b.getId(), b2.getId());
		Assert.assertEquals(b.getBookDescription(), b2.getBookDescription());
		
		em.getTransaction().commit();
		em.close();
	}
}
