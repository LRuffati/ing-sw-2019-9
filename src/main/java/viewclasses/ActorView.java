package viewclasses;


import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ActorView implements Serializable {
    private Color color;
    private String name;
    private List<ActorView> damageTaken;
    private Map<ActorView, Integer> marks;

    public ActorView(){
        color = new Color(0,0,0);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDamageTaken(List<ActorView> damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void setMarks(Map<ActorView, Integer> marks) {
        this.marks = marks;
    }


    public Color color() {
        return color;
    }

    public String name() {
        return name;
    }

    public List<ActorView> damageTaken() {
        return damageTaken;
    }

    public Map<ActorView, Integer> marks() {
        return marks;
    }
}
