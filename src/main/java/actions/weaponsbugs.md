##Fucile al plasma
+ prima slittamento di fase, poi effetto base:
+ anche solo con effetto base
    + Exception in thread "Thread-1" java.lang.IllegalStateException: The selector requires an unavailable
    target
           at actions.targeters.Targeter.giveChoices(Targeter.java:144)
           at actions.Action.iterate(Action.java:211)
           at actions.WeaponUse.pickAction(WeaponUse.java:96)
           at controller.controllermessage.PickActionMessage.pick(PickActionMessage.java:88)
           at network.ObjectMap.pick(ObjectMap.java:108)
           at network.rmi.server.ServerNetworkRMI.pick(ServerNetworkRMI.java:97)

##fucile laser
+ effetto base: mi ha fatto scegliere la cella e non la direzione, prima mi ha deto che non ci sono bersagli validi, poi l'ho rifatto, ha eseguito lo sparo ma non ha danneggiato il bersaglio nella direzione

##Lanciafiamme
+ propone solo modalità base e non barbecue, mi fa scegliere tra delle tile ed entro in un loop (Pietro)
+ lanciafiamme, solo modalità base come Pietro ma mi fa scegliere due tile e non entro in nessun loop (Carmelo)

##Mitragliatrice:
+ Modalità base, bersaglio nella tua stessa tile (down 0,left 2):
    + java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
at java.base/java.util.Objects.checkIndex(Objects.java:372)
at java.base/java.util.ArrayList.get(ArrayList.java:458)
at actions.Action.lambda$giveChoiceMaker$3(Action.java:107)
at actions.Action$1.pick(Action.java:159)
at controller.controllermessage.PickTargetMessage.pick(PickTargetMessage.java:96)

##Cannone Vortex
+ Modalità base => funzionante correttamente
+ Buco nero, bersaglio nella tua stessa tile (down 1, left 0):
    + java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
	at java.base/java.util.Objects.checkIndex(Objects.java:372)
	at java.base/java.util.ArrayList.get(ArrayList.java:458)
	at actions.Action.lambda$giveChoiceMaker$3(Action.java:107)
	at actions.Action$1.pick(Action.java:159)
	at controller.controllermessage.PickTargetMessage.pick(PickTargetMessage.java:96)

##Falce Protonica:
+ Modalità base => nessun errore evidente
+ Modalità mietitore => mi obbliga ad annidare anche la modalità base, poi esegue entrambe. Non posso eseguire solo 
mietitore

##Raggio Traente:
+ Modalità base => nessun errore evidente (danno + spostamento)

##Lanciagranate:
+ Modalità base => funzionante correttamente
+ Granata extra => funzionante correttamente

##Torpedine:
+ Modalità base => funzionante correttamente

##Granata Venom
+ Funzionante
