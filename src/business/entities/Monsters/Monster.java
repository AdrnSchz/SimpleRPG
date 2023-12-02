package business.entities.Monsters;

import business.entities.Characters.CharacterGame;

import java.util.List;
import java.util.Random;

/**
 * Class to represent a monster entity.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class Monster {
    // a string containing the name of the monster
    private final String name;
    // a string containing the level of difficulty of the monster.
    private final String challenge;
    // an integer containing the experience points this monster awards once defeated.
    private final int experience;
    // an integer containing the health the monster has.
    private int hitPoints;
    // an integer containing the value of speed the monster has.
    private int initiative;
    // a string containing the dice that has to be thrown to calculate its dmg(Ex: "d12").
    private final String damageDice;
    // a string containing the type of damage the monster deals.
    private final String damageType;


    /**
     * Constructor with the parameters to create a monster.
     * @param name a string containing the name of the monster.
     * @param challenge a string containing the level of difficulty of the monster.
     * @param experience an integer containing the experience points this monster awards once defeated.
     * @param hitPoints an integer containing the health the monster has.
     * @param initiative an integer containing the value of speed the monster has.
     * @param damageDice a string containing the dice that has to be thrown to calculate its dmg(Ex: "d12").
     * @param damageType a string containing the type of damage the monster deals.
     */
    public Monster(String name, String challenge, int experience, int hitPoints, int initiative, String damageDice,
                   String damageType) {
        this.name = name;
        this.challenge = challenge;
        this.experience = experience;
        this.hitPoints = hitPoints;
        this.initiative = initiative;
        this.damageDice = damageDice;
        this.damageType = damageType;
    }

    /**
     * Method to get the name of the monster.
     * @return string containing the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the level of difficulty the monster has.
     * @return string containing the level of difficulty.
     */
    public String getChallenge() {
        return challenge;
    }

    /**
     * Method to get the number of experience points the monster gives.
     * @return integer containing the number of experience points.
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Method to get the health the monster has.
     * @return integer containing the number of hit points.
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Method to add to the health the monster has the given value.
     * @param hitPoints integer containing the number of hit points that are going to be added.
     */
    public void addHitPoints(int hitPoints) {
        this.hitPoints += hitPoints;
    }
    /**
     * Method to get the speed value of the monster.
     * @return integer containing the speed value.
     */
    public int getInitiative() {
        return initiative;
    }

    /**
     * Method to calculate the initiative value of the monster
     */
    public void setInitiative() {this.initiative += (new Random().nextInt(12) + 1);}

    /**
     * Method to get the dice that has to be thrown to calculate the damage.
     * @return string containing the dice.
     */
    public String getDamageDice() {
        return damageDice;
    }

    /**
     * Method to get the target the monster is going to attack.
     * @param num integer containing the number of characters in the party.
     * @return integer containing the index of the character that is going to be attacked.
     */
    public int getTarget(int num) {
        Random random1 = new Random();

        return random1.nextInt(num);
    }


    /**
     * Method to get the damage type this monster deals.
     * @return a string containing the dmg type.
     */
    public String getDamageType() {
        return damageType;
    }

    /**
     * Method to calculate the number of hit points its attack is going to do.
     * @param characters list containing the characters in the party.
     * @param target integer containing the index of the character that is going to be attacked.
     * @param multiplier integer containing the multiplier of the damage.
     * @return integer containing the number of hit points it will deal.
     */
    public int attack(List<CharacterGame> characters, int target, int multiplier) {
        int damage = multiplier * (new Random().nextInt(Integer.parseInt(this.damageDice.substring(1))) + 1);
        characters.get(target).takeDamage(damage, this.damageType);

        return damage;
    }

    /**
     * Method to subtract the damage taken from the hit points.
     * @param damage integer with the number of hit points that are going to be subtracted.
     * @param damageType string containing the type of damage the attack taken deals.
     * @return integer containing the number of hit points that has been subtracted.
     */
    public int takeDamage(int damage, String damageType) {

        this.hitPoints -= damage;

        return damage;
    }

    /**
     * Method to check if the monster is alive.
     * @return true if it is alive, false if not.
     */
    public boolean isAlive() {return this.hitPoints > 0;}
}
