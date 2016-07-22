package org.epics.pvmarshaller.marshaller.serialisers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.PVBooleanArray;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.PVUnionArray;

/**
 * Serialises arrays
 * @author Matt Taylor
 *
 */
public class ArraySerialiser {
	
	Serialiser serialiser;
	
	PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
	
	/**
	 * Constructor
	 * @param serialiser The parent serialiser
	 */
	public ArraySerialiser(Serialiser serialiser) {
		this.serialiser = serialiser;
	}
	
	/**
	 * Sets the values of an array containing primitives
	 * @param field The object field to get the data from
	 * @param structure The structure to populate the data with
	 * @param object The parent object to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setPrimitiveArrayValue(Field field, PVStructure structure, Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> fieldType = field.getType();
		Class<?> componentType = fieldType.getComponentType();
		Method method = findGetter(object, field.getName());
		Object arrayObject = method.invoke(object);
		if (arrayObject != null) {
			setPrimitiveArrayValue(field.getName(), componentType, structure, arrayObject);
		}
	}
	
	/**
	 * Sets the values of an array containing primitives
	 * @param name The name of the field
	 * @param componentType The type of data contained in the field
	 * @param structure The structure to populate the data with
	 * @param arrayObject The array containing the data
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setPrimitiveArrayValue(String name, Class<?> componentType, PVStructure structure, Object arrayObject) throws IllegalArgumentException, IllegalAccessException
	{
		int numPut = 0;
		
		if (componentType.equals(Integer.class) || componentType.equals(int.class)) {
			int[] value;
			if (componentType.equals(int.class)) {
				value = (int[])arrayObject;
			} else {
				Integer[] wrapperArray = (Integer[])arrayObject;
				value = new int[wrapperArray.length];
				for (int i = 0; i < wrapperArray.length; i++) {
					value[i] = wrapperArray[i];
				}
			}
			PVIntArray array = structure.getSubField(PVIntArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, value.length - numPut, value, numPut);
			}
		} else if (componentType.equals(Short.class) || componentType.equals(short.class)) {
			short[] value;
			if (componentType.equals(short.class)) {
				value = (short[])arrayObject;
			} else {
				Short[] wrapperArray = (Short[])arrayObject;
				value = new short[wrapperArray.length];
				for (int i = 0; i < wrapperArray.length; i++) {
					value[i] = wrapperArray[i];
				}
			}
			PVShortArray array = structure.getSubField(PVShortArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, value.length - numPut, value, numPut);
			}
		} else if (componentType.equals(Long.class) || componentType.equals(long.class)) {
			long[] value;
			if (componentType.equals(long.class)) {
				value = (long[])arrayObject;
			} else {
				Long[] wrapperArray = (Long[])arrayObject;
				value = new long[wrapperArray.length];
				for (int i = 0; i < wrapperArray.length; i++) {
					value[i] = wrapperArray[i];
				}
			}
			PVLongArray array = structure.getSubField(PVLongArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, value.length - numPut, value, numPut);
			}
		} else if (componentType.equals(Byte.class) || componentType.equals(byte.class)) {
			byte[] value;
			if (componentType.equals(byte.class)) {
				value  = (byte[])arrayObject;
			} else {
				Byte[] wrapperArray = (Byte[])arrayObject;
				value = new byte[wrapperArray.length];
				for (int i = 0; i < wrapperArray.length; i++) {
					value[i] = wrapperArray[i];
				}
			}
			PVByteArray array = structure.getSubField(PVByteArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, value.length - numPut, value, numPut);
			}
		} else if (componentType.equals(Boolean.class) || componentType.equals(boolean.class)) {
			boolean[] value;
			if (componentType.equals(boolean.class)) {
				value = (boolean[])arrayObject;
			} else {
				Boolean[] wrapperArray = (Boolean[])arrayObject;
				value = new boolean[wrapperArray.length];
				for (int i = 0; i < wrapperArray.length; i++) {
					value[i] = wrapperArray[i];
				}
			}
			PVBooleanArray array = structure.getSubField(PVBooleanArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, value.length - numPut, value, numPut);
			}
		} else if (componentType.equals(Float.class) || componentType.equals(float.class)) {
			float[] value;
			if (componentType.equals(float.class)) {
				value = (float[])arrayObject;
			} else {
				Float[] wrapperArray = (Float[])arrayObject;
				value = new float[wrapperArray.length];
				for (int i = 0; i < wrapperArray.length; i++) {
					value[i] = wrapperArray[i];
				}
			}
			PVFloatArray array = structure.getSubField(PVFloatArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, value.length - numPut, value, numPut);
			}
		} else if (componentType.equals(Double.class) || componentType.equals(double.class)) {
			double[] value;
			if (componentType.equals(double.class)) {
				value = (double[])arrayObject;
			} else {
				Double[] wrapperArray = (Double[])arrayObject;
				value = new double[wrapperArray.length];
				for (int i = 0; i < wrapperArray.length; i++) {
					value[i] = wrapperArray[i];
				}
			}
			PVDoubleArray array = structure.getSubField(PVDoubleArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, value.length - numPut, value, numPut);
			}
		} else if (componentType.equals(Character.class) || componentType.equals(char.class)) {
			char[] value;
			if (componentType.equals(char.class)) {
				value = (char[])arrayObject;
			} else {
				Character[] wrapperArray = (Character[])arrayObject;
				value = new char[wrapperArray.length];
				for (int i = 0; i < wrapperArray.length; i++) {
					value[i] = wrapperArray[i];
				}
			}
			String[] stringArray = new String[value.length];
			for (int i = 0; i < value.length; i++) {
				stringArray[i] = String.valueOf(value[i]);
			}
			PVStringArray array = structure.getSubField(PVStringArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, stringArray.length - numPut, stringArray, numPut);
			}
		} else if (componentType.equals(String.class)) {
			String[] value = (String[])arrayObject;
			PVStringArray array = structure.getSubField(PVStringArray.class, name);
			while (numPut < value.length)
			{
				numPut += array.put(numPut, value.length - numPut, value, numPut);
			}
		} else {
			//throw new Exception();   ??????
		}
	}
	
	/**
	 * Sets the values of an array containing objects
	 * @param field The object field to get the data from
	 * @param structure The structure to populate the data with
	 * @param object The parent object to get the data from
	 * @throws Exception
	 */
	public void setObjectArrayValue(Field field, PVStructure structure, Object object) throws Exception
	{
		Method method = findGetter(object, field.getName());
		Object fieldObject = method.invoke(object);
		Object[] value = (Object[])fieldObject;
		
		setObjectArrayValue(field.getName(), structure, value);
		
	}
	
