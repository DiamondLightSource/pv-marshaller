package org.epics.pvmarshaller.marshaller.deserialisers;

import java.lang.reflect.InvocationTargetException;

import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVByte;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVField;
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
import org.epics.pvdata.pv.PVUnion;

/**
 * Deserialises a Union
 * @author Matt Taylor
 *
 */
public class UnionDeserialiser {
	Deserialiser deserialiser;
	
	/**
	 * Constructor
	 * @param deserialiser
	 */
	public UnionDeserialiser(Deserialiser deserialiser) {
		this.deserialiser = deserialiser;
	}
	
	/**
	 * Populates the target object with data from a Union PVField
	 * @param target The target object
	 * @param fieldName The name of the field to populate
	 * @param pvField The PVField to get the data from
	 * @throws Exception
	 */
	public void deserialise(Object target, String fieldName, PVField pvField) throws Exception {
		
		if (pvField instanceof PVUnion) {
			System.out.println("Was union");
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
				deserialiser.getScalarDeserialiser().deserialise(target, fieldName, unionpvField);
			} else if (unionpvField instanceof PVStructure) {
				deserialiser.getStructureDeserialiser().deserialise(target, fieldName, unionpvField);
			}
		}
	}
}
