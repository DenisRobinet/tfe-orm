package com.hers.robinet.tfe.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.hers.robinet.tfe.mananger.InfoConnection;

public class SqlLite extends Dialect{
	 
	@Override
	public String typeOf(Class<?> type) {
		if(type == String.class)
		{
			return "TEXT";
		}
		else if(type == Integer.class){
			return "INTEGER";
		}
		else if(type == Double.class){
			return "REAL";
		}
		else if(type == Timestamp.class){
			return "BLOB";
		}
		else{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean databaseEmpty(Connection connection,  InfoConnection info) throws SQLException {
		
		ResultSet res = connection.prepareStatement("SELECT count(name) FROM sqlite_master WHERE type='table'").executeQuery();
		
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
