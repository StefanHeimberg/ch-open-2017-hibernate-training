package org.thoughts.on.java.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String title;

	@Column
	private LocalDate publishingDate;

	@ManyToOne
	@JoinColumn(name="publisherid")
	private Publisher publisher;

	@ManyToMany
	@JoinTable(name = "BookAuthor", joinColumns = { @JoinColumn(name = "bookId", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "authorId", referencedColumnName = "id") })
	private Set<Author> authors = new HashSet<Author>();

	@OneToMany(mappedBy = "book")
	private Set<Review> review = new HashSet<Review>();

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Book)) {
			return false;
		}
		Book other = (Book) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Optional<LocalDate> getPublishingDate() {
		return Optional.ofNullable(this.publishingDate);
	}

	public void setPublishingDate(LocalDate publishingDate) {
		this.publishingDate = publishingDate;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (title != null && !title.trim().isEmpty())
			result += "title: " + title;
		return result;
	}

	public Optional<Publisher> getPublisher() {
		return Optional.ofNullable(this.publisher);
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
}