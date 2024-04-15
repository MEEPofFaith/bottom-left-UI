package blui;

import arc.*;
import arc.util.*;
import blui.ui.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import mindustry.world.meta.*;

public class BottomLeftUI extends Mod{

    public BottomLeftUI(){
    }

    @Override
    public void init(){
        BLVars.init();
        Vars.ui.settings.game.sliderPref("blui-longpress", 30, 15, 180, 15, s -> Strings.autoFixed(s / 60f, 2) + " " + StatUnit.seconds.localized());

        Core.app.post(BLSetup::init); //Wait until mods add tables
    }
}
