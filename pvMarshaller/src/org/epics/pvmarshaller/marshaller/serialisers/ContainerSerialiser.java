package org.epics.pvmarshaller.marshaller.serialisers;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.pv.FieldBuilder;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Union;

/**
 * Serialises containers
 * @author Matt Taylor
 *
 */
public class ContainerSerialiser {
	
	Serialiser serialiser;
	
	/**
	 * Constructor
	 * @param serialiser
	 */
	public ContainerSerialiser(Serialiser serialiser) {
		this.serialiser = serialiser;
	}
	
	/**
	 * Returns whether the specified class is a container
	 * @param fieldType
	 * @return
	 */
	public static boolean isContainer(Class<?> fieldType)
	{
		if (fieldType.isArray() || 
			Map.class.isAssignableFrom(fieldType) || 
			Collection.class.isAssignableFrom(fieldType))
		{
			return true;
		}
		
		System.out.println("not container");

		return false;
	}
	
	/**
	 * Returns whether the specified class would be serialised into an array in the PVStructure
	 * @param fieldType
	 * @return
	 */
	public static boolean isArrayTypeContainer(Class<?> fieldType)
	{
		if (fieldType.isArray() ||
			List.class.isAssignableFrom(fieldType))
		{
			return true;
		}

		return false;
	}
	
	/**
	 * Returns whether the specified class would be serialised into an structure in the PVStructure
	 * @param fieldType
	 * @return
	 */
	public static boolean isStructureTypeContainer(Class<?> fieldType)
	{
		if (Map.class.isAssignableFrom(fieldType))
		{
			return true;
		}

		return false;
	}
	
