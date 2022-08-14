package spirecalculator.ui;

import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Texture;
import spirecalculator.SpireCalculator;

public class PanelItem extends TopPanelItem {
    private static final Texture IMG = new Texture("spirecalculator/icon.png");
    public static final String ID = "calculator:CalculatorPanelItem";

    public PanelItem() {
	    super(IMG, ID);
    }

    @Override
    protected void onClick() {
        SpireCalculator.openCalculator(this.x + (hb_w / 2) + (int)(Math.random() * 21) - 10, this.y - 100 + (int)(Math.random() * 21) - 10);
    }
}