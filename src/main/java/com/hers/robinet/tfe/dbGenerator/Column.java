package com.hers.robinet.tfe.dbGenerator;


@SuppressWarnings("rawtypes")
public class Column {
	private String name;
	private Class type;
	private boolean autoIncrement;
	
	public Column(String name, Class type, boolean autoIncrement) {
		super();
		this.name = name;
		this.type = type;
		this.autoIncrement = autoIncrement;
	}

	public String getName() {
		return name;
	}

	public Class getType() {
		return type;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	
	
	
	

}
