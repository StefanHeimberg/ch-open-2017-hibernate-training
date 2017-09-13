package org.thoughts.on.java.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.thoughts.on.java.converter.RatingConverter;

@Entity
public class Review implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Version
	@Column
	private int version;

	@Column
	@Convert(converter = RatingConverter.class, disableConversion = true)
	private Rating rating;

	@Embedded
	@Convert(attributeName = "complexRating.summary", converter = RatingConverter.class)
	private ComplexRating complexRating;
	
	@Column
	private String comment;
	
	@Column
	private LocalDateTime postedAt;
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

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
		if (!(obj instanceof Review)) {
			return false;
		}
		Review other = (Review) obj;
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

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	public LocalDateTime getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(LocalDateTime postedAt) {
		this.postedAt = postedAt;
	}
	
	public ComplexRating getComplexRating() {
		return complexRating;
	}

	public void setComplexRating(ComplexRating complexRating) {
		this.complexRating = complexRating;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (comment != null && !comment.trim().isEmpty())
			result += "comment: " + comment;
		return result;
	}
}