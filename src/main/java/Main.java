import java.sql.SQLException;

import mananger.DbManager;
import mananger.InfoConnection;

public class Main {

	public static void main(String[] args) {
		
		String name = "MySQL";
		String url = "jdbc:mysql://localhost:3306/tfe";
		String user = "root";
		String password = "";
		String driver = "com.mysql.jdbc.Driver";
		String dialect = "dialect.MySql";
		boolean printInfo = InfoConnection.showInfo;
		
		InfoConnection info = new InfoConnection(name, url, user, password, driver, dialect, printInfo);
		

			try {
				try {
					DbManager test = new DbManager(info);
					Address add = new Address();
					test.addModel(add);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}

}
