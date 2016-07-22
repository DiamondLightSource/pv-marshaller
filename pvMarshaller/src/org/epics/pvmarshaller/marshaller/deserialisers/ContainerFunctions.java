package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Provides useful functions for collection classes
 * @author Matt Taylor
 *
 */
public class ContainerFunctions {

	/**
	 * Returns true if the specified type is an array
	 * @param type
	 * @return
	 */
	public static boolean isArray(Type type) {
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
	 * Returns true if the specified type is a list
	 * @param type
	 * @return
	 */
	public static boolean isList(Type type) {
		if (type instanceof Class) {
			Class<?> clazz = (Class<?>)type;
			if (List.class.isAssignableFrom(clazz)) {
				return true;
			}
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Class<?> clazz = (Class<?>)pt.getRawType();
			if (List.class.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the specified type is an interface
	 * @param type
	 * @return
	 */
	public static boolean isInterface(Type type) {
		if (type instanceof Class) {
			Class<?> clazz = (Class<?>)type;
			if (clazz.isInterface()) {
				return true;
			}
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Class<?> clazz = (Class<?>)pt.getRawType();
			if (clazz.isInterface()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the component type of a class
	 * @param type
	 * @return
	 */
	public static Class<?> getComponentType(Type type) {
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
	 * Gets the type of list from the type
	 * @param type
	 * @return
	 */
	public static Class<?> getListClass(Type type) {
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
	 * Gets the type of class that is contained in the specified list
	 * @param type
	 * @return
	 */
	public static Class<?> getListComponentClass(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
            Class<?> listClass = (Class<?>) pt.getActualTypeArguments()[0];
			return listClass;
		}
		return null;
	}
	
	/**
	 * Gets the field class of a list
	 * @param object The parent object containing the list
	 * @param variableName The name of the list member
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Class<?> getListFieldClass(Object object, String variableName) throws NoSuchFieldException {
		Class<?> clazz = object.getClass();
		while (clazz != Object.class)  {
			try {
				java.lang.reflect.Field listField = clazz.getDeclaredField(variableName);
	            ParameterizedType listType = (ParameterizedType) listField.getGenericType();
	            Type type = listType.getActualTypeArguments()[0];
	            if (type instanceof Class) {
		            Class<?> listClass = (Class<?>) type; 
		            return listClass;
	            } else if (type instanceof ParameterizedType) {
	            	ParameterizedType pt = (ParameterizedType) type;
	            	Class<?> listClass = (Class<?>)pt.getRawType();
		            return listClass;
	            }
			} catch (NoSuchFieldException ex) {
				
			}
		    clazz = clazz.getSuperclass();
		}
		throw new NoSuchFieldException("Unable to find field for " + variableName + " in class " + object.getClass());
	}
	
	/**
	 * Gets the type of map from a list parent object which has a list of maps
	 * @param parentObject The parent object
	 * @param variableName The name of the list variable
	 * @return The type of the map contained in the List
	 * @throws NoSuchFieldException
	 */
	public static Type getMapTypeFromListComponentParent(Object parentObject, String variableName) throws NoSuchFieldException {
		Class<?> clazz = parentObject.getClass();
		while (clazz != Object.class)  {
			try {
				java.lang.reflect.Field listField = clazz.getDeclaredField(variableName);
	            ParameterizedType listType = (ParameterizedType) listField.getGenericType();
	            Type type = listType.getActualTypeArguments()[0];
	            if (type instanceof Class) {
		            Class<?> listClass = (Class<?>) type; 
		            return listClass;
	            } else if (type instanceof ParameterizedType) {
	            	ParameterizedType pt = (ParameterizedType) type;
	            	if (!pt.getActualTypeArguments()[0].equals(String.class)) {
	    				throw new IllegalArgumentException("Map key type was not String");
	    			}
	    			
	    			return pt.getActualTypeArguments()[1];
	            }
			} catch (NoSuchFieldException ex) {
				
			}
		    clazz = clazz.getSuperclass();
		}
		throw new NoSuchFieldException("Unable to find field for " + variableName + " in class " + parentObject.getClass());
	}
}
