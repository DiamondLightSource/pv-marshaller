package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVStructureArray;
import org.epics.pvdata.pv.StructureArrayData;

/**
 * Deserialises a Structure Array
 * @author Matt Taylor
 *
 */
public class StructureArrayDeserialiser {
	
	Deserialiser deserialiser;
	
	/**
	 * Cosntructor
	 * @param deserialiser
	 */
	public StructureArrayDeserialiser(Deserialiser deserialiser) {
		this.deserialiser = deserialiser;
	}
	
	/**
	 * Populates the value of the target object from the PVField
	 * @param target The target object
	 * @param fieldName The field name
	 * @param pvField The PVField to get the data from
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws InstantiationException
	 */
	public void deserialise(Object target, String fieldName, PVField pvField) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException {
		
		if (pvField instanceof PVStructureArray) {
			PVStructureArray structureArrayField = (PVStructureArray)pvField;
			
			PVStructure dataArray[] = new PVStructure[structureArrayField.getLength()];
			StructureArrayData structureArrayData = new StructureArrayData();
			
			int numGot = 0;
			int totalGot = 0;
			PVStructure gotArray[];
			
			while (totalGot < structureArrayField.getLength()) {
				numGot = structureArrayField.get(numGot, structureArrayField.getLength() - numGot, structureArrayData);
				totalGot += numGot;
				gotArray = structureArrayData.data;
				
				for (int i = 0; i < numGot; i++) {
					dataArray[structureArrayData.offset + i] = gotArray[i];
				}
			}
			
			Method method = deserialiser.findSetter(target, fieldName);
			
			Parameter parameters[] = method.getParameters();
			
			if (parameters[0].getType().isArray()) {
				Class<?> componentType = parameters[0].getType().getComponentType();
				System.out.println(componentType);
				
				Object newArray[] = (Object[])Array.newInstance(componentType, dataArray.length);
				
				for (int i = 0; i < dataArray.length; i++) {
					PVStructure arrayPVStructure = dataArray[i];
					Object newObject = deserialiser.getStructureDeserialiser().createObjectFromPVStructure(arrayPVStructure, componentType);
					newArray[i] = newObject;
				}
				
				method.invoke(target, (Object)newArray);
				
			} else if (List.class.isAssignableFrom(parameters[0].getType()) || parameters[0].getType().equals(Collection.class)) {
	            Class<?> listClass = ContainerFunctions.getListFieldClass(target, fieldName);

				List list;
				if (parameters[0].getType().isInterface()) {
					list = new LinkedList<>();
				} else {
					list = (List) parameters[0].getType().newInstance();
				}
				
				for (int i = 0; i < dataArray.length; i++) {
					PVStructure arrayPVStructure = dataArray[i];
					Object newObject;
					if (Map.class.isAssignableFrom(listClass)) {
						Type componentType = ContainerFunctions.getMapTypeFromListComponentParent(target, fieldName);
						newObject = deserialiser.getMapDeserialiser().createMapFromPVStructure(arrayPVStructure, listClass, componentType);
					} else {
						newObject = deserialiser.getStructureDeserialiser().createObjectFromPVStructure(arrayPVStructure, listClass);
					}
					list.add(newObject);
				}
				method.invoke(target, list);
			} else {
				throw new IllegalArgumentException("Unsupported container type");
			}
		}
	}
	
	/**
	 * Gets the value of a PVField given the specified class of the contianer
	 * @param pvField The PVField to get the data from
	 * @param valueClass The type of container to return
	 * @return A container object representing the deserialised PVField
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws InstantiationException
	 */
	public Object deserialise(PVField pvField, Type valueClass) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException {
		
		if (pvField instanceof PVStructureArray) {
			PVStructureArray structureArrayField = (PVStructureArray)pvField;
			
			PVStructure dataArray[] = new PVStructure[structureArrayField.getLength()];
			StructureArrayData structureArrayData = new StructureArrayData();
			
			int numGot = 0;
			int totalGot = 0;
			PVStructure gotArray[];
			
			while (totalGot < structureArrayField.getLength()) {
				numGot = structureArrayField.get(numGot, structureArrayField.getLength() - numGot, structureArrayData);
				totalGot += numGot;
				gotArray = structureArrayData.data;
				
				for (int i = 0; i < numGot; i++) {
					dataArray[structureArrayData.offset + i] = gotArray[i];
				}
			}
			
			if (ContainerFunctions.isArray(valueClass)) {
				Class<?> componentType = ContainerFunctions.getComponentType(valueClass);
				System.out.println(componentType);
				
				Object newArray[] = (Object[])Array.newInstance(componentType, dataArray.length);
				
				for (int i = 0; i < dataArray.length; i++) {
					PVStructure arrayPVStructure = dataArray[i];
					Object newObject = deserialiser.getStructureDeserialiser().createObjectFromPVStructure(arrayPVStructure, componentType);
					newArray[i] = newObject;
				}
				
				return (Object)newArray;
				
			} else if (ContainerFunctions.isList(valueClass) || valueClass.equals(Collection.class)) {
	            Class<?> listClass = ContainerFunctions.getListClass(valueClass);
	            Class<?> componentType = ContainerFunctions.getListComponentClass(valueClass);

				List list;
				if (ContainerFunctions.isInterface(listClass)) {
					list = new LinkedList<>();
				} else {
					list = (List) ((Class<?>)listClass).newInstance();
				}
				
				for (int i = 0; i < dataArray.length; i++) {
					PVStructure arrayPVStructure = dataArray[i];
					Object newObject = deserialiser.getStructureDeserialiser().createObjectFromPVStructure(arrayPVStructure, componentType);
					list.add(newObject);
				}
				return list;
			} else {
				throw new IllegalArgumentException("Unsupported container type");
			}
		}
		return null;
	}
}
