package com.hers.robinet.tfe;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Street {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	@ManyToMany
	private Address adresses;
}
