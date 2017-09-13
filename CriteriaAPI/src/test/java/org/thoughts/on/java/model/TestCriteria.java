package org.thoughts.on.java.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestCriteria {

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
	public void testSimpleQuery() {
		log.info("... testSimpleQuery ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> q = cb.createQuery(Book.class);
		Root<Book> book = q.from(Book.class);
		q.select(book);
		
		List<Book> books = em.createQuery(q).getResultList();
		Assert.assertEquals(6, books.size());
		for (Book b : books) {
			log.info(b);
		}
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testWhere() {
		log.info("... testWhere ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		Root<Book> book = cq.from(Book.class);
		cq.select(book);
		
		ParameterExpression<Long> bookIdParam = cb.parameter(Long.class, "bookId");
		cq.where(cb.equal(book.get("id"), bookIdParam));
		
		TypedQuery<Book> q = em.createQuery(cq);
		q.setParameter(bookIdParam, 1L);
		Book b = q.getSingleResult();
		
		Assert.assertEquals(new Long(1), b.getId());
		log.info(b);
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testOrderBy() {
		log.info("... testOrderBy ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		Root<Book> book = cq.from(Book.class);	
		cq.select(book);
		
		cq.orderBy(cb.asc(book.get("title")));
		
		List<Book> books = em.createQuery(cq).getResultList();
		Assert.assertEquals(6, books.size());
		for (Book b : books) {
			log.info(b);
		}
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testJoin() {
		log.info("... testJoin ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		
		Root<Book> book = cq.from(Book.class);
		@SuppressWarnings("unused")
		Join<Object, Object> authors = book.join("authors");
		
		cq.select(book);
		
		ParameterExpression<Long> bookIdParam = cb.parameter(Long.class, "bookId");
		cq.where(cb.equal(book.get("id"), bookIdParam));
		
		TypedQuery<Book> q = em.createQuery(cq);
		q.setParameter(bookIdParam, 1L);
		Book b = q.getSingleResult();
		
		Assert.assertEquals(new Long(1), b.getId());
		log.info(b);
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testSelectAttribute() {
		log.info("... testSelectAttribute ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Book> book = cq.from(Book.class);
		
		cq.select(book.get("title"));

		ParameterExpression<Long> bookIdParam = cb.parameter(Long.class, "bookId");
		cq.where(cb.equal(book.get("id"), bookIdParam));
		
		TypedQuery<String> q = em.createQuery(cq);
		q.setParameter(bookIdParam, 1L);
		String title = q.getSingleResult();
		
		Assert.assertEquals("Effective Java", title);
		log.info(title);
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testSelectMutlipleAttribute() {
		log.info("... testSelectMutlipleAttribute ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Book> book = cq.from(Book.class);
		Join<Object, Object> authors = book.join("authors");
		
		Selection<Object> firstName = authors.get("firstName").alias("firstName");
		Path<Object> lastName = authors.get("lastName");
		Path<Object> title = book.get("title");
		cq.multiselect(firstName, lastName, title);
		
		ParameterExpression<Long> bookIdParam = cb.parameter(Long.class, "bookId");
		cq.where(cb.equal(book.get("id"), bookIdParam));
		
		TypedQuery<Tuple> q = em.createQuery(cq);
		q.setParameter(bookIdParam, 1L);	
		Tuple tuple = q.getSingleResult();
		
		Assert.assertEquals("Effective Java", tuple.get(title));
		log.info(tuple.get("firstName"));
		log.info(tuple.get(lastName));
		log.info(tuple.get(2));
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testSelectPojo() {
		log.info("... testSelectPojo ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AuthorBookValue> cq = cb.createQuery(AuthorBookValue.class);
		Root<Book> book = cq.from(Book.class);
		Join<Object, Object> authors = book.join("authors");
		
		cq.select(cb.construct(AuthorBookValue.class, authors.get("firstName"), authors.get("lastName"), book.get("title")));

		ParameterExpression<Long> bookIdParam = cb.parameter(Long.class, "bookId");
		cq.where(cb.equal(book.get("id"), bookIdParam));
		
		TypedQuery<AuthorBookValue> q = em.createQuery(cq);
		q.setParameter(bookIdParam, 1L);
		
		AuthorBookValue ab = q.getSingleResult();
		Assert.assertEquals("Effective Java", ab.getTitle());
		log.info(ab);
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testSelectGroupBy() {
		log.info("... testSelectGroupBy ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<Book> book = cq.from(Book.class);
		Join<Object, Object> authors = book.join("authors");
		
		Expression<Long> authorCount = cb.count(authors);
		cq.multiselect(book, authorCount);
		
		ParameterExpression<Long> bookIdParam = cb.parameter(Long.class, "bookId");
		cq.where(cb.equal(book.get("id"), bookIdParam));
		
		cq.groupBy(book.get("id"));
		
		TypedQuery<Tuple> q = em.createQuery(cq);
		q.setParameter(bookIdParam, 2L);
		Tuple tuple = q.getSingleResult();
		
		Assert.assertEquals(new Long(2), ((Book)tuple.get(0)).getId());
		log.info(((Book)tuple.get(0)).getTitle() + " was written by " + tuple.get(authorCount) + " authors.");
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testSubQuery() {
		log.info("... testSubQuery ...");

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		
		Root<Book> book = cq.from(Book.class);
			
		cq.select(book);
	
		Subquery<Long> sq = cq.subquery(Long.class);
		Root<Author> author = sq.from(Author.class);
		Join<Object, Object> authorBooks = author.join("books");
		sq.select(authorBooks.get("id"));
		ParameterExpression<Long> authorIdParam = cb.parameter(Long.class, "authorId");
		sq.where(cb.equal(author.get("id"), authorIdParam));
		
		cq.where(cb.in(book.get("id")).value(sq));
		
		TypedQuery<Book> q = em.createQuery(cq);
		q.setParameter(authorIdParam, 1L);
		Book b = q.getSingleResult();
		
		Assert.assertEquals(new Long(1), b.getId());
		log.info(b);
		
		em.getTransaction().commit();
		em.close();
	}
}
