package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVStructureArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.epics.pvmarshaller.marshaller.api.IPVStructureDeserialiser;
import org.epics.pvmarshaller.marshaller.deserialisers.Deserialiser;
import org.junit.Test;

public class CustomDeserialiserTest {

	@Test
	public void testRegisterClassDeserialiser() {

		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("CUSTOMprimitiveValue", ScalarType.pvInt).
			add("CUSTOMwrapperValue", ScalarType.pvInt).
			setId("IntTestClass").
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue.put(111);
		PVInt wrapperValue = testPVStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		wrapperValue.put(222);
		
		// Create expected object
		IntTestClass expectedObject = new IntTestClass();
		expectedObject.primitiveValue = 123;
		expectedObject.wrapperValue = 456;
		
		IntTestClass deserialisedObject = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			marshaller.registerDeserialiser("IntTestClass", new IntTestClassDeserialiser());
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, IntTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testRegisterClassDeserialiserPassNullClass() {

		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("CUSTOMprimitiveValue", ScalarType.pvInt).
			add("CUSTOMwrapperValue", ScalarType.pvInt).
			setId("IntTestClass").
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue.put(111);
		PVInt wrapperValue = testPVStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		wrapperValue.put(222);
		
		// Create expected object
		IntTestClass expectedObject = new IntTestClass();
		expectedObject.primitiveValue = 123;
		expectedObject.wrapperValue = 456;
		
		IntTestClass deserialisedObject = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			marshaller.registerDeserialiser("IntTestClass", new IntTestClassDeserialiser());
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, null);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testRegisterClassDeserialiserWithObjectMember() {

		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("CUSTOMprimitiveValue", ScalarType.pvInt).
			add("CUSTOMwrapperValue", ScalarType.pvInt).
			setId("IntTestClass").
			createStructure();

		Structure testStructure = fieldCreate.createFieldBuilder().
			add("containerPrimitiveValue", ScalarType.pvInt).
			add("intTestClassObj", structure).
			createStructure();
					
		PVStructure testPVStructure = pvDataCreate.createPVStructure(testStructure);
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "containerPrimitiveValue");
		primitiveValue.put(27);
		
		PVStructure testClassObjStructure = testPVStructure.getStructureField("intTestClassObj");
		PVInt cprimitiveValue = testClassObjStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		cprimitiveValue.put(111);
		PVInt cwrapperValue = testClassObjStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		cwrapperValue.put(222);
		
		// Create expected object
		IntTestClass intTestClass = new IntTestClass();
		intTestClass.primitiveValue = 123;
		intTestClass.wrapperValue = 456;
		
		IntTestContainerClass expectedObject = new IntTestContainerClass();
		expectedObject.containerPrimitiveValue = 27;
		expectedObject.intTestClassObj = intTestClass;
		
		IntTestContainerClass deserialisedObject = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			marshaller.registerDeserialiser("IntTestClass", new IntTestClassDeserialiser());
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, IntTestContainerClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testCustomDeserilaiserArrayOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("CUSTOMprimitiveValue", ScalarType.pvInt).
				add("CUSTOMwrapperValue", ScalarType.pvInt).
				setId("IntTestClass").
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("containerPrimitiveValue", ScalarType.pvInt).
			addArray("intTestClassObjArray", nestedStructure).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue1.put(111);
		PVInt customerWrapperValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue1.put(222);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue2.put(111);
		PVInt customerWrapperValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue2.put(222);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "containerPrimitiveValue");
		primitiveValue.put(99);
		PVStructureArray objArrayValue = testPVStructure.getSubField(PVStructureArray.class, "intTestClassObjArray");
		PVStructure[] intArray = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		objArrayValue.put(0, 2, intArray, 0);
		
		// Create expected object
		IntTestArrayClass expectedObject = new IntTestArrayClass();

		expectedObject.containerPrimitiveValue = 99;
		expectedObject.intTestClassObjArray = new IntTestClass[2];
		
