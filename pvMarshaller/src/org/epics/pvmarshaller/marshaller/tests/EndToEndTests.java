package org.epics.pvmarshaller.marshaller.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.epics.pvdata.pv.PVStructure;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.junit.Test;

public class EndToEndTests {

	@Test
	public void testPrimitvesEndToEnd() {
		PrimitivesTestClass testObject = new PrimitivesTestClass();
		testObject.primitiveIntValue = 1;
		testObject.wrapperIntValue = 2;

		testObject.primitiveShortValue = 3;  
		testObject.wrapperShortValue = 4;  
     
		testObject.primitiveLongValue = 5;    
		testObject.wrapperLongValue = 6l;  
      
		testObject.primitiveByteValue = 7;    
		testObject.wrapperByteValue = 8;  
      
		testObject.primitiveBooleanValue = true;
		testObject.wrapperBooleanValue = false;
     
		testObject.primitiveFloatValue = 9.1f;  
		testObject.wrapperFloatValue = 10.2f;
		
		testObject.primitiveDoubleValue = 11.3;
		testObject.wrapperDoubleValue = 12.4;
      
		testObject.primitiveCharValue = 'a';     
		testObject.wrapperCharValue = 'b';  
		
		testObject.stringValue = "Test String";
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			PVStructure serialisedStrucutre = marshaller.toPVStructure(testObject);
			PrimitivesTestClass deserialisedObject = marshaller.fromPVStructure(serialisedStrucutre, PrimitivesTestClass.class);
			assertEquals(testObject, deserialisedObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected Exception " + e.getMessage());
		}		
	}
	
	@Test
	public void testContainersEndToEnd() {
		ContainersTestClass testObject = new ContainersTestClass();
		
		int pArray[] = {10,20,30,40,50};
		testObject.primitiveArray = pArray;
		Integer wArray[] = {6,7,8,9};
		testObject.wrapperArray = wArray;
		PrimitivesTestClass o1 = new PrimitivesTestClass();
		o1.primitiveBooleanValue = true;
		o1.wrapperByteValue = (byte)77;
		PrimitivesTestClass o2 = new PrimitivesTestClass();
		o2.primitiveShortValue = 3;
		o2.wrapperByteValue = (byte)31;
		PrimitivesTestClass oArray[] = {o1, o2};
		testObject.objectArray = oArray;
		
		testObject.wrapperList = new LinkedList<>();
		testObject.wrapperList.add(77777l);
		testObject.wrapperList.add(888l);
		testObject.wrapperList.add(9999l);
		testObject.objectList = new ArrayList<>();
		PrimitivesTestClass o3 = new PrimitivesTestClass();
		o3.primitiveShortValue = -4;
		o3.stringValue = "asdfg";
		PrimitivesTestClass o4 = new PrimitivesTestClass();
		o4.primitiveDoubleValue = 999d;
		o4.stringValue = "qwerty";
		PrimitivesTestClass o5 = new PrimitivesTestClass();
		o5.primitiveIntValue = 35;
		o5.stringValue = "zxcv";
		testObject.objectList.add(o3);
		testObject.objectList.add(o4);
		testObject.objectList.add(o5);
		
		testObject.wrapperMap = new LinkedHashMap<>();
		testObject.wrapperMap.put("mk1", 123.456);
		testObject.wrapperMap.put("mk2", 6.34);
		testObject.wrapperMap.put("mk3", 345345345d);
		testObject.objectMap = new HashMap<>();
		PrimitivesTestClass o6 = new PrimitivesTestClass();
		o6.primitiveShortValue = 894;
		o6.wrapperCharValue = 'p';
		o6.wrapperBooleanValue = true;
		PrimitivesTestClass o7 = new PrimitivesTestClass();
		o7.primitiveShortValue = 23;
		o7.wrapperCharValue = '6';
		o7.wrapperBooleanValue = false;
		PrimitivesTestClass o8 = new PrimitivesTestClass();
		o8.primitiveShortValue = 999;
		o8.wrapperCharValue = '!';
		o8.wrapperBooleanValue = true;
		testObject.objectMap.put("omk1", o6);
		testObject.objectMap.put("omk2", o7);
		testObject.objectMap.put("omk3", o8);
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			PVStructure serialisedStrucutre = marshaller.toPVStructure(testObject);
			ContainersTestClass deserialisedObject = marshaller.fromPVStructure(serialisedStrucutre, ContainersTestClass.class);
			assertEquals(testObject, deserialisedObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected Exception " + e.getMessage());
		}		
	}
	
