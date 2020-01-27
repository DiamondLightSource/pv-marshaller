package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.epics.pvdata.factory.BasePVStructure;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.api.IPVStructureDeserialiser;

/**
 * Deserialises a Structure
 * @author Matt Taylor
 *
 */
public class StructureDeserialiser {
	
	Map<String, IPVStructureDeserialiser> registeredDeserialisers = new LinkedHashMap<String, IPVStructureDeserialiser>();
	Deserialiser deserialiser;
	
	/**
	 * Constructor
	 * @param deserialiser
	 */
	public StructureDeserialiser(Deserialiser deserialiser) {
		this.deserialiser = deserialiser;
	}
	
	/**
	 * Returns an object deserialised from the specified PVStructure
	 * @param pvStructure The PVStructure to deserialise
	 * @return The constructed object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public Object createObjectFromPVStructure(PVStructure pvStructure) throws Exception {
		return createObjectFromPVStructure(pvStructure, null);
	}
	
	/**
	 * Returns an object deserialised from the specified PVStructure with a given target class
	 * @param pvStructure The PVStructure to deserialise
	 * @param objectClass The class of the target object
	 * @return The constructed object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public Object createObjectFromPVStructure(PVStructure pvStructure, Class<?> objectClass) throws Exception {
		Object newObject;
		
		String id = pvStructure.getStructure().getID();
		
		if (registeredDeserialisers.containsKey(id)) {
			IPVStructureDeserialiser structureSeserialiser = registeredDeserialisers.get(id);
			newObject = structureSeserialiser.fromPVStructure(deserialiser, pvStructure);
		} else {
			if ((objectClass == null) || (objectClass == Object.class)) {
				objectClass = Map.class;
			}
		
			if (Map.class.isAssignableFrom(objectClass)) {
				return deserialiser.getMapDeserialiser().createMapFromPVStructure(pvStructure, objectClass, Object.class);
			} else {
			
				if (objectClass.isInterface()) {
					throw new IllegalArgumentException("Cannot create an instance of an interface (" + objectClass + ")");
				}
				newObject = objectClass.newInstance();
				
				Structure structure = pvStructure.getStructure();
				
				Field pvFields[] = structure.getFields();
				
				for (int i = 0; i < pvFields.length; i++) {					
					PVField pvField = pvStructure.getSubField(structure.getFieldName(i));
					
					switch (pvFields[i].getType()) {
					case scalar:
						deserialiser.getScalarDeserialiser().deserialise(newObject, structure.getFieldName(i), pvField);
						break;
					case scalarArray:
						deserialiser.getScalarArrayDeserialiser().deserialise(newObject, structure.getFieldName(i), pvField);
						break;
					case structure:
						deserialise(newObject, structure.getFieldName(i), pvField);
						break;
					case structureArray:
						deserialiser.getStructureArrayDeserialiser().deserialise(newObject, structure.getFieldName(i), pvField);
						break;
					case union:
						deserialiser.getUnionDeserialiser().deserialise(newObject, structure.getFieldName(i), pvField);
						break;
					case unionArray:
						deserialiser.getUnionArrayDeserialiser().deserialise(newObject, structure.getFieldName(i), pvField);
						break;
					default:
						break;
					}
				}
			}
		}
		return newObject;
	}
	
	/**
	 * Populates the target object with data from a PVField
	 * @param target The target object
	 * @param fieldName The name of the field to populate
	 * @param pvField The PVField to get the data from
	 * @throws Exception
	 */
	public void deserialise(Object target, String fieldName, PVField pvField) throws Exception {
		
		if (pvField instanceof BasePVStructure) {
			BasePVStructure structureField = (BasePVStructure)pvField;
			
			// Find the type of the structure to be able to deserialise it. Check name and match?
			Class<?> fieldClass = getClassFromFieldName(target, fieldName);
			if (fieldClass != null) {
				Object newObject;
				if (Map.class.isAssignableFrom(fieldClass)) {
					newObject = deserialiser.getMapDeserialiser().createMapFromPVStructure(structureField, target, fieldName);
				} else {
					newObject = createObjectFromPVStructure(structureField, fieldClass);
				}
				Method method = deserialiser.findSetter(target, fieldName);
				method.invoke(target, (Object) newObject);
			}
		}
	}
	
	/**
	 * Adds a Custom Deserialisers to the register
	 * @param customDeserialisers
	 */
	public void addCustomDeserialisers(String structureId, IPVStructureDeserialiser deserialiser) {
		registeredDeserialisers.put(structureId, deserialiser);
	}
	
	/**
	 * Gets the class from a specified field in an object
	 * @param target The object to get the field from
	 * @param fieldName The name of the field
	 * @return The class of the field
	 */
	private Class<?> getClassFromFieldName(Object target, String fieldName) {
		Class<?> foundClass = null;
		
		Class<?> currentClass = target.getClass();
		
		while (currentClass != Object.class) {
			try {
				java.lang.reflect.Field objectField = currentClass.getDeclaredField(fieldName);
				foundClass = objectField.getType();
			} catch (NoSuchFieldException e) {
			} catch (SecurityException e) {
				throw e;
			}
			currentClass = currentClass.getSuperclass();
		}

		if (foundClass == null && deserialiser.getIgnoreUnknownFields() == false) {
			throw new IllegalArgumentException("Unknown member: " + fieldName);
		}
		
		return foundClass;
	}
}
