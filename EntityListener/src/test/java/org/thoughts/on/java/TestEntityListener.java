package org.thoughts.on.java;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.thoughts.on.java.model.BlogPost;

public class TestEntityListener {

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
	public void testPostLoadCallback() {
		log.info("... testPostLoadCallback ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		BlogPost post = em.find(BlogPost.class, 3L);
		Assert.assertNotNull(post.getAge());
		log.info("The post [" + post.getTitle() + "] was publised on ["
				+ post.getPublishingDate() + "] and is [" + post.getAge()
				+ "] days old.");

		em.getTransaction().commit();
		em.close();
	}
}
