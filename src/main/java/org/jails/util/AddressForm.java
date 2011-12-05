package org.jails.util;

import org.hibernate.validator.constraints.NotBlank;
import org.jails.property.AcceptsNestedAttributes;
import org.jails.validation.RequiredChecks;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AcceptsNestedAttributes
public class AddressForm {
	@NotBlank(groups = RequiredChecks.class)
	@Size(min = 2, max = 125)
	private String street;
	private String apt;

	@NotBlank(groups = RequiredChecks.class)
	@Size(min = 2, max = 75)
	private String city;

	@NotNull
	private String state;

	@NotBlank(groups = RequiredChecks.class)
	@Size(min = 5, max = 15)
	private String zip;

	@NotNull
	private String country;

	public AddressForm(String street, String city, String state, String zip, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getApt() {
		return apt;
	}

	public void setApt(String apt) {
		this.apt = apt;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
