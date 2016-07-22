package org.epics.pvmarshaller.marshaller;

import java.util.LinkedHashMap;
import java.util.Map;

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
	
	Map<Class<?>, IPVStructureSerialiser<?>> registeredSerialisers = new LinkedHashMap<Class<?>, IPVStructureSerialiser<?>>();
	Map<String, IPVStructureDeserialiser> registeredDeserialisers = new LinkedHashMap<String, IPVStructureDeserialiser>();
	Map<Class<?>, String> registeredIds = new LinkedHashMap<Class<?>, String>();
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
		PVStructure pvStructure = serialiser.toPVStructure(source, registeredSerialisers, registeredIds);

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
		return deserialiser.fromPVStructure(pvStructure, targetClass, registeredDeserialisers);
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
		registeredSerialisers.put(clazz, serialiser);
	}
	
	/**
	 * Registers an Id to use in the PVStrucutre 'Id' field for the specified class
	 * @param clazz class to use this id for
	 * @param id The id to use
	 */
	public <T> void registerIdForClass(Class<T> clazz, String id)
	{
		registeredIds.put(clazz, id);
	}	
	
	/**
	 * Registers a custom deserialiser for a parcticular class
	 * @param structureId The Id to use this custom deserialiser for
	 * @param deserialiser An instance of {@link IPVStructureDeserialiser} to use for this id
	 */
	public <T> void registerDeserialiser(String structureId, IPVStructureDeserialiser deserialiser)
	{
		registeredDeserialisers.put(structureId, deserialiser);
	}
}
