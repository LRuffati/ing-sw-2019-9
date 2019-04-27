package grabbables;

import uid.GrabbableUID;

/**
 * Generic item that can be picked Up. Identified by a UID.
 */
public abstract class Grabbable {
    private GrabbableUID uid;

    public Grabbable(){
        uid = new GrabbableUID();
    }

    public GrabbableUID getId(){
        return uid;
    }

    @Override
    public boolean equals(Object obj){
        if(obj ==  null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        return uid.equals( ((Grabbable)obj).getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
