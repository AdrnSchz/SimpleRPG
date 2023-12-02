package persistence;

import business.entities.Adventure;

import java.util.List;

/**
 * Interface to manage the data of the adventures.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public interface AdventureDAO {

    /**
     * Method to get the data of adventures stored in the persistence source.
     * @return List of adventures instances.
     */
    List<Adventure> read();

    /**
     * Method to save new adventures in the persistence source.
     * @param adventures List of adventures instances.
     */
    void write(List<Adventure> adventures);
}
