import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Street {

	@Id
	@GeneratedValue
	private Integer id;
	@Id
	private Integer temp;
	private String name;
	@OneToOne
	private Address adresses;
}
