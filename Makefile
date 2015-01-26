#Author: Tiger Barras
#Makefile for cs455.overlay assignment

JC = javac

#Alias contains files in package cs455.overlay.node
NODE = Node.class

default: all

all: $(NODE)

#In alias NODE
Node.class:
	@echo "Compiling SocketClient..."
	$(JC) -d . ./cs455/overlay/node/Node.java

#cleans shit up
clean:
	rm ./cs455/overlay/node/Node.class
