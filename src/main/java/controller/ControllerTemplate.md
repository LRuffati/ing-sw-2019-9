is it my turn?
> no

is it my turn? 
> <PICKPOWERUP, powupPicker: [Raggio, Tele]>

```
powupPicker funziona come un ActionBundle,
ha un lock integrato 
```

powupPicker.pick(0) // -1 se non voglio niente
> «PICKTARGET, choiceMaker:[...]»

choiceMaker.pick(n)
> «PICKTARGET, choice...»

choiceMaker.pick(n)
> «TERMINATED, powUp»

merge (powUp) // prende le azioni e le applica
> <PICKPOWERUP, powupPicker: [Tele]>

powupPicker.pick(-1)
> «TERMINATED, null»

merge (null)
> «PICKACTION, actionbundle»

....

> «TERMINATED, actionbundle»

merge (actionbundle)
> <GRABCHOICE, "Arma da prendere",grabchooser:[weap1, weap2, weap3]>

grabchooser.pick(1)
> <GRABCHOICE, "Arma da lasciare", grabch...>

grabchooser.pick(2)
> <PAY, "Paga", powupColPick:[(powup1, R), (powup2, R), (powup3, B)]

powupColPick.pick([0,2]) // Non ho abbastanza rossi
> <PAY, ...>

powupPay([0,1,2]) // Finiti gli effetti
> <PICKPOWERUP, powupPicker: [Mirino]>

...
> «TERMINATED, mirino»

merge(mirino)
> <PICKPOWUP, powupPicker:[Tele]>

... // Powerup

... // Azione

> <RELOAD, reloader>

`reloader.yes() -> come un'azione`

reloader.no()
> <FINETURNO, null>

isitmyturn?
> no

isitmyturn?
> <TAKEBACK, takebackUse>

takebackUse.target()
> Target

takebackUse.use()
takebackUse.no()
> ACK

isitmyturn?
> <KILLED, ...>

canirespawn?
> NO

canirespawn?
> <CanRespawn, respawner>

respawner.options()
> <PICKLOCATION, powupColPick:[(Raggio, R),(Venom, R), (Tele, Y), (Mirino, B)]

powupColPick.pick(2)
> ACK
