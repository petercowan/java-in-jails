package org.jails.property;

import org.apache.commons.beanutils.PropertyUtils;

public abstract class Identity {
	public static IdentifyBy[] getIdentifiers(Object object) {
		IdentifyBy.List idList = ReflectionUtil
				.getClassAnnotation(IdentifyBy.List.class, object.getClass());
		IdentifyBy[] ids = null;
		if (idList != null) {
			ids = idList.value();
		} else {
			IdentifyBy id = ReflectionUtil.getClassAnnotation(IdentifyBy.class, object.getClass());
			if (id != null) {
				ids = new IdentifyBy[]{id};
			}
		}
		return ids;
	}

	public static IdentifyBy[] getIdentifiers(Object object, String property) {
		try {
			Class nestedType = PropertyUtils.getPropertyType(object, property);
			return getIdentifiers(nestedType);
		} catch (Exception e) {
			e.printStackTrace();
			return new IdentifyBy[0];
		}
	}

	public static boolean identifyBy(Object object, String idField) {
		IdentifyBy[] ids = getIdentifiers(object);

		for (int i = 0; ids != null && i < ids.length; i++) {
			IdentifyBy id = ids[i];
			if (idField.equals(id.field())) return true;
		}
		return false;
	}

	public static boolean identifyBy(Object object, String property, String idField) {
		IdentifyBy[] ids = getIdentifiers(object, property);

		for (int i = 0; ids != null && i < ids.length; i++) {
			IdentifyBy id = ids[i];
			if (idField.equals(id.field())) return true;
		}
		return false;
	}

}
