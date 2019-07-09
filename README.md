# pv-marshaller

pvMarshaller is a package that converts between Java Objects and pvData PVStructures

Serialisation is the process of converting a Java Object into a PVStructure

Deserialisation si the process of converting a PVStructure into a Java Object

The package also provides interfaces allowing developers to create their own serialisers and deserialisers to marshall specific classes or structures in a custom way.

Usage:

MyClass myObject = new MyClass();

// ...

// Set members of class

// ...



PVMarshaller marshaller = new PVMarshaller();


// Serialisation

PVStrcture serialisedPVStructure = marshaller.toPVStructure(myObject);


// Deserialisation

MyClass deserialisedObject = marshaller.fromPVStructure(serialisedPVStructure, MyClass.class);
