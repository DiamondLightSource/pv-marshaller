package org.epics.pvmarshaller.marshaller.api;

import java.lang.reflect.InvocationTargetException;

import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.serialisers.Serialiser;

/**
 * Interface to provide a custom serialiser to convert an object into a PVStructure object
 * @author Matt Taylor
 *
 * @param <T> The class that this serialiser converts from
 */
public interface IPVStructureSerialiser<T> {
	
	/**
	 * Builds a Structure object from the specified object
	 * @param serialiser The serialiser member of the PVMarshaller
	 * @param source The object to serialise
	 * @return The Structure representation of the object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Structure buildStructure(Serialiser serialiser, T source) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Populates the specified PVStructure with data from the Object source
	 * @param serialiser The serialiser member of the PVMarshaller
	 * @param source The object to serialise
	 * @param pvStructure The PVStructure object in which to populate the data
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void populatePVStructure(Serialiser serialiser, T source, PVStructure pvStructure) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
}
