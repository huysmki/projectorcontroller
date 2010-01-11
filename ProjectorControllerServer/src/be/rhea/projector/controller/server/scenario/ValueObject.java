package be.rhea.projector.controller.server.scenario;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueObject implements Serializable, Cloneable {

	static final long serialVersionUID = 1;
	private static Map<String,List<Field>> fieldMap = new HashMap<String,List<Field>>();
	
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		List<Field> list = getFields();
		for(Field field : list) {
			Object obj1 = getValueOfProperty(field, this);
			Object obj2 = getValueOfProperty(field, obj);

			if (!((obj1 == null) ? (obj2 == null) : obj1.equals(obj2))) {
				return false;
			}
		}
		return true;
	}

	public final int hashCode() {
		int result = 17;
		List<Field> list = getFields();
		for(Field field : list){

			Object value = getValueOfProperty(field, this);

			int c = (value == null) ? 0 : value.hashCode();

			result = (result * 37) + c;
		}

		return result;
	}

	private List<Field> getFields() {
		List<Field> fields = (List<Field>) fieldMap.get(this.getClass().getName());
		if (fields == null) {
			fields = new ArrayList<Field>();
			searchFields(this.getClass(), fields);
			fieldMap.put(this.getClass().getName(), fields);
		}
		return fields;
	}

	@SuppressWarnings("unchecked")
	private void searchFields(Class clazz, List<Field> list) {
		Field[] declaredFields = clazz.getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			Field field = declaredFields[i];
			list.add(field);
		}
		if (!clazz.getSuperclass().getName().equals(ValueObject.class.getName())) {
			searchFields(clazz.getSuperclass(), list);
		}
		
	}

	private Object getValueOfProperty(Field field, Object object) {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {
			throw new RuntimeException("Exception in retrieving value of field " + field.getName()
					+ " in object " + this.getClass().getName(), e);
		}
	}
	
}
