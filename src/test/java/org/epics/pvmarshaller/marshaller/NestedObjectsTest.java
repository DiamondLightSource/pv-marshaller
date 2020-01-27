package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.*;

import org.epics.pvdata.factory.FieldFactory;

import org.epics.pvdata.factory.PVDataFactory;

import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVField;

import org.epics.pvdata.pv.PVDataCreate;

import org.epics.pvdata.pv.PVInt;

import org.epics.pvdata.pv.PVStructure;

import org.epics.pvdata.pv.ScalarType;

import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Ignore;
import org.junit.Test;

public class NestedObjectsTest {

	@Test
	public void testNestedClass() {
		PVMarshaller marshaller = new PVMarshaller();

		ParentClass testClass = new ParentClass();
		testClass.parentIntValue = 12;
		testClass.childObject = new ChildClass();
		testClass.childObject.childIntValue = 99;
		testClass.childObject.leaf = new LeafClass();
		testClass.childObject.leaf.leafIntValue = 777;

		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Structure leafStructure = fieldCreate.createFieldBuilder().
				add("leafIntValue", ScalarType.pvInt).
				createStructure();
		
		Structure childStructure = fieldCreate.createFieldBuilder().
				add("childIntValue", ScalarType.pvInt).
				add("leaf", leafStructure).
				createStructure();
		
		Structure parentStructure = fieldCreate.createFieldBuilder().
		add("parentIntValue", ScalarType.pvInt).
		add("childObject", childStructure).
		createStructure();
		
		
		PVStructure parentPVStructure = pvDataCreate.createPVStructure(parentStructure);
		PVInt parentIntValue = parentPVStructure.getSubField(PVInt.class, "parentIntValue");
		parentIntValue.put(12);
		
		PVStructure childPVStructure = parentPVStructure.getStructureField("childObject");
		PVInt childIntValue = childPVStructure.getSubField(PVInt.class, "childIntValue");
		childIntValue.put(99);

		PVStructure leafPVStructure = childPVStructure.getStructureField("leaf");
		PVInt leafIntValue = leafPVStructure.getSubField(PVInt.class, "leafIntValue");
		leafIntValue.put(777);
		
		try {

			PVStructure pvRequest1Serialised = marshaller.toPVStructure(testClass);
			
			assertEquals(parentPVStructure, pvRequest1Serialised);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNestedNullObject() {

		PVMarshaller marshaller = new PVMarshaller();

		ParentClass testClass = new ParentClass();
		testClass.parentIntValue = -45643;
		testClass.childObject = null;
		
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure deviceStructure1 = fieldCreate.createFieldBuilder().
		add("parentIntValue", ScalarType.pvInt).
		createStructure();
		
		PVStructure pvRequest1 = pvDataCreate.createPVStructure(deviceStructure1);

		PVInt pvDouble = pvRequest1.getSubField(PVInt.class, "parentIntValue");
		pvDouble.put(-45643);

		try {

			PVStructure pvRequest1Serialised = marshaller.toPVStructure(testClass);
			
			assertEquals(pvRequest1, pvRequest1Serialised);
			
			boolean equal = PVEquals(pvRequest1, pvRequest1Serialised);
			
			assertEquals(pvRequest1, pvRequest1Serialised);
			assertTrue(equal);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPVStructureEqualityWithValues() {
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure1 = fieldCreate.createFieldBuilder().
				add("x", ScalarType.pvInt).
				createStructure();
				
		PVStructure pvStructure1 = pvDataCreate.createPVStructure(structure1);
		
		Structure structure2 = fieldCreate.createFieldBuilder().
				add("y", ScalarType.pvInt).
				createStructure();
				
		PVStructure pvStructure2 = pvDataCreate.createPVStructure(structure2);
		
		PVInt structure1Value = pvStructure1.getSubField(PVInt.class, "x");
		structure1Value.put(3);
		PVInt structure2Value = pvStructure2.getSubField(PVInt.class, "y");
		structure2Value.put(4);
		
		System.out.println("pvStructure1 = \n" + pvStructure1);
		System.out.println("pvStructure2 = \n" + pvStructure2);
		
		boolean StructureEquals = structure1.equals(structure2);
		boolean PVStructureEquals = pvStructure1.equals(pvStructure2);
		
		System.out.println("Structures equals = " + StructureEquals);
		System.out.println("PVStructures equals = " + PVStructureEquals);
		
		assertNotEquals(structure1, structure2);
		assertNotEquals(pvStructure1, pvStructure2);
	}
	
	@Test
	@Ignore
	public void testCircularReference() {
		CircularReferenceClass1 testObject = new CircularReferenceClass1();
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			marshaller.toPVStructure(testObject);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public boolean PVEquals(PVStructure obj1, PVStructure obj2) {
		// TODO anything else?
		
			final PVField[] bfields = obj2.getPVFields(); 
			if (bfields.length == obj1.getPVFields().length)
			{
		        for (int i = 0; i < obj1.getPVFields().length; i++)
		        	if (!obj1.getPVFields()[i].equals(bfields[i]))
		        		return false;
		        
		        return true;
			}
			else
				return false;
	}

	class ParentClass
	{
		int parentIntValue;
		ChildClass childObject;
		public int getParentIntValue() {
			return parentIntValue;
		}
		public ChildClass getChildObject() {
			return childObject;
		}
	}
	
	class ChildClass
	{
		int childIntValue;
		LeafClass leaf;
		public int getChildIntValue() {
			return childIntValue;
		}
		public LeafClass getLeaf() {
			return leaf;
		}
	}

	
	class LeafClass
	{
		int leafIntValue;

		public int getLeafIntValue() {
			return leafIntValue;
		}
	}
	
	public static class CircularReferenceClass1 {
		CircularRefernceClass2 crc2 = new CircularRefernceClass2();

		public CircularRefernceClass2 getCrc2() {
			return crc2;
		}
	}
	
	public static class CircularRefernceClass2 {
		CircularReferenceClass1 crc1 = new CircularReferenceClass1();

		public CircularReferenceClass1 getCrc1() {
			return crc1;
		}
	}
		
}
