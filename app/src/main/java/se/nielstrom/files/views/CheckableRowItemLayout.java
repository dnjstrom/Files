package se.nielstrom.files.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by Daniel on 2014-07-18.
 */
public class CheckableRowItemLayout extends LinearLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private boolean checked = false;

    public CheckableRowItemLayout(Context context) {
        super(context);
        init();
    }

    public CheckableRowItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckableRowItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //setBackgroundColor(Color.YELLOW);
    }

    @Override
    public void setChecked(boolean checked) {

        if (checked) {
           // setBackgroundColor(Color.BLUE);
        } else {
         //   setBackgroundColor(Color.YELLOW);
        }

        this.checked = checked;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }

        return drawableState;
    }
}
