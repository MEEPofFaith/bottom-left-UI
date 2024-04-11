package blui.ui;

import arc.*;
import arc.func.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import blui.*;
import blui.scene.ui.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.Mods.*;

import java.util.*;

import static mindustry.Vars.mobile;
import static mindustry.Vars.ui;

public class BLSetup{
    private static ArrayList<TableData> tables = new ArrayList<>();
    private static boolean init;
    private static Table cont = new Table();
    private static int current = -1;
    private static boolean folded;

    public static void addTable(Cons<Table> t){
        tables.add(new TableData(t));
    }

    public static void addTable(Cons<Table> t, Boolp visible){
        tables.add(new TableData(t, visible));
    }

    public static void offset(Table table){
        table.moveBy(0f, Scl.scl(mobile ? 46f : 0f)); //Account for command mode button on mobile.
    }

    public static void init(){
        if(!init){
            init = true;
            BLStyles.init();

            Table all = new Table().bottom().left();
            all.setOrigin(Align.bottomLeft);
            all.defaults().bottom().left();
            all.add(cont);
            next();

            all.table(Tex.buttonEdge3, t -> {
                HoldImageButton b = new HoldImageButton(Icon.refresh, BLStyles.bluiHoldImageStyle);
                b.clicked(() -> {
                    if(folded){
                        folded = false;
                        current--;
                    }
                    next();
                });
                b.held(() -> {
                    clear();
                    folded = true;
                });
                b.canHold(() -> !folded);
                b.resizeImage(BLVars.iconSize);

                b.getStyle().imageHeld = Icon.leftOpen;
                b.update(() -> b.getStyle().imageUp = folded ? Icon.rightOpen : Icon.refresh);

                t.add(b);
            });
            ui.hudGroup.addChild(all);
            offset(all);

            Events.on(WorldLoadEndEvent.class, e -> {
                if(!tables.get(current).visible()){
                    next();
                }
            });
        }
    }

    private static void next(){
        current = (current + 1) % tables.size();
        TableData table = tables.get(current);
        if(table.visible()){
            clear();
            table.table.get(cont);
        }else{
            next();
        }
    }

    private static void clear(){
        cont.clear();
        cont.background(null);
    }

    private static class TableData{
        public Boolp visible;
        public Cons<Table> table;

        public TableData(Cons<Table> table, Boolp visible){
            this.table = table;
            this.visible = visible;
        }

        public TableData(Cons<Table> table){
            this.table = table;
        }

        public boolean visible(){
            return visible == null || visible.get();
        }
    }
}
