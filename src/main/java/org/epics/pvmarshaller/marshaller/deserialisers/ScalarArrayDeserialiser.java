package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import org.epics.pvdata.pv.BooleanArrayData;
import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.IntArrayData;
import org.epics.pvdata.pv.LongArrayData;
import org.epics.pvdata.pv.PVBooleanArray;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVUByteArray;
import org.epics.pvdata.pv.PVUIntArray;
import org.epics.pvdata.pv.PVULongArray;
import org.epics.pvdata.pv.PVUShortArray;
import org.epics.pvdata.pv.ShortArrayData;
import org.epics.pvdata.pv.StringArrayData;

/**
 * Deserialises a Scalar Array
 * @author Matt Taylor
 *
 */
public class ScalarArrayDeserialiser {
	
	Deserialiser deserialiser;
	
	/**
	 * Constructor
	 * @param deserialiser
	 */
	public ScalarArrayDeserialiser(Deserialiser deserialiser) {
		this.deserialiser = deserialiser;
	}
	
	/**
	 * Populates the target object field with data from a PVField 
	 * @param target The target object to populate
	 * @param fieldName The field name to populate
	 * @param pvField The PVField to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws InstantiationException
	 */
	public void deserialise(Object target, String fieldName, PVField pvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException {
		
		if (pvField instanceof PVIntArray) {
			PVIntArray bpvField = (PVIntArray)pvField;
			setIntArrayValue(target, fieldName, bpvField);
		} else if (pvField instanceof PVShortArray) {
			PVShortArray bpvField = (PVShortArray)pvField;
			setShortArrayValue(target, fieldName, bpvField);
		} else if (pvField instanceof PVLongArray) {
			PVLongArray bpvField = (PVLongArray)pvField;
			setLongArrayValue(target, fieldName, bpvField);
		} else if (pvField instanceof PVByteArray) {
			PVByteArray bpvField = (PVByteArray)pvField;
			setByteArrayValue(target, fieldName, bpvField);
		} else if (pvField instanceof PVBooleanArray) {
			PVBooleanArray bpvField = (PVBooleanArray)pvField;
			setBooleanArrayValue(target, fieldName, bpvField);
		} else if (pvField instanceof PVFloatArray) {
			PVFloatArray bpvField = (PVFloatArray)pvField;
			setFloatArrayValue(target, fieldName, bpvField);
		} else if (pvField instanceof PVDoubleArray) {
			PVDoubleArray bpvField = (PVDoubleArray)pvField;
			setDoubleArrayValue(target, fieldName, bpvField);
		} else if (pvField instanceof PVStringArray) {
			PVStringArray bpvField = (PVStringArray)pvField;
			setStringArrayValue(target, fieldName, bpvField);
		} else if (pvField instanceof PVUIntArray) {
			throw new IllegalArgumentException("Field type of Unsigned Int Array is not supported (" + fieldName + ")");
		} else if (pvField instanceof PVUShortArray) {
			throw new IllegalArgumentException("Field type of Unsigned Short Array is not supported (" + fieldName + ")");
		} else if (pvField instanceof PVULongArray) {
			throw new IllegalArgumentException("Field type of Unsigned Long Array is not supported (" + fieldName + ")");
		} else if (pvField instanceof PVUByteArray) {
			throw new IllegalArgumentException("Field type of Unsigned Byte Array is not supported (" + fieldName + ")");
		} else {
			throw new IllegalArgumentException("Unsupported field type for " + fieldName);
		}
	}
	
	/**
	 * Populates data from an int array
	 * @param target The target object to populate
	 * @param variableName The name of the field to populate
	 * @param bpvField The array to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setIntArrayValue(Object target, String variableName, PVIntArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		int dataArray[] = new int[bpvField.getLength()];
		IntArrayData intArrayData = new IntArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		int gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(totalGot, bpvField.getLength() - totalGot, intArrayData);
			totalGot += numGot;
			gotArray = intArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[intArrayData.offset + i] = gotArray[i];
			}
		}
		
		Method method = deserialiser.findSetter(target, variableName);
		
		if (method == null) {
			return;
		}
		
		Parameter parameters[] = method.getParameters();
		
		if (parameters[0].getType().isArray()) {
			Class<?> componentType = parameters[0].getType().getComponentType();
			
			if (componentType.equals(int.class)) {
				method.invoke(target, dataArray);
			} else if (componentType.equals(Integer.class)) {
				Integer integerArray[] = new Integer[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					integerArray[i] = dataArray[i];
				}
				method.invoke(target, (Object)integerArray);
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
			List<Integer> list;
			if (parameters[0].getType().isInterface()) {
				list = new ArrayList<Integer>();
			} else {
				list = (List) parameters[0].getType().newInstance();
			}
			
			for (int integer : dataArray) {
				list.add(integer);
			}
			method.invoke(target, list);
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Populates data from a short array
	 * @param target The target object to populate
	 * @param variableName The name of the field to populate
	 * @param bpvField The array to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setShortArrayValue(Object target, String variableName, PVShortArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		short dataArray[] = new short[bpvField.getLength()];
		ShortArrayData shortArrayData = new ShortArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		short gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(totalGot, bpvField.getLength() - totalGot, shortArrayData);
			totalGot += numGot;
			gotArray = shortArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[shortArrayData.offset + i] = gotArray[i];
			}
		}
		
		Method method = deserialiser.findSetter(target, variableName);
		
		if (method == null) {
			return;
		}
		
		Parameter parameters[] = method.getParameters();
		
		if (parameters[0].getType().isArray()) {
			Class<?> componentType = parameters[0].getType().getComponentType();
			
			if (componentType.equals(short.class)) {
				method.invoke(target, dataArray);
			} else if (componentType.equals(Short.class)) {
				Short shortArray[] = new Short[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					shortArray[i] = dataArray[i];
				}
				method.invoke(target, (Object)shortArray);
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
			List<Short> list;
			if (parameters[0].getType().isInterface()) {
				list = new ArrayList<Short>();
			} else {
				list = (List) parameters[0].getType().newInstance();
			}
			for (short shortValue : dataArray) {
				list.add(shortValue);
			}
			method.invoke(target, list);
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Populates data from a long array
	 * @param target The target object to populate
	 * @param variableName The name of the field to populate
	 * @param bpvField The array to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setLongArrayValue(Object target, String variableName, PVLongArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		long dataArray[] = new long[bpvField.getLength()];
		LongArrayData longArrayData = new LongArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		long gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(totalGot, bpvField.getLength() - totalGot, longArrayData);
			totalGot += numGot;
			gotArray = longArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[longArrayData.offset + i] = gotArray[i];
			}
		}
		
		Method method = deserialiser.findSetter(target, variableName);
		
		if (method == null) {
			return;
		}
		
		Parameter parameters[] = method.getParameters();
		
		if (parameters[0].getType().isArray()) {
			Class<?> componentType = parameters[0].getType().getComponentType();
			
			if (componentType.equals(long.class)) {
				method.invoke(target, dataArray);
			} else if (componentType.equals(Long.class)) {
				Long longArray[] = new Long[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					longArray[i] = dataArray[i];
				}
				method.invoke(target, (Object)longArray);
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
			List<Long> list;
			if (parameters[0].getType().isInterface()) {
				list = new ArrayList<Long>();
			} else {
				list = (List) parameters[0].getType().newInstance();
			}
			for (long longValue : dataArray) {
				list.add(longValue);
			}
			method.invoke(target, list);
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Populates data from a byte array
	 * @param target The target object to populate
	 * @param variableName The name of the field to populate
	 * @param bpvField The array to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setByteArrayValue(Object target, String variableName, PVByteArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		byte dataArray[] = new byte[bpvField.getLength()];
		ByteArrayData byteArrayData = new ByteArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		byte gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(totalGot, bpvField.getLength() - totalGot, byteArrayData);
			totalGot += numGot;
			gotArray = byteArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[byteArrayData.offset + i] = gotArray[i];
			}
		}
		
		Method method = deserialiser.findSetter(target, variableName);
		
		if (method == null) {
			return;
		}
		
		Parameter parameters[] = method.getParameters();
		
		if (parameters[0].getType().isArray()) {
			Class<?> componentType = parameters[0].getType().getComponentType();
			
			if (componentType.equals(byte.class)) {
				method.invoke(target, dataArray);
			} else if (componentType.equals(Byte.class)) {
				Byte byteArray[] = new Byte[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					byteArray[i] = dataArray[i];
				}
				method.invoke(target, (Object)byteArray);
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
			List<Byte> list;
			if (parameters[0].getType().isInterface()) {
				list = new ArrayList<Byte>();
			} else {
				list = (List) parameters[0].getType().newInstance();
			}
			for (byte byteValue : dataArray) {
				list.add(byteValue);
			}
			method.invoke(target, list);
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Populates data from a boolean array
	 * @param target The target object to populate
	 * @param variableName The name of the field to populate
	 * @param bpvField The array to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setBooleanArrayValue(Object target, String variableName, PVBooleanArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		boolean dataArray[] = new boolean[bpvField.getLength()];
		BooleanArrayData booleanArrayData = new BooleanArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		boolean gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(totalGot, bpvField.getLength() - totalGot, booleanArrayData);
			totalGot += numGot;
			gotArray = booleanArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[booleanArrayData.offset + i] = gotArray[i];
			}
		}
		
		Method method = deserialiser.findSetter(target, variableName);
		
		if (method == null) {
			return;
		}
		
		Parameter parameters[] = method.getParameters();
		
		if (parameters[0].getType().isArray()) {
			Class<?> componentType = parameters[0].getType().getComponentType();
			
			if (componentType.equals(boolean.class)) {
				method.invoke(target, dataArray);
			} else if (componentType.equals(Boolean.class)) {
				Boolean booleanArray[] = new Boolean[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					booleanArray[i] = dataArray[i];
				}
				method.invoke(target, (Object)booleanArray);
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
			List<Boolean> list;
			if (parameters[0].getType().isInterface()) {
				list = new ArrayList<Boolean>();
			} else {
				list = (List) parameters[0].getType().newInstance();
			}
			for (boolean booleanValue : dataArray) {
				list.add(booleanValue);
			}
			method.invoke(target, list);
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Populates data from a float array
	 * @param target The target object to populate
	 * @param variableName The name of the field to populate
	 * @param bpvField The array to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setFloatArrayValue(Object target, String variableName, PVFloatArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		float dataArray[] = new float[bpvField.getLength()];
		FloatArrayData floatArrayData = new FloatArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		float gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(totalGot, bpvField.getLength() - totalGot, floatArrayData);
			totalGot += numGot;
			gotArray = floatArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[floatArrayData.offset + i] = gotArray[i];
			}
		}
		
		Method method = deserialiser.findSetter(target, variableName);
		
		if (method == null) {
			return;
		}
		
		Parameter parameters[] = method.getParameters();
		
		if (parameters[0].getType().isArray()) {
			Class<?> componentType = parameters[0].getType().getComponentType();
			
			if (componentType.equals(float.class)) {
				method.invoke(target, dataArray);
			} else if (componentType.equals(Float.class)) {
				Float floatArray[] = new Float[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					floatArray[i] = dataArray[i];
				}
				method.invoke(target, (Object)floatArray);
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
			List<Float> list;
			if (parameters[0].getType().isInterface()) {
				list = new ArrayList<Float>();
			} else {
				list = (List) parameters[0].getType().newInstance();
			}
			for (float floatValue : dataArray) {
				list.add(floatValue);
			}
			method.invoke(target, list);
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Populates data from a double array
	 * @param target The target object to populate
	 * @param variableName The name of the field to populate
	 * @param bpvField The array to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setDoubleArrayValue(Object target, String variableName, PVDoubleArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		double dataArray[] = new double[bpvField.getLength()];
		DoubleArrayData doubleArrayData = new DoubleArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		double gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(totalGot, bpvField.getLength() - totalGot, doubleArrayData);
			totalGot += numGot;
			gotArray = doubleArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[doubleArrayData.offset + i] = gotArray[i];
			}
		}
		
		Method method = deserialiser.findSetter(target, variableName);
		
		if (method == null) {
			return;
		}
		
		Parameter parameters[] = method.getParameters();
		
		if (parameters[0].getType().isArray()) {
			Class<?> componentType = parameters[0].getType().getComponentType();
			
			if (componentType.equals(double.class)) {
				method.invoke(target, dataArray);
			} else if (componentType.equals(Double.class)) {
				Double doubleArray[] = new Double[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					doubleArray[i] = dataArray[i];
				}
				method.invoke(target, (Object)doubleArray);
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
			List<Double> list;
			if (parameters[0].getType().isInterface()) {
				list = new ArrayList<Double>();
			} else {
				list = (List) parameters[0].getType().newInstance();
			}
			for (double doubleValue : dataArray) {
				list.add(doubleValue);
			}
			method.invoke(target, list);
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Populates data from a string or char array
	 * @param target The target object to populate
	 * @param variableName The name of the field to populate
	 * @param bpvField The array to get data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private void setStringArrayValue(Object target, String variableName, PVStringArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException {
		
		String dataArray[] = new String[bpvField.getLength()];
		StringArrayData StringArrayData = new StringArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		String gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(totalGot, bpvField.getLength() - totalGot, StringArrayData);
			totalGot += numGot;
			gotArray = StringArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[StringArrayData.offset + i] = gotArray[i];
			}
		}
		
		Method method = deserialiser.findSetter(target, variableName);
		
		if (method == null) {
			return;
		}
		
		Parameter parameters[] = method.getParameters();
		
		if (parameters[0].getType().isArray()) {
			Class<?> componentType = parameters[0].getType().getComponentType();
			
			if (componentType.equals(String.class)) {
				method.invoke(target, (Object)dataArray);
			} else if (componentType.equals(char.class)) {
				char charArray[] = new char[dataArray.length];
				for (int i = 0; i < dataArray.length; i++) {
					charArray[i] = dataArray[i].charAt(0);
				}
				method.invoke(target, (Object)charArray);
			} else if (componentType.equals(Character.class)) {
				Character charArray[] = new Character[dataArray.length];
				for (int i = 0; i < dataArray.length; i++) {
					if (dataArray[i].length() > 1) {
						throw new IllegalArgumentException(variableName + " has too many characters for a single char");
					}
					charArray[i] = dataArray[i].charAt(0);
				}
				method.invoke(target, (Object)charArray);
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
            Class<?> listClass = getListFieldClass(target, variableName);
            
            if (listClass.equals(String.class)) {
    			List<String> list;
    			if (parameters[0].getType().isInterface()) {
    				list = new ArrayList<String>();
    			} else {
    				list = (List) parameters[0].getType().newInstance();
    			}
				for (String StringValue : dataArray) {
					list.add(StringValue);
				}
				method.invoke(target, list);
			} else if (listClass.equals(Character.class)) {
				List<Character> list;
				if (parameters[0].getType().isInterface()) {
					list = new ArrayList<Character>();
				} else {
					list = (List) parameters[0].getType().newInstance();
				}
				for (String stringValue : dataArray) {
					if (stringValue.length() > 1) {
						throw new IllegalArgumentException(variableName + " has too many characters for a single char");
					}
					list.add(stringValue.charAt(0));
				}
				method.invoke(target, list);
			} else {
				throw new IllegalArgumentException("Unknown List type");
			}
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Gets the type of field of a list in a parent object
	 * @param object The object to check
	 * @param variableName The name of the field
	 * @return
	 * @throws NoSuchFieldException
	 */
	private static Class<?> getListFieldClass(Object object, String variableName) throws NoSuchFieldException {
		Class<?> clazz = object.getClass();
		while (clazz != Object.class)  {
			try {
				java.lang.reflect.Field listField = clazz.getDeclaredField(variableName);
				Type listFieldType = listField.getGenericType();
				if (listFieldType instanceof ParameterizedType) {
		            ParameterizedType listType = (ParameterizedType) listFieldType;
		            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
		            return listClass;
				} else {
					// No type info on list so assume Object
					return Object.class;
				}
			} catch (NoSuchFieldException ex) {
				
			}
		    clazz = clazz.getSuperclass();
		}
		throw new NoSuchFieldException("Unable to find field for " + variableName + " in class " + object.getClass());
	}
}
