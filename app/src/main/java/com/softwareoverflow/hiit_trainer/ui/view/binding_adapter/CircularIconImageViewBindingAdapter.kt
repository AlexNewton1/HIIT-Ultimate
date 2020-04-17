package com.softwareoverflow.hiit_trainer.ui.view.binding_adapter

import android.graphics.Color
import androidx.databinding.BindingAdapter
import com.softwareoverflow.hiit_trainer.ui.getDrawableId
import com.softwareoverflow.hiit_trainer.ui.view.CircularIconImageView

@BindingAdapter("colorHex")
fun CircularIconImageView.setBackgroundColorByHex(hex: String?){
    if(hex != null)
        this.setColor(Color.parseColor(hex))
}

@BindingAdapter("iconName")
fun CircularIconImageView.setIconByName(iconName: String?){
    this.setBackground(iconName?.getDrawableId(context))
}