package com.hers.robinet.tfe.descriptor;

public class IndexDescriptor {

	private String name;
	private String columnList;
	private boolean unique;
	public IndexDescriptor(javax.persistence.Index indexAnnotation) {
		name = indexAnnotation.name();
		columnList = indexAnnotation.columnList();
		unique = indexAnnotation.unique();
	}

	public String getName() {
		return name;
	}

	public String getColumnList() {
		return columnList;
	}

	public boolean isUnique() {
		return unique;
	}
}
