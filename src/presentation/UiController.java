package presentation;

import business.AdventureManager;
import business.CharacterManager;
import business.EncounterManager;
import business.entities.Adventure;
import business.entities.Characters.CharacterGame;
import business.entities.Encounter;
import business.entities.Monsters.Monster;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is to control the logic of the use interface. Talking to the UiManager class and to the business layer.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class UiController {
    /**
     * Instance of the CharacterManager class {@link CharacterManager} to talk with the business layer.
     */
    private final CharacterManager cm;

    /**
     * Instance of the AdventureManager {@link AdventureManager} class to talk with the business layer.
     */
    private final AdventureManager am;

    /**
     * Instance of the EncounterManager class {@link EncounterManager} to talk with the business layer.
     */
    private final EncounterManager em;

    /**
     * Instance of the UiManager class {@link UiManager} to print and receive data from the user.
     */
    private final UiManager ui;

    private int persistenceSource;

    /**
     * Constructor with parameters to create a user interface controller.
     * @param cm Instance of the CharacterManager class {@link CharacterManager} to talk with the business layer.
     * @param am Instance of the AdventureManager {@link AdventureManager} class to talk with the business layer.
     * @param em Instance of the EncounterManager class {@link EncounterManager} to talk with the business layer.
     * @param ui Instance of the UiManager class {@link UiManager} to print and receive data from the user.
     */
    public UiController(CharacterManager cm, AdventureManager am, EncounterManager em, UiManager ui) {
        this.cm = cm;
        this.am = am;
        this.em = em;
        this.ui = ui;
    }
    /**
     * Main method of the controller and of the project logic.
     * If no problem finding the files is found. It will run infinitely until the exit option is pressed,
     * presenting the user with the application's menu and executing the chosen option.
     */
    public void run() {
        int option = 0;
        ui.printWelcome();

        while (option != 1 && option != 2) {
            ui.askDataSource();
            option = ui.getIntInput();

            if (option == 1) {
                persistenceSource = 1;
                ui.printLoading();
                if (!checkJSONFiles()) return;
            }
            else if (option == 2) {
                persistenceSource = 2;
                ui.printLoading();
                em.setMonsterDao(2);
                am.setAdventureDAO(2);
                cm.setCharacterDAO(2);

                if (em.loadMonsters() == null) {
                    persistenceSource = 1;
                    em.setMonsterDao(1);
                    am.setAdventureDAO(1);
                    cm.setCharacterDAO(1);
                    ui.printAPIError();

                    if (!checkJSONFiles()) return;
                }
            }
            else {
                ui.printInvalidOption();
            }
        }

        ui.printLoaded();
        while (option != 5) {
            if (cm.checkNumCharacters() < 3) {
                ui.printMenuDisabled();
            }
            else {
                ui.printMenu();
            }
            option = ui.getIntInput();
            switch (option) {
                case 1 -> characterCreation();
                case 2 -> listCharacters();
                case 3 -> createAdventure();
                case 4 -> playAdventure();
                case 5 -> ui.printExit();
                default -> ui.printInvalidOption();
            }
        }
    }

    /**
     * Method to check if the JSON files can be found.
     * @return True if the files are found, false if not.
     */
    private boolean checkJSONFiles() {
        if (em.loadMonsters() == null) {
            ui.printFileNotFound("monsters.json");
            return false;
        }
        if (cm.getCharacters() == null) {
            ui.printFileNotFound("characters.json");
            return false;
        }
        if (!am.checkAdventureFile()) {
            ui.printFileNotFound("adventures.json");
            return false;
        }

        return true;
    }

    /**
     * Method to execute the character creation option.
     */
    private void characterCreation() {
        ui.askCharacterName();
        String characterName = cm.validateName(ui.getStringInput().toLowerCase());

        if (characterName.equals("1")) {
            ui.printInvalidName();
        } else {
            ui.askPlayerName(characterName);
            String playerName = ui.getStringInput();
            int level;
            do {
                ui.askLevel();
                level = ui.getIntInput();
                if (level < 1 || level > 10) {
                    ui.printInvalidOption();
                }
            } while (level < 1 || level > 10);

            int[] randomValues = cm.generateStats();
            int[] stats = cm.calculateStats(randomValues);
            ui.printStats(characterName, randomValues, stats, level);
            String classType;
            do {
                ui.askClass();
                classType = ui.getStringInput();

                if (!classType.equals("Adventurer") && !classType.equals("Cleric") && !classType.equals("Wizard")) {
                    ui.printInvalidOption();
                }
            } while (!classType.equals("Adventurer") && !classType.equals("Cleric") && !classType.equals("Wizard"));

            ui.printCharacterCreated(characterName, classType);
            cm.createCharacter(characterName, playerName, level, stats, classType);
        }
    }

    /**
     * Method to execute the list characters option.
     */
    private void listCharacters() {
        ui.askFilter();
        String input = ui.getStringInput();
        List<CharacterGame> adventurers = cm.getCharacters(input);
        if (adventurers.size() == 0) {
            ui.printNoCharacters();
        }
        else {
            ui.listAdventurers(input, adventurers);
            int index = ui.getIntInput();
            if (index < 0 || index > adventurers.size()) {
                ui.printInvalidOption();
            }
            else if (index != 0){
                index -= 1;
                ui.printCharacter(adventurers.get(index), cm.calculateLevel(adventurers.get(index).getXp()));
                do {
                    ui.askDeleteCharacter(adventurers.get(index).getName());
                    input = ui.getStringInput();
                    if (input.equals(adventurers.get(index).getName())) {
                        ui.printDeleteCharacter(adventurers.get(index).getName());
                        cm.deleteCharacter(adventurers.get(index).getName(), persistenceSource);
                    }
                    else if (!input.equals("")){
                        ui.printInvalidName();
                    }
                } while (!input.equals("") && !input.equals(adventurers.get(index).getName()));
            }
        }
    }

    /**
     * Method to execute the create adventure option.
     */
    private void createAdventure() {
        ui.askAdventureName();
        String input = ui.getStringInput();

        if (am.isUniqueName(input)) {
            ui.askEncounters(input);
            int numEncounters = 0, i = 0;
            while (i < 3) {
                ui.askEncountersInput();
                numEncounters = ui.getIntInput();
                if (numEncounters < 1 || numEncounters > 4) {
                    ui.printInvalidOption();
                    i++;
                } else {
                    break;
                }
            }
            if (i < 3) {
                List<Encounter> encounters = new ArrayList<>();
                ui.encounterCreationQuote(numEncounters);
                encounterCreation(encounters, numEncounters);
                am.createAdventure(input, numEncounters, encounters);
            }
        } else {
            ui.printInvalidName();
        }
    }

    /**
     * Method to execute the encounter creation logic inside the create adventure option.
     * @param encounters list of encounter {@link Encounter} instances the adventure will be formed of.
     * @param numEncounters integer contain the number of the encounters the adventure will have.
     */
    private void encounterCreation(List<Encounter> encounters, int numEncounters) {
        for (int i = 0; i < numEncounters; i++) {
            int selection = 0;
            encounters.add(new Encounter());
            while(selection != 3) {
                ui.askEncounterOptions(numEncounters, i + 1, encounters.get(i).getMonsterTypes().size(), encounters.get(i).getMonsterTypes(), encounters.get(i).getAmount());
                selection = ui.getIntInput();
                if (selection < 1 || selection > 3) {
                    ui.printInvalidOption();
                }
                else if (selection == 1) {
                    addMonsterToEncounter(encounters, i);
                }
                else if (selection == 2) {
                    removeMonsterFromEncounter(encounters, i);
                }
            }
        }
    }

    /**
     * Method to execute the add monster option inside the encounter creation method.
     * @param encounters list of encounter {@link Encounter} instances the adventure will be formed of.
     * @param i integer contain the index of the encounter the monster added will belong to.
     */
    private void addMonsterToEncounter(List<Encounter> encounters, int i) {
        List<Monster> monsters = em.loadMonsters();
        ui.listMonsters(monsters);
        int index = ui.getIntInput();
        if (index < 1 || index > monsters.size()) {
            ui.printInvalidOption();
        }
        else {
            ui.askMonsterAmount(monsters.get(index - 1).getName());
            int amount = ui.getIntInput();
            if (amount < 1) {
                ui.printInvalidOption();
            }
            else if (em.checkBosses(encounters.get(i), monsters.get(index - 1), amount)) {
                ui.printInvalidBossAmount();
            }
            else {
                encounters.set(i, em.addMonsterToEncounter(encounters.get(i), monsters.get(index - 1), amount));
            }
        }
    }

    /**
     * Method to execute the remove monster option inside the encounter creation method.
     * @param encounters list of encounter {@link Encounter} instances the adventure will be formed of.
     * @param i integer contain the index of the encounter the monster removed belongs to.
     */
    private void removeMonsterFromEncounter(List<Encounter> encounters, int i) {
        ui.askMonsterToDelete();
        int index = ui.getIntInput();
        if (index < 1 || index > encounters.get(i).getMonsterTypes().size()) {
            ui.printInvalidOption();
        }
        else {
            ui.printMonsterDeleted(encounters.get(i).getMonsterTypes().get(index - 1), encounters.get(i).getAmount().get(index - 1));
            encounters.set(i, em.removeMonsterFromEncounter(encounters.get(i), index - 1));
        }
    }

    /**
     * Method to execute the play adventure option.
     */
    private void playAdventure() {
        if (cm.checkNumCharacters() < 3) {
            ui.printInvalidOption();
        }
        else {
            List<CharacterGame> characters = new ArrayList<>(cm.getCharacters());
            List<Adventure> adventures = new ArrayList<>(am.getAdventures());

            ui.listAdventures(adventures);
            int index = ui.getIntInput();

            if (index < 1 || index > adventures.size()) {
                ui.printInvalidOption();
            } else {
                Adventure adventure = adventures.get(index - 1);
                List<CharacterGame> party = getParty(characters, adventure);
                if (party != null) {
                    cm.initializeHitPoints(party);
                    startEncounters(party, adventure);
                }
            }
        }
    }

    /**
     * Method to execute the logic to get the party members inside the play adventure option.
     * @param characters list of character {@link CharacterGame} instances that can join the party.
     * @param adventure adventure {@link Adventure} instance the party is going to go through.
     * @return list of character {@link CharacterGame} instances that are going to go through the adventure.
     */
    private @Nullable List<CharacterGame> getParty(List<CharacterGame> characters, Adventure adventure) {
        ui.askPartySize(adventure.getName());
        int partySize = ui.getIntInput();
        List<CharacterGame> party;

        if (partySize < 3 || partySize > 5) {
            ui.printInvalidOption();
            return null;
        } else {
            party = new ArrayList<>();
            ui.printPartySize(partySize);
            while (party.size() < partySize) {
                ui.askMemberToJoin(partySize, characters, party);
                int index = ui.getIntInput();
                if (index < 1 || index > characters.size()) {
                    ui.printInvalidOption();
                } else {
                    if (cm.isUniqueName(characters.get(index - 1).getName(), party)) {
                        party.add(characters.get(index - 1));
                    } else {
                        ui.printAlreadySelected(characters.get(index - 1).getName());
                    }
                }
            }
            ui.startAdventure(party, adventure.getName());
        }
        return party;
    }

    /**
     * Method to execute the logic to go through the adventure encounters and its different stages inside
     * the play adventure option.
     * @param party list of character {@link CharacterGame} instances that are going to go through the adventure.
     * @param adventure adventure {@link Adventure} instance the party is going to go through.
     */
    private void startEncounters(List<CharacterGame> party, Adventure adventure) {
        int currentEncounter = 0;
        while (currentEncounter < adventure.getNumEncounters()) {
            ui.encounterInitialization(adventure.getEncounters().get(currentEncounter), currentEncounter + 1);
            ui.printPreparationStage(cm.preparationStage(party), am.rollInitiative(party, adventure, currentEncounter));
            ui.printCombatStage(am.combatStage(adventure.getEncounters().get(currentEncounter), party));

            if (!cm.partyIsAlive(party)) {
                ui.printPartyDefeated();
                return;
            }
            ui.printRestStage(am.restStage(party, adventure.getEncounters().get(currentEncounter)));
            currentEncounter++;
        }
        ui.printVictory(adventure.getName());
        cm.updateLevel(party, persistenceSource);
    }
}
