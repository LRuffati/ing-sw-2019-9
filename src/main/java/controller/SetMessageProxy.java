package controller;

import controller.controllermessage.ControllerMessage;

public class SetMessageProxy {
    /**
     * The slave connected to this object
     */
    public final SlaveController slave;

    /**
     * The message in {@link #slave} at creation
     */
    private ControllerMessage originalMessage;

    private boolean flag;

    SetMessageProxy(SlaveController slave){
        this.slave=slave;
        flag = false;
    }

    void setMessage(ControllerMessage message){
        originalMessage = message;
    }

    /**
     * This function will check that {@link SlaveController#getCurrentMessage()} hasn't changed
     * from {@link #originalMessage} if it did return false, else
     *
     * @param toSet the controller message that should be received after
     * @return True if the controller message was succesfully set, false if some other action
     * already changed the controller message
     */
    public synchronized boolean setControllerMessage(ControllerMessage toSet){
        if (slave.lockMessageSet.tryLock()) {
            if (!flag)
                return false;
            if (slave.getCurrentMessage() != originalMessage) { // I'm interested in the reference
                flag = false;
                return false;
            }
            flag = false;
            slave.setCurrentMessage(toSet);
            slave.lockMessageSet.unlock();
            return true;
        } else {
            flag = true;
            return false;
        }
    }
}
