package com.hers.robinet.tfe.descriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hers.robinet.tfe.mananger.DbManager;
import com.hers.robinet.tfe.mananger.InternalException;
import com.hers.robinet.tfe.mananger.SchemaDB;

public class RelationManyToManyDescriptor extends RelationDescriptor{
	private final static Logger LOGGER = Logger.getLogger(DbManager.class.getName());
	
	ClassDescriptor classRelation;
	public RelationManyToManyDescriptor(Field attribute, SchemaDB schema, ClassDescriptor tableClass){

		relationType = DbManager.ManyToMany;
		ParameterizedType stringListType;
		try{
		stringListType = (ParameterizedType) attribute.getGenericType();
		}catch(ClassCastException e)
		{
			throw new ModelRuntimeException(tableClass.getIdentifiantName()+" don't have a List()");
		}
	    ParameterizedType relation = (ParameterizedType)stringListType.getActualTypeArguments()[0];
	    
	    Class<?> relationWith;
	    try {
	    	relationWith = Class.forName(relation.getActualTypeArguments()[0].getTypeName());
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new InternalException("Error : the class "+relation.getActualTypeArguments()[0].getTypeName()+" cannot be found");
		}
	    
		classRelation = schema.getClassDescriptor(relationWith);
		
		for (Annotation annotation : attribute.getAnnotations()){
			if(annotation instanceof javax.persistence.Column)
			{
				isnullable = ((javax.persistence.Column) annotation).nullable();
				isUnique = ((javax.persistence.Column) annotation).unique();
			}
			else if (annotation instanceof javax.persistence.Id)
			{
				isId = true;
			}
			else if (annotation instanceof javax.persistence.GeneratedValue){
				throw new ModelRuntimeException("@GeneratedValue can only be use on Integer");
			}
		}
		
		
		if(classRelation==null)
		{
			isAbstract = true;
		}
		else{
			isAbstract = true;
			
			schema.add(new ClassDescriptor(classRelation, tableClass));
		}
	}
	
	public ClassDescriptor getReferencedTable()
	{
		return classRelation;
	}
}
