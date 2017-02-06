package mananger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;

import dialect.Column;
import dialect.Fk;
import dialect.Table;


@SuppressWarnings("rawtypes")
public class SchemaDB {
	
	ArrayList<Class> Classes = new ArrayList<>();
	
	public void add(Class object)
	{
		Classes.add(object);
	}
	
	public ArrayList<Table> generate()
	{
		
		ArrayList<Table> tables = new ArrayList<>();
		for(int i=0;i<Classes.size();++i)
		{
			
			Field[] fields = Classes.get(i).getDeclaredFields();
			
			String tableName = Classes.get(i).getName();
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
				if(type!=Integer.class && type!=Double.class && type!=String.class && type!=LocalDateTime.class)
				{
					boolean id = false;
					for(Annotation anno : annotations)
			    	{
						if(anno instanceof javax.persistence.OneToOne || anno instanceof javax.persistence.ManyToOne)
						{

							int k=i;
							while(k>=0 && Classes.get(k)!=type)
							{
								--k;
							}
							
							if(k!=-1)
							{
								ArrayList<Column> ref = tables.get(k).getIds();
								
								ArrayList<Column> arrayFK = new ArrayList<>();
								for (Column column : ref) {
									Column temp = new Column("FK_"+tables.get(k).getName()+"_"+column.getName(), column.getType(), false);
									col.add(temp);
									arrayFK.add(temp);
								}
								
								Fk fk = new Fk(arrayFK, ref, tables.get(k));
								fks.add(fk);
							}
							else if(anno instanceof javax.persistence.ManyToOne)
							{
									//TODO verify if can do SQL error (FK)
									tables.remove(tables.size()-1);
									Class temp= Classes.remove(i);
									Classes.add(i+1, temp);
									--i;
								
							}
							

						}
						else if(anno instanceof javax.persistence.OneToMany)
						{
							//Do by Many to one
						}
						else if(anno instanceof javax.persistence.ManyToMany)
						{
							
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
		
		return tables;
		
		
	}

}
