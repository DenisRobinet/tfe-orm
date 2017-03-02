package com.hers.robinet.tfe.dbGenerator;

import java.util.LinkedHashMap;

import com.hers.robinet.tfe.mananger.Model;

import jpaHelper.JpaClass;

public class SchemaDB {
	
	LinkedHashMap<String, JpaClass> classes = new LinkedHashMap<String, JpaClass>();
	
	public void add(Class<? extends Model> object)
	{
		JpaClass temp = new JpaClass(object, this);
		classes.put(temp.getIdentifiantName(),temp);
	}
	
	public LinkedHashMap<String, JpaClass> getSchema()
	{
		return classes;
	}
	
	public JpaClass getJpaClass(Class<?> type)
	{
		return classes.get(JpaClass.getIdentifiantNameFromClass(type));
	}
	
	public void add(JpaClass add)
	{
		classes.put(add.getIdentifiantName(),add);
	}
	
	public int isBefore(Class<?> first, Class<?> second)
	{
		String firstName = JpaClass.getIdentifiantNameFromClass(first);
		String secondName = JpaClass.getIdentifiantNameFromClass(second);
		int before = 0;
		for(int i=0;i<classes.size() && before == 0;++i)
		{
			if(classes.get(firstName).getType()==first)//TODO FALSE
			{
				before = 1;
			}
			else if (classes.get(secondName).getType()==second)
			{
				before = -1;
			}
		}
		return before;
	}
}
