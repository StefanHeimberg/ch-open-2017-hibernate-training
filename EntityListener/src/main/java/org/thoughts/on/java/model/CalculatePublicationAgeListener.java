package org.thoughts.on.java.model;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.PostLoad;

import org.apache.log4j.Logger;

public class CalculatePublicationAgeListener {

	@PostLoad
	public void calculateAge(Publication p) {
		Logger log = Logger.getLogger(this.getClass().getName());
		log.info("Calculate age of publication.");
		p.age = Period.between(p.publishingDate, LocalDate.now()).getYears();
	}
}
