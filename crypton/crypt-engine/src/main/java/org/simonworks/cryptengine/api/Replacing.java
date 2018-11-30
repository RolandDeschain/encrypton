package org.simonworks.cryptengine.api;

import org.apache.commons.lang3.StringUtils;

public class Replacing {
	
	private String fieldId = StringUtils.EMPTY;
	private final String oldValue;
	private String newValue = StringUtils.EMPTY;
	
	private final int start;
	private final int end;
	
	public Replacing(String line, int start, int end) {
		this.start = start;
		this.end = end;
		this.oldValue = line.substring(start, end).trim();
	}
	
	public int length() {
		return ( start - end );
	}
	
	public String getFieldId() {
		return fieldId;
	}
	
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	
	public String getOldValue() {
		return oldValue;
	}
	
	public String getNewValue() {
		return newValue;
	}
	
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Replacing [fieldId=").append(fieldId).append(", oldValue=").append(oldValue)
				.append(", newValue=").append(newValue).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldId == null) ? 0 : fieldId.hashCode());
		result = prime * result + ((oldValue == null) ? 0 : oldValue.hashCode());
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
		Replacing other = (Replacing) obj;
		if (fieldId == null) {
			if (other.fieldId != null)
				return false;
		} else if (!fieldId.equals(other.fieldId))
			return false;
		if (oldValue == null) {
			if (other.oldValue != null)
				return false;
		} else if (!oldValue.equals(other.oldValue))
			return false;
		return true;
	}

}
