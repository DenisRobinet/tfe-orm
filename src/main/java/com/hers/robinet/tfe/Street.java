package com.hers.robinet.tfe;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.hers.robinet.tfe.mananger.Model;
import com.hers.robinet.tfe.mananger.RelationShip;

@Entity
public class Street extends Model{

	@Id
	@GeneratedValue
	public Integer id;
	public String name;
	@ManyToOne
	public RelationShip<Address> adresses;
}