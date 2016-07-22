package org.epics.pvmarshaller.marshaller.serialisers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.epics.pvdata.pv.FieldBuilder;
import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVByte;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVFloat;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVShort;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;

/**
 * Serialises primitive values
 * @author Matt Taylor
 *
 */
public class PrimitiveSerialiser {

	/**
	 * Gets whether the specified class if a primitive value.
	 * In PVData, a string is included in the list of primitives
	 * @param fieldType The field type to check
	 * @return
	 */
	public static boolean isPrimitive(Class<?> fieldType) {
		if (fieldType.equals(Integer.class) || fieldType.equals(int.class) || fieldType.equals(Short.class)
				|| fieldType.equals(short.class) || fieldType.equals(Long.class) || fieldType.equals(long.class)
				|| fieldType.equals(Byte.class) || fieldType.equals(byte.class) || fieldType.equals(Boolean.class)
				|| fieldType.equals(boolean.class) || fieldType.equals(Float.class) || fieldType.equals(float.class)
				|| fieldType.equals(Double.class) || fieldType.equals(double.class) || fieldType.equals(Character.class)
				|| fieldType.equals(char.class) || fieldType.equals(String.class)) {
			return true;
		}

		return false;
	}

	/**
	 * Gets the PVData scalar type of the given class	
	 * @param fieldType The class to check 
	 * @return
	 */
	public static ScalarType getScalarType(Class<?> fieldType) {

		ScalarType type = null;

		if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
			type = ScalarType.pvInt;
		} else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
			type = ScalarType.pvShort;
		} else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
			type = ScalarType.pvLong;
		} else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
			type = ScalarType.pvByte;
		} else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
			type = ScalarType.pvBoolean;
		} else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
			type = ScalarType.pvFloat;
		} else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
			type = ScalarType.pvDouble;
		} else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
			type = ScalarType.pvString;
		} else if (fieldType.equals(String.class)) {
			type = ScalarType.pvString;
		}

		return type;
	}

	/**
	 * Adds the specified field to the field builder structure
	 * @param field The field to add
	 * @param fieldBuilder The fieldBuilder object
	 */
	public static void addToPVStructure(Field field, FieldBuilder fieldBuilder) {
		Class<?> fieldType = field.getType();

		String name = field.getName();
		ScalarType type = null;

		type = getScalarType(fieldType);

		fieldBuilder.add(name, type);
	}

	/**
	 * Adds a generic field to the field builder structure
	 * @param field The field to add
	 * @param fieldBuilder The fieldBuilder object
	 * @param obj The object to add
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void addGenericToPVStructure(Field field, FieldBuilder fieldBuilder, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> fieldType = obj.getClass();

		String name = field.getName();
		ScalarType type = null;

		type = getScalarType(fieldType);

		fieldBuilder.add(name, type);
	}

	/**
	 * Populates a PVStructure with data from a primitve
	 * @param field The field to get the data from
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setFieldValue(Field field, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> fieldType = field.getType();

		field.setAccessible(true);

		if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
			setIntFieldValue(field, structure, object);
		} else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
			setShortFieldValue(field, structure, object);
		} else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
			setLongFieldValue(field, structure, object);
		} else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
			setByteFieldValue(field, structure, object);
		} else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
			setBooleanFieldValue(field, structure, object);
		} else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
			setFloatFieldValue(field, structure, object);
		} else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
			setDoubleFieldValue(field, structure, object);
		} else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
			setCharFieldValue(field, structure, object);
		} else if (fieldType.equals(String.class)) {
			setStringFieldValue(field, structure, object);
		}
	}

	/**
	 * Populates a PVStructure with data from a generic field
	 * @param field The field
	 * @param structure  The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setGenericFieldValue(Field field, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		Method method = Serialiser.findGetter(object, field.getName());
		Object fieldObject = method.invoke(object);
		Class<?> fieldType = fieldObject.getClass();

		field.setAccessible(true);

		if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
			setIntFieldValue(field, structure, object);
		} else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
			setShortFieldValue(field, structure, object);
		} else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
			setLongFieldValue(field, structure, object);
		} else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
			setByteFieldValue(field, structure, object);
		} else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
			setBooleanFieldValue(field, structure, object);
		} else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
			setFloatFieldValue(field, structure, object);
		} else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
			setDoubleFieldValue(field, structure, object);
		} else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
			setCharFieldValue(field, structure, object);
		} else if (fieldType.equals(String.class)) {
			setStringFieldValue(field, structure, object);
		}
	}

	/**
	 * Populates an int field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setIntFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			int value = (int) fieldObject;
			PVInt pvInt = structure.getSubField(PVInt.class, source.getName());
			pvInt.put(value);
		}
	}

	/**
	 * Populates a short field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setShortFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			short value = (short) fieldObject;
			PVShort pvShort = structure.getSubField(PVShort.class, source.getName());
			pvShort.put(value);
		}
	}

	/**
	 * Populates a long field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setLongFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			long value = (long) fieldObject;
			PVLong pvLong = structure.getSubField(PVLong.class, source.getName());
			pvLong.put(value);
		}
	}

	/**
	 * Populates a byte field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setByteFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			byte value = (byte) fieldObject;
			PVByte pvByte = structure.getSubField(PVByte.class, source.getName());
			pvByte.put(value);
		}
	}

	/**
	 * Populates a boolean field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setBooleanFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			boolean value = (boolean) fieldObject;
			PVBoolean pvBoolean = structure.getSubField(PVBoolean.class, source.getName());
			pvBoolean.put(value);
		}
	}

	/**
	 * Populates a float field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setFloatFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			float value = (float) fieldObject;
			PVFloat pvFloat = structure.getSubField(PVFloat.class, source.getName());
			pvFloat.put(value);
		}
	}

	/**
	 * Populates a double field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setDoubleFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			double value = (double) fieldObject;
			PVDouble pvDouble = structure.getSubField(PVDouble.class, source.getName());
			pvDouble.put(value);
		}
	}

	/**
	 * Populates a char field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setCharFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			String value = String.valueOf(fieldObject);
			PVString pvString = structure.getSubField(PVString.class, source.getName());
			pvString.put(value);
		}
	}

	/**
	 * Populates a string field
	 * @param source The source field
	 * @param structure The PVStructure to populate
	 * @param object The object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setStringFieldValue(Field source, PVStructure structure, Object object)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = Serialiser.findGetter(object, source.getName());
		Object fieldObject = method.invoke(object);
		if (fieldObject != null) {
			String value = (String) fieldObject;
			PVString pvString = structure.getSubField(PVString.class, source.getName());
			pvString.put(value);
		}
	}
}