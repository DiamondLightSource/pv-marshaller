package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVShort;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVStructureArray;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.PVUnionArray;
import org.epics.pvdata.pv.Scalar;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Union;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Assert;
import org.junit.Test;

public class DeserialiseMapsTest {

	@Test
	public void testDeserialiseMapOfIntegers() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test structure to deserialise
		MapOfPrimitivesTestClass expectedObject = new MapOfPrimitivesTestClass();

		expectedObject.integerMap = new HashMap<String, Integer>();
		expectedObject.integerMap.put("x", 617);
		expectedObject.integerMap.put("y", -633);
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				add("x", ScalarType.pvInt).
				add("y", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("integerMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("integerMap");
		
		PVInt xValue = mapPVStructure.getSubField(PVInt.class, "x");
		xValue.put(617);
		PVInt yValue = mapPVStructure.getSubField(PVInt.class, "y");
		yValue.put(-633);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfPrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfPrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseMapOfPrimitives() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test structure to deserialise
		MapOfPrimitivesTestClass expectedObject = new MapOfPrimitivesTestClass();

		expectedObject.integerMap = new HashMap<String, Integer>();
		expectedObject.integerMap.put("a", 617);
		expectedObject.integerMap.put("b", -633);
		
		expectedObject.shortMap = new HashMap<String, Short>();
		expectedObject.shortMap.put("cc", (short)33);
		expectedObject.shortMap.put("dd", (short)44);
		
		expectedObject.longMap = new HashMap<String, Long>();
		expectedObject.longMap.put("eee", 55l);
		expectedObject.longMap.put("fff", 66l);
		
		expectedObject.byteMap = new HashMap<String, Byte>();
		expectedObject.byteMap.put("gggg", (byte)7);
		expectedObject.byteMap.put("hhhh", (byte)8);
		
		expectedObject.booleanMap = new HashMap<String, Boolean>();
		expectedObject.booleanMap.put("iii", true);
		expectedObject.booleanMap.put("jjj", false);
		
		expectedObject.floatMap = new HashMap<String, Float>();
		expectedObject.floatMap.put("kk", 9.5f);
		expectedObject.floatMap.put("ll", 10.2f);
		
		expectedObject.doubleMap = new HashMap<String, Double>();
		expectedObject.doubleMap.put("m", 1111d);
		expectedObject.doubleMap.put("n", 1212d);
		
		expectedObject.characterMap = new HashMap<String, Character>();
		expectedObject.characterMap.put("o", 't');
		expectedObject.characterMap.put("p", 'u');
		
		expectedObject.stringMap = new HashMap<String, String>();
		expectedObject.stringMap.put("q", "vvvv");
		expectedObject.stringMap.put("r", "wwwwww");
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure intMapStructure = fieldCreate.createFieldBuilder().
				add("a", ScalarType.pvInt).
				add("b", ScalarType.pvInt).
				createStructure();
		
		Structure shortMapStructure = fieldCreate.createFieldBuilder().
				add("cc", ScalarType.pvShort).
				add("dd", ScalarType.pvShort).
				createStructure();
		
		Structure longMapStructure = fieldCreate.createFieldBuilder().
				add("eee", ScalarType.pvLong).
				add("fff", ScalarType.pvLong).
				createStructure();
		
		Structure byteMapStructure = fieldCreate.createFieldBuilder().
				add("gggg", ScalarType.pvByte).
				add("hhhh", ScalarType.pvByte).
				createStructure();
		
		Structure boolMapStructure = fieldCreate.createFieldBuilder().
				add("iii", ScalarType.pvBoolean).
				add("jjj", ScalarType.pvBoolean).
				createStructure();
		
		Structure floatMapStructure = fieldCreate.createFieldBuilder().
				add("kk", ScalarType.pvFloat).
				add("ll", ScalarType.pvFloat).
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
			add("characterMap", charMapStructure).
			add("stringMap", stringMapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVStructure intMapPVStructure = testPVStructure.getStructureField("integerMap");
		PVInt xValue = intMapPVStructure.getSubField(PVInt.class, "a");
		xValue.put(617);
		PVInt yValue = intMapPVStructure.getSubField(PVInt.class, "b");
		yValue.put(-633);
		
		PVStructure shortMapPVStructure = testPVStructure.getStructureField("shortMap");
		PVShort cValue = shortMapPVStructure.getSubField(PVShort.class, "cc");
		cValue.put((short)33);
		PVShort dValue = shortMapPVStructure.getSubField(PVShort.class, "dd");
		dValue.put((short)44);
		
		PVStructure longMapPVStructure = testPVStructure.getStructureField("longMap");
		PVLong eValue = longMapPVStructure.getSubField(PVLong.class, "eee");
		eValue.put(55);
		PVLong fValue = longMapPVStructure.getSubField(PVLong.class, "fff");
		fValue.put(66);
		
		PVStructure byteMapPVStructure = testPVStructure.getStructureField("byteMap");
		PVByte gValue = byteMapPVStructure.getSubField(PVByte.class, "gggg");
		gValue.put((byte)7);
		PVByte hValue = byteMapPVStructure.getSubField(PVByte.class, "hhhh");
		hValue.put((byte)8);
		
		PVStructure boolMapPVStructure = testPVStructure.getStructureField("booleanMap");
		PVBoolean iValue = boolMapPVStructure.getSubField(PVBoolean.class, "iii");
		iValue.put(true);
		PVBoolean jValue = boolMapPVStructure.getSubField(PVBoolean.class, "jjj");
		jValue.put(false);
		
		PVStructure floatMapPVStructure = testPVStructure.getStructureField("floatMap");
		PVFloat kValue = floatMapPVStructure.getSubField(PVFloat.class, "kk");
		kValue.put(9.5f);
		PVFloat lValue = floatMapPVStructure.getSubField(PVFloat.class, "ll");
		lValue.put(10.2f);
		
		PVStructure doubleMapPVStructure = testPVStructure.getStructureField("doubleMap");
		PVDouble mValue = doubleMapPVStructure.getSubField(PVDouble.class, "m");
		mValue.put(1111d);
		PVDouble nValue = doubleMapPVStructure.getSubField(PVDouble.class, "n");
		nValue.put(1212d);
		
		PVStructure charMapPVStructure = testPVStructure.getStructureField("characterMap");
		PVString oValue = charMapPVStructure.getSubField(PVString.class, "o");
		oValue.put("t");
		PVString pValue = charMapPVStructure.getSubField(PVString.class, "p");
		pValue.put("u");
		
		PVStructure stringMapPVStructure = testPVStructure.getStructureField("stringMap");
		PVString qValue = stringMapPVStructure.getSubField(PVString.class, "q");
		qValue.put("vvvv");
		PVString rValue = stringMapPVStructure.getSubField(PVString.class, "r");
		rValue.put("wwwwww");
		
		MapOfPrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfPrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseMapOfWrongType() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				add("x", ScalarType.pvBoolean).
				add("y", ScalarType.pvBoolean).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("integerMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("integerMap");
		
		PVBoolean xValue = mapPVStructure.getSubField(PVBoolean.class, "x");
		xValue.put(true);
		PVBoolean yValue = mapPVStructure.getSubField(PVBoolean.class, "y");
		yValue.put(false);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		try {
			marshaller.fromPVStructure(testPVStructure, MapOfPrimitivesTestClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void testDeserialiseMapOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfObjectsTestClass expectedObject = new MapOfObjectsTestClass();

		MapObjectTestClass testObject1 = new MapObjectTestClass();
		testObject1.primitiveValue = 5432;
		
		MapObjectTestClass testObject2 = new MapObjectTestClass();
		testObject2.primitiveValue = -12345;
		
		expectedObject.objectMap = new LinkedHashMap<String, MapObjectTestClass>();
		expectedObject.objectMap.put("object1", testObject1);
		expectedObject.objectMap.put("secObj", testObject2);
		
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
		
		PVStructure testVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testVStructure.getStructureField("objectMap");
		
		PVStructure object1PVStructure = mapPVStructure.getStructureField("object1");
		PVInt o1XValue = object1PVStructure.getSubField(PVInt.class, "primitiveValue");
		o1XValue.put(5432);
		
		PVStructure secObjPVStructure = mapPVStructure.getStructureField("secObj");
		PVInt o2XValue = secObjPVStructure.getSubField(PVInt.class, "primitiveValue");
		o2XValue.put(-12345);
		
		MapOfObjectsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testVStructure, MapOfObjectsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseMapOfMaps() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfMapsTestClass expectedObject = new MapOfMapsTestClass();
		
		LinkedHashMap<String, Integer> map1 = new LinkedHashMap<String, Integer>();
		map1.put("int1", 1);
		map1.put("int2", 2);
		
		LinkedHashMap<String, Integer> map2 = new LinkedHashMap<String, Integer>();
		map2.put("int3", 3);
		map2.put("int4", 4);
		
		expectedObject.mapMap = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		
		expectedObject.mapMap.put("FirstMap", map1);
		expectedObject.mapMap.put("SecMap", map2);
		
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
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapmapPVStructure = testPVStructure.getStructureField("mapMap");
		
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
		System.out.println(testPVStructure);
		
		MapOfMapsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfMapsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseMapOfArrays() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfArraysTestClass expectedObject = new MapOfArraysTestClass();

		expectedObject.arrayMap = new HashMap<String, Character[]>();
		
		Character[] array1 = new Character[3];
		array1[0] = 'a';
		array1[1] = 'b';
		array1[2] = 'c';

		Character[] array2 = new Character[2];
		array2[0] = 'd';
		array2[1] = 'e';
		
		expectedObject.arrayMap.put("x", array1);
		expectedObject.arrayMap.put("y", array2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", ScalarType.pvString).
				addArray("y", ScalarType.pvString).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("arrayMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("arrayMap");
		
		PVStringArray xArray = mapPVStructure.getSubField(PVStringArray.class, "x");
		xArray.put(0, 3, new String[]{"a", "b", "c"}, 0);
		
		PVStringArray yArray = mapPVStructure.getSubField(PVStringArray.class, "y");
		yArray.put(0, 2, new String[]{"d", "e"}, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseMapOfObjectArrays() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected object
		MapOfObjectArraysTestClass expectedObject = new MapOfObjectArraysTestClass();

		expectedObject.objectArrayMap = new HashMap<String, MapObjectTestClass[]>();
		
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
		
		expectedObject.objectArrayMap.put("x", array1);
		expectedObject.objectArrayMap.put("y", array2);
		
		// Create PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure objStruct = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", objStruct).
				addArray("y", objStruct).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("objectArrayMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("objectArrayMap");
		
		PVStructureArray xArray = mapPVStructure.getSubField(PVStructureArray.class, "x");
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(55);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(66);
		
		PVStructure[] objArray1 = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		xArray.put(0, 2, objArray1, 0);
		
		
		PVStructureArray yArray = mapPVStructure.getSubField(PVStructureArray.class, "y");
		
		PVStructure expectedNestedPVStructure3 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue3 = expectedNestedPVStructure3.getSubField(PVInt.class, "primitiveValue");
		primitiveValue3.put(77);
		PVStructure expectedNestedPVStructure4 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue4 = expectedNestedPVStructure4.getSubField(PVInt.class, "primitiveValue");
		primitiveValue4.put(88);
		PVStructure expectedNestedPVStructure5 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue5 = expectedNestedPVStructure5.getSubField(PVInt.class, "primitiveValue");
		primitiveValue5.put(99);
		
		PVStructure[] objArray2 = {expectedNestedPVStructure3, expectedNestedPVStructure4, expectedNestedPVStructure5};
		yArray.put(0, 3, objArray2, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfObjectArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfObjectArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseMapOfLists() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		MapOfListsTestClass expectedObject = new MapOfListsTestClass();

		expectedObject.listMap = new HashMap<String, List<Character>>();
		
		List<Character> list1 = new LinkedList<Character>();
		list1.add('a');
		list1.add('b');
		list1.add('c');

		List<Character> list2 = new ArrayList<Character>();
		list2.add('d');
		list2.add('e');
		
		expectedObject.listMap.put("x", list1);
		expectedObject.listMap.put("y", list2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", ScalarType.pvString).
				addArray("y", ScalarType.pvString).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("listMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("listMap");
		
		PVStringArray xArray = mapPVStructure.getSubField(PVStringArray.class, "x");
		xArray.put(0, 3, new String[]{"a", "b", "c"}, 0);
		
		PVStringArray yArray = mapPVStructure.getSubField(PVStringArray.class, "y");
		yArray.put(0, 2, new String[]{"d", "e"}, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfListsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfListsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseMapOfObjectLists() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected object
		MapOfObjectListsTestClass expectedObject = new MapOfObjectListsTestClass();

		expectedObject.objectListMap = new HashMap<String, List<MapObjectTestClass>>();
		
		List<MapObjectTestClass> list1 = new LinkedList<MapObjectTestClass>();
		MapObjectTestClass obj1 = new MapObjectTestClass();
		obj1.primitiveValue = 55;
		MapObjectTestClass obj2 = new MapObjectTestClass();
		obj2.primitiveValue = 66;
		list1.add(obj1);
		list1.add(obj2);

		List<MapObjectTestClass> list2 = new LinkedList<MapObjectTestClass>();
		MapObjectTestClass obj3 = new MapObjectTestClass();
		obj3.primitiveValue = 77;
		MapObjectTestClass obj4 = new MapObjectTestClass();
		obj4.primitiveValue = 88;
		MapObjectTestClass obj5 = new MapObjectTestClass();
		obj5.primitiveValue = 99;
		list2.add(obj3);
		list2.add(obj4);
		list2.add(obj5);
		
		expectedObject.objectListMap.put("x", list1);
		expectedObject.objectListMap.put("y", list2);
		
		// Create PVStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure objStruct = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", objStruct).
				addArray("y", objStruct).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("objectListMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("objectListMap");
		
		PVStructureArray xArray = mapPVStructure.getSubField(PVStructureArray.class, "x");
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(55);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(66);
		
		PVStructure[] objArray1 = {expectedNestedPVStructure1, expectedNestedPVStructure2};
		xArray.put(0, 2, objArray1, 0);
		
		
		PVStructureArray yArray = mapPVStructure.getSubField(PVStructureArray.class, "y");
		
		PVStructure expectedNestedPVStructure3 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue3 = expectedNestedPVStructure3.getSubField(PVInt.class, "primitiveValue");
		primitiveValue3.put(77);
		PVStructure expectedNestedPVStructure4 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue4 = expectedNestedPVStructure4.getSubField(PVInt.class, "primitiveValue");
		primitiveValue4.put(88);
		PVStructure expectedNestedPVStructure5 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue5 = expectedNestedPVStructure5.getSubField(PVInt.class, "primitiveValue");
		primitiveValue5.put(99);
		
		PVStructure[] objArray2 = {expectedNestedPVStructure3, expectedNestedPVStructure4, expectedNestedPVStructure5};
		yArray.put(0, 3, objArray2, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfObjectListsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfObjectListsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseUnknownMap() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				add("x", ScalarType.pvInt).
				add("y", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("integerMap", mapStructure).
			add("unknownMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("integerMap");
		
		PVInt xValue = mapPVStructure.getSubField(PVInt.class, "x");
		xValue.put(617);
		PVInt yValue = mapPVStructure.getSubField(PVInt.class, "y");
		yValue.put(-633);
		
		PVStructure unknownMapPVStructure = testPVStructure.getStructureField("unknownMap");
		
		PVInt uxValue = unknownMapPVStructure.getSubField(PVInt.class, "x");
		uxValue.put(123);
		PVInt uyValue = unknownMapPVStructure.getSubField(PVInt.class, "y");
		uyValue.put(-456);
				
		try {
			marshaller.fromPVStructure(testPVStructure, MapOfPrimitivesTestClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("unknownMap"));
		}
	}
	
	@Test
	public void testDeserialiseUnknownMapWithIgnore() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test structure to deserialise
		MapOfPrimitivesTestClass expectedObject = new MapOfPrimitivesTestClass();

		expectedObject.integerMap = new HashMap<String, Integer>();
		expectedObject.integerMap.put("x", 617);
		expectedObject.integerMap.put("y", -633);
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				add("x", ScalarType.pvInt).
				add("y", ScalarType.pvInt).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("integerMap", mapStructure).
			add("unknownMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("integerMap");
		
		PVInt xValue = mapPVStructure.getSubField(PVInt.class, "x");
		xValue.put(617);
		PVInt yValue = mapPVStructure.getSubField(PVInt.class, "y");
		yValue.put(-633);
		
		PVStructure unknownMapPVStructure = testPVStructure.getStructureField("unknownMap");
		
		PVInt uxValue = unknownMapPVStructure.getSubField(PVInt.class, "x");
		uxValue.put(123);
		PVInt uyValue = unknownMapPVStructure.getSubField(PVInt.class, "y");
		uyValue.put(-456);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(testPVStructure);
		
		MapOfPrimitivesTestClass deserialisedObject = null;
		
		try {
			marshaller.setIgnoreUnknownFields(true);
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfPrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseMapOfUnionPrimitives() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test structure to deserialise
		MapOfPrimitivesTestClass expectedObject = new MapOfPrimitivesTestClass();

		expectedObject.integerMap = new HashMap<String, Integer>();
		expectedObject.integerMap.put("x", 617);
		expectedObject.integerMap.put("y", -633);
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				add("x", union).
				add("y", union).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("integerMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("integerMap");
		
		Scalar scalar = fieldCreate.createScalar(ScalarType.pvInt);
		PVScalar pvs1 = pvDataCreate.createPVScalar(scalar);
		PVInt pvi1 = (PVInt)pvs1;
		pvi1.put(617);
		PVUnion xValue = mapPVStructure.getSubField(PVUnion.class, "x");
		xValue.set(pvi1);
		
		PVScalar pvs2 = pvDataCreate.createPVScalar(scalar);
		PVInt pvi2 = (PVInt)pvs2;
		pvi2.put(-633);
		PVUnion yValue = mapPVStructure.getSubField(PVUnion.class, "y");
		yValue.set(pvi2);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfPrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfPrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseMapOfUnionObjects() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		MapOfObjectsTestClass expectedObject = new MapOfObjectsTestClass();

		expectedObject.objectMap = new HashMap<String, MapObjectTestClass>();
		MapObjectTestClass obj1 = new MapObjectTestClass();
		obj1.primitiveValue = 123;
		MapObjectTestClass obj2 = new MapObjectTestClass();
		obj2.primitiveValue = 456;
		expectedObject.objectMap.put("x", obj1);
		expectedObject.objectMap.put("y", obj2);
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				add("x", union).
				add("y", union).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("objectMap", mapStructure).
			createStructure();
		
		Structure mapObjectTestStructure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("objectMap");
		
		PVStructure pvs1 = pvDataCreate.createPVStructure(mapObjectTestStructure);
		PVInt pvs1i = pvs1.getIntField("primitiveValue");
		pvs1i.put(123);
		PVUnion xValue = mapPVStructure.getSubField(PVUnion.class, "x");
		xValue.set(pvs1);

		PVStructure pvs2 = pvDataCreate.createPVStructure(mapObjectTestStructure);
		PVInt pvs2i = pvs2.getIntField("primitiveValue");
		pvs2i.put(456);
		PVUnion yValue = mapPVStructure.getSubField(PVUnion.class, "y");
		yValue.set(pvs2);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(testPVStructure);
		
		MapOfObjectsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfObjectsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseMapOfUnionPrimitivesArray() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test structure to deserialise
		MapOfArraysTestClass expectedObject = new MapOfArraysTestClass();

		expectedObject.arrayMap = new HashMap<String, Character[]>();
		
		Character[] array1 = new Character[3];
		array1[0] = 'a';
		array1[1] = 'b';
		array1[2] = 'c';

		Character[] array2 = new Character[2];
		array2[0] = 'd';
		array2[1] = 'e';
		
		expectedObject.arrayMap.put("x", array1);
		expectedObject.arrayMap.put("y", array2);
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", union).
				addArray("y", union).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("arrayMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("arrayMap");
		
		Scalar scalar = fieldCreate.createScalar(ScalarType.pvString);
		PVScalar pvs1 = pvDataCreate.createPVScalar(scalar);
		PVString pvss1 = (PVString)pvs1;
		pvss1.put("a");
		PVUnion u1Value = pvDataCreate.createPVUnion(union);
		u1Value.set(pvss1);
		PVScalar pvs2 = pvDataCreate.createPVScalar(scalar);
		PVString pvss2 = (PVString)pvs2;
		pvss2.put("b");
		PVUnion u2Value = pvDataCreate.createPVUnion(union);
		u2Value.set(pvss2);
		PVScalar pvs3 = pvDataCreate.createPVScalar(scalar);
		PVString pvss3 = (PVString)pvs3;
		pvss3.put("c");
		PVUnion u3Value = pvDataCreate.createPVUnion(union);
		u3Value.set(pvss3);
		
		PVUnion[] pvUnion1 = new PVUnion[3];
		pvUnion1[0] = u1Value;
		pvUnion1[1] = u2Value;
		pvUnion1[2] = u3Value;
		
		PVUnionArray xValue = mapPVStructure.getSubField(PVUnionArray.class, "x");
		xValue.put(0, 3, pvUnion1, 0);
		

		PVScalar pvs4 = pvDataCreate.createPVScalar(scalar);
		PVString pvss4 = (PVString)pvs4;
		pvss4.put("d");
		PVUnion u4Value = pvDataCreate.createPVUnion(union);
		u4Value.set(pvss4);
		PVScalar pvs5 = pvDataCreate.createPVScalar(scalar);
		PVString pvss5 = (PVString)pvs5;
		pvss5.put("e");
		PVUnion u5Value = pvDataCreate.createPVUnion(union);
		u5Value.set(pvss5);
		
		PVUnion[] pvUnion2 = new PVUnion[2];
		pvUnion2[0] = u4Value;
		pvUnion2[1] = u5Value;
		
		PVUnionArray yValue = mapPVStructure.getSubField(PVUnionArray.class, "y");
		yValue.put(0, 2, pvUnion2, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseMapOfUnionObjectArrays() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected object
		MapOfObjectArraysTestClass expectedObject = new MapOfObjectArraysTestClass();

		expectedObject.objectArrayMap = new HashMap<String, MapObjectTestClass[]>();
		
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
		
		expectedObject.objectArrayMap.put("x", array1);
		expectedObject.objectArrayMap.put("y", array2);
		
		// Create PVStructure to deserialise
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
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("objectArrayMap");
		
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(55);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(66);

		PVUnion u1Value = pvDataCreate.createPVUnion(union);
		u1Value.set(expectedNestedPVStructure1);
		PVUnion u2Value = pvDataCreate.createPVUnion(union);
		u2Value.set(expectedNestedPVStructure2);
		
		PVUnion[] pvUnion1 = new PVUnion[2];
		pvUnion1[0] = u1Value;
		pvUnion1[1] = u2Value;
		
		PVUnionArray xValue = mapPVStructure.getSubField(PVUnionArray.class, "x");
		xValue.put(0, 2, pvUnion1, 0);
		
		PVStructure expectedNestedPVStructure3 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue3 = expectedNestedPVStructure3.getSubField(PVInt.class, "primitiveValue");
		primitiveValue3.put(77);
		PVStructure expectedNestedPVStructure4 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue4 = expectedNestedPVStructure4.getSubField(PVInt.class, "primitiveValue");
		primitiveValue4.put(88);
		PVStructure expectedNestedPVStructure5 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue5 = expectedNestedPVStructure5.getSubField(PVInt.class, "primitiveValue");
		primitiveValue5.put(99);


		PVUnion u3Value = pvDataCreate.createPVUnion(union);
		u3Value.set(expectedNestedPVStructure3);
		PVUnion u4Value = pvDataCreate.createPVUnion(union);
		u4Value.set(expectedNestedPVStructure4);
		PVUnion u5Value = pvDataCreate.createPVUnion(union);
		u5Value.set(expectedNestedPVStructure5);
		
		PVUnion[] pvUnion2 = new PVUnion[3];
		pvUnion2[0] = u3Value;
		pvUnion2[1] = u4Value;
		pvUnion2[2] = u5Value;
		
		PVUnionArray yValue = mapPVStructure.getSubField(PVUnionArray.class, "y");
		yValue.put(0, 3, pvUnion2, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfObjectArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfObjectArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void testDeserialiseMapOfUnionPrimitivesLists() {
		
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test structure to deserialise
		MapOfListsTestClass expectedObject = new MapOfListsTestClass();

		expectedObject.listMap = new LinkedHashMap<String, List<Character>>();
		
		List<Character> list1 = new LinkedList<Character>();
		list1.add('a');
		list1.add('b');
		list1.add('c');

		List<Character> list2 = new LinkedList<Character>();
		list2.add('d');
		list2.add('e');
		
		expectedObject.listMap.put("x", list1);
		expectedObject.listMap.put("y", list2);
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure mapStructure = fieldCreate.createFieldBuilder().
				addArray("x", union).
				addArray("y", union).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("listMap", mapStructure).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("listMap");
		
		Scalar scalar = fieldCreate.createScalar(ScalarType.pvString);
		PVScalar pvs1 = pvDataCreate.createPVScalar(scalar);
		PVString pvss1 = (PVString)pvs1;
		pvss1.put("a");
		PVUnion u1Value = pvDataCreate.createPVUnion(union);
		u1Value.set(pvss1);
		PVScalar pvs2 = pvDataCreate.createPVScalar(scalar);
		PVString pvss2 = (PVString)pvs2;
		pvss2.put("b");
		PVUnion u2Value = pvDataCreate.createPVUnion(union);
		u2Value.set(pvss2);
		PVScalar pvs3 = pvDataCreate.createPVScalar(scalar);
		PVString pvss3 = (PVString)pvs3;
		pvss3.put("c");
		PVUnion u3Value = pvDataCreate.createPVUnion(union);
		u3Value.set(pvss3);
		
		PVUnion[] pvUnion1 = new PVUnion[3];
		pvUnion1[0] = u1Value;
		pvUnion1[1] = u2Value;
		pvUnion1[2] = u3Value;
		
		PVUnionArray xValue = mapPVStructure.getSubField(PVUnionArray.class, "x");
		xValue.put(0, 3, pvUnion1, 0);
		

		PVScalar pvs4 = pvDataCreate.createPVScalar(scalar);
		PVString pvss4 = (PVString)pvs4;
		pvss4.put("d");
		PVUnion u4Value = pvDataCreate.createPVUnion(union);
		u4Value.set(pvss4);
		PVScalar pvs5 = pvDataCreate.createPVScalar(scalar);
		PVString pvss5 = (PVString)pvs5;
		pvss5.put("e");
		PVUnion u5Value = pvDataCreate.createPVUnion(union);
		u5Value.set(pvss5);
		
		PVUnion[] pvUnion2 = new PVUnion[2];
		pvUnion2[0] = u4Value;
		pvUnion2[1] = u5Value;
		
		PVUnionArray yValue = mapPVStructure.getSubField(PVUnionArray.class, "y");
		yValue.put(0, 2, pvUnion2, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfListsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfListsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseMapOfUnionObjectLists() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected object
		MapOfObjectListsTestClass expectedObject = new MapOfObjectListsTestClass();

		expectedObject.objectListMap = new HashMap<String, List<MapObjectTestClass>>();
		
		List<MapObjectTestClass> list1 = new LinkedList<MapObjectTestClass>();
		MapObjectTestClass obj1 = new MapObjectTestClass();
		obj1.primitiveValue = 55;
		MapObjectTestClass obj2 = new MapObjectTestClass();
		obj2.primitiveValue = 66;
		list1.add(obj1);
		list1.add(obj2);

		List<MapObjectTestClass> list2 = new LinkedList<MapObjectTestClass>();
		MapObjectTestClass obj3 = new MapObjectTestClass();
		obj3.primitiveValue = 77;
		MapObjectTestClass obj4 = new MapObjectTestClass();
		obj4.primitiveValue = 88;
		MapObjectTestClass obj5 = new MapObjectTestClass();
		obj5.primitiveValue = 99;
		list2.add(obj3);
		list2.add(obj4);
		list2.add(obj5);
		
		expectedObject.objectListMap.put("x", list1);
		expectedObject.objectListMap.put("y", list2);
		
		// Create PVStructure to deserialise
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
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStructure mapPVStructure = testPVStructure.getStructureField("objectListMap");
		
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "primitiveValue");
		primitiveValue1.put(55);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "primitiveValue");
		primitiveValue2.put(66);

		PVUnion u1Value = pvDataCreate.createPVUnion(union);
		u1Value.set(expectedNestedPVStructure1);
		PVUnion u2Value = pvDataCreate.createPVUnion(union);
		u2Value.set(expectedNestedPVStructure2);
		
		PVUnion[] pvUnion1 = new PVUnion[2];
		pvUnion1[0] = u1Value;
		pvUnion1[1] = u2Value;
		
		PVUnionArray xValue = mapPVStructure.getSubField(PVUnionArray.class, "x");
		xValue.put(0, 2, pvUnion1, 0);
		
		PVStructure expectedNestedPVStructure3 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue3 = expectedNestedPVStructure3.getSubField(PVInt.class, "primitiveValue");
		primitiveValue3.put(77);
		PVStructure expectedNestedPVStructure4 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue4 = expectedNestedPVStructure4.getSubField(PVInt.class, "primitiveValue");
		primitiveValue4.put(88);
		PVStructure expectedNestedPVStructure5 = pvDataCreate.createPVStructure(objStruct);
		PVInt primitiveValue5 = expectedNestedPVStructure5.getSubField(PVInt.class, "primitiveValue");
		primitiveValue5.put(99);


		PVUnion u3Value = pvDataCreate.createPVUnion(union);
		u3Value.set(expectedNestedPVStructure3);
		PVUnion u4Value = pvDataCreate.createPVUnion(union);
		u4Value.set(expectedNestedPVStructure4);
		PVUnion u5Value = pvDataCreate.createPVUnion(union);
		u5Value.set(expectedNestedPVStructure5);
		
		PVUnion[] pvUnion2 = new PVUnion[3];
		pvUnion2[0] = u3Value;
		pvUnion2[1] = u4Value;
		pvUnion2[2] = u5Value;
		
		PVUnionArray yValue = mapPVStructure.getSubField(PVUnionArray.class, "y");
		yValue.put(0, 3, pvUnion2, 0);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");;
		System.out.println(testPVStructure);
		
		MapOfObjectListsTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, MapOfObjectListsTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserializeMapWithTypeId() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// create a map to deserialise directly
		Map<String, Object> expectedObject = new HashMap<>();
		expectedObject.put("name", "fred");
		expectedObject.put("age", 52);
		expectedObject.put("doubleVal", 123.456);
		expectedObject.put("typeid", "type/person");
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure = fieldCreate.createFieldBuilder()
				.add("name", ScalarType.pvString)
				.add("age", ScalarType.pvInt)
				.add("doubleVal", ScalarType.pvDouble)
				.setId("type/person")
				.createStructure();
		
		PVStructure testPvStructure = pvDataCreate.createPVStructure(structure);
		testPvStructure.getStringField("name").put("fred");
		testPvStructure.getIntField("age").put(52);
		testPvStructure.getDoubleField("doubleVal").put(123.456);
		
		Object deserialisedObject = null;
		try {
			marshaller.registerMapTypeIdKey("typeid");
			deserialisedObject = marshaller.fromPVStructure(testPvStructure, null); // TODO or call getMapDeserializer()
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} 
		
		assertEquals(expectedObject, deserialisedObject);
	}

	public static class MapOfPrimitivesTestClass {
		Map<String, Integer> integerMap;
		Map<String, Short> shortMap;
		Map<String, Long> longMap;
		Map<String, Byte> byteMap;
		Map<String, Boolean> booleanMap;
		Map<String, Float> floatMap;
		Map<String, Double> doubleMap;
		Map<String, Character> characterMap;
		Map<String, String> stringMap;
		public Map<String, Integer> getIntegerMap() {
			return integerMap;
		}
		public void setIntegerMap(Map<String, Integer> integerMap) {
			this.integerMap = integerMap;
		}
		public Map<String, Short> getShortMap() {
			return shortMap;
		}
		public void setShortMap(Map<String, Short> shortMap) {
			this.shortMap = shortMap;
		}
		public Map<String, Long> getLongMap() {
			return longMap;
		}
		public void setLongMap(Map<String, Long> longMap) {
			this.longMap = longMap;
		}
		public Map<String, Byte> getByteMap() {
			return byteMap;
		}
		public void setByteMap(Map<String, Byte> byteMap) {
			this.byteMap = byteMap;
		}
		public Map<String, Boolean> getBooleanMap() {
			return booleanMap;
		}
		public void setBooleanMap(Map<String, Boolean> booleanMap) {
			this.booleanMap = booleanMap;
		}
		public Map<String, Float> getFloatMap() {
			return floatMap;
		}
		public void setFloatMap(Map<String, Float> floatMap) {
			this.floatMap = floatMap;
		}
		public Map<String, Double> getDoubleMap() {
			return doubleMap;
		}
		public void setDoubleMap(Map<String, Double> doubleMap) {
			this.doubleMap = doubleMap;
		}
		public Map<String, Character> getCharacterMap() {
			return characterMap;
		}
		public void setCharacterMap(Map<String, Character> characterMap) {
			this.characterMap = characterMap;
		}
		public Map<String, String> getStringMap() {
			return stringMap;
		}
		public void setStringMap(Map<String, String> stringMap) {
			this.stringMap = stringMap;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((booleanMap == null) ? 0 : booleanMap.hashCode());
			result = prime * result + ((byteMap == null) ? 0 : byteMap.hashCode());
			result = prime * result + ((characterMap == null) ? 0 : characterMap.hashCode());
			result = prime * result + ((doubleMap == null) ? 0 : doubleMap.hashCode());
			result = prime * result + ((floatMap == null) ? 0 : floatMap.hashCode());
			result = prime * result + ((integerMap == null) ? 0 : integerMap.hashCode());
			result = prime * result + ((longMap == null) ? 0 : longMap.hashCode());
			result = prime * result + ((shortMap == null) ? 0 : shortMap.hashCode());
			result = prime * result + ((stringMap == null) ? 0 : stringMap.hashCode());
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
			MapOfPrimitivesTestClass other = (MapOfPrimitivesTestClass) obj;
			if (booleanMap == null) {
				if (other.booleanMap != null)
					return false;
			} else if (!booleanMap.equals(other.booleanMap))
				return false;
			if (byteMap == null) {
				if (other.byteMap != null)
					return false;
			} else if (!byteMap.equals(other.byteMap))
				return false;
			if (characterMap == null) {
				if (other.characterMap != null)
					return false;
			} else if (!characterMap.equals(other.characterMap))
				return false;
			if (doubleMap == null) {
				if (other.doubleMap != null)
					return false;
			} else if (!doubleMap.equals(other.doubleMap))
				return false;
			if (floatMap == null) {
				if (other.floatMap != null)
					return false;
			} else if (!floatMap.equals(other.floatMap))
				return false;
			if (integerMap == null) {
				if (other.integerMap != null)
					return false;
			} else if (!integerMap.equals(other.integerMap))
				return false;
			if (longMap == null) {
				if (other.longMap != null)
					return false;
			} else if (!longMap.equals(other.longMap))
				return false;
			if (shortMap == null) {
				if (other.shortMap != null)
					return false;
			} else if (!shortMap.equals(other.shortMap))
				return false;
			if (stringMap == null) {
				if (other.stringMap != null)
					return false;
			} else if (!stringMap.equals(other.stringMap))
				return false;
			return true;
		}
	}
	
	public static class MapObjectTestClass
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
			MapObjectTestClass other = (MapObjectTestClass) obj;
			if (primitiveValue != other.primitiveValue)
				return false;
			return true;
		}
	}
	
	public static class MapOfObjectsTestClass {
		Map<String, MapObjectTestClass> objectMap;

		public Map<String, MapObjectTestClass> getObjectMap() {
			return objectMap;
		}

		public void setObjectMap(Map<String, MapObjectTestClass> objectMap) {
			this.objectMap = objectMap;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((objectMap == null) ? 0 : objectMap.hashCode());
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
			MapOfObjectsTestClass other = (MapOfObjectsTestClass) obj;
			if (objectMap == null) {
				if (other.objectMap != null)
					return false;
			} else if (!objectMap.equals(other.objectMap))
				return false;
			return true;
		}
	}
	
	public static class MapOfMapsTestClass {
		LinkedHashMap<String, LinkedHashMap<String, Integer>> mapMap;

		public LinkedHashMap<String, LinkedHashMap<String, Integer>> getMapMap() {
			return mapMap;
		}

		public void setMapMap(LinkedHashMap<String, LinkedHashMap<String, Integer>> mapMap) {
			this.mapMap = mapMap;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((mapMap == null) ? 0 : mapMap.hashCode());
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
			MapOfMapsTestClass other = (MapOfMapsTestClass) obj;
			if (mapMap == null) {
				if (other.mapMap != null)
					return false;
			} else if (!mapMap.equals(other.mapMap))
				return false;
			return true;
		}
	}
	
	public static class MapOfArraysTestClass {
		Map<String, Character[]> arrayMap;

		public Map<String, Character[]> getArrayMap() {
			return arrayMap;
		}

		public void setArrayMap(Map<String, Character[]> arrayMap) {
			this.arrayMap = arrayMap;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((arrayMap == null) ? 0 : arrayMap.hashCode());
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
			MapOfArraysTestClass other = (MapOfArraysTestClass) obj;
			if (arrayMap == null) {
				if (other.arrayMap != null)
					return false;
			} else {
				if (arrayMap.size() != other.arrayMap.size()) {
					return false;
				}
				for (String key : arrayMap.keySet()) {
					if (!other.arrayMap.containsKey(key)) {
						return false;
					} else {
						if (!Arrays.equals(arrayMap.get(key), other.arrayMap.get(key))) {
							return false;
						}
					}
				}
			}
			return true;
		}
	}
	
	public static class MapOfObjectArraysTestClass {
		Map<String, MapObjectTestClass[]> objectArrayMap;

		public Map<String, MapObjectTestClass[]> getObjectArrayMap() {
			return objectArrayMap;
		}

		public void setObjectArrayMap(Map<String, MapObjectTestClass[]> objectArrayMap) {
			this.objectArrayMap = objectArrayMap;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((objectArrayMap == null) ? 0 : objectArrayMap.hashCode());
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
			MapOfObjectArraysTestClass other = (MapOfObjectArraysTestClass) obj;
			if (objectArrayMap == null) {
				if (other.objectArrayMap != null)
					return false;
			} else {
				if (objectArrayMap.size() != other.objectArrayMap.size()) {
					return false;
				}
				for (String key : objectArrayMap.keySet()) {
					if (!other.objectArrayMap.containsKey(key)) {
						return false;
					} else {
						if (!Arrays.equals(objectArrayMap.get(key), other.objectArrayMap.get(key))) {
							return false;
						}
					}
				}
			}
			return true;
		}
	}
	
	public static class MapOfListsTestClass {
		Map<String, List<Character>> listMap;

		public Map<String, List<Character>> getListMap() {
			return listMap;
		}

		public void setListMap(Map<String, List<Character>> listMap) {
			this.listMap = listMap;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((listMap == null) ? 0 : listMap.hashCode());
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
			MapOfListsTestClass other = (MapOfListsTestClass) obj;
			if (listMap == null) {
				if (other.listMap != null)
					return false;
			} else if (!listMap.equals(other.listMap))
				return false;
			return true;
		}
	}
	
	public static class MapOfObjectListsTestClass {
		Map<String, List<MapObjectTestClass>> objectListMap;

		public Map<String, List<MapObjectTestClass>> getObjectListMap() {
			return objectListMap;
		}

		public void setObjectListMap(Map<String, List<MapObjectTestClass>> objectListMap) {
			this.objectListMap = objectListMap;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((objectListMap == null) ? 0 : objectListMap.hashCode());
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
			MapOfObjectListsTestClass other = (MapOfObjectListsTestClass) obj;
			if (objectListMap == null) {
				if (other.objectListMap != null)
					return false;
			} else if (!objectListMap.equals(other.objectListMap))
				return false;
			return true;
		}
		
	}

}
