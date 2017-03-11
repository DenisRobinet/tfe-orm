package com.hers.robinet.tfe;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hers.robinet.tfe.mananger.Model;
import com.hers.robinet.tfe.mananger.RelationShip;

@Entity
@Table(name = "Street")
public class Street extends Model{

	@Id
	@GeneratedValue
	public Integer id;
	public String name;
	@OneToOne
	public RelationShip<Address> adresses;
}