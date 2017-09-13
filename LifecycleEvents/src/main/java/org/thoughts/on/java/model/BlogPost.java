package org.thoughts.on.java.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PostLoad;

import org.apache.log4j.Logger;

@Entity(name = "BlogPost")
@DiscriminatorValue("Blog")
public class BlogPost extends Publication {
	
	@Column
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@PostLoad
	public void calculateAge() {
		Logger log = Logger.getLogger(this.getClass().getName());
		log.info("Calculate age of blog post.");
		this.age = ChronoUnit.DAYS.between(publishingDate, LocalDate.now());
	}
}
