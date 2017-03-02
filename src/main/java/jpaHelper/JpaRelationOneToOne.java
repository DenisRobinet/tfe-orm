package jpaHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import com.hers.robinet.tfe.dbGenerator.SchemaDB;
import com.hers.robinet.tfe.mananger.DbManager;

public class JpaRelationOneToOne extends JpaRelation{
	
	public JpaRelationOneToOne(Field attribute, SchemaDB schema){
		relationType = DbManager.OneToOne;
		Class<?> relationWith = (Class<?>)(((ParameterizedType)attribute.getGenericType()).getActualTypeArguments()[0]);
		
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
			ArrayList<JpaRelation> FKattributes = classRelation.getIds();
			for (JpaRelation fk : FKattributes) {
				for(int i=0;i<fk.getName().size();++i)
				{
					this.attribute.add(fk.getAttribute().get(i));
					name.add("FK_"+classRelation.getName()+"_"+fk.getName().get(i));
					type.add(fk.getType().get(i));
				}
			}
		}
		
	}
}