		IntTestClass testObject1 = new IntTestClass();
		testObject1.primitiveValue = 123;
		testObject1.wrapperValue = 456;
		
		IntTestClass testObject2 = new IntTestClass();
		testObject2.primitiveValue = 123;
		testObject2.wrapperValue = 456;
		
		expectedObject.intTestClassObjArray[0] = testObject1;
		expectedObject.intTestClassObjArray[1] = testObject2;
		
		IntTestArrayClass deserialisedObject = null;
		
		try {
			// Register a custom serialiser for the IntTestClass
			marshaller.registerDeserialiser("IntTestClass", new IntTestClassDeserialiser());
			
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, IntTestArrayClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testCustomDeserilaiserListOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("CUSTOMprimitiveValue", ScalarType.pvInt).
				add("CUSTOMwrapperValue", ScalarType.pvInt).
				setId("IntTestClass").
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("containerPrimitiveValue", ScalarType.pvInt).
			addArray("intTestClassObjList", nestedStructure).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue1.put(111);
		PVInt customerWrapperValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue1.put(222);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue2.put(111);
		PVInt customerWrapperValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue2.put(222);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "containerPrimitiveValue");
		primitiveValue.put(99);
		PVStructureArray objArrayValue = testPVStructure.getSubField(PVStructureArray.class, "intTestClassObjList");
		PVStructure[] intArray = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		objArrayValue.put(0, 2, intArray, 0);
		
		// Create expected object
		IntTestListClass expectedObject = new IntTestListClass();

		expectedObject.containerPrimitiveValue = 99;
		expectedObject.intTestClassObjList = new LinkedList<IntTestClass>();
		
		IntTestClass testObject1 = new IntTestClass();
		testObject1.primitiveValue = 123;
		testObject1.wrapperValue = 456;
		
		IntTestClass testObject2 = new IntTestClass();
		testObject2.primitiveValue = 123;
		testObject2.wrapperValue = 456;
		
		expectedObject.intTestClassObjList.add(testObject1);
		expectedObject.intTestClassObjList.add(testObject2);
		
		IntTestListClass deserialisedObject = null;
		
