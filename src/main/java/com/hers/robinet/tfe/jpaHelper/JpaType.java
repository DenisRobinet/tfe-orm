package com.hers.robinet.tfe.jpaHelper;

import java.lang.reflect.Field;

public class JpaType {

	private Field attribute;
	private String name;
	private Class<?> type;
	
	public JpaType(Field attribute, String name, Class<?> type) {
		super();
		this.attribute = attribute;
		this.name = name;
		this.type = type;
	}
	public Field getAttribute() {
		return attribute;
	}
	public String getName() {
		return name;
	}
	public Class<?> getType() {
		return type;
	}

	
}
