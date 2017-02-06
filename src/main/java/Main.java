import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.LongFunction;
import java.util.function.Predicate;

import mananger.DbManager;
import mananger.InfoConnection;
import mananger.Model;
import operator.Eq;

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

					DbManager test = new DbManager(info);
					//test.addModel(Address.class);
					ArrayList<Model> res = test.where(Address.class, new Eq("id", "1")); 
					
					for (Model model : res) {
						System.out.println(model);
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
			} catch (NoSuchMethodError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	
	}
	
	

}
