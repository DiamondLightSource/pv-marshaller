package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVByte;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVFloat;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVShort;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUByte;
import org.epics.pvdata.pv.PVUInt;
import org.epics.pvdata.pv.PVULong;
import org.epics.pvdata.pv.PVUShort;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.Structure;

/**
 * Deserialises into a map object
 * @author Matt Taylor
 *
 */
public class MapDeserialiser {

	Deserialiser deserialiser;
	
	/**
	 * Constructor
	 * @param deserialiser
	 */
	public MapDeserialiser(Deserialiser deserialiser) {
		this.deserialiser = deserialiser;
	}
	
	/**
	 * Creates a map from the given PVStructure
	 * @param pvStructure PVStructure representing a map to deserialise
	 * @param parentObject Parent object of the map
	 * @param fieldName Field name of the map
	 * @return
	 * @throws Exception
	 */
	public Map createMapFromPVStructure(PVStructure pvStructure, Object parentObject, String fieldName) throws Exception {
		
		Class<?> mapClass = null;
		Type keyClass;
		Type valueClass = null;
		java.lang.reflect.Field objectField = parentObject.getClass().getDeclaredField(fieldName);
		mapClass = objectField.getType();
		ParameterizedType pt = (ParameterizedType)objectField.getGenericType();
        
		keyClass = pt.getActualTypeArguments()[0];
		valueClass = pt.getActualTypeArguments()[1];
		
		if (!keyClass.equals(String.class)) {
			throw new IllegalArgumentException("Map key type was not String");
		}
		
		return createMapFromPVStructure(pvStructure, mapClass, valueClass);
	}
	
	/**
	 * Creates a map from the given PVStructure
	 * @param pvStructure PVStructure representing a map to deserialise
	 * @param mapClass The type of map
	 * @param valueClass The value class of the map
	 * @return
	 * @throws Exception
	 */
	public Map createMapFromPVStructure(PVStructure pvStructure, Class<?> mapClass, Type valueClass) throws Exception {
		
		Map newMap;
		
		// If the declared class is just of the Map interface, then create a LinkedHashMap, else use the concrete type declared
		if (mapClass.equals(Map.class)) {
			newMap = new LinkedHashMap<>();
		} else {
			newMap = (Map) mapClass.newInstance();
		}
		
		Structure structure = pvStructure.getStructure();
		
		Field structureFields[] = structure.getFields();
		
		for (int i = 0; i < structureFields.length; i++) {
			PVField pvField = pvStructure.getSubField(structure.getFieldName(i));
			
			switch (structureFields[i].getType()) {
			case scalar:
				setScalarValue(newMap, structure.getFieldName(i), pvField, valueClass);
				break;
			case scalarArray:
				setScalarArrayValue(newMap, structure.getFieldName(i), pvField, valueClass);
				break;
			case structure:
				setStructureValue(newMap, structure.getFieldName(i), pvField, valueClass);
				break;
			case structureArray:
				setStructureArrayValue(newMap, structure.getFieldName(i), pvField, valueClass);
				break;
			case union:
				setUnionValue(newMap, structure.getFieldName(i), pvField, valueClass);
				break;
			case unionArray:
				setUnionArrayValue(newMap, structure.getFieldName(i), pvField, valueClass);
				break;
			default:
				break;
			}
		}
		
		return newMap;
	}
	
