package persistence;

import business.entities.Characters.CharacterGame;

import java.util.List;

/**
 * Interface to manage the data of the characters.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public interface CharacterDAO {

    /**
     * Method to get the characters.
     * @return List of characters instances.
     */
    List<CharacterGame> read();

    /**
     * Method save new characters in the persistence source.
     * @param characters List of characters instances.
     */
    void write(List<CharacterGame> characters);

    /**
     * Method to delete a character.
     * @param character Character instance to be deleted.
     */
    void deleteCharacter(CharacterGame character);

    /**
     * Method to update a character.
     * @param character Character instance to be updated.
     */
    void updateCharacter(CharacterGame character);
}
