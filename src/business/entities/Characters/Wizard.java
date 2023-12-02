package business.entities.Characters;

import business.entities.Monsters.Monster;

import java.util.List;
import java.util.Random;

/**
 * Class to represent the specific behaviour and traits of a Wizard entity.
 * Implements the methods from the CharacterGame interface.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class Wizard extends CharacterGame {
    /**
     * Constructor with the parameters to create a character.
     *
     * @param name      a string containing the name of the character.
     * @param player    a string containing the name of the player who owns the character.
     * @param xp        an integer containing the number of experience points that the character has.
     * @param body      an integer representing the stat "body" of the character.
     * @param mind      an integer representing the stat "mind" of the character.
     * @param spirit    an integer representing the stat "spirit" of the character.
     * @param classType a string containing the class of the character.
     */
    public Wizard(String name, String player, int xp, int body, int mind, int spirit, String classType) {
        super(name, player, xp, body, mind, spirit, classType);
    }

    /**
     * Method to calculate and set the maximum hit points of the character.
     * @param level integer with the level the character is.
     */
    public void calculateMaxHitPoints(int level) {this.setMaxHitPoints((this.getBody() + 10) * level);}

    /**
     * Method to initialize the initiative.
     */
    public void calculateInitiative() {this.setInitiative(((new Random().nextInt(20) + 1) + this.getMind()));}

    /**
     * Method for the character to execute its action during the preparation stage.
     * @param party list containing the characters of the party.
     * @return a string containing the action performed and its result.
     */
    public String preparationAction(List<CharacterGame> party) {
        int shield = this.getLevel() * ((new Random().nextInt(6) + 1) + this.getMind());
        this.setShield(shield);

        return (this.getName() + " uses Mage shield. Shield recharges to " + this.getShield() + ".\n");
    }

    /**
     * Method to set his stats at the end of the rest stage to the value prior to the support action.
     * @param party list containing the characters of the party.
     */
    public void unbuff(List<CharacterGame> party) {
        this.setShield(0);
    }

    /**
     * Method to get the monster {@link Monster} the character is going to attack.
     * @param monsters list of monster {@link Monster} instances.
     * @param party list of character {@link CharacterGame} instances.
     * @return an integer representing the index the monster {@link Monster} instance which is going to be attacked.
     */
    public int getTarget(List<Monster> monsters, List<CharacterGame> party) {
        if (monsters.size() >= 3) return -2;
        else {
            int highestHpMonster = 0;
            for (int i = 1; i < monsters.size(); i++) {
                if (monsters.get(i).getHitPoints() > monsters.get(highestHpMonster).getHitPoints()) {
                    highestHpMonster = i;
                }
            }
            return highestHpMonster;
        }
    }

    /**
     * Method to get the type of damage the character class deals.
     * @return a string containing the damage types.
     */
    public String getAttackType() {
        return "Magical";
    }

    /**
     * Method to get the attack action the character class does.
     * @return a string array containing the attack actions.
     */
    public String[] getAttackAction() {
        return new String[]{"Arcane missile", "Fireball"};
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
        if (target == -2) {
            int damage = multiplier * ((new Random().nextInt(4) + 1) + this.getMind());
            String damageInfo[] = new String[2];
            damageInfo[0] = Integer.toString(damage);
            StringBuilder enemyNames = new StringBuilder();

            for (int i = 0; i < monsters.size(); i++) {
                monsters.get(i).takeDamage(damage, this.getAttackType());

                if (i == 0) {
                    enemyNames.append(monsters.get(i).getName());
                }
                else if (i == monsters.size() - 1) {
                    enemyNames.append(" and ").append(monsters.get(i).getName());
                }
                else {
                    enemyNames.append(", ").append(monsters.get(i).getName());
                }
            }

            damageInfo[1] = enemyNames.toString();
            return damageInfo;
        }
        else {
            int damage = multiplier * ((new Random().nextInt(6) + 1) + this.getMind());
            monsters.get(target).takeDamage(damage, this.getAttackType());

            String[] damageInfo = new String[1];
            damageInfo[0] = Integer.toString(damage);
            return damageInfo;
        }
    }

    /**
     * Method for the character to carry out its action during the rest stage.
     * @param party list containing the characters of the party.
     * @return a string containing the action performed and its result.
     */
    public String restAction(List<CharacterGame> party) {
        return (this.getName() + " is reading a book.\n");
    }

    /**
     * Method to subtract the damage taken to the current hit points.
     * @param damage integer containing the hit points value the attack received is going to deal
     * @return an integer containing the damage taken.
     */
    public int takeDamage(int damage, String attackType) {
        int initialDamage;
        if (attackType.equals("Magical")) {
            damage -= this.getLevel();
            if (damage < 0) damage = 0;
        }
        initialDamage = damage;

        if (getShield() > 0) {
            damage = this.subtractFromShield(damage);
            if (damage != 0) this.addToHitPoints(damage);
        } else {
            this.addToHitPoints(damage * -1);
        }
        return initialDamage;
    }

    /**
     * Method to check if it can evolve
     * @return the class to which it will evolve if can evolve, "No" if it can't.
     */
    public String canEvolve() {
        return "No";
    }
}
