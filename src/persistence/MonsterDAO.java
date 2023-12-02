package persistence;

import business.entities.Monsters.Monster;

import java.util.List;

/**
 * Interface to manage the data of the monsters.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public interface MonsterDAO {
    /**
     * Method to read the monsters from the peristence source.
     *
     * @return List of monsters.
     */
    List<Monster> read();
}
