package com.hers.robinet.tfe.jpaHelper;

public class JpaIndex {

	private String name;
	private String columnList;
	
	public JpaIndex(javax.persistence.Index indexAnnotation) {
		name = indexAnnotation.name();
		columnList = indexAnnotation.columnList();
	}

	public String getName() {
		return name;
	}

	public String getColumnList() {
		return columnList;
	}
}
