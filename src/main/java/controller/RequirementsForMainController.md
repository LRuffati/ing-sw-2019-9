# Required

## Tile
+ `bool endTurn(Actor)`: called at the end of Actor's turn, refills empty slots and if it's a spawn 
point adds the control of the spawn point if the Actor's pawn is on the tile (also damages him).  
Returns true if any 

c'è il metodo refill su Gamemap. Per il resto ancora niente

## Actor
+ `void damage(Actor, int, Runnable)` DONE 

+ `void damageRaw(Actor, int, Runnable)`
DONE  
Adds the given amount of damage by the given player. damageRaw is to be called by PowerUps and 
doesn't trigger the conversion of marks into damages  
After all operations run Runnable to resume the effect resolution
+ `bool endTurn(Actor,Scoreboard)`  
If the player has been killed update the Scoreboard and reset the board. If the game is in final 
frenzy mode the reset switches the board. Return true if player needs to respawn
+ `void drawPowerupRaw(int)` DONE  
Picks a powerup even if the amount causes it to go above the limit
+ `void beginFF(boolean)`  
If the damage track is empty switch to FF actions (decide which based on boolean, if true the 
actor is before the first player). If already damaged wait for the next endTurn triggering a reset
+ `void setLastInFrenzy()` DONE
+ `boolean isLastInFrenzy()` DONE  
Defaults to false  
True if this is the actor of the player who started the final frenzy (and is the last to play a 
turn)
+ `void discardPowerup(Powerup)` DONE  
Discards the powerup back into the deck
+ `String name()`: the name of the player linked to this actor

actor.pawn().getUsername() per ora. se si vuole aggiungere anche all'actor boh ok


## Domination point extends Actor
+ 

## Scoreboard

## GameMap

## Powerup
+ `TileUID spawnLocation(Set<Tile>)`  DONE
