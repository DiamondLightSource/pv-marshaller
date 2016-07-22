package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.epics.pvdata.pv.PVBooleanArray;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.BooleanArrayData;
import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.IntArrayData;
import org.epics.pvdata.pv.LongArrayData;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.ShortArrayData;
import org.epics.pvdata.pv.StringArrayData;

/**
 * Deserialises into a map scalar array object
 * @author Matt Taylor
 *
 */
public class MapScalarArrayDeserialiser {
	
	/**
	 * Returns the value of the PVField 
	 * @param pvField The field to deserialise
	 * @param valueClass The type of container to deserialise into
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws InstantiationException
	 */
	public static Object deserialise(PVField pvField, Type valueClass) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException {
		
		if (pvField instanceof PVIntArray) {
			PVIntArray bpvField = (PVIntArray)pvField;
			return setIntArrayValue(valueClass, bpvField);
		} else if (pvField instanceof PVShortArray) {
			PVShortArray bpvField = (PVShortArray)pvField;
			return setShortArrayValue(valueClass, bpvField);
		} else if (pvField instanceof PVLongArray) {
			PVLongArray bpvField = (PVLongArray)pvField;
			return setLongArrayValue(valueClass, bpvField);
		} else if (pvField instanceof PVByteArray) {
			PVByteArray bpvField = (PVByteArray)pvField;
			return setByteArrayValue(valueClass, bpvField);
		} else if (pvField instanceof PVBooleanArray) {
			PVBooleanArray bpvField = (PVBooleanArray)pvField;
			return setBooleanArrayValue(valueClass, bpvField);
		} else if (pvField instanceof PVFloatArray) {
			PVFloatArray bpvField = (PVFloatArray)pvField;
			return setFloatArrayValue(valueClass, bpvField);
		} else if (pvField instanceof PVDoubleArray) {
			PVDoubleArray bpvField = (PVDoubleArray)pvField;
			return setDoubleArrayValue(valueClass, bpvField);
		} else if (pvField instanceof PVStringArray) {
			PVStringArray bpvField = (PVStringArray)pvField;
			return setStringArrayValue(valueClass, bpvField);
		} 
		return null;
	}
	
