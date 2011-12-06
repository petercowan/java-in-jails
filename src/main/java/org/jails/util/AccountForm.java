package org.jails.util;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.jails.property.AcceptsNestedAttributes;
import org.jails.property.IdentifyBy;
import org.jails.validation.RequiredChecks;
import org.jails.validation.constraint.FieldMatch;
import org.jails.validation.constraint.StrongPassword;

import javax.validation.Valid;
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
@AcceptsNestedAttributes
public class AccountForm {
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

	@NotNull
	@Valid
	private AddressForm address;

	@NotBlank(groups = RequiredChecks.class)
	@CreditCardNumber
	private String creditCardNumber;

	@NotNull(groups = RequiredChecks.class)
	@DecimalMin(value = "1000.00")
	@DecimalMax(value = "7000.00")
	private Double balance;

	@Max(value = 123)
	@Min(value = 18)
	private Integer age;

	@Past
	private Date birthday;

	public AccountForm() {}

	public AccountForm(String name, String accountName, String password,
					   String creditCardNumber, Double balance, Integer age, Date birthday) {
		this.name = name;
		this.accountName = accountName;
		this.password = password;
		this.creditCardNumber = creditCardNumber;
		this.balance = balance;
		this.age = age;
		this.birthday = birthday;
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

	public AddressForm getAddress() {
		return address;
	}

	public void setAddress(AddressForm address) {
		this.address = address;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}
