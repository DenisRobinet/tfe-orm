package mananger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dialect.Column;
import dialect.Dialect;
import operator.Operator;

public class DbManager {

	InfoConnection info;
	Dialect dialect;
	Connection connection;
	
	public DbManager(InfoConnection info) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException{
		
		this.info = info;
		dialect = (Dialect)ClassLoader.getSystemClassLoader().loadClass(info.getDialect()).newInstance();

		connection = dialect.getConnection(info);
	}
	
	@SuppressWarnings("rawtypes")
	public void addModel(Class model) throws SQLException
	{
	    Field[] fieldList = model.getDeclaredFields();
		Column[] col = new Column[fieldList.length];
		ArrayList<String> id = new ArrayList<String>();
	    int i=0;
	    for(Field field : fieldList){
	    	
			  Class type = field.getType();
	    	  String name = field.getName();
	    	  Annotation[] annotations = field.getDeclaredAnnotations();
	    	  boolean autoIncrement = false;

	    	  for(Annotation anno : annotations)
	    	  {
	    		  if(anno instanceof javax.persistence.GeneratedValue)
	    		  {
	    			  autoIncrement = true;
	    		  }
	    		  else if(anno instanceof javax.persistence.Id)
	    		  {
	    			  id.add(name);
	    		  }
	    	  }
	    	  col[i] = new Column(name, type, autoIncrement);
	    	  ++i;
	    }
	    
	    String commande = createCommande(model.getName(), col, id);
	    
	    if(info.isPrintInfo())
	    {
	    	System.out.println(commande);
	    }
	    
	    connection.prepareStatement(commande).execute();
	    
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<Model> where(Class type, Operator op) throws SQLException, NoSuchMethodError, InvocationTargetException, IllegalAccessException, InstantiationException, IllegalArgumentException, NoSuchMethodException, SecurityException{
		
		String command = whereCommande(type.getName(), op);
		
	    if(info.isPrintInfo())
	    {
	    	System.out.println(command);
	    }
		
		ResultSet set = connection.prepareStatement(command).executeQuery();
		
		
		Field[] fields =  type.getDeclaredFields();
		Class[] cArg = new Class[fields.length];
		
		for(int i=0;i<cArg.length;++i)
		{
			cArg[i] = fields[i].getType();
		}
		
		ArrayList<Model> res = new ArrayList<>();
		while(set.next())
		{
			Object[] initargs = new Object[cArg.length];
			
			for (int i=0; i<fields.length;++i) {
							
				initargs[i] = fields[i].getType().cast(set.getObject(fields[i].getName()));
				
			}
			
			res.add((Model)type.getDeclaredConstructor(cArg).newInstance(initargs));
		}
		
		return res;
	}

	
	
	
	//TODO WITH DIALECT ------------------------------------------------------------------
	private String createCommande(String tableName, Column[] columns, ArrayList<String> ids)
	{

		StringBuilder build = new StringBuilder();
		
		build.append("CREATE TABLE ");
		build.append(tableName);
		build.append(" (\n");
		
		
		build.append('\t');
		build.append(columns[0].getName());
		build.append(" ");
		
		build.append(dialect.typeOf(columns[0].getType()));
		
		if(columns[0].isAutoIncrement())
		{
			build.append("AUTO_INCREMENT ");
		}
		
		for (int i=1;i<columns.length;++i) {
			build.append(",\n\t");
			
			build.append(columns[i].getName());
			build.append(" ");
			
			build.append(dialect.typeOf(columns[i].getType()));
			
			if(columns[i].isAutoIncrement())
			{
				build.append("AUTO_INCREMENT ");
			}
			
		}
		
		
		build.append(",\n\tPRIMARY KEY (");
		build.append(ids.get(0));
		
		for (int i=1; i<ids.size();++i) {
			build.append(" ,");
			build.append(ids.get(i));
		}
		build.append(")");
		
		build.append("\n)");
		
		return build.toString();
	}
	
	private String whereCommande(String table, Operator op)
	{
		return "SELECT * FROM "+table+" WHERE "+op.getCondtion();
	}
}
