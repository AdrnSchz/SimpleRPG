package business.entities.Monsters;

import business.entities.Characters.CharacterGame;

import java.util.List;
import java.util.Random;

/**
 * Class to represent a Boss monster entity.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class Boss extends Monster {
    /**
     * Constructor with the parameters to create a monster.
     *
     * @param name       a string containing the name of the monster.
     * @param challenge  a string containing the level of difficulty of the monster.
     * @param experience an integer containing the experience points this monster awards once defeated.
     * @param hitPoints  an integer containing the health the monster has.
     * @param initiative an integer containing the value of speed the monster has.
     * @param damageDice a string containing the dice that has to be thrown to calculate its dmg(Ex: "d12").
     * @param damageType a string containing the type of damage the monster deals.
     */
    public Boss(String name, String challenge, int experience, int hitPoints, int initiative, String damageDice, String damageType) {
        super(name, challenge, experience, hitPoints, initiative, damageDice, damageType);
    }

    /**
     * Method to get the target the monster is going to attack.
     * @param num integer containing the number of characters in the party.
     * @return integer containing the index of the character that is going to be attacked.
     */
    @Override
    public int getTarget(int num) {
        return -1;
    }

    /**
     * Method to subtract the damage taken from the hit points.
     * @param damage integer with the number of hit points that are going to be subtracted.
     * @param damageType string containing the type of damage the attack taken deals.
     * @return integer containing the number of hit points that has been subtracted.
     */
    @Override
    public int takeDamage(int damage, String damageType) {
        if (this.getDamageType().equals(damageType)) {
            damage = damage / 2;
            this.addHitPoints(damage * -1);
        }
        else {
            this.addHitPoints(damage * -1);
        }

        return damage;
    }

    /**
     * Method to calculate the number of hit points its attack is going to do.
     * @param characters list containing the characters in the party.
     * @param target integer containing the index of the character that is going to be attacked.
     * @param multiplier integer containing the multiplier of the damage.
     * @return integer containing the number of hit points it will deal.
     */
    @Override
    public int attack(List<CharacterGame> characters, int target, int multiplier) {
        int damage = multiplier * (new Random().nextInt(Integer.parseInt(this.getDamageDice().substring(1))) + 1);

        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).isAlive()) {
                characters.get(i).takeDamage(damage, this.getDamageType());
            }
        }

        return damage;
    }
}
