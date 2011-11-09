package org.jails.util;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

public class ModelBean {
	@NotBlank
	@Size(min = 2, max = 25)
	private String name;

	@Max(value = 12345)
	@Min(value = 0)
	private Integer size;

	@NotBlank
	@Past
	private Date dateCreated;

	public ModelBean(String name, Integer size, Date dateCreated) {
		this.name = name;
		this.size = size;
		this.dateCreated = dateCreated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