	@Test
	public void testContainersEndToEndFailsDueToDifferentStructures() {
		ContainersTestClass testObject = new ContainersTestClass();
		
		int pArray[] = {10,20,30,40,50};
		testObject.primitiveArray = pArray;
		Integer wArray[] = {6,7,8,9};
		testObject.wrapperArray = wArray;
		PrimitivesTestClass o1 = new PrimitivesTestClass();
		o1.primitiveBooleanValue = true;
		o1.wrapperByteValue = (byte)77;
		PrimitivesTestClass o2 = new PrimitivesTestClass();
		o2.primitiveShortValue = 3;
		o2.wrapperCharValue = 'k';
		PrimitivesTestClass oArray[] = {o1, o2};
		testObject.objectArray = oArray;
		
		testObject.wrapperList = new LinkedList<>();
		testObject.wrapperList.add(77777l);
		testObject.wrapperList.add(888l);
		testObject.wrapperList.add(9999l);
		testObject.objectList = new ArrayList<>();
		PrimitivesTestClass o3 = new PrimitivesTestClass();
		o3.primitiveShortValue = -4;
		o3.wrapperFloatValue = 5.5f;
		PrimitivesTestClass o4 = new PrimitivesTestClass();
		o4.primitiveDoubleValue = 999d;
		o4.stringValue = "qwerty";
		PrimitivesTestClass o5 = new PrimitivesTestClass();
		o5.primitiveIntValue = 35;
		o5.wrapperBooleanValue = true;
		testObject.objectList.add(o3);
		testObject.objectList.add(o4);
		testObject.objectList.add(o5);
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			PVStructure serialisedStrucutre = marshaller.toPVStructure(testObject);
			ContainersTestClass deserialisedObject = marshaller.fromPVStructure(serialisedStrucutre, ContainersTestClass.class);
			assertEquals(testObject, deserialisedObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected Exception " + e.getMessage());
		}		
	}

	@Test
	public void testNestedObjectsEndToEnd() {
		NestedObjectsTestClass testObject = new NestedObjectsTestClass();
		testObject.primitiveValue = 1.98f;
		
		testObject.objectValue = new PrimitivesTestClass();
		testObject.objectValue.primitiveLongValue = 3444l;
		testObject.objectValue.wrapperShortValue = 99;
		
		testObject.containersValue = new ContainersTestClass();
		testObject.containersValue.wrapperList = new LinkedList<>();
		testObject.containersValue.wrapperList.add(123l);
		testObject.containersValue.wrapperList.add(555l);
		testObject.containersValue.wrapperList.add(67890l);
		
		testObject.containersValue.objectArray = new PrimitivesTestClass[2];
		PrimitivesTestClass ptc1 = new PrimitivesTestClass();
		ptc1.primitiveBooleanValue = false;
		ptc1.wrapperCharValue = 't';
		PrimitivesTestClass ptc2 = new PrimitivesTestClass();
		ptc2.primitiveIntValue = 421;
		ptc2.wrapperCharValue = 'j';
		testObject.containersValue.objectArray[0] = ptc1;
		testObject.containersValue.objectArray[1] = ptc2;
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			PVStructure serialisedStrucutre = marshaller.toPVStructure(testObject);
			NestedObjectsTestClass deserialisedObject = marshaller.fromPVStructure(serialisedStrucutre, NestedObjectsTestClass.class);
			assertEquals(testObject, deserialisedObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected Exception " + e.getMessage());
		}		
	}	

