#Author: Tiger Barras
#Makefile for cs455.overlay assignment

JC = javac

#Alias contains files in package cs455.overlay.node
NODE = Node.class
#Alias contains files in package cs455.overlay.transport
TRANSPORT = ServerThread.class ConnectionCache.class RegisterConnectionCache.class

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
ConnectionCache.class:
	@echo "Compiling ConnectionCache. . ."
	$(JC) -d . ./cs455/overlay/transport/ConnectionCache.java
RegisterConnectionCache.class:
	@echo "Compiling RegisterConnectionCache. . ."
	$(JC) -d . ./cs455/overlay/transport/RegisterConnectionCache.java


#cleans shit up
clean:
	$(RM) ./cs455/overlay/node/Node.class
	$(RM) ./cs455/overlay/transport/ServerThread.class
	$(RM) ./cs455/overlay/transport/ConnectionCache.class
	$(RM) ./cs455/overlay/transport/RegisterConnectionCache.class
