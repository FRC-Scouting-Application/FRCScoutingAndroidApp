package ca.tnoah.customviews;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import org.w3c.dom.Text;

import ca.tnoah.frc.scouting.R;

public class Counter extends ConstraintLayout {

    private final TextView labelTV;
    private final TextView valueTV;

    private final AppCompatImageButton incrementButton;
    private final AppCompatImageButton decrementButton;

    private int value = 0;

    public Counter(Context context) {
        this(context, null);
    }

    public Counter(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Counter,
                0, 0
        );

        View view = init(context);

        labelTV = view.findViewById(R.id.counterLabel);
        valueTV = view.findViewById(R.id.counterValue);
        incrementButton = view.findViewById(R.id.counterAdd);
        decrementButton = view.findViewById(R.id.counterSubtract);

        incrementButton.setOnClickListener((l) -> increment());
        decrementButton.setOnClickListener((l) -> decrement());
    }

    private View init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_counter, this, false);
        addView(view);

        return view;
    }

    public CharSequence getLabel() {
        return labelTV.getText();
    }

    public void setLabel(CharSequence text) {
        labelTV.setText(text);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        valueTV.setText(String.valueOf(value));
    }

    public void increment() {
        this.value++;
        setValue(value);
    }

    public void decrement() {
        this.value--;
        setValue(value);
    }

}