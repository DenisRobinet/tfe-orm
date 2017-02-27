package com.hers.robinet.tfe.dbGenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.ArrayList;


@SuppressWarnings("rawtypes")
public class SchemaDB {
	
	ArrayList<Class> Classes = new ArrayList();
	
	public void add(Class object)
	{
		Classes.add(object);
	}
	
	public ArrayList<Table> generate() throws NoSuchFieldException, SecurityException, ClassNotFoundException
	{
		
		
		ArrayList<Table> tables = new ArrayList();
		ArrayList<Filling> filling = new ArrayList<Filling>();
				
		
		for(int i=0;i<Classes.size();++i)
		{
			
			Field[] fields = Classes.get(i).getDeclaredFields();
			
			String tableName = Classes.get(i).getSimpleName();
			ArrayList<Column> col = new ArrayList<Column>();
			ArrayList<Column> ids = new ArrayList<Column>();
			ArrayList<Fk> fks  = new ArrayList<Fk>();
			
			tables.add(new Table(tableName, col, ids, fks));
		
			for(int j=0;j<fields.length;++j)
			{
				Class type = fields[j].getType();
				String name = fields[j].getName();
				
				Annotation[] annotations = fields[j].getDeclaredAnnotations();
		    	boolean autoIncrement = false;
				if(type!=Integer.class && type!=Double.class && type!=String.class && type!=Timestamp.class)
				{
					boolean id = false;
					for(Annotation anno : annotations)
			    	{
						if(anno instanceof javax.persistence.OneToOne || anno instanceof javax.persistence.ManyToOne)
						{

							type = (Class<?>)(((ParameterizedType)fields[j].getGenericType()).getActualTypeArguments()[0]);
							int k=i;
							while(k>=0 && Classes.get(k)!=type)
							{
								--k;
							}
							
							if(k!=-1)
							{
								ArrayList<Column> ref = tables.get(k).getIds();
								
								ArrayList<Column> arrayFK = new ArrayList();
								for (Column column : ref) {
									Column temp = new Column("FK_"+tables.get(k).getName()+"_"+column.getName(), column.getType(), false);
									col.add(temp);
									arrayFK.add(temp);
								}
								
								Fk fk = new Fk(arrayFK, ref, tables.get(k));
								fks.add(fk);
							}else if(anno instanceof javax.persistence.ManyToOne)
							{
								throw new UnsupportedOperationException();
							}
						}
						else if(anno instanceof javax.persistence.OneToMany)
						{
							//Do by Many to one
						}
						else if(anno instanceof javax.persistence.ManyToMany)
						{
					        ParameterizedType stringListType = (ParameterizedType) fields[j].getGenericType();
					        ParameterizedType relation = (ParameterizedType)stringListType.getActualTypeArguments()[0];
					        type = Class.forName(relation.getActualTypeArguments()[0].getTypeName());
					        
							int k=i;
							while(k>=0 && Classes.get(k)!=type)
							{
								--k;
							}
							
							if(k!=-1)
							{
								Filling fillTemp = new Filling(tables.get(k), tables.get(tables.size()-1));
								filling.add(fillTemp);
							}
						}
						else if(anno instanceof javax.persistence.Id)
				    	{
							id = true;
				    	}
						else{
							throw new UnsupportedOperationException();
						}
			    	}
					
					if(id == true)
					{
						if(fks.size()>0)
							ids.addAll(fks.get(fks.size()-1).getCols());
					}
					
				}
				else if(type==Integer.class)
				{
					Column temp = new Column(name, type, autoIncrement);
					col.add(temp);
			    	for(Annotation anno : annotations)
			    	{
			    		if(anno instanceof javax.persistence.GeneratedValue)
			    		{
			    			temp.setAutoIncrement(true);
			    		}
			    		else if(anno instanceof javax.persistence.Id)
			    		{
			    			ids.add(temp);
			    		}
			    		else{
			    			throw new UnsupportedOperationException();
			    		}
			    	}
				}else
				{
					Column temp = new Column(name, type, autoIncrement);
					col.add(temp);
			    	for(Annotation anno : annotations)
			    	{
			    		if(anno instanceof javax.persistence.Id)
			    		{
			    			ids.add(temp);
			    		}
			    		else{
			    			throw new UnsupportedOperationException();
			    		}
			    	}
			    	
				}
			}
		}
		
		
		for (Filling fill : filling) {
			 
			ArrayList<Column> arrayFK = new ArrayList();
			ArrayList<Fk> fks = new ArrayList();
			ArrayList<Column> cols  = new ArrayList();
			
			ArrayList<Column> ref = fill.getTable1().getIds();
			for (Column column : ref) {
				Column temp = new Column("FK_"+fill.getTable1().getName()+"_"+column.getName(), column.getType(), false);
				arrayFK.add(temp);
			}
			
			cols.addAll(arrayFK);
			Fk fk = new Fk(arrayFK, ref, fill.getTable1());
			fks.add(fk);
			
			arrayFK = new ArrayList();
			ref = fill.getTable2().getIds();
			for (Column column : ref) {
				Column temp = new Column("FK_"+fill.getTable2().getName()+"_"+column.getName(), column.getType(), false);
				arrayFK.add(temp);
			}
			cols.addAll(arrayFK);
			fk = new Fk(arrayFK, ref, fill.getTable2());
			fks.add(fk);
			
			Table tableTemp = new Table("Fill_"+fill.getTable1().getName()+"_"+fill.getTable2().getName(), cols, cols, fks);
		
			tables.add(tableTemp);
		}
		
		return tables;
		
		
	}

}
