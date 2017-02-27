package com.hers.robinet.tfe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.LongFunction;
import java.util.function.Predicate;

import com.hers.robinet.tfe.dbGenerator.SchemaDB;
import com.hers.robinet.tfe.mananger.DbManager;
import com.hers.robinet.tfe.mananger.InfoConnection;
import com.hers.robinet.tfe.operator.Eq;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Lunched");
		/*String name = "MySQL";
		String url = "jdbc:mysql://localhost:3306/";
		String database = "tfe";
		String user = "root";
		String password = "";
		String driver = "com.mysql.jdbc.Driver";
		String dialect = "dialect.MySql";
		boolean printInfo = InfoConnection.showInfo;
		*/
		
		String name = "SqlLite";
		String url = "jdbc:sqlite:";
		String database = "tfe.db";
		String user = "root";
		String password = "";
		String driver = "org.sqlite.JDBC";
		String dialect = "com.hers.robinet.tfe.dialect.SqlLite";
		boolean printInfo = InfoConnection.showInfo;
		
		InfoConnection info = new InfoConnection(name, url,database,  user, password, driver, dialect, printInfo);
		
		
		
		
		SchemaDB schema = new SchemaDB();
		schema.add(Street.class);
		schema.add(Address.class);
		
		try {
			new DbManager(info, schema);
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
		}
	}
	
	

}
