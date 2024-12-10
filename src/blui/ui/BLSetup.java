package blui.ui;

import arc.func.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import blui.*;
import blui.scene.ui.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class BLSetup{
    private static BLUITable bluiTable;

    private static BLUITable getBluiTable(){
        if(bluiTable != null) return bluiTable;

        //Already added to UI.
        Element e = ui.hudGroup.find("blui");
        if(e instanceof BLUITable t){
            return bluiTable = t;
        }

        //Create for the first time. Also add setting here.
        Vars.ui.settings.game.sliderPref("blui-longpress", 30, 15, 180, 15, s -> Strings.autoFixed(s / 60f, 2) + " " + StatUnit.seconds.localized());

        BLUITable all = new BLUITable();
        all.bottom().left();
        all.name = "blui";
        all.setOrigin(Align.bottomLeft);
        all.defaults().bottom().left();
        ui.hudGroup.addChild(all);

        return bluiTable = all;
    }

    public static void addTable(Cons<Table> t){
        getBluiTable().tables.add(new TableData(t));
    }

    public static void addTable(Cons<Table> t, Boolp visible){
        getBluiTable().tables.add(new TableData(t, visible));
    }

    /**
     * Offsets the position of a table in the bottom-left corner of the screen
     * to account for the command mode button on mobile being there.
     */
    public static void offset(Table table){
        if(mobile) table.moveBy(0f, Scl.scl(46f));
    }

    private static class BLUITable extends Table{
        private final Seq<TableData> tables = new Seq<>();
        private final Table cont = new Table();
        private int current = -1;
        private boolean folded;

        public BLUITable(){
            add(cont);
            table(Tex.buttonEdge3, t -> {
                HoldImageButton b = new HoldImageButton(Icon.refresh, BLStyles.bluiHoldImageStyle);
                b.clicked(() -> {
                    if(folded){
                        folded = false;
                        current--;
                    }
                    next();
                });
                b.held(() -> {
                    clearTable();
                    folded = true;
                });
                b.canHold(() -> !folded);
                b.resizeImage(BLVars.iconSize);

                b.getStyle().imageHeld = Icon.leftOpen;
                b.update(() -> b.getStyle().imageUp = folded ? Icon.rightOpen : Icon.refresh);

                t.add(b);
            }).update(t -> checkVisibility());
            visible(() -> ui.hudfrag.shown && !ui.minimapfrag.shown() && hasVisible());
        }

        private void next(){
            if(tables.isEmpty()) return;

            current = (current + 1) % tables.size;
            TableData table = tables.get(current);
            if(table.visible()){
                clearTable();
                cont.defaults().bottom().left();
                table.table.get(cont);
            }else{
                next();
            }
        }

        private void clearTable(){
            cont.clear();
            cont.background(null);
        }

        /** If current table is not visible, switch to next one. */
        private void checkVisibility(){
            if(tables.any() && !tables.get(current).visible() && hasVisible()) next();
        }

        private boolean hasVisible(){
            return tables.contains(TableData::visible);
        }
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