	/**
	 * Sets the values of an array containing objects
	 * @param name The name of the field
	 * @param structure The structure to populate the data with
	 * @param arrayObject The array containing the data
	 * @throws Exception
	 */
	public void setObjectArrayValue(String name, PVStructure structure, Object arrayObject) throws Exception
	{
		Object[] value = (Object[])arrayObject;
		
		PVUnionArray pvUnionValue = structure.getSubField(PVUnionArray.class, name);
		PVUnion[] unionArray = new PVUnion[value.length];
		
		//PVStructureArray array = structure.getSubField(PVStructureArray.class, name);
		//PVStructure[] pvStructures = new PVStructure[value.length];
		
		for (int i = 0; i < value.length; i++) {
			//pvStructures[i] = pvDataCreate.createPVStructure(array.getStructureArray().getStructure());
			//PVStructure pvs = pvStructures[i];
			
			PVStructure pvs = serialiser.toPVStructure(value[i]);

			PVUnion pvUnion = pvDataCreate.createPVVariantUnion();
			
			pvUnion.set(pvs);
			//serialiser.getObjectSerialiser().setValues(value[i], pvs);
			unionArray[i] = pvUnion;
		}
		int numPut = 0;
		while (numPut < unionArray.length)
		{
			numPut += pvUnionValue.put(numPut, unionArray.length - numPut, unionArray, numPut);
		}
	}
	
	/**
	 * Helper method to find the getter Method for a named field in an object
	 * @param object The object to get the getter in
	 * @param variableName The name of the field to get the getter for
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static Method findGetter(Object object, String variableName) throws IllegalArgumentException {
		Class<?> clazz = object.getClass();
		while (clazz != Object.class)  {
			Method[] allMethods = clazz.getDeclaredMethods();
			 
		    for (Method m : allMethods) {
		    	if (m.getName().toLowerCase().equals("get" + variableName.toLowerCase()) && m.getParameters().length == 0) {
		    		m.setAccessible(true);
		    		return m;
		    	}
		    }
		    
		    // Didn't find a 'get' method, try 'is'
		    for (Method m : allMethods) {
		    	if (m.getName().toLowerCase().equals("is" + variableName.toLowerCase()) && m.getParameters().length == 0) {
		    		m.setAccessible(true);
		    		return m;
		    	}
		    }
		    
		    // Didn't find any method in this class, try the superclass
		    clazz = clazz.getSuperclass();
		}
		throw new IllegalArgumentException("Unable to find getter for " + variableName + " in class " + object.getClass());
	}
}
