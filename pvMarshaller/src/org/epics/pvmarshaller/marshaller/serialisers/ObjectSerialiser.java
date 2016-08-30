package org.epics.pvmarshaller.marshaller.serialisers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.pv.FieldBuilder;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.api.IPVStructureSerialiser;

/**
 * Serialises an object
 * @author Matt Taylor
 *
 */
public class ObjectSerialiser {
	
	Serialiser serialiser;
	Map<Class<?>, IPVStructureSerialiser<?>> registeredSerialisers = new LinkedHashMap<Class<?>, IPVStructureSerialiser<?>>();
	Map<Class<?>, String> registeredIds = new LinkedHashMap<Class<?>, String>();
	Map<Class<?>, List<String>> registeredFields = new LinkedHashMap<Class<?>, List<String>>();
	
	/**
	 * Constructor
	 * @param serialiser
	 */
	public ObjectSerialiser(Serialiser serialiser) {
		this.serialiser = serialiser;
	}
	
	/**
	 * Creates a Structure that represents the given object
	 * @param obj The object to serialise
	 * @return
	 * @throws Exception
	 */
	public Structure buildObject(Object obj) throws Exception
	{
		Class<?> clazz = obj.getClass();
		return buildObjectFromClass(clazz, obj);
	}
	
	/**
	 * Creates a Structure that represents the given object with the specified class
	 * @param clazz The class of the object to serialise
	 * @param obj The object to serialise
	 * @return
	 * @throws Exception
	 */
	public Structure buildObjectFromClass(Class<?> clazz, Object obj) throws Exception
	{
		IPVStructureSerialiser customSerialiser = getCustomSerialiserForClass(clazz);
		
		if (customSerialiser != null) {
			return customSerialiser.buildStructure(serialiser, obj);
		} else {
			
			FieldCreate fieldCreate = FieldFactory.getFieldCreate();
	
			FieldBuilder fieldBuilder = fieldCreate.createFieldBuilder();
			
			Class<?> classToCheck = clazz;
	
			while (classToCheck != Object.class)
			{
				Field[] declaredFields = classToCheck.getDeclaredFields();
				for (Field field : declaredFields)
				{
					if (!field.getName().equals("this$0"))
					{
						if (!Modifier.isTransient(field.getModifiers())) {
							field.setAccessible(true);
							
							if (canAddFieldForObject(field, obj))
							{
								if (PrimitiveSerialiser.isPrimitive(field.getType()))
								{
									PrimitiveSerialiser.addToPVStructure(field, fieldBuilder);
								}
								else if (ContainerSerialiser.isContainer(field.getType()))
								{
									serialiser.getContainerSerialiser().addToPVStructure(field, fieldBuilder, obj);
								}
								else
								{
									field.setAccessible(true);
									Method method = findGetter(obj, field.getName());
									Object nestedObject = method.invoke(obj);
									Class<?> nestedObjectClass = nestedObject.getClass();
									
									// Check again for primitive here in case of generic class not showing up as a primitive before.
									if (PrimitiveSerialiser.isPrimitive(nestedObjectClass)) {
										PrimitiveSerialiser.addGenericToPVStructure(field, fieldBuilder, nestedObject);
									} else {
										fieldBuilder.add(field.getName(), buildObjectFromClass(nestedObjectClass, nestedObject));
									}
								}
							}
						}
					}
				}
				
				classToCheck = classToCheck.getSuperclass();
			}
			
			String idMapping = getIdMappingForClass(clazz);
			if (idMapping != null) {
				fieldBuilder.setId(idMapping);
			}
			
			// Create PVStructure from Structure
			Structure requestStructure = fieldBuilder.createStructure();
	
			return requestStructure;
		}
	}
	
