# txtUML
Textual, eXecutable, Translatable UML

See the documentation and installation instructions on http://txtuml.inf.elte.hu



The name txtUML stands for textual, executable and translatable UML. It is an open source project with the goal to make model driven development easier.

## Textual
Experience shows that graphical diagrams are valuable for understanding software. On the other hand, writing software in text is usually far more efficient than drawing diagrams in a modeling IDE. The txtUML project aims at combining the advantages of these two approaches: creating models textually and still have possibility to visualize it on graphical diagrams.

txtUML is a Java library that provides the constructs of executable UML modeling. A model is a Java program that uses this library. Models can be edited, searched, version controlled, refactored as any other Java program, and there is no need to learn new syntax.

## Executable
UML is mostly used to informally specify the architecture of software, but it is capable to do much more. Executable models specify all aspects of software from components and classes to state machines and activities. They can be executed, debugged and tested independently of the target platform where the software will finally work.

Since txtUML models are Java programs, it is possible to run, debug and test them using the standard Java runtime environments, debuggers and test frameworks.

## Translatable
Model compilers translate the abstract models to programs that are written in an implementation language (like C, C++, Java etc.) and are designed to work in a specific runtime environment (hardware, operating system, specific libraries). Our project includes a generic C++ model compiler that can be used as is, or as a basis to create a new compilers for different languages and special platforms.
