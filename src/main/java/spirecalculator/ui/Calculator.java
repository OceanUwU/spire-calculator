package spirecalculator.ui;

import basemod.BaseMod;
import basemod.interfaces.PreUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import spirecalculator.SpireCalculator;

import java.util.ArrayList;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Calculator implements RenderSubscriber, PreUpdateSubscriber {
    private static Texture background = ImageMaster.loadImage("spirecalculator/calculator.png");
    private static Texture display = ImageMaster.loadImage("spirecalculator/display.png");
    public static int width = 300, height = 500;
    private static int displayWidth = 264, displayHeight = 76;
    private static ScriptEngineManager manager = new ScriptEngineManager();
    private static ScriptEngine engine = manager.getEngineByName("js");
    private static BitmapFont font = FontHelper.cardTypeFont;

    private Hitbox hb;
    private float x, y;
    public ArrayList<Button> buttons = new ArrayList<>();
    private String expression = "";
    private float startX, startY;
    public Color color;

    public Calculator(float xLoc, float yLoc, Color calcColor) {
        x = xLoc - (width / 2);
        y = yLoc - height;
        color = calcColor;
        hb = new Hitbox(x, y, width, height);

        int index = 0;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 4; column++) {
                Button button = new Button(13+(70*column), 20+(70*row), index++, this);
                button.move(x, y);
                buttons.add(button);
            }
        }

        CloseButton closeButton = new CloseButton(this);
        closeButton.move(x, y);
        buttons.add(closeButton);

        BaseMod.subscribe(this);
    }

    public void close() {
        for (Button button : buttons) {
            button.remove();
        }
        buttons.clear();
        SpireCalculator.calculators.remove(this);
        BaseMod.unsubscribeLater(this);
    }

    public void addToExpr(String text) {
        if (expression.contains("="))
            expression = "";
        expression += text;
    }

    public void evaluate() {
        try {
            if (!expression.contains("="))
                expression += "=" + engine.eval(expression);
        } catch(Exception e) {
            e.printStackTrace();
            expression += "=" + "ERR";
        }
    }

    public void pressButton(int type) {
        switch(type) {
            case 0: case 3:
            case 4: case 5: case 6: case 7:
            case 8: case 9: case 10: case 11:
            case 12: case 13: case 14: case 15:
            case 16: case 17:
                addToExpr(Button.texts.get(type));
                break;
            case 1: addToExpr(".");
                break;
            case 18:
                if (expression.length() > 0)
                    expression = expression.substring(0, expression.length()-1);
                break;
            case 19: expression = "";
                break;
            case 2: evaluate();
                break;
            case 20: close();
                break;
        }
    }

    public void receivePreUpdate() {
        hb.update();
        if (hb.hovered && InputHelper.justClickedLeft) {
            startX = InputHelper.mX;
            startY = InputHelper.mY;
            hb.clickStarted = true;
        }
        if (hb.clickStarted) {
            x += InputHelper.mX - startX;
            y += InputHelper.mY - startY;
            hb.translate(x, y);
            startX = InputHelper.mX;
            startY = InputHelper.mY;
            for (Button button : buttons) {
                button.move(x, y);
            }
        }
    }

    public void receiveRender(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(background, x, y);
        sb.draw(display, x+13, y+height-112);
        for (Button button : buttons) {
            sb.setColor(color);
            button.render(sb, color);
        }

        if (expression.length() > 0) {
            font.getData().setScale(Math.min(displayWidth / FontHelper.getWidth(font, expression, 1.0f) * 2.5f, 3.0f));
            FontHelper.renderFontRightAligned(sb, font, expression, x+width-18, y+height-69, Color.BLACK);
        }
    }
}