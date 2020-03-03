package org.epics.pvmarshaller.marshaller.serialisers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
import org.epics.pvdata.pv.Structure;

/**
 * Serialises a list
 * @author Matt Taylor
 *
 */
public class ListSerialiser {
	
	Serialiser serialiser;
	static PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
	
	/**
	 * Constructor
	 * @param serialiser
	 */
	public ListSerialiser(Serialiser serialiser) {
		this.serialiser = serialiser;
	}
	
	/**
	 * Populates the values in a primitive list
	 * @param name The name of the field
	 * @param structure The PVStructure to populate
	 * @param list The list to populate data from
	 * @param componentType The type that the list contains
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setPrimitiveListValue(String name, PVStructure structure, List<?> list, Class<?> componentType) throws IllegalArgumentException, IllegalAccessException
	{
		int numPut = 0;
		
		if (componentType.equals(Integer.class) || componentType.equals(int.class)) {
			List<Integer> value = (List<Integer>)list;
			PVIntArray array = structure.getSubField(PVIntArray.class, name);
			int[] valueAsArray = new int[value.size()];
			for (int i = 0; i < value.size(); i++)
			{
				valueAsArray[i] = value.get(i);
			}
			while (numPut < valueAsArray.length)
			{
				numPut += array.put(numPut, valueAsArray.length - numPut, valueAsArray, numPut);
			}
		} else if (componentType.equals(Short.class) || componentType.equals(short.class)) {
			List<Short> value = (List<Short>)list;
			PVShortArray array = structure.getSubField(PVShortArray.class, name);
			short[] valueAsArray = new short[value.size()];
			for (int i = 0; i < value.size(); i++)
			{
				valueAsArray[i] = value.get(i);
			}
			while (numPut < valueAsArray.length)
			{
				numPut += array.put(numPut, valueAsArray.length - numPut, valueAsArray, numPut);
			}
		} else if (componentType.equals(Long.class) || componentType.equals(long.class)) {
			List<Long> value = (List<Long>)list;
			PVLongArray array = structure.getSubField(PVLongArray.class, name);
			long[] valueAsArray = new long[value.size()];
			for (int i = 0; i < value.size(); i++)
			{
				valueAsArray[i] = value.get(i);
			}
			while (numPut < valueAsArray.length)
			{
				numPut += array.put(numPut, valueAsArray.length - numPut, valueAsArray, numPut);
			}
		} else if (componentType.equals(Byte.class) || componentType.equals(byte.class)) {
			List<Byte> value = (List<Byte>)list;
			PVByteArray array = structure.getSubField(PVByteArray.class, name);
			byte[] valueAsArray = new byte[value.size()];
			for (int i = 0; i < value.size(); i++)
			{
				valueAsArray[i] = value.get(i);
			}
			while (numPut < valueAsArray.length)
			{
				numPut += array.put(numPut, valueAsArray.length - numPut, valueAsArray, numPut);
			}
		} else if (componentType.equals(Boolean.class) || componentType.equals(boolean.class)) {
			List<Boolean> value = (List<Boolean>)list;
			PVBooleanArray array = structure.getSubField(PVBooleanArray.class, name);
			boolean[] valueAsArray = new boolean[value.size()];
			for (int i = 0; i < value.size(); i++)
			{
				valueAsArray[i] = value.get(i);
			}
			while (numPut < valueAsArray.length)
			{
				numPut += array.put(numPut, valueAsArray.length - numPut, valueAsArray, numPut);
			}
		} else if (componentType.equals(Float.class) || componentType.equals(float.class)) {
			List<Float> value = (List<Float>)list;
			PVFloatArray array = structure.getSubField(PVFloatArray.class, name);
			float[] valueAsArray = new float[value.size()];
			for (int i = 0; i < value.size(); i++)
			{
				valueAsArray[i] = value.get(i);
			}
			while (numPut < valueAsArray.length)
			{
				numPut += array.put(numPut, valueAsArray.length - numPut, valueAsArray, numPut);
			}
		} else if (componentType.equals(Double.class) || componentType.equals(double.class)) {
			List<Double> value = (List<Double>)list;
			PVDoubleArray array = structure.getSubField(PVDoubleArray.class, name);
			double[] valueAsArray = new double[value.size()];
			for (int i = 0; i < value.size(); i++)
			{
				valueAsArray[i] = value.get(i);
			}
			while (numPut < valueAsArray.length)
			{
				numPut += array.put(numPut, valueAsArray.length - numPut, valueAsArray, numPut);
			}
		} else if (componentType.equals(Character.class) || componentType.equals(char.class)) {
			List<Character> value = (List<Character>)list;
			PVStringArray array = structure.getSubField(PVStringArray.class, name);
			String[] stringArray = new String[value.size()];
			for (int i = 0; i < value.size(); i++) {
				stringArray[i] = String.valueOf(value.get(i));
			}
			while (numPut < stringArray.length)
			{
				numPut += array.put(numPut, stringArray.length - numPut, stringArray, numPut);
			}
		} else if (componentType.equals(String.class)) {
			List<String> value = (List<String>)list;
			PVStringArray array = structure.getSubField(PVStringArray.class, name);
			String[] valueAsArray = new String[value.size()];
			for (int i = 0; i < value.size(); i++)
			{
				valueAsArray[i] = value.get(i);
			}
			while (numPut < valueAsArray.length)
			{
				numPut += array.put(numPut, valueAsArray.length - numPut, valueAsArray, numPut);
			}
		} else {
			//throw new Exception();   ??????
		}
	}
	
	/**
	 * Populates the values from a list field in the parent object
	 * @param source The source list field
	 * @param structure The PVStructure
	 * @param object The parent object that contains the list field
	 * @throws Exception
	 */
	public void setObjectListValue(Field field, PVStructure structure, Object object) throws Exception
	{
		Method method = Serialiser.findGetter(object, field.getName());
		Object listObject = method.invoke(object);
		List<Object> value = (List<Object>)listObject;
		
		setObjectListValue(field.getName(), structure, value);
		
	}
	
