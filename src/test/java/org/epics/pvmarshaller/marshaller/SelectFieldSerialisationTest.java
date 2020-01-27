package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class SelectFieldSerialisationTest {

	@Test
	public void testNoFields() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		SelectFieldsTestObject testClass = new SelectFieldsTestObject();

		testClass.intField = 3;
		testClass.stringField = "test string";
		testClass.doubleField = 1.5;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			LinkedList<String> noFields = new LinkedList<>();
			marshaller.registerFieldListForClass(SelectFieldsTestObject.class, noFields);
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testOneFieldOnly() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		SelectFieldsTestObject testClass = new SelectFieldsTestObject();

		testClass.intField = 3;
		testClass.stringField = "test string";
		testClass.doubleField = 1.5;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("intField", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "intField");
		primitiveValue.put(3);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			LinkedList<String> noFields = new LinkedList<>();
			noFields.add("intField");
			marshaller.registerFieldListForClass(SelectFieldsTestObject.class, noFields);
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testOneFieldInParentAndOneInChild() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ChildClass testClass = new ChildClass();

		testClass.intField = 3;
		testClass.stringField = "test string";
		testClass.doubleField = 1.5;
		testClass.newIntField = 18;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
				add("newIntField", ScalarType.pvInt).
				add("stringField", ScalarType.pvString).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt intField = expectedPVStructure.getIntField("newIntField");
		intField.put(18);
		PVString stringField = expectedPVStructure.getStringField("stringField");
		stringField.put("test string");
		PVStructure serialisedPVStructure = null;
		
		try {
			LinkedList<String> noFields = new LinkedList<>();
			noFields.add("newIntField");
			noFields.add("stringField");
			marshaller.registerFieldListForClass(SelectFieldsTestObject.class, noFields);
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testNoExcludeFields() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		SelectFieldsTestObject testClass = new SelectFieldsTestObject();

		testClass.intField = 3;
		testClass.stringField = "test string";
		testClass.doubleField = 1.5;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
				add("intField", ScalarType.pvInt).
				add("stringField", ScalarType.pvString).
				add("doubleField", ScalarType.pvDouble).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt intField = expectedPVStructure.getIntField("intField");
		intField.put(3);
		PVString stringField = expectedPVStructure.getStringField("stringField");
		stringField.put("test string");
		PVDouble doubleField = expectedPVStructure.getDoubleField("doubleField");
		doubleField.put(1.5);
		PVStructure serialisedPVStructure = null;
		
		try {
			LinkedList<String> noFields = new LinkedList<>();
			marshaller.registerExcludeFieldListForClass(SelectFieldsTestObject.class, noFields);
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testExcludeOneField() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		SelectFieldsTestObject testClass = new SelectFieldsTestObject();

		testClass.intField = 3;
		testClass.stringField = "test string";
		testClass.doubleField = 1.5;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
				add("stringField", ScalarType.pvString).
				add("doubleField", ScalarType.pvDouble).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVString stringField = expectedPVStructure.getStringField("stringField");
		stringField.put("test string");
		PVDouble doubleField = expectedPVStructure.getDoubleField("doubleField");
		doubleField.put(1.5);
		PVStructure serialisedPVStructure = null;
		
		try {
			LinkedList<String> noFields = new LinkedList<>();
			noFields.add("intField");
			marshaller.registerExcludeFieldListForClass(SelectFieldsTestObject.class, noFields);
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testExcludeOneFieldInParent() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		ChildClass testClass = new ChildClass();

		testClass.intField = 3;
		testClass.stringField = "test string";
		testClass.doubleField = 1.5;
		testClass.newIntField = 18;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
				add("newIntField", ScalarType.pvInt).
				add("stringField", ScalarType.pvString).
				add("doubleField", ScalarType.pvDouble).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt intField = expectedPVStructure.getIntField("newIntField");
		intField.put(18);
		PVString stringField = expectedPVStructure.getStringField("stringField");
		stringField.put("test string");
		PVDouble doubleField = expectedPVStructure.getDoubleField("doubleField");
		doubleField.put(1.5);
		PVStructure serialisedPVStructure = null;
		
		try {
			LinkedList<String> noFields = new LinkedList<>();
			noFields.add("intField");
			marshaller.registerExcludeFieldListForClass(SelectFieldsTestObject.class, noFields);
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	public static class SelectFieldsTestObject {
		int intField;
		String stringField;
		Double doubleField;
		
		public int getIntField() {
			return intField;
		}
		public void setIntField(int intField) {
			this.intField = intField;
		}
		public String getStringField() {
			return stringField;
		}
		public void setStringField(String stringField) {
			this.stringField = stringField;
		}
		public Double getDoubleField() {
			return doubleField;
		}
		public void setDoubleField(Double doubleField) {
			this.doubleField = doubleField;
		}
	}
	
	public static class ChildClass extends SelectFieldsTestObject {
		int newIntField;
		
		public int getNewIntField() {
			return newIntField;
		}

		public void setNewIntField(int newIntField) {
			this.newIntField = newIntField;
		}
	}
}
