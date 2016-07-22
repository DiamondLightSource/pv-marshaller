package org.epics.pvmarshaller.marshaller.tests;

import static org.junit.Assert.assertEquals;

import org.epics.pvdata.pv.PVStructure;

public class TestHelper {
	public static void assertPVStructuresEqual(PVStructure pvStructure1, PVStructure pvStructure2) {
		assertEquals(pvStructure1.getStructure(), pvStructure2.getStructure());
		assertEquals(pvStructure1, pvStructure2);		
	}
}
