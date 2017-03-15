package com.hers.robinet.tfe.mananger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hers.robinet.tfe.descriptor.ClassDescriptor;
import com.hers.robinet.tfe.descriptor.ModelRuntimeException;
import com.hers.robinet.tfe.dialect.Dialect;
import com.hers.robinet.tfe.operator.Operator;

/**
 * ACCEPTED TYPE : Integer, Double , String, LocalDateTime
 * @author Robinet
 *
 */
public class DbManager {
	
	private final static Logger LOGGER = Logger.getLogger(DbManager.class.getName());
	
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
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new ModelRuntimeException("Instanciation failed : "+e.getMessage());
		} catch (IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new ModelRuntimeException("Illegal access : "+e.getMessage());
		}

		connection = dialect.getConnection(info);
		LinkedHashMap<String,ClassDescriptor> tables = schema.getSchema();
		
		boolean isEmpty = dialect.databaseEmpty(connection, info);
		for (ClassDescriptor table : tables.values()) {
			table.prepare(connection, dialect, isEmpty);
		}
	}
	
	public int context(Model model) throws IllegalArgumentException, IllegalAccessException, SQLException{
		if(model.getContextSate()==Model.toCreate)
		{
			
		}
		
		return 0;
	}

	
	/**
	 * Load a model with his id
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public boolean select(Model model) throws SQLException
	{	
		ClassDescriptor classDesc = schema.getClassDescriptor(model.getClass());
		return  classDesc.select(connection, dialect, model);
	}
	
	/**
	 * Load a model with the operator
	 * @param model
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public Model select(Class<?> model, Operator operator) throws SQLException
	{	
		//TODO
		ClassDescriptor classDesc = schema.getClassDescriptor(model);
	 	PreparedStatement prepState = classDesc.select(connection, dialect, operator);
		ResultSet resultset = prepState.executeQuery();
		return classDesc.generate(resultset);
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

}
