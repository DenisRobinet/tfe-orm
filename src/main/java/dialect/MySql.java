package dialect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import mananger.InfoConnection;

public class MySql extends Dialect{

	@Override
	public Connection getConnection(InfoConnection info) throws SQLException, ClassNotFoundException{
		Class.forName(info.getDriver());
		return DriverManager.getConnection(info.getUrl()+"/"+info.getDatabase(),info.getUser(),info.getPassword());
	}
	
	 

	@Override
	@SuppressWarnings("rawtypes") 
	public String typeOf(Class type) {
		if(type == String.class)
		{
			return "TEXT";
		}
		else if(type == Integer.class){
			return "INT";
		}
		else if(type == Double.class){
			return "DOUBLE";
		}
		else if(type == LocalDateTime.class){
			//TODO can be improve
			return "VARCHAR(50)";
		}
		else{
			throw new IllegalArgumentException();
		}
	}

	@Override
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



	@Override
	public boolean databaseEmpty(Connection connection,  InfoConnection info) throws SQLException {
		
		ResultSet res = connection.prepareStatement("SELECT COUNT(DISTINCT `table_name`) FROM `information_schema`.`columns` WHERE `table_schema` = '"+info.getDatabase()+"'").executeQuery();
		
		if(res.next())
		{
			return true;
		}
		else{
			return false;
		}
	}


}
