package com.hers.robinet.tfe.operator;

public class Eq implements Operator {

	private String champ;
	private String value;
	
	public Eq(String champ, String value)
	{
		this.champ = champ;
		this.value = value;
	}
	
	public String getCondtion() {
		return champ +"=" +value;
	}


}
