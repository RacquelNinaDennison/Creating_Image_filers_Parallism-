
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

CLASSES2=MeanFilterParallel.class MeanFilterSerial.class MedianFilterParallel.class MedianFilterSerial.class


CLASSES=$(CLASSES2:%.class=$(BINDIR)/%.class)

default: $(CLASSES)

run-medianSerial: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MedianFilterSerial 
	
run-medianParallel: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MedianFilterParallel 
    
run-meanSerial: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MeanFilterSerial  

run-meanFilterParallel: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MeanFilterParallel 
	
clean:
	$(RM) $(BINDIR)/*.class
	