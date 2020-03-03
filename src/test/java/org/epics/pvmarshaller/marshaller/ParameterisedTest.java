package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVInt;
import org.epics.pvdata.pv.PVIntArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class ParameterisedTest {

	@Test
	public void testParametersiedClass() {
		
		Box<Integer> testClass = new Box<Integer>();
		testClass.set(4);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
			add("myThing", ScalarType.pvInt).
			createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVInt primitiveValue = expectedPVStructure.getSubField(PVInt.class, "myThing");
		primitiveValue.put(4);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	@Test
	public void testParametersiedListClass() {
		
		ListOfParametersClass<Integer> testClass = new ListOfParametersClass<Integer>();
		testClass.myThings = new ArrayList<Integer>();
		testClass.myThings.add(123);
		testClass.myThings.add(89);
		
		// Create expected PVStructure
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();
		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		Structure structure = fieldCreate.createFieldBuilder().
				addArray("myThings", ScalarType.pvInt).
				createStructure();
			
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(structure);
		PVIntArray primitiveValue = expectedPVStructure.getSubField(PVIntArray.class, "myThings");
		int[] intArray = {123, 89};
		primitiveValue.put(0, 2, intArray, 0);
		
		PVStructure serialisedPVStructure = null;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			serialisedPVStructure = marshaller.toPVStructure(testClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		System.out.println("Serialised Structure:\n" + serialisedPVStructure + "\n---\n");
		
		TestHelper.assertPVStructuresEqual(expectedPVStructure, serialisedPVStructure);
	}
	
	static <E> Class<E> getClassE(List<E> list) {
	    Class<?> listClass = list.getClass();

	    Type gSuper = listClass.getGenericSuperclass();
	    if(!(gSuper instanceof ParameterizedType))
	        throw new IllegalArgumentException();

	    ParameterizedType pType = (ParameterizedType)gSuper;

	    Type tArg = pType.getActualTypeArguments()[0];
	    if(!(tArg instanceof Class<?>))
	        throw new IllegalArgumentException();

	    @SuppressWarnings("unchecked")
	    final Class<E> classE = (Class<E>)tArg;
	    return classE;
	}
	
	Class<?> getClassFromList(Field field, List<?> list) {
		Class<?> componentType = null;
		if ((list != null) && (!list.isEmpty())) {
			Object firstElement = list.get(0);
			componentType = firstElement.getClass();
		}
		return componentType;
	}
	
	class Box<T> {
	    private T myThing;

	    public void set(T myThing) { this.myThing = myThing; }
	    public T get() { return myThing; }
		public T getMyThing() {
			return myThing;
		}
	} 
	
	class ListOfParametersClass<T> {
		private List<T> myThings;

		public List<T> getMyThings() {
			return myThings;
		}
	}
	
	class ListOfIntsClass {
		private List<Integer> myIntegers;

		public List<Integer> getMyIntegers() {
			return myIntegers;
		}
	}
	
	interface MyInterface {
		
	}
	
	class MyImplementer1 implements MyInterface {
		int intFromImpl1;

		public int getIntFromImpl1() {
			return intFromImpl1;
		}
	}
	
	class MyImplementer2 implements MyInterface {
		int intFromImpl2;

		public int getIntFromImpl2() {
			return intFromImpl2;
		}
	}
	
	class ListOfInterfacesClass {
		private List<MyInterface> myInterfaces;

		public List<MyInterface> getMyInterfaces() {
			return myInterfaces;
		}
	}

}
