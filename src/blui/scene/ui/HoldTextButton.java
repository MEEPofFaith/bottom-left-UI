package blui.scene.ui;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.util.*;
import blui.*;

public class HoldTextButton extends TextButton{
    private Runnable held = () -> {
    };
    private boolean heldAct;
    private HoldTextButtonStyle style;
    private boolean repeat = false;
    private boolean hasReset;

    public HoldTextButton(String text){
        this(text, Core.scene.getStyle(HoldTextButtonStyle.class));
    }

    public HoldTextButton(String text, HoldTextButtonStyle style){
        super(text, style);
    }

    @Override
    public void setStyle(Button.ButtonStyle style){
        if(style == null){
            throw new NullPointerException("style cannot be null");
        }else if(!(style instanceof HoldTextButtonStyle s)){
            throw new IllegalArgumentException("style must be a TextButtonStyle.");
        }else{
            super.setStyle(style);
            this.style = s;
            if(label != null){
                Label.LabelStyle labelStyle = this.label.getStyle();
                labelStyle.font = s.font;
                labelStyle.fontColor = s.fontColor;
                this.label.setStyle(labelStyle);
            }

        }
    }

    public void draw(){
        Color fontColor;
        if(isDisabled() && style.disabledFontColor != null){
            fontColor = style.disabledFontColor;
        }else if(isHeld() && style.heldFontColor != null){
            fontColor = style.heldFontColor;
        }else if(isPressed() && style.downFontColor != null){
            fontColor = style.downFontColor;
        }else if(isChecked() && style.checkedFontColor != null){
            fontColor = isOver() && style.checkedOverFontColor != null ? style.checkedOverFontColor : style.checkedFontColor;
        }else if(isOver() && style.overFontColor != null){
            fontColor = style.overFontColor;
        }else{
            fontColor = style.fontColor;
        }

        if(fontColor != null){
            label.getStyle().fontColor = fontColor;
        }

        Button.super.draw();
    }

    @Override
    public ClickListener clicked(Cons<ClickListener> tweaker, final Cons<ClickListener> runner){
        ClickListener click;
        addListener(click = new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                if(runner != null && !heldAct){
                    runner.get(this);
                }
            }
        });
        tweaker.get(click);
        addReset(); //Make sure click happens before reset
        return click;
    }

    @Override
    public void act(float delta){
        super.act(delta);

        if(isPressed()){
            BLVars.pressTimer += Time.delta;
            if(BLVars.pressTimer > BLVars.longPress && (repeat || !heldAct)){
                heldAct = true;
                held.run();
            }
        }
    }

    public boolean isHeld(){
        return isPressed() && BLVars.pressTimer > BLVars.longPress;
    }

    public void addReset(){
        if(hasReset) return;

        released(() -> {
            Log.info("reset");
            heldAct = false;
            BLVars.pressTimer = 0;
        });

        hasReset = true;
    }

    public boolean repeat(){
        return repeat;
    }

    public void setRepeat(boolean repeat){
        this.repeat = repeat;
    }

    public static class HoldTextButtonStyle extends TextButtonStyle{
        public Color heldFontColor;

        public HoldTextButtonStyle(){
        }

        public HoldTextButtonStyle(Drawable up, Drawable down, Drawable checked, Font font){
            super(up, down, checked, font);
        }

        public HoldTextButtonStyle(TextButtonStyle style){
            super(style);
        }

        public HoldTextButtonStyle(HoldTextButtonStyle style){
            super(style);
            if(style.heldFontColor != null){
                this.heldFontColor = new Color(style.heldFontColor);
            }
        }
    }
}
