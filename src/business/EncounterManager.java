package business;


import business.entities.Encounter;
import business.entities.Monsters.Monster;
import org.jetbrains.annotations.NotNull;
import persistence.API.APIMonsterDAO;
import persistence.JSON.JSONMonsterDAO;
import persistence.MonsterDAO;

import java.util.List;

/**
 * This class is to manage all the logic regarding an encounter{@link Encounter}.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class EncounterManager {
    /**
     * Instance of the JSONMonsterDAO class {@link JSONMonsterDAO} to talk with the persistence layer.
     */
    private MonsterDAO mDao;

    /**
     * Constructor to create an encounter manager.
     */
    public EncounterManager() {
        this.mDao = new JSONMonsterDAO();
    }

    /**
     * Setter method to set the interface of the monsterDAO {@link MonsterDAO} to be used.
     */
    public void setMonsterDao(int option) {
        if (option == 1) {
            this.mDao = new JSONMonsterDAO();
        }
        else {
            this.mDao = new APIMonsterDAO();
        }
    }

    /**
     * Method to add monster {@link Monster} instances into an encounter.
     * @param encounter {@link Encounter} object containing the encounter the monsters {@link Monster} belong to.
     * @param monster monster {@link Monster} object to be added into the encounter {@link Encounter}.
     * @param amount integer value with the amount of monster {@link Monster} instances to be added.
     * @return the encounter {@link Encounter} instance to which the monster {@link Monster} instance/s was added.
     */
    public Encounter addMonsterToEncounter(Encounter encounter, Monster monster, int amount ) {
        if (encounter.getMonsterTypes().size() == 0) {
            encounter.setMonsterTypes(monster.getName());
            encounter.setAmount(amount);
            for (int i = 0; i < amount; i++) {
                encounter.setMonsters(monster);
            }
        }
        else {
            for (int i = 0; i < encounter.getMonsterTypes().size(); i++) {
                if (encounter.getMonsterTypes().get(i).equals(monster.getName())) {
                    encounter.setAmount(i, amount);
                    for (int j = 0; j < encounter.getAmount().get(i); j++) {
                        encounter.setMonsters(monster);
                    }
                    return encounter;
                }
            }
            encounter.setMonsterTypes(monster.getName());
            encounter.setAmount(amount);
            for (int j = 0; j < amount; j++) {
                encounter.setMonsters(monster);
            }
        }
        return encounter;
    }

    /**
     * Method to check if the monster {@link Monster} to be added into encounter {@link Encounter} makes the encounter
     * to have more than 1 boss monster{@link Monster}.
     * @param encounter {@link Encounter} object containing the encounter the monsters {@link Monster} belong to.
     * @param monster monster {@link Monster} object to be added into the encounter {@link Encounter}.
     * @param amount integer value with the amount of monster {@link Monster} instances to be added.
     * @return true if the encounter will have more than 1 boss monster {@link Monster}, false if not.
     */
    public boolean checkBosses(Encounter encounter, @NotNull Monster monster, int amount) {
        if (monster.getChallenge().equals("Boss") && amount > 1) {
            return true;
        }

        else if (monster.getChallenge().equals("Boss")){
            for (int i = 0; i < encounter.getMonsters().size(); i++) {
                if (encounter.getMonsters().get(i).getChallenge().equals("Boss")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to get all the monster {@link Monster} stored in the system.
     * @return a list of monster {@link Monster} instances.
     */
    public List<Monster> loadMonsters() {
        return mDao.read();
    }

    /**
     * Method to remove a monster type{@link Monster} from an encounter {@link Encounter}.
     * @param encounter {@link Encounter} object containing the encounter the monsters {@link Monster} belong to.
     * @param index integer containing the index of the monster type to be removed.
     * @return the encounter {@link Encounter} instance from which the monster {@link Monster} instance/s was removed.
     */
    public Encounter removeMonsterFromEncounter(Encounter encounter, int index) {
        String name = encounter.getMonsterTypes().get(index);
        encounter.getMonsterTypes().remove(index);
        encounter.getAmount().remove(index);
        for (int i = 0; i < encounter.getMonsters().size(); i++) {
            if (encounter.getMonsters().get(i).getName().equals(name)) {
                encounter.getMonsters().remove(i);
                i--;
            }
        }
        return encounter;
    }

    /**
     * Method to calculate the initiative of each monster {@link Monster} of the encounter {@link Encounter}.
     * @param encounter {@link Encounter} object containing the encounter the monsters {@link Monster} belong to.
     */
    public void rollInitiative(Encounter encounter) {
        for (int i = 0; i < encounter.getMonsters().size(); i++) {
            encounter.getMonsters().get(i).setInitiative();
        }
    }

    /**
     * Method to sort the monster in the order they will attack
     * @param encounter {@link Encounter} object containing the encounter the monsters {@link Monster} belong to.
     */
    public void sortInitiative(Encounter encounter) {
        encounter.getMonsters().sort((o1, o2) -> o2.getInitiative() - o1.getInitiative());
    }

    /**
     * Method to calculate the total experience an encounter awards upon completion.
     * @param encounter encounter {@link Encounter} instance which we want to calculate the experience points of.
     * @return an integer with a value representing the total number of experience points awarded.
     */
    public int getEncounterXp(Encounter encounter) {
        List<Monster> monsters = loadMonsters();
        int xp = 0;

        for (int i = 0; i < encounter.getMonsterTypes().size(); i++) {
            for (Monster monster : monsters) {
                if (encounter.getMonsterTypes().get(i).equals(monster.getName())) {
                    xp += encounter.getAmount().get(i) * monster.getExperience();
                }
            }
        }
        return xp;
    }
}