	/**
	 * Sets the values of a map from a scalar PVField
	 * @param newMap the map
	 * @param key the key in the map to populate
	 * @param pvField the field to get the data from
	 * @param valueClass the type of the value class in the map
	 */
	private static void setScalarValue(Map newMap, String key, PVField pvField, Type valueClass) {
		if (pvField instanceof PVInt) {
			if (valueClass.equals(Integer.class) || valueClass.equals(int.class) || valueClass.equals(Object.class)) {
				PVInt bpvField = (PVInt)pvField;
				newMap.put(key, bpvField.get());
			} else {
				throw new IllegalArgumentException("Attempted to put Integer into non-Integer map (" + valueClass + ")");
			}
		} else if (pvField instanceof PVShort) {
			if (valueClass.equals(Short.class) || valueClass.equals(short.class) || valueClass.equals(Object.class)) {
			PVShort bpvField = (PVShort)pvField;
			newMap.put(key, bpvField.get());
			} else {
				throw new IllegalArgumentException("Attempted to put Short into non-Short map (" + valueClass + ")");
			}
		} else if (pvField instanceof PVLong) {
			if (valueClass.equals(Long.class) || valueClass.equals(long.class) || valueClass.equals(Object.class)) {
			PVLong bpvField = (PVLong)pvField;
			newMap.put(key, bpvField.get());
			} else {
				throw new IllegalArgumentException("Attempted to put Long into non-Long map (" + valueClass + ")");
			}
		} else if (pvField instanceof PVByte) {
			if (valueClass.equals(Byte.class) || valueClass.equals(byte.class) || valueClass.equals(Object.class)) {
			PVByte bpvField = (PVByte)pvField;
			newMap.put(key, bpvField.get());
			} else {
				throw new IllegalArgumentException("Attempted to put Byte into non-Byte map (" + valueClass + ")");
			}
		} else if (pvField instanceof PVBoolean) {
			if (valueClass.equals(Boolean.class) || valueClass.equals(boolean.class) || valueClass.equals(Object.class)) {
			PVBoolean bpvField = (PVBoolean)pvField;
			newMap.put(key, bpvField.get());
			} else {
				throw new IllegalArgumentException("Attempted to put Boolean into non-Boolean map (" + valueClass + ")");
			}
		} else if (pvField instanceof PVFloat) {
			if (valueClass.equals(Float.class) || valueClass.equals(float.class) || valueClass.equals(Object.class)) {
				PVFloat bpvField = (PVFloat)pvField;
				newMap.put(key, bpvField.get());
			} else {
				throw new IllegalArgumentException("Attempted to put Float into non-Float map (" + valueClass + ")");
			}
		} else if (pvField instanceof PVDouble) {
			if (valueClass.equals(Double.class) || valueClass.equals(double.class) || valueClass.equals(Object.class)) {
				PVDouble bpvField = (PVDouble)pvField;
				newMap.put(key, bpvField.get());
			} else {
				throw new IllegalArgumentException("Attempted to put Double into non-Double map (" + valueClass + ")");
			}
		} else if (pvField instanceof PVString) {
			if (valueClass.equals(Character.class) || valueClass.equals(char.class)) {
				PVString bpvField = (PVString)pvField;
				newMap.put(key, bpvField.get().charAt(0));
			} else if (valueClass.equals(String.class) || valueClass.equals(Object.class)) {
				PVString bpvField = (PVString)pvField;
				newMap.put(key, bpvField.get());
			} else {
				throw new IllegalArgumentException("Attempted to put String/Char into non-String/Char map (" + valueClass + ")");
			} 
		}
	}
	
	/**
	 * Sets the values of a map from a scalar array PVField
	 * @param newMap the map
	 * @param key the key in the map to populate
	 * @param pvField the field to get the data from
	 * @param valueClass the type of the value class in the map
	 * @throws Exception
	 */
	private static void setScalarArrayValue(Map newMap, String key, PVField pvField, Type valueClass) throws Exception {
		// If the valueClass is Object, then it's a map of Objects, so just use a list to hold the array.
		if (valueClass == Object.class)
		{
			valueClass = List.class;
		}
		Object newObject = MapScalarArrayDeserialiser.deserialise(pvField, valueClass);
		newMap.put(key, newObject);
	}
	
