package blui;

import arc.*;
import arc.util.*;
import blui.ui.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

public class BottomLeftUI extends Mod{

    public BottomLeftUI(){
    }

    @Override
    public void init(){
        Core.app.post(BLSetup::init); //Wait until mods add tables
    }
}
