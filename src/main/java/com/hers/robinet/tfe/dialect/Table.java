package com.hers.robinet.tfe.dialect;

import java.util.ArrayList;

public class Table {

	private String name;
	private ArrayList<Column> cols;
	private ArrayList<Column> ids;
	private ArrayList<Fk> fks;
	
	public Table(String name, ArrayList<Column> cols, ArrayList<Column> ids, ArrayList<Fk> fks) {
		super();
		this.name = name;
		this.cols = cols;
		this.ids = ids;
		this.fks = fks;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Column> getCols() {
		return cols;
	}

	public ArrayList<Column> getIds() {
		return ids;
	}

	public ArrayList<Fk> getFks() {
		return fks;
	}
	
	
	
}