	/**
	 * Adds a structure representing a container field to the field builder
	 * @param field The container field of the object
	 * @param fieldBuilder The fieldBuilder object with the current structure
	 * @param parentObject Object in which the container field resides
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void addToPVStructure(Field field, FieldBuilder fieldBuilder, Object parentObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String name = field.getName();
		
		Method m = Serialiser.findGetter(parentObject, field.getName());
		Object containerObject = m.invoke(parentObject);
		
		Class<?> fieldType = containerObject.getClass();
		
		if (fieldType.isArray()) {
			Class<?> componentType = fieldType.getComponentType();
			if (PrimitiveSerialiser.isPrimitive(componentType)) {
				fieldBuilder.addArray(name, PrimitiveSerialiser.getScalarType(componentType));
			} else if (isArrayTypeContainer(componentType)) {
				throw new IllegalArgumentException("PVData does not support Arrays of Arrays");
			} else if (isStructureTypeContainer(componentType)) {
				// Java doesn't support Arrays of generics so should never get here
				throw new IllegalArgumentException("Unable to create an Arrays of Maps");
			} else {
				Method method = Serialiser.findGetter(parentObject, field.getName());
				Object arrayObject = method.invoke(parentObject);
				int arrayLength = Array.getLength(arrayObject);
				if (arrayLength >  0) {
					Union union = FieldFactory.getFieldCreate().createVariantUnion();
					fieldBuilder.addArray(name, union);
				}
			}
		} else if (List.class.isAssignableFrom(fieldType)) {
			field.setAccessible(true);
			Method method = Serialiser.findGetter(parentObject, field.getName());
			Object listObject = method.invoke(parentObject);
			List<?> list = (List<?>)listObject;
			Class<?> componentType = ListSerialiser.getClassFromList(list);

			if (PrimitiveSerialiser.isPrimitive(componentType)) {
				fieldBuilder.addArray(name, PrimitiveSerialiser.getScalarType(componentType));
			} else if (isArrayTypeContainer(componentType)) {
				throw new IllegalArgumentException("PVData does not support Arrays of Arrays");
			} else {
				// All lists are union arrays
				if (list.size() > 0) {
					Union union = FieldFactory.getFieldCreate().createVariantUnion();
					fieldBuilder.addArray(name, union);
				}
			}
		} else if (Map.class.isAssignableFrom(fieldType)) {
			ParameterizedType pt = (ParameterizedType)field.getGenericType();
            Type types[] = pt.getActualTypeArguments();
            if (types.length != 2) {
            	throw new IllegalArgumentException("Incorrect map length");
            } else if (!types[0].equals(String.class)) {
            	throw new IllegalArgumentException("Map keys must be strings: " + name);
            } else {
            	field.setAccessible(true);
    			Method method = Serialiser.findGetter(parentObject, field.getName());
    			Object mapObject = method.invoke(parentObject);
				Map<String, ?> map = (Map<String, ?>)mapObject;
				Structure componentStructure = serialiser.getMapSerialiser().buildStructureFromMap(map);
				fieldBuilder.add(name, componentStructure);
            }
		} else {
			throw new IllegalArgumentException("Unsupported container type: " + fieldType);
		}
	}
	
	/**
	 * Adds a structure representing a container field to the field builder from the container object itself
	 * @param fieldType The type of the field
	 * @param name The name of the field
	 * @param fieldBuilder The fieldBuilder object
	 * @param containerObject The container object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void addToStructureWithContainerObject(Class<?> fieldType, String name, FieldBuilder fieldBuilder, Object containerObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		if (fieldType.isArray()) {
			Class<?> componentType = fieldType.getComponentType();
			if (PrimitiveSerialiser.isPrimitive(componentType)) {
				fieldBuilder.addArray(name, PrimitiveSerialiser.getScalarType(componentType));
			} else if (isArrayTypeContainer(componentType)) {
				throw new IllegalArgumentException("PVData does not support Arrays of Arrays");
			} else {
				Union union = FieldFactory.getFieldCreate().createVariantUnion();
				fieldBuilder.addArray(name, union);
			}
		} else if (List.class.isAssignableFrom(fieldType)) {
			List<?> list = (List<?>)containerObject;
			Class<?> componentType = ListSerialiser.getClassFromList(list);

			if (PrimitiveSerialiser.isPrimitive(componentType)) {
				fieldBuilder.addArray(name, PrimitiveSerialiser.getScalarType(componentType));
			} else if (isArrayTypeContainer(componentType)) {
				throw new IllegalArgumentException("PVData does not support Arrays of Arrays");
			} else {
				Union union = FieldFactory.getFieldCreate().createVariantUnion();
				fieldBuilder.addArray(name, union);
			}
		} else if (Map.class.isAssignableFrom(fieldType)) {			
            	Map<String, ?> map = (Map<String, ?>)containerObject;
				Structure componentStructure = serialiser.getMapSerialiser().buildStructureFromMap(map);
				fieldBuilder.add(name, componentStructure);
		} else {
			throw new IllegalArgumentException("Unsupported container type: " + fieldType);
		}
	}
	
	/**
	 * Populates the value of a field in the PVStructure from the field in the specified object
	 * @param field The field
	 * @param structure The PVStructure to populate
	 * @param object The parent object to get the container object from to populate with 
	 * @throws Exception
	 */
	public void setFieldValue(Field field, PVStructure structure, Object object) throws Exception {
		Method m = Serialiser.findGetter(object, field.getName());
		Object containerObject = m.invoke(object);
		
		if (containerObject != null) {
			Class<?> fieldType = containerObject.getClass();
	
			field.setAccessible(true);
			if (fieldType.isArray()) {
				setArrayFieldValue(field, structure, object);
			} else if (List.class.isAssignableFrom(fieldType)) {
				setListFieldValue(field, structure, object);
			} else if (Map.class.isAssignableFrom(fieldType)) {
				setMapFieldValue(field, structure, object);
			}
		}
	}
	
	/**
	 * Populates the value of a field in the PVStructure from the container object itself
	 * @param fieldName The field name
	 * @param structure The PVStructure to populate
	 * @param containerObject The container object
	 * @param containerType The type of the container object
	 * @throws Exception
	 */
	public void setFieldValueWithContainerObject(String fieldName, PVStructure structure, Object containerObject, Class<?> containerType) throws Exception {

		if (containerObject != null) {
			if (containerType.isArray()) {
				setArrayFieldValueWithContainerObject(fieldName, structure, containerObject);
			} else if (List.class.isAssignableFrom(containerType)) {
				setListFieldValueWithContainerObject(fieldName, structure, containerObject);
			} else if (Map.class.isAssignableFrom(containerType)) {
				setMapFieldValueWithContainerObject(fieldName, structure, containerObject);
			}
		}
	}

	/**
	 * Populates the value of an array field with a field in the given object 
	 * @param source The field
	 * @param structure The PVStructure to populate
	 * @param object The parent object
	 * @throws Exception
	 */
	private void setArrayFieldValue(Field field, PVStructure structure, Object object)
			throws Exception {
		Class<?> componentType = field.getType().getComponentType();
		
		if (PrimitiveSerialiser.isPrimitive(componentType)) {
			ArraySerialiser.setPrimitiveArrayValue(field, structure, object);
		} else if (isArrayTypeContainer(componentType)) {
			throw new IllegalArgumentException(field.getName() + " is an array of arrays.");
		} else if (isStructureTypeContainer(componentType)) {
			throw new IllegalArgumentException("Unable to create an Arrays of Maps");
		} else {
			serialiser.getArraySerialiser().setObjectArrayValue(field, structure, object);
		}
	}

