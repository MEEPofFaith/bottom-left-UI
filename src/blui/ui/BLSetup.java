package blui.ui;

import arc.*;
import arc.func.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import blui.*;
import blui.scene.ui.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class BLSetup{
    private static final Seq<TableData> tables = new Seq<>();
    private static final Table cont = new Table();
    private static boolean init;
    private static int current = -1;
    private static boolean folded;

    public static void addTable(Cons<Table> t){
        tables.add(new TableData(t));
    }

    public static void addTable(Cons<Table> t, Boolp visible){
        tables.add(new TableData(t, visible));
    }

    /**
     * Offsets the position of a table in the bottom-left corner of the screen
     * to account for the command mode button on mobile being there.
     */
    public static void offset(Table table){
        if(mobile) table.moveBy(0f, Scl.scl(46f));
    }

    public static void init(){
        if(!init){
            init = true;
            BLStyles.init();

            Table all = new Table().bottom().left();
            all.name = "blui";
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
            }).update(t -> checkVisibility());
            all.visible(BLSetup::visible);

            ui.hudGroup.addChild(all);
            offset(all);

            Events.on(WorldLoadEndEvent.class, e -> {
                if(visible()) checkVisibility();
            });
        }
    }

    /** If current table is not visible, switch to next one. */
    private static void checkVisibility(){
        if(tables.any() && !tables.get(current).visible() && hasVisible()) next();
    }

    private static boolean visible(){
        return ui.hudfrag.shown && !ui.minimapfrag.shown() && hasVisible();
    }

    private static boolean hasVisible(){
        return tables.contains(TableData::visible);
    }

    private static void next(){
        if(tables.isEmpty()) return;

        current = (current + 1) % tables.size;
        TableData table = tables.get(current);
        if(table.visible()){
            clear();
            cont.defaults().bottom().left();
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
