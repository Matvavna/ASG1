#Author: Tiger Barras
#Makefile for cs455.overlay assignment

JC = javac
C = .class #put this is so I stop deleting .java files....

#Alias contains files in package cs455.overlay.node
NODE = Node.class
#Alias contains files in package cs455.overlay.transport
TRANSPORT = ServerThread.class Sender.class RecieverThread.class ConnectionCache.class RegisterConnectionCache.class NodeConnectionCache.class
#Alias contains files in package cs455.overlay.exception
EXCEPTION = ConnectionCacheException.class
#package cs455.overlay.wireformats
WIREFORMATS = Event.class OverlayNodeSendsRegistration.class

default: all

all: $(NODE) $(TRANSPORT) $(EXCEPTION) $(WIREFORMATS)

#In alias NODE
Node.class:
	@echo "Compiling Node. . ."
	$(JC) -d . ./cs455/overlay/node/Node.java

#In alias TRANSPORT
ServerThread.class:
	@echo "Compiling ServerThread. . ."
	$(JC) -d . ./cs455/overlay/transport/ServerThread.java
Sender.class:
	@echo "Compiling Sender. . ."
	$(JC) -d . ./cs455/overlay/transport/Sender.java
RecieverThread.class:
	@echo "Compiling RecieverThread. . ."
	$(JC) -d . ./cs455/overlay/transport/RecieverThread.java
ConnectionCache.class:
	@echo "Compiling ConnectionCache. . ."
	$(JC) -d . ./cs455/overlay/transport/ConnectionCache.java
RegisterConnectionCache.class:
	@echo "Compiling RegisterConnectionCache. . ."
	$(JC) -d . ./cs455/overlay/transport/RegisterConnectionCache.java
NodeConnectionCache.class:
	@echo "Compiling NodeConnectionCache. . ."
	$(JC) -d . ./cs455/overlay/transport/NodeConnectionCache.java

#In alias EXCEPTION
ConnectionCacheException.class:
	@echo "Compiling ConnectionClassException. . ."
	$(JC) -d . ./cs455/overlay/exception/ConnectionCacheException.java

#In alias WIREFORAMTS
Event.class:
	@echo "Compiling Event. . ."
	$(JC) -d . ./cs455/overlay/wireformats/Event.java
OverlayNodeSendsRegistration.class:
	@echo "Compiling OverlayNodeSendsRegistration. . ."
	$(JC) -d . ./cs455/overlay/wireformats/OverlayNodeSendsRegistration.java


#cleans shit up
clean:
	$(RM) ./cs455/overlay/node/Node$(C)
	$(RM) ./cs455/overlay/transport/ServerThread$(C)
	$(RM) ./cs455/overlay/transport/Sender$(C)
	$(RM) ./cs455/overlay/transport/ConnectionCache$(C)
	$(RM) ./cs455/overlay/transport/RegisterConnectionCache$(C)
	$(RM) ./cs455/overlay/exception/ConnectionCacheException$(C)
	$(RM) ./cs455/overlay/exception/NodeCacheException$(C)
	$(RM) ./cs455/overlay/wireformats/Event$(C)
	$(RM) ./cs455/overlay.wireformats/OverlayNodeSendsRegistration$(C)
