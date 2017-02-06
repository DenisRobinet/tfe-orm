package dialect;

import java.sql.Connection;
import java.sql.SQLException;

import mananger.InfoConnection;

public abstract class Dialect{

	public abstract Connection getConnection(InfoConnection info)throws SQLException, ClassNotFoundException;

	@SuppressWarnings("rawtypes")
	public abstract String typeOf(Class type);
	
}
