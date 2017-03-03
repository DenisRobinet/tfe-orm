package com.hers.robinet.tfe;

import java.sql.SQLException;

import com.hers.robinet.tfe.dbGenerator.SchemaDB;
import com.hers.robinet.tfe.mananger.DbManager;
import com.hers.robinet.tfe.mananger.InfoConnection;
import com.hers.robinet.tfe.mananger.RelationShip;

public class Main {

	public static void main(String[] args) {
		/*
		String name = "MySQL";
		String url = "jdbc:mysql://localhost:3306/";
		String database = "tfe";
		String user = "root";
		String password = "";
		String driver = "com.mysql.jdbc.Driver";
		String dialect = "com.hers.robinet.tfe.dialect.MySql";
		boolean printInfo = InfoConnection.showInfo;*/
		
		
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
		schema.add(Address.class);
		schema.add(Street.class);
		
		try {
			DbManager manager = new DbManager(info, schema);

			Street temp = new Street();
			Address ad = new Address();
			ad.id = 1;
			ad.setCity("CITY");
			//manager.context(ad);
			temp.name = "aa";
			//manager.context(temp);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	

}
