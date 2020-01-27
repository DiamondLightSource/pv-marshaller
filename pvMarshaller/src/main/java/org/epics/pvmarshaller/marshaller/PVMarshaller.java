package org.epics.pvmarshaller.marshaller;

import java.util.List;

import org.epics.pvdata.pv.PVStructure;
import org.epics.pvmarshaller.marshaller.api.IPVStructureDeserialiser;
import org.epics.pvmarshaller.marshaller.api.IPVStructureSerialiser;
import org.epics.pvmarshaller.marshaller.deserialisers.Deserialiser;
import org.epics.pvmarshaller.marshaller.serialisers.Serialiser;

/**
 * Class to provide the ability to convert any Object into a PVStructure representing that object and back
 * @author Matt Taylor
 *
 */
public class PVMarshaller {
	
	Serialiser serialiser = new Serialiser();
	Deserialiser deserialiser = new Deserialiser();
	
	/**
	 * Creates a PVStructure representation of the supplied Object
	 * @param source The object to serialise
	 * @return The PVStructure representation of the object
	 * @throws Exception
	 */
	public PVStructure toPVStructure(Object source) throws Exception
	{
		PVStructure pvStructure = serialiser.toPVStructure(source);

		return pvStructure;
	}

	/**
	 * Creates an Object of specific type from the supplied PVStructure
	 * @param pvStructure The PVStructure to deserialise
	 * @param targetClass The class of the expected return object
	 * @return The object
	 * @throws Exception
	 */
	public <T> T fromPVStructure(PVStructure pvStructure, Class<T> targetClass) throws Exception
	{
		return deserialiser.fromPVStructure(pvStructure, targetClass);
	}
	
	/**
	 * Populates the specified field within the specified PVStructure with the value of the object
	 * @param pvStructure The PVStructure to populate
	 * @param fieldName The field to populate
	 * @param value The object to use to populate the PVStructure
	 * @throws Exception
	 */
	public void setFieldWithValue(PVStructure pvStructure, String fieldName, Object value) throws Exception {
		serialiser.setFieldWithValue(pvStructure, fieldName, value);
	}
	
	/**
	 * Deserialises and returns a single field within a Structure
	 * @param pvStructure The structure to get the object from
	 * @param fieldName The field to deserialise
	 * @return The deserialised object
	 * @throws Exception
	 */
	public Object getObjectFromField(PVStructure pvStructure, String fieldName) throws Exception {
		return deserialiser.getObjectFromField(pvStructure, fieldName);
	}
	
	/**
	 * Sets whether fields that exist in a PVStructure but not in the object will cause an exception
	 * to be thrown during deserialisation. (False by default)
	 * @param ignore True if unknown fields should be ignored
	 */
	public void setIgnoreUnknownFields(boolean ignore) {
		deserialiser.setIgnoreUnknownFields(ignore);
	}
	
	/**
	 * Registers a custom serialiser for a parcticular class
	 * @param clazz The class to use this custom serialiser for
	 * @param serialiser An instance of {@link IPVStructureSerialiser} to use for this class
	 */
	public <T> void registerSerialiser(Class<T> clazz, IPVStructureSerialiser<T> serialiser)
	{
		this.serialiser.getObjectSerialiser().addCustomSerialiser(clazz, serialiser);
	}
	
	/**
	 * Registers an Id to use in the PVStrucutre 'Id' field for the specified class
	 * @param clazz class to use this id for
	 * @param id The id to use
	 */
	public <T> void registerIdForClass(Class<T> clazz, String id)
	{
		this.serialiser.getObjectSerialiser().addIdMapping(clazz, id);
	}	
	
	/**
	 * Registers a custom deserialiser for a particular class
	 * @param structureId The Id to use this custom deserialiser for
	 * @param deserialiser An instance of {@link IPVStructureDeserialiser} to use for this id
	 */
	public <T> void registerDeserialiser(String structureId, IPVStructureDeserialiser deserialiser)
	{
		this.deserialiser.getStructureDeserialiser().addCustomDeserialisers(structureId, deserialiser);
	}
	
	/**
	 * Registers a list of fields to serialise for a particular class
	 * @param clazz The class to register the list for
	 * @param fieldsToSerialise The list of fields to serialise
	 */
	public void registerFieldListForClass(Class<?> clazz, List<String> fieldsToSerialise)
	{
		this.serialiser.getObjectSerialiser().addFieldsToSerialise(clazz, fieldsToSerialise);
	}
	
	/**
	 * Registers a list of fields to exclude from serialisation for a particular class
	 * @param clazz The class to register the list for
	 * @param fieldsToExclude The list of fields to exclude
	 */
	public void registerExcludeFieldListForClass(Class<?> clazz, List<String> fieldsToExclude)
	{
		this.serialiser.getObjectSerialiser().addFieldsToExclude(clazz, fieldsToExclude);
	}
	
	/**
	 * Registers the key to use as the type id for Maps.
	 * The key will be used to set the ID of the PVStructure, and will not be included in the PVStructure as a field
	 * @param mapTypeIdKey The key to use
	 */
	public void registerMapTypeIdKey(String mapTypeIdKey) {
		serialiser.getMapSerialiser().setMapTypeIdKey(mapTypeIdKey);
		deserialiser.getMapDeserialiser().setMapTypeIdKey(mapTypeIdKey);
	}
}
