package com.softwareoverflow.hiit_trainer.ui

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import kotlin.math.abs

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


fun getWorkoutPrepSet(context: Context): WorkoutSetDTO = WorkoutSetDTO(
    ExerciseTypeDTO(
        null,
        context.getString(R.string.get_ready),
        "icon_heart_pulse",
        "#FF000000"
    ),
    5, // TODO - maybe allow this to be configurable
    0,
    1,
    0
)

fun getWorkoutCompleteExerciseType(context: Context): ExerciseTypeDTO = ExerciseTypeDTO(
    null,
    context.getString(R.string.workout_complete),
    "icon_trophy",
    "#FF000000"
)

fun String.getDrawableId(context: Context) =
    context.resources.getIdentifier(this, "drawable", context.packageName)

fun String.getColorId() = Color.parseColor(this)

fun <T> List<T>.takeN(n: Int): List<T> {
    return when {
        n < 0 -> this.takeLast(abs(n))
        n > 0 -> this.take(n)
        else -> emptyList()
    }
}

fun hideKeyboard(activity: Activity) {
    val inputManager =
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    val currentlyFocusedView = activity.currentFocus
    if (currentlyFocusedView != null)
        inputManager.hideSoftInputFromWindow(
            currentlyFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
}