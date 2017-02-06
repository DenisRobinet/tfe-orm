package mananger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import dialect.Dialect;
import dialect.Table;
import operator.Operator;

/**
 * ACCEPTED TYPE : Integer, Double , String, LocalDateTime
 * @author Robinet
 *
 */
public class DbManager {

	InfoConnection info;
	Dialect dialect;
	Connection connection;
	
	public DbManager(InfoConnection info, SchemaDB schema) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException{
		
		this.info = info;
		dialect = (Dialect)ClassLoader.getSystemClassLoader().loadClass(info.getDialect()).newInstance();

		connection = dialect.getConnection(info);
		
		if(dialect.databaseEmpty(connection, info))
		{
			ArrayList<Table> tables = schema.generate();
			
			
			for (Table table : tables) {
				StringBuilder build = new StringBuilder();
				dialect.compile(table, build);
				build.append(";");
				String query = build.toString();

				print(query);
				connection.prepareStatement(query).execute();
			}			
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<Object> where(Class type, Operator op) throws SQLException, NoSuchMethodError, InvocationTargetException, IllegalAccessException, InstantiationException, IllegalArgumentException, NoSuchMethodException, SecurityException{
		
		String command = whereCommande(type.getName(), op);
		
		print(command);
		
		ResultSet set = connection.prepareStatement(command).executeQuery();
		
		
		Field[] fields =  type.getDeclaredFields();
		Class[] cArg = new Class[fields.length];
		
		for(int i=0;i<cArg.length;++i)
		{
			cArg[i] = fields[i].getType();
		}
		
		ArrayList<Object> res = new ArrayList<>();
		while(set.next())
		{
			Object[] initargs = new Object[cArg.length];
			
			for (int i=0; i<fields.length;++i) {
							
				initargs[i] = fields[i].getType().cast(set.getObject(fields[i].getName()));
				
			}
			
			res.add((Object)type.getDeclaredConstructor(cArg).newInstance(initargs));
		}
		
		return res;
	}
	
	@SuppressWarnings("rawtypes")
	public void insert(Object model) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		Class type = model.getClass();
		String TableName = type.getName();
		Field[] fields = type.getDeclaredFields();
		Object[] value = new Object[fields.length];
		
		for (int i=0;i<fields.length;++i) {
			fields[i].setAccessible(true);
			value[i] = fields[i].get(model);
		}
		
		String command = insertCommande(TableName, value);
		
		PreparedStatement prep = connection.prepareStatement(command);
		for(int i=0;i<value.length;++i)
		{
			prep.setObject(i+1, value[i]);
		}
		prep.execute();
		
		print(command+"\nValues:"+Arrays.toString(value));
		
	}
	
	@SuppressWarnings("rawtypes")
	public void update(Object model, Operator op) throws IllegalArgumentException, IllegalAccessException
	{
		Class type = model.getClass();
		String TableName = type.getName();
		Field[] fields = type.getDeclaredFields();
		Object[] value = new Object[fields.length];
		
		for (int i=0;i<fields.length;++i) {
			fields[i].setAccessible(true);
			value[i] = fields[i].get(model);
		}
	}
	
	public void delete(Object model)
	{
		
	}
	
	
	private void print(String print)
	{
		if(info.isPrintInfo())
		{
			System.out.println(print);
		}
	}
	
	
	//TODO WITH DIALECT ------------------------------------------------------------------
	
	private String whereCommande(String table, Operator op)
	{
		return "SELECT * FROM "+table+" WHERE "+op.getCondtion();
	}
	
	private String insertCommande(String tableName, Object[] value)
	{
		StringBuilder build = new StringBuilder();
		
		build.append("INSERT INTO ");
		build.append(tableName);
		build.append("\nVALUES(");
		//build.append(value[0]);		
		build.append("?");
		
		for(int i=1;i<value.length;++i)
		{
			build.append(",");
			//build.append(dialect.valueOf(value[i]));
			build.append("?");
		}
		build.append(");");
		return build.toString();
	}
}