	/**
	 * Populates the values from a list object
	 * @param name The name of the field
	 * @param structure The PVStructure to populate
	 * @param listObject The list object
	 * @throws Exception
	 */
	public void setObjectListValue(String name, PVStructure structure, Object listObject) throws Exception
	{
		List<Object> value = (List<Object>)listObject;
		
		PVUnionArray pvUnionValue = structure.getSubField(PVUnionArray.class, name);
		PVUnion[] unionArray = new PVUnion[value.size()];
		
		for (int i = 0; i < value.size(); i++) {
			PVStructure pvs = serialiser.toPVStructure(value.get(i));
			PVUnion pvUnion = pvDataCreate.createPVVariantUnion();
			pvUnion.set(pvs);
			unionArray[i] = pvUnion;
		}
		int numPut = 0;
		while (numPut < unionArray.length)
		{
			numPut += pvUnionValue.put(numPut, unionArray.length - numPut, unionArray, numPut);
		}
	}
	
	/**
	 * Populates the values from a list of maps in the parent object
	 * @param field The list field in the parent object
	 * @param structure The PVStructure to populate
	 * @param object The parent object
	 * @throws Exception
	 */
	public void setMapListValue(Field field, PVStructure structure, Object object) throws Exception
	{
		Method method = Serialiser.findGetter(object, field.getName());
		Object listObject = method.invoke(object);
		List<Object> value = (List<Object>)listObject;
		
		setMapListValue(field.getName(), structure, value);
	}
	
	/**
	 * Populates the field in a PVStrucutre with values from a list object
	 * @param name The field name
	 * @param structure The PVStructure to populate
	 * @param listObject The list object
	 * @throws Exception
	 */
	public void setMapListValue(String name, PVStructure structure, Object listObject) throws Exception
	{
		List<Object> value = (List<Object>)listObject;
		
		PVUnionArray pvUnionValue = structure.getSubField(PVUnionArray.class, name);
		PVUnion[] unionArray = new PVUnion[value.size()];
				
		for (int i = 0; i < value.size(); i++) {
			Map map = (Map)value.get(i);
			PVUnion pvUnion = pvDataCreate.createPVVariantUnion();
			Structure mapStructure = serialiser.getMapSerialiser().buildStructureFromMap(map);
			PVStructure pvs = pvDataCreate.createPVStructure(mapStructure);
			pvUnion.set(pvs);
			unionArray[i] = pvUnion;
			serialiser.getMapSerialiser().setMapValues(pvs, map);
		}
		int numPut = 0;
		while (numPut < unionArray.length)
		{
			numPut += pvUnionValue.put(numPut, unionArray.length - numPut, unionArray, numPut);
		}
	}
	
	/**
	 * Gets the class from a list by looking at the first element.
	 * N.B. This should only be used to check if it's a list of primitives or of objects
	 * as it can't be guaranteed that all objects in a list are of the same type
	 * @param list The list to check
	 * @return
	 */
	public static Class<?> getClassFromList(List<?> list) {
		Class<?> componentType = null;
		if ((list != null) && (!list.isEmpty())) {
			Object firstElement = list.get(0);
			componentType = firstElement.getClass();
			
			// check all elements are the same. If not, it's an Object List
			for (int i = 1; i < list.size(); i++) {
				if (list.get(i).getClass().equals(componentType) == false) {
					return Object.class;
				}
			}
		}
		else {
			componentType = Object.class;
		}
		return componentType;
	}
}
