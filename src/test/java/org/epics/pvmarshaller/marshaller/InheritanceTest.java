package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.*;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class InheritanceTest {

	@Test
	public void testExtends() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ExtendingClass testClass = new ExtendingClass();
		testClass.baseInteger = 27;
		testClass.extendingInteger = 84;
		testClass.baseObj = new TestObjClass();
		testClass.baseObj.objString = "Test String";
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure testObjSructure = fieldCreate.createFieldBuilder().
			add("objString", ScalarType.pvString).
			createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("extendingInteger", ScalarType.pvInt).
			add("baseInteger", ScalarType.pvInt).
			add("baseObj", testObjSructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt baseValue = expectedPVStructure.getSubField(PVInt.class, "baseInteger");
		baseValue.put(27);
		PVInt extendingValue = expectedPVStructure.getSubField(PVInt.class, "extendingInteger");
		extendingValue.put(84);
		PVStructure baseObjValue = expectedPVStructure.getStructureField("baseObj");
		PVString baseObjStringValue = baseObjValue.getSubField(PVString.class, "objString");
		baseObjStringValue.put("Test String");
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testExtendsTwice() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		SecondExtendingClass testClass = new SecondExtendingClass();
		testClass.baseInteger = 27;
		testClass.extendingInteger = 84;
		testClass.furtherExtendingInteger = 1984;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("furtherExtendingInteger", ScalarType.pvInt).
			add("extendingInteger", ScalarType.pvInt).
			add("baseInteger", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt baseValue = expectedPVStructure.getSubField(PVInt.class, "baseInteger");
		baseValue.put(27);
		PVInt extendingValue = expectedPVStructure.getSubField(PVInt.class, "extendingInteger");
		extendingValue.put(84);
		PVInt furtherExtendingValue = expectedPVStructure.getSubField(PVInt.class, "furtherExtendingInteger");
		furtherExtendingValue.put(1984);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testImplements() {
		
		// Create test class to serialise
		ContainingClass testClass = new ContainingClass();
		testClass.containingInteger = 55;
		ImplementingClass implementingObject = new ImplementingClass();
		implementingObject.implentingInteger = 123;
		
		testClass.implementingObject = implementingObject;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure implementingStructure = fieldCreate.createFieldBuilder().
				add("implentingInteger", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("containingInteger", ScalarType.pvInt).
			add("implementingObject", implementingStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt containingValue = expectedPVStructure.getSubField(PVInt.class, "containingInteger");
		containingValue.put(55);
		
		PVStructure implemetingPVStructure = expectedPVStructure.getStructureField("implementingObject");
		PVInt extendingValue = implemetingPVStructure.getSubField(PVInt.class, "implentingInteger");
		extendingValue.put(123);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	class TestObjClass {
		String objString;

		public String getObjString() {
			return objString;
		}
	}
	
	class BaseClass {
		int baseInteger;
		TestObjClass baseObj;
		
		public TestObjClass getBaseObj() {
			return baseObj;
		}

		public int getBaseInteger() {
			return baseInteger;
		}
	}
	
	class ExtendingClass extends BaseClass
	{
		int extendingInteger;

		public int getExtendingInteger() {
			return extendingInteger;
		}
	}
	
	class SecondExtendingClass extends ExtendingClass
	{
		int furtherExtendingInteger;

		public int getFurtherExtendingInteger() {
			return furtherExtendingInteger;
		}
	}
	
	interface IAnInterface {
		void doSomething();
	}
	
	class ImplementingClass implements IAnInterface {

		private int implentingInteger;
		public int getImplentingInteger() {
			return implentingInteger;
		}
		@Override
		public void doSomething() {
			// TODO Auto-generated method stub
			
		}
	}
	
	class ContainingClass {
		int containingInteger;
		IAnInterface implementingObject;
		public int getContainingInteger() {
			return containingInteger;
		}
		public IAnInterface getImplementingObject() {
			return implementingObject;
		}
	}

}
