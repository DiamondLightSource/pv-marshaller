package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.*;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class DeserialiseNestedObjectTest {

	@Test
	public void testDeserialiseNestedObject() {
		PVMarshaller marshaller = new PVMarshaller();

		ParentTestClass expectedObject = new ParentTestClass();
		expectedObject.childObject = new ChildTestClass();
		expectedObject.childObject.primitiveValue = 99;

		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure childStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				setId("errrr").
				createStructure();
		
		Structure parentStructure = fieldCreate.createFieldBuilder().
				add("childObject", childStructure).
				createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(parentStructure);
		
		PVStructure childPVStructure = testPVStructure.getStructureField("childObject");
		PVInt childIntValue = childPVStructure.getSubField(PVInt.class, "primitiveValue");
		childIntValue.put(99);
		
		try {
			ParentTestClass deserialisedObject = marshaller.fromPVStructure(testPVStructure, ParentTestClass.class);
			assertEquals(expectedObject, deserialisedObject);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testDeserialiseUnknownObject() {
		PVMarshaller marshaller = new PVMarshaller();

		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure childStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				createStructure();
		
		Structure parentStructure = fieldCreate.createFieldBuilder().
				add("unknownObject", childStructure).
				createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(parentStructure);
		
		PVStructure childPVStructure = testPVStructure.getStructureField("unknownObject");
		PVInt childIntValue = childPVStructure.getSubField(PVInt.class, "primitiveValue");
		childIntValue.put(99);
		
		try {
			marshaller.fromPVStructure(testPVStructure, ParentTestClass.class);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("unknownObject"));
		}
	}

	@Test
	public void testDeserialiseUnknownObjectWithIgnore() {
		PVMarshaller marshaller = new PVMarshaller();

		ParentTestClass expectedObject = new ParentTestClass();
		expectedObject.childObject = new ChildTestClass();
		expectedObject.childObject.primitiveValue = 99;

		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure childStructure = fieldCreate.createFieldBuilder().
				add("primitiveValue", ScalarType.pvInt).
				setId("errrr").
				createStructure();
		
		Structure parentStructure = fieldCreate.createFieldBuilder().
				add("unknownObject", childStructure).
				add("childObject", childStructure).
				createStructure();
		
		PVStructure testPVStructure = pvDataCreate.createPVStructure(parentStructure);
		
		PVStructure childPVStructure = testPVStructure.getStructureField("childObject");
		PVInt childIntValue = childPVStructure.getSubField(PVInt.class, "primitiveValue");
		childIntValue.put(99);
		
		PVStructure unknownPVStructure = testPVStructure.getStructureField("unknownObject");
		PVInt unknownIntValue = unknownPVStructure.getSubField(PVInt.class, "primitiveValue");
		unknownIntValue.put(99);
		
		try {
			marshaller.setIgnoreUnknownFields(true);
			ParentTestClass deserialisedObject = marshaller.fromPVStructure(testPVStructure, ParentTestClass.class);
			assertEquals(expectedObject, deserialisedObject);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static class ParentTestClass {
		private ChildTestClass childObject;

		public ChildTestClass getChildObject() {
			return childObject;
		}

		public void setChildObject(ChildTestClass childObject) {
			this.childObject = childObject;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((childObject == null) ? 0 : childObject.hashCode());
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
			ParentTestClass other = (ParentTestClass) obj;
			if (childObject == null) {
				if (other.childObject != null)
					return false;
			} else if (!childObject.equals(other.childObject))
				return false;
			return true;
		}
		
		
	}
	
	public static class ChildTestClass {
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
			ChildTestClass other = (ChildTestClass) obj;
			if (primitiveValue != other.primitiveValue)
				return false;
			return true;
		}
		
	}
}
