package dialect;

public class Column {
	private String name;
	@SuppressWarnings("rawtypes")
	private Class type;
	private boolean autoIncrement;
	
	@SuppressWarnings("rawtypes")
	public Column(String name, Class type, boolean autoIncrement) {
		super();
		this.name = name;
		this.type = type;
		this.autoIncrement = autoIncrement;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("rawtypes")
	public Class getType() {
		return type;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	
	
	
	

}
