package com.hers.robinet.tfe.descriptor;

import java.lang.reflect.Field;

import com.hers.robinet.tfe.mananger.DbManager;
import com.hers.robinet.tfe.mananger.SchemaDB;

public class RelationOneToManyDescriptor extends RelationDescriptor{

	public RelationOneToManyDescriptor(Field attribute, SchemaDB schema){
		relationType = DbManager.OneToMany;
		isAbstract = true;
	}
}