	/**
	 * Populates the value of an array field in the PVStructure from the container object itself
	 * @param fieldName The field name
	 * @param structure The PVStructure to populate
	 * @param arrayObject The array object
	 * @throws Exception
	 */
	private void setArrayFieldValueWithContainerObject(String fieldName, PVStructure structure, Object arrayObject)
			throws Exception {
		
		Class<?> componentType = arrayObject.getClass().getComponentType();
		
		if (PrimitiveSerialiser.isPrimitive(componentType)) {
			ArraySerialiser.setPrimitiveArrayValue(fieldName, componentType, structure, arrayObject);
		} else if (isArrayTypeContainer(componentType)) {
			throw new IllegalArgumentException(fieldName + " is an array of arrays.");
		} else if (isStructureTypeContainer(componentType)) {
			throw new IllegalArgumentException("Unable to create an Arrays of Maps");
		} else {
			serialiser.getArraySerialiser().setObjectArrayValue(fieldName, structure, arrayObject);
		}
	}
	
	/**
	 * Populates the value of a list field with a field in the given object
	 * @param field The field
	 * @param structure The PVStructure to populate
	 * @param parentObject The parent object
	 * @throws Exception
	 */
	private void setListFieldValue(Field field, PVStructure structure, Object parentObject)
			throws Exception {

		Method method = Serialiser.findGetter(parentObject, field.getName());
		Object listObject = method.invoke(parentObject);
		List<?> list = (List<?>)listObject;
		if (list != null) {
			Class<?> componentType = ListSerialiser.getClassFromList(list);
			
			if (PrimitiveSerialiser.isPrimitive(componentType)) {
				ListSerialiser.setPrimitiveListValue(field.getName(), structure, list, componentType);
			} else if (isArrayTypeContainer(componentType)) {
				throw new IllegalArgumentException(field.getName() + " is a list of lists.");
			} else if (isStructureTypeContainer(componentType)) {
				serialiser.getListSerialiser().setMapListValue(field, structure, parentObject);
			} else {
				serialiser.getListSerialiser().setObjectListValue(field, structure, parentObject);
			}
		}
	}
	
	/**
	 * Populates the value of a list field in the PVStructure from the container object itself
	 * @param fieldName The field name
	 * @param structure The PVStructure to populate
	 * @param containerObject The list object
	 * @throws Exception
	 */
	private void setListFieldValueWithContainerObject(String fieldName, PVStructure structure, Object containerObject)
			throws Exception {
		
		List<?> list = (List<?>)containerObject;
		if (list != null) {
			Class<?> componentType = ListSerialiser.getClassFromList(list);
			
			if (PrimitiveSerialiser.isPrimitive(componentType)) {
				ListSerialiser.setPrimitiveListValue(fieldName, structure, list, componentType);
			} else if (isArrayTypeContainer(componentType)) {
				throw new IllegalArgumentException(fieldName + " is a list of lists.");
			} else if (isStructureTypeContainer(componentType)) {
				throw new IllegalArgumentException("Maps of lists of Maps are not currently supported: " + fieldName);
			} else {
				serialiser.getListSerialiser().setObjectListValue(fieldName, structure, containerObject);
			}
		}
	}
	
	/**
	 * Populates the value of a map field with a field in the given object
	 * @param field The field
	 * @param structure The PVStructure to populate
	 * @param parentObject The parent object
	 * @throws Exception
	 */
	public void setMapFieldValue(Field field, PVStructure structure, Object parentObject)
			throws Exception {

		Method method = Serialiser.findGetter(parentObject, field.getName());
		Object mapObject = method.invoke(parentObject);
		Map<String, ?> map = (Map<String, ?>)mapObject;
		if (map != null) {
			serialiser.getMapSerialiser().setMapValues(field.getName(), structure, map);
		}
	}
	
	/**
	 * Populates the value of a map field in the PVStructure from the container object itself
	 * @param name The field name
	 * @param structure The PVStructure to populate
	 * @param containerObject The list object
	 * @throws Exception
	 */
	public void setMapFieldValueWithContainerObject(String name, PVStructure structure, Object containerObject)
			throws Exception {
		
		Map<String, ?> map = (Map<String, ?>)containerObject;
		if (map != null) {
			serialiser.getMapSerialiser().setMapValues(name, structure, map);
		}
	}
}
