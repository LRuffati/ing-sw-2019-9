# Objects interactions

## PowerUp use
+ Start point: SlaveController.setPowUps
    + Arguments:
        1. The effects prior to this
        2. The list of ActionBundles following
        3. (Maybe) the action to run when no action bundles available (reload)
+ Side effects:
    + All powerups selected must be discarded afterwards
    + If I use the sight I must pay an additional cube or powerup
+ Finalizer:
    + If no powerup has been selected then the action called should be either the first 
    ActionBundle or the Reload if no ActionBundle is present. If I create an action bundle then 
    the finalizer of actionBundle should call setPowUps providing a list of effects and the tail 
    of the previous list of ActionBundle
    + If a powerup has been selected I should call the MainController to apply the effect. The 
    finalizer passed to the MainController should call setPowUps with the original list of 
    effects and ActionBundle 
    + Each powerup must provide a apply function to call if it is chosen, it will return a 
    controllerMessage (the result of .pick({n}) on the Original controllerMessage) and accept the
     finalizer (information on what to do after effects have been merged in main)
+ Notes: As in actionBundle once I have called MainController I am not allowed to call it again.  
Threads already implement this since **they can only be started once**
## ActionBundle
+ Start Point: SlaveController.setNextAction called by a PowerUp finalizer
+ Side Effects:  
When it resolves the effects 
    + Grab will cause:
        1. A change in the ammo/powerup available
        2. A new weapon being available and possibly an old one being discarded
    + Pay:
        1. A change in the ammo and/or
        2. Discarding some of the powerups  
        Split payEffect in paywithammo and paywithpowup
+ Finalizer: The finalizer is twofold:
    1. A function executing checks
    2. A thread provided by the creator which will apply the effects to the main as well as tell 
    the Main to call setPowUps with the list of effects

## Action
+ Start point: ActionBundle.pickAction() o WeaponUse.pickAction()
+ Side effects: None, it operates exclusively on a sandbox
+ Finalizer: provided by the parent, simply called

## WeaponUse
+ Start point: Spawned by Fire when templates are being converted in Action 
+ Side effects: None in the standard use since all actions only modify the effects of an action
+ Finalizer: same as any other effect resolution in Action

## Reload
+ Start point: effect called by an action, uses RollBack message so embedd in a sandbox
+ Side effect: When resolved 
