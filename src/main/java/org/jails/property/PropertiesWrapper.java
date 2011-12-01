package org.jails.property;

import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PropertiesWrapper extends HashMap<String, String[]> {
	private static Logger logger = LoggerFactory.getLogger(PropertiesWrapper.class);

	private Class classType;
	private String name;
	private String prefix;

	public PropertiesWrapper(int initialCapacity, float loadFactor, Class classType) {
		super(initialCapacity, loadFactor);
		this.classType = classType;
		setName();
	}

	public PropertiesWrapper(int initialCapacity, Class classType) {
		super(initialCapacity);
		this.classType = classType;
		setName();
	}

	public PropertiesWrapper(Class classType) {
		this.classType = classType;
		setName();
	}

	public PropertiesWrapper(Map<? extends String, ? extends String[]> m, Class classType) {
		super(m.size());
		this.classType = classType;
		setName();
		putAll(m);
	}

	private void setName() {
		name = Strings.initLowercase(classType.getSimpleName());
		prefix = name + ".";
	}

	private boolean isNamed(String key) {
		return key.startsWith(prefix);
	}

	private boolean isIndexed(String key) {
		return key.startsWith(name + "[");
	}

	private Integer getIndex(String key) {
		try {
			return Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getIndexedPrefix(int index) {
		return name + "[" + index + "]" + ".";
	}

	private String chopKeyName(String key) {
		if (isNamed(key) || isIndexed(key)) key = key.substring(key.indexOf(".") + 1);
		return key;
	}

	public String formatKey(String key, Integer index) {
		index = (index == null && isIndexed(key)) ? getIndex(key) : index;
		key = chopKeyName(key);
		if (index == null) {
			System.out.println("formatted key: " + prefix + key);

			return prefix + key;
		} else {
			System.out.println("formatted key: " + getIndexedPrefix(index) + key);
			return getIndexedPrefix(index) + key;
		}
	}

	public String formatKey(String key) {
		return formatKey(key, null);
	}

	@Override
	public String[] get(Object key) {
		return get(key, null);
	}

	public String[] get(Object key, Integer index) {
		if (key == null) {
			return null;
		} else {
			String keyStr = (key instanceof String) ? (String) key : key.toString();
			return super.get(formatKey(keyStr, index));
		}
	}

	private String getValue(String[] values) {
		String value = (values == null || values.length == 0) ? null : values[0];
		System.out.println("Got value: " + value);
		return value;
	}

	public String getValue(String key) {
		System.out.println("Getting key: " + key);
		String[] values = get(key);
		return getValue(values);
	}

	public String getValue(String key, int index) {
		String[] values = get(key, index);
		return getValue(values);
	}

	@Override
	public String[] put(String key, String[] value) {
		return super.put(formatKey(key), value);
	}

	public String[] put(String key, int index, String[] value) {
		return super.put(formatKey(key, index), value);
	}

	public String[] put(String key, String value) {
		return put(key, new String[]{value});
	}

	public String[] put(String key, int index, String value) {
		return put(key, index, new String[]{value});
	}

	@Override
	public void putAll(Map<? extends String, ? extends String[]> m) {
		for (String key : m.keySet()) {
			System.out.println("Adding key: " + key + ": " + getValue(m.get(key)));
			if (key != null) put(key, m.get(key));
		}
	}

	@Override
	public String[] remove(Object key) {
		return super.remove(formatKey((String) key));
	}

	public String[] remove(Object key, int index) {
		return super.remove(formatKey((String) key, index));
	}

	public String removeValue(Object key) {
		String[] value = super.remove(formatKey((String) key));
		return getValue(value);
	}

	public String removeValue(Object key, int index) {
		String[] value = super.remove(formatKey((String) key, index));
		return getValue(value);
	}

	public void renameKey(String oldKey, String newKey) {
		String[] value = remove(oldKey);
		put(newKey, value);
	}

	public void renameKey(String oldKey, int index, String newKey) {
		String[] value = remove(oldKey, index);
		put(newKey, index, value);
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(formatKey((String) key));
	}

	public boolean containsKey(Object key, int index) {
		return super.containsKey(formatKey((String) key, index));
	}

	private String getMetaKey(String key, String suffix) {
		String keyRoot = key.substring(0, key.lastIndexOf(".") + 1);
		String keyName = key.substring(key.lastIndexOf(".") + 1);
		return keyRoot + "_" + keyName + "_" + suffix;
	}

	public String[] composeDates(String key, Integer index) {
		String[] value = get(key, index);
		if (value == null || value.length == 0) {
			key = formatKey(key);
			String metaKey = getMetaKey(key, "date_day");

			String[] days = get(getMetaKey(key, "date_day"), index);
			String[] months = get(getMetaKey(key, "date_month"), index);
			String[] years = get(getMetaKey(key, "date_year"), index);
			if (days != null && months != null && years != null
					&& days.length > 0
					&& days.length == months.length && months.length == years.length) {
				String[] dateValue = new String[days.length];
				for (int i = 0; i < days.length; i++) {
					String day = days[i];
					String month = months[i];
					String year = years[i];
					dateValue[i] = year + "-" + month + "-" + day;
				}
				return dateValue;
			}
		}
		return value;
	}

	public String[] composeDates(String key) {
		return composeDates(key, null);
	}

	public String composeDate(String key, Integer index) {
		String[] value = composeDates(key, index);
		return getValue(value);
	}

	public String composeDate(String key) {
		return composeDate(key, null);
	}

	public String[] getSelectOther(String key, Integer index) {
		String[] select = get(key, index);
		if (select != null && select.length > 0) {
			key = formatKey(key);
			String[] value = new String[select.length];
			String[] others = get(getMetaKey(key, "select_other"), index);
			if (others != null && others.length > 0) {
				for (int i = 0; i < others.length; i++) {
					if ("other".equalsIgnoreCase(select[i])) {
						value[i] = others[i];
					} else {
						value[i] = select[i];
					}
				}
				return value;
			}
		}
		return select;
	}

	public String[] getSelectOther(String key) {
		return getSelectOther(key, null);
	}

	public String getSelectOtherValue(String key, Integer index) {
		String[] value = getSelectOther(key, index);
		return getValue(value);
	}

	public String getSelectOtherValue(String key) {
		return getSelectOtherValue(key, null);
	}

	public String[][] getLists(String key, Integer index) {
		String[] values = get(key, index);
		String[][] lists;
		if (values != null && values.length > 0) {
			lists = new String[values.length][];
			for (int i = 0; i < values.length; i++) {
				String val = values[i];
				lists[i] = val.split(",");
			}
		} else {
			lists = new String[0][0];
		}
		return lists;

	}

	public String[][] getLists(String key) {
		return getLists(key, null);
	}

	public String[] getList(String key, Integer index) {
		String[][] lists = getLists(key, index);
		return (lists.length > 0) ? lists[0] : new String[0];
	}

	public String[] getList(String key) {
		return getList(key, null);
	}


}
