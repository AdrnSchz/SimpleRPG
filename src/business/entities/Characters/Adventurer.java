package business.entities.Characters;

import business.entities.Encounter;
import business.entities.Monsters.Monster;

import java.util.List;
import java.util.Random;

/**
 * Class to represent the specific behaviour and traits of a Adventurer entity.
 * Implements the methods from the CharacterGame interface.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class Adventurer extends CharacterGame {


    /**
     * Constructor with the parameters to create an adventurer.
     *
     * @param name      a string containing the name of the character.
     * @param player    a string containing the name of the player who owns the character.
     * @param xp        an integer containing the number of experience points that the character has.
     * @param body      an integer representing the stat "body" of the character.
     * @param mind      an integer representing the stat "mind" of the character.
     * @param spirit    an integer representing the stat "spirit" of the character.
     * @param classType a string containing the class of the character.
     */
    public Adventurer(String name, String player, int xp, int body, int mind, int spirit, String classType) {
        super(name, player, xp, body, mind, spirit, classType);
    }

    /**
     * Method for the character to execute its action during the preparation stage.
     * @param party list containing the characters of the party.
     * @return a string containing the action performed and its result.
     */
    public String preparationAction(List<CharacterGame> party) {
        this.addToSpirit(1);
        return (this.getName() + " uses Self-Motivated. Their Spirit is increased by +1.\n");
    }

    /**
     * Method to set his stats at the end of the rest stage to the value prior to the support action.
     * @param party list containing the characters of the party.
     */
    public void unbuff(List<CharacterGame> party) {
        this.addToSpirit(-1);
    }

    /**
     * Method to initialize the initiative.
     */
    public void calculateInitiative() {this.setInitiative(((new Random().nextInt(12) + 1) + this.getSpirit()));}

    /**
     * Method to calculate and set the maximum hit points of the character.
     * @param level integer with the level the character is.
     */
    public void calculateMaxHitPoints(int level) {this.setMaxHitPoints((this.getBody() + 10) * level);}

    /**
     * Method to subtract the damage taken to the current hit points.
     * @param damage integer containing the hit points value the attack received is going to deal
     * @return an integer containing the damage taken.
     */
    public int takeDamage(int damage, String attackType) {
        this.addToHitPoints(damage * - 1);
        if (this.getHitPoints() < 0) {
            this.setHitPoints(0);
        }

        return damage;
    }

    /**
     * Method to get the monster {@link Monster} the character is going to attack.
     * @param monsters list of monster {@link Monster} instances.
     * @param party list of character {@link CharacterGame} instances.
     * @return an integer representing the index the monster {@link Monster} instance which is going to be attacked.
     */
    public int getTarget(List<Monster> monsters, List<CharacterGame> party) {
        int lowestHpMonster = 0;
        for (int i = 1; i < monsters.size(); i++) {
            if (monsters.get(i).getHitPoints() < monsters.get(lowestHpMonster).getHitPoints()) {
                lowestHpMonster = i;
            }
        }
        return lowestHpMonster;
    }

    /**
     * Method to get the type of damage the character class deals.
     * @return a string containing the damage types.
     */
    public String getAttackType() {
        return "Physical";
    }

    /**
     * Method to get the attack action the character class does.
     * @return a string array containing the attack actions.
     */
    public String[] getAttackAction() {
        return new String[]{"Sword slash"};
    }

    /**
     * Method to calculate the damage the character attack is going to do.
     * @param monsters list of monster {@link Monster} instances.
     * @param target integer with the index of the monster {@link Monster} instance which is going to be attacked.
     * @param multiplier integer with the multiplier of the attack.
     * @param party list of character {@link CharacterGame} instances.
     * @return a string array with the number of hit points the attack is going to deal and extra information if needed.
     */
    public String[] attack(List<Monster> monsters, int target, int multiplier, List<CharacterGame> party) {
        int damage = multiplier * ((new Random().nextInt(6) + 1) + this.getBody());
        monsters.get(target).takeDamage(damage, this.getAttackType());

        String[] damageInfo = new String[1];
        damageInfo[0] = Integer.toString(damage);
        return damageInfo;
    }

    /**
     * Method for the character to carry out its action during the rest stage.
     * @param party list containing the characters of the party.
     * @return a string containing the action performed and its result.
     */
    public String restAction(List<CharacterGame> party) {
        int heal = new Random().nextInt(8) + 1;
        this.addToHitPoints(heal + this.getMind());

        return (this.getName() + " uses Bandage time. Heals " + (heal + this.getMind()) + " hit points.\n");
    }

    /**
     * Method to check if it can evolve
     * @return the class to which it will evolve if can evolve, "No" if it can't.
     */
    public String canEvolve() {
        if (this.getXp() >= 300) {
            return "Warrior";
        }
        else {
            return "No";
        }
    }
}