package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVByte;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVFloat;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.PVUnionArray;
import org.epics.pvdata.pv.Scalar;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Union;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class UnionTest {
	
	@Test
	public void testDeserialiseVariableUnion() {
		
		// Create test pvStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("myvalue", union).
			add("descriptor", ScalarType.pvString).
			add("myObject", union).
			createStructure();
		
		Scalar scalar = fieldCreate.createScalar(ScalarType.pvFloat);
		
		PVScalar pvs = pvDataCreate.createPVScalar(scalar);
		
		PVFloat pvf = (PVFloat)pvs;
		pvf.put(123.45f);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVUnion pvUnionValue = testPVStructure.getSubField(PVUnion.class, "myvalue");
		pvUnionValue.set(pvf);
		
		PVString wrapperValue = testPVStructure.getSubField(PVString.class, "descriptor");
		wrapperValue.put("test string");
		
		PVUnion pvStructUnionValue = testPVStructure.getSubField(PVUnion.class, "myObject");
		Structure innserStructure = fieldCreate.createFieldBuilder().
				add("aDouble", ScalarType.pvDouble).
				createStructure();
		PVStructure innerPVStructure = pvDataCreate.createPVStructure(innserStructure);
		PVDouble pvDoubleValue = innerPVStructure.getSubField(PVDouble.class, "aDouble");
		pvDoubleValue.put(55.66d);
		
		pvStructUnionValue.set(innerPVStructure);
		
		UnionTestClass expectedObject = new UnionTestClass();
		expectedObject.myvalue = 123.45f;
		expectedObject.descriptor = "test string";
		expectedObject.myObject = new TestObjectClass();
		expectedObject.myObject.aDouble = 55.66d;
		
		System.out.println(structure);
		System.out.println("-------");
		System.out.println(testPVStructure);
		System.out.println("=========");
		
		UnionTestClass deserialisedObject = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, UnionTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void testDeserialiseVariableUnionArray() {
		
		// Create test pvStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure structure = fieldCreate.createFieldBuilder().
			addArray("myScalarArray", union).
			addArray("myObjectArray", union).
			createStructure();
		
		Scalar scalar = fieldCreate.createScalar(ScalarType.pvByte);
		
		PVScalar pvs = pvDataCreate.createPVScalar(scalar);
		PVByte pvb1 = (PVByte)pvs;
		pvb1.put((byte)31);
		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(pvb1);

		PVScalar pvs2 = pvDataCreate.createPVScalar(scalar);
		PVByte pvb2 = (PVByte)pvs2;
		pvb2.put((byte)42);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(pvb2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVUnionArray pvUnionValue = testPVStructure.getSubField(PVUnionArray.class, "myScalarArray");
		pvUnionValue.put(0, 2, unionArray, 0);
		
		PVUnionArray pvStructUnionValue = testPVStructure.getSubField(PVUnionArray.class, "myObjectArray");
		Structure innerStructure = fieldCreate.createFieldBuilder().
				add("aDouble", ScalarType.pvDouble).
				createStructure();
		PVStructure innerPVStructure1 = pvDataCreate.createPVStructure(innerStructure);
		PVDouble pvDoubleValue = innerPVStructure1.getSubField(PVDouble.class, "aDouble");
		pvDoubleValue.put(111d);
		PVStructure innerPVStructure2 = pvDataCreate.createPVStructure(innerStructure);
		PVDouble pvDoubleValue2 = innerPVStructure2.getSubField(PVDouble.class, "aDouble");
		pvDoubleValue2.put(222d);
		
		PVUnion pvu3 = pvDataCreate.createPVVariantUnion();
		pvu3.set(innerPVStructure1);
		PVUnion pvu4 = pvDataCreate.createPVVariantUnion();
		pvu4.set(innerPVStructure2);
		
		PVUnion[] unionStructArray = new PVUnion[2];
		unionStructArray[0] = pvu3;
		unionStructArray[1] = pvu4;
		pvStructUnionValue.put(0, 2, unionStructArray, 0);
		
		UnionArrayTestClass expectedObject = new UnionArrayTestClass();
		byte[] byteArray = new byte[2];
		byteArray[0] = (byte)31;
		byteArray[1] = (byte)42;
		
		TestObjectClass[] objectArray = new TestObjectClass[2];
		objectArray[0] = new TestObjectClass();
		objectArray[0].aDouble = 111d;
		objectArray[1] = new TestObjectClass();
		objectArray[1].aDouble = 222d;
		
		expectedObject.myScalarArray = byteArray;
		expectedObject.myObjectArray = objectArray;
		
		UnionArrayTestClass deserialisedObject = null;
		try {
			PVMarshaller marshaller = new PVMarshaller();
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, UnionArrayTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);;
	}
	
	@Test
	public void testDeserialiseRegularUnion() {
		
		// Create test pvStructure to deserialise
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Field intField = fieldCreate.createScalar(ScalarType.pvInt);
		Field doubleField = fieldCreate.createScalar(ScalarType.pvDouble);
		
		String[] fieldNames = {"intField", "doubleField"};
		Field[] fields = {intField, doubleField};
		Union union = fieldCreate.createUnion(fieldNames, fields);
		
		Structure structure = fieldCreate.createFieldBuilder().
			add("myValue", union).
			add("descriptor", ScalarType.pvString).
			createStructure();
		
		PVField pvField = pvDataCreate.createPVField(doubleField);
		PVDouble pvd = (PVDouble)pvField;
		pvd.put(123.45d);
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVString wrapperValue = testPVStructure.getSubField(PVString.class, "descriptor");
		wrapperValue.put("test string");
				
		try {
			PVMarshaller marshaller = new PVMarshaller();
			marshaller.fromPVStructure(testPVStructure, UnionTestClass.class);
			fail("No excpetion thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public static class TestObjectClass {
		Double aDouble;

		public Double getaDouble() {
			return aDouble;
		}

		public void setaDouble(Double aDouble) {
			this.aDouble = aDouble;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((aDouble == null) ? 0 : aDouble.hashCode());
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
			TestObjectClass other = (TestObjectClass) obj;
			if (aDouble == null) {
				if (other.aDouble != null)
					return false;
			} else if (!aDouble.equals(other.aDouble))
				return false;
			return true;
		}
	}
	
	public static class UnionTestClass {
		private float myvalue;
		private String descriptor;
		private TestObjectClass myObject;
		
		public float getMyvalue() {
			return myvalue;
		}
		public void setMyvalue(float myvalue) {
			this.myvalue = myvalue;
		}
		public String getDescriptor() {
			return descriptor;
		}
		public void setDescriptor(String descriptor) {
			this.descriptor = descriptor;
		}
		public TestObjectClass getMyObject() {
			return myObject;
		}
		public void setMyObject(TestObjectClass myObject) {
			this.myObject = myObject;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((descriptor == null) ? 0 : descriptor.hashCode());
			result = prime * result + ((myObject == null) ? 0 : myObject.hashCode());
			result = prime * result + Float.floatToIntBits(myvalue);
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
			UnionTestClass other = (UnionTestClass) obj;
			if (descriptor == null) {
				if (other.descriptor != null)
					return false;
			} else if (!descriptor.equals(other.descriptor))
				return false;
			if (myObject == null) {
				if (other.myObject != null)
					return false;
			} else if (!myObject.equals(other.myObject))
				return false;
			if (Float.floatToIntBits(myvalue) != Float.floatToIntBits(other.myvalue))
				return false;
			return true;
		}
		
	}
	
	public static class UnionArrayTestClass {
		byte[]  myScalarArray;
		TestObjectClass[] myObjectArray;
		public byte[] getMyScalarArray() {
			return myScalarArray;
		}
		public void setMyScalarArray(byte[] myScalarArray) {
			this.myScalarArray = myScalarArray;
		}
		public TestObjectClass[] getMyObjectArray() {
			return myObjectArray;
		}
		public void setMyObjectArray(TestObjectClass[] myObjectArray) {
			this.myObjectArray = myObjectArray;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(myObjectArray);
			result = prime * result + Arrays.hashCode(myScalarArray);
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
			UnionArrayTestClass other = (UnionArrayTestClass) obj;
			if (!Arrays.equals(myObjectArray, other.myObjectArray))
				return false;
			if (!Arrays.equals(myScalarArray, other.myScalarArray))
				return false;
			return true;
		}
	}

}
