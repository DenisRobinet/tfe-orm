package com.hers.robinet.tfe.mananger;

public abstract class Model {

	public static final int nothing = 0; // or other than create, update, ...
	public static final int toCreate = 1;
	public static final int toUpdate = 2;
	public static final int toDelete = 3;
	
	private int state = 0;
	
	public Model()
	{
		state = toCreate;
	}
	
	public void setContextState(int state)
	{
		this.state = state;
	}
	
	public int getContextSate()
	{
		return state;
	}
}
