package org.epics.pvmarshaller.marshaller.tests;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVFloat;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVString;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.PVUnionArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Union;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.epics.pvmarshaller.marshaller.api.IPVStructureSerialiser;
import org.epics.pvmarshaller.marshaller.serialisers.Serialiser;
import org.junit.Test;

public class CustomSerialiserTests {

	@Test
	public void testRegisterClassSerialiser() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestClass testClass = new IntTestClass();

		testClass.primitiveValue = 5;
		testClass.wrapperValue = 616;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("CUSTOMprimitiveValue", ScalarType.pvInt).
			add("CUSTOMwrapperValue", ScalarType.pvInt).
			setId("IntTestClass").
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue.put(111);
		PVInt wrapperValue = expectedPVStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		wrapperValue.put(222);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			// Register a custom serialiser for the IntTestClass
			marshaller.registerSerialiser(IntTestClass.class, new CustomIntSerialiser());
			
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testRegisterClassSerialiserWithObjectMember() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestContainerClass testClass = new IntTestContainerClass();
		
		IntTestClass intTestClass = new IntTestClass();

		intTestClass.primitiveValue = 5;
		intTestClass.wrapperValue = 616;
		
		testClass.containerPrimitiveValue = 66;
		testClass.intTestClassObj = intTestClass;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure innerStructure = fieldCreate.createFieldBuilder().
			add("CUSTOMprimitiveValue", ScalarType.pvInt).
			add("CUSTOMwrapperValue", ScalarType.pvInt).
			setId("IntTestClass").
			createStructure();
		
		Structure testStructure = fieldCreate.createFieldBuilder().
				add("containerPrimitiveValue", ScalarType.pvInt).
				add("intTestClassObj", innerStructure).
				createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(testStructure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "containerPrimitiveValue");
		primitiveValue.put(66);
		PVStructure objFieldPVStructure = expectedPVStructure.getStructureField("intTestClassObj");
		PVInt customPrimitiveValue = objFieldPVStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		customPrimitiveValue.put(111);
		PVInt customerWrapperValue = objFieldPVStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue.put(222);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			// Register a custom serialiser for the IntTestClass
			marshaller.registerSerialiser(IntTestClass.class, new CustomIntSerialiser());
			
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testCustomSerilaiserArrayOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestArrayClass testClass = new IntTestArrayClass();

		testClass.containerPrimitiveValue = 99;
		testClass.intTestClassObjArray = new IntTestClass[2];
		
		IntTestClass testObject1 = new IntTestClass();
		testObject1.primitiveValue = 20;
		testObject1.wrapperValue = 21;
		
		IntTestClass testObject2 = new IntTestClass();
		testObject2.primitiveValue = 141;
		testObject2.wrapperValue = 142;
		
