package com.hers.robinet.tfe.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.hers.robinet.tfe.mananger.InfoConnection;

public class MySql extends Dialect{

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
		else if(type == Timestamp.class){
			//TODO can be improve
			return "VARCHAR(50)";
		}
		else{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean databaseEmpty(Connection connection,  InfoConnection info) throws SQLException {
		
		ResultSet res = connection.prepareStatement("SELECT COUNT(DISTINCT `table_name`) FROM `information_schema`.`columns` WHERE `table_schema` = '"+info.getDatabase()+"'").executeQuery();
		
		if(res.next())
		{
			if(res.getInt(1)==0)
			{
				return true;
			}
			else{
				return false;
			}
			
		}
		else{
			return false;
		}
	}


}
