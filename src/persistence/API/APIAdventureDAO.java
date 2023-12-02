package persistence.API;

import business.entities.Adventure;
import business.entities.Characters.CharacterGame;
import business.entities.Encounter;
import business.entities.Monsters.Boss;
import business.entities.Monsters.Monster;
import com.google.gson.Gson;
import persistence.AdventureDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is to store and read adventure instances {@link Adventure} information through an API.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class APIAdventureDAO implements AdventureDAO {
    /**
     * A gson instance to read the information from the API
     */
    private final Gson gson;

    /**
     * Constructor to create a APIAdventureDAO.
     */
    public APIAdventureDAO() {
        gson = new Gson();
    }

    /**
     * Method to get the data of adventures through the api.
     * @return List of adventures instances.
     */
    @Override
    public List<Adventure> read() {
        List<Adventure> adventures = new ArrayList<>();
        String URL = "https://balandrau.salle.url.edu/dpoo/S1_Project_ICE9/adventures";
        String response = null;

        try {
            ApiHelper api = new ApiHelper();
            response = api.getFromUrl(URL);
            Adventure[] adventureArr = gson.fromJson(response, Adventure[].class);

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
        } catch (IOException e) {
            return null;
        }

        return adventures;
    }

    /**
     * Method to post new adventures through the api.
     * @param adventures List of adventures instances.
     */
    @Override
    public void write(List<Adventure> adventures) {
        String URL = "https://balandrau.salle.url.edu/dpoo/S1_Project_ICE9/adventures";
        String body = gson.toJson(adventures.get(adventures.size() - 1));

        try {
            ApiHelper api = new ApiHelper();
            api.postToUrl(URL, body);
        } catch (IOException Ignored) {}
    }
}
