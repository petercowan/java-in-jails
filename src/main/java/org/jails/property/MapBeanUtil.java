package org.jails.property;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;

import java.util.Date;

public class MapBeanUtil {
	static MapBeanUtil instance;

	synchronized static MapBeanUtil getInstance() {
		if (instance == null) {
			instance = new MapBeanUtil();
		}
		return instance;
	}

	void initializeConverters() {
		ConvertUtilsBean convertUtilsBean = BeanUtilsBean.getInstance().getConvertUtils();
		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(new String[]{"mm-dd-yyyy","mm/dd/yyyy","mm-dd-yyyy HH:mm:ss aa","mm/dd/yyyy HH:mm:ss aa"});
		convertUtilsBean.register(dateConverter, Date.class);
		Converter converter = convertUtilsBean.lookup(Object[].class);
	}
}