		testClass.intTestClassObjArray[0] = testObject1;
		testClass.intTestClassObjArray[1] = testObject2;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("CUSTOMprimitiveValue", ScalarType.pvInt).
				add("CUSTOMwrapperValue", ScalarType.pvInt).
				setId("IntTestClass").
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("containerPrimitiveValue", ScalarType.pvInt).
			addArray("intTestClassObjArray", union).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue1.put(111);
		PVInt customerWrapperValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue1.put(222);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue2.put(111);
		PVInt customerWrapperValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue2.put(222);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "containerPrimitiveValue");
		primitiveValue.put(99);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		PVUnionArray pvUnionValue = expectedPVStructure.getSubField(PVUnionArray.class, "intTestClassObjArray");
		pvUnionValue.put(0, 2, unionArray, 0);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			// Register a custom serialiser for the IntTestClass
			marshaller.registerSerialiser(IntTestClass.class, new CustomIntSerialiser());
			
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testCustomSerilaiserListOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestListClass testClass = new IntTestListClass();

		testClass.containerPrimitiveValue = 99;
		testClass.intTestClassObjList = new LinkedList<IntTestClass>();
		
		IntTestClass testObject1 = new IntTestClass();
		testObject1.primitiveValue = 20;
		testObject1.wrapperValue = 21;
		
		IntTestClass testObject2 = new IntTestClass();
		testObject2.primitiveValue = 141;
		testObject1.wrapperValue = 142;
		
		testClass.intTestClassObjList.add(testObject1);
		testClass.intTestClassObjList.add(testObject2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

		Union union = fieldCreate.createVariantUnion();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("CUSTOMprimitiveValue", ScalarType.pvInt).
				add("CUSTOMwrapperValue", ScalarType.pvInt).
				setId("IntTestClass").
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("containerPrimitiveValue", ScalarType.pvInt).
			addArray("intTestClassObjList", union).
			createStructure();
		
		PVStructure expectedNestedPVStructure1 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue1.put(111);
		PVInt customerWrapperValue1 = expectedNestedPVStructure1.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue1.put(222);
		PVStructure expectedNestedPVStructure2 = pvDataCreate.createPVStructure(nestedStructure);
		PVInt primitiveValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		primitiveValue2.put(111);
		PVInt customerWrapperValue2 = expectedNestedPVStructure2.getSubField(PVInt.class, "CUSTOMwrapperValue");
		customerWrapperValue2.put(222);
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "containerPrimitiveValue");
		primitiveValue.put(99);

		PVUnion pvu1 = pvDataCreate.createPVVariantUnion();
		pvu1.set(expectedNestedPVStructure1);
		PVUnion pvu2 = pvDataCreate.createPVVariantUnion();
		pvu2.set(expectedNestedPVStructure2);
		
		PVUnion[] unionArray = new PVUnion[2];
		unionArray[0] = pvu1;
		unionArray[1] = pvu2;
		PVUnionArray pvUnionValue = expectedPVStructure.getSubField(PVUnionArray.class, "intTestClassObjList");
		pvUnionValue.put(0, 2, unionArray, 0);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			// Register a custom serialiser for the IntTestClass
			marshaller.registerSerialiser(IntTestClass.class, new CustomIntSerialiser());
			
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}

	@Test
	public void testCustomSerilaiserMapOfObjects() {
		PVMarshaller marshaller = new PVMarshaller();
		
		// Create test class to serialise
		IntTestMapClass testClass = new IntTestMapClass();

		testClass.containerPrimitiveValue = 11;
		testClass.intTestClassObjMap = new LinkedHashMap<String, IntTestClass>();
		
		IntTestClass testObject1 = new IntTestClass();
		testObject1.primitiveValue = 20;
		testObject1.wrapperValue = 21;
		
		IntTestClass testObject2 = new IntTestClass();
		testObject2.primitiveValue = 141;
		testObject1.wrapperValue = 142;
		
		testClass.intTestClassObjMap.put("to1", testObject1);
		testClass.intTestClassObjMap.put("to2", testObject2);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure nestedStructure = fieldCreate.createFieldBuilder().
				add("CUSTOMprimitiveValue", ScalarType.pvInt).
				add("CUSTOMwrapperValue", ScalarType.pvInt).
				setId("IntTestClass").
				createStructure();

		Structure mapStructure = fieldCreate.createFieldBuilder().
			    add("to1", nestedStructure).
			    add("to2", nestedStructure).
				createStructure();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("containerPrimitiveValue", ScalarType.pvInt).
			    add("intTestClassObjMap", mapStructure).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);

		PVStructure mapPVStructure = expectedPVStructure.getStructureField("intTestClassObjMap");
		
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "containerPrimitiveValue");
		primitiveValue.put(11);
		
		PVStructure object1PVStructure = mapPVStructure.getStructureField("to1");
		PVInt o1primitiveValue1 = object1PVStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		o1primitiveValue1.put(111);
		PVInt o1customerWrapperValue1 = object1PVStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		o1customerWrapperValue1.put(222);
		
		PVStructure object2PVStructure = mapPVStructure.getStructureField("to2");
		PVInt o2primitiveValue1 = object2PVStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
		o2primitiveValue1.put(111);
		PVInt o2customerWrapperValue1 = object2PVStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
		o2customerWrapperValue1.put(222);
		
		
		PVStructure serialisedPVStructure = null;
		
		try {
			// Register a custom serialiser for the IntTestClass
			marshaller.registerSerialiser(IntTestClass.class, new CustomIntSerialiser());
			
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testCustomSerialiseWithInterface() {
		
		// Create test class to serialise
		ImplementingClass testObject = new ImplementingClass();
		testObject.implentingInteger = 25;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("aNumber", ScalarType.pvInt).
				setId("TESTID").
				createStructure();
				
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt containingValue = expectedPVStructure.getSubField(PVInt.class, "aNumber");
		containingValue.put(50); // expect double as custom serialiser doubles the value
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			
			// Register a custom deserialiser for the interface
			marshaller.registerSerialiser(IAnInterface.class, new CustomDoublerSerialiser());
			serialisedPVStructure = marshaller.toPVStructure(testObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
				
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testCustomSerialiseWithExtends() {
		
		// Create test class to serialise
		ChildClass testObject = new ChildClass();
		testObject.childCharVal = 'g';
		testObject.parentFloatVal = 1.1f;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("aDouble", ScalarType.pvDouble).
				setId("BaseVal").
				createStructure();
				
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVDouble containingValue = expectedPVStructure.getSubField(PVDouble.class, "aDouble");
		containingValue.put(5.5d); // expect double as custom serialiser multiplies the float value by 5
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			
			// Register a custom deserialiser for the parent class
			marshaller.registerSerialiser(ParentClass.class, new CustomExtendsSerialiser());
			serialisedPVStructure = marshaller.toPVStructure(testObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
				
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testCustomSerialiseWithInterfaceInParent() {
		
		// Create test class to serialise
		ChildOfImplementingClass testObject = new ChildOfImplementingClass();
		testObject.childBooleanVal = true;
		testObject.setInt(123);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("aNumber", ScalarType.pvInt).
				setId("TESTID").
				createStructure();
				
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt containingValue = expectedPVStructure.getSubField(PVInt.class, "aNumber");
		containingValue.put(246); // expect double as custom serialiser doubles the value
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			
			// Register a custom deserialiser for the interface
			marshaller.registerSerialiser(IAnInterface.class, new CustomDoublerSerialiser());
			serialisedPVStructure = marshaller.toPVStructure(testObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
				
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testCustomSerialiseWith2InterfacesThrowsException() {
		
		// Create test class to serialise.
		Implements2InterfacesClass testObject = new Implements2InterfacesClass();
		testObject.implentingInteger = 25;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			
			// Register custom deserialisers for the interfaces
			marshaller.registerSerialiser(IAnInterface.class, new CustomDoublerSerialiser());
			marshaller.registerSerialiser(IAnotherInterface.class, new CustomAnotherSerialiser());
			marshaller.toPVStructure(testObject);
			fail("No exception thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("more than one custom serialiser"));
		}
	}
	
	@Test
	public void testCustomSerialiseChildWith2Interfaces() {
		
		// Create test class to serialise.
		Implements2InterfacesChildClass testObject = new Implements2InterfacesChildClass();
		testObject.setInt(25);
		testObject.childBooleanVal = true;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			
			// Register custom deserialisers for the interfaces
			marshaller.registerSerialiser(IAnInterface.class, new CustomDoublerSerialiser());
			marshaller.registerSerialiser(IAnotherInterface.class, new CustomAnotherSerialiser());
			marshaller.toPVStructure(testObject);
		} catch (Exception e) {
			fail("No exception thrown");
			assertTrue(e instanceof IllegalArgumentException);
			assertTrue(e.getMessage().contains("more than one custom serialiser"));
		}
	}

	@Test
	public void testRegisterClassIdMapping() {
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
			setId("TestId").
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "primitiveValue");
		primitiveValue.put(5);
		PVInt wrapperValue = expectedPVStructure.getSubField(PVInt.class, "wrapperValue");
		wrapperValue.put(616);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			// Register an Id for the IntTestClass
			marshaller.registerIdForClass(IntTestClass.class, "TestId");
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		assertEquals(expectedPVStructure.getStructure(), serialisedPVStructure.getStructure());
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testClassIdMappingWithInterface() {
		
		// Create test class to serialise
		ImplementingClass testObject = new ImplementingClass();
		testObject.implentingInteger = 25;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("implentingInteger", ScalarType.pvInt).
				setId("TiD").
				createStructure();
				
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt containingValue = expectedPVStructure.getSubField(PVInt.class, "implentingInteger");
		containingValue.put(25);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			
			// Register an id for the interface
			marshaller.registerIdForClass(IAnInterface.class, "TiD");
			serialisedPVStructure = marshaller.toPVStructure(testObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
				
		assertEquals(expectedPVStructure.getStructure(), serialisedPVStructure.getStructure());
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testClassIdMappingWithExtends() {
		
		// Create test class to serialise
		ChildClass testObject = new ChildClass();
		testObject.childCharVal = 'g';
		testObject.parentFloatVal = 1.1f;
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure structure = fieldCreate.createFieldBuilder().
				add("childCharVal", ScalarType.pvString).
				add("parentFloatVal", ScalarType.pvFloat).
				setId("TestID").
				createStructure();
				
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVString containingValueStr = expectedPVStructure.getSubField(PVString.class, "childCharVal");
		containingValueStr.put("g");
		PVFloat containingValue = expectedPVStructure.getSubField(PVFloat.class, "parentFloatVal");
		containingValue.put(1.1f);
		
		System.out.println("s:");
		System.out.println(structure);
		System.out.println("pv:");
		System.out.println(expectedPVStructure);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			
			// Register an id for the parent class
			marshaller.registerIdForClass(ParentClass.class, "TestID");
			serialisedPVStructure = marshaller.toPVStructure(testObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertEquals(expectedPVStructure.getStructure(), serialisedPVStructure.getStructure());
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
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

	class IntTestContainerClass
	{
		private int containerPrimitiveValue;
		private IntTestClass intTestClassObj;
		public int getContainerPrimitiveValue() {
			return containerPrimitiveValue;
		}
		public IntTestClass getIntTestClassObj() {
			return intTestClassObj;
		}
	}

	class IntTestArrayClass
	{
		private int containerPrimitiveValue;
		private IntTestClass intTestClassObjArray[];
		public int getContainerPrimitiveValue() {
			return containerPrimitiveValue;
		}
		public IntTestClass[] getIntTestClassObjArray() {
			return intTestClassObjArray;
		}
	}

	class IntTestListClass
	{
		private int containerPrimitiveValue;
		private List<IntTestClass> intTestClassObjList;
		public int getContainerPrimitiveValue() {
			return containerPrimitiveValue;
		}
		public List<IntTestClass> getIntTestClassObjList() {
			return intTestClassObjList;
		}
	}

	class IntTestMapClass
	{
		private int containerPrimitiveValue;
		private Map<String,IntTestClass> intTestClassObjMap;
		public int getContainerPrimitiveValue() {
			return containerPrimitiveValue;
		}
		public Map<String, IntTestClass> getIntTestClassObjMap() {
			return intTestClassObjMap;
		}
	}
	
	public static interface IAnInterface {
		int getInt();

		@Override
		int hashCode();

		@Override
		boolean equals(Object obj);
	}
	
	public static interface IAnotherInterface {
		int getValue();
	}
	
	public static class ImplementingClass implements IAnInterface {

		private int implentingInteger;

		public int getImplentingInteger() {
			return implentingInteger;
		}

		@Override
		public int getInt() {
			return implentingInteger;
			
		}
		
		public void setInt(int val) {
			implentingInteger = val;
		}
	}
	
	class ChildOfImplementingClass extends ImplementingClass {
		private boolean childBooleanVal;

		public boolean isChildBooleanVal() {
			return childBooleanVal;
		}
	}
	
	class ParentClass {
		Float parentFloatVal;

		public Float getParentFloatVal() {
			return parentFloatVal;
		}
	}
	
	class ChildClass extends ParentClass {
		private char childCharVal;

		public char getChildCharVal() {
			return childCharVal;
		}
	}
	
	class Implements2InterfacesClass implements IAnInterface, IAnotherInterface {

		private int implentingInteger;

		public int getImplentingInteger() {
			return implentingInteger;
		}

		@Override
		public int getInt() {
			return implentingInteger;
		}
		
		public void setInt(int val) {
			implentingInteger = val;
		}

		@Override
		public int getValue() {
			return implentingInteger;
		}
	}
	
	class Implements2InterfacesChildClass extends ImplementingClass implements IAnotherInterface {

		private boolean childBooleanVal;

		public boolean isChildBooleanVal() {
			return childBooleanVal;
		}

		@Override
		public int getValue() {
			return 123;
		}
	}
	
	class CustomIntSerialiser implements IPVStructureSerialiser<IntTestClass> {

		@Override
		public Structure buildStructure(Serialiser serialiser, IntTestClass source) {
			FieldCreate fieldCreate = FieldFactory.getFieldCreate();
			Structure structure = fieldCreate.createFieldBuilder().
				add("CUSTOMprimitiveValue", ScalarType.pvInt).
				add("CUSTOMwrapperValue", ScalarType.pvInt).
				setId("IntTestClass").
				createStructure();
			return structure;
		}

		@Override
		public void populatePVStructure(Serialiser serialiser, IntTestClass source, PVStructure pvStructure) {
			PVInt primitiveValue = pvStructure.getSubField(PVInt.class, "CUSTOMprimitiveValue");
			primitiveValue.put(111);
			PVInt wrapperValue = pvStructure.getSubField(PVInt.class, "CUSTOMwrapperValue");
			wrapperValue.put(222);
		}
		
	}
	
	class CustomDoublerSerialiser implements IPVStructureSerialiser<IAnInterface> {

		@Override
		public Structure buildStructure(Serialiser serialiser, IAnInterface source) {
			FieldCreate fieldCreate = FieldFactory.getFieldCreate();
			Structure structure = fieldCreate.createFieldBuilder().
				add("aNumber", ScalarType.pvInt).
				setId("TESTID").
				createStructure();
			return structure;
		}

		@Override
		public void populatePVStructure(Serialiser serialiser, IAnInterface source, PVStructure pvStructure) {
			PVInt primitiveValue = pvStructure.getSubField(PVInt.class, "aNumber");
			primitiveValue.put(source.getInt() * 2);			
		}
	}
	
	class CustomExtendsSerialiser implements IPVStructureSerialiser<ParentClass> {

		@Override
		public Structure buildStructure(Serialiser serialiser, ParentClass source) {
			FieldCreate fieldCreate = FieldFactory.getFieldCreate();
			Structure structure = fieldCreate.createFieldBuilder().
				add("aDouble", ScalarType.pvDouble).
				setId("BaseVal").
				createStructure();
			return structure;
		}

		@Override
		public void populatePVStructure(Serialiser serialiser, ParentClass source, PVStructure pvStructure) {
			PVDouble primitiveValue = pvStructure.getSubField(PVDouble.class, "aDouble");
			primitiveValue.put(source.parentFloatVal * 5);			
		}
	}
	
	class CustomAnotherSerialiser implements IPVStructureSerialiser<IAnotherInterface> {

		@Override
		public Structure buildStructure(Serialiser serialiser, IAnotherInterface source) {
			FieldCreate fieldCreate = FieldFactory.getFieldCreate();
			Structure structure = fieldCreate.createFieldBuilder().
				add("aNumber", ScalarType.pvInt).
				setId("TESTID").
				createStructure();
			return structure;
		}

		@Override
		public void populatePVStructure(Serialiser serialiser, IAnotherInterface source, PVStructure pvStructure) {
			PVInt primitiveValue = pvStructure.getSubField(PVInt.class, "aNumber");
			primitiveValue.put(source.getValue() * 2);			
		}
	}
}
