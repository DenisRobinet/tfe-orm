package com.hers.robinet.tfe.operator;

import com.hers.robinet.tfe.dialect.Dialect;

public abstract interface Operator {

	public void getCondtion(Dialect dialect, StringBuilder sb);
}
