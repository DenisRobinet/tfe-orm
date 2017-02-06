package dialect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mananger.InfoConnection;

public class MySql extends Dialect{

	@Override
	public Connection getConnection(InfoConnection info) throws SQLException, ClassNotFoundException{
		Class.forName(info.getDriver());
		return DriverManager.getConnection(info.getUrl(),info.getUser(),info.getPassword());
	}

	@Override
	@SuppressWarnings("rawtypes") 
	public String typeOf(Class type) {
		if(type == String.class)
		{
			return "VARCHAR(100) ";
		}
		else if(type == Integer.class){
			return "INT ";
		}
		return null;
	}
	
	
	
	


}
