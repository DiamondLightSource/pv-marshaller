package org.epics.pvmarshaller.marshaller.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContainerTests.class,
	InheritanceTests.class,
	MapTests.class,
	NestedObjectsTests.class,
	ParameterisedTests.class,
	PrimitivesTests.class,
	DeserialiseInheritanceTests.class,
	DeserialiseMapsTests.class,
	DeserialiseNestedObjectTests.class,
	DeserialisePrimitiveArraysTests.class,
	DeserialisePrimitivesTests.class,
	DeserialiseObjectArraysTests.class,
	CustomSerialiserTests.class,
	CustomDeserialiserTests.class,
	UnionTests.class,
	MiscellaneousTests.class,
	SelectFieldSerialisationTests.class,
	EndToEndTests.class
})
public class MarshallerSuite {

}
