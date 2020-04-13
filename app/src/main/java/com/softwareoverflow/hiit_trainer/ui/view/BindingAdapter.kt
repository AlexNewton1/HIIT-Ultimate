package com.softwareoverflow.hiit_trainer.ui.view

import androidx.databinding.BindingAdapter
import com.softwareoverflow.hiit_trainer.ui.view.exercise_type_creator.XViewPagerPicker
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

/*
@BindingAdapter("data")
fun  SelectableRecyclerView.setData(items: List<ExerciseTypeDTO>?){
    Timber.d("1waybind setting data from binding $items")
    if(this.adapter is ExerciseTypePickerListAdapter){
        (this.adapter as ExerciseTypePickerListAdapter).submitList(items)
    }
}*/
