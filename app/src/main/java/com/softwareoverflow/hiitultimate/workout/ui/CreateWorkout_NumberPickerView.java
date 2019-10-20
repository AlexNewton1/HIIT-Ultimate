package com.softwareoverflow.hiitultimate.workout.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.softwareoverflow.hiitultimate.R;

public class CreateWorkout_NumberPickerView extends ConstraintLayout {

    private EditText numberPickerET;

    public CreateWorkout_NumberPickerView(Context context) {
        super(context);
        setup(context, null);
    }

    public CreateWorkout_NumberPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public CreateWorkout_NumberPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }


    private void setup(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.partial_create_workout_number_picker, this);

        numberPickerET = view.findViewById(R.id.editText_partialView_createWorkout);
        TextView labelTV = view.findViewById(R.id.textView_partialView_createWorkout);

        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CreateWorkout_NumberPickerView,
                0,
                0
        );

        try {
            int defaultValue = attributes.getInt(
                    R.styleable.CreateWorkout_NumberPickerView_numberPicker_defaultValue, 0);
            numberPickerET.setText(String.valueOf(defaultValue));
            CharSequence label = attributes.getText(R.styleable.CreateWorkout_NumberPickerView_numberPicker_label);
            labelTV.setText(label);

            invalidate();
            requestLayout();

            Log.d("debugger3", "Setting up view: " + defaultValue + ", " + label);
        } finally {
            attributes.recycle();
        }
    }

    /**
     * @return the integer value shown in the edit text
     * @throws NumberFormatException when the value cannot be cast to an integer (e.g an empty value)
     */
    public int getValue() throws NumberFormatException{
        return Integer.valueOf(numberPickerET.getText().toString());
    }
}
