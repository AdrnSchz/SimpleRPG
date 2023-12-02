package business.entities.Characters;

import business.entities.Monsters.Monster;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Random;

/**
 * Abstract class to represent the general behaviour and traits of a character entity.
 *
 * @author Adrián Sánchez
 * @version 1.0 28/06/2023
 */
public abstract class CharacterGame {
    // name of the character.
    @Expose()
    private final String name;
    // name of the player who owns the character.
    @Expose()
    private final String player;
    //number of experience points that the character has.
    @Expose()
    private int xp;
    //stat "body" of the character.
    @Expose()
    private int body;
    // stat "mind" of the character.
    @Expose()
    private int mind;
    // stat "spirit" of the character.
    @Expose()
    private int spirit;
    // class of the character.
    @Expose()
    @SerializedName("class")
    private final String classType;
    // value to indicate the order in which character will attack.
    private int initiative;
    // maximum health points.
    private int maxHitPoints;
    // current health points.
    private int hitPoints;
    // amount of a stat buffed during preparation stage
    private int buff;
    // amount of shield the character has.
    private int shield = 0;

    /**
     * Constructor with the parameters to create a character.
     * @param name a string containing the name of the character.
     * @param player a string containing the name of the player who owns the character.
     * @param xp an integer containing the number of experience points that the character has.
     * @param body an integer representing the stat "body" of the character.
     * @param mind an integer representing the stat "mind" of the character.
     * @param spirit an integer representing the stat "spirit" of the character.
     * @param classType a string containing the class of the character.
     */
    public CharacterGame(String name, String player, int xp, int body, int mind, int spirit, String classType) {
        this.name = name;
        this.player = player;
        this.xp = xp;
        this.body = body;
        this.mind = mind;
        this.spirit = spirit;
        this.classType = classType;
    }

    /**
     * Method to get the name of the character.
     * @return a string containing the name of the character.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the name of the player who owns the character.
     * @return a string containing the name of the player.
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Method to get the number of experience points.
     * @return an integer containing the number of experience points the character has.
     */
    public int getXp() {
        return xp;
    }

    /**
     * Method to set the experience points of the character.
     * @param xp integer with the number of experience points it has.
     */
    public void setXp(int xp) {this.xp = xp;}

    /**
     * Method to add experience points to the current number of points it has.
     * @param xp integer with the number of experience points to be added.
     */
    public void addXp(int xp) {this.xp += xp;}

    /**
     * Method to get the value of the stat "body".
     * @return an integer with the value of the stat "body".
     */
    public int getBody() {
        return body;
    }

    /**
     * Method to get the value of the stat "mind".
     * @return an integer with the value of the stat "mind".
     */
    public int getMind() {
        return mind;
    }

    /**
     * Method to get the value of the stat "spirit".
     * @return an integer with the value of the stat "spirit".
     */
    public int getSpirit() {
        return spirit;
    }

    /**
     * Method to get the class of the character.
     * @return a string containing the class the character has.
     */
    public String getClassType() {
        return classType;
    }

    /**
     * Method to get the value of initiative the character has.
     * @return an integer with the initiative value of the character.
     */
    public int getInitiative() {
        return initiative;
    }

    /**
     * Method to get the maximum number of heat points.
     * @return an integer containing the number of maximum hit points.
     */
    public int getMaxHitPoints() {return maxHitPoints;}

    /**
     * Method to get the number of current hit points.
     * @return an integer with the number of current hit points.
     */
    public int getHitPoints() {return hitPoints;}

    /**
     * Method to set the current hit points to the given value.
     * @param hitPoints integer containing the hit points value to which the character hit points will be set.
     */
    public void setHitPoints(int hitPoints) {this.hitPoints = hitPoints;}

    /**
     * Method to add the given value to the current hit points.
     * @param hitPoints integer containing the hit points value to be added to the current hit points.
     */
    public void addToHitPoints(int hitPoints) {
        this.hitPoints += hitPoints;
        if (this.hitPoints > maxHitPoints) {
            this.hitPoints = maxHitPoints;
        }

        if (this.hitPoints < 0) {
            this.hitPoints = 0;
        }
    }

    /**
     * Method to set the maximum hit points to the given value.
     * @param hitPoints integer with the level the character is.
     */
    public void setMaxHitPoints(int hitPoints) {this.maxHitPoints = hitPoints;}

    /**
     * Method to add the given value to the current spirit.
     * @param spirit integer containing the value to be added to the current spirit.
     */
    public void addToSpirit(int spirit) {this.spirit += spirit;}

    /**
     * Method to add the given value to the current mind.
     * @param mind integer containing the value to be added to the current mind.
     */
    public void addToMind(int mind) {this.mind += mind;}

    /**
     * Method to set the initiative to the given value.
     */
    public void setInitiative(int initiative) {this.initiative = initiative;}

    /**
     * Method to check if the character is alive or not
     * @return true if it is alive, false if it is not.
     */
    public boolean isAlive() {return this.hitPoints > 0;}

    /**
     * Method to get the amount of buff given
     * @return the amount of buff given
     */
    public int getBuff() {return buff;}

    /**
     * Method to set the amount of buff given
     * @param buff integer containing the amount of buff given
     */
    public void setBuff(int buff) {this.buff = buff;}

