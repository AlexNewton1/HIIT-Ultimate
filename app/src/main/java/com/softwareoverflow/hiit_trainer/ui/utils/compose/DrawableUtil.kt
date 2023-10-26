package com.softwareoverflow.hiit_trainer.ui.utils.compose

import android.content.Context
import androidx.annotation.DrawableRes
import com.softwareoverflow.hiit_trainer.R


@DrawableRes
fun getDrawableId(name: String, context: Context): Int {
    return context.resources.getIdentifier(
        name,
        "drawable",
        context.packageName
    )
}

val etIcons = listOf(
    R.drawable.et_icon_flash,
    R.drawable.et_icon_heart,
    R.drawable.et_icon_dumbbell,
    R.drawable.et_icon_kettlebell,
    R.drawable.et_icon_jump_rope,
    R.drawable.et_icon_weight_lift,
    R.drawable.et_icon_run,
    R.drawable.et_icon_squat,
    R.drawable.et_icon_high_knees,
    R.drawable.et_icon_star_jump,
    R.drawable.et_icon_plank,
    R.drawable.et_icon_sit_up,
    R.drawable.et_icon_hollow_body,
)
