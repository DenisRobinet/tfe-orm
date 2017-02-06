package operator;

public class Eq implements Operator {

	private String champ;
	private String value;
	
	public Eq(String champ, String value)
	{
		this.champ = champ;
		this.value = value;
	}

	@Override
	public String getCondtion() {
		
		return champ +"=" +value;
	}

}
