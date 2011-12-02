package org.jails.util;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.jails.property.IdentifyBy;
import org.jails.validation.RequiredChecks;
import org.jails.validation.constraint.FieldMatch;
import org.jails.validation.constraint.StrongPassword;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@IdentifyBy(field = "id")
@FieldMatch(field = "password", matchField = "confirmPassword", message = "Passwords must match.")
public class TestForm {
	@NotBlank(groups = RequiredChecks.class)
	@Size(min = 2, max = 25)
	private String name;

	@NotBlank(groups = RequiredChecks.class)
	@Pattern(regexp = "[0-9a-zA-Z]*", message = "may only contain letters and numbers.")
	private String accountName;

	@NotBlank(groups = RequiredChecks.class)
	@StrongPassword
	private String password;

	@NotBlank(groups = RequiredChecks.class)
	@StrongPassword
	private String confirmPassword;

	@NotBlank(groups = RequiredChecks.class)
	@CreditCardNumber
	private String creditCardNumber;

	@NotNull(groups = RequiredChecks.class)
	@DecimalMin(value = "1000.00")
	@DecimalMax(value = "7000.00")
	private Double limit;

	@Max(value = 12345)
	@Min(value = 3)
	private Integer size;

	@NotNull(groups = RequiredChecks.class)
	@Past
	private Date dateCreated;

	public TestForm() {
	}

	public TestForm(String name, String accountName, String password,
					String creditCardNumber, Double limit, Integer size, Date dateCreated) {
		this.name = name;
		this.accountName = accountName;
		this.password = password;
		this.creditCardNumber = creditCardNumber;
		this.limit = limit;
		this.size = size;
		this.dateCreated = dateCreated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public Double getLimit() {
		return limit;
	}

	public void setLimit(Double limit) {
		this.limit = limit;
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
