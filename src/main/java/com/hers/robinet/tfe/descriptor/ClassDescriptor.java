package com.hers.robinet.tfe.descriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hers.robinet.tfe.dialect.Dialect;
import com.hers.robinet.tfe.mananger.DbManager;
import com.hers.robinet.tfe.mananger.InternalException;
import com.hers.robinet.tfe.mananger.Model;
import com.hers.robinet.tfe.mananger.SchemaDB;
import com.hers.robinet.tfe.operator.And;
import com.hers.robinet.tfe.operator.Eq;
import com.hers.robinet.tfe.operator.Operator;
import com.mysql.jdbc.Statement;

/**
 * supported annotation:
 * @Table(name="tableName",
 * 		indexes = {@Index(name = "my_index_name",  columnList="iso_code", unique = True),
                  @Index(name = "my_index_name2", columnList="name, colum2", unique = False)})
 */
public class ClassDescriptor{
	
	private final static Logger LOGGER = Logger.getLogger(ClassDescriptor.class.getName());
	
	public static String getIdentifiantNameFromClass(Class<?> type){
		return type.getName();
	}
	
	private Class<?> type;
	private String identifiantName;
	private String name;
	private ArrayList<IndexDescriptor> indexes = new ArrayList<IndexDescriptor>();
	private ArrayList<RelationDescriptor> attributes = new ArrayList<RelationDescriptor>();
	private ArrayList<RelationDescriptor> ids = new ArrayList<RelationDescriptor>();
	private ArrayList<RelationDescriptor> fks = new ArrayList<RelationDescriptor>();
	
	public Class<?> getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public ArrayList<IndexDescriptor> getIndexes() {
		return indexes;
	}

	public ArrayList<RelationDescriptor> getAttributes() {
		return attributes;
	}

	public ArrayList<RelationDescriptor> getIds() {
		return ids;
	}

	public ArrayList<RelationDescriptor> getFks() {
		return fks;
	}

	public String getIdentifiantName() {
		return identifiantName;
	}
	
	public ClassDescriptor(Class<?> type, SchemaDB schema)
	{
		this.type = type;

		name = null;
		for (Annotation annotation : type.getAnnotations())
			if(annotation instanceof javax.persistence.Table){
				name = ((javax.persistence.Table) annotation).name();
				if (name =="")
					name=null;
				for (javax.persistence.Index index : ((javax.persistence.Table) annotation).indexes()) {
					indexes.add(new IndexDescriptor(index));
				}
			}
				
		if(name == null)
			name = type.getSimpleName();
		identifiantName = getIdentifiantNameFromClass(type);
		
		int generatedValueCount = 0;
		for (Field attribute : type.getDeclaredFields()) {
			RelationDescriptor temp = RelationDescriptor.getAttribute(attribute, this, schema);
			
			if (temp != null)
			{
				if(temp.isAutoGenerated()){
					++generatedValueCount;
					if(generatedValueCount>1)
						throw new ModelRuntimeException("A table can only have one @GeneretedValue");
				}
				
				if(temp.isId() && !temp.isAbstract)
				{
					ids.add(temp);
				}
				
				if(temp.getRelationType() != DbManager.noRelation)
				{
					fks.add(temp);
				}
				
				attributes.add(temp);
			}
		}
	}
	
	public ClassDescriptor(ClassDescriptor table1, ClassDescriptor table2)
	{
		identifiantName = "FILL_"+table1.getIdentifiantName()+"_"+table2.getIdentifiantName();
		name="FILL_"+table1.getName()+"_"+table2.getName();
		

		RelationManyToOneDescriptor att1 = new RelationManyToOneDescriptor(table1);
		RelationManyToOneDescriptor att2 = new RelationManyToOneDescriptor(table2);
		attributes.add(att1);
		attributes.add(att2);
		ids = attributes;
		fks = attributes;
	}

