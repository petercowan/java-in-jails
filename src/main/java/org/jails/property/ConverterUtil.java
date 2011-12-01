package org.jails.property;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;

import java.util.Date;

public class ConverterUtil {
	static ConverterUtil instance;
	private ConvertUtilsBean converter;

	synchronized static ConverterUtil getInstance() {
		if (instance == null) {
			instance = new ConverterUtil();
			instance.initializeConverters();
		}
		return instance;
	}

	private void initializeConverters() {
		converter = BeanUtilsBean.getInstance().getConvertUtils();
		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(new String[]{"MM-dd-yyyy","MM/dd/yyyy","MM-dd-yyyy HH:mm:ss aa","MM/dd/yyyy HH:mm:ss aa"});
		converter.register(dateConverter, Date.class);
		converter.lookup(Object[].class);
	}

	public String convert(Object object) {
		return converter.convert(object);
	}

	public boolean canConvert(Object object) {
		return converter.lookup(object.getClass()) != null;
	}

	public ConvertUtilsBean getConverter() {
		return converter;
	}
}
