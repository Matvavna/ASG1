#Author: Tiger Barras
#Makefile for cs455.overlay assignment

JC = javac
C = .class #put this is so I stop deleting .java files....

#Aliases for directory paths
NODEPATH = ./cs455/overlay/node/
TRANSPORTPATH = ./cs455/overlay/transport/
WIREFORMATSPATH = ./cs455/overlay/wireformats/

#Alias contains files in package cs455.overlay.node
NODE = Node.class MessageNode.class
#Alias contains files in package cs455.overlay.transport
TRANSPORT = ServerThread.class Sender.class RecieverThread.class ConnectionCache.class RegisterConnectionCache.class NodeConnectionCache.class
#Alias contains files in package cs455.overlay.exception
EXCEPTION = ConnectionCacheException.class
#package cs455.overlay.wireformats
WIREFORMATS = Event.class OverlayNodeSendsRegistration.class

default: all

all: $(WIREFORMATS) $(NODE) $(TRANSPORT) $(EXCEPTION)

#In alias NODE
Node.class:
	@echo "Compiling Node. . ."
	$(JC) -d . $(NODEPATH)Node.java
MessageNode.class:
	@echo "Compiling MessageNode. . . "
	$(JC) -d . $(NODEPATH)MessageNode.java

#In alias TRANSPORT
ServerThread.class:
	@echo "Compiling ServerThread. . ."
	$(JC) -d . $(TRANSPORTPATH)ServerThread.java
Sender.class:
	@echo "Compiling Sender. . ."
	$(JC) -d . $(TRANSPORTPATH)Sender.java
RecieverThread.class:
	@echo "Compiling RecieverThread. . ."
	$(JC) -d . $(TRANSPORTPATH)RecieverThread.java
ConnectionCache.class:
	@echo "Compiling ConnectionCache. . ."
	$(JC) -d . $(TRANSPORTPATH)ConnectionCache.java
RegisterConnectionCache.class:
	@echo "Compiling RegisterConnectionCache. . ."
	$(JC) -d . $(TRANSPORTPATH)RegisterConnectionCache.java
NodeConnectionCache.class:
	@echo "Compiling NodeConnectionCache. . ."
	$(JC) -d . $(TRANSPORTPATH)NodeConnectionCache.java

#In alias EXCEPTION
ConnectionCacheException.class:
	@echo "Compiling ConnectionClassException. . ."
	$(JC) -d . ./cs455/overlay/exception/ConnectionCacheException.java

#In alias WIREFORAMTS
Event.class:
	@echo "Compiling Event. . ."
	$(JC) -d . $(WIREFORMATSPATH)Event.java
OverlayNodeSendsRegistration.class:
	@echo "Compiling OverlayNodeSendsRegistration. . ."
	$(JC) -d . $(WIREFORMATSPATH)OverlayNodeSendsRegistration.java

#let's put all this in a tarball
#Aliases for .java files
JNODE = $(NODEPATH)Node.java $(NODEPATH)MessageNode.java
JTRANSPORT = $(TRANSPORTPATH)ServerThread.java $(TRANSPORTPATH)Sender.java $(TRANSPORTPATH)RecieverThread.java $(TRANSPORTPATH)ConnectionCache.java $(TRANSPORTPATH)RegisterConnectionCache.java $(TRANSPORTPATH)NodeConnectionCache.java
JEXCEPTION = ./cs455/overlay/exception/ConnectionCacheException.java
JWIREFORMATS = $(WIREFORMATSPATH)Event.java $(WIREFORMATSPATH)OverlayNodeSendsRegistration.java
package:
	tar -cvf Barras_William_ASG1.tar Makefile $(JNODE) $(JTRANSPORT) $(JEXCEPTION) $(JWIREFORMATS)

#cleans shit up
clean:
	$(RM) ./cs455/overlay/node/Node$(C)
	$(RM) ./cs455/overlay/node/MessageNode$(C)
	$(RM) ./cs455/overlay/transport/ServerThread$(C)
	$(RM) ./cs455/overlay/transport/Sender$(C)
	$(RM) ./cs455/overlay/transport/ConnectionCache$(C)
	$(RM) ./cs455/overlay/transport/RegisterConnectionCache$(C)
	$(RM) ./cs455/overlay/exception/ConnectionCacheException$(C)
	$(RM) ./cs455/overlay/exception/NodeCacheException$(C)
	$(RM) ./cs455/overlay/wireformats/Event$(C)
	$(RM) ./cs455/overlay/wireformats/OverlayNodeSendsRegistration$(C)