		try {
			// Register a custom serialiser for the IntTestClass
			marshaller.registerDeserialiser("IntTestClass", new IntTestClassDeserialiser());
			
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, IntTestListClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testCustomDeserilaiserMapOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("CUSTOMprimitiveValue", ScalarType.pvInt).
				add("CUSTOMwrapperValue", ScalarType.pvInt).
				setId("IntTestClass").
				createStructure();

		Structure mapStructure = fieldCreate.createFieldBuilder().
			    add("to1", nestedStructure).
			    add("to2", nestedStructure).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("containerPrimitiveValue", ScalarType.pvInt).
			    add("intTestClassObjMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);

		PVStructure mapPVStructure = testPVStructure.getStructureField("intTestClassObjMap");
		
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "containerPrimitiveValue");
		primitiveValue.put(11);
		
		PVStructure object1PVStructure = mapPVStructure.getStructureField("to1");
		PVInt o1primitiveValue1 = object1PVStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		o1primitiveValue1.put(111);
		PVInt o1customerWrapperValue1 = object1PVStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		o1customerWrapperValue1.put(222);
		
		PVStructure object2PVStructure = mapPVStructure.getStructureField("to2");
		PVInt o2primitiveValue1 = object2PVStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		o2primitiveValue1.put(333);
		PVInt o2customerWrapperValue1 = object2PVStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		o2customerWrapperValue1.put(444);
		
		// Create expected object
		IntTestMapClass expectedObject = new IntTestMapClass();

		expectedObject.containerPrimitiveValue = 11;
		expectedObject.intTestClassObjMap = new LinkedHashMap<String, IntTestClass>();
		
		IntTestClass testObject1 = new IntTestClass();
		testObject1.primitiveValue = 123;
		testObject1.wrapperValue = 456;
		
		IntTestClass testObject2 = new IntTestClass();
		testObject2.primitiveValue = 123;
		testObject2.wrapperValue = 456;
		
		expectedObject.intTestClassObjMap.put("to1", testObject1);
		expectedObject.intTestClassObjMap.put("to2", testObject2);
		
		IntTestMapClass deserialisedObject = null;
		
		try {
			// Register a custom deserialiser for the IntTestClass
			marshaller.registerDeserialiser("IntTestClass", new IntTestClassDeserialiser());
			
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, IntTestMapClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testCustomDeserialiseImplements() {
		
		// Create test class to deserialise
		ContainingClass expectedObject = new ContainingClass();
		expectedObject.containingInteger = 55;
		ImplementingClass implementingObject = new ImplementingClass();
		implementingObject.implentingInteger = 200; // Expect double as the custom deserialiser returns double the value
		
		expectedObject.implementingObject = implementingObject;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure implementingStructure = fieldCreate.createFieldBuilder().
				add("implentingInteger", ScalarType.pvInt).
				setId("TESTID").
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
		extendingValue.put(100); 
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(testPVStructure);
		
		ContainingClass deserialisedObject = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			
			// Register a custom deserialiser for the TESTID id
			marshaller.registerDeserialiser("TESTID", new ImplementerDeserialiser());
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, ContainingClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
				
		assertEquals(expectedObject, deserialisedObject);
	}
	
	public static class IntTestClass
	{
		private int primitiveValue;
		private Integer wrapperValue;
		
		public int getPrimitiveValue() {
			return primitiveValue;
		}
		public void setPrimitiveValue(int primitiveValue) {
			this.primitiveValue = primitiveValue;
		}
		public Integer getWrapperValue() {
			return wrapperValue;
		}
		public void setWrapperValue(Integer wrapperValue) {
			this.wrapperValue = wrapperValue;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + primitiveValue;
			result = prime * result + ((wrapperValue == null) ? 0 : wrapperValue.hashCode());
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
			IntTestClass other = (IntTestClass) obj;
			if (primitiveValue != other.primitiveValue)
				return false;
			if (wrapperValue == null) {
				if (other.wrapperValue != null)
					return false;
			} else if (!wrapperValue.equals(other.wrapperValue))
				return false;
			return true;
		}
	}

	public static class IntTestContainerClass
	{
		private int containerPrimitiveValue;
		private IntTestClass intTestClassObj;
		public int getContainerPrimitiveValue() {
			return containerPrimitiveValue;
		}
		public void setContainerPrimitiveValue(int containerPrimitiveValue) {
			this.containerPrimitiveValue = containerPrimitiveValue;
		}
		public IntTestClass getIntTestClassObj() {
			return intTestClassObj;
		}
		public void setIntTestClassObj(IntTestClass intTestClassObj) {
			this.intTestClassObj = intTestClassObj;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + containerPrimitiveValue;
			result = prime * result + ((intTestClassObj == null) ? 0 : intTestClassObj.hashCode());
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
			IntTestContainerClass other = (IntTestContainerClass) obj;
			if (containerPrimitiveValue != other.containerPrimitiveValue)
				return false;
			if (intTestClassObj == null) {
				if (other.intTestClassObj != null)
					return false;
			} else if (!intTestClassObj.equals(other.intTestClassObj))
				return false;
			return true;
		}
		
	}

	public static class IntTestArrayClass
	{
		private int containerPrimitiveValue;
		private IntTestClass intTestClassObjArray[];
		public int getContainerPrimitiveValue() {
			return containerPrimitiveValue;
		}
		public void setContainerPrimitiveValue(int containerPrimitiveValue) {
			this.containerPrimitiveValue = containerPrimitiveValue;
		}
		public IntTestClass[] getIntTestClassObjArray() {
			return intTestClassObjArray;
		}
		public void setIntTestClassObjArray(IntTestClass[] intTestClassObjArray) {
			this.intTestClassObjArray = intTestClassObjArray;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + containerPrimitiveValue;
			result = prime * result + Arrays.hashCode(intTestClassObjArray);
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
			IntTestArrayClass other = (IntTestArrayClass) obj;
			if (containerPrimitiveValue != other.containerPrimitiveValue)
				return false;
			if (!Arrays.equals(intTestClassObjArray, other.intTestClassObjArray))
				return false;
			return true;
		}
		
		
	}

	public static class IntTestListClass
	{
		private int containerPrimitiveValue;
		private List<IntTestClass> intTestClassObjList;
		public int getContainerPrimitiveValue() {
			return containerPrimitiveValue;
		}
		public void setContainerPrimitiveValue(int containerPrimitiveValue) {
			this.containerPrimitiveValue = containerPrimitiveValue;
		}
		public List<IntTestClass> getIntTestClassObjList() {
			return intTestClassObjList;
		}
		public void setIntTestClassObjList(List<IntTestClass> intTestClassObjList) {
			this.intTestClassObjList = intTestClassObjList;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + containerPrimitiveValue;
			result = prime * result + ((intTestClassObjList == null) ? 0 : intTestClassObjList.hashCode());
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
			IntTestListClass other = (IntTestListClass) obj;
			if (containerPrimitiveValue != other.containerPrimitiveValue)
				return false;
			if (intTestClassObjList == null) {
				if (other.intTestClassObjList != null)
					return false;
			} else if (!intTestClassObjList.equals(other.intTestClassObjList))
				return false;
			return true;
		}
		
		
	}

	public static class IntTestMapClass
	{
		private int containerPrimitiveValue;
		private Map<String,IntTestClass> intTestClassObjMap;
		
		public int getContainerPrimitiveValue() {
			return containerPrimitiveValue;
		}
		public void setContainerPrimitiveValue(int containerPrimitiveValue) {
			this.containerPrimitiveValue = containerPrimitiveValue;
		}
		public Map<String, IntTestClass> getIntTestClassObjMap() {
			return intTestClassObjMap;
		}
		public void setIntTestClassObjMap(Map<String, IntTestClass> intTestClassObjMap) {
			this.intTestClassObjMap = intTestClassObjMap;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + containerPrimitiveValue;
			result = prime * result + ((intTestClassObjMap == null) ? 0 : intTestClassObjMap.hashCode());
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
			IntTestMapClass other = (IntTestMapClass) obj;
			if (containerPrimitiveValue != other.containerPrimitiveValue)
				return false;
			if (intTestClassObjMap == null) {
				if (other.intTestClassObjMap != null)
					return false;
			} else if (!intTestClassObjMap.equals(other.intTestClassObjMap))
				return false;
			return true;
		}
		
		
	}
	
	public static class IntTestClassDeserialiser implements IPVStructureDeserialiser {

		@Override
		public Object fromPVStructure(Deserialiser deserialiser, PVStructure pvStructure) {
			IntTestClass intTestClass = new IntTestClass();
			
			intTestClass.primitiveValue = 123;
			intTestClass.wrapperValue = 456;
			
			return intTestClass;
		}
		
	}
	
	public static class ImplementerDeserialiser implements IPVStructureDeserialiser {

		@Override
		public Object fromPVStructure(Deserialiser deserialiser, PVStructure pvStructure) {

			PVInt extendingValue = pvStructure.getSubField(PVInt.class, "implentingInteger");
			int gotValue = extendingValue.get();
			
			ImplementingClass implementingObject = new ImplementingClass();
						
			// Times by 2 to ensure it's different and that the test actually uses this deserialiser
			implementingObject.implentingInteger = gotValue;
			implementingObject.doSomething();
			
			return implementingObject;
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
			implentingInteger *= 2;			
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
	
	public static class SecondImplementingClass implements IAnInterface {

		private int implentingInteger;
		public int getImplentingInteger() {
			return implentingInteger;
		}
		public void setImplentingInteger(int implentingInteger) {
			this.implentingInteger = implentingInteger;
		}
		@Override
		public void doSomething() {
			implentingInteger *= 3;			
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
}
