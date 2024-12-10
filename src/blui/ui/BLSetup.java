package blui.ui;

import arc.*;
import arc.func.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import blui.*;
import blui.scene.ui.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class BLSetup{
    private static Element bluiTable;

    private static Element getBluiTable(){
        if(bluiTable != null) return bluiTable;

        BLVars.init();
        BLStyles.init();

        //Already added to UI.
        Element e = ui.hudGroup.find("blui");
        if(e != null && e.getClass().getName().contains("BLUITable")){
            return bluiTable = e;
        }

        //Create for the first time. Also add setting here.
        ui.settings.game.sliderPref("blui-longpress", 30, 15, 180, 15, s -> Strings.autoFixed(s / 60f, 2) + " " + StatUnit.seconds.localized());

        BLUITable all = new BLUITable();
        all.bottom().left();
        all.name = "blui";
        all.setOrigin(Align.bottomLeft);
        ui.hudGroup.addChild(all);

        settings.put("blui-table-class", BLUITable.class.getName());

        return bluiTable = all;
    }

    public static void addTable(Cons<Table> t){
        addTable(t, null);
    }

    public static void addTable(Cons<Table> t, Boolp visible){
        Element bluiTable = getBluiTable();
        Class<?> bluiTableClass = SafeReflect.clazz(settings.getString("blui-table-class"));

        Seq<Cons<Table>> tables = SafeReflect.get(bluiTableClass, bluiTable, "tables");
        if(tables == null) return;
        tables.add(t);

        if(visible == null) return;
        ObjectMap<Cons<Table>, Boolp> visibles = SafeReflect.get(bluiTableClass, bluiTable, "visibles");
        if(visibles == null) return;
        visibles.put(t, visible);
    }

    /**
     * Offsets the position of a table in the bottom-left corner of the screen
     * to account for the command mode button on mobile being there.
     */
    public static void offset(Table table){
        if(mobile) table.moveBy(0f, Scl.scl(46f));
    }

    private static class BLUITable extends Table{
        private final Seq<Cons<Table>> tables = new Seq<>();
        private final ObjectMap<Cons<Table>, Boolp> visibles = new ObjectMap<>();
        private final Table cont = new Table();
        private int current = -1;
        private boolean folded;

        public BLUITable(){
            defaults().bottom().left();

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

            Events.on(ClientLoadEvent.class, e -> {
                next();
                offset(this);
            });
        }

        private void next(){
            if(tables.isEmpty()) return;

            current = (current + 1) % tables.size;
            Cons<Table> table = tables.get(current);
            if(tableVisible(table)){
                clearTable();
                cont.defaults().bottom().left();
                table.get(cont);
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
            if(tables.any() && !tableVisible(tables.get(current)) && hasVisible()) next();
        }

        private boolean hasVisible(){
            return tables.contains(this::tableVisible);
        }

        private boolean tableVisible(Cons<Table> table){
            return !visibles.containsKey(table) || visibles.get(table).get();
        }
    }
}