	/**
	 * Creates a new int container from field values
	 * @param type The type of container to create
	 * @param bpvField The field values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object setIntArrayValue(Type type, PVIntArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		int dataArray[] = new int[bpvField.getLength()];
		IntArrayData intArrayData = new IntArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		int gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(numGot, bpvField.getLength() - numGot, intArrayData);
			totalGot += numGot;
			gotArray = intArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[intArrayData.offset + i] = gotArray[i];
			}
		}
		
		if (isArray(type)) {
			Class<?> componentType = getComponentType(type);
			
			if (componentType.equals(int.class)) {
				return dataArray;
			} else if (componentType.equals(Integer.class)) {
				Integer integerArray[] = new Integer[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					integerArray[i] = dataArray[i];
				}
				return (Object)integerArray;
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (isList(type)) {
			List<Integer> list;
			Class<?> clazz = getListClass(type);
			if (clazz.isInterface()) {
				list = new LinkedList<Integer>();
			} else {
				list = (List) clazz.newInstance();
			}
			
			for (int integer : dataArray) {
				list.add(integer);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Creates a new short container from field values
	 * @param type The type of container to create
	 * @param bpvField The field values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object setShortArrayValue(Type type, PVShortArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		short dataArray[] = new short[bpvField.getLength()];
		ShortArrayData shortArrayData = new ShortArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		short gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(numGot, bpvField.getLength() - numGot, shortArrayData);
			totalGot += numGot;
			gotArray = shortArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[shortArrayData.offset + i] = gotArray[i];
			}
		}
		
		if (isArray(type)) {
			Class<?> componentType = getComponentType(type);
			
			if (componentType.equals(short.class)) {
				return dataArray;
			} else if (componentType.equals(Short.class)) {
				Short shortArray[] = new Short[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					shortArray[i] = dataArray[i];
				}
				return (Object)shortArray;
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (isList(type)) {
			List<Short> list;
			Class<?> clazz = getListClass(type);
			if (clazz.isInterface()) {
				list = new LinkedList<Short>();
			} else {
				list = (List) clazz.newInstance();
			}
			for (short shortValue : dataArray) {
				list.add(shortValue);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Creates a new long container from field values
	 * @param type The type of container to create
	 * @param bpvField The field values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object setLongArrayValue(Type type, PVLongArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		long dataArray[] = new long[bpvField.getLength()];
		LongArrayData longArrayData = new LongArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		long gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(numGot, bpvField.getLength() - numGot, longArrayData);
			totalGot += numGot;
			gotArray = longArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[longArrayData.offset + i] = gotArray[i];
			}
		}
		
		if (isArray(type)) {
			Class<?> componentType = getComponentType(type);
			
			if (componentType.equals(long.class)) {
				return dataArray;
			} else if (componentType.equals(Long.class)) {
				Long longArray[] = new Long[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					longArray[i] = dataArray[i];
				}
				return (Object)longArray;
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (isList(type)) {
			List<Long> list;
			Class<?> clazz = getListClass(type);
			if (clazz.isInterface()) {
				list = new LinkedList<Long>();
			} else {
				list = (List) clazz.newInstance();
			}
			for (long longValue : dataArray) {
				list.add(longValue);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Creates a new byte container from field values
	 * @param type The type of container to create
	 * @param bpvField The field values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object setByteArrayValue(Type type, PVByteArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		byte dataArray[] = new byte[bpvField.getLength()];
		ByteArrayData byteArrayData = new ByteArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		byte gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(numGot, bpvField.getLength() - numGot, byteArrayData);
			totalGot += numGot;
			gotArray = byteArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[byteArrayData.offset + i] = gotArray[i];
			}
		}
		
		if (isArray(type)) {
			Class<?> componentType = getComponentType(type);
			
			if (componentType.equals(byte.class)) {
				return dataArray;
			} else if (componentType.equals(Byte.class)) {
				Byte byteArray[] = new Byte[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					byteArray[i] = dataArray[i];
				}
				return (Object)byteArray;
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (isList(type)) {
			List<Byte> list;
			Class<?> clazz = getListClass(type);
			if (clazz.isInterface()) {
				list = new LinkedList<Byte>();
			} else {
				list = (List) clazz.newInstance();
			}
			for (byte byteValue : dataArray) {
				list.add(byteValue);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Creates a new boolean container from field values
	 * @param type The type of container to create
	 * @param bpvField The field values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object setBooleanArrayValue(Type type, PVBooleanArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		boolean dataArray[] = new boolean[bpvField.getLength()];
		BooleanArrayData booleanArrayData = new BooleanArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		boolean gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(numGot, bpvField.getLength() - numGot, booleanArrayData);
			totalGot += numGot;
			gotArray = booleanArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[booleanArrayData.offset + i] = gotArray[i];
			}
		}
		
		if (isArray(type)) {
			Class<?> componentType = getComponentType(type);
			
			if (componentType.equals(boolean.class)) {
				return dataArray;
			} else if (componentType.equals(Boolean.class)) {
				Boolean booleanArray[] = new Boolean[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					booleanArray[i] = dataArray[i];
				}
				return (Object)booleanArray;
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (isList(type)) {
			List<Boolean> list;
			Class<?> clazz = getListClass(type);
			if (clazz.isInterface()) {
				list = new LinkedList<Boolean>();
			} else {
				list = (List) clazz.newInstance();
			}
			for (boolean booleanValue : dataArray) {
				list.add(booleanValue);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Creates a new float container from field values
	 * @param type The type of container to create
	 * @param bpvField The field values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object setFloatArrayValue(Type type, PVFloatArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		float dataArray[] = new float[bpvField.getLength()];
		FloatArrayData floatArrayData = new FloatArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		float gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(numGot, bpvField.getLength() - numGot, floatArrayData);
			totalGot += numGot;
			gotArray = floatArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[floatArrayData.offset + i] = gotArray[i];
			}
		}

		if (isArray(type)) {
			Class<?> componentType = getComponentType(type);
			
			if (componentType.equals(float.class)) {
				return dataArray;
			} else if (componentType.equals(Float.class)) {
				Float floatArray[] = new Float[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					floatArray[i] = dataArray[i];
				}
				return (Object)floatArray;
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (isList(type)) {
			List<Float> list;
			Class<?> clazz = getListClass(type);
			if (clazz.isInterface()) {
				list = new LinkedList<Float>();
			} else {
				list = (List) clazz.newInstance();
			}
			for (float floatValue : dataArray) {
				list.add(floatValue);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Creates a new double container from field values
	 * @param type The type of container to create
	 * @param bpvField The field values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object setDoubleArrayValue(Type type, PVDoubleArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		
		double dataArray[] = new double[bpvField.getLength()];
		DoubleArrayData doubleArrayData = new DoubleArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		double gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(numGot, bpvField.getLength() - numGot, doubleArrayData);
			totalGot += numGot;
			gotArray = doubleArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[doubleArrayData.offset + i] = gotArray[i];
			}
		}
		
		if (isArray(type)) {
			Class<?> componentType = getComponentType(type);
			
			if (componentType.equals(double.class)) {
				return dataArray;
			} else if (componentType.equals(Double.class)) {
				Double doubleArray[] = new Double[dataArray.length];
				
				for (int i = 0; i < dataArray.length; i++) {
					doubleArray[i] = dataArray[i];
				}
				return (Object)doubleArray;
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (isList(type)) {
			List<Double> list;
			Class<?> clazz = getListClass(type);
			if (clazz.isInterface()) {
				list = new LinkedList<Double>();
			} else {
				list = (List) clazz.newInstance();
			}
			for (double doubleValue : dataArray) {
				list.add(doubleValue);
			}
			return list;
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Creates a new string or char container from field values
	 * @param type The type of container to create
	 * @param bpvField The field values
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object setStringArrayValue(Type type, PVStringArray bpvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException {
		
		String dataArray[] = new String[bpvField.getLength()];
		StringArrayData StringArrayData = new StringArrayData();
		
		int numGot = 0;
		int totalGot = 0;
		String gotArray[];
		
		while (totalGot < bpvField.getLength()) {
			numGot = bpvField.get(numGot, bpvField.getLength() - numGot, StringArrayData);
			totalGot += numGot;
			gotArray = StringArrayData.data;
			
			for (int i = 0; i < numGot; i++) {
				dataArray[StringArrayData.offset + i] = gotArray[i];
			}
		}
		
		if (isArray(type)) {
			Class<?> componentType = getComponentType(type);
			
			if (componentType.equals(String.class)) {
				return (Object)dataArray;
			} else if (componentType.equals(char.class)) {
				char charArray[] = new char[dataArray.length];
				for (int i = 0; i < dataArray.length; i++) {
					charArray[i] = dataArray[i].charAt(0);
				}
				return (Object)charArray;
			} else if (componentType.equals(Character.class)) {
				Character charArray[] = new Character[dataArray.length];
				for (int i = 0; i < dataArray.length; i++) {
					if (dataArray[i].length() > 1) {
						throw new IllegalArgumentException(bpvField.getFieldName() + " has too many characters for a single char");
					}
					charArray[i] = dataArray[i].charAt(0);
				}
				return (Object)charArray;
			} else {
				throw new IllegalArgumentException("Unknown setter type");
			}
		} else if (isList(type)) {
            Class<?> listClass = getListComponentClass(type);
            
            if (listClass.equals(String.class)) {
    			List<String> list;
    			Class<?> clazz = getListClass(type);
    			if (clazz.isInterface()) {
    				list = new LinkedList<String>();
    			} else {
    				list = (List) clazz.newInstance();
    			}
				for (String StringValue : dataArray) {
					list.add(StringValue);
				}
				return list;
			} else if (listClass.equals(Character.class)) {
				List<Character> list;
				Class<?> clazz = getListClass(type);
				if (clazz.isInterface()) {
					list = new LinkedList<Character>();
				} else {
					list = (List) clazz.newInstance();
				}
				for (String stringValue : dataArray) {
					if (stringValue.length() > 1) {
						throw new IllegalArgumentException(bpvField.getFieldName() + " has too many characters for a single char");
					}
					list.add(stringValue.charAt(0));
				}
				return list;
			} else {
				throw new IllegalArgumentException("Unknown List type");
			}
		} else {
			throw new IllegalArgumentException("Unsupported container type");
		}		
	}
	
	/**
	 * Returns true if the type is an array
	 * @param type
	 * @return
	 */
	private static boolean isArray(Type type) {
		if (type instanceof Class) {
			Class<?> clazz = (Class<?>)type;
			if (clazz.isArray()) {
				return true;
			}
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Class<?> clazz = (Class<?>)pt.getRawType();
			if (clazz.isArray()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the type is a list
	 * @param type
	 * @return
	 */
	private static boolean isList(Type type) {
		if (type instanceof Class) {
			Class<?> clazz = (Class<?>)type;
			if (List.class.isAssignableFrom(clazz) || clazz.equals(Collection.class)) {
				return true;
			}
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Class<?> clazz = (Class<?>)pt.getRawType();
			if (List.class.isAssignableFrom(clazz) || clazz.equals(Collection.class)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the component type
	 * @param type
	 * @return
	 */
	private static Class<?> getComponentType(Type type) {
		if (type instanceof Class) {
			Class<?> clazz = (Class<?>)type;
			return clazz.getComponentType();
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Class<?> clazz = (Class<?>)pt.getRawType();
			return clazz.getComponentType();
		}
		return null;
	}
	
	/**
	 * Gets the type of list
	 * @param type
	 * @return
	 */
	private static Class<?> getListClass(Type type) {
		if (type instanceof Class) {
			Class<?> clazz = (Class<?>)type;
			return clazz;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Class<?> clazz = (Class<?>)pt.getRawType();
			return clazz;
		}
		return null;
	}
	
	/**
	 * Gets the component class of a list
	 * @param type
	 * @return
	 */
	private static Class<?> getListComponentClass(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
            Class<?> listClass = (Class<?>) pt.getActualTypeArguments()[0];
			return listClass;
		}
		return null;
	}
}
