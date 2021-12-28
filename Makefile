# This is the Makefile to use for Homework Assignment #3
# To use, at the prompt, type:
#
# 	make driver		# This will make executable driver
# or
#       make directories        # makes correct directories
#       make install            # copies files to correct place
#       make clean              # cleans up directory
#       make public            # installs assignment into public area

.SUFFIXES: .java .class
.java.class:
	javac -g $<

all:	Main

Main:	Main.class MyLib.class LongStack.class Tracker.class Calc.class Size.class
	echo 'java Main $$*' > Calc
	chmod ug+rx Calc

Size.class: Size.java
Calc.class: Calc.java
Main.class: Main.java
MyLib.class: MyLib.java
LongStack.class: LongStack.java
Tracker.class: Tracker.java

clean:
	rm -f core *.class

new:
	make clean
	make

backup:	*.[ch] Makefile
	cp *.[ch] Makefile $(HOME)/hw3/backup