    /**
     * Method to get the amount of shield the character has
     * @return the amount of shield the character has
     */
    public int getShield() {return this.shield;}

    /**
     * Method to set the amount of shield the character will have
     * @param shield integer containing the amount of shield the character will have
     */
    public void setShield(int shield) {this.shield = shield;}

    /**
     * Method to substract the damage taken from the shield it has
     * @param damage integer containing the amount of damage taken
     */
    public int subtractFromShield(int damage) {
        this.shield -= damage;
        if (this.shield < 0) {
            return this.shield;
        }
        return 0;
    }

    /**
     * Method to get the level of the character
     * @return the level the character is
     */
    public int getLevel() {
        int level = 1;
        if (xp >= 0 && xp < 100) {level = 1;}
        else if (xp >= 100 && xp < 200) {level = 2;}
        else if (xp >= 200 && xp < 300) {level = 3;}
        else if (xp >= 300 && xp < 400) {level = 4;}
        else if (xp >= 400 && xp < 500) {level = 5;}
        else if (xp >= 500 && xp < 600) {level = 6;}
        else if (xp >= 600 && xp < 700) {level = 7;}
        else if (xp >= 700 && xp < 800) {level = 8;}
        else if (xp >= 800 && xp < 900) {level = 9;}
        else if (xp >= 900) {level = 10;}
        return level;
    }




    // Methods to override by other classes




    /**
     * Method for the character to execute its action during the preparation stage.
     * @param party list containing the characters of the party.
     * @return a string containing the action performed and its result.
     */
    public String preparationAction(List<CharacterGame> party) {
        this.spirit += 1;
        return (this.name + " uses Self-Motivated. Their Spirit is increased by +1.\n");
    }

    /**
     * Method to set his stats at the end of the rest stage to the value prior to the support action.
     * @param party list containing the characters of the party.
     */
    public void unbuff(List<CharacterGame> party) {
        this.spirit -= 1;
    }

    /**
     * Method to initialize the initiative.
     */
    public void calculateInitiative() {this.initiative = ((new Random().nextInt(12) + 1) + this.spirit);}

    /**
     * Method to calculate and set the maximum hit points of the character.
     * @param level integer with the level the character is.
     */
    public void calculateMaxHitPoints(int level) {this.maxHitPoints = (this.body + 10) * level;}

    /**
     * Method to subtract the damage taken to the current hit points.
     * @param damage integer containing the hit points value the attack received is going to deal
     * @return an integer containing the damage taken.
     */
    public int takeDamage(int damage, String attackType) {
        this.hitPoints -= damage;
        if (this.hitPoints < 0) {
            this.hitPoints = 0;
        }

        return damage;
    }

    /**
     * Method to get the monster {@link Monster} the character is going to attack.
     * @param monsters list of monster {@link Monster} instances.
     * @param party list of character {@link CharacterGame} instances.
     * @return an integer representing the index the monster {@link Monster} instance which is going to be attacked.
     */
    public int getTarget(List<Monster> monsters, List<CharacterGame> party) {
        int lowestHpMonster = 0;
        for (int i = 1; i < monsters.size(); i++) {
            if (monsters.get(i).getHitPoints() < monsters.get(lowestHpMonster).getHitPoints()) {
                lowestHpMonster = i;
            }
        }
        return lowestHpMonster;
    }

    /**
     * Method to get the type of damage the character class deals.
     * @return a string containing the damage types.
     */
    public String getAttackType() {
        return "physical";
    }

    /**
     * Method to get the attack action the character class does.
     * @return a string array containing the attack actions.
     */
    public String[] getAttackAction() {
        return new String[]{"Sword slash"};
    }

    /**
     * Method to calculate the damage the character attack is going to do.
     * @param monsters list of monster {@link Monster} instances.
     * @param target integer with the index of the monster {@link Monster} instance which is going to be attacked.
     * @param multiplier integer with the multiplier of the attack.
     * @param party list of character {@link CharacterGame} instances.
     * @return a string array with the number of hit points the attack is going to deal and extra information if needed.
     */
    public String[] attack(List<Monster> monsters, int target, int multiplier, List<CharacterGame> party) {
        int damage = multiplier * ((new Random().nextInt(6) + 1) + this.body);
        monsters.get(target).takeDamage(damage, this.getAttackType());

        String[] damageInfo = new String[1];
        damageInfo[0] = Integer.toString(damage);
        return damageInfo;
    }

    /**
     * Method for the character to carry out its action during the rest stage.
     * @param party list containing the characters of the party.
     * @return a string containing the action performed and its result.
     */
    public String restAction(List<CharacterGame> party) {
        int heal = new Random().nextInt(8) + 1;
        this.hitPoints += heal + this.mind;

        if(this.hitPoints > this.maxHitPoints) {
            this.hitPoints = this.maxHitPoints;
        }
        return (this.name + " uses Bandage time. Heals " + (heal + this.mind) + " hit points.\n");
    }

    /**
     * Method to check if it can evolve
     * @return the class to which it will evolve if can evolve, "No" if it can't.
     */
    public String canEvolve() {
        if (this.xp >= 100) {
            return "Warrior";
        }
        else {
            return "No";
        }
    }
}