package com.softwareoverflow.hiit_trainer.ui.view.binding_adapter

import androidx.databinding.BindingAdapter
import com.softwareoverflow.hiit_trainer.ui.view.exercise_type_creator.XViewPagerPicker

@BindingAdapter("iconName")
fun XViewPagerPicker.setIconName(name: String?) {
    name?.let {
        this.setIconByName(name)
    }
}

@BindingAdapter("colorHex")
fun XViewPagerPicker.setColorHex(hex: String?) {
    hex?.let {
        this.setColorByHex(hex)
    }
}