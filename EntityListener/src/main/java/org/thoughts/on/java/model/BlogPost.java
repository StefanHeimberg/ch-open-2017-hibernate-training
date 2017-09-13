package org.thoughts.on.java.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ExcludeSuperclassListeners;

@Entity(name = "BlogPost")
@DiscriminatorValue("Blog")
@EntityListeners( CalculatePublicationAgeInDaysListener.class )
@ExcludeSuperclassListeners
public class BlogPost extends Publication {
	
	@Column
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
