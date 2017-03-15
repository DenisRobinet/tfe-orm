package com.hers.robinet.tfe.descriptor;

import java.lang.reflect.Field;

public class TypeDescriptor {

	private Field attribute;
	private String name;
	private Class<?> type;
	
	public TypeDescriptor(Field attribute, String name, Class<?> type) {
		super();
		attribute.setAccessible(true);
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
