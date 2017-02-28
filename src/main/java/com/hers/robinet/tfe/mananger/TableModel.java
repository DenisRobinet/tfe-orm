package com.hers.robinet.tfe.mananger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TableModel {

	private static final int noRelation = 0;
	private static final int manyToManyRel= 1;
	private static final int OneToOneRel = 2;
	private static final int oneToManyRel = 3;
	private static final int manyToOneRel = 4;
	
	Class<?> object;
	ArrayList<Field> allField = new ArrayList<Field>();
	ArrayList<Field> ids = new ArrayList<Field>();
	ArrayList<Field> manyToMany = new ArrayList<Field>();
	ArrayList<Field> oneToOne = new ArrayList<Field>();
	ArrayList<Field> oneToMany = new ArrayList<Field>();
	ArrayList<Field> manyToOne = new ArrayList<Field>();
	
	public TableModel(Class<?> object)
	{
		this.object = object;
		Field[] fields = object.getDeclaredFields();
		
		
		for (Field field : fields) {
			Annotation[] annotations = field.getDeclaredAnnotations();
			boolean isTransiant = false;
			int relationType = noRelation;
			boolean isId = false;
			
			for (int i=0;i<annotations.length;++i) {
				if(annotations[i] instanceof javax.persistence.Id)
				{
					isId = true;
				}
				else if(annotations[i] instanceof javax.persistence.Transient)
				{
					isTransiant = true;
				}
				else if(annotations[i] instanceof javax.persistence.OneToOne)
				{
					relationType = OneToOneRel;
				}				
				else if(annotations[i] instanceof javax.persistence.ManyToMany)
				{
					relationType = manyToManyRel;
				}
				else if(annotations[i] instanceof javax.persistence.OneToMany)
				{
					relationType = oneToManyRel;
				}
				else if(annotations[i] instanceof javax.persistence.ManyToOne)
				{
					relationType = manyToOneRel;
				}
			}
			
			if(!isTransiant)
			{
				allField.add(field);
				if(isId)
				{
					ids.add(field);
				}
				
				if(relationType==OneToOneRel)
				{
					oneToOne.add(field);
				}
				else if(relationType==manyToManyRel)
				{
					manyToMany.add(field);
				}
				else if(relationType==oneToManyRel)
				{
					oneToMany.add(field);
				}
				else if(relationType==manyToOneRel)
				{
					manyToOne.add(field);
				}
				
			}
		}
	}
}
