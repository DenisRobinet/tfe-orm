package com.hers.robinet.tfe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.LongFunction;
import java.util.function.Predicate;

import com.hers.robinet.tfe.dbGenerator.SchemaDB;
import com.hers.robinet.tfe.mananger.DbManager;
import com.hers.robinet.tfe.mananger.InfoConnection;
import com.hers.robinet.tfe.mananger.Model;
import com.hers.robinet.tfe.mananger.ReflectionHelper;
import com.hers.robinet.tfe.mananger.RelationShip;
import com.hers.robinet.tfe.operator.Eq;

public class Main {

	public static void main(String[] args) {
		
		String name = "MySQL";
		String url = "jdbc:mysql://localhost:3306/";
		String database = "tfe";
		String user = "root";
		String password = "";
		String driver = "com.mysql.jdbc.Driver";
		String dialect = "com.hers.robinet.tfe.dialect.MySql";
		boolean printInfo = InfoConnection.showInfo;
		
		/*
		String name = "SqlLite";
		String url = "jdbc:sqlite:";
		String database = "tfe.db";
		String user = "root";
		String password = "";
		String driver = "org.sqlite.JDBC";
		String dialect = "com.hers.robinet.tfe.dialect.SqlLite";
		boolean printInfo = InfoConnection.showInfo;*/
		
		InfoConnection info = new InfoConnection(name, url,database,  user, password, driver, dialect, printInfo);
		
		
		
		
		SchemaDB schema = new SchemaDB();
		schema.add(Address.class);
		schema.add(Street.class);
		
		try {
			DbManager manager = new DbManager(info, schema);

			Street temp = new Street();
			Address ad = new Address();
			ad.id = 1;
			ad.setCity("CITY");
			manager.context(ad);
			temp.name = "aa";
			temp.adresses = new RelationShip<Address>(ad);
			manager.context(temp);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
