package org.epics.pvmarshaller.marshaller.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVStructureArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class DeserialiseInheritanceTests {

	@Test
	public void testDeserialiseExtends() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
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
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVInt baseValue = testPVStructure.getSubField(PVInt.class, "baseInteger");
		baseValue.put(27);
		PVInt extendingValue = testPVStructure.getSubField(PVInt.class, "extendingInteger");
		extendingValue.put(84);
		PVStructure baseObjValue = testPVStructure.getStructureField("baseObj");
		PVString baseObjStringValue = baseObjValue.getSubField(PVString.class, "objString");
		baseObjStringValue.put("TestString");
		
		// Create object expected 
		ExtendingClass expectedObject = new ExtendingClass();
		expectedObject.baseInteger = 27;
		expectedObject.extendingInteger = 84;
		expectedObject.baseObj = new TestObjClass();
		expectedObject.baseObj.objString = "TestString";
		
		ExtendingClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, ExtendingClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testExtendsTwice() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		SecondExtendingClass expectedObject = new SecondExtendingClass();
		expectedObject.baseInteger = 27;
		expectedObject.extendingInteger = 84;
		expectedObject.furtherExtendingInteger = 1984;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("furtherExtendingInteger", ScalarType.pvInt).
			add("extendingInteger", ScalarType.pvInt).
			add("baseInteger", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt baseValue = testPVStructure.getSubField(PVInt.class, "baseInteger");
		baseValue.put(27);
		PVInt extendingValue = testPVStructure.getSubField(PVInt.class, "extendingInteger");
		extendingValue.put(84);
		PVInt furtherExtendingValue = testPVStructure.getSubField(PVInt.class, "furtherExtendingInteger");
		furtherExtendingValue.put(1984);
		
		SecondExtendingClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, SecondExtendingClass.class);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testImplementsFails() {
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure implementingStructure = fieldCreate.createFieldBuilder().
				add("implentingInteger", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("containingInteger", ScalarType.pvInt).
			add("implementingObject", implementingStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt containingValue = testPVStructure.getSubField(PVInt.class, "containingInteger");
		containingValue.put(55);
		
		PVStructure implemetingPVStructure = testPVStructure.getStructureField("implementingObject");
		PVInt extendingValue = implemetingPVStructure.getSubField(PVInt.class, "implentingInteger");
		extendingValue.put(123);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(testPVStructure);
		
		try {
			// Expect failure as can't instantiate an instance of an interface, and no deserialiser specified in structure id
			PVMarshaller marshaller = new PVMarshaller();
			marshaller.fromPVStructure(testPVStructure, ContainingClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("Cannot create an instance of an interface"));
		}
	}
	
	@Test
	public void testDeserialiseArrayOfInterfacess() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("implentingInteger", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("arrayOfInterfaces", nestedStructure).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "implentingInteger");
		primitiveValue1.put(123);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "implentingInteger");
		primitiveValue2.put(456);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructureArray primitiveValue = testPVStructure.getSubField(PVStructureArray.class, "arrayOfInterfaces");
		PVStructure[] intArray = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		primitiveValue.put(0, 2, intArray, 0);
		

		// Create expected object
		ArrayOfInterfacesClass expectedObject = new ArrayOfInterfacesClass();

		expectedObject.arrayOfInterfaces = new IAnInterface[2];
		
		ImplementingClass testObject1 = new ImplementingClass();
		testObject1.implentingInteger = 123;
		
		ImplementingClass testObject2 = new ImplementingClass();
		testObject2.implentingInteger = 456;
		
		expectedObject.arrayOfInterfaces[0] = testObject1;
		expectedObject.arrayOfInterfaces[1] = testObject2;
				
		try {
			marshaller.fromPVStructure(testPVStructure, ArrayOfInterfacesClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("Cannot create an instance of an interface"));
		}
	}
	
	@Test
	public void testDeserialiseListOfInterfacess() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("implentingInteger", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("arrayOfInterfaces", nestedStructure).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "implentingInteger");
		primitiveValue1.put(789);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "implentingInteger");
		primitiveValue2.put(101112);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructureArray primitiveValue = testPVStructure.getSubField(PVStructureArray.class, "arrayOfInterfaces");
		PVStructure[] intArray = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		primitiveValue.put(0, 2, intArray, 0);
		

		// Create expected object
		ListOfInterfacesClass expectedObject = new ListOfInterfacesClass();

		expectedObject.listOfInterfaces = new LinkedList<IAnInterface>();
		
		ImplementingClass testObject1 = new ImplementingClass();
		testObject1.implentingInteger = 789;
		
		ImplementingClass testObject2 = new ImplementingClass();
		testObject2.implentingInteger = 101112;
		
		expectedObject.listOfInterfaces.add(testObject1);
		expectedObject.listOfInterfaces.add(testObject2);
				
		try {
			marshaller.fromPVStructure(testPVStructure, ArrayOfInterfacesClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("Cannot create an instance of an interface"));
		}
	}
	
	public static class TestObjClass {
		String objString;

		public String getObjString() {
			return objString;
		}

		public void setObjString(String objString) {
			this.objString = objString;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((objString == null) ? 0 : objString.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestObjClass other = (TestObjClass) obj;
			if (objString == null) {
				if (other.objString != null)
					return false;
			} else if (!objString.equals(other.objString))
				return false;
			return true;
		}
		
	}
	
	public static class BaseClass {
		int baseInteger;
		TestObjClass baseObj;
		
		public TestObjClass getBaseObj() {
			return baseObj;
		}

		public void setBaseObj(TestObjClass baseObj) {
			this.baseObj = baseObj;
		}

		public int getBaseInteger() {
			return baseInteger;
		}

		public void setBaseInteger(int baseInteger) {
			this.baseInteger = baseInteger;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + baseInteger;
			result = prime * result + ((baseObj == null) ? 0 : baseObj.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BaseClass other = (BaseClass) obj;
			if (baseInteger != other.baseInteger)
				return false;
			if (baseObj == null) {
				if (other.baseObj != null)
					return false;
			} else if (!baseObj.equals(other.baseObj))
				return false;
			return true;
		}

	}
	
	public static class ExtendingClass extends BaseClass
	{
		int extendingInteger;

		public int getExtendingInteger() {
			return extendingInteger;
		}

		public void setExtendingInteger(int extendingInteger) {
			this.extendingInteger = extendingInteger;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + extendingInteger;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			ExtendingClass other = (ExtendingClass) obj;
			if (extendingInteger != other.extendingInteger)
				return false;
			return true;
		}
	}
	
	public static class SecondExtendingClass extends ExtendingClass
	{
		int furtherExtendingInteger;

		public int getFurtherExtendingInteger() {
			return furtherExtendingInteger;
		}

		public void setFurtherExtendingInteger(int furtherExtendingInteger) {
			this.furtherExtendingInteger = furtherExtendingInteger;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + furtherExtendingInteger;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			SecondExtendingClass other = (SecondExtendingClass) obj;
			if (furtherExtendingInteger != other.furtherExtendingInteger)
				return false;
			return true;
		}
	}
	
	public static interface IAnInterface {
		void doSomething();

		@Override
		int hashCode();

		@Override
		boolean equals(Object obj);
	}
	
	public static class ImplementingClass implements IAnInterface {

		private int implentingInteger;
		public int getImplentingInteger() {
			return implentingInteger;
		}
		public void setImplentingInteger(int implentingInteger) {
			this.implentingInteger = implentingInteger;
		}
		@Override
		public void doSomething() {
			
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + implentingInteger;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ImplementingClass other = (ImplementingClass) obj;
			if (implentingInteger != other.implentingInteger)
				return false;
			return true;
		}
	}
	
	public static class ContainingClass {
		int containingInteger;
		public int getContainingInteger() {
			return containingInteger;
		}
		public void setContainingInteger(int containingInteger) {
			this.containingInteger = containingInteger;
		}
		IAnInterface implementingObject;
		public IAnInterface getImplementingObject() {
			return implementingObject;
		}
		public void setImplementingObject(IAnInterface implementingObject) {
			this.implementingObject = implementingObject;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + containingInteger;
			result = prime * result + ((implementingObject == null) ? 0 : implementingObject.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ContainingClass other = (ContainingClass) obj;
			if (containingInteger != other.containingInteger)
				return false;
			if (implementingObject == null) {
				if (other.implementingObject != null)
					return false;
			} else if (!implementingObject.equals(other.implementingObject))
				return false;
			return true;
		}
	}

	public static class ArrayOfInterfacesClass {
		IAnInterface arrayOfInterfaces[];

		public IAnInterface[] getArrayOfInterfaces() {
			return arrayOfInterfaces;
		}

		public void setArrayOfInterfaces(IAnInterface[] arrayOfInterfaces) {
			this.arrayOfInterfaces = arrayOfInterfaces;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(arrayOfInterfaces);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ArrayOfInterfacesClass other = (ArrayOfInterfacesClass) obj;
			if (!Arrays.equals(arrayOfInterfaces, other.arrayOfInterfaces))
				return false;
			return true;
		}
	}

	public static class ListOfInterfacesClass {
		List<IAnInterface> listOfInterfaces;

		public List<IAnInterface> getListOfInterfaces() {
			return listOfInterfaces;
		}

		public void setListOfInterfaces(List<IAnInterface> listOfInterfaces) {
			this.listOfInterfaces = listOfInterfaces;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((listOfInterfaces == null) ? 0 : listOfInterfaces.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ListOfInterfacesClass other = (ListOfInterfacesClass) obj;
			if (listOfInterfaces == null) {
				if (other.listOfInterfaces != null)
					return false;
			} else if (!listOfInterfaces.equals(other.listOfInterfaces))
				return false;
			return true;
		}
	}
}
