package com.hers.robinet.tfe.operator;

import com.hers.robinet.tfe.dialect.Dialect;

public class Eq implements Operator {

	private String champ;
	private String value;
	
	public Eq(String champ, String value)
	{
		this.champ = champ;
		this.value = value;
	}
	
	public Eq(String champ)
	{
		this.champ = champ;
		this.value = null;
	}
	
	public void getCondtion(Dialect dialect, StringBuilder sb) {
		sb.append(champ);
		sb.append(" ");
		sb.append(dialect.getEquals());
		sb.append(" ");
		if(value == null)
		{
			sb.append("?");
		}
		else{
			sb.append(value);
		}
	}


}
