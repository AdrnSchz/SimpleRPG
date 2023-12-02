package business.entities;

import java.util.List;

/**
 * Class to represent an adventure entity.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public class Adventure {
    // a string containing the name of the adventure.
    private final String name;

    // an integer containing the number of encounters.
    private final int numEncounters;

    /**
     * a list of encounter {@link Encounter} instances.
     */
    private final List<Encounter> encounters;

    /**
     * Constructor with the parameters to create an Adventure entity.
     * @param name a string containing the name of the adventure.
     * @param numEncounters an integer containing the number of encounters.
     * @param encounters a list of encounter {@link Encounter} instances.
     */
    public Adventure(String name, int numEncounters, List<Encounter> encounters) {
        this.name = name;
        this.numEncounters = numEncounters;
        this.encounters = encounters;
    }

    /**
     * Method to get the name of the adventure.
     * @return a string containing the name of the adventure.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the number of encounters of the adventure.
     * @return an integer containing the number of encounters in the adventure.
     */
    public int getNumEncounters() {
        return numEncounters;
    }

    /**
     * Method to get the encounters {@link Encounter} in the adventure.
     * @return a list with the encounter {@link Encounter} instances of the adventure.
     */
    public List<Encounter> getEncounters() {
        return encounters;
    }
}
