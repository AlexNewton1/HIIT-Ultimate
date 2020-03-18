package com.softwareoverflow.hiit_trainer.repository.dto

import android.content.Context
import android.graphics.drawable.Drawable

class ExerciseTypeDTO(var name: String, var iconName: String, var color: String) {

    // TODO maybe change the icon to the R.drawable identifer to remove need for this method
    fun getIconId(context: Context): Int {
        return context.resources.getIdentifier(iconName, "drawable", context.packageName)
    }

    fun getIconDrawable(context: Context): Drawable {
        val id = getIconId(context)
        return context.resources.getDrawable(id, context.theme)
    }

    fun getColor(context: Context): Int {
        return context.resources.getIdentifier(color, "color", context.packageName)
    }
}


/**

        TODO investigate use cases architecture further as a potential alternative to the DTO model

        **/