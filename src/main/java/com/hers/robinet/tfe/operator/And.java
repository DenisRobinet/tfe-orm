package com.hers.robinet.tfe.operator;

import com.hers.robinet.tfe.dialect.Dialect;

public class And implements Operator{

	Operator op1, op2;
	
	public And(Operator op1, Operator op2)
	{
		this.op1 = op1;
		this.op2 = op2;
	}
	
	@Override
	public void getCondtion(Dialect dialect, StringBuilder sb) {
		sb.append("(");
		op1.getCondtion(dialect, sb);
		sb.append(" ");
		sb.append(dialect.getAnd());
		sb.append(" ");
		op2.getCondtion(dialect, sb);
		sb.append(")");
	}

}
