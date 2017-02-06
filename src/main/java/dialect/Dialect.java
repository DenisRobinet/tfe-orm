package dialect;

import java.sql.Connection;
import java.sql.SQLException;

import mananger.InfoConnection;

public abstract class Dialect{

	public abstract Connection getConnection(InfoConnection info)throws SQLException, ClassNotFoundException;

	@SuppressWarnings("rawtypes")
	public abstract String typeOf(Class type);
	
	public abstract boolean databaseEmpty(Connection connection, InfoConnection info) throws SQLException;
	
	public abstract void compile(Table tables, StringBuilder build);
}
