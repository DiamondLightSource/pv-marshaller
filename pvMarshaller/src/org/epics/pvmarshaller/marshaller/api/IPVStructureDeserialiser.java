package org.epics.pvmarshaller.marshaller.api;

import java.lang.reflect.InvocationTargetException;

import org.epics.pvdata.pv.PVStructure;
import org.epics.pvmarshaller.marshaller.deserialisers.Deserialiser;

/**
 * Interface to provide a custom deserialiser to convert a PVStructure object into any other object
 * @author Matt Taylor
 *
 */
public interface IPVStructureDeserialiser {
	/**
	 * Creates a new instance of an object from a PVStructure representation of that object
	 * @param deserialiser the deserialiser object of the PVMarshaller
	 * @param pvStructure the PVStructure to deserialise
	 * @return The created object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public Object fromPVStructure(Deserialiser deserialiser, PVStructure pvStructure) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException ;
}
