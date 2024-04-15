package blui;

import arc.*;

public class BLVars{
    public static float pressTimer;
    public static float longPress = 30;
    public static float iconSize = 40f, buttonSize = 24f, sliderWidth = 140f, fieldWidth = 80f;

    protected static void init(){
        longPress = Core.settings.getInt("blui-longpress", 30);
    }
}
