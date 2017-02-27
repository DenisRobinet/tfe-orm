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
public class Address{

	  @Id
	  @GeneratedValue
	  private Integer id;
	  @OneToMany
	  private List<RelationShip<Street>> street;
	  private String city;
	  private String province;
	  private String country;
	  private String postcode;


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

	  /**
	   * @return the country
	   */
	  public String getCountry() {
	    return country;
	  }

	  /**
	   * @param country the country to set
	   */
	  public Address setCountry(String country) {
	    this.country = country;
	    return this;
	  }

	  /**
	   * @return the postcode
	   */
	  public String getPostcode() {
	    return postcode;
	  }
	  
	  /**
	   * @param postcode the postcode to set
	   */
	  public Address setPostcode(String postcode) {
	    this.postcode = postcode;
	    return this;
	  }

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", city=" + city + ", province=" + province + ", country="
				+ country + ", postcode=" + postcode + "]";
	}
	  

	
}
