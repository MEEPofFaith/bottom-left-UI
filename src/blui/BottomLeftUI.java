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
        BLSetup.addTable(t -> t.label(() -> "test1").wrapLabel(false));
        BLSetup.addTable(t -> t.label(() -> "test2222222").wrapLabel(false));
        BLSetup.addTable(t -> t.label(() -> "test3333333333333333333").wrapLabel(false));

        BLSetup.init();
    }
}
