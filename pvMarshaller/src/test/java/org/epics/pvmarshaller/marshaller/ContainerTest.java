package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVBooleanArray;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.PVUnionArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Union;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class ContainerTest {

	@Test
	public void testArrayOfIntegers() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ArrayOfIntsTestClass testClass = new ArrayOfIntsTestClass();

		testClass.integerArray = new int[3];
		testClass.integerArray[0] = 4;
		testClass.integerArray[1] = 180;
		testClass.integerArray[2] = 996;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("integerArray", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveValue = expectedPVStructure.getSubField(PVIntArray.class, "integerArray");
		int[] intArray = {4, 180, 996};
		primitiveValue.put(0, 3, intArray, 0);
		
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
	public void testArrayOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ArrayOfObjectsTestClass testClass = new ArrayOfObjectsTestClass();

		testClass.objectArray = new ObjectTestClass[2];
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 20;
		testObject1.wrapperValue = 33;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 141;
		testObject2.wrapperValue = 55;
		
		testClass.objectArray[0] = testObject1;
		testClass.objectArray[1] = testObject2;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				add("wrapperValue", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectArray", union).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(20);
		PVInt wrapperValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "wrapperValue");
		wrapperValue1.put(33);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(141);
		PVInt wrapperValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "wrapperValue");
		wrapperValue2.put(55);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		PVUnionArray pvUnionValue = expectedPVStructure.getSubField(PVUnionArray.class, "objectArray");
		pvUnionValue.put(0, 2, unionArray, 0);
		
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
	public void testArrayOfArrays() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ArrayOfArraysTestClass testClass = new ArrayOfArraysTestClass();

		testClass.integerArrayArray = new int[3][2];
		testClass.integerArrayArray[0][0] = 4;
		testClass.integerArrayArray[0][1] = 5;
		testClass.integerArrayArray[1][0] = 6;
		testClass.integerArrayArray[1][1] = 7;
		testClass.integerArrayArray[2][0] = 8;
		testClass.integerArrayArray[2][1] = 9;
		
		try {
			marshaller.toPVStructure(testClass);
			fail("no exception thrown for array of arrays");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void testListOfIntegers() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfIntegersTestClass testClass = new ListOfIntegersTestClass();

		testClass.integerList = new LinkedList<Integer>();
		
		testClass.integerList.add(358);
		testClass.integerList.add(934623);
		testClass.integerList.add(33);
		testClass.integerList.add(-5);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("integerList", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveValue = expectedPVStructure.getSubField(PVIntArray.class, "integerList");
		int[] intArray = {358, 934623, 33, -5};
		primitiveValue.put(0, 4, intArray, 0);
		
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
	public void testListOfAllPrimitives() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfPrimitivesTestClass testClass = new ListOfPrimitivesTestClass();

		testClass.integerList = new ArrayList<Integer>();
		testClass.integerList.add(5647);
		testClass.integerList.add(33);
		testClass.shortList = new ArrayList<Short>();
		testClass.shortList.add((short)1);
		testClass.shortList.add((short)-6);
		testClass.longList = new ArrayList<Long>();
		testClass.longList.add(345345l);
		testClass.longList.add(565656l);
		testClass.byteList = new ArrayList<Byte>();
		testClass.byteList.add((byte)4);
		testClass.byteList.add((byte)244);
		testClass.booleanList = new ArrayList<Boolean>();
		testClass.booleanList.add(true);
		testClass.booleanList.add(false);
		testClass.floatList = new ArrayList<Float>();
		testClass.floatList.add(9999f);
		testClass.floatList.add(434.2f);
		testClass.doubleList = new ArrayList<Double>();
		testClass.doubleList.add(34534534534d);
		testClass.doubleList.add(3399.1234d);
		testClass.charList = new ArrayList<Character>();
		testClass.charList.add('a');
		testClass.charList.add('b');
		testClass.stringList = new ArrayList<String>();
		testClass.stringList.add("String 1");
		testClass.stringList.add("String 2");
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("integerList", ScalarType.pvInt).
			addArray("shortList", ScalarType.pvShort).
			addArray("longList", ScalarType.pvLong).
			addArray("byteList", ScalarType.pvByte).
			addArray("booleanList", ScalarType.pvBoolean).
			addArray("floatList", ScalarType.pvFloat).
			addArray("doubleList", ScalarType.pvDouble).
			addArray("charList", ScalarType.pvString).
			addArray("stringList", ScalarType.pvString).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVIntArray intField = expectedPVStructure.getSubField(PVIntArray.class, "integerList");
		int[] intArray = {5647, 33};
		intField.put(0, 2, intArray, 0);
		
		PVShortArray shortField = expectedPVStructure.getSubField(PVShortArray.class, "shortList");
		short[] shortArray = {1, -6};
		shortField.put(0, 2, shortArray, 0);
		
		PVLongArray longField = expectedPVStructure.getSubField(PVLongArray.class, "longList");
		long[] longArray = {345345l, 565656l};
		longField.put(0, 2, longArray, 0);
		
		PVByteArray byteField = expectedPVStructure.getSubField(PVByteArray.class, "byteList");
		byte[] byteArray = {(byte)4, (byte)244};
		byteField.put(0, 2, byteArray, 0);
		
		PVBooleanArray booleanField = expectedPVStructure.getSubField(PVBooleanArray.class, "booleanList");
		boolean[] booleanArray = {true, false};
		booleanField.put(0, 2, booleanArray, 0);
		
		PVFloatArray floatField = expectedPVStructure.getSubField(PVFloatArray.class, "floatList");
		float[] floatArray = {9999f, 434.2f};
		floatField.put(0, 2, floatArray, 0);
		
		PVDoubleArray doubleField = expectedPVStructure.getSubField(PVDoubleArray.class, "doubleList");
		double[] doubleArray = {34534534534d, 3399.1234d};
		doubleField.put(0, 2, doubleArray, 0);
		
		PVStringArray charField = expectedPVStructure.getSubField(PVStringArray.class, "charList");
		String[] charArray = {"a", "b"};
		charField.put(0, 2, charArray, 0);
		
		PVStringArray stringField = expectedPVStructure.getSubField(PVStringArray.class, "stringList");
		String[] stringArray = {"String 1", "String 2"};
		stringField.put(0, 2, stringArray, 0);
		
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
	public void testListOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfObjectsTestClass testClass = new ListOfObjectsTestClass();

		testClass.objectList = new LinkedList<ObjectTestClass>();
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 6532;
		testObject1.wrapperValue = 333;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 444;
		testObject2.wrapperValue = 999;
		
		testClass.objectList.add(testObject1);
		testClass.objectList.add(testObject2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				add("wrapperValue", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectList", union).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(6532);
		PVInt wrapperValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "wrapperValue");
		wrapperValue1.put(333);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(444);
		PVInt wrapperValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "wrapperValue");
		wrapperValue2.put(999);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		PVUnionArray pvUnionValue = expectedPVStructure.getSubField(PVUnionArray.class, "objectList");
		pvUnionValue.put(0, 2, unionArray, 0);
		
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
	public void testNullArray() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ArrayOfIntsTestClass testClass = new ArrayOfIntsTestClass();

		testClass.integerArray = null;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
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
	public void testBigArray() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ArrayOfIntsTestClass testClass = new ArrayOfIntsTestClass();

		testClass.integerArray = new int[3000000];
		
		for (int i = 0; i < 3000000; i++) {
			testClass.integerArray[i] = i;
		}
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("integerArray", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveValue = expectedPVStructure.getSubField(PVIntArray.class, "integerArray");
		primitiveValue.put(0, 3000000, testClass.integerArray, 0);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testNullList() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfIntegersTestClass testClass = new ListOfIntegersTestClass();

		testClass.integerList = null;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
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
	public void testSetOfIntegers() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		SetOfIntegersTestClass testClass = new SetOfIntegersTestClass();

		testClass.intSet = new HashSet<Integer>();
		
		testClass.intSet.add(358);
		testClass.intSet.add(934623);
		testClass.intSet.add(33);
		testClass.intSet.add(-5);
		
		try {
			marshaller.toPVStructure(testClass);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void testCollectionOfIntegersPassesWithList() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		CollectionOfIntegersTestClass testClass = new CollectionOfIntegersTestClass();

		testClass.intCollection = new LinkedList<Integer>();
		
		testClass.intCollection.add(358);
		testClass.intCollection.add(934623);
		testClass.intCollection.add(33);
		testClass.intCollection.add(-5);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("intCollection", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveValue = expectedPVStructure.getSubField(PVIntArray.class, "intCollection");
		int[] intArray = {358, 934623, 33, -5};
		primitiveValue.put(0, 4, intArray, 0);
		
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
	public void testCollectionOfIntegersFailsWithSet() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		CollectionOfIntegersTestClass testClass = new CollectionOfIntegersTestClass();

		testClass.intCollection = new HashSet<Integer>();
		
		testClass.intCollection.add(358);
		testClass.intCollection.add(934623);
		testClass.intCollection.add(33);
		testClass.intCollection.add(-5);
		
		try {
			marshaller.toPVStructure(testClass);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testCollectionOfObjectsPassesWithList() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		CollectionOfObjectsTestClass testClass = new CollectionOfObjectsTestClass();

		testClass.objectList = new LinkedList<ObjectTestClass>();
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 6532;
		testObject1.wrapperValue = 333;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 444;
		testObject2.wrapperValue = 999;
		
		testClass.objectList.add(testObject1);
		testClass.objectList.add(testObject2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				add("wrapperValue", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectList", union).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(6532);
		PVInt wrapperValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "wrapperValue");
		wrapperValue1.put(333);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(444);
		PVInt wrapperValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "wrapperValue");
		wrapperValue2.put(999);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		PVUnionArray pvUnionValue = expectedPVStructure.getSubField(PVUnionArray.class, "objectList");
		pvUnionValue.put(0, 2, unionArray, 0);
		
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
	public void testCollectionOfObjectsFailsWithSet() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		CollectionOfObjectsTestClass testClass = new CollectionOfObjectsTestClass();

		testClass.objectList = new HashSet<ObjectTestClass>();
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 6532;
		testObject1.wrapperValue = 333;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 444;
		testObject2.wrapperValue = 999;
		
		testClass.objectList.add(testObject1);
		testClass.objectList.add(testObject2);
		
		try {
			marshaller.toPVStructure(testClass);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testListOfObjectsWithNullsCausingDifferentStructureSecondHasLess() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfObjectsTestClass testClass = new ListOfObjectsTestClass();

		testClass.objectList = new LinkedList<ObjectTestClass>();
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 6532;
		testObject1.wrapperValue = 333;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 444;
		
		testClass.objectList.add(testObject1);
		testClass.objectList.add(testObject2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure1 = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				add("wrapperValue", ScalarType.pvInt).
				createStructure();
		
		Structure nestedStructure2 = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectList", union).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure1);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(6532);
		PVInt wrapperValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "wrapperValue");
		wrapperValue1.put(333);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure2);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(444);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		PVUnionArray pvUnionValue = expectedPVStructure.getSubField(PVUnionArray.class, "objectList");
		pvUnionValue.put(0, 2, unionArray, 0);
		
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
	public void testListOfObjectsWithNullsCausingDifferentStructureSecondHasMore() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfObjectsTestClass testClass = new ListOfObjectsTestClass();

		testClass.objectList = new LinkedList<ObjectTestClass>();
		
		ObjectTestClass testObject1 = new ObjectTestClass();
		testObject1.primitiveValue = 6532;
		
		ObjectTestClass testObject2 = new ObjectTestClass();
		testObject2.primitiveValue = 444;
		testObject2.wrapperValue = 999;
		
		testClass.objectList.add(testObject1);
		testClass.objectList.add(testObject2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure1 = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure nestedStructure2 = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				add("wrapperValue", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectList", union).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure1);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(6532);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure2);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(444);
		PVInt wrapperValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "wrapperValue");
		wrapperValue2.put(999);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		PVUnionArray pvUnionValue = expectedPVStructure.getSubField(PVUnionArray.class, "objectList");
		pvUnionValue.put(0, 2, unionArray, 0);
		
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
	public void testEmptyArrayOfIntegers() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ArrayOfIntsTestClass testClass = new ArrayOfIntsTestClass();

		testClass.integerArray = new int[0];
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("integerArray", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
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
	public void testEmptyArrayOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ArrayOfObjectsTestClass testClass = new ArrayOfObjectsTestClass();

		testClass.objectArray = new ObjectTestClass[0];
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectArray", union).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
				
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testEmptyListOfIntegers() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfIntegersTestClass testClass = new ListOfIntegersTestClass();

		testClass.integerList = new LinkedList<>();
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		// Because of Java Type erasure, the marshaller cannot necessarily know this is an integer list.
		// It therefore has to assume it's an object list so union array is expected
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("integerList", union).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
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
	public void testEmptyListOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfObjectsTestClass testClass = new ListOfObjectsTestClass();

		testClass.objectList = new LinkedList<>();
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("objectList", union).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	
	class ArrayOfIntsTestClass {
		int[] integerArray;

		public int[] getIntegerArray() {
			return integerArray;
		}
	}
	
	class ObjectTestClass
	{
		private int primitiveValue;
		private Integer wrapperValue;
		public int getPrimitiveValue() {
			return primitiveValue;
		}
		public Integer getWrapperValue() {
			return wrapperValue;
		}
	}
	
	class ArrayOfObjectsTestClass {
		ObjectTestClass[] objectArray;

		public ObjectTestClass[] getObjectArray() {
			return objectArray;
		}
	}
	
	class ArrayOfArraysTestClass {
		int[][] integerArrayArray;

		public int[][] getIntegerArrayArray() {
			return integerArrayArray;
		}
	}
	
	class ListOfIntegersTestClass {
		LinkedList<Integer> integerList;

		public LinkedList<Integer> getIntegerList() {
			return integerList;
		}
	}
	
	class ListOfPrimitivesTestClass {
		List<Integer> integerList;
		List<Short> shortList;
		List<Long> longList;
		List<Byte> byteList;
		List<Boolean> booleanList;
		List<Float> floatList;
		List<Double> doubleList;
		List<Character> charList;
		List<String> stringList;
		public List<Integer> getIntegerList() {
			return integerList;
		}
		public List<Short> getShortList() {
			return shortList;
		}
		public List<Long> getLongList() {
			return longList;
		}
		public List<Byte> getByteList() {
			return byteList;
		}
		public List<Boolean> getBooleanList() {
			return booleanList;
		}
		public List<Float> getFloatList() {
			return floatList;
		}
		public List<Double> getDoubleList() {
			return doubleList;
		}
		public List<Character> getCharList() {
			return charList;
		}
		public List<String> getStringList() {
			return stringList;
		}
	}
	
	class ListOfObjectsTestClass {
		List<ObjectTestClass> objectList;

		public List<ObjectTestClass> getObjectList() {
			return objectList;
		}
	}
	
	class SetOfIntegersTestClass {
		Set<Integer> intSet;

		public Set<Integer> getIntSet() {
			return intSet;
		}
	}
	
	class CollectionOfIntegersTestClass {
		Collection<Integer> intCollection;

		public Collection<Integer> getIntCollection() {
			return intCollection;
		}
	}
	
	class CollectionOfObjectsTestClass {
		Collection<ObjectTestClass> objectList;

		public Collection<ObjectTestClass> getObjectList() {
			return objectList;
		}
	}

}
