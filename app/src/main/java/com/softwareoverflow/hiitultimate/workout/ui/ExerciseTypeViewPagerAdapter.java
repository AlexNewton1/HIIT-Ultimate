package com.softwareoverflow.hiitultimate.workout.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwareoverflow.hiitultimate.R;
import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;

import java.util.List;

public class ExerciseTypeViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<ExerciseTypeEntity> exerciseTypes;

    // The size of the ViewPager
    private int width, height;

    public ExerciseTypeViewPagerAdapter(Context context, List<ExerciseTypeEntity> exerciseTypes,
                                        int width, int height){
        this.context = context;
        this.exerciseTypes = exerciseTypes;

        this.width = width;
        this.height = height;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View layout = LayoutInflater.from(context).inflate(
                R.layout.pager_adapter_exercise_type, container, false);
        ExerciseTypeEntity exerciseType = exerciseTypes.get(position);

        FloatingActionButton exerciseTypeFab = layout.findViewById(R.id.fab_exerciseTypeIcon);
        int fabSize = (int) (Math.min(height, width) * 0.75);
        exerciseTypeFab.setCustomSize(fabSize);
        exerciseTypeFab.setScaleType(ImageView.ScaleType.CENTER);
        TextView nameTextView = layout.findViewById(R.id.tv_exerciseTypeName);

        // Set the relevant values

        exerciseTypeFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(exerciseType.getColorHex())));
        exerciseTypeFab.setImageResource(exerciseType.getIconId());
        nameTextView.setText(exerciseType.getName());

//        RoundedImageView imageView = layout.findViewById(R.id.fab_exerciseTypeIcon);
//        imageView.setBackgroundColor(Color.parseColor(exerciseType.getColorHex()));
//        imageView.setImageResource(exerciseType.getIconId());

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return exerciseTypes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public ExerciseTypeEntity getItemAtPosition(int position){
        return exerciseTypes.get(position);
    }
}
