package org.thoughts.on.java.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.PostLoad;

import org.apache.log4j.Logger;

public class CalculatePublicationAgeInDaysListener {

	@PostLoad
	public void calculateAge(Publication p) {
		Logger log = Logger.getLogger(this.getClass().getName());
		log.info("Calculate age of publication in days.");
		p.age = ChronoUnit.DAYS.between(p.publishingDate, LocalDate.now());
	}
}
