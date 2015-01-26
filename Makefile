#Author: Tiger Barras
#Makefile for cs455.overlay assignment

JC = javac

#Alias contains files in package cs455.overlay.node
NODE = Node.class
#Alias contains files in package cs455.overlay.transport
TRANSPORT = ServerThread.class

default: all

all: $(NODE) $(TRANSPORT)

#In alias NODE
Node.class:
	@echo "Compiling Node. . ."
	$(JC) -d . ./cs455/overlay/node/Node.java
#In alias TRANSPORT
ServerThread.class:
	@echo "Compiling ServerThread. . ."
	$(JC) -d . ./cs455/overlay/transport/ServerThread.java


#cleans shit up
clean:
	$(RM) ./cs455/overlay/node/Node.class
	$(RM) ./cs455/overlay/transport/ServerThread.class
