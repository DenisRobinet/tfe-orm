package com.hers.robinet.tfe.dialect;

import java.util.ArrayList;

public class Fk {
	ArrayList<Column> cols;
	ArrayList<Column> ref;
	Table table;
	public Fk(ArrayList<Column> cols, ArrayList<Column> ref, Table table) {
		super();
		this.cols = cols;
		this.ref = ref;
		this.table = table;
	}
	public ArrayList<Column> getCols() {
		return cols;
	}
	public ArrayList<Column> getRef() {
		return ref;
	}
	public Table getTable() {
		return table;
	}
	
	
}
