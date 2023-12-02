package business.entities.Characters;

import business.entities.Monsters.Monster;

import java.util.List;
import java.util.Random;

/**
 * Class to represent the specific behaviour and traits of a Paladin entity.
 * Implements the methods from the CharacterGame interface.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class Paladin extends CharacterGame {
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
    public Paladin(String name, String player, int xp, int body, int mind, int spirit, String classType) {
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
    public void calculateInitiative() {this.setInitiative(((new Random().nextInt(10) + 1) + this.getSpirit()));}

    /**
     * Method for the character to execute its action during the preparation stage.
     * @param party list containing the characters of the party.
     * @return a string containing the action performed and its result.
     */
    public String preparationAction(List<CharacterGame> party) {
        int buff = new Random().nextInt(3) + 1;
        this.setBuff(buff);
        for (int i = 0; i < party.size(); i++) {
            party.get(i).addToMind(buff);
        }

        return (this.getName() + " uses Blessing of good luck. Everyone's Mind increases by +2.\n");
    }

    /**
     * Method to set his stats at the end of the rest stage to the value prior to the support action.
     * @param party list containing the characters of the party.
     */
    public void unbuff(List<CharacterGame> party) {
        for (int i = 0; i < party.size(); i++) {
            party.get(i).addToMind(this.getBuff() * -1);
        }
    }

    /**
     * Method to get the monster {@link Monster} the character is going to attack.
     * @param monsters list of monster {@link Monster} instances.
     * @param party list of character {@link CharacterGame} instances.
     * @return an integer representing the index the monster {@link Monster} instance which is going to be attacked.
     */
    public int getTarget(List<Monster> monsters, List<CharacterGame> party) {
        for (int i = 1; i < party.size(); i++) {
            if (party.get(i).getHitPoints() < (party.get(i).getMaxHitPoints() / 2)) {
                return -1;
            }
        }
        return new Random().nextInt(monsters.size());
    }

    /**
     * Method to get the type of damage the character class deals.
     * @return a string containing the damage types.
     */
    public String getAttackType() {
        return "Psychical";
    }

    /**
     * Method to get the attack action the character class does.
     * @return a string array containing the attack actions.
     */
    public String[] getAttackAction() {
        return new String[]{"Never on my watch", "Prayer of mass healing"};
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
        if (target == -1) {
            int heal = (new Random().nextInt(10) + 1) + this.getMind();
            String healInfo[] = new String[2];
            healInfo[0] = Integer.toString(heal);
            StringBuilder partyNames = new StringBuilder();

            for (int i = 0; i < party.size(); i++) {
                party.get(i).addToHitPoints(heal);

                if (i == 0) {
                    partyNames.append(party.get(i).getName());
                }
                else if (i == party.size() - 1) {
                    partyNames.append(" and ").append(party.get(i).getName());
                }
                else {
                    partyNames.append(", ").append(party.get(i).getName());
                }
            }

            healInfo[1] = partyNames.toString();
            return healInfo;
        }
        else {
            int damage = multiplier * ((new Random().nextInt(8) + 1) + this.getSpirit());
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
        int heal = (new Random().nextInt(10) + 1) + this.getMind();
        StringBuilder partyNames = new StringBuilder();

        for (int i = 0; i < party.size(); i++) {
            party.get(i).addToHitPoints(heal);

            if (i == 0) {
                partyNames.append(party.get(i).getName());
            }
            else if (i == party.size() - 1) {
                partyNames.append(" and ").append(party.get(i).getName());
            }
            else {
                partyNames.append(", ").append(party.get(i).getName());
            }
        }

        return (this.getName() + " uses Prayer of mass healing. Heals " + (heal) + " hit points to " + partyNames + ".\n");
    }

    /**
     * Method to subtract the damage taken to the current hit points.
     * @param damage integer containing the hit points value the attack received is going to deal
     * @return an integer containing the damage taken.
     */
    public int takeDamage(int damage, String attackType) {
        if (attackType.equals("Psychical")) {
            damage = damage / 2;
        }

        this.addToHitPoints(damage * - 1);

        return damage;
    }

    /**
     * Method to check if it can evolve
     * @return the class to which it will evolve if can evolve, "No" if it can't.
     */
    public String canEvolve() {
        return "No";
    }
}