	/**
	 * Sets the values of a map from a structure PVField
	 * @param newMap the map
	 * @param key the key in the map to populate
	 * @param pvField the field to get the data from
	 * @param valueClass the type of the value class in the map
	 * @throws Exception
	 */
	private void setStructureValue(Map newMap, String key, PVField pvField, Type valueClass) throws Exception {

		if (pvField instanceof PVStructure) {
			PVStructure structureField = (PVStructure)pvField;
			
			if (valueClass instanceof Class) {
				Class<?> clazz = (Class<?>)valueClass;
				Object newObject = deserialiser.getStructureDeserialiser().createObjectFromPVStructure(structureField, clazz);
				newMap.put(key, newObject);
			} else if (valueClass instanceof ParameterizedType) {
				Type rawType = ((ParameterizedType)valueClass).getRawType();
				Class<?> rawClass = (Class<?>)rawType;
				if (Map.class.isAssignableFrom(rawClass)) {
					ParameterizedType pt = (ParameterizedType)valueClass;
					
					Type innerKeyClass = pt.getActualTypeArguments()[0];
					if (!innerKeyClass.equals(String.class)) {
						throw new IllegalArgumentException("Map key type was not String");
					}
					
					Type innerValueClass = pt.getActualTypeArguments()[1];
					Object newObject = createMapFromPVStructure(structureField, rawClass, innerValueClass);
					newMap.put(key, newObject);
				} else if (List.class.isAssignableFrom(rawClass)) {
					throw new IllegalArgumentException("List type unexpectedly found when deserialising map structure");
				} else {
					throw new IllegalArgumentException("Unknown paramaterised class");
				}
			} else {
				throw new IllegalArgumentException("Unknown class type");
			}

		} else {
			throw new IllegalArgumentException("Incorrect field type: " + pvField.getClass());
		}
	}
	
	/**
	 * Sets the values of a map from a structure array PVField
	 * @param newMap the map
	 * @param key the key in the map to populate
	 * @param pvField the field to get the data from
	 * @param valueClass the type of the value class in the map
	 * @throws Exception
	 */
	private void setStructureArrayValue(Map newMap, String key, PVField pvField, Type valueClass) throws Exception {
		// If the valueClass is Object, then it's a map of Objects, so just use a list to hold the array.
		if (valueClass == Object.class)
		{
			valueClass = List.class;
		}
		Object newObject = deserialiser.getStructureArrayDeserialiser().deserialise(pvField, valueClass);
		newMap.put(key, newObject);
	}
	
	/**
	 * Sets the values of a map from a union PVField
	 * @param newMap the map
	 * @param key the key in the map to populate
	 * @param pvField the field to get the data from
	 * @param valueClass the type of the value class in the map
	 * @throws Exception
	 */
	private void setUnionValue(Map newMap, String key, PVField pvField, Type valueClass) throws Exception {
		if (pvField instanceof PVUnion) {
			PVUnion pvUnion = (PVUnion)pvField;
			if (!pvUnion.getUnion().isVariant()) {
				throw new IllegalArgumentException("Regular unions are not supported");
			}
			
			PVField unionpvField = pvUnion.get();
			if ((unionpvField instanceof PVInt) || 
					(unionpvField instanceof PVShort) ||
					(unionpvField instanceof PVLong) ||
					(unionpvField instanceof PVByte) ||
					(unionpvField instanceof PVBoolean) ||
					(unionpvField instanceof PVFloat) ||
					(unionpvField instanceof PVDouble) ||
					(unionpvField instanceof PVString) ||
					(unionpvField instanceof PVUInt) ||
					(unionpvField instanceof PVUShort) ||
					(unionpvField instanceof PVULong) ||
					(unionpvField instanceof PVUByte)) {
				setScalarValue(newMap, key, unionpvField, valueClass);
			} else if (unionpvField instanceof PVStructure) {
				setStructureValue(newMap, key, unionpvField, valueClass);
			}
		}
	}
	
	/**
	 * Sets the values of a map from a union array PVField
	 * @param newMap the map
	 * @param key the key in the map to populate
	 * @param pvField the field to get the data from
	 * @param valueClass the type of the value class in the map
	 * @throws Exception
	 */
	private void setUnionArrayValue(Map newMap, String key, PVField pvField, Type valueClass) throws Exception {
		// If the valueClass is Object, then it's a map of Objects, so just use a list to hold the array.
		if (valueClass == Object.class)
		{
			valueClass = List.class;
		}
		Object newObject = deserialiser.getUnionArrayDeserialiser().deserialise(pvField, valueClass);
		newMap.put(key, newObject);
	}

}
