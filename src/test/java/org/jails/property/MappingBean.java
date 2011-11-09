package org.jails.property;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

@AcceptsNestedAttributes
public class MappingBean {
	String[] array;
	BigDecimal bigDecimal;
	BigInteger bigInteger;
	Boolean[] booleanArray;
	Boolean booleanProperty;
	Byte[] byteArray;
	Byte byteProperty;
	Calendar calendar;
	Date date;
	Double[] doubleArray;
	Double doubleProperty;
	Float[] floatArray;
	Float floatProperty;
	Integer[] integerArray;
	Integer integer;
	Long[] longArray;
	Long longProperty;
	Number number;
	Short[] shortArray;
	Short shortProperty;
	String stringProperty;

	@NotNull
	@Valid
	@AcceptsNestedAttributes
	MappingBean mappingBean;

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}

	public BigInteger getBigInteger() {
		return bigInteger;
	}

	public void setBigInteger(BigInteger bigInteger) {
		this.bigInteger = bigInteger;
	}

	public Boolean[] getBooleanArray() {
		return booleanArray;
	}

	public void setBooleanArray(Boolean[] booleanArray) {
		this.booleanArray = booleanArray;
	}

	public Boolean getBooleanProperty() {
		return booleanProperty;
	}

	public void setBooleanProperty(Boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public Byte getByteProperty() {
		return byteProperty;
	}

	public void setByteProperty(Byte byteProperty) {
		this.byteProperty = byteProperty;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double[] getDoubleArray() {
		return doubleArray;
	}

	public void setDoubleArray(Double[] doubleArray) {
		this.doubleArray = doubleArray;
	}

	public Double getDoubleProperty() {
		return doubleProperty;
	}

	public void setDoubleProperty(Double doubleProperty) {
		this.doubleProperty = doubleProperty;
	}

	public Float[] getFloatArray() {
		return floatArray;
	}

	public void setFloatArray(Float[] floatArray) {
		this.floatArray = floatArray;
	}

	public Float getFloatProperty() {
		return floatProperty;
	}

	public void setFloatProperty(Float floatProperty) {
		this.floatProperty = floatProperty;
	}

	public Integer[] getIntegerArray() {
		return integerArray;
	}

	public void setIntegerArray(Integer[] integerArray) {
		this.integerArray = integerArray;
	}

	public Integer getInteger() {
		return integer;
	}

	public void setInteger(Integer integer) {
		this.integer = integer;
	}

	public Long[] getLongArray() {
		return longArray;
	}

	public void setLongArray(Long[] longArray) {
		this.longArray = longArray;
	}

	public Long getLongProperty() {
		return longProperty;
	}

	public void setLongProperty(Long longProperty) {
		this.longProperty = longProperty;
	}

	public Number getNumber() {
		return number;
	}

	public void setNumber(Number number) {
		this.number = number;
	}

	public Short[] getShortArray() {
		return shortArray;
	}

	public void setShortArray(Short[] shortArray) {
		this.shortArray = shortArray;
	}

	public Short getShortProperty() {
		return shortProperty;
	}

	public void setShortProperty(Short shortProperty) {
		this.shortProperty = shortProperty;
	}

	public String getStringProperty() {
		return stringProperty;
	}

	public void setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
	}

	public MappingBean getMappingBean() {
		return mappingBean;
	}

	public void setMappingBean(MappingBean mappingBean) {
		this.mappingBean = mappingBean;
	}
}

