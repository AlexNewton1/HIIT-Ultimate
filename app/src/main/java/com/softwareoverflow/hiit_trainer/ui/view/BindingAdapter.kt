package com.softwareoverflow.hiit_trainer.ui.view

import androidx.databinding.BindingAdapter
import com.softwareoverflow.hiit_trainer.ui.view.picker.XViewPagerPicker
import timber.log.Timber

@BindingAdapter("iconName")
fun XViewPagerPicker.setIconName(name: String?) {
    name?.let{
        Timber.d("ViewPager: Setting icon name from binding: $name")
        this.setIconByName(name)
    }
}

@BindingAdapter("colorHex")
fun XViewPagerPicker.setColorHex(hex: String?){
    hex?.let{
        Timber.d("ViewPager: Setting color hex from binding: $hex")
        this.setColorByHex(hex)
    }
}