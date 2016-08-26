package org.epics.pvmarshaller.marshaller.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVByte;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVFloat;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVShort;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.PVUnionArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Union;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class MapTests {

	@Test
	public void testMapOfIntegers() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfIntegersTestClass testClass = new MapOfIntegersTestClass();

		testClass.integerMap = new HashMap<String, Integer>();
		testClass.integerMap.put("x", 617);
		testClass.integerMap.put("y", -633);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				add("x", ScalarType.pvInt).
				add("y", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("integerMap", mapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = expectedPVStructure.getStructureField("integerMap");
		
		PVInt xValue = mapPVStructure.getSubField(PVInt.class, "x");
		xValue.put(617);
		PVInt yValue = mapPVStructure.getSubField(PVInt.class, "y");
		yValue.put(-633);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(expectedPVStructure);
		
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
	public void testMapOfPrimitives() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfPrimitivesTestClass testClass = new MapOfPrimitivesTestClass();

		testClass.integerMap = new HashMap<String, Integer>();
		testClass.integerMap.put("a", 617);
		testClass.integerMap.put("b", -633);
		testClass.shortMap = new HashMap<String, Short>();
		testClass.shortMap.put("c", (short)47);
		testClass.shortMap.put("d", (short)82);
		testClass.longMap = new HashMap<String, Long>();
		testClass.longMap.put("e", 2217l);
		testClass.longMap.put("f", -635673l);
		testClass.byteMap = new HashMap<String, Byte>();
		testClass.byteMap.put("g", (byte)8);
		testClass.byteMap.put("h", (byte)76);
		testClass.booleanMap = new HashMap<String, Boolean>();
		testClass.booleanMap.put("i", true);
		testClass.booleanMap.put("j", false);
		testClass.floatMap = new HashMap<String, Float>();
		testClass.floatMap.put("k", 345.3f);
		testClass.floatMap.put("l", 2f);
		testClass.doubleMap = new HashMap<String, Double>();
		testClass.doubleMap.put("m", -123.56d);
		testClass.doubleMap.put("n", 33333333d);
		testClass.charMap = new LinkedHashMap<String, Character>();
		testClass.charMap.put("o", 'e');
		testClass.charMap.put("p", 'y');
		testClass.stringMap = new HashMap<String, String>();
		testClass.stringMap.put("q", "TestString 1");
		testClass.stringMap.put("r", "TestString 2");
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure intMapStructure = fieldCreate.createFieldBuilder().
				add("a", ScalarType.pvInt).
				add("b", ScalarType.pvInt).
				createStructure();
		Structure shortMapStructure = fieldCreate.createFieldBuilder().
				add("c", ScalarType.pvShort).
				add("d", ScalarType.pvShort).
				createStructure();
		Structure longMapStructure = fieldCreate.createFieldBuilder().
				add("e", ScalarType.pvLong).
				add("f", ScalarType.pvLong).
				createStructure();
		Structure byteMapStructure = fieldCreate.createFieldBuilder().
				add("g", ScalarType.pvByte).
				add("h", ScalarType.pvByte).
				createStructure();
		Structure boolMapStructure = fieldCreate.createFieldBuilder().
				add("i", ScalarType.pvBoolean).
				add("j", ScalarType.pvBoolean).
				createStructure();
		Structure floatMapStructure = fieldCreate.createFieldBuilder().
				add("k", ScalarType.pvFloat).
				add("l", ScalarType.pvFloat).
				createStructure();
		Structure doubleMapStructure = fieldCreate.createFieldBuilder().
				add("m", ScalarType.pvDouble).
				add("n", ScalarType.pvDouble).
				createStructure();
		Structure charMapStructure = fieldCreate.createFieldBuilder().
				add("o", ScalarType.pvString).
				add("p", ScalarType.pvString).
				createStructure();
		Structure stringMapStructure = fieldCreate.createFieldBuilder().
				add("q", ScalarType.pvString).
				add("r", ScalarType.pvString).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("integerMap", intMapStructure).
			add("shortMap", shortMapStructure).
			add("longMap", longMapStructure).
			add("byteMap", byteMapStructure).
			add("booleanMap", boolMapStructure).
			add("floatMap", floatMapStructure).
			add("doubleMap", doubleMapStructure).
			add("charMap", charMapStructure).
			add("stringMap", stringMapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVStructure intMapPVStructure = expectedPVStructure.getStructureField("integerMap");
		PVInt aValue = intMapPVStructure.getSubField(PVInt.class, "a");
		aValue.put(617);
		PVInt bValue = intMapPVStructure.getSubField(PVInt.class, "b");
		bValue.put(-633);
		
		PVStructure shortMapPVStructure = expectedPVStructure.getStructureField("shortMap");
		PVShort cValue = shortMapPVStructure.getSubField(PVShort.class, "c");
		cValue.put((short)47);
		PVShort dValue = shortMapPVStructure.getSubField(PVShort.class, "d");
		dValue.put((short)82);
		
		PVStructure longMapPVStructure = expectedPVStructure.getStructureField("longMap");
		PVLong eValue = longMapPVStructure.getSubField(PVLong.class, "e");
		eValue.put(2217l);
		PVLong fValue = longMapPVStructure.getSubField(PVLong.class, "f");
		fValue.put(-635673l);
		
		PVStructure byteMapPVStructure = expectedPVStructure.getStructureField("byteMap");
		PVByte gValue = byteMapPVStructure.getSubField(PVByte.class, "g");
		gValue.put((byte)8);
		PVByte hValue = byteMapPVStructure.getSubField(PVByte.class, "h");
		hValue.put((byte)76);
		
		PVStructure booleanMapPVStructure = expectedPVStructure.getStructureField("booleanMap");
		PVBoolean iValue = booleanMapPVStructure.getSubField(PVBoolean.class, "i");
		iValue.put(true);
		PVBoolean jValue = booleanMapPVStructure.getSubField(PVBoolean.class, "j");
		jValue.put(false);
		
		PVStructure floatMapPVStructure = expectedPVStructure.getStructureField("floatMap");
		PVFloat kValue = floatMapPVStructure.getSubField(PVFloat.class, "k");
		kValue.put(345.3f);
		PVFloat lValue = floatMapPVStructure.getSubField(PVFloat.class, "l");
		lValue.put(2f);
		
		PVStructure doubleMapPVStructure = expectedPVStructure.getStructureField("doubleMap");
		PVDouble mValue = doubleMapPVStructure.getSubField(PVDouble.class, "m");
		mValue.put(-123.56d);
		PVDouble nValue = doubleMapPVStructure.getSubField(PVDouble.class, "n");
		nValue.put(33333333d);
		
		PVStructure charMapPVStructure = expectedPVStructure.getStructureField("charMap");
		PVString oValue = charMapPVStructure.getSubField(PVString.class, "o");
		oValue.put("e");
		PVString pValue = charMapPVStructure.getSubField(PVString.class, "p");
		pValue.put("y");
		
		PVStructure stringMapPVStructure = expectedPVStructure.getStructureField("stringMap");
		PVString qValue = stringMapPVStructure.getSubField(PVString.class, "q");
		qValue.put("TestString 1");
		PVString rValue = stringMapPVStructure.getSubField(PVString.class, "r");
		rValue.put("TestString 2");
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(expectedPVStructure);
		
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
	public void testMapOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfObjectsTestClass testClass = new MapOfObjectsTestClass();

		MapObjectTestClass testObject1 = new MapObjectTestClass();
		testObject1.primitiveValue = 5432;
		
		MapObjectTestClass testObject2 = new MapObjectTestClass();
		testObject2.primitiveValue = -12345;
		
		testClass.objectMap = new LinkedHashMap<String, MapObjectTestClass>();
		testClass.objectMap.put("object1", testObject1);
		testClass.objectMap.put("secObj", testObject2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure object1Structure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		Structure object2Structure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				add("object1", object1Structure).
				add("secObj", object2Structure).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("objectMap", mapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = expectedPVStructure.getStructureField("objectMap");
		
		PVStructure object1PVStructure = mapPVStructure.getStructureField("object1");
		PVInt o1XValue = object1PVStructure.getSubField(PVInt.class, "primitiveValue");
		o1XValue.put(5432);
		
		PVStructure secObjPVStructure = mapPVStructure.getStructureField("secObj");
		PVInt o2XValue = secObjPVStructure.getSubField(PVInt.class, "primitiveValue");
		o2XValue.put(-12345);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
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
	public void testMapOfArrays() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfArraysTestClass testClass = new MapOfArraysTestClass();

		testClass.arrayMap = new HashMap<String, int[]>();
		
		int[] array1 = new int[3];
		array1[0] = 12;
		array1[1] = 13;
		array1[2] = 14;

		int[] array2 = new int[2];
		array2[0] = 20;
		array2[1] = 21;
		
		testClass.arrayMap.put("x", array1);
		testClass.arrayMap.put("y", array2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", ScalarType.pvInt).
				addArray("y", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("arrayMap", mapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = expectedPVStructure.getStructureField("arrayMap");
		
		PVIntArray xArray = mapPVStructure.getSubField(PVIntArray.class, "x");
		xArray.put(0, 3, new int[]{12, 13, 14}, 0);
		
		PVIntArray yArray = mapPVStructure.getSubField(PVIntArray.class, "y");
		yArray.put(0, 2, new int[]{20, 21}, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(expectedPVStructure);
		
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
	public void testMapOfObjectArrays() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfObjectArraysTestClass testClass = new MapOfObjectArraysTestClass();

		testClass.objectArrayMap = new HashMap<String, MapObjectTestClass[]>();
		
		MapObjectTestClass[] array1 = new MapObjectTestClass[2];
		MapObjectTestClass obj1 = new MapObjectTestClass();
		obj1.primitiveValue = 55;
		MapObjectTestClass obj2 = new MapObjectTestClass();
		obj2.primitiveValue = 66;
		array1[0] = obj1;
		array1[1] = obj2;

		MapObjectTestClass[] array2 = new MapObjectTestClass[3];
		MapObjectTestClass obj3 = new MapObjectTestClass();
		obj3.primitiveValue = 77;
		MapObjectTestClass obj4 = new MapObjectTestClass();
		obj4.primitiveValue = 88;
		MapObjectTestClass obj5 = new MapObjectTestClass();
		obj5.primitiveValue = 99;
		array2[0] = obj3;
		array2[1] = obj4;
		array2[2] = obj5;
		
		testClass.objectArrayMap.put("x", array1);
		testClass.objectArrayMap.put("y", array2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure objStruct = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", union).
				addArray("y", union).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("objectArrayMap", mapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = expectedPVStructure.getStructureField("objectArrayMap");
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(55);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(66);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		
		PVUnionArray pvUnionValue = mapPVStructure.getSubField(PVUnionArray.class, "x");
		pvUnionValue.put(0, 2, unionArray, 0);
				
		PVStructure expectedNestedPVStructure3 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue3 = expectedNestedPVStructure3.getSubField(PVInt.class, "primitiveValue");
		primitiveValue3.put(77);
		PVStructure expectedNestedPVStructure4 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue4 = expectedNestedPVStructure4.getSubField(PVInt.class, "primitiveValue");
		primitiveValue4.put(88);
		PVStructure expectedNestedPVStructure5 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue5 = expectedNestedPVStructure5.getSubField(PVInt.class, "primitiveValue");
		primitiveValue5.put(99);

		PVUnion pvu3 = pvDataCreate.createPVVariantUnion();
		pvu3.set(expectedNestedPVStructure3);
		PVUnion pvu4 = pvDataCreate.createPVVariantUnion();
		pvu4.set(expectedNestedPVStructure4);
		PVUnion pvu5 = pvDataCreate.createPVVariantUnion();
		pvu5.set(expectedNestedPVStructure5);
		
		PVUnion[] unionArrayY = new PVUnion[3];
		unionArrayY[0] = pvu3;
		unionArrayY[1] = pvu4;
		unionArrayY[2] = pvu5;
		
		PVUnionArray pvUnionValueY = mapPVStructure.getSubField(PVUnionArray.class, "y");
		pvUnionValueY.put(0, 3, unionArrayY, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(expectedPVStructure);
		
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
	public void testMapOfObjectLists() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfObjectListsTestClass testClass = new MapOfObjectListsTestClass();

		testClass.objectListMap = new HashMap<String, List<MapObjectTestClass>>();
		
		List<MapObjectTestClass> list1 = new LinkedList<MapObjectTestClass>();
		MapObjectTestClass obj1 = new MapObjectTestClass();
		obj1.primitiveValue = 55;
		MapObjectTestClass obj2 = new MapObjectTestClass();
		obj2.primitiveValue = 66;
		list1.add(obj1);
		list1.add(obj2);

		List<MapObjectTestClass> list2 = new ArrayList<MapObjectTestClass>();
		MapObjectTestClass obj3 = new MapObjectTestClass();
		obj3.primitiveValue = 77;
		MapObjectTestClass obj4 = new MapObjectTestClass();
		obj4.primitiveValue = 88;
		MapObjectTestClass obj5 = new MapObjectTestClass();
		obj5.primitiveValue = 99;
		list2.add(obj3);
		list2.add(obj4);
		list2.add(obj5);
		
		testClass.objectListMap.put("x", list1);
		testClass.objectListMap.put("y", list2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure objStruct = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", union).
				addArray("y", union).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("objectListMap", mapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = expectedPVStructure.getStructureField("objectListMap");
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(55);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(66);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		
		PVUnionArray pvUnionValue = mapPVStructure.getSubField(PVUnionArray.class, "x");
		pvUnionValue.put(0, 2, unionArray, 0);
				
		PVStructure expectedNestedPVStructure3 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue3 = expectedNestedPVStructure3.getSubField(PVInt.class, "primitiveValue");
		primitiveValue3.put(77);
		PVStructure expectedNestedPVStructure4 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue4 = expectedNestedPVStructure4.getSubField(PVInt.class, "primitiveValue");
		primitiveValue4.put(88);
		PVStructure expectedNestedPVStructure5 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue5 = expectedNestedPVStructure5.getSubField(PVInt.class, "primitiveValue");
		primitiveValue5.put(99);

		PVUnion pvu3 = pvDataCreate.createPVVariantUnion();
		pvu3.set(expectedNestedPVStructure3);
		PVUnion pvu4 = pvDataCreate.createPVVariantUnion();
		pvu4.set(expectedNestedPVStructure4);
		PVUnion pvu5 = pvDataCreate.createPVVariantUnion();
		pvu5.set(expectedNestedPVStructure5);
		
		PVUnion[] unionArrayY = new PVUnion[3];
		unionArrayY[0] = pvu3;
		unionArrayY[1] = pvu4;
		unionArrayY[2] = pvu5;
		
		PVUnionArray pvUnionValueY = mapPVStructure.getSubField(PVUnionArray.class, "y");
		pvUnionValueY.put(0, 3, unionArrayY, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(expectedPVStructure);
		
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
	public void testMapOfLists() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfListsTestClass testClass = new MapOfListsTestClass();

		testClass.listMap = new HashMap<String, List<Integer>>();
		
		List<Integer> list1 = new LinkedList<>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		
		List<Integer> list2 = new LinkedList<>();
		list2.add(4);
		list2.add(5);
		list2.add(6);
		
		testClass.listMap.put("x", list1);
		testClass.listMap.put("y", list2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", ScalarType.pvInt).
				addArray("y", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("listMap", mapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = expectedPVStructure.getStructureField("listMap");
		
		PVIntArray xArray = mapPVStructure.getSubField(PVIntArray.class, "x");
		xArray.put(0, 3, new int[]{1, 2, 3}, 0);
		
		PVIntArray yArray = mapPVStructure.getSubField(PVIntArray.class, "y");
		yArray.put(0, 3, new int[]{4, 5, 6}, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(expectedPVStructure);
		
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
	public void testMapOfMaps() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfMapsTestClass  testClass = new MapOfMapsTestClass();
		
		Map<String, Integer> map1 = new LinkedHashMap<String, Integer>();
		map1.put("int1", 1);
		map1.put("int2", 2);
		
		Map<String, Integer> map2 = new LinkedHashMap<String, Integer>();
		map2.put("int3", 3);
		map2.put("int4", 4);
		
		testClass.mapMap = new LinkedHashMap<String, Map<String, Integer>>();
		
		testClass.mapMap.put("FirstMap", map1);
		testClass.mapMap.put("SecMap", map2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure map1Structure = fieldCreate.createFieldBuilder().
				add("int1", ScalarType.pvInt).
				add("int2", ScalarType.pvInt).
				createStructure();
		Structure map2Structure = fieldCreate.createFieldBuilder().
				add("int3", ScalarType.pvInt).
				add("int4", ScalarType.pvInt).
				createStructure();
		
		Structure mapmapStructure = fieldCreate.createFieldBuilder().
				add("FirstMap", map1Structure).
				add("SecMap", map2Structure).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("mapMap", mapmapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapmapPVStructure = expectedPVStructure.getStructureField("mapMap");
		
		PVStructure map1PVStructure = mapmapPVStructure.getStructureField("FirstMap");
		PVInt o1XValue = map1PVStructure.getSubField(PVInt.class, "int1");
		o1XValue.put(1);
		PVInt o2XValue = map1PVStructure.getSubField(PVInt.class, "int2");
		o2XValue.put(2);
		
		PVStructure map2PVStructure = mapmapPVStructure.getStructureField("SecMap");
		PVInt o3XValue = map2PVStructure.getSubField(PVInt.class, "int3");
		o3XValue.put(3);
		PVInt o4XValue = map2PVStructure.getSubField(PVInt.class, "int4");
		o4XValue.put(4);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
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
	public void testMapOfObjectMaps() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfObjectMapsTestClass  testClass = new MapOfObjectMapsTestClass();
		
		Map<String, MapObjectTestClass> map1 = new LinkedHashMap<String, MapObjectTestClass>();
		

		MapObjectTestClass testObject1 = new MapObjectTestClass();
		testObject1.primitiveValue = 111;
		MapObjectTestClass testObject2 = new MapObjectTestClass();
		testObject2.primitiveValue = 222;
		
		map1.put("obj1", testObject1);
		map1.put("obj2", testObject2);
		
		Map<String, MapObjectTestClass> map2 = new LinkedHashMap<String, MapObjectTestClass>();
		MapObjectTestClass testObject3 = new MapObjectTestClass();
		testObject3.primitiveValue = 333;
		MapObjectTestClass testObject4 = new MapObjectTestClass();
		testObject4.primitiveValue = 444;
		map2.put("obj3", testObject3);
		map2.put("obj4", testObject4);
		
		testClass.objectMapMap = new LinkedHashMap<String, Map<String, MapObjectTestClass>>();
		
		testClass.objectMapMap.put("FirstMap", map1);
		testClass.objectMapMap.put("SecMap", map2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure objStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure map1Structure = fieldCreate.createFieldBuilder().
				add("obj1", objStructure).
				add("obj2", objStructure).
				createStructure();
		Structure map2Structure = fieldCreate.createFieldBuilder().
				add("obj3", objStructure).
				add("obj4", objStructure).
				createStructure();
		
		Structure mapmapStructure = fieldCreate.createFieldBuilder().
				add("FirstMap", map1Structure).
				add("SecMap", map2Structure).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("objectMapMap", mapmapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapmapPVStructure = expectedPVStructure.getStructureField("objectMapMap");
		
		PVStructure map1PVStructure = mapmapPVStructure.getStructureField("FirstMap");
		PVStructure obj1PVStructure = map1PVStructure.getSubField(PVStructure.class, "obj1");
		PVInt o1XValue = obj1PVStructure.getSubField(PVInt.class, "primitiveValue");
		o1XValue.put(111);
		PVStructure obj2PVStructure = map1PVStructure.getSubField(PVStructure.class, "obj2");
		PVInt o2XValue = obj2PVStructure.getSubField(PVInt.class, "primitiveValue");
		o2XValue.put(222);
		
		PVStructure map2PVStructure = mapmapPVStructure.getStructureField("SecMap");
		PVStructure obj3PVStructure = map2PVStructure.getSubField(PVStructure.class, "obj3");
		PVInt o3XValue = obj3PVStructure.getSubField(PVInt.class, "primitiveValue");
		o3XValue.put(333);
		PVStructure obj4PVStructure = map2PVStructure.getSubField(PVStructure.class, "obj4");
		PVInt o4XValue = obj4PVStructure.getSubField(PVInt.class, "primitiveValue");
		o4XValue.put(444);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
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
	public void testNullMap() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfIntegersTestClass testClass = new MapOfIntegersTestClass();

		testClass.integerMap = null;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure = fieldCreate.createFieldBuilder().
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(expectedPVStructure);
		
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
	public void testListOfMaps() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ListOfMapsTestClass testClass = new ListOfMapsTestClass();

		testClass.mapList = new LinkedList<Map<String, Integer>>();
		
		HashMap<String, Integer> testMap1 = new HashMap<String, Integer>();
		testMap1.put("k1", 141);
		testMap1.put("k2", 242);

		HashMap<String, Integer> testMap2 = new HashMap<String, Integer>();
		testMap2.put("k1", 343);
		testMap2.put("k3", 444);
		
		testClass.mapList.add(testMap1);
		testClass.mapList.add(testMap2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure1 = fieldCreate.createFieldBuilder().
				add("k1", ScalarType.pvInt).
				add("k2", ScalarType.pvInt).
				createStructure();
		
		Structure nestedStructure2 = fieldCreate.createFieldBuilder().
				add("k1", ScalarType.pvInt).
				add("k3", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("mapList", union).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure1);
		PVInt primitiveValue11 = expectedNestedPVStructure1.getSubField(PVInt.class, "k1");
		primitiveValue11.put(141);
		PVInt primitiveValue12 = expectedNestedPVStructure1.getSubField(PVInt.class, "k2");
		primitiveValue12.put(242);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure2);
		PVInt primitiveValue21 = expectedNestedPVStructure2.getSubField(PVInt.class, "k1");
		primitiveValue21.put(343);
		PVInt primitiveValue22 = expectedNestedPVStructure2.getSubField(PVInt.class, "k3");
		primitiveValue22.put(444);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVUnionArray primitiveValue = expectedPVStructure.getSubField(PVUnionArray.class, "mapList");
		
		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = {pvu1, pvu2};
		primitiveValue.put(0, 2, unionArray, 0);
		
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
	public void testMapOfListsOfMaps() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfListsOfMapsTestClass testClass = new MapOfListsOfMapsTestClass();

		testClass.listMap = new HashMap<String, List<Map<String, Integer>>>();

		List<Map<String, Integer>> list1 = new LinkedList<Map<String, Integer>>();
		HashMap<String, Integer> testMap1 = new HashMap<String, Integer>();
		testMap1.put("k1", 141);
		testMap1.put("k2", 242);

		HashMap<String, Integer> testMap2 = new HashMap<String, Integer>();
		testMap2.put("k1", 343);
		testMap2.put("k3", 444);
		
		list1.add(testMap1);
		list1.add(testMap2);
		
		List<Map<String, Integer>> list2 = new LinkedList<Map<String, Integer>>();
		HashMap<String, Integer> testMap3 = new HashMap<String, Integer>();
		testMap3.put("k3", 4356);
		testMap3.put("k4", 234);
		
		list2.add(testMap3);
				
		testClass.listMap.put("x", list1);
		testClass.listMap.put("y", list2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure1 = fieldCreate.createFieldBuilder().
				add("k1", ScalarType.pvInt).
				add("k2", ScalarType.pvInt).
				createStructure();
		
		Structure nestedStructure2 = fieldCreate.createFieldBuilder().
				add("k1", ScalarType.pvInt).
				add("k3", ScalarType.pvInt).
				createStructure();
		
		Structure nestedStructure3 = fieldCreate.createFieldBuilder().
				add("k3", ScalarType.pvInt).
				add("k4", ScalarType.pvInt).
				createStructure();

		Structure listMapStructure = fieldCreate.createFieldBuilder().
				addArray("x", union).
				addArray("y", union).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("listMap", listMapStructure).
			createStructure();
		
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure1);
		PVInt primitiveValue11 = expectedNestedPVStructure1.getSubField(PVInt.class, "k1");
		primitiveValue11.put(141);
		PVInt primitiveValue12 = expectedNestedPVStructure1.getSubField(PVInt.class, "k2");
		primitiveValue12.put(242);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure2);
		PVInt primitiveValue21 = expectedNestedPVStructure2.getSubField(PVInt.class, "k1");
		primitiveValue21.put(343);
		PVInt primitiveValue22 = expectedNestedPVStructure2.getSubField(PVInt.class, "k3");
		primitiveValue22.put(444);
		PVStructure expectedNestedPVStructure3 = pvDataCreate.createPVStructure(nestedStructure3);
		PVInt primitiveValue31 = expectedNestedPVStructure3.getSubField(PVInt.class, "k3");
		primitiveValue31.put(4356);
		PVInt primitiveValue32 = expectedNestedPVStructure3.getSubField(PVInt.class, "k4");
		primitiveValue32.put(234);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVStructure lmValue = expectedPVStructure.getStructureField("listMap");
		
		PVUnionArray xValue = lmValue.getSubField(PVUnionArray.class, "x");
		
		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = {pvu1, pvu2};
		xValue.put(0, 2, unionArray, 0);
		
		PVUnionArray yValue = lmValue.getSubField(PVUnionArray.class, "y");
		
		PVUnion pvu3 = pvDataCreate.createPVVariantUnion();
		pvu3.set(expectedNestedPVStructure3);
		
		PVUnion[] yUnionArray = {pvu3};
		yValue.put(0, 1, yUnionArray, 0);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testMapOfJavaObjectsWithTypeId() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfJavaObjectsTestClass testClass = new MapOfJavaObjectsTestClass();

		String strVal = "A String";
		int intVal = 34;
		Map<String, Object> nestedMap = new LinkedHashMap<String, Object>();
		nestedMap.put("nestedInt", 43);
		nestedMap.put("typeid", "NestedTypeID");
		nestedMap.put("name", "Thing");
		
		testClass.objectMap = new LinkedHashMap<String, Object>();
		testClass.objectMap.put("strVal", strVal);
		testClass.objectMap.put("intVal", intVal);
		testClass.objectMap.put("nestedMap", nestedMap);
		testClass.objectMap.put("typeid", "TestTypeID");
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Structure nestedMapStructure = fieldCreate.createFieldBuilder().
				add("nestedInt", ScalarType.pvInt).
				add("name", ScalarType.pvString).
				setId("NestedTypeID").
				createStructure();
						
		Structure objMapStructure = fieldCreate.createFieldBuilder().
				add("strVal", ScalarType.pvString).
				add("intVal", ScalarType.pvInt).
				add("nestedMap", nestedMapStructure).
				setId("TestTypeID").
				createStructure();
				
		Structure structure = fieldCreate.createFieldBuilder().
			add("objectMap", objMapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = expectedPVStructure.getStructureField("objectMap");
		
		PVInt intValValue = mapPVStructure.getSubField(PVInt.class, "intVal");
		intValValue.put(34);
		PVString strValValue = mapPVStructure.getSubField(PVString.class, "strVal");
		strValValue.put("A String");
		
		PVStructure nestedMapPVStructure = mapPVStructure.getStructureField("nestedMap");
		PVInt nestedIntValue = nestedMapPVStructure.getSubField(PVInt.class, "nestedInt");
		nestedIntValue.put(43);
		PVString nestedNameValue = nestedMapPVStructure.getSubField(PVString.class, "name");
		nestedNameValue.put("Thing");
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			marshaller.registerMapTypeIdKey("typeid");
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	class MapOfIntegersTestClass {
		Map<String, Integer> integerMap;

		public Map<String, Integer> getIntegerMap() {
			return integerMap;
		}
	}
	
	class MapOfPrimitivesTestClass {
		Map<String, Integer> integerMap;
		Map<String, Short> shortMap;
		Map<String, Long> longMap;
		Map<String, Byte> byteMap;
		Map<String, Boolean> booleanMap;
		Map<String, Float> floatMap;
		Map<String, Double> doubleMap;
		Map<String, Character> charMap;
		Map<String, String> stringMap;
		public Map<String, Integer> getIntegerMap() {
			return integerMap;
		}
		public Map<String, Short> getShortMap() {
			return shortMap;
		}
		public Map<String, Long> getLongMap() {
			return longMap;
		}
		public Map<String, Byte> getByteMap() {
			return byteMap;
		}
		public Map<String, Boolean> getBooleanMap() {
			return booleanMap;
		}
		public Map<String, Float> getFloatMap() {
			return floatMap;
		}
		public Map<String, Double> getDoubleMap() {
			return doubleMap;
		}
		public Map<String, Character> getCharMap() {
			return charMap;
		}
		public Map<String, String> getStringMap() {
			return stringMap;
		}
	}
	
	class MapObjectTestClass
	{
		private int primitiveValue;

		public int getPrimitiveValue() {
			return primitiveValue;
		}
	}
	
	class MapOfObjectsTestClass {
		Map<String, MapObjectTestClass> objectMap;

		public Map<String, MapObjectTestClass> getObjectMap() {
			return objectMap;
		}
	}
	
	class MapOfArraysTestClass {
		Map<String, int[]> arrayMap;

		public Map<String, int[]> getArrayMap() {
			return arrayMap;
		}
	}
	
	class MapOfObjectArraysTestClass {
		Map<String, MapObjectTestClass[]> objectArrayMap;

		public Map<String, MapObjectTestClass[]> getObjectArrayMap() {
			return objectArrayMap;
		}
	}
	
	class MapOfObjectListsTestClass {
		Map<String, List<MapObjectTestClass>> objectListMap;

		public Map<String, List<MapObjectTestClass>> getObjectListMap() {
			return objectListMap;
		}
	}
	
	class MapOfListsTestClass {
		Map<String, List<Integer>> listMap;

		public Map<String, List<Integer>> getListMap() {
			return listMap;
		}
	}
	
	class MapOfMapsTestClass {
		Map<String, Map<String, Integer>> mapMap;

		public Map<String, Map<String, Integer>> getMapMap() {
			return mapMap;
		}
	}
	
	class MapOfObjectMapsTestClass {
		Map<String, Map<String, MapObjectTestClass>> objectMapMap;

		public Map<String, Map<String, MapObjectTestClass>> getObjectMapMap() {
			return objectMapMap;
		}
	}
	
	class ListOfMapsTestClass {
		List<Map<String, Integer>> mapList;

		public List<Map<String, Integer>> getMapList() {
			return mapList;
		}
	}
	
	class MapOfListsOfMapsTestClass {
		Map<String, List<Map<String, Integer>>> listMap;

		public Map<String, List<Map<String, Integer>>> getListMap() {
			return listMap;
		}
	}
	
	class MapOfJavaObjectsTestClass {
		Map<String, Object> objectMap;

		public Map<String, Object> getObjectMap() {
			return objectMap;
		}
	}

}
