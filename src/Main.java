import business.AdventureManager;
import business.CharacterManager;
import business.EncounterManager;
import presentation.UiController;
import presentation.UiManager;

public class Main {
    public static void main(String[] args) {
        CharacterManager cm = new CharacterManager();
        AdventureManager am = new AdventureManager();
        EncounterManager em = new EncounterManager();
        UiManager ui = new UiManager();
        UiController uiC = new UiController(cm, am, em, ui);

        uiC.run();

    }
}
