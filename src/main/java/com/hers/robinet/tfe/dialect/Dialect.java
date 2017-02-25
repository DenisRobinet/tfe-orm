package com.hers.robinet.tfe.dialect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hers.robinet.tfe.mananger.InfoConnection;

public abstract class Dialect{

	public Connection getConnection(InfoConnection info) throws SQLException, ClassNotFoundException{
		Class.forName(info.getDriver());
		return DriverManager.getConnection(info.getUrl()+info.getDatabase(),info.getUser(),info.getPassword());
	}
	
	@SuppressWarnings("rawtypes")
	public abstract String typeOf(Class type);
	
	public abstract boolean databaseEmpty(Connection connection, InfoConnection info) throws SQLException;


	public void compile(Table table, StringBuilder build) {
		build.append("\nCREATE TABLE ");
		build.append(table.getName());
		build.append(" (\n");
		
		
		build.append('\t');
		build.append(table.getCols().get(0).getName());
		build.append(" ");
		
		build.append(typeOf(table.getCols().get(0).getType()));
		
		if(table.getCols().get(0).isAutoIncrement())
		{
			build.append(" AUTO_INCREMENT ");
		}
		
		for (int i=1;i<table.getCols().size();++i) {
			build.append(",\n\t");
			
			build.append(table.getCols().get(i).getName());
			build.append(" ");
			
			build.append(typeOf(table.getCols().get(i).getType()));
			
			if(table.getCols().get(i).isAutoIncrement())
			{
				build.append(" AUTO_INCREMENT ");
			}
			
		}
		
		if(table.getIds().size()>0)
		{
			build.append(",\n\tPRIMARY KEY (");
			build.append(table.getIds().get(0).getName());
			
			for (int i=1; i<table.getIds().size();++i) {
				build.append(" ,");
				build.append(table.getIds().get(i).getName());
			}
			build.append(")");
		}

		
		for (Fk fk : table.getFks()) {
			build.append(",\n\tFOREIGN KEY (");
			
			build.append(fk.getCols().get(0).getName());
			for (int i=1;i<fk.getCols().size();++i) {
				build.append(", ");
				build.append(fk.getCols().get(i).getName());
			}
			
			build.append(") REFERENCES ");
			build.append(fk.getTable().getName());
			build.append("(");
			build.append(fk.getRef().get(0).getName());
			for (int i=1;i<fk.getCols().size();++i) {
				build.append(", ");
				build.append(fk.getRef().get(i).getName());
			}
			
			build.append(")");
		}
		build.append("\n)");
	}
}
