package business;

import business.entities.Adventure;
import business.entities.Characters.Champion;
import business.entities.Characters.CharacterGame;
import business.entities.Characters.Paladin;
import business.entities.Characters.Warrior;
import business.entities.Encounter;
import business.entities.Monsters.Boss;
import business.entities.Monsters.Monster;
import persistence.API.APIAdventureDAO;
import persistence.AdventureDAO;
import persistence.JSON.JSONAdventureDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is to manage all the logic regarding an adventure{@link Adventure}.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class AdventureManager {
    /**
     * Instance of the JSONAdventureDAO class {@link JSONAdventureDAO} to talk with the persistence layer.
     */
    private AdventureDAO aDao;

    /**
     * Instance of the encounter manager class {@link EncounterManager} to manage the logic regarding encounters
     * {@link Encounter} in an adventure.
     */
    private final EncounterManager em;

    /**
     * Instance of the character manager class {@link CharacterManager} to manage the logic regarding characters
     * {@link CharacterGame} in an adventure.
     */
    private final CharacterManager cm;

    /**
     * Constructor to create an adventure manager.
     */
    public AdventureManager() {
        this.aDao = new JSONAdventureDAO();
        this.em = new EncounterManager();
        this.cm = new CharacterManager();
    }

    /**
     * Setter method to set the interface of the adventureDAO {@link AdventureDAO} to be used.
     */
    public void setAdventureDAO(int option) {
        if (option == 1) {
            this.aDao = new JSONAdventureDAO();
        }
        else {
            this.aDao = new APIAdventureDAO();
        }
    }

    /**
     * Method to check if the file storing the adventures can be accessed.
     * @return true if is found, false if it is not.
     */
    public boolean checkAdventureFile() {
        return aDao.read() != null;
    }

    /**
     * Method to check if the name is unique between all the adventure {@link Adventure} names.
     * @param name string containing the name to be checked.
     * @return true if it is unique, false if not.
     */
    public boolean isUniqueName(String name) {
        List<Adventure> adventures = new ArrayList<>(aDao.read());

        for (Adventure adventure : adventures) {
            if (adventure.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to create an adventure{@link Adventure} and persist it.
     * @param name string containing the name of the adventure{@link Adventure}.
     * @param numEncounters integer containing the number of encounters{@link Encounter} an adventure has
     * {@link Adventure}.
     * @param encounters list of encounter {@link Encounter} instances the adventure {@link Adventure} has.
     */
    public void createAdventure(String name, int numEncounters, List<Encounter> encounters) {
        List<Adventure> adventures = new ArrayList<>(aDao.read());
        Adventure adventure = new Adventure(name, numEncounters, encounters);
        adventures.add(adventure);
        aDao.write(adventures);
    }

    /**
     * Method to get all the adventures stored in the system.
     * @return a list of adventure {@link Adventure} insatnces containing all the adventures of the system.
     */
    public List<Adventure> getAdventures() {return aDao.read();}

    /**
     * Method to calculate the initiatives of either monsters{@link Monster} and characters{@link CharacterGame} and
     * sort them in order.
     * @param party list of character{@link CharacterGame} instances containing the characters participating in the
     * adventure {@link Adventure}.
     * @param adventure adventure {@link Adventure} object containing the adventure that is being played.
     * @param currentEncounter integer value containing the index of the encounter currently being played.
     * @return a string contained the order in which each character {@link CharacterGame} and monster{@link Monster}
     * is going to attack.
     */
    public String rollInitiative(List<CharacterGame> party, Adventure adventure, int currentEncounter) {
        em.rollInitiative(adventure.getEncounters().get(currentEncounter));
        em.sortInitiative(adventure.getEncounters().get(currentEncounter));
        cm.rollInitiative(party);
        cm.sortInitiative(party);
        return initiativeOrderLog(adventure.getEncounters().get(currentEncounter), party);
    }

    /**
     * Method to create a string that he order in which each character {@link CharacterGame} and
     * monster{@link Monster} is going to attack.
     * @param encounter encounter {@link Encounter} object containing the encounter the party members
     * {@link CharacterGame} are currently facing.
     * @param party list of character {@link CharacterGame} instances containing the characters playing the adventure.
     * @return a string containing the order in which each character {@link CharacterGame} and monster{@link Monster}
     * is going to attack.
     */
    private String initiativeOrderLog(Encounter encounter, List<CharacterGame> party) {
        int i =0, j = 0;
        StringBuilder log = new StringBuilder();
        log.append("\nRolling initiative...\n");

        while (i < encounter.getMonsters().size() && j < party.size()) {
            if (encounter.getMonsters().get(i).getInitiative() > party.get(j).getInitiative()) {
                log.append("  - ").append(encounter.getMonsters().get(i).getInitiative()).append("\t")
                        .append(encounter.getMonsters().get(i).getName()).append("\n");
                i++;
            }
            else {
                log.append("  - ").append(party.get(j).getInitiative()).append("\t")
                        .append(party.get(j).getName()).append("\n");
                j++;
            }
        }
        if (i < encounter.getMonsters().size()) {
            for (int k = i; k < encounter.getMonsters().size(); k++) {
                log.append("  - ").append(encounter.getMonsters().get(k).getInitiative()).append("\t")
                        .append(encounter.getMonsters().get(k).getName()).append("\n");
            }
        }
        else if (j < party.size()) {
            for (int k = j; k < party.size(); k++) {
                log.append("  - ").append(party.get(k).getInitiative()).append("\t")
                        .append(party.get(k).getName()).append("\n");
            }
        }
        return log.toString();
    }

    /**
     * Method with the logic involving the combat stage.
     * @param encounter encounter {@link Encounter} object containing the encounter the party members
     * {@link CharacterGame} are currently facing.
     * @param party list of character {@link CharacterGame} instances containing the characters playing the adventure.
     * @return a string containing all the actions performed during the combat stage and its results.
     */
    public String combatStage(Encounter encounter, List<CharacterGame> party) {
        StringBuilder log = new StringBuilder();
        int i, j, l = 1;
        while (encounter.getMonsters().size() != 0 && cm.partyIsAlive(party)) {
            i = 0;
            j = 0;
            log.append("\nRound ").append(l).append(":\n").append("Party:\n");
            for (CharacterGame characterGame : party) {
                log.append("  - ").append(characterGame.getName()).append("\tHP: ").append(characterGame.getHitPoints())
                        .append(" / ").append(characterGame.getMaxHitPoints()).append(" hit points");
                if (characterGame.getShield() > 0) log.append(" (Shield: ").append(characterGame.getShield()).append(")");
                log.append("\n");
            }
            while (i < encounter.getMonsters().size() && j < party.size() && (encounter.getMonsters().size() != 0 && cm.partyIsAlive(party))) {
                if (encounter.getMonsters().get(i).getInitiative() > party.get(j).getInitiative()) {
                    log.append(monsterAttack(encounter.getMonsters(), i, party));
                    i++;
                } else {
                    if (party.get(j).isAlive()) {
                        log.append(characterAttack(party.get(j), encounter.getMonsters(), party));
                    }
                    j++;
                }
            }
            if (i < encounter.getMonsters().size()) {
                for (int k = i; k < encounter.getMonsters().size(); k++) {
                    if (encounter.getMonsters().size() != 0 && cm.partyIsAlive(party)) {
                        log.append(monsterAttack(encounter.getMonsters(), i, party));
                    }
                }
            } else if (j < party.size()) {
                for (int k = j; k < party.size(); k++) {
                    if (encounter.getMonsters().size() != 0 && party.get(k).isAlive()) {
                        log.append(characterAttack(party.get(j), encounter.getMonsters(), party));
                    }
                }
            }
            log.append("\nEnd of round ").append(l).append(".\n");
            l++;
        }
        if (cm.partyIsAlive(party)) {
            log.append("All enemies are defeated\n\n");
        }
        return log.toString();
    }

    /**
     * Method with the logic involving the monster{@link Monster} attack.
     * @param monsters monster list {@link Monster} containing monster instances.
     * @param i index of the monster {@link Monster} that is going to attack.
     * @param party list of character {@link CharacterGame} instances containing the characters playing the adventure.
     * @return a string containing the attack information(name of the monster, damage, objective, if it landed or not,
     * if it killed the objective or not.
     */
    private String monsterAttack(List<Monster> monsters, int i, List<CharacterGame> party) {
        StringBuilder log = new StringBuilder();
        while (true) {
            Random random2 = new Random();
            int index = monsters.get(i).getTarget(party.size());
            int hit = random2.nextInt(10) + 1;

            if (index == -1) {
                log = bossAttack(monsters.get(i), party, log, hit);
                break;
            }
            else {
                if (party.get(index).isAlive()) {
                    log.append("\n").append(monsters.get(i).getName())
                            .append(" attacks ").append(party.get(index).getName()).append(".\n");
                    if (hit == 1) {
                        log.append("Fails and deals 0 ").append(monsters.get(i).getDamageType()).append(" damage.\n");
                    }
                    else if (hit == 10) {
                        int damage = monsters.get(i).attack(party, index, 2);
                        log.append("Critical hit and deals ").append(damage).append(" ").append(monsters.get(i).getDamageType()).append(" damage.\n");
                    }
                    else {
                        int damage = monsters.get(i).attack(party, index, 1);
                        log.append("Hits and deals ").append(damage).append(" ").append(monsters.get(i).getDamageType()).append(" damage.\n");
                    }
                    if (!party.get(index).isAlive()) {
                        log.append(party.get(index).getName()).append(" falls unconscious.\n");
                    }
                    break;
                }
            }
        }
        return log.toString();
    }

    /**
     * Method with the logic involving the attack of a "boss" monster {@link Monster}.
     * @param monster monster {@link Monster} object containing the monster attacking.
     * @param party list of character {@link CharacterGame} instances containing the characters playing the adventure.
     * @param log string builder containing the log of the attack.
     * @param hit integer containing the hit value of the monster.
     * @return a string builder containing the log of the attack.
     */
    private StringBuilder bossAttack(Monster monster, List<CharacterGame> party, StringBuilder log, int hit) {
        List<Integer> alive = new ArrayList<>();

        for (int i = 0; i < party.size(); i++) {
            if (party.get(i).isAlive()) {
                alive.add(i);
            }
        }

        for (int i = 0; i < alive.size(); i++) {
            if (i == alive.size() - 1 && i == 0) log.append("\n").append(monster.getName()).append(" attacks ").append(party.get(alive.get(i)).getName()).append(".\n");
            else if (i == alive.size() - 1) log.append(" and ").append(party.get(alive.get(i)).getName()).append(".\n");
            else if (i == 0) log.append("\n").append(monster.getName()).append(" attacks ").append(party.get(alive.get(i)).getName());
            else log.append(", ").append(party.get(alive.get(i)).getName());
        }

        int damage = 0;
        if (hit == 1) {
            log.append("Fails and deals 0 ").append(monster.getDamageType()).append(" damage.\n");
        }
        else if (hit == 10) {
            damage = monster.attack(party, -1, 2);
            log.append("Critical hit and deals ").append(damage).append(" ").append(monster.getDamageType()).append(" damage.\n");
        }
        else {
            damage = monster.attack(party, -1, 1);
            log.append("Hits and deals ").append(damage).append(" ").append(monster.getDamageType()).append(" damage.\n");
        }

        if (damage != 0) {
            for (int i = 0; i < alive.size(); i++) {
                if (!party.get(alive.get(i)).isAlive()) {
                    log.append(party.get(alive.get(i)).getName()).append(" falls unconscious.\n");
                }
            }
        }
        return log;
    }
    /**
     * Method with the logic involving the character {@link CharacterGame} attack.
     * @param character character {@link CharacterGame} object containing the character attacking.
     * @param monsters list of monster {@link Monster} instances containing the monsters alive in the
     * encounter{@link Encounter}.
     * @param party list of character {@link CharacterGame} instances containing the characters playing the adventure.
     * @return a string containing the attack information(name of the monster, damage, objective, if it landed or not,
     * if it killed the objective or not.
     */
    private String characterAttack(CharacterGame character, List<Monster> monsters, List<CharacterGame> party) {
        StringBuilder log = new StringBuilder();
        int index = character.getTarget(monsters, party), hit = new Random().nextInt(10) + 1;

        if (index == -1) {
            String[] healInfo = character.attack(monsters, index, 1, party);
            log.append("\n").append(character.getName()).append(" uses ").append(character.getAttackAction()[1])
                    .append(". Heals ").append(healInfo[0]).append(" hit points to ").append(healInfo[1]).append(".\n");
        }
        else if (index == -2) {
            if (hit == 1) {
                String[] damageInfo = character.attack(monsters, index, 0, party);
                log.append("\n").append(character.getName()).append(" attacks ").append(damageInfo[1])
                        .append(" with ").append(character.getAttackAction()[1]).append(".\n");
                log.append("Fails and deals 0 ").append(character.getAttackType()).append(" damage.\n");
            }
            else if (hit == 10) {
                String[] damageInfo = character.attack(monsters, index, 2, party);
                log.append("\n").append(character.getName()).append(" attacks ").append(damageInfo[1])
                        .append(" with ").append(character.getAttackAction()[1]).append(".\n");
                log.append("Critical hit and deals ").append(damageInfo[0]).append(" ").append(character.getAttackType()).append(" damage.\n");
            }
            else {
                String[] damageInfo = character.attack(monsters, index, 1, party);
                log.append("\n").append(character.getName()).append(" attacks ").append(damageInfo[1])
                        .append(" with ").append(character.getAttackAction()[1]).append(".\n");
                log.append("Hits and deals ").append(damageInfo[0]).append(" ").append(character.getAttackType()).append(" damage.\n");
            }
            for (int i = 0; i < monsters.size(); i++) {
                if (!monsters.get(i).isAlive()) {
                    log.append(monsters.get(i).getName()).append(" dies.\n");
                    monsters.remove(i);
                }
            }
        }
        else {
            log.append("\n").append(character.getName()).append(" attacks ").append(monsters.get(index).getName())
                    .append(" with ").append(character.getAttackAction()[0]).append(".\n");
            if (hit == 1) {
                log.append("Fails and deals 0 ").append(character.getAttackType()).append(" damage.\n");
            } else if (hit == 10) {
                String[] damage = character.attack(monsters, index, 2, party);
                log.append("Critical hit and deals ").append(damage[0]).append(" ").append(character.getAttackType()).append(" damage.\n");
            } else {
                String[] damage = character.attack(monsters, index, 1, party);
                log.append("Hits and deals ").append(damage[0]).append(" ").append(character.getAttackType()).append(" damage.\n");
            }
            if (!monsters.get(index).isAlive()) {
                log.append(monsters.get(index).getName()).append(" dies.\n");
                monsters.remove(index);
            }
        }
        return log.toString();
    }

    /**
     * Method with the logic involving the rest stage.
     * @param party list of character {@link CharacterGame} instances containing the characters playing the adventure.
     * @param encounter encounter {@link Encounter} object containing the encounter the party members
     * {@link CharacterGame} are currently facing.
     * @return a string containing all the actions performed during the rest stage and its results.
     */
    public String restStage(List<CharacterGame> party, Encounter encounter) {
        StringBuilder log = new StringBuilder();
        int xp = em.getEncounterXp(encounter);

        for (CharacterGame characterGame : party) {
            int level = characterGame.getLevel();
            log.append(characterGame.getName()).append(" gains ").append(xp).append(" xp. ");
            characterGame.addXp(xp);
            characterGame.unbuff(party);

            if (characterGame.getLevel() != level) {
                log.append(characterGame.getName()).append(" levels up. They are now lvl ").append(cm.calculateLevel(characterGame.getXp())).append("!");
                characterGame.calculateMaxHitPoints(characterGame.getLevel());
                characterGame.setHitPoints(characterGame.getMaxHitPoints());

                switch (characterGame.canEvolve()) {
                    case "Warrior" -> {
                        Warrior warrior = new Warrior(characterGame.getName(), characterGame.getPlayer(),
                                characterGame.getXp(), characterGame.getBody(), characterGame.getMind(), characterGame.getSpirit(),
                                "Warrior");
                        warrior.calculateMaxHitPoints(warrior.getLevel());
                        warrior.setHitPoints(warrior.getMaxHitPoints());
                        log.append("\n").append(characterGame.getName()).append(" evolves to Warrior!");
                        party.set(party.indexOf(characterGame), warrior);
                    }
                    case "Champion" -> {
                        Champion champion = new Champion(characterGame.getName(), characterGame.getPlayer(),
                                characterGame.getXp(), characterGame.getBody(), characterGame.getMind(), characterGame.getSpirit(),
                                "Champion");
                        champion.calculateMaxHitPoints(champion.getLevel());
                        champion.setHitPoints(champion.getMaxHitPoints());
                        log.append("\n").append(characterGame.getName()).append(" evolves to Champion!");
                        party.set(party.indexOf(characterGame), champion);
                    }
                    case "Paladin" -> {
                        Paladin paladin = new Paladin(characterGame.getName(), characterGame.getPlayer(),
                                characterGame.getXp(), characterGame.getBody(), characterGame.getMind(), characterGame.getSpirit(),
                                "Paladin");
                        paladin.calculateMaxHitPoints(paladin.getLevel());
                        paladin.setHitPoints(paladin.getMaxHitPoints());
                        log.append("\n").append(characterGame.getName()).append(" evolves to Paladin!");
                        party.set(party.indexOf(characterGame), paladin);
                    }
                }
            }
            log.append("\n");
        }

        log.append("\n");

        for (CharacterGame characterGame : party) {
            if (characterGame.isAlive()) {
                log.append(characterGame.restAction(party));
            }
            else {
                log.append(characterGame.getName()).append(" is unconscious\n");
            }
        }
        return log.toString();
    }
}
