package org.epics.pvmarshaller.marshaller.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import org.junit.Test;

public class DeserialiseObjectArraysTests {

	@Test
	public void testDeserialiseArrayOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectArray", nestedStructure).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(20);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(141);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructureArray primitiveValue = testPVStructure.getSubField(PVStructureArray.class, "objectArray");
		PVStructure[] intArray = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		primitiveValue.put(0, 2, intArray, 0);
		

		// Create expected object
		ArrayOfObjectsTestClass expectedObject = new ArrayOfObjectsTestClass();

		expectedObject.objectArray = new ObjectTestClass[2];
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 20;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 141;
		
		expectedObject.objectArray[0] = testObject1;
		expectedObject.objectArray[1] = testObject2;
		
		ArrayOfObjectsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, ArrayOfObjectsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseListOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectList", nestedStructure).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(41);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(222);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructureArray primitiveValue = testPVStructure.getSubField(PVStructureArray.class, "objectList");
		PVStructure[] intArray = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		primitiveValue.put(0, 2, intArray, 0);
		

		// Create expected object
		ListOfObjectsTestClass expectedObject = new ListOfObjectsTestClass();

		expectedObject.objectList = new LinkedList<ObjectTestClass>();
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 41;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 222;
		
		expectedObject.objectList.add(testObject1);
		expectedObject.objectList.add(testObject2);
		
		ListOfObjectsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, ListOfObjectsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseListOfMaps() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected object
		ListOfMapsTestClass expectedObject = new ListOfMapsTestClass();

		expectedObject.mapList = new LinkedList<Map<String, Integer>>();
		
		HashMap<String, Integer> testMap1 = new HashMap<String, Integer>();
		testMap1.put("k1", 141);
		testMap1.put("k2", 242);

		HashMap<String, Integer> testMap2 = new HashMap<String, Integer>();
		testMap2.put("k1", 343);
		testMap2.put("k2", 444);
		
		expectedObject.mapList.add(testMap1);
		expectedObject.mapList.add(testMap2);
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("k1", ScalarType.pvInt).
				add("k2", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("mapList", nestedStructure).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue11 = expectedNestedPVStructure1.getSubField(PVInt.class, "k1");
		primitiveValue11.put(141);
		PVInt primitiveValue12 = expectedNestedPVStructure1.getSubField(PVInt.class, "k2");
		primitiveValue12.put(242);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue21 = expectedNestedPVStructure2.getSubField(PVInt.class, "k1");
		primitiveValue21.put(343);
		PVInt primitiveValue22 = expectedNestedPVStructure2.getSubField(PVInt.class, "k2");
		primitiveValue22.put(444);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructureArray primitiveValue = testPVStructure.getSubField(PVStructureArray.class, "mapList");
		PVStructure[] intArray = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		primitiveValue.put(0, 2, intArray, 0);
		
		ListOfMapsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, ListOfMapsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}


	@Test
	public void testDeserialiseCollectionOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectList", nestedStructure).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(41);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(222);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructureArray primitiveValue = testPVStructure.getSubField(PVStructureArray.class, "objectList");
		PVStructure[] intArray = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		primitiveValue.put(0, 2, intArray, 0);
		

		// Create expected object
		CollectionOfObjectsTestClass expectedObject = new CollectionOfObjectsTestClass();

		expectedObject.objectList = new LinkedList<ObjectTestClass>();
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 41;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 222;
		
		expectedObject.objectList.add(testObject1);
		expectedObject.objectList.add(testObject2);
		
		CollectionOfObjectsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, CollectionOfObjectsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	public static class ObjectTestClass
	{
		private int primitiveValue;

		public int getPrimitiveValue() {
			return primitiveValue;
		}

		public void setPrimitiveValue(int primitiveValue) {
			this.primitiveValue = primitiveValue;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + primitiveValue;
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
			ObjectTestClass other = (ObjectTestClass) obj;
			if (primitiveValue != other.primitiveValue)
				return false;
			return true;
		}
	}
	
	public static class ArrayOfObjectsTestClass {
		ObjectTestClass[] objectArray;

		public ObjectTestClass[] getObjectArray() {
			return objectArray;
		}

		public void setObjectArray(ObjectTestClass[] objectArray) {
			this.objectArray = objectArray;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(objectArray);
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
			ArrayOfObjectsTestClass other = (ArrayOfObjectsTestClass) obj;
			if (!Arrays.equals(objectArray, other.objectArray))
				return false;
			return true;
		}
	}
	
	public static class ListOfObjectsTestClass {
		List<ObjectTestClass> objectList;

		public List<ObjectTestClass> getObjectList() {
			return objectList;
		}

		public void setObjectList(List<ObjectTestClass> objectList) {
			this.objectList = objectList;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((objectList == null) ? 0 : objectList.hashCode());
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
			ListOfObjectsTestClass other = (ListOfObjectsTestClass) obj;
			if (objectList == null) {
				if (other.objectList != null)
					return false;
			} else if (!objectList.equals(other.objectList))
				return false;
			return true;
		}
	}
	
	public static class ListOfMapsTestClass {
		List<Map<String, Integer>> mapList;

		public List<Map<String, Integer>> getMapList() {
			return mapList;
		}

		public void setMapList(List<Map<String, Integer>> mapList) {
			this.mapList = mapList;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((mapList == null) ? 0 : mapList.hashCode());
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
			ListOfMapsTestClass other = (ListOfMapsTestClass) obj;
			if (mapList == null) {
				if (other.mapList != null)
					return false;
			} else if (!mapList.equals(other.mapList))
				return false;
			return true;
		}
	}
	
	public static class CollectionOfObjectsTestClass {
		Collection<ObjectTestClass> objectList;

		public Collection<ObjectTestClass> getObjectList() {
			return objectList;
		}

		public void setObjectList(Collection<ObjectTestClass> objectList) {
			this.objectList = objectList;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((objectList == null) ? 0 : objectList.hashCode());
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
			CollectionOfObjectsTestClass other = (CollectionOfObjectsTestClass) obj;
			if (objectList == null) {
				if (other.objectList != null)
					return false;
			} else if (!objectList.equals(other.objectList))
				return false;
			return true;
		}
	}
}
