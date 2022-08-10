
# make file for the assignemnet 1 code
# 26 February 2022

.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
SCRIPTS=scripts
JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
PYTHON=/usr/bin/python

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES2=MeanFilterParallel.class MeanFilterSerial.class MedianFilterParallel.class MedianFilterSerial.class test.class


CLASSES=$(CLASSES2:%.class=$(BINDIR)/%.class)

default: $(CLASSES)

run-medianSerial: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MedianFilterSerial $(ARGS)  
	
run-medianParallel: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MedianFilterParallel $(ARGS)  
    
run-meanSerial: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MeanFilterSerial $(ARGS)  

run-meanParallel: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MeanFilterParallel $(ARGS)  

run-test: $(CLASSES)
	$(JAVA) -cp $(BINDIR) test  
	
clean:
	$(RM) $(BINDIR)/*.class
	