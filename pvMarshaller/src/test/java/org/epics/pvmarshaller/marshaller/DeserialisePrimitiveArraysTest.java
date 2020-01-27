package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVBooleanArray;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVLongArray;
import org.epics.pvdata.pv.PVShortArray;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUByteArray;
import org.epics.pvdata.pv.PVUIntArray;
import org.epics.pvdata.pv.PVULongArray;
import org.epics.pvdata.pv.PVUShortArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class DeserialisePrimitiveArraysTest {

	@Test
	public void DeserialiseIntegerArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.primitiveIntArray = new int[3];
		expectedObject.primitiveIntArray[0] = 11;
		expectedObject.primitiveIntArray[1] = 22;
		expectedObject.primitiveIntArray[2] = 33;
		expectedObject.wrapperIntArray = new Integer[2];
		expectedObject.wrapperIntArray[0] = 44;
		expectedObject.wrapperIntArray[1] = 55;
		expectedObject.wrapperIntList = new ArrayList<Integer>();
		expectedObject.wrapperIntList.add(66);
		expectedObject.wrapperIntList.add(77);
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveIntArray", ScalarType.pvInt).
			addArray("wrapperIntArray", ScalarType.pvInt).
			addArray("wrapperIntList", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveArrayValue = testPVStructure.getSubField(PVIntArray.class, "primitiveIntArray");
		int[] intArray = {11, 22, 33};
		primitiveArrayValue.put(0, 3, intArray, 0);
		PVIntArray wrapperArrayValue = testPVStructure.getSubField(PVIntArray.class, "wrapperIntArray");
		int[] integerArray = {44, 55};
		wrapperArrayValue.put(0, 2, integerArray, 0);
		PVIntArray wrapperListValue = testPVStructure.getSubField(PVIntArray.class, "wrapperIntList");
		int[] integerList = {66, 77};
		wrapperListValue.put(0, 2, integerList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseShortArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.primitiveShortArray = new short[3];
		expectedObject.primitiveShortArray[0] = 111;
		expectedObject.primitiveShortArray[1] = 222;
		expectedObject.primitiveShortArray[2] = 333;
		expectedObject.wrapperShortArray = new Short[2];
		expectedObject.wrapperShortArray[0] = 444;
		expectedObject.wrapperShortArray[1] = 555;
		expectedObject.wrapperShortList = new LinkedList<Short>();
		expectedObject.wrapperShortList.add((short)666);
		expectedObject.wrapperShortList.add((short)777);
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveShortArray", ScalarType.pvShort).
			addArray("wrapperShortArray", ScalarType.pvShort).
			addArray("wrapperShortList", ScalarType.pvShort).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVShortArray primitiveArrayValue = testPVStructure.getSubField(PVShortArray.class, "primitiveShortArray");
		short[] shortArray = {111, 222, 333};
		primitiveArrayValue.put(0, 3, shortArray, 0);
		PVShortArray wrapperArrayValue = testPVStructure.getSubField(PVShortArray.class, "wrapperShortArray");
		short[] shortobjArray = {444, 555};
		wrapperArrayValue.put(0, 2, shortobjArray, 0);
		PVShortArray wrapperListValue = testPVStructure.getSubField(PVShortArray.class, "wrapperShortList");
		short[] shortList = {666, 777};
		wrapperListValue.put(0, 2, shortList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseLongArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.primitiveLongArray = new long[3];
		expectedObject.primitiveLongArray[0] = 111;
		expectedObject.primitiveLongArray[1] = 222;
		expectedObject.primitiveLongArray[2] = 333;
		expectedObject.wrapperLongArray = new Long[2];
		expectedObject.wrapperLongArray[0] = 444l;
		expectedObject.wrapperLongArray[1] = 555l;
		expectedObject.wrapperLongList = new LinkedList<Long>();
		expectedObject.wrapperLongList.add(666l);
		expectedObject.wrapperLongList.add(777l);
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveLongArray", ScalarType.pvLong).
			addArray("wrapperLongArray", ScalarType.pvLong).
			addArray("wrapperLongList", ScalarType.pvLong).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVLongArray primitiveArrayValue = testPVStructure.getSubField(PVLongArray.class, "primitiveLongArray");
		long[] longArray = {111, 222, 333};
		primitiveArrayValue.put(0, 3, longArray, 0);
		PVLongArray wrapperArrayValue = testPVStructure.getSubField(PVLongArray.class, "wrapperLongArray");
		long[] longobjArray = {444, 555};
		wrapperArrayValue.put(0, 2, longobjArray, 0);
		PVLongArray wrapperListValue = testPVStructure.getSubField(PVLongArray.class, "wrapperLongList");
		long[] longList = {666, 777};
		wrapperListValue.put(0, 2, longList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseByteArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.primitiveByteArray = new byte[3];
		expectedObject.primitiveByteArray[0] = 11;
		expectedObject.primitiveByteArray[1] = 22;
		expectedObject.primitiveByteArray[2] = 33;
		expectedObject.wrapperByteArray = new Byte[2];
		expectedObject.wrapperByteArray[0] = 44;
		expectedObject.wrapperByteArray[1] = 55;
		expectedObject.wrapperByteList = new LinkedList<Byte>();
		expectedObject.wrapperByteList.add((byte)66);
		expectedObject.wrapperByteList.add((byte)77);
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveByteArray", ScalarType.pvByte).
			addArray("wrapperByteArray", ScalarType.pvByte).
			addArray("wrapperByteList", ScalarType.pvByte).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVByteArray primitiveArrayValue = testPVStructure.getSubField(PVByteArray.class, "primitiveByteArray");
		byte[] byteArray = {11, 22, 33};
		primitiveArrayValue.put(0, 3, byteArray, 0);
		PVByteArray wrapperArrayValue = testPVStructure.getSubField(PVByteArray.class, "wrapperByteArray");
		byte[] byteobjArray = {44, 55};
		wrapperArrayValue.put(0, 2, byteobjArray, 0);
		PVByteArray wrapperListValue = testPVStructure.getSubField(PVByteArray.class, "wrapperByteList");
		byte[] byteList = {66, 77};
		wrapperListValue.put(0, 2, byteList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseBooleanArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.primitiveBooleanArray = new boolean[3];
		expectedObject.primitiveBooleanArray[0] = false;
		expectedObject.primitiveBooleanArray[1] = true;
		expectedObject.primitiveBooleanArray[2] = true;
		expectedObject.wrapperBooleanArray = new Boolean[2];
		expectedObject.wrapperBooleanArray[0] = true;
		expectedObject.wrapperBooleanArray[1] = false;
		expectedObject.wrapperBooleanList = new LinkedList<Boolean>();
		expectedObject.wrapperBooleanList.add(true);
		expectedObject.wrapperBooleanList.add(false);
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveBooleanArray", ScalarType.pvBoolean).
			addArray("wrapperBooleanArray", ScalarType.pvBoolean).
			addArray("wrapperBooleanList", ScalarType.pvBoolean).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVBooleanArray primitiveArrayValue = testPVStructure.getSubField(PVBooleanArray.class, "primitiveBooleanArray");
		boolean[] booleanArray = {false, true, true};
		primitiveArrayValue.put(0, 3, booleanArray, 0);
		PVBooleanArray wrapperArrayValue = testPVStructure.getSubField(PVBooleanArray.class, "wrapperBooleanArray");
		boolean[] booleanobjArray = {true, false};
		wrapperArrayValue.put(0, 2, booleanobjArray, 0);
		PVBooleanArray wrapperListValue = testPVStructure.getSubField(PVBooleanArray.class, "wrapperBooleanList");
		boolean[] booleanList = {true, false};
		wrapperListValue.put(0, 2, booleanList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseFloatArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.primitiveFloatArray = new float[3];
		expectedObject.primitiveFloatArray[0] = 111f;
		expectedObject.primitiveFloatArray[1] = 222f;
		expectedObject.primitiveFloatArray[2] = 333f;
		expectedObject.wrapperFloatArray = new Float[2];
		expectedObject.wrapperFloatArray[0] = 444f;
		expectedObject.wrapperFloatArray[1] = 555f;
		expectedObject.wrapperFloatList = new LinkedList<Float>();
		expectedObject.wrapperFloatList.add(666f);
		expectedObject.wrapperFloatList.add(777f);
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveFloatArray", ScalarType.pvFloat).
			addArray("wrapperFloatArray", ScalarType.pvFloat).
			addArray("wrapperFloatList", ScalarType.pvFloat).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVFloatArray primitiveArrayValue = testPVStructure.getSubField(PVFloatArray.class, "primitiveFloatArray");
		float[] floatArray = {111, 222, 333};
		primitiveArrayValue.put(0, 3, floatArray, 0);
		PVFloatArray wrapperArrayValue = testPVStructure.getSubField(PVFloatArray.class, "wrapperFloatArray");
		float[] floatobjArray = {444, 555};
		wrapperArrayValue.put(0, 2, floatobjArray, 0);
		PVFloatArray wrapperListValue = testPVStructure.getSubField(PVFloatArray.class, "wrapperFloatList");
		float[] floatList = {666, 777};
		wrapperListValue.put(0, 2, floatList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseDoubleArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.primitiveDoubleArray = new double[3];
		expectedObject.primitiveDoubleArray[0] = 111;
		expectedObject.primitiveDoubleArray[1] = 222;
		expectedObject.primitiveDoubleArray[2] = 333;
		expectedObject.wrapperDoubleArray = new Double[2];
		expectedObject.wrapperDoubleArray[0] = 444d;
		expectedObject.wrapperDoubleArray[1] = 555d;
		expectedObject.wrapperDoubleList = new LinkedList<Double>();
		expectedObject.wrapperDoubleList.add((double)666);
		expectedObject.wrapperDoubleList.add((double)777);
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveDoubleArray", ScalarType.pvDouble).
			addArray("wrapperDoubleArray", ScalarType.pvDouble).
			addArray("wrapperDoubleList", ScalarType.pvDouble).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVDoubleArray primitiveArrayValue = testPVStructure.getSubField(PVDoubleArray.class, "primitiveDoubleArray");
		double[] doubleArray = {111, 222, 333};
		primitiveArrayValue.put(0, 3, doubleArray, 0);
		PVDoubleArray wrapperArrayValue = testPVStructure.getSubField(PVDoubleArray.class, "wrapperDoubleArray");
		double[] doubleobjArray = {444, 555};
		wrapperArrayValue.put(0, 2, doubleobjArray, 0);
		PVDoubleArray wrapperListValue = testPVStructure.getSubField(PVDoubleArray.class, "wrapperDoubleList");
		double[] doubleList = {666, 777};
		wrapperListValue.put(0, 2, doubleList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseCharArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.primitiveCharArray = new char[3];
		expectedObject.primitiveCharArray[0] = 'a';
		expectedObject.primitiveCharArray[1] = 'b';
		expectedObject.primitiveCharArray[2] = 'c';
		expectedObject.wrapperCharArray = new Character[2];
		expectedObject.wrapperCharArray[0] = 'd';
		expectedObject.wrapperCharArray[1] = 'e';
		expectedObject.wrapperCharList = new LinkedList<Character>();
		expectedObject.wrapperCharList.add('f');
		expectedObject.wrapperCharList.add('g');
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveCharArray", ScalarType.pvString).
			addArray("wrapperCharArray", ScalarType.pvString).
			addArray("wrapperCharList", ScalarType.pvString).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStringArray primitiveArrayValue = testPVStructure.getSubField(PVStringArray.class, "primitiveCharArray");
		String[] charArray = {"a", "b", "c"};
		primitiveArrayValue.put(0, 3, charArray, 0);
		PVStringArray wrapperArrayValue = testPVStructure.getSubField(PVStringArray.class, "wrapperCharArray");
		String[] charObjArray = {"d", "e"};
		wrapperArrayValue.put(0, 2, charObjArray, 0);
		PVStringArray wrapperListValue = testPVStructure.getSubField(PVStringArray.class, "wrapperCharList");
		String[] charList = {"f", "g"};
		wrapperListValue.put(0, 2, charList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseStringArrayTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();

		expectedObject.wrapperStringArray = new String[2];
		expectedObject.wrapperStringArray[0] = "dd";
		expectedObject.wrapperStringArray[1] = "ee";
		expectedObject.wrapperStringList = new LinkedList<String>();
		expectedObject.wrapperStringList.add("ff");
		expectedObject.wrapperStringList.add("gg");
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("wrapperStringArray", ScalarType.pvString).
			addArray("wrapperStringList", ScalarType.pvString).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVStringArray wrapperArrayValue = testPVStructure.getSubField(PVStringArray.class, "wrapperStringArray");
		String[] StringobjArray = {"dd", "ee"};
		wrapperArrayValue.put(0, 2, StringobjArray, 0);
		PVStringArray wrapperListValue = testPVStructure.getSubField(PVStringArray.class, "wrapperStringList");
		String[] StringList = {"ff", "gg"};
		wrapperListValue.put(0, 2, StringList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseUnsignedIntegerArrayThrowsException() {
		PVMarshaller marshaller = new PVMarshaller();
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveIntArray", ScalarType.pvUInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVUIntArray primitiveArrayValue = testPVStructure.getSubField(PVUIntArray.class, "primitiveIntArray");
		int[] intArray = {11, 22, 33};
		primitiveArrayValue.put(0, 3, intArray, 0);
		
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void DeserialiseUnsignedShortArrayThrowsException() {
		PVMarshaller marshaller = new PVMarshaller();
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveShortArray", ScalarType.pvUShort).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVUShortArray primitiveArrayValue = testPVStructure.getSubField(PVUShortArray.class, "primitiveShortArray");
		short[] intArray = {11, 22, 33};
		primitiveArrayValue.put(0, 3, intArray, 0);
		
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void DeserialiseUnsignedLongArrayThrowsException() {
		PVMarshaller marshaller = new PVMarshaller();
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveLongArray", ScalarType.pvULong).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVULongArray primitiveArrayValue = testPVStructure.getSubField(PVULongArray.class, "primitiveLongArray");
		long[] intArray = {11, 22, 33};
		primitiveArrayValue.put(0, 3, intArray, 0);
		
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void DeserialiseUnsignedByteArrayThrowsException() {
		PVMarshaller marshaller = new PVMarshaller();
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("primitiveByteArray", ScalarType.pvUByte).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVUByteArray primitiveArrayValue = testPVStructure.getSubField(PVUByteArray.class, "primitiveByteArray");
		byte[] intArray = {11, 22, 33};
		primitiveArrayValue.put(0, 3, intArray, 0);
		
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void DeserialiseUnknownIntegerArray() {
		PVMarshaller marshaller = new PVMarshaller();
						
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("unknownIntArray", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveArrayValue = testPVStructure.getSubField(PVIntArray.class, "unknownIntArray");
		int[] intArray = {11, 22, 33};
		primitiveArrayValue.put(0, 3, intArray, 0);
				
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("unknownIntArray"));
		}
	}

	@Test
	public void DeserialiseUnknownIntegerArrayTestCorrectValuesWithIgnore() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected Object
		PrimitivesArraysTestClass expectedObject = new PrimitivesArraysTestClass();
		
		expectedObject.wrapperIntList = new ArrayList<Integer>();
		expectedObject.wrapperIntList.add(66);
		expectedObject.wrapperIntList.add(77);
				
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("unknownIntArray", ScalarType.pvInt).
			addArray("wrapperIntList", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveArrayValue = testPVStructure.getSubField(PVIntArray.class, "unknownIntArray");
		int[] intArray = {11, 22, 33};
		primitiveArrayValue.put(0, 3, intArray, 0);
		PVIntArray wrapperListValue = testPVStructure.getSubField(PVIntArray.class, "wrapperIntList");
		int[] integerList = {66, 77};
		wrapperListValue.put(0, 2, integerList, 0);
		
		PrimitivesArraysTestClass deserialisedObject = null;
		
		try {
			marshaller.setIgnoreUnknownFields(true);
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesArraysTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseCollectionOfIntegersWithList() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test pvstructure to serialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("intCollection", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveValue = testPVStructure.getSubField(PVIntArray.class, "intCollection");
		int[] intArray = {358, 934623, 33, -5};
		primitiveValue.put(0, 4, intArray, 0);
		
		// Create expected object
		CollectionOfIntegersTestClass expectedObject = new CollectionOfIntegersTestClass();

		expectedObject.intCollection = new LinkedList<Integer>();
		
		expectedObject.intCollection.add(358);
		expectedObject.intCollection.add(934623);
		expectedObject.intCollection.add(33);
		expectedObject.intCollection.add(-5);
		
		
		CollectionOfIntegersTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, CollectionOfIntegersTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	public static class PrimitivesArraysTestClass
	{		
		private int[] primitiveIntArray;
		private Integer[] wrapperIntArray;
		private ArrayList<Integer> wrapperIntList;
		
		private short[] primitiveShortArray;
		private Short[] wrapperShortArray;
		private List<Short> wrapperShortList;
		
		private long[] primitiveLongArray;
		private Long[] wrapperLongArray;
		private List<Long> wrapperLongList;
		
		private byte[] primitiveByteArray;
		private Byte[] wrapperByteArray;
		private List<Byte> wrapperByteList;
		
		private boolean[] primitiveBooleanArray;
		private Boolean[] wrapperBooleanArray;
		private List<Boolean> wrapperBooleanList;
		
		private float[] primitiveFloatArray;
		private Float[] wrapperFloatArray;
		private List<Float> wrapperFloatList;
		
		private double[] primitiveDoubleArray;
		private Double[] wrapperDoubleArray;
		private List<Double> wrapperDoubleList;
		
		private char[] primitiveCharArray;
		private Character[] wrapperCharArray;
		private List<Character> wrapperCharList;
		
		private String[] wrapperStringArray;
		private List<String> wrapperStringList;
		
		public int[] getPrimitiveIntArray() {
			return primitiveIntArray;
		}
		public void setPrimitiveIntArray(int[] primitiveIntArray) {
			this.primitiveIntArray = primitiveIntArray;
		}
		public Integer[] getWrapperIntArray() {
			return wrapperIntArray;
		}
		public void setWrapperIntArray(Integer[] wrapperIntArray) {
			this.wrapperIntArray = wrapperIntArray;
		}
		public ArrayList<Integer> getWrapperIntList() {
			return wrapperIntList;
		}
		public void setWrapperIntList(ArrayList<Integer> wrapperIntList) {
			this.wrapperIntList = wrapperIntList;
		}
		public short[] getPrimitiveShortArray() {
			return primitiveShortArray;
		}
		public void setPrimitiveShortArray(short[] primitiveShortArray) {
			this.primitiveShortArray = primitiveShortArray;
		}
		public Short[] getWrapperShortArray() {
			return wrapperShortArray;
		}
		public void setWrapperShortArray(Short[] wrapperShortArray) {
			this.wrapperShortArray = wrapperShortArray;
		}
		public List<Short> getWrapperShortList() {
			return wrapperShortList;
		}
		public void setWrapperShortList(List<Short> wrapperShortList) {
			this.wrapperShortList = wrapperShortList;
		}
		public long[] getPrimitiveLongArray() {
			return primitiveLongArray;
		}
		public void setPrimitiveLongArray(long[] primitiveLongArray) {
			this.primitiveLongArray = primitiveLongArray;
		}
		public Long[] getWrapperLongArray() {
			return wrapperLongArray;
		}
		public void setWrapperLongArray(Long[] wrapperLongArray) {
			this.wrapperLongArray = wrapperLongArray;
		}
		public List<Long> getWrapperLongList() {
			return wrapperLongList;
		}
		public void setWrapperLongList(List<Long> wrapperLongList) {
			this.wrapperLongList = wrapperLongList;
		}
		public byte[] getPrimitiveByteArray() {
			return primitiveByteArray;
		}
		public void setPrimitiveByteArray(byte[] primitiveByteArray) {
			this.primitiveByteArray = primitiveByteArray;
		}
		public Byte[] getWrapperByteArray() {
			return wrapperByteArray;
		}
		public void setWrapperByteArray(Byte[] wrapperByteArray) {
			this.wrapperByteArray = wrapperByteArray;
		}
		public List<Byte> getWrapperByteList() {
			return wrapperByteList;
		}
		public void setWrapperByteList(List<Byte> wrapperByteList) {
			this.wrapperByteList = wrapperByteList;
		}
		public boolean[] getPrimitiveBooleanArray() {
			return primitiveBooleanArray;
		}
		public void setPrimitiveBooleanArray(boolean[] primitiveBooleanArray) {
			this.primitiveBooleanArray = primitiveBooleanArray;
		}
		public Boolean[] getWrapperBooleanArray() {
			return wrapperBooleanArray;
		}
		public void setWrapperBooleanArray(Boolean[] wrapperBooleanArray) {
			this.wrapperBooleanArray = wrapperBooleanArray;
		}
		public List<Boolean> getWrapperBooleanList() {
			return wrapperBooleanList;
		}
		public void setWrapperBooleanList(List<Boolean> wrapperBooleanList) {
			this.wrapperBooleanList = wrapperBooleanList;
		}
		public float[] getPrimitiveFloatArray() {
			return primitiveFloatArray;
		}
		public void setPrimitiveFloatArray(float[] primitiveFloatArray) {
			this.primitiveFloatArray = primitiveFloatArray;
		}
		public Float[] getWrapperFloatArray() {
			return wrapperFloatArray;
		}
		public void setWrapperFloatArray(Float[] wrapperFloatArray) {
			this.wrapperFloatArray = wrapperFloatArray;
		}
		public List<Float> getWrapperFloatList() {
			return wrapperFloatList;
		}
		public void setWrapperFloatList(List<Float> wrapperFloatList) {
			this.wrapperFloatList = wrapperFloatList;
		}
		public double[] getPrimitiveDoubleArray() {
			return primitiveDoubleArray;
		}
		public void setPrimitiveDoubleArray(double[] primitiveDoubleArray) {
			this.primitiveDoubleArray = primitiveDoubleArray;
		}
		public Double[] getWrapperDoubleArray() {
			return wrapperDoubleArray;
		}
		public void setWrapperDoubleArray(Double[] wrapperDoubleArray) {
			this.wrapperDoubleArray = wrapperDoubleArray;
		}
		public List<Double> getWrapperDoubleList() {
			return wrapperDoubleList;
		}
		public void setWrapperDoubleList(List<Double> wrapperDoubleList) {
			this.wrapperDoubleList = wrapperDoubleList;
		}
		public char[] getPrimitiveCharArray() {
			return primitiveCharArray;
		}
		public void setPrimitiveCharArray(char[] primitiveCharArray) {
			this.primitiveCharArray = primitiveCharArray;
		}
		public Character[] getWrapperCharArray() {
			return wrapperCharArray;
		}
		public void setWrapperCharArray(Character[] wrapperCharArray) {
			this.wrapperCharArray = wrapperCharArray;
		}
		public List<Character> getWrapperCharList() {
			return wrapperCharList;
		}
		public void setWrapperCharList(List<Character> wrapperCharList) {
			this.wrapperCharList = wrapperCharList;
		}
		public String[] getWrapperStringArray() {
			return wrapperStringArray;
		}
		public void setWrapperStringArray(String[] wrapperStringArray) {
			this.wrapperStringArray = wrapperStringArray;
		}
		public List<String> getWrapperStringList() {
			return wrapperStringList;
		}
		public void setWrapperStringList(List<String> wrapperStringList) {
			this.wrapperStringList = wrapperStringList;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(primitiveBooleanArray);
			result = prime * result + Arrays.hashCode(primitiveByteArray);
			result = prime * result + Arrays.hashCode(primitiveCharArray);
			result = prime * result + Arrays.hashCode(primitiveDoubleArray);
			result = prime * result + Arrays.hashCode(primitiveFloatArray);
			result = prime * result + Arrays.hashCode(primitiveIntArray);
			result = prime * result + Arrays.hashCode(primitiveLongArray);
			result = prime * result + Arrays.hashCode(primitiveShortArray);
			result = prime * result + Arrays.hashCode(wrapperBooleanArray);
			result = prime * result + ((wrapperBooleanList == null) ? 0 : wrapperBooleanList.hashCode());
			result = prime * result + Arrays.hashCode(wrapperByteArray);
			result = prime * result + ((wrapperByteList == null) ? 0 : wrapperByteList.hashCode());
			result = prime * result + Arrays.hashCode(wrapperCharArray);
			result = prime * result + ((wrapperCharList == null) ? 0 : wrapperCharList.hashCode());
			result = prime * result + Arrays.hashCode(wrapperDoubleArray);
			result = prime * result + ((wrapperDoubleList == null) ? 0 : wrapperDoubleList.hashCode());
			result = prime * result + Arrays.hashCode(wrapperFloatArray);
			result = prime * result + ((wrapperFloatList == null) ? 0 : wrapperFloatList.hashCode());
			result = prime * result + Arrays.hashCode(wrapperIntArray);
			result = prime * result + ((wrapperIntList == null) ? 0 : wrapperIntList.hashCode());
			result = prime * result + Arrays.hashCode(wrapperLongArray);
			result = prime * result + ((wrapperLongList == null) ? 0 : wrapperLongList.hashCode());
			result = prime * result + Arrays.hashCode(wrapperShortArray);
			result = prime * result + ((wrapperShortList == null) ? 0 : wrapperShortList.hashCode());
			result = prime * result + Arrays.hashCode(wrapperStringArray);
			result = prime * result + ((wrapperStringList == null) ? 0 : wrapperStringList.hashCode());
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
			PrimitivesArraysTestClass other = (PrimitivesArraysTestClass) obj;
			if (!Arrays.equals(primitiveBooleanArray, other.primitiveBooleanArray))
				return false;
			if (!Arrays.equals(primitiveByteArray, other.primitiveByteArray))
				return false;
			if (!Arrays.equals(primitiveCharArray, other.primitiveCharArray))
				return false;
			if (!Arrays.equals(primitiveDoubleArray, other.primitiveDoubleArray))
				return false;
			if (!Arrays.equals(primitiveFloatArray, other.primitiveFloatArray))
				return false;
			if (!Arrays.equals(primitiveIntArray, other.primitiveIntArray))
				return false;
			if (!Arrays.equals(primitiveLongArray, other.primitiveLongArray))
				return false;
			if (!Arrays.equals(primitiveShortArray, other.primitiveShortArray))
				return false;
			if (!Arrays.equals(wrapperBooleanArray, other.wrapperBooleanArray))
				return false;
			if (wrapperBooleanList == null) {
				if (other.wrapperBooleanList != null)
					return false;
			} else if (!wrapperBooleanList.equals(other.wrapperBooleanList))
				return false;
			if (!Arrays.equals(wrapperByteArray, other.wrapperByteArray))
				return false;
			if (wrapperByteList == null) {
				if (other.wrapperByteList != null)
					return false;
			} else if (!wrapperByteList.equals(other.wrapperByteList))
				return false;
			if (!Arrays.equals(wrapperCharArray, other.wrapperCharArray))
				return false;
			if (wrapperCharList == null) {
				if (other.wrapperCharList != null)
					return false;
			} else if (!wrapperCharList.equals(other.wrapperCharList))
				return false;
			if (!Arrays.equals(wrapperDoubleArray, other.wrapperDoubleArray))
				return false;
			if (wrapperDoubleList == null) {
				if (other.wrapperDoubleList != null)
					return false;
			} else if (!wrapperDoubleList.equals(other.wrapperDoubleList))
				return false;
			if (!Arrays.equals(wrapperFloatArray, other.wrapperFloatArray))
				return false;
			if (wrapperFloatList == null) {
				if (other.wrapperFloatList != null)
					return false;
			} else if (!wrapperFloatList.equals(other.wrapperFloatList))
				return false;
			if (!Arrays.equals(wrapperIntArray, other.wrapperIntArray))
				return false;
			if (wrapperIntList == null) {
				if (other.wrapperIntList != null)
					return false;
			} else if (!wrapperIntList.equals(other.wrapperIntList))
				return false;
			if (!Arrays.equals(wrapperLongArray, other.wrapperLongArray))
				return false;
			if (wrapperLongList == null) {
				if (other.wrapperLongList != null)
					return false;
			} else if (!wrapperLongList.equals(other.wrapperLongList))
				return false;
			if (!Arrays.equals(wrapperShortArray, other.wrapperShortArray))
				return false;
			if (wrapperShortList == null) {
				if (other.wrapperShortList != null)
					return false;
			} else if (!wrapperShortList.equals(other.wrapperShortList))
				return false;
			if (!Arrays.equals(wrapperStringArray, other.wrapperStringArray))
				return false;
			if (wrapperStringList == null) {
				if (other.wrapperStringList != null)
					return false;
			} else if (!wrapperStringList.equals(other.wrapperStringList))
				return false;
			return true;
		}
	}
	

	
	public static class CollectionOfIntegersTestClass {
		Collection<Integer> intCollection;

		public Collection<Integer> getIntCollection() {
			return intCollection;
		}

		public void setIntCollection(Collection<Integer> intCollection) {
			this.intCollection = intCollection;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((intCollection == null) ? 0 : intCollection.hashCode());
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
			CollectionOfIntegersTestClass other = (CollectionOfIntegersTestClass) obj;
			if (intCollection == null) {
				if (other.intCollection != null)
					return false;
			} else if (!intCollection.equals(other.intCollection))
				return false;
			return true;
		}
	}
}
