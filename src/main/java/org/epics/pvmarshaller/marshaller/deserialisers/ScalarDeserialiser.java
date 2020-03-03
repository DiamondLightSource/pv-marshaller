package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVByte;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVFloat;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVShort;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVUByte;
import org.epics.pvdata.pv.PVUInt;
import org.epics.pvdata.pv.PVULong;
import org.epics.pvdata.pv.PVUShort;

/**
 * Deserialise a Scalar value
 * @author Matt Taylor
 *
 */
public class ScalarDeserialiser {
	
	Deserialiser deserialiser;
	
	/**
	 * Constructor
	 * @param deserialiser
	 */
	public ScalarDeserialiser(Deserialiser deserialiser) {
		this.deserialiser = deserialiser;
	}
	
	/**
	 * Populates the target object field with data from a PVField 
	 * @param target The target object
	 * @param fieldName The field to populate
	 * @param pvField The field to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void deserialise(Object target, String fieldName, PVField pvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		if (pvField instanceof PVInt) {
			PVInt bpvField = (PVInt)pvField;
			setValue(target, fieldName, bpvField.get());
		} else if (pvField instanceof PVShort) {
			PVShort bpvField = (PVShort)pvField;
			setValue(target, fieldName, bpvField.get());
		} else if (pvField instanceof PVLong) {
			PVLong bpvField = (PVLong)pvField;
			setValue(target, fieldName, bpvField.get());
		} else if (pvField instanceof PVByte) {
			PVByte bpvField = (PVByte)pvField;
			setValue(target, fieldName, bpvField.get());
		} else if (pvField instanceof PVBoolean) {
			PVBoolean bpvField = (PVBoolean)pvField;
			setValue(target, fieldName, bpvField.get());
		} else if (pvField instanceof PVFloat) {
			PVFloat bpvField = (PVFloat)pvField;
			setValue(target, fieldName, bpvField.get());
		} else if (pvField instanceof PVDouble) {
			PVDouble bpvField = (PVDouble)pvField;
			setValue(target, fieldName, bpvField.get());
		} else if (pvField instanceof PVString) {
			PVString bpvField = (PVString)pvField;
			setStringValue(target, fieldName, bpvField.get());
		} else if (pvField instanceof PVUInt) {
			throw new IllegalArgumentException("Field type of Unsigned Int is not supported (" + fieldName + ")");
		} else if (pvField instanceof PVUShort) {
			throw new IllegalArgumentException("Field type of Unsigned Short is not supported (" + fieldName + ")");
		} else if (pvField instanceof PVULong) {
			throw new IllegalArgumentException("Field type of Unsigned Long is not supported (" + fieldName + ")");
		} else if (pvField instanceof PVUByte) {
			throw new IllegalArgumentException("Field type of Unsigned Byte is not supported (" + fieldName + ")");
		} else {
			throw new IllegalArgumentException("Unsupported field type for " + fieldName);
		}
	}
	
	/**
	 * Deserialises values from a PVField and returns the result
	 * @param pvField The PVField to get data from
	 * @param componentType The type of data (string or char)
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object deserialise(PVField pvField, Class<?> componentType) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		if (pvField instanceof PVInt) {
			PVInt bpvField = (PVInt)pvField;
			return bpvField.get();
		} else if (pvField instanceof PVShort) {
			PVShort bpvField = (PVShort)pvField;
			return bpvField.get();
		} else if (pvField instanceof PVLong) {
			PVLong bpvField = (PVLong)pvField;
			return bpvField.get();
		} else if (pvField instanceof PVByte) {
			PVByte bpvField = (PVByte)pvField;
			return bpvField.get();
		} else if (pvField instanceof PVBoolean) {
			PVBoolean bpvField = (PVBoolean)pvField;
			return bpvField.get();
		} else if (pvField instanceof PVFloat) {
			PVFloat bpvField = (PVFloat)pvField;
			return bpvField.get();
		} else if (pvField instanceof PVDouble) {
			PVDouble bpvField = (PVDouble)pvField;
			return bpvField.get();
		} else if (pvField instanceof PVString) {
			PVString bpvField = (PVString)pvField;
			return getStringValue(bpvField.get(), componentType, pvField.getFieldName());
		} else if (pvField instanceof PVUInt) {
			throw new IllegalArgumentException("Field type of Unsigned Int is not supported (" + pvField.getFieldName() + ")");
		} else if (pvField instanceof PVUShort) {
			throw new IllegalArgumentException("Field type of Unsigned Short is not supported (" + pvField.getFieldName() + ")");
		} else if (pvField instanceof PVULong) {
			throw new IllegalArgumentException("Field type of Unsigned Long is not supported (" + pvField.getFieldName() + ")");
		} else if (pvField instanceof PVUByte) {
			throw new IllegalArgumentException("Field type of Unsigned Byte is not supported (" + pvField.getFieldName() + ")");
		} else {
			throw new IllegalArgumentException("Unsupported field type for " + pvField.getFieldName());
		}
	}
	
	/**
	 * Sets the value of a field in the target object
	 * @param target The target object
	 * @param variableName The name of the field to set
	 * @param value The value to use
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void setValue(Object target, String variableName, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = deserialiser.findSetter(target, variableName);
		if (method != null) {
			method.invoke(target, value);
		}
	}
	
	/**
	 * Sets a string or char value of a field in the target object
	 * @param target The target object
	 * @param variableName The name of the field to set
	 * @param value The value to use
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void setStringValue(Object target, String variableName, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// Determine if the object member is a string or a char
		Method method = deserialiser.findSetter(target, variableName);
		if (method != null) {
			Parameter parameters[] = method.getParameters();
			if (parameters[0].getType().equals(Character.class) || 
				parameters[0].getType().equals(char.class)) {
				if (value.length() > 1) {
					throw new IllegalArgumentException(variableName + " has too many characters for a single char");
				}
				char charValue = value.charAt(0);
				method.invoke(target, charValue);
			} else {
				method.invoke(target, value);
			}
		}
	}
	
	/**
	 * Gets the string or char value of a string depending on the specified component type
	 * @param value The source String value
	 * @param componentType The component type
	 * @param variableName The name of the variable
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Object getStringValue(String value, Class<?> componentType, String variableName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		if (componentType.equals(Character.class) || 
				componentType.equals(char.class)) {
			if (value.length() > 1) {
				throw new IllegalArgumentException(variableName + " has too many characters for a single char");
			}
			char charValue = value.charAt(0);
			return charValue;
		} else {
			return value;
		}
	}
}
