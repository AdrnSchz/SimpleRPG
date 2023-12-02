package business.entities;

import business.entities.Monsters.Monster;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent an Encounter entity.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class Encounter {
    /**
     * List of strings containing the monster types(names) of the monsters of the encounter
     */
    private final List<String> monsterTypes;

    /**
     * List of integers containing the amount of each type of monster the encounter has.
     */
    private final List<Integer> amount;

    /**
     * List of monster {@link Monster} instances the encounter has
     */
    private final List<Monster> monsters;

    /**
     * Constructor to initialize an encounter with empty lists.
     */
    public Encounter() {
        this.monsterTypes = new ArrayList<>();
        this.amount = new ArrayList<>();
        this.monsters = new ArrayList<>();
    }

    /**
     * Constructor to initialize an encounter with the parameters.
     * @param monsterTypes list containing the monster types
     * @param amount list containing the amount of each monster type
     * @param monsters list containing the monster instances
     */
    public Encounter(List<String> monsterTypes, List<Integer> amount, List<Monster> monsters) {
        this.monsterTypes = monsterTypes;
        this.amount = amount;
        this.monsters = monsters;
    }

    /**
     * Methos to get the types of the monsters in the encounter.
     * @return a list of strings containing the monster types.
     */
    public List<String> getMonsterTypes() {
        return monsterTypes;
    }

    /**
     * Method to add a monster types to the encounter.
     * @param monsterType string containing the monster type.
     */
    public void setMonsterTypes(String monsterType) {
        this.monsterTypes.add(monsterType);
    }

    /**
     * Method to get amount of each monster type the encounter has.
     * @return a list of integers containing the number of monster of each type the encounter has.
     */
    public List<Integer> getAmount() {
        return amount;
    }

    /**
     * Method to add an amount of monsters of a new type to the encounter.
     * @param addAmount integer containing the number of monsters of a type to be added in the encounter.
     */
    public void setAmount(int addAmount) {
        this.amount.add(addAmount);
    }

    /**
     * Method to add an amount of monsters of a type to the encounter.
     * @param index integer containing the position of the list where the amount is to be added.
     * @param addAmount integer containing the amount of monsters to be added.
     */
    public void setAmount(int index, int addAmount) {
        this.amount.add(index, this.getAmount().get(index) + addAmount);
    }

    /**
     * Method to get all the monster{@link Monster} instances the encounter has.
     * @return a list with all the monster {@link Monster} instances the encounter has.
     */
    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Method to add a monster {@link Monster} instance to the encounter.
     * @param monster monster {@link Monster} instance to be added to the encounter.
     */
    public void setMonsters(Monster monster) {
        this.monsters.add(monster);
    }

}
