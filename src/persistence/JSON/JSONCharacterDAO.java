package persistence.JSON;

import business.entities.Adventure;
import business.entities.Characters.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import persistence.CharacterDAO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is to store and read character instances {@link CharacterGame} information from a json file.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class JSONCharacterDAO implements CharacterDAO {
    /**
     * A gson instance to read the information from the json file
     */
    private final Gson gson;

    /**
     * Constructor to create a JSONCharacterDAO.
     */
    public JSONCharacterDAO() { this.gson = new Gson(); }

    /**
     * A method to read the character {@link CharacterGame} instances from a json file.
     * @return a list of character instances {@link CharacterGame} if the file is found. In case is not found,
     * returns null.
     */
    public List<CharacterGame> read() {
        try {
            JsonArray array = JsonParser.parseReader(new FileReader("data/characters.json")).getAsJsonArray();

            CharacterGame[] characterArr = gson.fromJson(array, Adventurer[].class);

            List<CharacterGame> characters = new ArrayList<>(characterArr.length);

            for (int i = 0; i < characterArr.length; i++) {
                switch (characterArr[i].getClassType()) {
                    case "Adventurer" -> characters.add(new Adventurer(characterArr[i].getName(), characterArr[i].getPlayer(),
                            characterArr[i].getXp(), characterArr[i].getBody(), characterArr[i].getMind(), characterArr[i].getSpirit(),
                            characterArr[i].getClassType()));
                    case "Warrior" -> characters.add(new Warrior(characterArr[i].getName(), characterArr[i].getPlayer(),
                            characterArr[i].getXp(), characterArr[i].getBody(), characterArr[i].getMind(), characterArr[i].getSpirit(),
                            characterArr[i].getClassType()));
                    case "Champion" -> characters.add(new Champion(characterArr[i].getName(), characterArr[i].getPlayer(),
                            characterArr[i].getXp(), characterArr[i].getBody(), characterArr[i].getMind(), characterArr[i].getSpirit(),
                            characterArr[i].getClassType()));
                    case "Cleric" -> characters.add(new Cleric(characterArr[i].getName(), characterArr[i].getPlayer(),
                            characterArr[i].getXp(), characterArr[i].getBody(), characterArr[i].getMind(), characterArr[i].getSpirit(),
                            characterArr[i].getClassType()));
                    case "Paladin" -> characters.add(new Paladin(characterArr[i].getName(), characterArr[i].getPlayer(),
                            characterArr[i].getXp(), characterArr[i].getBody(), characterArr[i].getMind(), characterArr[i].getSpirit(),
                            characterArr[i].getClassType()));
                    case "Wizard" -> characters.add(new Wizard(characterArr[i].getName(), characterArr[i].getPlayer(),
                            characterArr[i].getXp(), characterArr[i].getBody(), characterArr[i].getMind(), characterArr[i].getSpirit(),
                            characterArr[i].getClassType()));

                }
            }

            return characters;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * A method to store character {@link CharacterGame} instances in a json file.
     * The IOException is ignored as it previously checked that the file is found.
     * @param characters a list of character {@link CharacterGame} instances to be written in the file.
     */
    public void write(List<CharacterGame> characters) {
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.setPrettyPrinting().create();
        String json = gson.toJson(characters);
        try (FileWriter writer = new FileWriter("data/characters.json")) {
            writer.write(json);
        } catch (IOException ignored) {}
    }

    @Override
    public void deleteCharacter(CharacterGame character) {}

    @Override
    public void updateCharacter(CharacterGame character) {}
}
