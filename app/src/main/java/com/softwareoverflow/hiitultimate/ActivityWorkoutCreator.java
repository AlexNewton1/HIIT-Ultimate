package com.softwareoverflow.hiitultimate;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import com.softwareoverflow.hiitultimate.database.dto.Workout;

import java.util.List;

public class ActivityWorkoutCreator extends AppCompatActivity implements View.OnClickListener {

    LiveData<Workout> workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_creator);

        // TODO - parse any workout from the Bundle (loading workout)

        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.done_button).setOnClickListener(this);
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        IWizardFragment fragment = (IWizardFragment) getVisibleFragment();
        assert fragment != null;

        switch(view.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.done_button:
                fragment.onWizardStepComplete();
                break;
        }
    }

    // TODO - handle back presses to alert user to possible loss of unsaved data
}
