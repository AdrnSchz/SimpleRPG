package persistence.API;

import business.entities.Adventure;
import business.entities.Monsters.Boss;
import business.entities.Monsters.Monster;
import com.google.gson.Gson;
import persistence.MonsterDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is to store and read monster instances {@link Monster} information through an API.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class APIMonsterDAO implements MonsterDAO {

    /**
     * Gson instance.
     */
    private final Gson gson;

    /**
     * Constructor method.
     */
    public APIMonsterDAO() {
        gson = new Gson();
    }

    /**
     * Method to get the monsters information through the API.
     * @return List of monsters.
     */
    @Override
    public List<Monster> read() {
        List<Monster> monsters = new ArrayList<>();
        String URL = "https://balandrau.salle.url.edu/dpoo/shared/monsters";
        String response = null;

        try {
            ApiHelper api = new ApiHelper();
            response = api.getFromUrl(URL);
            Monster[] monsterArr = gson.fromJson(response, Monster[].class);

            for (int i = 0; i < monsterArr.length; i++) {
                if (monsterArr[i].getChallenge().equals("Boss")) {
                    monsters.add(new Boss(monsterArr[i].getName(), monsterArr[i].getChallenge(), monsterArr[i].getExperience(),
                            monsterArr[i].getHitPoints(), monsterArr[i].getInitiative(), monsterArr[i].getDamageDice(),
                            monsterArr[i].getDamageType()));
                }
                else {
                    monsters.add(new Monster(monsterArr[i].getName(), monsterArr[i].getChallenge(), monsterArr[i].getExperience(),
                            monsterArr[i].getHitPoints(), monsterArr[i].getInitiative(), monsterArr[i].getDamageDice(),
                            monsterArr[i].getDamageType()));
                }
            }
        } catch (IOException e) {
            return null;
        }

        return monsters;
    }
}
