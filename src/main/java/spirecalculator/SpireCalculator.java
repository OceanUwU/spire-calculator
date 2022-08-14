package spirecalculator;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import spirecalculator.ui.Calculator;
import spirecalculator.ui.PanelItem;

import java.util.ArrayList;

@SpireInitializer
public class SpireCalculator implements PostInitializeSubscriber {
    public static ArrayList<Calculator> calculators = new ArrayList<>();

    public static void initialize() {
        BaseMod.subscribe(new SpireCalculator());
    }

    public void receivePostInitialize() {
        BaseMod.addTopPanelItem(new PanelItem());
    }

    public static void openCalculator(float x, float y) {
        calculators.add(new Calculator(x, y));
    }

    public static void closeCalculators() {
        while (calculators.size() > 0) {
            calculators.get(0).close();
        }
    }

    @SpirePatch(
        clz=MainMenuScreen.class,
        method=SpirePatch.CONSTRUCTOR,
        paramtypez={boolean.class}
    )
    public static class MainMenuPatch {
        public static void Postfix() {
            closeCalculators();
        }
    }
}