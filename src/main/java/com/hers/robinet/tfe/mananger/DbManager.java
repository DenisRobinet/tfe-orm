package com.hers.robinet.tfe.mananger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import com.hers.robinet.tfe.dbGenerator.SchemaDB;
import com.hers.robinet.tfe.dialect.Dialect;
import com.hers.robinet.tfe.operator.Operator;

import jpaHelper.JpaClass;
import jpaHelper.ModelException;

/**
 * ACCEPTED TYPE : Integer, Double , String, LocalDateTime
 * @author Robinet
 *
 */
public class DbManager {
	
	public static final int noRelation=0;
	public static final int OneToOne=1;
	public static final int OneToMany=2;
	public static final int ManyToOne=3;
	public static final int ManyToMany=4;
	
	InfoConnection info;
	Dialect dialect;
	Connection connection;
	SchemaDB schema;
	
	public DbManager(InfoConnection info, SchemaDB schema) throws SQLException, ClassNotFoundException{
		
		this.schema= schema;
		this.info = info;
		try {
			dialect = (Dialect)Class.forName(info.getDialect()).newInstance();
		} catch (InstantiationException e) {
			throw new ModelException("Instanciation failed : "+e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ModelException("Illegal access : "+e.getMessage());
		}

		connection = dialect.getConnection(info);

		if(dialect.databaseEmpty(connection, info))
		{	
			LinkedHashMap<String,JpaClass> tables = schema.getSchema();
			for (JpaClass table : tables.values()) {
				StringBuilder build = new StringBuilder();
				dialect.compile(table, build);
				build.append(";");
				String query = build.toString();

				print(query);
				//connection.prepareStatement(query).execute();
			}			
		}
	}
	
	public int context(Model model) throws IllegalArgumentException, IllegalAccessException, SQLException{
		if(model.getContextSate()==Model.toCreate)
		{
			Class<?> type = model.getClass();
			String TableName = ReflectionHelper.getTableName(type);
			Field[] fields = type.getDeclaredFields();
			ArrayList<Object> value = new ArrayList<Object>();
			ArrayList<String> columName = new ArrayList<String>();
			
			for (int i=0;i<fields.length;++i) {
				fields[i].setAccessible(true);
				Object valueTemp = fields[i].get(model);
				if(valueTemp!=null && !isPrimaryType(fields[i].getType()))
				{
					Annotation[] annotations = fields[i].getDeclaredAnnotations();
					
					for (Annotation annotation : annotations) {
						if(annotation instanceof javax.persistence.ManyToOne)
						{
							RelationShip<Model> relation = (RelationShip<Model>)fields[i].get(model);
							ArrayList<Field> fieldsFk = ReflectionHelper.getIDs(relation.getElement().getClass());

							for (Field field : fieldsFk) {
								columName.add("FK_"+ReflectionHelper.getTableName(relation.getElement().getClass())+"_"+ReflectionHelper.getColumnName(field));
								value.add(field.get(relation.getElement()));
							}
						}
						else if(annotation instanceof javax.persistence.OneToOne)
						{
							RelationShip<Model> relation = (RelationShip<Model>)fields[i].get(model);
							Model temp =  relation.getElement();
							if(schema.isBefore(type, temp.getClass())==1)
							{
								ArrayList<Field> fieldsFk = ReflectionHelper.getIDs(relation.getElement().getClass());

								for (Field field : fieldsFk) {
									columName.add("FK_"+ReflectionHelper.getTableName(relation.getElement().getClass())+"_"+ReflectionHelper.getColumnName(field));
									value.add(field.get(relation.getElement()));
								}
							}
							else{
								//need update of the temp (after creation)
							}
						}
					}
				}
				else if (valueTemp!=null){
					columName.add(ReflectionHelper.getColumnName(fields[i]));
					value.add(valueTemp);
				}

			}
			
			String command = insertCommande(TableName, columName);
			print(command);
			if(command != null)
			{
				PreparedStatement prep = connection.prepareStatement(command);
				for(int i=0;i<value.size();++i)
				{
					prep.setObject(i+1, value.get(i));
				}
				prep.execute();
				
				print("Values:"+value.toString());
			}
		}
		
		return 0;
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
		
		ArrayList<Object> res = new ArrayList();
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
	
	public static boolean isPrimaryType(Class<?> type)
	{
		return type == Integer.class || type == Double.class || type == String.class || type == Timestamp.class;
	}
	
	
	private void print(String print)
	{
		if(info.isPrintInfo())
		{
			if(print==null)
			{
				System.out.println("No query to show");
			}
			else{
				System.out.println(print);
			}
			
		}
	}
	
	
	//TODO WITH DIALECT ------------------------------------------------------------------
	
	private String whereCommande(String table, Operator op)
	{
		return "SELECT * FROM "+table+" WHERE "+op.getCondtion();
	}
	
	private String insertCommande(String tableName, ArrayList<String> columnName)
	{
		if(columnName.size()==0)
		{
			return null;
		}
		
		StringBuilder build = new StringBuilder();
		
		build.append("\nINSERT INTO ");
		build.append(tableName);
		build.append(" (");
		build.append(columnName.get(0));
		for(int i=1;i<columnName.size();++i)
		{
			build.append(",");
			build.append(columnName.get(i));
		}
		
		build.append(") VALUES(");
		build.append("?");
		
		for(int i=1;i<columnName.size();++i)
		{
			build.append(",");
			build.append("? ");
		}
		build.append(");");
		return build.toString();
	}
}