	@Test
	public void testExtendsEndToEnd() {
		ExtendingClass testObject = new ExtendingClass();
		testObject.setPrimitiveIntValue(1);
		testObject.setWrapperIntValue(2);

		testObject.setPrimitiveShortValue((short)3);  
		testObject.setWrapperShortValue((short)4);  
     
		testObject.setPrimitiveLongValue(5);    
		testObject.setWrapperLongValue(6l);  
      
		testObject.setPrimitiveByteValue((byte)7);    
		testObject.setWrapperByteValue((byte)8);  
      
		testObject.setPrimitiveBooleanValue(true);
		testObject.setWrapperBooleanValue(false);
     
		testObject.setPrimitiveFloatValue(9.1f);  
		testObject.setWrapperFloatValue(10.2f);
		
		testObject.setPrimitiveDoubleValue(11.3);
		testObject.setWrapperDoubleValue(12.4);
      
		testObject.setPrimitiveCharValue('a');     
		testObject.setWrapperCharValue('b');  
		
		testObject.setStringValue("Test String");
		
		testObject.extendFloat = 33434.6f;
		testObject.extendString = "ExtendingString";
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			PVStructure serialisedStrucutre = marshaller.toPVStructure(testObject);
			PrimitivesTestClass deserialisedObject = marshaller.fromPVStructure(serialisedStrucutre, ExtendingClass.class);
			assertEquals(testObject, deserialisedObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected Exception " + e.getMessage());
		}		
	}
	