	/**
	 * Create a object with the resultSet. it need to be in position and ave all the feild
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public Model generate(ResultSet resultSet) throws SQLException
	{
		Model model = null;

		try {
			model = (Model)type.getDeclaredConstructor().newInstance();
			this.generate(resultSet, model);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ModelRuntimeException("A model need to have a constructor with no parameters");
		}
		
		return model;
	}
	
	public void generate(ResultSet resultSet, Model model) throws SQLException
	{
		try {
			RelationDescriptor.JpaRelationIterator it = new RelationDescriptor.JpaRelationIterator(attributes);
			while(it.hasNext())
			{
				TypeDescriptor td = it.next();
				if(!it.getCurrentRelation().isAbstract)
				{
					if(it.getCurrentRelation().getRelationType()==DbManager.noRelation)
					{
						Object obj = resultSet.getObject(td.getName());
						td.getAttribute().set(model, obj);
					}
					else if(it.getCurrentRelation().getRelationType()==DbManager.OneToOne)
					{
						
					}
					
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new ModelRuntimeException("error with the parameter at the creation");
		}
	}

	
	//-------------------------------END IMMUABLE-------------
	
	private PreparedStatement insert;
	private PreparedStatement selectID;
	private PreparedStatement update;
	private PreparedStatement delete;
	
	public void prepare(Connection connection, Dialect dialect, boolean create) throws SQLException{	    
	    String queryInsert = dialect.compileInsert(this);
	    System.out.println(queryInsert);
	    insert = connection.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);
	   
		String querySelectID = prepareSelectID(dialect);
		if(querySelectID != null)
		{
			System.out.println(querySelectID);
			selectID = connection.prepareStatement(querySelectID);
		}
		
	    if(create)
	    {
			String queryCreate = dialect.compileCreate(this);
		    
			System.out.println(queryCreate);
		    
		    PreparedStatement createPS =  connection.prepareStatement(queryCreate);
		    createPS.executeUpdate();
		    createPS.close();
	    }


	}
	
	public int insert(Model model) throws SQLException
	{
		if(insert == null) {
			LOGGER.log(Level.SEVERE, "The insert preparedstatement is not initialized");
			throw new InternalException("The insert preparedstatement is not initialized");
		}
		
		RelationDescriptor.JpaRelationIterator it = new RelationDescriptor.JpaRelationIterator(attributes);
		
		int i=1;
		try {
			while(it.hasNext())
			{
				TypeDescriptor td = it.next();
				if(!it.getCurrentRelation().isAbstract)
				{
					insert.setObject(i, td.getAttribute().get(model));
					++i;
				}
			}
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new InternalException("Error : the model is not a instance of this classDescriptor representation.");
		} catch (IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new InternalException("Error : cannot access the field.");
		}
		
		int res = insert.executeUpdate();
		
		
		return res;
	}
	
	public String prepareSelectID(Dialect dialect){
	    Operator op = null;
	    RelationDescriptor.JpaRelationIterator it = new RelationDescriptor.JpaRelationIterator(ids);
		if(it.hasNext())
		{
			TypeDescriptor td = it.next();
			op = new Eq(td.getName());
		}
		
		while(it.hasNext())
		{
			TypeDescriptor td = it.next();
			op = new And(op, new Eq(td.getName()));
		}
	    
		if(op != null)
		{
			String querySelectID = dialect.compileSelectAll(this, op);
			return querySelectID;
		}
		return null;
	}
	
	public boolean select(Connection connection, Dialect dialect, Model model) throws SQLException{
		if(selectID == null) throw new ModelRuntimeException("The model don't have ID");
		
		RelationDescriptor.JpaRelationIterator it = new RelationDescriptor.JpaRelationIterator(ids);
		
		int i=1;
		try {
			while(it.hasNext())
			{
				TypeDescriptor td = it.next();
				selectID.setObject(i, td.getAttribute().get(model));
				++i;
			}
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new InternalException("Error : the model is not a instance of this classDescriptor representation.");
		} catch (IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new InternalException("Error : cannot access the field.");
		}
		
		ResultSet resultset = selectID.executeQuery();
		
		boolean res = false;
		if(resultset.next())
		{
			generate(resultset, model);
			res = true;
		}
		resultset.close();
		
		return res;
	}
	
	public PreparedStatement select(Connection connection, Dialect dialect, Operator operator){
		return null;
	}
	
}
