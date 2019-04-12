package uid;

import java.rmi.server.UID;

public abstract class ObjectUID {
    private UID uid;

    public ObjectUID(){
        this.uid = new UID();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        return uid.equals( ((ObjectUID)obj).getUID());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private java.rmi.server.UID getUID(){
        return uid;
    }
}
