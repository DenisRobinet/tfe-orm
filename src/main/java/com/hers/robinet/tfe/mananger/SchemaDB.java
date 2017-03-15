package com.hers.robinet.tfe.mananger;

import java.util.LinkedHashMap;

import com.hers.robinet.tfe.descriptor.ClassDescriptor;

public class SchemaDB {
	
	LinkedHashMap<String, ClassDescriptor> classes = new LinkedHashMap<String, ClassDescriptor>();
	
	public void add(Class<? extends Model> object)
	{
		ClassDescriptor temp = new ClassDescriptor(object, this);
		classes.put(temp.getIdentifiantName(),temp);
	}

	public LinkedHashMap<String, ClassDescriptor> getSchema()
	{
		return classes;
	}

	public ClassDescriptor getClassDescriptor(Class<?> type)
	{
		return classes.get(ClassDescriptor.getIdentifiantNameFromClass(type));
	}
	
	public void add(ClassDescriptor add)
	{
		classes.put(add.getIdentifiantName(),add);
	}
}
