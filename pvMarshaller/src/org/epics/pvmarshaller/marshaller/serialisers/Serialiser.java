package org.epics.pvmarshaller.marshaller.serialisers;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.api.IPVStructureSerialiser;

/**
 * Top level class for the serialisation of an Object into a PVStrucures
 * @author Matt Taylor
 *
 */
public class Serialiser {
	
	PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
	
	PrimitiveSerialiser primitiveSerialiser = new PrimitiveSerialiser();
	ContainerSerialiser containerSerialiser = new ContainerSerialiser(this);
	ArraySerialiser arraySerialiser = new ArraySerialiser(this);
	ListSerialiser listSerialiser = new ListSerialiser(this);
	MapSerialiser mapSerialiser = new MapSerialiser(this);
	ObjectSerialiser objectSerialiser = new ObjectSerialiser(this);
	
	/**
	 * Converts an Object into a PVStructure
	 * @param source The object to convert
	 * @return PVStructure representing the source object
	 * @throws Exception
	 */
	public PVStructure toPVStructure(Object source) throws Exception
	{
		Structure requestStructure = buildStructure(source);
		
		PVStructure pvStructure = pvDataCreate.createPVStructure(requestStructure);
		
		setValues(source, pvStructure);
		
		return pvStructure;
	}
	
	/**
	 * Creates a Structure representation of the given source Object
	 * @param source The Object to convert into a Structure
	 * @return Structure representing the source object
	 * @throws Exception
	 */
	public Structure buildStructure(Object source) throws Exception {
		return objectSerialiser.buildObject(source);
	}
	
	/**
	 * Populates the specified PVStructure with data from the specified source Object
	 * @param source The source object to populate data from
	 * @param pvStructure The PVStructure to populate data into
	 * @throws Exception
	 */
	public void setValues(Object source, PVStructure pvStructure) throws Exception {
		objectSerialiser.setValues(source, pvStructure);
	}
	
	/**
	 * Populates the specified field within the specified PVStructure with the value of the object
	 * @param pvStructure The PVStructure to populate
	 * @param fieldName The field to populate
	 * @param value The object to use to populate the PVStructure
	 * @throws Exception
	 */
	public void setFieldWithValue(PVStructure pvStructure, String fieldName, Object value) throws Exception {
		Class<?> mapValueType = value.getClass();
		
		if (PrimitiveSerialiser.isPrimitive(mapValueType)) {
			MapSerialiser.setPrimitiveMapValue(pvStructure, fieldName, value, mapValueType);
		} else if (ContainerSerialiser.isContainer(mapValueType)) {
			mapSerialiser.setContainerMapValue(pvStructure, fieldName, value, mapValueType);
		} else {
			mapSerialiser.setObjectMapValue(pvStructure, fieldName, value, mapValueType);
		}
	}
	
	/**
	 * Gets the PrimitiveSerialiser used for serialising Primitive values
	 * @return
	 */
	public PrimitiveSerialiser getPrimitiveSerialiser() {
		return primitiveSerialiser;
	}

	/**
	 * Gets the ContainerSerialiser used for serialising Containers (Collections)
	 * @return
	 */
	public ContainerSerialiser getContainerSerialiser() {
		return containerSerialiser;
	}
	
	/**
	 * Gets the ArraySerialiser used for serialising Arrays
	 * @return
	 */
	public ArraySerialiser getArraySerialiser() {
		return arraySerialiser;
	}

	/**
	 * Gets the ListSerialiser used for serialising Lists
	 * @return
	 */
	public ListSerialiser getListSerialiser() {
		return listSerialiser;
	}

	/**
	 * Gets the MapSerialiser used for serialising Maps
	 * @return
	 */
	public MapSerialiser getMapSerialiser() {
		return mapSerialiser;
	}

	/**
	 * Gets the ObjectSerialiser used for serialising Objects
	 * @return
	 */
	public ObjectSerialiser getObjectSerialiser() {
		return objectSerialiser;
	}
	
	/**
	 * Finds the getter method for the given variable in the given object
	 * @param object The object to search
	 * @param variableName The name of the variable
	 * @return The getter method
	 * @throws IllegalArgumentException
	 */
	public static Method findGetter(Object object, String variableName) throws IllegalArgumentException {
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
