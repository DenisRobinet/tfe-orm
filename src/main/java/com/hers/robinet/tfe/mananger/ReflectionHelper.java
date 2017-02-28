package com.hers.robinet.tfe.mananger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ReflectionHelper {


	public static String getTableName(Class<?> type)
	{
		String name = null;
		for (Annotation annotation : type.getAnnotations())
			if(annotation instanceof javax.persistence.Table)
				name = ((javax.persistence.Table) annotation).name();
		
		if(name == null)
			name = type.getSimpleName();
		return name;
	}

	public static String getColumnName(Field field)
	{
		String name = null;
		for (Annotation annotation : field.getAnnotations())
			if(annotation instanceof javax.persistence.Column)
				name = ((javax.persistence.Column) annotation).name();
		
		if(name == null)
			name = field.getName();
		return name;
	}
	
	public static ArrayList<Field> getIDs(Class<?> type)
	{
		ArrayList<Field> ids = new ArrayList<Field>();
		Field[] fields = type.getDeclaredFields();
		
		for (Field field : fields) {
			boolean isId = false;
			Annotation[] annotations = field.getAnnotations();
			for(int i=0;i<annotations.length;++i)
				if(annotations[i] instanceof javax.persistence.Id)
					isId = true;
			if(isId)
				ids.add(field);
		}
		return ids;
	}
	
	public static ArrayList<Field> getAnnotationFieldID(Class<?> object)
	{
		Field[] fields = object.getDeclaredFields();
		ArrayList<Field> res = new ArrayList<Field>();
		
		for (Field field : fields) {
			Annotation[] annotations = field.getDeclaredAnnotations();
			boolean found = false;
			for (int i=0;i<annotations.length && found==false;++i) {
				if(annotations[i] instanceof javax.persistence.Id)
				{
					res.add(field);
					found = true;
				}
			}
		}
		return res;
	}
}
