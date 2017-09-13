package org.thoughts.on.java.model;

import javax.persistence.Embeddable;

@Embeddable
public class ComplexRating {

	private Rating summary;

	public Rating getSummary() {
		return summary;
	}

	public void setSummary(Rating summary) {
		this.summary = summary;
	}
}
