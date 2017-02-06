package mananger;

public class InfoConnection {

	public static final boolean showInfo = true;
	public static final boolean hideInfo = false;
	
	private String name;
	private String url;
	private String database;
	private String user;
	private String password;
	private String driver;
	private String dialect;
	private boolean printInfo;
	
	public InfoConnection(String name, String url,String database, String user,String password, String driver, String dialect, boolean printInfo) {
		super();
		this.name = name;
		this.url = url;
		this.database = database;
		this.user = user;
		this.password = password;
		this.driver = driver;
		this.dialect = dialect;
		this.printInfo = printInfo;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
	
	public String getDatabase() {
		return database;
	}

	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}

	public String getDriver() {
		return driver;
	}
	
	public String getDialect() {
		return dialect;
	}

	public boolean isPrintInfo() {
		return printInfo;
	}
}
