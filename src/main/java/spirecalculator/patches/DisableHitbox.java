package spirecalculator.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.Hitbox;

import spirecalculator.ui.Button;

@SpirePatch(clz=Hitbox.class, method="update", paramtypez={})
public class DisableHitbox {
    public static void Postfix(Hitbox __instance) {
        if (Button.anyPressed)
            __instance.clicked = false;
    }
}