JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class: 
	$(JC) $(JFLAGS) $*.java

CLASSES = / Node.java / RedBlackTree.java / MinHeap.java / risingCity.java

default: classes

classes: $(CLASSES:.java=.class)

clean: 
	$(RM) *.class