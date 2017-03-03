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
public class Address extends Model{

	  @Id
	  @GeneratedValue
	  public Integer id;
	  @OneToMany
	  public List<RelationShip<Street>> street;
	  @Id
	  private String city;
	  private String province;


	/**
	   * @return the id
	   */
	  public Integer getId() {
	    return id;
	  }

	  /**
	   * @param id the id to set
	   */
	  public Address setId(Integer id) {
	    this.id = id;
	    return this;
	  }

	  /**
	   * @return the city
	   */
	  public String getCity() {
	    return city;
	  }

	  /**
	   * @param city the city to set
	   */
	  public Address setCity(String city) {
	    this.city = city;
	    return this;
	  }

	  /**
	   * @return the province
	   */
	  public String getProvince() {
	    return province;
	  }

	  /**
	   * @param province the province to set
	   */
	  public Address setProvince(String province) {
	    this.province = province;
	    return this;
	  }


	
}
