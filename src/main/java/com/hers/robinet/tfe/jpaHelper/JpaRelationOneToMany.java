package com.hers.robinet.tfe.jpaHelper;

import java.lang.reflect.Field;

import com.hers.robinet.tfe.dbGenerator.SchemaDB;
import com.hers.robinet.tfe.mananger.DbManager;

public class JpaRelationOneToMany extends JpaRelation{

	public JpaRelationOneToMany(Field attribute, SchemaDB schema){
		relationType = DbManager.OneToMany;
		isAbstract = true;
	}
}
