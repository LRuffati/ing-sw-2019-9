# Controller

## Roles involved
The elements of the controller subsystem are as follows:

+ Main controller: Is a singleton for the game and private lobby (if we were to implement the 
multiple games requirement this would manage the individual lobby but not the overall lobby with 
all players in queue with the server)
+ Slave controller: Is spawned by the Main controller to handle interactions with an individual 
user, after the pairing is performed all valid interactions between game client and server must 
be mediated by the same slave controller instance.
+ Client controller: manages the interaction with the user
+ Network (Clientside and serverside): transfers information between client and server. The 
server side is particularly relevant because it stores references to all objects that the client 
must or might wish to access directly, it therefore acts as a proxy

## Data structures:
+ Effect: interface which provides the information on what changed and how on the gamemap
+ ControllerMessage: is the interaction mediator between parts of the controller. It provides 3 
basics informations:  
    1. The state of the User
    2. Possible choices (null if no choice isn't needed)
    3. A method to perform a choice amongst the options
+ NetworkDatabase: Maps an id to the elements that remain serverside and allows to perform 
operations on them

## Concurrecy requirements
+ Main must be the only class able to effect a change on the model
+ Slaves and Network interfaces share no object outside of the read only model
+ No two Players may be in a state other than Wait at the same time, the network should check 
that an action isn't being performed by the client while the connected slave is in a wait state
+ When a slave controller receives the Wait instruction it should initiate the reset of the info 
stored by the network interface to limit the access to concurrency breaking objects

## Bootstrapping assumptions
+ Prior to the initialization of the game a parallel system is used for status updates. This 
system remains in place for subsequent updates, such as players disconnecting
+ When the game begins a special signal is sent by the Main to the slaves who, upon request, 
relay it to the clients. This signal tells the client to start polling for an available actions
+ Client and Slave are set to Wait for the moment, and issued with a pristine version of the gameMap

# Communications

### Slave <-> Network
Slave to network:

+ When the **nextInstruction** is of type Wait reset the classes stored by the network other than
 the current Sandbox/GameMap
 
Network to slave:

+ To retrieve the next instruction
+ To get the latest sandbox/gameMap
+ 

### Other classes <-> Network

Other classes are classes like ActionBundle etc as well as ControllerMessages

Classes to Network:

Network to classes:
+ For ControllerMessages call pick or serialize them

### Network <-> Main

Network to main:
+ On connection to the server
+ 

Main to network: none

### ControllerMessage -> Main

+ When an actionbundle terminates it sends the effect list to the Main

### ControllerMessage -> Slave

+ Sets the Slave to Wait when starting interaction with Main
+ Restores the previous state on failure by main interaction

### Slave <-> Master
 Slave to master:
 + [network status updates]
 + [timer events]
 
 Master to slave:
 + Update next action
 + 

# Problems

## Serialization
It is enough to save the state of the SlaveController, however how do I serialize runnables?

## SlaveController

1. Main signals the slave to start the main turn, however after the first actionbundle or effect 
slave will be put in a sleeping state and main will take over. How to restore the status to the 
previous point?
    1. 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    