	/**
	 * Populates the values in a PVStructure with the specified object
	 * @param obj The object to populate from
	 * @param pvStructure The PVStructure to populate
	 * @throws Exception
	 */
	public void setValues(Object obj, PVStructure pvStructure) throws Exception
	{
		Class<?> clazz = obj.getClass();
		
		IPVStructureSerialiser customSerialiser = getCustomSerialiserForClass(clazz);
		
		if (customSerialiser != null) {
			customSerialiser.populatePVStructure(serialiser, obj, pvStructure);
		} else {	
			// Set values in structure
			while (clazz != Object.class)
			{
				Field[] declaredFields = clazz.getDeclaredFields();
				for (Field field : declaredFields)
				{
					if (!field.getName().equals("this$0"))
					{
						if (!Modifier.isTransient(field.getModifiers())) {
							field.setAccessible(true);
							if (canAddFieldForObject(field, obj))
							{
								if (PrimitiveSerialiser.isPrimitive(field.getType()))
								{
									PrimitiveSerialiser.setFieldValue(field, pvStructure, obj);
								}
								else if (ContainerSerialiser.isContainer(field.getType()))
								{
									serialiser.getContainerSerialiser().setFieldValue(field, pvStructure, obj);
								}
								else
								{
									setObjectValue(field, pvStructure, obj);
								}
							}
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
	}
	
	/**
	 * Populates the values in a PVStructure with the specified field in the parent object
	 * @param childField The child field to get the data from
	 * @param parentStructure The PVStructure to populate
	 * @param parentObject The parent object to get the child field from
	 * @throws Exception
	 */
	private void setObjectValue(Field childField, PVStructure parentStructure, Object parentObject) throws Exception
	{		
		childField.setAccessible(true);
		Method method = findGetter(parentObject, childField.getName());
		Object childObject = method.invoke(parentObject);
		
		if (childObject != null) {
			
			Class<?> clazz = childObject.getClass();
			
			// Check again for primitive here in case of generic class not showing up as a primitive before.
			if (PrimitiveSerialiser.isPrimitive(clazz)) {
				PrimitiveSerialiser.setGenericFieldValue(childField, parentStructure, parentObject);
			} else {
				
				PVStructure childPVStructure = parentStructure.getStructureField(childField.getName());
	
				IPVStructureSerialiser customSerialiser = getCustomSerialiserForClass(clazz);
				
				if (customSerialiser != null) {
					customSerialiser.populatePVStructure(serialiser, childObject, childPVStructure);
				} else {
					Field[] declaredFields = clazz.getDeclaredFields();
					for (Field field : declaredFields)
					{
						if (!field.getName().equals("this$0"))
						{
							if (!Modifier.isTransient(field.getModifiers())) {
								if (PrimitiveSerialiser.isPrimitive(field.getType()))
								{
									PrimitiveSerialiser.setFieldValue(field, childPVStructure, childObject);
								}
								else if (ContainerSerialiser.isContainer(field.getType()))
								{
									serialiser.getContainerSerialiser().setFieldValue(field, childPVStructure, childObject);
								}
								else
								{
									setObjectValue(field, childPVStructure, childObject);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Adds a custom serialiser to the register
	 * @param customSerialisers
	 */
	public void addCustomSerialiser(Class<?> clazz, IPVStructureSerialiser<?> serialiser) {
		registeredSerialisers.put(clazz, serialiser);
	}
	
	/**
	 * Adds a custom id to class mapping to the register
	 * @param idMappings
	 */
	public void addIdMapping(Class<?> clazz, String id) {
		registeredIds.put(clazz, id);
	}
	
	/**
	 * Adds a field list to the register
	 * @param idMappings
	 */
	public void addFieldsToSerialise(Class<?> clazz, List<String> fieldsToSerialise) {
		registeredFields.put(clazz, fieldsToSerialise);
	}
	
	/**
	 * Gets the registered custom serialiser for a given class
	 * @param clazz The class to get the custom serialiser for
	 * @return the serialiser or null if there isn't one registered
	 */
	private IPVStructureSerialiser getCustomSerialiserForClass(Class<?> clazz) {
		
		IPVStructureSerialiser foundSerialiser = null;

		Class<?> classToCheck = clazz;
		
		while (classToCheck != Object.class) {
			
			if (registeredSerialisers.containsKey(classToCheck)) {
				return registeredSerialisers.get(classToCheck);
			} else {
				List<Class<?>> classInterfacesList = Arrays.asList(classToCheck.getInterfaces());
				// Check for serialiser for interface
				for (Class<?> keyClass : registeredSerialisers.keySet()) {
					if (keyClass.isInterface()) {
						if (classInterfacesList.contains(keyClass)) {
							if (foundSerialiser != null) {
								throw new IllegalArgumentException("Class has more than one custom serialiser registered for the interfaces it implements: " + classToCheck);
							}
							foundSerialiser = registeredSerialisers.get(keyClass);
						}
					}
				}
				
				if (foundSerialiser != null) {
					return foundSerialiser;
				}
				
				if (registeredSerialisers.containsKey(classToCheck)) {
					return registeredSerialisers.get(classToCheck);
				}
				
				// Check for serialiser of any base class
				classToCheck = classToCheck.getSuperclass();
			}
		}
		return null;
	}
	
	/**
	 * Gets the registered custom ID mapping for the specified class
	 * @param clazz The class to get the custom id mapping for
	 * @return The id or null if there isn't one registered
	 */
	private String getIdMappingForClass(Class<?> clazz) {
		String foundString = null;

		Class<?> classToCheck = clazz;
		
		while (classToCheck != Object.class) {
			
			if (registeredIds.containsKey(classToCheck)) {
				return registeredIds.get(classToCheck);
			} else {
				List<Class<?>> classInterfacesList = Arrays.asList(classToCheck.getInterfaces());
				// Check for id for interface
				for (Class<?> keyClass : registeredIds.keySet()) {
					if (keyClass.isInterface()) {
						if (classInterfacesList.contains(keyClass)) {
							if (foundString != null) {
								throw new IllegalArgumentException("Class has more than one id registered for the interfaces it implements: " + classToCheck);
							}
							foundString = registeredIds.get(keyClass);
						}
					}
				}
				
				if (foundString != null) {
					return foundString;
				}
				
				if (registeredIds.containsKey(classToCheck)) {
					return registeredIds.get(classToCheck);
				}
				
				// Check for id of any base class
				classToCheck = classToCheck.getSuperclass();
			}
		}
		return null;
	}
	
	/**
	 * Gets the registered custom ID mapping for the specified class
	 * @param clazz The class to get the custom id mapping for
	 * @return The id or null if there isn't one registered
	 */
	private boolean canAddFieldForClass(Class<?> clazz, String fieldName) {
		boolean foundList = false;

		Class<?> classToCheck = clazz;
		
		while (classToCheck != Object.class) {
			
			if (registeredFields.containsKey(classToCheck)) {
				foundList = true;
				List<String> fields = registeredFields.get(classToCheck);
				
				if (fields.contains(fieldName)) {
					return true;
				}
			} else {
				List<Class<?>> classInterfacesList = Arrays.asList(classToCheck.getInterfaces());
				// Check for id for interface
				for (Class<?> keyClass : registeredFields.keySet()) {
					if (keyClass.isInterface()) {
						if (classInterfacesList.contains(keyClass)) {
							foundList = true;

							List<String> fields = registeredFields.get(keyClass);
							
							if (fields.contains(fieldName)) {
								return true;
							}
						}
					}
				}
			}
			// Check for id of any base class
			classToCheck = classToCheck.getSuperclass();
		}
		
		// If we got here, then check the class or its parents or interfaces waeren't found in any list.
		// If they were then don't add field. If not in any list then can add field
		if (foundList == true) {
			return false;
		}
		
		return true;
	}

	/**
	 * Determines whether a field can be added for an object.
	 * @param field The field to check
	 * @param object The object to check the field for
	 * @return true if the field should be added for this object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private boolean canAddFieldForObject(Field field, Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		Class<?> objectClass = object.getClass();
		
		if (canAddFieldForClass(objectClass, field.getName()) == false) {
			return false;
		}
		
		Class<?> fieldType = field.getType();

		if (fieldType.equals(int.class) ||
			fieldType.equals(short.class) ||
			fieldType.equals(long.class) ||
			fieldType.equals(byte.class) ||
			fieldType.equals(boolean.class) ||
			fieldType.equals(float.class) ||
			fieldType.equals(double.class) ||
			fieldType.equals(char.class))
		{
			return true;
		} else {
			field.setAccessible(true);
			Method method = findGetter(object, field.getName());
			Object fieldObject = method.invoke(object);
			if (fieldObject != null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Finds the Getter method for the specified variable in an object
	 * @param object The object to get the getter in
	 * @param variableName The variable to get the getter for
	 * @return The getter method
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
