package org.thoughts.on.java.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFunctions {

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
    public void callCustomFunctionNative() {
    	log.info("... callCustomFunctionNative ...");
    	EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Object r = em.createNativeQuery("SELECT calculate(a.id, 1) FROM Author a "
        		+ "WHERE a.id = 1").getSingleResult();
        log.info("Calculated result: "+r);
        
        em.getTransaction().commit();
        em.close();
    } 
    
    @Test
    public void callFunctionJPQLSelect() {
    	log.info("... callFunctionJPQLSelect ...");
    	EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Long n = em.createQuery("SELECT COUNT(a) FROM Author a", Long.class).getSingleResult();
        log.info("Number of authors: "+n);
        
        em.getTransaction().commit();
        em.close();
    }  
    
    @Test
    public void callCustomFunctionJPQLSelect() {
    	log.info("... callCustomFunctionJPQLSelect ...");
    	EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Object r = em.createQuery("SELECT function('calculate', a.id, 1) "
        		+ "FROM Author a WHERE a.id = 1").getSingleResult();
        log.info("Calculated result: "+r);
        
        em.getTransaction().commit();
        em.close();
    }
    
    @Test
    public void callCustomFunctionJPQLWhere() {
    	log.info("... callCustomFunctionJPQLWhere ...");
    	EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Author a = em.createQuery("SELECT a FROM Author a WHERE a.id = function('calculate', 1, 1)", Author.class).getSingleResult();
        log.info("Author with calculated id ["+a.getId()+"] "+a);
        
        em.getTransaction().commit();
        em.close();
    }
    
    @Test
    public void callCustomFunctionHibernateSelect() {
    	log.info("... callCustomFunctionHibernateSelect ...");
    	EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Object r = em.createQuery("SELECT calculate(a.id, 1) "
        		+ "FROM Author a WHERE a.id = 1").getSingleResult();
        log.info("Calculated result: "+r);
        
        em.getTransaction().commit();
        em.close();
    }

}
