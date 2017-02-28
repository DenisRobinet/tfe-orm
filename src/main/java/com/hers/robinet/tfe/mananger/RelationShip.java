package com.hers.robinet.tfe.mananger;

public class RelationShip<T extends Model> {

	private T element;
	
	public static final int nothing = 0; // or other than create, update, ...
	public static final int toCreate = 1;
	public static final int toUpdate = 2;
	public static final int toDelete = 3;
	
	private int state = 0;
	
	public RelationShip(T element)
	{
		state = toCreate;
		this.element = element;
	}
	
	public void setContextState(int state)
	{
		this.state = state;
	}
	
	public int getContextSate()
	{
		return state;
	}
	
	public T getElement()
	{
		return element;
	}
	
	public void setElement(T element)
	{
		this.element = element;
	}
	
	public String toString()
	{
		return "RelationShip to "+element.getClass().getName();
	}
}
