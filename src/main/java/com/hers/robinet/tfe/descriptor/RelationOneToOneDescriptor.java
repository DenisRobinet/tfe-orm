package com.hers.robinet.tfe.descriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import com.hers.robinet.tfe.mananger.DbManager;
import com.hers.robinet.tfe.mananger.SchemaDB;

public class RelationOneToOneDescriptor extends RelationDescriptor{
	ClassDescriptor classRelation;
	public RelationOneToOneDescriptor(Field attribute, SchemaDB schema){
		relationType = DbManager.OneToOne;
		Class<?> relationWith = (Class<?>)(((ParameterizedType)attribute.getGenericType()).getActualTypeArguments()[0]);
		
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
			ArrayList<RelationDescriptor> FKattributes = classRelation.getIds();
			for (RelationDescriptor fk : FKattributes) {
				for(int i=0;i<fk.getTypeStruct().size();++i)
				{
					typeStruct.add(new TypeDescriptor(fk.getTypeStruct().get(i).getAttribute(), "FK_"+classRelation.getName()+"_"+fk.getTypeStruct().get(i).getName(), fk.getTypeStruct().get(i).getType()));
				}
			}
		}
	}
	
	public ClassDescriptor getReferencedTable()
	{
		return classRelation;
	}
}
