package org.thoughts.on.java.model;

import java.io.Serializable;
import java.util.Objects;

public class MyJson implements Serializable {

	private static final long serialVersionUID = 1L;

	private String stringProp;

	private Long longProp;

	public String getStringProp() {
		return stringProp;
	}

	public void setStringProp(String stringProp) {
		this.stringProp = stringProp;
	}

	public Long getLongProp() {
		return longProp;
	}

	public void setLongProp(Long longProp) {
		this.longProp = longProp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((longProp == null) ? 0 : longProp.hashCode());
		result = prime * result
				+ ((stringProp == null) ? 0 : stringProp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyJson other = (MyJson) obj;
		return Objects.equals(getStringProp(), other.getStringProp())
				&& Objects.equals(getLongProp(), other.getLongProp());
	}

	@Override
	public String toString() {
		return "MyJson [stringProp=" + stringProp + ", longProp=" + longProp
				+ "]";
	}
}
