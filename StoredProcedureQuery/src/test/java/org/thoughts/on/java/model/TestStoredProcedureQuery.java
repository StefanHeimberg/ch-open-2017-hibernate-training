package org.thoughts.on.java.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStoredProcedureQuery {

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
	public void calculate() {
		log.info("... calculate ...");
		EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
		// define the stored procedure
		StoredProcedureQuery query = em.createStoredProcedureQuery("calculate");
		query.registerStoredProcedureParameter("x", Double.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("y", Double.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("sum", Double.class, ParameterMode.OUT);
		
		// set input parameter
		query.setParameter("x", 1.23d);
		query.setParameter("y", 4d);
		
		// call the stored procedure and get the result
		query.execute();
		Double sum = (Double) query.getOutputParameterValue("sum");
		log.info(sum);

        em.getTransaction().commit();
        em.close();
	}
	
	@Test
	public void getBooks() {
		EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
		StoredProcedureQuery query = em.createStoredProcedureQuery("get_books", Book.class);
		query.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);

		List<Book> books = query.getResultList();
		for (Book b : books) {
			log.info(b.getTitle());
		}

        em.getTransaction().commit();
        em.close();
	}
	
	@Test
	public void getReviews() {
		EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
		StoredProcedureQuery bq = em.createStoredProcedureQuery("get_books", Book.class);
		bq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
		
		List<Book> books = bq.getResultList();
		for (Book b : books) {
			log.info(b.getTitle());
			
			StoredProcedureQuery rq = em.createStoredProcedureQuery("get_reviews", Review.class);
			rq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
			rq.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
			
			rq.setParameter(2, b.getId());
			List<Review> reviews = rq.getResultList();
			for (Review r : reviews) {
				log.info(r);
			}
		}

        em.getTransaction().commit();
        em.close();
	}
}