	@Test
	public void testParametersiedClassEndToEnd() {
		
		Box<Integer> testObject = new Box<Integer>();
		testObject.setMyThing(4);
		
		try {
			PVMarshaller marshaller = new PVMarshaller();
			PVStructure serialisedStrucutre = marshaller.toPVStructure(testObject);
			Box<?> deserialisedObject = marshaller.fromPVStructure(serialisedStrucutre, Box.class);
			assertEquals(testObject, deserialisedObject);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected Exception " + e.getMessage());
		}	
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

	public static class ContainersTestClass {
		private int primitiveArray[];
		private Integer wrapperArray[];
		private PrimitivesTestClass objectArray[];
		
		private List<Long> wrapperList;
		private List<PrimitivesTestClass> objectList;
		
		private Map<String, Double> wrapperMap;
		private Map<String, PrimitivesTestClass> objectMap;
		
		public int[] getPrimitiveArray() {
			return primitiveArray;
		}
		public void setPrimitiveArray(int[] primitiveArray) {
			this.primitiveArray = primitiveArray;
		}
		public Integer[] getWrapperarray() {
			return wrapperArray;
		}
		public void setWrapperarray(Integer[] wrapperarray) {
			this.wrapperArray = wrapperarray;
		}
		public PrimitivesTestClass[] getObjectArray() {
			return objectArray;
		}
		public void setObjectArray(PrimitivesTestClass[] objectArray) {
			this.objectArray = objectArray;
		}
		public List<Long> getWrapperList() {
			return wrapperList;
		}
		public void setWrapperList(List<Long> wrapperList) {
			this.wrapperList = wrapperList;
		}
		public List<PrimitivesTestClass> getObjectList() {
			return objectList;
		}
		public void setObjectList(List<PrimitivesTestClass> objectList) {
			this.objectList = objectList;
		}
		public Map<String, Double> getWrapperMap() {
			return wrapperMap;
		}
		public void setWrapperMap(Map<String, Double> wrapperMap) {
			this.wrapperMap = wrapperMap;
		}
		public Map<String, PrimitivesTestClass> getObjectMap() {
			return objectMap;
		}
		public void setObjectMap(Map<String, PrimitivesTestClass> objectMap) {
			this.objectMap = objectMap;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(objectArray);
			result = prime * result + ((objectList == null) ? 0 : objectList.hashCode());
			result = prime * result + ((objectMap == null) ? 0 : objectMap.hashCode());
			result = prime * result + Arrays.hashCode(primitiveArray);
			result = prime * result + ((wrapperList == null) ? 0 : wrapperList.hashCode());
			result = prime * result + ((wrapperMap == null) ? 0 : wrapperMap.hashCode());
			result = prime * result + Arrays.hashCode(wrapperArray);
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
			ContainersTestClass other = (ContainersTestClass) obj;
			if (!Arrays.equals(objectArray, other.objectArray))
				return false;
			if (objectList == null) {
				if (other.objectList != null)
					return false;
			} else if (!objectList.equals(other.objectList))
				return false;
			if (objectMap == null) {
				if (other.objectMap != null)
					return false;
			} else if (!objectMap.equals(other.objectMap))
				return false;
			if (!Arrays.equals(primitiveArray, other.primitiveArray))
				return false;
			if (wrapperList == null) {
				if (other.wrapperList != null)
					return false;
			} else if (!wrapperList.equals(other.wrapperList))
				return false;
			if (wrapperMap == null) {
				if (other.wrapperMap != null)
					return false;
			} else if (!wrapperMap.equals(other.wrapperMap))
				return false;
			if (!Arrays.equals(wrapperArray, other.wrapperArray))
				return false;
			return true;
		}
	}

	public static class NestedObjectsTestClass {
		float primitiveValue;
		PrimitivesTestClass objectValue;
		ContainersTestClass containersValue;
		public float getPrimitiveValue() {
			return primitiveValue;
		}
		public void setPrimitiveValue(float primitiveValue) {
			this.primitiveValue = primitiveValue;
		}
		public PrimitivesTestClass getObjectValue() {
			return objectValue;
		}
		public void setObjectValue(PrimitivesTestClass objectValue) {
			this.objectValue = objectValue;
		}
		public ContainersTestClass getContainersValue() {
			return containersValue;
		}
		public void setContainersValue(ContainersTestClass containersValue) {
			this.containersValue = containersValue;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((containersValue == null) ? 0 : containersValue.hashCode());
			result = prime * result + ((objectValue == null) ? 0 : objectValue.hashCode());
			result = prime * result + Float.floatToIntBits(primitiveValue);
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
			NestedObjectsTestClass other = (NestedObjectsTestClass) obj;
			if (containersValue == null) {
				if (other.containersValue != null)
					return false;
			} else if (!containersValue.equals(other.containersValue))
				return false;
			if (objectValue == null) {
				if (other.objectValue != null)
					return false;
			} else if (!objectValue.equals(other.objectValue))
				return false;
			if (Float.floatToIntBits(primitiveValue) != Float.floatToIntBits(other.primitiveValue))
				return false;
			return true;
		}
		
	}
	
	public static class ExtendingClass extends PrimitivesTestClass {
		String extendString;
		float extendFloat;
		public String getExtendString() {
			return extendString;
		}
		public void setExtendString(String extendString) {
			this.extendString = extendString;
		}
		public float getExtendFloat() {
			return extendFloat;
		}
		public void setExtendFloat(float extendFloat) {
			this.extendFloat = extendFloat;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Float.floatToIntBits(extendFloat);
			result = prime * result + ((extendString == null) ? 0 : extendString.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			ExtendingClass other = (ExtendingClass) obj;
			if (Float.floatToIntBits(extendFloat) != Float.floatToIntBits(other.extendFloat))
				return false;
			if (extendString == null) {
				if (other.extendString != null)
					return false;
			} else if (!extendString.equals(other.extendString))
				return false;
			return true;
		}
	}

	
	public static class Box<T> {

	    private T myThing;
	    
		public T getMyThing() {
			return myThing;
		}
		public void setMyThing(T myThing) {
			this.myThing = myThing;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((myThing == null) ? 0 : myThing.hashCode());
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
			Box<?> other = (Box<?>) obj;
			if (myThing == null) {
				if (other.myThing != null)
					return false;
			} else if (!myThing.equals(other.myThing))
				return false;
			return true;
		}
	} 
	
}
