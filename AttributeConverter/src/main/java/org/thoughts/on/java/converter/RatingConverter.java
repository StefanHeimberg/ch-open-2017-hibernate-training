package org.thoughts.on.java.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.log4j.Logger;
import org.thoughts.on.java.model.Rating;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, Integer> {
	
	Logger log = Logger.getLogger(this.getClass().getName());
	
    @Override
    public Integer convertToDatabaseColumn(Rating r) {
    	if (r == null) {
    		return null;
    	}
    	
    	Integer dbValue;
    	switch (r) {
    	case ONE:
    		dbValue = 1;
    		break;
    		
    	case TWO:
    		dbValue = 2;
    		break;
    		
    	case THREE:
    		dbValue = 3;
    		break;
    		
    	case FOUR:
    		dbValue = 4;
    		break;
    		
    	case FIVE:
    		dbValue = 5;
    		break;
    		
    	default:
    		throw new IllegalArgumentException("Rating [" + r.toString() + "] not supported.");
    	}
    	
    	log.info("Convert rating ["+r+"] with ordinal ["+r.ordinal()+"] to ["+dbValue+"]");
    	return dbValue;
    }

    @Override
    public Rating convertToEntityAttribute(Integer i) {
    	if (i == null) {
    		return null;
    	}
    	
    	Rating r;
    	switch (i) {
    	case 1:
    		r = Rating.ONE;
    		break;
    		
    	case 2:
    		r = Rating.ONE;
    		break;
    		
    	case 3:
    		r = Rating.ONE;
    		break;
    		
    	case 4:
    		r = Rating.ONE;
    		break;
    		
    	case 5:
    		r = Rating.ONE;
    		break;
    		
    	default:
    		throw new IllegalArgumentException("Rating [" + i + "] not supported.");
    	}
    	
    	log.info("Convert ["+i+"] to rating ["+r+"] with ordinal ["+r.ordinal()+"]");
    	return r;
    }
}