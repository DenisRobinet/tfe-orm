package jpaHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import com.hers.robinet.tfe.dbGenerator.SchemaDB;
import com.hers.robinet.tfe.mananger.DbManager;

public class JpaRelationManyToMany extends JpaRelation{
	
	public JpaRelationManyToMany(Field attribute, SchemaDB schema, JpaClass tableClass){

		relationType = DbManager.ManyToMany;
		System.out.println("azeazeazea");
		ParameterizedType stringListType;
		try{
		stringListType = (ParameterizedType) attribute.getGenericType();
		}catch(ClassCastException e)
		{
			throw new ModelException(tableClass.getIdentifiantName()+" don't have a List()");
		}
	    ParameterizedType relation = (ParameterizedType)stringListType.getActualTypeArguments()[0];
	    
	    Class<?> relationWith;
	    try {
	    	relationWith = Class.forName(relation.getActualTypeArguments()[0].getTypeName());
		} catch (ClassNotFoundException e) {
			throw new ModelException("Error : the class "+relation.getActualTypeArguments()[0].getTypeName()+" cannot be found\n"+
									 "This error should never append.");
		}
	    
		JpaClass classRelation = schema.getJpaClass(relationWith);
		
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
				throw new ModelException("@GeneratedValue can only be use on Integer");
			}
		}
		
		
		if(classRelation==null)
		{
			isAbstract = true;
		}
		else{
			isAbstract = true;
			
			schema.add(new JpaClass(classRelation, tableClass));
		}
		
	}
}
