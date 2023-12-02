package persistence.JSON;

import business.entities.Adventure;
import business.entities.Characters.CharacterGame;
import business.entities.Encounter;
import business.entities.Monsters.Boss;
import business.entities.Monsters.Monster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import persistence.AdventureDAO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is to store and read adventure instances {@link Adventure} information from a json file.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class JSONAdventureDAO implements AdventureDAO {
    /**
     * A gson instance to read the information from the json file
     */
    private final Gson gson;

    /**
     * Constructor to create a JSONAdventureDAO.
     */
    public JSONAdventureDAO() { this.gson = new Gson(); }

    /**
     * A method to read the adventure {@link Adventure} instances from a json file.
     * @return a list of adventure instances {@link Adventure} if the file is found. In case is not found,
     * returns null.
     */
    public List<Adventure> read() {
        try {
            JsonArray array = JsonParser.parseReader(new FileReader("data/adventures.json")).getAsJsonArray();

            Adventure[] adventureArr = gson.fromJson(array, Adventure[].class);

            List<Adventure> adventures = new ArrayList<>(adventureArr.length);

            for (int i = 0; i < adventureArr.length; i++) {
                List<Encounter> encounters = new ArrayList<>();
                for (int j = 0; j < adventureArr[i].getNumEncounters(); j++) {
                    Encounter encounter = adventureArr[i].getEncounters().get(j);
                    List<Monster> monsters = new ArrayList<>();
                    for (int k = 0; k < adventureArr[i].getEncounters().get(j).getMonsters().size(); k++) {
                        Monster monster = adventureArr[i].getEncounters().get(j).getMonsters().get(k);
                        if (monster.getChallenge().equals("Boss")) {
                            monsters.add(new Boss(monster.getName(), monster.getChallenge(), monster.getExperience(),
                                    monster.getHitPoints(), monster.getInitiative(), monster.getDamageDice(), monster.getDamageType()));
                        } else {
                            monsters.add(new Monster(monster.getName(), monster.getChallenge(), monster.getExperience(),
                                    monster.getHitPoints(), monster.getInitiative(), monster.getDamageDice(), monster.getDamageType()));
                        }
                    }
                    encounters.add(new Encounter(encounter.getMonsterTypes(), encounter.getAmount(), monsters));
                }
                adventures.add(new Adventure(adventureArr[i].getName(), adventureArr[i].getNumEncounters(), encounters));
            }

            return adventures;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * A method to store character {@link Adventure} instances in a json file.
     * The IOException is ignored as it previously checked that the file is found.
     * @param adventures a list of adventure {@link Adventure} instances to be written in the file.
     */
    public void write(List<Adventure> adventures) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(adventures);
        try (FileWriter writer = new FileWriter("data/adventures.json")) {
            writer.write(json);
        } catch (IOException e) {
        }
    }
}
