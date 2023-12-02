package presentation;

import business.entities.Adventure;
import business.entities.Characters.CharacterGame;
import business.entities.Encounter;
import business.entities.Monsters.Monster;

import java.util.List;
import java.util.Scanner;

/**
 * This class is for printing data and receiving data from the console.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class UiManager {
    /**
     *Instance of the scanner class to get the data from the user and parse it.
     */
    private Scanner sc = new Scanner(System.in);

    /**
     * Method to receive from the user the whole line inputted.
     * @return The whole input received from the user as a String.
     */
    public String getStringInput() {return sc.nextLine();}

    /**
     * Method to receive the inputted data transformed into an integer.
     * @return The input data as an int. In case of a numberFormatException it returns -1.
     */
    public int getIntInput() {
        int input;
        try {
            input = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
        return input;
    }

    /**
     * Method to print the initialization of the program.
     */
    public void printWelcome() {
        System.out.println("""
           ____ _               __       __    ____ ___   ___   _____
          / __/(_)__ _   ___   / /___   / /   / __// _ \\ / _ \\ / ___/
         _\\ \\ / //  ' \\ / _ \\ / // -_) / /__ _\\ \\ / , _// ___// (_ /
        /___//_//_/_/_// .__//_/ \\__/ /____//___//_/|_|/_/    \\___/
                      /_/
        \nWelcome to Simple SRPG.
        """);
    }

    /**
     * Method to ask from where the user wants to retrieve the data.
     */
    public void askDataSource() {
        System.out.print("""
                Do you want to use your local or cloud data?
                    1) Local data
                    2) Cloud data
                
                -> Answer:\s""");
    }

    /**
     * Method to print loading while the data is being loaded.
     */
    public void printLoading() {
        System.out.println("\nLoading data...");
    }

    /**
     * Method to print the data was successfully loaded after accessing the files where the data is stored.
     */
    public void printLoaded() {
        System.out.println("Data was successfully loaded.");
    }

    /**
     * Method to print the program could not connect to the api server
     */
    public void printAPIError() {
        System.out.println("Couldn’t connect to the remote server.\nReverting to local data.");
        printLoading();
    }

    /**
     * Method to print the main menu and ask for an option.
     */
    public void printMenu() {
        System.out.print("""
                \nThe tavern keeper looks at you and says:
                "Welcome adventurer! How can I help you?"

                \t1) Character creation
                \t2) List characters
                \t3) Create an adventure
                \t4) Start an adventure
                \t5) Exit

                Your answer:\s""");
    }

    /**
     * Method to print the main menu with "start adventure" option disabled and ask for an option.
     */
    public void printMenuDisabled() {
        System.out.print("""
                \nThe tavern keeper looks at you and says:
                "Welcome adventurer! How can I help you?"

                \t1) Character creation
                \t2) List characters
                \t3) Create an adventure
                \t4) Start an adventure (disabled: create 3 characters first)
                \t5) Exit

                Your answer:\s""");
    }

    /**
     * Method to ask for a character {@link CharacterGame} name.
     */
    public void askCharacterName() {
        System.out.print("""
                \nTavern keeper: "Oh, so you are new to this land."
                "What's your name?"
                
                -> Enter your name:\s""");
    }
    /**
     * Method to ask for characters {@link CharacterGame} player name.
     * @param characterName a string containing the character's name.
     */
    public void askPlayerName(String characterName) {
        System.out.print("\nTavern keeper: \"Hello, " +characterName + ", be welcome.\"\n" +
                "\"And now, if I may break the fourth wall, who is your Player?\"\n\n" +
                "-> Enter the player's name: ");
    }
    /**
     * Method to ask for a character's {@link CharacterGame} level
     */
    public void askLevel() {
        System.out.print("""
                \nTavern keeper: "I see, I see..."
                "Now, are you an experienced adventurer?"
                
                -> Enter the character's level [1..10]:\s""");
    }
    /**
     * Method to print the generation of the stats and the stats.
     * @param characterName a string containing the character's {@link CharacterGame} name.
     * @param randomValues an array of integers containing the values obtained after throwing the dices.
     * @param stats an array of integers containing the character's {@link CharacterGame} stats values.
     * @param level an integer containing the character's {@link CharacterGame} level.
     */
    public void printStats(String characterName, int[] randomValues, int[] stats, int level) {
        String[] stat = {"Body", "Mind", "Spirit"};
        int j = 0;
        System.out.println("\nTavern keeper: \"Oh, so you are level " + level + "!\"\n" +
                "\"Great, let me get a closer look at you...\"\n\n" +
                "Generating stats...\n");

        for (int i = 0; i < 3; i++) {
            System.out.println(stat[i] + ":\tYou rolled " + (randomValues[j] + randomValues[j+1]) +
                               " (" + randomValues[j] + " and " + randomValues[j+1] + ").");
            j += 2;
        }

        System.out.println("\nYour stats are:");
        for (int i = 0; i < 3; i++) {
            System.out.println("\s\s- " + stat[i] + ": " + stats[i]);
        }
        System.out.println("\nTavern keeper: \"Looking good!\"\n\"And lastly, ?\"");
    }

    /**
     * Method to ask for a character's class.
     */
    public void askClass() {
        System.out.print("\n-> Enter the character's initial class [Adventurer, Cleric, Wizard]:\s");
    }

    /**
     * Method to print the class has been sucessfully selected and the character has been created.
     * @param characterName name of the character
     * @param className class of the character
     */
    public void printCharacterCreated(String characterName, String className) {
        switch (className) {
            case "Adventurer" -> className = "Warrior";
            case "Cleric" -> className = "Paladin";
            case "Wizard" -> className = "Wizard";
        }
        System.out.println("\nTavern keeper: \"Any decent party needs one of those.\"\n\"I guess that means you're a " +
                className +" by now, nice!\"");
        System.out.println("\nThe new character " + characterName + " has been created.");
    }
    /**
     * Method to ask for a character's {@link CharacterGame} player name to show its characters.
     */
    public void askFilter(){
        System.out.print("""
                Tavern keeper: "Lads! They want to see you!"
                "Who piques your interest?"
                
                -> Enter the name of the Player to filter:\s""");
    }

    /**
     * Method to print a list of the characters {@link CharacterGame} that have a player in common. And ask for one
     * to meet.
     * @param input a string containing the player name.
     * @param adventurers list containing all the characters {@link CharacterGame} instances to be listed.
     */
    public void listAdventurers(String input, List<CharacterGame> adventurers) {
        if (input.equals("")) {
            System.out.println("\nYou watch as all adventurers get up from their chairs and approach you.\n");
        }
        else {
            System.out.println("\nYou watch as some adventurers get up from their chairs and approach you\n");
        }

        listCharacters(adventurers);

        System.out.print("\n  0. Back\n\n" +
                "Who would you like to meet [0.." + adventurers.size() +"]: ");
    }

    /**
     * Method to list a set of characters.
     * @param characters list containing all the characters {@link CharacterGame} instances to be listed.
     */
    public void listCharacters(List<CharacterGame> characters) {
        for (int i = 0; i < characters.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + characters.get(i).getName());
        }
    }

    /**
     * Method to print all the information about a character {@link CharacterGame}.
     * @param character a character object {@link CharacterGame} containing the character whose information is to
     * be printed.
     * @param level an integer containing the level of the character {@link CharacterGame} to be printed.
     */
    public void printCharacter(CharacterGame character, int level) {
        System.out.print("\nTavern keeper: \"Hey " + character.getName() +
                " get here; the boss wants to see you!\"\n\n" +
                "* Name:\t\t" + character.getName() + "\n" +
                "* Player:\t" + character.getPlayer() + "\n" +
                "* Class:\t" + character.getClassType() + "\n" +
                "* Level:\t" + level + "\n" +
                "* XP:\t\t" + character.getXp() + "\n" +
                "* Body:\t\t" + character.getBody() + "\n" +
                "* Mind:\t\t" + character.getMind() + "\n" +
                "* Spirit:\t" + character.getSpirit() + "\n");
    }

    /**
     * Method to ask for the name of the character {@link CharacterGame} to be deleted as confirmation before doing so.
     * @param characterName a string containing the name of the character {@link CharacterGame} to be deleted.
     */
    public void askDeleteCharacter(String characterName) {
        System.out.print("\n[Enter the name to delete, or press enter to cancel]\n" +
                "-> Do you want to delete " + characterName + "? ");
    }

    /**
     * Method to print the deletion message after a character {@link CharacterGame} has been deleted.
     * @param characterName a string with the name of the deleted character {@link CharacterGame}.
     */
    public void printDeleteCharacter(String characterName) {
        System.out.println("\nTavern keeper: \"I'm sorry kiddo, but you have to leave.\"\n\n" +
                "Character " + characterName + " left the Guild.");
    }

    /**
     * Method to ask for an adventure's {@link Adventure} name of the adventure to be created.
     */
    public void askAdventureName() {
        System.out.print("""
                \nTavern keeper: "Planning an adventure? Good luck with that!"
                
                "-> Name your adventure:\s""");
    }

    /**
     * Method to print the adventure {@link Adventure} name and an introduction phrase before asking for the number
     * of encounters.
     * @param name a string containing the adventure {@link Adventure} name.
     */
    public void askEncounters(String name) {
        System.out.print("\nTavern keeper: \"You plan to undertake " + name + ", really?\"\n" +
                "\"How long will it take?\"\n");
    }

    /**
     * Method to ask for the number of encounters in the adventure {@link Adventure} to be created.
     */
    public void askEncountersInput() {
        System.out.print("\n\"-> How many encounters do you want [1..4]: ");
    }

    /**
     * Method to print the number of encounters quote.
     * @param numEncounters an integer containing the number of encounters of an adventure {@link Adventure}.
     */
    public void encounterCreationQuote(int numEncounters) {
        System.out.println("\nTavern keeper: \"" + numEncounters + " encounters? That is too much for me...\"");
    }

    /**
     * Method to show the encounter {@link Encounter} creation menu and ask for an option.
     * @param numEncounters an integer containing the adventure {@link Adventure} number of encounters.
     * @param currEncounter an integer containing the current encounter number.
     * @param numMonsters an integer containing the number of different types of monster {@link Monster}.
     * @param monsters a string list containing the different types of monsters in the encounter {@link Encounter}.
     * @param amount an integer list containing the amounts of monster of each type in the encounter {@link Encounter}.
     */
    public void askEncounterOptions(int numEncounters, int currEncounter, int numMonsters, List<String> monsters,
                                    List<Integer> amount)  {
        System.out.print("\n\n* Encounter " + currEncounter + " / " + numEncounters + "\n" +
                            "* Monsters in encounter");
        if (numMonsters == 0) {
            System.out.print("\n  # Empty");
        }
        else {
            for (int i = 0; i < numMonsters; i++) {
                System.out.print("\n  " + (i + 1) + ". " + monsters.get(i) + " (x" + amount.get(i) + ")");
            }
        }
        System.out.print("""
                \n
                1. Add monster
                2. Remove monster
                3. Continue

                -> Enter an option [1..3]:\s""");
    }

    /**
     * Method to list all the possible monster types and ask for one to choose.
     * @param monsters a list of all the different types of monsters(monster names) {@link Monster}.
     */
    public void listMonsters(List<Monster> monsters) {
        for (int i = 0; i < monsters.size(); i++) {
            System.out.print("\n" + (i + 1) + ". " + monsters.get(i).getName() +
                    " (" + monsters.get(i).getChallenge() + ")");
        }
        System.out.print("\n\n-> Choose a monster to add [1.." + monsters.size() + "]: ");
    }

    /**
     * Method to ask for the amount of the monsters of the type of monster selected.
     * @param monsterName a string containing the monster type(monster names) {@link Monster} selected before.
     */
    public void askMonsterAmount(String monsterName) {
        System.out.print("-> How many "+ monsterName + "(s) do you want to add: ");
    }

    /**
     * Method to ask for a monster {@link Monster} to be deleted from the encounter {@link Encounter}.
     */
    public void askMonsterToDelete() {
        System.out.print("-> Which monster do you want to delete: ");
    }

    /**
     * Method to print the monsters deleted.
     * @param monsterName a string containing the monster type(monster names) {@link Monster} of the monsters deleted.
     * @param amount the number of the monsters of that type that were deleted.
     */
    public void printMonsterDeleted(String monsterName, int amount) {
        System.out.print("\n" + amount + " " + monsterName + " were removed from the encounter.");
    }

    /**
     * Method to list all the adventures available.
     * @param adventures a list of adventure (monster names) {@link Adventure} instances containing all the adventures
     * in the system.
     */
    public void listAdventures(List<Adventure> adventures) {
        System.out.println("""
                \nTavern keeper: "So, you are looking to go on an adventure?"
                "Where do you fancy going?"
                
                Available adventures:""");
        for (int i = 0; i < adventures.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + adventures.get(i).getName());
        }
        System.out.print("\n-> Choose an adventure: ");
    }

    /**
     * Method to ask for the size of the party that will go through the adventure.
     * @param adventureName a string containing the name of the adventure {@link Adventure}.
     */
    public void askPartySize(String adventureName) {
        System.out.print("\nTavern keeper: \"" + adventureName + " it is!\"\n" +
                "\"And how many people shall join you?\"\n\n" +
                "-> Choose a number of characters [3..5]: ");
    }

    /**
     * Method to print the party size quote and an introduction quote before showing all the available
     * characters{@link CharacterGame}.
     * @param partySize and integer containing the party size.
     */
    public void printPartySize(int partySize) {
        System.out.println("\nTavern keeper: \"Great, " + partySize + " it is.\"\n" +
                "\"Who among these lads shall join you?\"");
    }

    /**
     * Method to print your party members, all the available characters{@link CharacterGame} and ask for a character
     * to join the party.
     * @param partySize an integer with the party size.
     * @param characters a list of characters {@link CharacterGame} instances containing all the characters available.
     * @param party a list of characters {@link CharacterGame} instances containing all the characters in the party.
     */
    public void askMemberToJoin(int partySize, List<CharacterGame> characters, List<CharacterGame> party) {
        System.out.print("\n\n------------------------------\n" +
                "Your party (" + party.size() + " / " + partySize + "):\n");
        for (int i = 0; i < partySize; i++) {
            if (i >= party.size()) {
                System.out.println("  " + (i + 1) + ". Empty");
            }
            else {
                System.out.println("  " + (i + 1) + ". " + party.get(i).getName());
            }
        }
        System.out.println("------------------------------\n" +
                "Available characters:");
        listCharacters(characters);
        System.out.print("\n-> Choose character " + (party.size() + 1) + " in your party: ");
    }

    /**
     * Method to print your party members and the initialization of the adventure.
     * @param party a list of characters {@link CharacterGame} instances containing all the characters in the party.
     * @param adventureName a string containing the name of the adventure {@link Adventure} to be started.
     */
    public void startAdventure(List<CharacterGame> party, String adventureName) {
        System.out.print("\n\n------------------------------\n" +
                "Your party (" + party.size() + " / " + party.size() + "):\n");
        listCharacters(party);
        System.out.println("------------------------------");
        System.out.println("\nTavern keeper: \"Great, good luck with your adventure lads!\"\n\n\n" +
                "The \"" + adventureName + "\" will start soon...");
    }

    /**
     * Method to print the initialization of an encounter{@link Encounter}.
     * @param encounter an encounter{@link Encounter} object containing the encounter to be started.
     * @param currEncounter an integer containing the number of the current encounter.
     */
    public void encounterInitialization(Encounter encounter, int currEncounter) {
        System.out.println("\n---------------------\n" +
                "Starting encounter " + currEncounter + ":");
        for (int i = 0; i < encounter.getAmount().size(); i++) {
            System.out.println("  - " + encounter.getAmount().get(i) + "x " + encounter.getMonsterTypes().get(i));
        }
        System.out.print("""
                ---------------------
                                
                                
                -------------------------
                *** Preparation stage ***
                -------------------------
                """);
    }

    /**
     * Method to print the preparation stage actions and the initiative order.
     * @param logActions a string containing all the actions performed by the party characters during the preparation
     * stage.
     * @param logInitiative a string containing the initiative order.
     */
    public void printPreparationStage(String logActions, String logInitiative) {
        print(logActions);
        print(logInitiative);
    }

    /**
     * Method to print all the actions performed by the party members{@link CharacterGame} and the
     * monsters{@link Monster} of an encounter{@link Encounter}.
     * @param log a string containing all the actions performed during the stage by either monsters {@link Monster}
     * or characters{@link CharacterGame}.
     */
    public void printCombatStage(String log) {
        System.out.print("""
                
                                           
                -------------------
                *** Combat stage ***
                -------------------""");
        print(log);
    }

    /**
     * Method to print the rest stage actions and the experienced gained by the party members{@link CharacterGame}.
     * @param log a string containing the actions performed during this stage and the experience gained.
     */
    public void printRestStage(String log) {
        System.out.println("""
                ------------------------
                *** Short rest stage ***
                ------------------------""");
        print(log);
    }

    /**
     * Method to print a victory message after completing an adventure{@link Adventure}.
     * @param adventureName a string containing the adventure {@link Adventure} name of the adventure completed.
     */
    public void printVictory(String adventureName) {
        System.out.println("\n\nCongratulations, your party completed \"" + adventureName + "\"\n");
    }

    /**
     * Method to print a defeat message upon TPU(total party unconscious) in an adventure{@link Adventure}.
     */
    public void printPartyDefeated() {
        System.out.println("""

                Tavern keeper: "Lad, wake up. Yes, your party fell unconscious."
                "Don't worry, you are safe back at the Tavern."
                """);
    }

    /**
     * Method to print an error message when a file is not found.
     * @param file a string containing the file name of the file not found.
     */
    public void printFileNotFound(String file) {
        System.out.println("Error: The " + file + " file can't be accessed.");
    }

    /**
     * Method to print a message.
     * @param s string with the message to be printed.
     */
    public void print(String s) {
        System.out.print(s);
    }

    /**
     * Method to print an error quote upon selecting a character{@link CharacterGame} to join your party when
     * it is already in.
     * @param name a string containing the name of the character {@link CharacterGame} selected.
     */
    public void printAlreadySelected(String name){
        System.out.println("\nTavern keeper: \"I'm sorry, but " + name + " is already in your party.\"");
    }

    /**
     * Method to print an error quote upon selecting more than 1 boss{@link Monster} per encounter{@link Encounter}.
     */
    public void printInvalidBossAmount() {
        System.out.println("\nTavern keeper: \"I'm sorry, but you can't have more than 1 boss per encounter.\"");
    }

    /**
     * Method to print an error quote when selecting a non-existent player{@link CharacterGame}.
     */
    public void printNoCharacters() {
        System.out.println("\nTavern keeper: \"I'm sorry, but there is not such a player.\"");
    }

    /**
     * Method to print an exit quote.
     */
    public void printExit() {
        System.out.println("\nTavern keeper: \"Are you leaving already? See you soon, adventurer.\"");
    }

    /**
     * Method to print an error quote upon writing an invalid name{@link CharacterGame}.
     */
    public void printInvalidName() {
        System.out.println("\nTavern keeper: \"I'm sorry, but that name is invalid.\"");
    }

    /**
     * Method to print an error quote upon selecting an invalid option.
     */
    public void printInvalidOption() {
        System.out.println("\nTavern keeper: \"I'm sorry, I didn't understand you.\"");
    }
}
