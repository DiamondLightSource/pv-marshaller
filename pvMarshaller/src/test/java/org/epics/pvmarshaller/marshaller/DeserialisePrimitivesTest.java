package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import org.epics.pvdata.pv.PVShort;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUByte;
import org.epics.pvdata.pv.PVUInt;
import org.epics.pvdata.pv.PVULong;
import org.epics.pvdata.pv.PVUShort;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class DeserialisePrimitivesTest {

	@Test
	public void DeserialiseIntegerTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveIntValue = 5;
		expectedObject.wrapperIntValue = 616;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveIntValue", ScalarType.pvInt).
			add("wrapperIntValue", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "primitiveIntValue");
		primitiveValue.put(5);
		PVInt wrapperValue = testPVStructure.getSubField(PVInt.class, "wrapperIntValue");
		wrapperValue.put(616);
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Deserialised Structure:\n" + deserialisedObject);
		System.out.println("pv= " + deserialisedObject.primitiveIntValue);
		System.out.println("wv= " + deserialisedObject.wrapperIntValue);
		System.out.println("---\n");
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseShortTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveShortValue = 62;
		expectedObject.wrapperShortValue = -3;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveShortValue", ScalarType.pvShort).
			add("wrapperShortValue", ScalarType.pvShort).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVShort primitiveValue = testPVStructure.getSubField(PVShort.class, "primitiveShortValue");
		primitiveValue.put((short)62);
		PVShort wrapperValue = testPVStructure.getSubField(PVShort.class, "wrapperShortValue");
		wrapperValue.put((short)-3);
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseLongTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveLongValue = 3434435l;
		expectedObject.wrapperLongValue = 76767l;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveLongValue", ScalarType.pvLong).
			add("wrapperLongValue", ScalarType.pvLong).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVLong primitiveValue = testPVStructure.getSubField(PVLong.class, "primitiveLongValue");
		primitiveValue.put(3434435l);
		PVLong wrapperValue = testPVStructure.getSubField(PVLong.class, "wrapperLongValue");
		wrapperValue.put(76767l);
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseByteTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveByteValue = (byte)233;
		expectedObject.wrapperByteValue = (byte)1;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveByteValue", ScalarType.pvByte).
			add("wrapperByteValue", ScalarType.pvByte).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVByte primitiveValue = testPVStructure.getSubField(PVByte.class, "primitiveByteValue");
		primitiveValue.put((byte)233);
		PVByte wrapperValue = testPVStructure.getSubField(PVByte.class, "wrapperByteValue");
		wrapperValue.put((byte)1);
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseBooleanTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveBooleanValue = true;
		expectedObject.wrapperBooleanValue = false;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveBooleanValue", ScalarType.pvBoolean).
			add("wrapperBooleanValue", ScalarType.pvBoolean).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVBoolean primitiveValue = testPVStructure.getSubField(PVBoolean.class, "primitiveBooleanValue");
		primitiveValue.put(true);
		PVBoolean wrapperValue = testPVStructure.getSubField(PVBoolean.class, "wrapperBooleanValue");
		wrapperValue.put(false);
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseFloatTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveFloatValue = 2.6f;
		expectedObject.wrapperFloatValue = -11234f;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveFloatValue", ScalarType.pvFloat).
			add("wrapperFloatValue", ScalarType.pvFloat).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVFloat primitiveValue = testPVStructure.getSubField(PVFloat.class, "primitiveFloatValue");
		primitiveValue.put(2.6f);
		PVFloat wrapperValue = testPVStructure.getSubField(PVFloat.class, "wrapperFloatValue");
		wrapperValue.put(-11234f);
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
				
		assertEquals(expectedObject, deserialisedObject);
	}

	@Test
	public void DeserialiseDoubleTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveDoubleValue = 123456789;
		expectedObject.wrapperDoubleValue = 123.456;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveDoubleValue", ScalarType.pvDouble).
			add("wrapperDoubleValue", ScalarType.pvDouble).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVDouble primitiveValue = testPVStructure.getSubField(PVDouble.class, "primitiveDoubleValue");
		primitiveValue.put(123456789);
		PVDouble wrapperValue = testPVStructure.getSubField(PVDouble.class, "wrapperDoubleValue");
		wrapperValue.put(123.456);
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void DeserialiseCharTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveCharValue = 'a';
		expectedObject.wrapperCharValue = 'b';
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveCharValue", ScalarType.pvString).
			add("wrapperCharValue", ScalarType.pvString).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVString primitiveValue = testPVStructure.getSubField(PVString.class, "primitiveCharValue");
		primitiveValue.put("a");
		PVString wrapperValue = testPVStructure.getSubField(PVString.class, "wrapperCharValue");
		wrapperValue.put("b");
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Deserialised Structure:\n" + deserialisedObject);
		System.out.println("pv= " + deserialisedObject.primitiveIntValue);
		System.out.println("wv= " + deserialisedObject.wrapperIntValue);
		System.out.println("---\n");
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void DeserialiseStringTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.stringValue = "test string";
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("stringValue", ScalarType.pvString).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVString primitiveValue = testPVStructure.getSubField(PVString.class, "stringValue");
		primitiveValue.put("test string");
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}
	
	@Test
	public void DeserialiseUnsignedIntTestThrowsException() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveIntValue", ScalarType.pvUInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVUInt primitiveValue = testPVStructure.getSubField(PVUInt.class, "primitiveIntValue");
		primitiveValue.put(5);
		
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
			fail("No Exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("Unsigned Int"));
		}
	}
	
	@Test
	public void DeserialiseUnsignedShortTestThrowsException() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveShortValue", ScalarType.pvUShort).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVUShort primitiveValue = testPVStructure.getSubField(PVUShort.class, "primitiveShortValue");
		primitiveValue.put((short)5);
		
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
			fail("No Exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("Unsigned Short"));
		}
	}
	
	@Test
	public void DeserialiseUnsignedLongTestThrowsException() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveLongValue", ScalarType.pvULong).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVULong primitiveValue = testPVStructure.getSubField(PVULong.class, "primitiveLongValue");
		primitiveValue.put(5);
		
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
			fail("No Exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("Unsigned Long"));
		}
	}
	
	@Test
	public void DeserialiseUnisgnedByteTestThrowsException() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveByteValue", ScalarType.pvUByte).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVUByte primitiveValue = testPVStructure.getSubField(PVUByte.class, "primitiveByteValue");
		primitiveValue.put((byte)5);
				
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
			fail("No Exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("Unsigned Byte"));
		}
	}

	@Test
	public void DeserialiseUnknownPrimitive() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveIntValue", ScalarType.pvInt).
			add("unknownIntValue", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "primitiveIntValue");
		primitiveValue.put(5);
		PVInt unknownValue = testPVStructure.getSubField(PVInt.class, "unknownIntValue");
		unknownValue.put(14);
				
		try {
			marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
			fail("Exception not thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("unknownIntValue"));
		}
	}

	@Test
	public void DeserialiseUnknownPrimitiveWithIgnore() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		PrimitivesTestClass expectedObject = new PrimitivesTestClass();

		expectedObject.primitiveIntValue = 5;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveIntValue", ScalarType.pvInt).
			add("unknownIntValue", ScalarType.pvInt).
			createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = testPVStructure.getSubField(PVInt.class, "primitiveIntValue");
		primitiveValue.put(5);
		PVInt unknownValue = testPVStructure.getSubField(PVInt.class, "unknownIntValue");
		unknownValue.put(616);
		
		PrimitivesTestClass deserialisedObject = null;
		
		try {
			marshaller.setIgnoreUnknownFields(true);
			deserialisedObject = marshaller.fromPVStructure(testPVStructure, PrimitivesTestClass.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(expectedObject, deserialisedObject);
	}

	public static class PrimitivesTestClass
	{		
		private int primitiveIntValue;
		private Integer wrapperIntValue;
		
		private short primitiveShortValue;
		private Short wrapperShortValue;
		
		private long primitiveLongValue;
		private Long wrapperLongValue;
		
		private byte primitiveByteValue;
		private Byte wrapperByteValue;

		private boolean primitiveBooleanValue;
		private Boolean wrapperBooleanValue;
		
		private float primitiveFloatValue;
		private Float wrapperFloatValue;
		
		private double primitiveDoubleValue;
		private Double wrapperDoubleValue;
		
		private char primitiveCharValue;
		private Character wrapperCharValue;
		
		private String stringValue;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (primitiveBooleanValue ? 1231 : 1237);
			result = prime * result + primitiveByteValue;
			result = prime * result + primitiveCharValue;
			long temp;
			temp = Double.doubleToLongBits(primitiveDoubleValue);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + Float.floatToIntBits(primitiveFloatValue);
			result = prime * result + primitiveIntValue;
			result = prime * result + (int) (primitiveLongValue ^ (primitiveLongValue >>> 32));
			result = prime * result + primitiveShortValue;
			result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
			result = prime * result + ((wrapperBooleanValue == null) ? 0 : wrapperBooleanValue.hashCode());
			result = prime * result + ((wrapperByteValue == null) ? 0 : wrapperByteValue.hashCode());
			result = prime * result + ((wrapperCharValue == null) ? 0 : wrapperCharValue.hashCode());
			result = prime * result + ((wrapperDoubleValue == null) ? 0 : wrapperDoubleValue.hashCode());
			result = prime * result + ((wrapperFloatValue == null) ? 0 : wrapperFloatValue.hashCode());
			result = prime * result + ((wrapperIntValue == null) ? 0 : wrapperIntValue.hashCode());
			result = prime * result + ((wrapperLongValue == null) ? 0 : wrapperLongValue.hashCode());
			result = prime * result + ((wrapperShortValue == null) ? 0 : wrapperShortValue.hashCode());
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
			PrimitivesTestClass other = (PrimitivesTestClass) obj;
			if (primitiveBooleanValue != other.primitiveBooleanValue)
				return false;
			if (primitiveByteValue != other.primitiveByteValue)
				return false;
			if (primitiveCharValue != other.primitiveCharValue)
				return false;
			if (Double.doubleToLongBits(primitiveDoubleValue) != Double.doubleToLongBits(other.primitiveDoubleValue))
				return false;
			if (Float.floatToIntBits(primitiveFloatValue) != Float.floatToIntBits(other.primitiveFloatValue))
				return false;
			if (primitiveIntValue != other.primitiveIntValue)
				return false;
			if (primitiveLongValue != other.primitiveLongValue)
				return false;
			if (primitiveShortValue != other.primitiveShortValue)
				return false;
			if (stringValue == null) {
				if (other.stringValue != null)
					return false;
			} else if (!stringValue.equals(other.stringValue))
				return false;
			if (wrapperBooleanValue == null) {
				if (other.wrapperBooleanValue != null)
					return false;
			} else if (!wrapperBooleanValue.equals(other.wrapperBooleanValue))
				return false;
			if (wrapperByteValue == null) {
				if (other.wrapperByteValue != null)
					return false;
			} else if (!wrapperByteValue.equals(other.wrapperByteValue))
				return false;
			if (wrapperCharValue == null) {
				if (other.wrapperCharValue != null)
					return false;
			} else if (!wrapperCharValue.equals(other.wrapperCharValue))
				return false;
			if (wrapperDoubleValue == null) {
				if (other.wrapperDoubleValue != null)
					return false;
			} else if (!wrapperDoubleValue.equals(other.wrapperDoubleValue))
				return false;
			if (wrapperFloatValue == null) {
				if (other.wrapperFloatValue != null)
					return false;
			} else if (!wrapperFloatValue.equals(other.wrapperFloatValue))
				return false;
			if (wrapperIntValue == null) {
				if (other.wrapperIntValue != null)
					return false;
			} else if (!wrapperIntValue.equals(other.wrapperIntValue))
				return false;
			if (wrapperLongValue == null) {
				if (other.wrapperLongValue != null)
					return false;
			} else if (!wrapperLongValue.equals(other.wrapperLongValue))
				return false;
			if (wrapperShortValue == null) {
				if (other.wrapperShortValue != null)
					return false;
			} else if (!wrapperShortValue.equals(other.wrapperShortValue))
				return false;
			return true;
		}
		
		public int getPrimitiveIntValue() {
			return primitiveIntValue;
		}

		public void setPrimitiveIntValue(int primitiveIntValue) {
			this.primitiveIntValue = primitiveIntValue;
		}

		public Integer getWrapperIntValue() {
			return wrapperIntValue;
		}

		public void setWrapperIntValue(Integer wrapperIntValue) {
			this.wrapperIntValue = wrapperIntValue;
		}

		public short getPrimitiveShortValue() {
			return primitiveShortValue;
		}

		public void setPrimitiveShortValue(short primitiveShortValue) {
			this.primitiveShortValue = primitiveShortValue;
		}

		public Short getWrapperShortValue() {
			return wrapperShortValue;
		}

		public void setWrapperShortValue(Short wrapperShortValue) {
			this.wrapperShortValue = wrapperShortValue;
		}

		public long getPrimitiveLongValue() {
			return primitiveLongValue;
		}

		public void setPrimitiveLongValue(long primitiveLongValue) {
			this.primitiveLongValue = primitiveLongValue;
		}

		public Long getWrapperLongValue() {
			return wrapperLongValue;
		}

		public void setWrapperLongValue(Long wrapperLongValue) {
			this.wrapperLongValue = wrapperLongValue;
		}

		public byte getPrimitiveByteValue() {
			return primitiveByteValue;
		}

		public void setPrimitiveByteValue(byte primitiveByteValue) {
			this.primitiveByteValue = primitiveByteValue;
		}

		public Byte getWrapperByteValue() {
			return wrapperByteValue;
		}

		public void setWrapperByteValue(Byte wrapperByteValue) {
			this.wrapperByteValue = wrapperByteValue;
		}

		public boolean isPrimitiveBooleanValue() {
			return primitiveBooleanValue;
		}

		public void setPrimitiveBooleanValue(boolean primitiveBooleanValue) {
			this.primitiveBooleanValue = primitiveBooleanValue;
		}

		public Boolean getWrapperBooleanValue() {
			return wrapperBooleanValue;
		}

		public void setWrapperBooleanValue(Boolean wrapperBooleanValue) {
			this.wrapperBooleanValue = wrapperBooleanValue;
		}

		public float getPrimitiveFloatValue() {
			return primitiveFloatValue;
		}

		public void setPrimitiveFloatValue(float primitiveFloatValue) {
			this.primitiveFloatValue = primitiveFloatValue;
		}

		public Float getWrapperFloatValue() {
			return wrapperFloatValue;
		}

		public void setWrapperFloatValue(Float wrapperFloatValue) {
			this.wrapperFloatValue = wrapperFloatValue;
		}

		public double getPrimitiveDoubleValue() {
			return primitiveDoubleValue;
		}

		public void setPrimitiveDoubleValue(double primitiveDoubleValue) {
			this.primitiveDoubleValue = primitiveDoubleValue;
		}

		public Double getWrapperDoubleValue() {
			return wrapperDoubleValue;
		}

		public void setWrapperDoubleValue(Double wrapperDoubleValue) {
			this.wrapperDoubleValue = wrapperDoubleValue;
		}

		public char getPrimitiveCharValue() {
			return primitiveCharValue;
		}

		public void setPrimitiveCharValue(char primitiveCharValue) {
			this.primitiveCharValue = primitiveCharValue;
		}

		public Character getWrapperCharValue() {
			return wrapperCharValue;
		}

		public void setWrapperCharValue(Character wrapperCharValue) {
			this.wrapperCharValue = wrapperCharValue;
		}

		public String getStringValue() {
			return stringValue;
		}

		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}
		
		
	}
}
