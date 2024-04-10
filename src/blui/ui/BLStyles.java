package blui.ui;

import arc.*;
import arc.graphics.*;
import arc.scene.ui.ImageButton.*;
import arc.util.*;
import blui.scene.ui.HoldImageButton.*;
import mindustry.ui.*;

public class BLStyles{
    public static ImageButtonStyle
        bluiImageStyle;
    public static HoldImageButtonStyle
        defaultHoldi,
        bluiHoldImageStyle;

    protected static void init(){
        bluiImageStyle = new ImageButtonStyle(Styles.logici){{
            down = Styles.flatDown;
            over = Styles.flatOver;
            imageDisabledColor = Color.gray;
            imageUpColor = Color.white;
        }};

        defaultHoldi = new HoldImageButtonStyle(Styles.defaulti);
        Core.scene.addStyle(HoldImageButtonStyle.class, defaultHoldi);

        bluiHoldImageStyle = new HoldImageButtonStyle(bluiImageStyle);
    }
}
