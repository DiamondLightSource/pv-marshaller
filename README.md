# pv-marshaller

[![Build Status](https://travis-ci.org/DiamondLightSource/pv-marshaller.svg?branch=master)](https://travis-ci.org/DiamondLightSource/pv-marshaller)

pvMarshaller is a package that converts between Java Objects and pvData PVStructures

Serialisation is the process of converting a Java Object into a PVStructure

Deserialisation is the process of converting a PVStructure into a Java Object

The package also provides interfaces allowing developers to create their own serialisers and deserialisers to marshall specific classes or structures in a custom way.

Usage:

```java
MyClass myObject = new MyClass();

// ...

// Set members of class

// ...



PVMarshaller marshaller = new PVMarshaller();


// Serialisation

PVStrcture serialisedPVStructure = marshaller.toPVStructure(myObject);


// Deserialisation

MyClass deserialisedObject = marshaller.fromPVStructure(serialisedPVStructure, MyClass.class);
```

