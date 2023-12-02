package business;

import business.entities.Characters.Adventurer;
import business.entities.Characters.Cleric;
import business.entities.Characters.Wizard;
import persistence.API.APICharacterDAO;
import persistence.CharacterDAO;
import persistence.JSON.JSONCharacterDAO;
import business.entities.Characters.CharacterGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is to manage all the logic regarding an characters{@link CharacterGame}.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class CharacterManager {
    /**
     * Instance of the JSONCharacterDAO class {@link JSONCharacterDAO} to talk with the persistence layer.
     */
    private CharacterDAO cDao;

    /**
     * Constructor to create a character manager.
     */
    public CharacterManager() {
        this.cDao = new JSONCharacterDAO();
    }

    /**
     * Setter method to set the interface of the characterDAO {@link CharacterDAO} to be used.
     */
    public void setCharacterDAO(int option) {
        if (option == 1) {
            this.cDao = new JSONCharacterDAO();
        }
        else {
            this.cDao = new APICharacterDAO();
        }
    }

    /**
     * Method to validate if an inputted name is valid or not to be a character{@link CharacterGame} name and make
     * the proper modifications.
     * @param name string containing the name to be analyzed.
     * @return the name with the proper modifications if it is valid. 1 if it is not valid.
     */
    public String validateName(String name) {
        String[] splitName = name.split("\\s+");
        String newName = "";
        char letter;

        for (int i = 0; i < splitName.length; i++) {
            if (!splitName[i].matches("[A-zÀ-ú]+")) {
                return "1";
            }
            if (i == 0) {
                letter = splitName[i].charAt(0);
                letter = Character.toUpperCase(letter);
                newName =  letter + splitName[i].substring(1);
            }
            else {
                letter = splitName[i].charAt(0);
                letter = Character.toUpperCase(letter);
                newName = newName + " " + letter + splitName[i].substring(1);
            }
        }
        if (isUniqueName(newName)) {return newName;}
        else {return "1";}
    }

    /**
     * Method to check if an inputted character{@link CharacterGame} name is unique or not.
     * @param name string containing the name to be analyzed.
     * @return true if it is unique, false if it is not.
     */
    public boolean isUniqueName(String name) {
        List<CharacterGame> characters = new ArrayList<>(cDao.read());

        for (CharacterGame character : characters) {
            if (character.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to check if an inputted character{@link CharacterGame} name is unique or not. Compared to a given list
     * of characters.
     * @param name string containing the name to be analyzed.
     * @param characters a list of character {@link CharacterGame} instances to which the name inputted is going
     * to be compared.
     * @return true if it is unique, false if it is not.
     */
    public boolean isUniqueName(String name, List<CharacterGame> characters) {
        for (CharacterGame character : characters) {
            if (character.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to calculate the xp a character {@link CharacterGame} has based on the level it has.
     * @param level integer containing a value representing the level.
     * @return an integer representing the xp the level corresponds to.
     */
    public int calculateXp(int level) {
        int xp = 0;
        switch (level) {
            case 1 -> xp = 0;
            case 2 -> xp = 100;
            case 3 -> xp = 200;
            case 4 -> xp = 300;
            case 5 -> xp = 400;
            case 6 -> xp = 500;
            case 7 -> xp = 600;
            case 8 -> xp = 700;
            case 9 -> xp = 800;
            case 10 -> xp = 900;
        }
        return xp;
    }

    /**
     * Method to calculate the level a character {@link CharacterGame} has based on the xp it has.
     * @param xp integer containing a value representing the xp.
     * @return an integer representing the level the xp corresponds to.
     */
    public int calculateLevel(int xp) {
        int level = 1;
        if (xp >= 0 && xp < 100) {level = 1;}
        else if (xp >= 100 && xp < 200) {level = 2;}
        else if (xp >= 200 && xp < 300) {level = 3;}
        else if (xp >= 300 && xp < 400) {level = 4;}
        else if (xp >= 400 && xp < 500) {level = 5;}
        else if (xp >= 500 && xp < 600) {level = 6;}
        else if (xp >= 600 && xp < 700) {level = 7;}
        else if (xp >= 700 && xp < 800) {level = 8;}
        else if (xp >= 800 && xp < 900) {level = 9;}
        else if (xp >= 900) {level = 10;}
        return level;
    }

    /**
     * Method to throw two six sides dice for each stat of a character{@link CharacterGame}.
     * @return an array of integer values representing the value each dice got.
     */
    public int[] generateStats() {
        int[] randomValues = new int[6];
        for (int i = 0; i < 6; i++) {
            randomValues[i] = new Random().nextInt(6) + 1;
        }
        return randomValues;
    }

    /**
     * Method to calculate each stat value based on the sum of the 2 dice thrown for each character
     * {@link CharacterGame} stat.
     * @param randomValues integer array with the values of the 2 dice thrown for each stat.
     * @return an array of integer values representing the value of each stat(body, mind, spirit).
     */
    public int[] calculateStats(int[] randomValues) {
        int[] stats = new int[3];
        int j = 0;
        for (int i = 0; i < 3; i++) {
            switch (randomValues[j] + randomValues[j + 1]) {
                case 2 -> stats[i] = -1;
                case 3,4,5 -> stats[i] = 0;
                case 6,7,8,9 -> stats[i] = 1;
                case 10,11 -> stats[i] = 2;
                case 12 -> stats[i] = 3;
            }
            j += 2;
        }
        return stats;
    }

    /**
     * Method to create a character{@link CharacterGame} and persist it.
     * @param name string containing the character {@link CharacterGame} name.
     * @param player string containing the name of the player who owns the character{@link CharacterGame}.
     * @param level integer containing the level of the character{@link CharacterGame}.
     * @param stats array of integer values representing each stat value(body, mind, spirit).
     */
    public void createCharacter(String name, String player, int level, int[] stats, String classType) {
        List<CharacterGame> characters = new ArrayList<>(cDao.read());
        CharacterGame character = null;
        switch (classType) {
            case "Adventurer" -> character = new Adventurer(name, player, calculateXp(level), stats[0], stats[1], stats[2], "Adventurer");
            case "Cleric" -> character = new Cleric(name, player, calculateXp(level), stats[0], stats[1], stats[2], "Cleric");
            case "Wizard" -> character = new Wizard(name, player, calculateXp(level), stats[0], stats[1], stats[2], "Wizard");
        }
        characters.add(character);
        cDao.write(characters);
    }

    /**
     * Method to remove a character {@link CharacterGame} from the character list and persist it.
     * @param characterName string containing the name of the character {@link CharacterGame} to be removed.
     * @param source integer containing the characters that is going to be deleted.
     */
    public void deleteCharacter(String characterName, int source) {
        List<CharacterGame> characters = new ArrayList<>(cDao.read());

        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getName().equals(characterName)) {
                if (source == 2) {
                    cDao.deleteCharacter(characters.get(i));
                }
                else if (source == 1) {
                    characters.remove(i);
                    cDao.write(characters);
                }
            }
        }
    }

    /**
     * Method to get all the character{@link CharacterGame} instances stored in the system.
     * @return a list containing all the character {@link CharacterGame} instances.
     */
    public List<CharacterGame> getCharacters() {
        return cDao.read();
    }

    /**
     * Method to get all the character {@link CharacterGame} instances owned by the same player.
     * @param input string containing the name of the player or part of it.
     * @return a lits of the character {@link CharacterGame} instances owned by that player.
     */
    public List<CharacterGame> getCharacters(String input) {
        List<CharacterGame> characters = new ArrayList<>(cDao.read());
        List<CharacterGame> adventurers = new ArrayList<>();

        if (input.equals("")) {
            adventurers = characters;
        } else {
            for (CharacterGame character : characters) {
                if (character.getPlayer().equalsIgnoreCase(input) ||
                        character.getPlayer().toLowerCase().contains(input.toLowerCase())) {
                    adventurers.add(character);
                }
            }
        }

        return adventurers;
    }

    /**
     * Method to check the number of character {@link CharacterGame} instances stored in the system.
     * @return an integer value representing the number of character {@link CharacterGame} instances it has.
     */
    public int checkNumCharacters() {return cDao.read().size();}

    /**
     * Method with the logic regarding the actions each character {@link CharacterGame} instance will do during the
     * preparation stage.
     * @param party list of character {@link CharacterGame} instances participating in an adventure.
     * @return a string containing the action each character{@link CharacterGame} did and its result.
     */
    public String preparationStage(List<CharacterGame> party) {
        StringBuilder log = new StringBuilder();
        for (CharacterGame character : party) {
            log.append(character.preparationAction(party));
        }
        return log.toString();
    }

    /**
     * Method to calculate the initiative of each character {@link CharacterGame} of the party.
     * @param party list of character {@link CharacterGame} instances participating in an adventure.
     */
    public void rollInitiative(List<CharacterGame> party) {
        for (CharacterGame characterGame : party) {
            characterGame.calculateInitiative();
        }
    }

    /**
     * Method to sort the party members{@link CharacterGame} in the order in which they will attack.
     * @param party list of character {@link CharacterGame} instances participating in an adventure.
     */
    public void sortInitiative(List<CharacterGame> party) {
        party.sort((o1, o2) -> o2.getInitiative() - o1.getInitiative());
    }

    /**
     * Method to calculate the maximum hit points of each character {@link CharacterGame} of the party and initialize
     * its hit points.
     * @param party list of character {@link CharacterGame} instances participating in an adventure.
     */
    public void initializeHitPoints(List<CharacterGame> party) {
        for (CharacterGame characterGame : party) {
            characterGame.calculateMaxHitPoints(calculateLevel(characterGame.getXp()));
            characterGame.setHitPoints(characterGame.getMaxHitPoints());
        }
    }

    /**
     * Method to check if the party is alive or not.
     * @param party list of character {@link CharacterGame} instances participating in an adventure.
     * @return true if at least one character {@link CharacterGame} of the party is alive, false if none is alive.
     */
    public boolean partyIsAlive(List<CharacterGame> party) {
        for (CharacterGame character : party) {
            if (character.getHitPoints() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to add the experience points gained after an encounter to each member of the party and update its level
     * if necessary.
     * @param party list of character {@link CharacterGame} instances participating in an adventure.
     * @param source persistence source where the characters {@link CharacterGame} are stored.
     */
    public void updateLevel(List<CharacterGame> party, int source) {
        List<CharacterGame> characters = new ArrayList<>(getCharacters());
        for (CharacterGame partyMember : party) {
            for (CharacterGame characterGame : characters) {
                if (partyMember.getName().equals(characterGame.getName())) {
                    characters.set(characters.indexOf(characterGame), partyMember);
                }
            }
        }
        if (source == 1) {
            cDao.write(characters);
        } else {
            for (int i = 0; i < party.size(); i++) {
                cDao.updateCharacter(party.get(i));
            }
        }
    }
}
