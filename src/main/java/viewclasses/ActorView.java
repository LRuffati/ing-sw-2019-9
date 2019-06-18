package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import uid.DamageableUID;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class contains the Actor that is used by the view and transmitted from the server to the client
 */
public class ActorView implements Serializable {
    private Color color;
    private String username;

    private DamageableUID uid;
    private TileView position;
    private int numOfDeaths;
    private int score;
    private boolean firstPlayer;

    private Map<AmmoColor, Integer> ammo;

    private List<ActorView> damageTaken;
    private Map<ActorView, Integer> marks;

    private List<WeaponView> loadedWeapon;
    private List<WeaponView> unloadedWeapon;
    private List<PowerUpView> powerUp;
    private static final int HP = 5;

    public int getHP(){
        return HP;
    }

    public List<WeaponView> getLoadedWeapon() {
        return loadedWeapon;
    }

    public List<WeaponView> getUnloadedWeapon() {
        return unloadedWeapon;
    }

    public List<PowerUpView> getPowerUp() {
        return powerUp;
    }

    public ActorView(){
        loadedWeapon = new ArrayList<>();
    }

    public ActorView(DamageableUID uid){
        this.uid = uid;
    }


    public void setColor(Color color) {
        this.color = color;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void setPosition(TileView position) {
        this.position = position;
    }

    public void setDamageTaken(List<ActorView> damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void setMarks(Map<ActorView, Integer> marks) {
        this.marks = marks;
    }

    public void setLoadedWeapon(List<WeaponView> loadedWeapon) {
        this.loadedWeapon = loadedWeapon;
    }

    public void setUnloadedWeapon(List<WeaponView> unloadedWeapon) {
        this.unloadedWeapon = unloadedWeapon;
    }

    public void setPowerUp(List<PowerUpView> powerUp) {
        this.powerUp = powerUp;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNumOfDeaths(int numOfDeaths) {
        this.numOfDeaths = numOfDeaths;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setAmmo(AmmoAmount ammo) {
        this.ammo = ammo.getAmounts();
    }



    public Color color() {
        return color;
    }

    public String name() {
        return username;
    }

    public TileView position() {
        return position;
    }

    public List<ActorView> damageTaken() {
        return damageTaken;
    }

    public Map<ActorView, Integer> marks() {
        return marks;
    }

    public int numOfDeaths() {
        return numOfDeaths;
    }

    public boolean firstPlayer() {
        return firstPlayer;
    }

    public int score() {
        return score;
    }

    public DamageableUID uid() {
        return uid;
    }

    public Map<AmmoColor, Integer> ammo() {
        return ammo;
    }

    public List<WeaponView> loadedWeapon() {
        return loadedWeapon;
    }

    public List<WeaponView> unloadedWeapon() {
        return unloadedWeapon;
    }

    public List<PowerUpView> powerUp() {
        return powerUp;
    }

    public String getAnsi(){
        if(color == Color.white) return "\u001B[37m";
        if(color == Color.magenta) return "\u001B[35m";
        if(color == Color.green) return "\u001B[32m";
        if(color == Color.yellow) return "\u001B[33m";
        if(color == Color.blue) return "\u001B[34m";
        return "\u001B[0m";
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        return uid.equals(((ActorView) obj).uid());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
