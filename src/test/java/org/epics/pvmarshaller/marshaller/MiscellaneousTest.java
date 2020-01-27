package org.epics.pvmarshaller.marshaller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.DoubleArrayData;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVDataCreate;
import org.epics.pvdata.pv.PVDouble;
import org.epics.pvdata.pv.PVDoubleArray;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.PVUnion;
import org.epics.pvdata.pv.PVUnionArray;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.epics.pvdata.pv.Union;
import org.epics.pvmarshaller.marshaller.PVMarshaller;
import org.epics.pvmarshaller.marshaller.api.IPVStructureDeserialiser;
import org.epics.pvmarshaller.marshaller.api.IPVStructureSerialiser;
import org.epics.pvmarshaller.marshaller.deserialisers.Deserialiser;
import org.epics.pvmarshaller.marshaller.serialisers.Serialiser;
import org.junit.Test;

public class MiscellaneousTest {
			
	@Test
	public void testROIList() {
		FieldCreate fieldCreate = FieldFactory.getFieldCreate();

		PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();
		
		Structure CRstructure = fieldCreate.createFieldBuilder().
				add("radius", ScalarType.pvDouble).
				add("angle", ScalarType.pvDouble).
				addArray("point", ScalarType.pvDouble).
				setId("IROI").
				createStructure();
		
		Structure RRstructure = fieldCreate.createFieldBuilder().
				addArray("lengths", ScalarType.pvDouble).
				add("angle", ScalarType.pvDouble).
				addArray("point", ScalarType.pvDouble).
				setId("IROI").
				createStructure();
		
		Union union = fieldCreate.createVariantUnion();
		
		Structure CMstructure = fieldCreate.createFieldBuilder().
				addArray("regions", union).
				setId("CompoundModel").
				createStructure();
		
		PVStructure expectedPVStructure = pvDataCreate.createPVStructure(CMstructure);

		PVUnionArray pvRegionsUnionArrayValue = expectedPVStructure.getSubField(PVUnionArray.class, "regions");

		PVStructure crPVStructure = pvDataCreate.createPVStructure(CRstructure);
		PVDouble crrValue = crPVStructure.getSubField(PVDouble.class, "radius");
		crrValue.put(1);
		PVDouble craValue = crPVStructure.getSubField(PVDouble.class, "angle");
		craValue.put(2.5);
		PVDoubleArray crpValue = crPVStructure.getSubField(PVDoubleArray.class, "point");
		double[] crPoint = {1d, 2d};
		crpValue.put(0, 2, crPoint, 0);

		PVStructure rrPVStructure = pvDataCreate.createPVStructure(RRstructure);
		PVDoubleArray rrlValue = rrPVStructure.getSubField(PVDoubleArray.class, "lengths");
		double[] rrLengths = {12d, 34d};
		rrlValue.put(0, 2, rrLengths, 0);
		PVDouble rraValue = rrPVStructure.getSubField(PVDouble.class, "angle");
		rraValue.put(4.4d);
		PVDoubleArray rrpValue = rrPVStructure.getSubField(PVDoubleArray.class, "point");
		double[] rrPoint = {4d, 5d};
		rrpValue.put(0, 2, rrPoint, 0);
		
		PVUnion regionArray[] = new PVUnion[2];
		
		regionArray[0] = pvDataCreate.createPVUnion(union);
		regionArray[0].set(crPVStructure);
		
		regionArray[1] = pvDataCreate.createPVUnion(union);
		regionArray[1].set(rrPVStructure);
		
		pvRegionsUnionArrayValue.put(0, 2, regionArray, 0);
		
		System.out.println(CMstructure);
		System.out.println("-------");
		System.out.println(expectedPVStructure);
		System.out.println("=========");
		
		// Create expected object
		CompoundModel expectedObject = new CompoundModel();
		expectedObject.regions = new LinkedList<>();
		CircularROI cr1 = new CircularROI();
		cr1.angle = 2.5;
		cr1.radius = 1;
		cr1.point = new double[]{1d, 2d,};
		RectangularROI rr1 = new RectangularROI();
		rr1.angle = 4.4;
		rr1.lengths = new double[]{12, 34};
		rr1.point = new double[]{4, 5};
		expectedObject.regions.add(cr1);
		expectedObject.regions.add(rr1);
		
		PVMarshaller marshaller = new PVMarshaller();
		CompoundModel model = null;
		
		try {
			marshaller.registerDeserialiser("IROI", new IROIDeserialiser());
			model = marshaller.fromPVStructure(expectedPVStructure, CompoundModel.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail (e.getMessage());
		}
		
		assertEquals(expectedObject, model);
	}

	@Test
	public void testTestROIListEndToEnd() {
		CompoundModel model = new CompoundModel();
		model.regions = new LinkedList<>();
		CircularROI cr1 = new CircularROI();
		cr1.angle = 1;
		cr1.radius = 2;
		cr1.point = new double[]{3d, 4d,};
		CircularROI cr2 = new CircularROI();
		cr2.angle = 5;
		cr2.radius = 6;
		cr2.point = new double[]{7d, 8d,};
		RectangularROI rr1 = new RectangularROI();
		rr1.angle = 9;
		rr1.lengths = new double[]{10, 11};
		rr1.point = new double[]{12, 13};
		CircularROI cr3 = new CircularROI();
		cr3.angle = 14;
		cr3.radius = 15;
		cr3.point = new double[]{16d, 17d,};
		model.regions.add(cr1);
		model.regions.add(cr2);
		model.regions.add(rr1);
		model.regions.add(cr3);
		
		PVMarshaller marshaller = new PVMarshaller();

		try {
			marshaller.registerSerialiser(CompoundModel.class, new CompoundModelSerialiser());
			marshaller.registerSerialiser(IROI.class, new IROISerialiser());
			marshaller.registerDeserialiser("IROI", new IROIDeserialiser());
			
			PVStructure serialisedStructure = marshaller.toPVStructure(model);
			

			System.out.println(serialisedStructure);
			System.out.println("=========");
			
			CompoundModel deserialisedModel = marshaller.fromPVStructure(serialisedStructure, CompoundModel.class);
			
			assertEquals(model, deserialisedModel);
		} catch (Exception e) {
			e.printStackTrace();
			fail (e.getMessage());
		}
		
	}


	public static interface IROI {
		
	}
	
	public static class CircularROI implements IROI {
		double radius;
		double angle;
		double[] point;
		public double getRadius() {
			return radius;
		}
		public void setRadius(double radius) {
			this.radius = radius;
		}
		public double getAngle() {
			return angle;
		}
		public void setAngle(double angle) {
			this.angle = angle;
		}
		public double[] getPoint() {
			return point;
		}
		public void setPoint(double[] point) {
			this.point = point;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(angle);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + Arrays.hashCode(point);
			temp = Double.doubleToLongBits(radius);
			result = prime * result + (int) (temp ^ (temp >>> 32));
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
			CircularROI other = (CircularROI) obj;
			if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle))
				return false;
			if (!Arrays.equals(point, other.point))
				return false;
			if (Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius))
				return false;
			return true;
		}
		
	}
	
	public static class RectangularROI implements IROI {
		double[] lengths;
		double angle;
		double[] point;
		public double[] getLengths() {
			return lengths;
		}
		public void setLengths(double[] lengths) {
			this.lengths = lengths;
		}
		public double getAngle() {
			return angle;
		}
		public void setAngle(double angle) {
			this.angle = angle;
		}
		public double[] getPoint() {
			return point;
		}
		public void setPoint(double[] point) {
			this.point = point;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(angle);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + Arrays.hashCode(lengths);
			result = prime * result + Arrays.hashCode(point);
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
			RectangularROI other = (RectangularROI) obj;
			if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle))
				return false;
			if (!Arrays.equals(lengths, other.lengths))
				return false;
			if (!Arrays.equals(point, other.point))
				return false;
			return true;
		}
		
	}
	
	public static class CompoundModel {
		List<IROI> regions;

		public List<IROI> getRegions() {
			return regions;
		}

		public void setRegions(List<IROI> regions) {
			this.regions = regions;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((regions == null) ? 0 : regions.hashCode());
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
			CompoundModel other = (CompoundModel) obj;
			if (regions == null) {
				if (other.regions != null)
					return false;
			} else if (!regions.equals(other.regions))
				return false;
			return true;
		}
		
	}
	
	public static class IROISerialiser implements IPVStructureSerialiser<IROI> {

		@Override
		public Structure buildStructure(Serialiser serialiser, IROI source)
				throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

			FieldCreate fieldCreate = FieldFactory.getFieldCreate();
			
			if (source instanceof CircularROI) {
				Structure structure = fieldCreate.createFieldBuilder().
						add("radius", ScalarType.pvDouble).
						add("angle", ScalarType.pvDouble).
						addArray("point", ScalarType.pvDouble).
						setId("IROI").
						createStructure();
					return structure;
			} else if (source instanceof RectangularROI) {
				Structure structure = fieldCreate.createFieldBuilder().
						addArray("lengths", ScalarType.pvDouble).
						add("angle", ScalarType.pvDouble).
						addArray("point", ScalarType.pvDouble).
						setId("IROI").
						createStructure();
					return structure;
			} else {
				System.out.println("Unknown IROI type");
			}
			return null;
		}

		@Override
		public void populatePVStructure(Serialiser serialiser, IROI source, PVStructure pvStructure)
				throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			if (source instanceof CircularROI) {
				CircularROI cr = (CircularROI)source;
				PVDouble radius = pvStructure.getSubField(PVDouble.class, "radius");
				radius.put(cr.getRadius());	
				PVDouble angle = pvStructure.getSubField(PVDouble.class, "angle");
				angle.put(cr.getAngle());		
				PVDoubleArray point = pvStructure.getSubField(PVDoubleArray.class, "point");
				point.put(0, cr.getPoint().length, cr.getPoint(), 0);
			} else if (source instanceof RectangularROI) {
				RectangularROI rr = (RectangularROI)source;
				PVDoubleArray lengths = pvStructure.getSubField(PVDoubleArray.class, "lengths");
				lengths.put(0, rr.getLengths().length, rr.getLengths(), 0);
				PVDouble angle = pvStructure.getSubField(PVDouble.class, "angle");
				angle.put(rr.getAngle());		
				PVDoubleArray point = pvStructure.getSubField(PVDoubleArray.class, "point");
				point.put(0, rr.getPoint().length, rr.getPoint(), 0);
			} else {
				System.out.println("Unknown IROI type");
			}
			
		}
		
	}
	
	public static class IROIDeserialiser implements IPVStructureDeserialiser {

		@Override
		public Object fromPVStructure(Deserialiser deserialiser, PVStructure pvStructure) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException {
			PVDouble radiusField = pvStructure.getDoubleField("radius");
			if (radiusField != null) {
				
				CircularROI circRoi = new CircularROI();
				circRoi.angle = pvStructure.getDoubleField("angle").get();
				circRoi.radius = pvStructure.getDoubleField("radius").get();
				PVScalarArray pvScallarArray = pvStructure.getScalarArrayField("point", ScalarType.pvDouble);
				PVDoubleArray doubleArray = (PVDoubleArray)pvScallarArray;
				DoubleArrayData doubleArrayData = new DoubleArrayData();
				doubleArray.get(0, doubleArray.getLength(), doubleArrayData);
				circRoi.point = doubleArrayData.data;
				return circRoi;
			} else {
				RectangularROI rectRoi = new RectangularROI();
				PVScalarArray lpvScallarArray = pvStructure.getScalarArrayField("lengths", ScalarType.pvDouble);
				PVDoubleArray ldoubleArray = (PVDoubleArray)lpvScallarArray;
				DoubleArrayData lDdoubleArrayData = new DoubleArrayData();
				ldoubleArray.get(0, ldoubleArray.getLength(), lDdoubleArrayData);
				rectRoi.lengths = lDdoubleArrayData.data;
				rectRoi.angle = pvStructure.getDoubleField("angle").get();
				PVScalarArray pvScallarArray = pvStructure.getScalarArrayField("point", ScalarType.pvDouble);
				PVDoubleArray doubleArray = (PVDoubleArray)pvScallarArray;
				DoubleArrayData doubleArrayData = new DoubleArrayData();
				doubleArray.get(0, doubleArray.getLength(), doubleArrayData);
				rectRoi.point = doubleArrayData.data;
				return rectRoi;
			}
		}
	}
	
	public static class CompoundModelSerialiser implements IPVStructureSerialiser<CompoundModel> {

		@Override
		public Structure buildStructure(Serialiser serialiser, CompoundModel source)
				throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			FieldCreate fieldCreate = FieldFactory.getFieldCreate();
			
			Union union = fieldCreate.createVariantUnion();
			
			Structure CMstructure = fieldCreate.createFieldBuilder().
					addArray("regions", union).
					setId("CompoundModel").
					createStructure();
			return CMstructure;
		}

		@Override
		public void populatePVStructure(Serialiser serialiser, CompoundModel source, PVStructure pvStructure) {

			FieldCreate fieldCreate = FieldFactory.getFieldCreate();
			PVDataCreate pvDataCreate = PVDataFactory.getPVDataCreate();

			Union union = fieldCreate.createVariantUnion();
			//PVStructure expectedPVStructure = pvDataCreate.createPVStructure(pvStructure);

			PVUnionArray pvRegionsUnionArrayValue = pvStructure.getSubField(PVUnionArray.class, "regions");
			
			

			PVUnion regionArray[] = new PVUnion[source.regions.size()];
			
			for (int i = 0; i < source.regions.size(); i++) {

				try {
					regionArray[i] = pvDataCreate.createPVUnion(union);
					PVStructure crPVStructure = serialiser.toPVStructure(source.regions.get(i));
					regionArray[i].set(crPVStructure);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			pvRegionsUnionArrayValue.put(0, regionArray.length, regionArray, 0);
		}
		
	}
	
}

class TestClass1
{
	int hmm;
	String blah;
}