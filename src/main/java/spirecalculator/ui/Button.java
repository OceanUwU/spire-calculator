package spirecalculator.ui;

import basemod.BaseMod;
import basemod.interfaces.PreUpdateSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class Button implements PreUpdateSubscriber {
    private static Texture texture = ImageMaster.loadImage("spirecalculator/button.png");
    private static int width = 64, height = 64;
    public static ArrayList<String> texts = new ArrayList<>(Arrays.asList(
        "0", "•", "=", "+", "1", "2", "3", "-", "4", "5", "6", "*", "7", "8", "9", "/", "(", ")", "<", "C", "x"
    ));
    private static BitmapFont font = FontHelper.cardTypeFont;
    public static Color hoveredMult = new Color(0.9F, 0.9F, 0.9F, 1.0F);
    public static Color pressedMult = new Color(0.7F, 0.7F, 0.7F, 1.0F);

    public Hitbox hb;
    public float x, y, xOffset, yOffset;
    private int type;
    private String text;
    public Calculator calculator;

    public Button(float newXOffset, float newYOffset, int buttonType, Calculator setCalculator) {
        xOffset = newXOffset;
        yOffset = newYOffset;
        type = buttonType;
        text = texts.get(type);
        calculator = setCalculator;
        hb = new Hitbox(x, y, width, height);

        BaseMod.subscribe(this);
    }

    public void remove() {
        BaseMod.unsubscribeLater(this);
    }
    
    public void move(float newX, float newY) {
        x = newX + xOffset;
        y = newY + yOffset;
        hb.translate(x, y);
    }

    
    public void receivePreUpdate() {
        hb.update();
        if (hb.hovered && InputHelper.justClickedLeft)
            hb.clickStarted = true;
        if (hb.clicked) {
            calculator.pressButton(type);
            hb.clicked = false;
        }
    }

    public void render(SpriteBatch sb, Color color) {
        if (hb.clickStarted)
            sb.setColor(calculator.color.cpy().mul(pressedMult));
        else if (hb.hovered)
            sb.setColor(calculator.color.cpy().mul(hoveredMult));
        sb.draw(texture, x, y);

        font.getData().setScale(2.5f);
        FontHelper.renderFontCentered(sb, font, text, x + (width/2), y + (height/2), Color.BLACK);
    }
}