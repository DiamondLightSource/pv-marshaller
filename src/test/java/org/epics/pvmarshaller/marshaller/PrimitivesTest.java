package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVBoolean;
import org.epics.pvdata.pv.PVByte;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVFloat;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVLong;
import org.epics.pvdata.pv.PVShort;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class PrimitivesTest {
	
	@Test
	public void IntegerprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestClass testClass = new IntTestClass();

		testClass.primitiveValue = 5;
		testClass.wrapperValue = 616;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvInt).
			add("wrapperValue", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "primitiveValue");
		primitiveValue.put(5);
		PVInt wrapperValue = expectedPVStructure.getSubField(PVInt.class, "wrapperValue");
		wrapperValue.put(616);
		
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
	public void IntegerprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestClass testClass = new IntTestClass();

		testClass.primitiveValue = 5;
		testClass.wrapperValue = 616;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvInt).
			add("wrapperValue", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "primitiveValue");
		primitiveValue.put(42);
		PVInt wrapperValue = expectedPVStructure.getSubField(PVInt.class, "wrapperValue");
		wrapperValue.put(1);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void IntegerprimitivesTestMaxMinValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestClass testClass = new IntTestClass();

		testClass.primitiveValue = Integer.MAX_VALUE;
		testClass.wrapperValue = Integer.MIN_VALUE;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvInt).
			add("wrapperValue", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "primitiveValue");
		primitiveValue.put(Integer.MAX_VALUE);
		PVInt wrapperValue = expectedPVStructure.getSubField(PVInt.class, "wrapperValue");
		wrapperValue.put(Integer.MIN_VALUE);
		
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
	public void ShortprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ShortTestClass testClass = new ShortTestClass();

		testClass.primitiveValue = 65;
		testClass.wrapperValue = 96;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvShort).
			add("wrapperValue", ScalarType.pvShort).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVShort primitiveValue = expectedPVStructure.getSubField(PVShort.class, "primitiveValue");
		primitiveValue.put((short) 65);
		PVShort wrapperValue = expectedPVStructure.getSubField(PVShort.class, "wrapperValue");
		wrapperValue.put((short) 96);
		
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
	public void ShortprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ShortTestClass testClass = new ShortTestClass();

		testClass.primitiveValue = 55;
		testClass.wrapperValue = 96;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvShort).
			add("wrapperValue", ScalarType.pvShort).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVShort primitiveValue = expectedPVStructure.getSubField(PVShort.class, "primitiveValue");
		primitiveValue.put((short) 4);
		PVShort wrapperValue = expectedPVStructure.getSubField(PVShort.class, "wrapperValue");
		wrapperValue.put((short) 3);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void ShortprimitivesTestMaxMinValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ShortTestClass testClass = new ShortTestClass();

		testClass.primitiveValue = Short.MAX_VALUE;
		testClass.wrapperValue = Short.MIN_VALUE;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvShort).
			add("wrapperValue", ScalarType.pvShort).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVShort primitiveValue = expectedPVStructure.getSubField(PVShort.class, "primitiveValue");
		primitiveValue.put(Short.MAX_VALUE);
		PVShort wrapperValue = expectedPVStructure.getSubField(PVShort.class, "wrapperValue");
		wrapperValue.put(Short.MIN_VALUE);
		
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
	public void LongprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		LongTestClass testClass = new LongTestClass();

		testClass.primitiveValue = 88;
		testClass.wrapperValue = 589863l;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvLong).
			add("wrapperValue", ScalarType.pvLong).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVLong primitiveValue = expectedPVStructure.getSubField(PVLong.class, "primitiveValue");
		primitiveValue.put(88);
		PVLong wrapperValue = expectedPVStructure.getSubField(PVLong.class, "wrapperValue");
		wrapperValue.put(589863l);
		
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
	public void LongprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		LongTestClass testClass = new LongTestClass();

		testClass.primitiveValue = 3457l;
		testClass.wrapperValue = 13l;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvLong).
			add("wrapperValue", ScalarType.pvLong).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVLong primitiveValue = expectedPVStructure.getSubField(PVLong.class, "primitiveValue");
		primitiveValue.put(420);
		PVLong wrapperValue = expectedPVStructure.getSubField(PVLong.class, "wrapperValue");
		wrapperValue.put(5);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void LongprimitivesTestMaxMinValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		LongTestClass testClass = new LongTestClass();

		testClass.primitiveValue = Long.MAX_VALUE;
		testClass.wrapperValue = Long.MIN_VALUE;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvLong).
			add("wrapperValue", ScalarType.pvLong).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVLong primitiveValue = expectedPVStructure.getSubField(PVLong.class, "primitiveValue");
		primitiveValue.put(Long.MAX_VALUE);
		PVLong wrapperValue = expectedPVStructure.getSubField(PVLong.class, "wrapperValue");
		wrapperValue.put(Long.MIN_VALUE);
		
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
	public void ByteprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ByteTestClass testClass = new ByteTestClass();

		testClass.primitiveValue = 123;
		testClass.wrapperValue = 16;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvByte).
			add("wrapperValue", ScalarType.pvByte).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVByte primitiveValue = expectedPVStructure.getSubField(PVByte.class, "primitiveValue");
		primitiveValue.put((byte) 123);
		PVByte wrapperValue = expectedPVStructure.getSubField(PVByte.class, "wrapperValue");
		wrapperValue.put((byte) 16);
		
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
	public void ByteprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ByteTestClass testClass = new ByteTestClass();

		testClass.primitiveValue = 33;
		testClass.wrapperValue = 76;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvByte).
			add("wrapperValue", ScalarType.pvByte).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVByte primitiveValue = expectedPVStructure.getSubField(PVByte.class, "primitiveValue");
		primitiveValue.put((byte) 4);
		PVByte wrapperValue = expectedPVStructure.getSubField(PVByte.class, "wrapperValue");
		wrapperValue.put((byte) 3);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void ByteprimitivesTestMaxMinValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ByteTestClass testClass = new ByteTestClass();

		testClass.primitiveValue = Byte.MAX_VALUE;
		testClass.wrapperValue = Byte.MIN_VALUE;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvByte).
			add("wrapperValue", ScalarType.pvByte).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVByte primitiveValue = expectedPVStructure.getSubField(PVByte.class, "primitiveValue");
		primitiveValue.put(Byte.MAX_VALUE);
		PVByte wrapperValue = expectedPVStructure.getSubField(PVByte.class, "wrapperValue");
		wrapperValue.put(Byte.MIN_VALUE);
		
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
	public void BooleanprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		BooleanTestClass testClass = new BooleanTestClass();

		testClass.primitiveValue = true;
		testClass.wrapperValue = false;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvBoolean).
			add("wrapperValue", ScalarType.pvBoolean).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVBoolean primitiveValue = expectedPVStructure.getSubField(PVBoolean.class, "primitiveValue");
		primitiveValue.put(true);
		PVBoolean wrapperValue = expectedPVStructure.getSubField(PVBoolean.class, "wrapperValue");
		wrapperValue.put(false);
		
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
	public void BooleanprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		BooleanTestClass testClass = new BooleanTestClass();

		testClass.primitiveValue = true;
		testClass.wrapperValue = false;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvBoolean).
			add("wrapperValue", ScalarType.pvBoolean).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVBoolean primitiveValue = expectedPVStructure.getSubField(PVBoolean.class, "primitiveValue");
		primitiveValue.put(false);
		PVBoolean wrapperValue = expectedPVStructure.getSubField(PVBoolean.class, "wrapperValue");
		wrapperValue.put(true);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	

	
	@Test
	public void FloatprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		FloatTestClass testClass = new FloatTestClass();

		testClass.primitiveValue = 5.0f;
		testClass.wrapperValue = 65.666f;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvFloat).
			add("wrapperValue", ScalarType.pvFloat).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVFloat primitiveValue = expectedPVStructure.getSubField(PVFloat.class, "primitiveValue");
		primitiveValue.put(5.0f);
		PVFloat wrapperValue = expectedPVStructure.getSubField(PVFloat.class, "wrapperValue");
		wrapperValue.put(65.666f);
		
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
	public void FloatprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		FloatTestClass testClass = new FloatTestClass();

		testClass.primitiveValue = 7643f;
		testClass.wrapperValue = 123.6f;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvFloat).
			add("wrapperValue", ScalarType.pvFloat).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVFloat primitiveValue = expectedPVStructure.getSubField(PVFloat.class, "primitiveValue");
		primitiveValue.put(55.555f);
		PVFloat wrapperValue = expectedPVStructure.getSubField(PVFloat.class, "wrapperValue");
		wrapperValue.put(1.0f);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void FloatprimitivesTestMaxMinValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		FloatTestClass testClass = new FloatTestClass();

		testClass.primitiveValue = Float.MAX_VALUE;
		testClass.wrapperValue = Float.MIN_VALUE;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvFloat).
			add("wrapperValue", ScalarType.pvFloat).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVFloat primitiveValue = expectedPVStructure.getSubField(PVFloat.class, "primitiveValue");
		primitiveValue.put(Float.MAX_VALUE);
		PVFloat wrapperValue = expectedPVStructure.getSubField(PVFloat.class, "wrapperValue");
		wrapperValue.put(Float.MIN_VALUE);
		
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
	public void DoubleprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		DoubleTestClass testClass = new DoubleTestClass();

		testClass.primitiveValue = 5763563.2345;
		testClass.wrapperValue = 616545763d;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvDouble).
			add("wrapperValue", ScalarType.pvDouble).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVDouble primitiveValue = expectedPVStructure.getSubField(PVDouble.class, "primitiveValue");
		primitiveValue.put(5763563.2345);
		PVDouble wrapperValue = expectedPVStructure.getSubField(PVDouble.class, "wrapperValue");
		wrapperValue.put(616545763d);
		
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
	public void DoubleprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		DoubleTestClass testClass = new DoubleTestClass();

		testClass.primitiveValue = 652.67;
		testClass.wrapperValue = 99999999d;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvDouble).
			add("wrapperValue", ScalarType.pvDouble).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVDouble primitiveValue = expectedPVStructure.getSubField(PVDouble.class, "primitiveValue");
		primitiveValue.put(4465.23);
		PVDouble wrapperValue = expectedPVStructure.getSubField(PVDouble.class, "wrapperValue");
		wrapperValue.put(145642135);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void DoubleprimitivesTestMaxMinValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		DoubleTestClass testClass = new DoubleTestClass();

		testClass.primitiveValue = Double.MAX_VALUE;
		testClass.wrapperValue = Double.MIN_VALUE;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvDouble).
			add("wrapperValue", ScalarType.pvDouble).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVDouble primitiveValue = expectedPVStructure.getSubField(PVDouble.class, "primitiveValue");
		primitiveValue.put(Double.MAX_VALUE);
		PVDouble wrapperValue = expectedPVStructure.getSubField(PVDouble.class, "wrapperValue");
		wrapperValue.put(Double.MIN_VALUE);
		
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
	public void CharprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		CharTestClass testClass = new CharTestClass();

		testClass.primitiveValue = 'c';
		testClass.wrapperValue = 'h';
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvString).
			add("wrapperValue", ScalarType.pvString).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVString primitiveValue = expectedPVStructure.getSubField(PVString.class, "primitiveValue");
		primitiveValue.put("c");
		PVString wrapperValue = expectedPVStructure.getSubField(PVString.class, "wrapperValue");
		wrapperValue.put("h");
		
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
	public void CharprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		CharTestClass testClass = new CharTestClass();

		testClass.primitiveValue = 'a';
		testClass.wrapperValue = 'b';
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvString).
			add("wrapperValue", ScalarType.pvString).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVString primitiveValue = expectedPVStructure.getSubField(PVString.class, "primitiveValue");
		primitiveValue.put("y");
		PVString wrapperValue = expectedPVStructure.getSubField(PVString.class, "wrapperValue");
		wrapperValue.put("u");
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void CharprimitivesTestMaxMinValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		CharTestClass testClass = new CharTestClass();

		testClass.primitiveValue = Character.MAX_VALUE;
		testClass.wrapperValue = Character.MIN_VALUE;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvString).
			add("wrapperValue", ScalarType.pvString).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVString primitiveValue = expectedPVStructure.getSubField(PVString.class, "primitiveValue");
		primitiveValue.put(String.valueOf(Character.MAX_VALUE));
		PVString wrapperValue = expectedPVStructure.getSubField(PVString.class, "wrapperValue");
		wrapperValue.put(String.valueOf(Character.MIN_VALUE));
		
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
	public void StringprimitivesTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		StringTestClass testClass = new StringTestClass();

		testClass.stringValue = "a test string";
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("stringValue", ScalarType.pvString).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVString primitiveValue = expectedPVStructure.getSubField(PVString.class, "stringValue");
		primitiveValue.put("a test string");
		
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
	public void StringprimitivesTestInCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		StringTestClass testClass = new StringTestClass();

		testClass.stringValue = "a string";
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("stringValue", ScalarType.pvString).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVString primitiveValue = expectedPVStructure.getSubField(PVString.class, "stringValue");
		primitiveValue.put("a different string");
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertNotEquals(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void IntegerPrimitivesTestNullWrapperValue() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestClass testClass = new IntTestClass();

		testClass.wrapperValue = null;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("primitiveValue", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "primitiveValue");
		primitiveValue.put(0);
		
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
	public void TransientTestCorrectValues() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		TransientTestClass testClass = new TransientTestClass();

		testClass.myInt = 5;
		testClass.myTransientInt = 616;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("myInt", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "myInt");
		primitiveValue.put(5);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	public boolean PVEquals(PVStructure obj1, PVStructure obj2) {
		// TODO anything else?
		
			final PVField[] bfields = obj2.getPVFields(); 
			if (bfields.length == obj1.getPVFields().length)
			{
		        for (int i = 0; i < obj1.getPVFields().length; i++)
		        	if (!obj1.getPVFields()[i].equals(bfields[i]))
		        		return false;
		        
		        return true;
			}
			else
				return false;
	}

	class IntTestClass
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

	class ShortTestClass
	{
		private short primitiveValue;
		private Short wrapperValue;
		public short getPrimitiveValue() {
			return primitiveValue;
		}
		public Short getWrapperValue() {
			return wrapperValue;
		}
	}

	class LongTestClass
	{
		private long primitiveValue;
		private Long wrapperValue;
		public long getPrimitiveValue() {
			return primitiveValue;
		}
		public Long getWrapperValue() {
			return wrapperValue;
		}
	}

	class ByteTestClass
	{
		private byte primitiveValue;
		private Byte wrapperValue;
		public byte getPrimitiveValue() {
			return primitiveValue;
		}
		public Byte getWrapperValue() {
			return wrapperValue;
		}
	}

	class BooleanTestClass
	{
		private boolean primitiveValue;
		private Boolean wrapperValue;
		public boolean isPrimitiveValue() {
			return primitiveValue;
		}
		public Boolean getWrapperValue() {
			return wrapperValue;
		}
	}

	class FloatTestClass
	{
		private float primitiveValue;
		private Float wrapperValue;
		public float getPrimitiveValue() {
			return primitiveValue;
		}
		public Float getWrapperValue() {
			return wrapperValue;
		}
	}

	class DoubleTestClass
	{
		private double primitiveValue;
		private Double wrapperValue;
		public double getPrimitiveValue() {
			return primitiveValue;
		}
		public Double getWrapperValue() {
			return wrapperValue;
		}
	}

	class CharTestClass
	{
		private char primitiveValue;
		private Character wrapperValue;
		public char getPrimitiveValue() {
			return primitiveValue;
		}
		public Character getWrapperValue() {
			return wrapperValue;
		}
	}

	class StringTestClass
	{
		private String stringValue;

		public String getStringValue() {
			return stringValue;
		}
	}
	
	class TransientTestClass {
		private int myInt;
		private transient int myTransientInt;
		public int getMyInt() {
			return myInt;
		}
		public int getMyTransientInt() {
			return myTransientInt;
		}
		
	}

}
