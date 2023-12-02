package persistence.API;

import business.entities.Adventure;
import business.entities.Characters.*;
import com.google.gson.Gson;
import persistence.CharacterDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is to store and read character instances {@link CharacterGame} information through an API.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class APICharacterDAO implements CharacterDAO {

    /**
     * Gson instance.
     */
    private final Gson gson;

    /**
     * Constructor method.
     */
    public APICharacterDAO() {
        gson = new Gson();
    }

    /**
     * Method to get the characters through the api.
     * @return List of characters instances.
     */
    @Override
    public List<CharacterGame> read() {
        List<CharacterGame> characters = new ArrayList<>();
        String URL = "https://balandrau.salle.url.edu/dpoo/S1_Project_ICE9/characters";
        String response;

        try {
            ApiHelper api = new ApiHelper();
            response = api.getFromUrl(URL);
            CharacterGame[] characterArr = gson.fromJson(response, Adventurer[].class);
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
        } catch (IOException e) {
            return null;
        }

        return characters;
    }

    /**
     * Method save new characters through the API.
     * @param characters List of characters instances.
     */
    @Override
    public void write(List<CharacterGame> characters) {
        String URL = "https://balandrau.salle.url.edu/dpoo/S1_Project_ICE9/characters";
        String body = gson.toJson(characters.get(characters.size() - 1));

        try {
            ApiHelper api = new ApiHelper();
            api.postToUrl(URL, body);
        } catch (IOException Ignored) {}
    }

    /**
     * Method to delete a character.
     * @param character Character instance to be deleted.
     */
    @Override
    public void deleteCharacter(CharacterGame character) {
        String URL = "https://balandrau.salle.url.edu/dpoo/S1_Project_ICE9/characters?name=" + character.getName();

        try {
            ApiHelper api = new ApiHelper();
            api.deleteFromUrl(URL);
        } catch (IOException Ignored) {}
    }

    /**
     * Method to update a character.
     * @param character Character instance to be updated.
     */
    @Override
    public void updateCharacter(CharacterGame character) {
        String URL_Delete = "https://balandrau.salle.url.edu/dpoo/S1_Project_ICE9/characters?name=" + character.getName();
        String URL_POST = "https://balandrau.salle.url.edu/dpoo/S1_Project_ICE9/characters";
        String body = gson.toJson(character);

        try {
            ApiHelper api = new ApiHelper();
            api.deleteFromUrl(URL_Delete);
            api.postToUrl(URL_POST, body);
        } catch (IOException Ignored) {}
    }
}
