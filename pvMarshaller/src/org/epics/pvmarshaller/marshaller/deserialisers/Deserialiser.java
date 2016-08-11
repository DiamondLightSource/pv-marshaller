package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.epics.pvdata.pv.PVStructure;
import org.epics.pvmarshaller.marshaller.api.IPVStructureDeserialiser;

/**
 * Top level class for the deserialisation of PVStrucures into an Object
 * @author Matt Taylor
 *
 */
public class Deserialiser {
	
	MapDeserialiser mapDeserialiser = new MapDeserialiser(this);
	MapScalarArrayDeserialiser mapScalarArrayDeserialiser = new MapScalarArrayDeserialiser();
	ScalarArrayDeserialiser scalarArrayDeserialiser = new ScalarArrayDeserialiser(this);
	ScalarDeserialiser scalarDeserialiser = new ScalarDeserialiser(this);
	StructureArrayDeserialiser structureArrayDeserialiser = new StructureArrayDeserialiser(this);
	StructureDeserialiser structureDeserialiser = new StructureDeserialiser(this);
	UnionDeserialiser unionDeserialiser = new UnionDeserialiser(this);
	UnionArrayDeserialiser unionArrayDeserialiser = new UnionArrayDeserialiser(this);
	
	boolean ignoreUnknownFields = false;
	
	/**
	 * Converts the specified PVStructure into an object of the type specified
	 * @param pvStructure The PVStructure to convert
	 * @param targetClass The class of the expected output object
	 * @return An object of type T
	 * @throws Exception
	 */
	public <T> T fromPVStructure(PVStructure pvStructure, Class<T> targetClass) throws Exception {
		T deserialisedObject = (T)structureDeserialiser.createObjectFromPVStructure(pvStructure, targetClass);
 	    
		return deserialisedObject;
	}
	
	/**
	 * Converts the specified PVStructure into an object of the type specified with a given set of custom deserialisers
	 * @param pvStructure The PVStructure to convert
	 * @param targetClass The class of the expected output object
	 * @param customDeserialisers A collection of custom deserialisers
	 * @return An object of type T
	 * @throws Exception
	 */
	public <T> T fromPVStructure(PVStructure pvStructure, Class<T> targetClass, Map<String, IPVStructureDeserialiser> customDeserialisers) throws Exception {
		structureDeserialiser.setCustomDeserialisers(customDeserialisers);
		
		return fromPVStructure(pvStructure, targetClass);
	}

	/**
	 * Gets the MapDeserialiser used for converting maps
	 * @return
	 */
	public MapDeserialiser getMapDeserialiser() {
		return mapDeserialiser;
	}

	/**
	 * Gets the MapScalarArrayDeserialiser used for converting maps of scalar arrays
	 * @return
	 */
	public MapScalarArrayDeserialiser getMapScalarArrayDeserialiser() {
		return mapScalarArrayDeserialiser;
	}

	/**
	 * Gets the ScalarArrayDeserialiser used for converting scalar arrays
	 * @return
	 */
	public ScalarArrayDeserialiser getScalarArrayDeserialiser() {
		return scalarArrayDeserialiser;
	}

	/**
	 * Gets the ScalarDeserialiser used for converting Scalar fields
	 * @return
	 */
	public ScalarDeserialiser getScalarDeserialiser() {
		return scalarDeserialiser;
	}

	/**
	 * Gets the StructureArrayDeserialiser used for converting Arrays of Structures
	 * @return
	 */
	public StructureArrayDeserialiser getStructureArrayDeserialiser() {
		return structureArrayDeserialiser;
	}

	/**
	 * Gets the StructureDeserialiser used for converting Structures
	 * @return
	 */
	public StructureDeserialiser getStructureDeserialiser() {
		return structureDeserialiser;
	}

	/**
	 * Gets the UnionDeserialiser used for converting unions
	 * @return
	 */
	public UnionDeserialiser getUnionDeserialiser() {
		return unionDeserialiser;
	}

	/**
	 * Gets the UnionArrayDeserialiser used for converting union arrays
	 * @return
	 */
	public UnionArrayDeserialiser getUnionArrayDeserialiser() {
		return unionArrayDeserialiser;
	}

	/**
	 * Gets whether unknown fields should be ignored
	 * @return
	 */
	public boolean getIgnoreUnknownFields() {
		return ignoreUnknownFields;
	}

	/**
	 * Sets whether unknown fields should be ignored
	 * @param ignoreUnknownMembers
	 */
	public void setIgnoreUnknownFields(boolean ignoreUnknownMembers) {
		this.ignoreUnknownFields = ignoreUnknownMembers;
	}
	
	/**
	 * Finds the setter method for a given member in the specified Object
	 * @param object The object to find the setter in
	 * @param variableName The name of the member to find the setter for
	 * @return the Setter method, or null if no setter is found and IgnoreUnknownFields has been set to true
	 * @throws IllegalArgumentException
	 */
	public Method findSetter(Object object, String variableName) throws IllegalArgumentException {
		Class<?> clazz = object.getClass();
		while (clazz != Object.class)  {
			Method[] allMethods = clazz.getDeclaredMethods();
			 
		    for (Method m : allMethods) {
		    	if (m.getName().toLowerCase().equals("set" + variableName.toLowerCase()) && m.getParameters().length == 1) {
		    		m.setAccessible(true);
		    		return m;
		    	}
		    }
		    clazz = clazz.getSuperclass();
		}
		if (ignoreUnknownFields == false) {
			throw new IllegalArgumentException("Unable to find setter for " + variableName + " in class " + object.getClass());
		}
		return null;
	}
}
