package com.hers.robinet.tfe;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.hers.robinet.tfe.mananger.RelationShip;

@Entity
public class Street {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	@ManyToOne
	private RelationShip<Address> adresses;
}
