package persistence.JSON;

import business.entities.Adventure;
import business.entities.Monsters.Boss;
import business.entities.Monsters.Monster;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import persistence.MonsterDAO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is to read monster instances {@link Monster} information from a json file.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class JSONMonsterDAO implements MonsterDAO {
    /**
     * A gson instance to read the information from the json file
     */
    private final Gson gson;

    /**
     * Constructor to create a JSONMonsterDAO.
     */
    public JSONMonsterDAO() { this.gson = new Gson(); }

    /**
     * A method to read the monster {@link Monster} instances from a json file.
     * @return a list of monster instances {@link Monster} if the file is found. In case is not found, returns null.
     */
    public List<Monster> read() {
        try {
            JsonArray array = JsonParser.parseReader(new FileReader("data/monsters.json")).getAsJsonArray();

            Monster[] monsterArr = gson.fromJson(array, Monster[].class);

            List<Monster> monsters = new ArrayList<>(monsterArr.length);

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

            return monsters;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}