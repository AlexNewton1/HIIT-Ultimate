package com.softwareoverflow.hiit_trainer.ui.view.binding_adapter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("text")
fun EditText.setIntText(value: Int?) {
    if(this.getIntText() != value)
        this.setText(value.toString())
}

@InverseBindingAdapter(attribute = "text")
fun EditText.getIntText() : Int? {
    return try {
        this.text.toString().toInt()
    } catch (ex: NumberFormatException){
        null
    }
}

@BindingAdapter("textAttrChanged")
fun EditText.intTextAttrChanged(listener: InverseBindingListener){
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener.onChange()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

@BindingAdapter("text")
fun EditText.setNullTextToEmpty(value: String?){
    this.setText(value ?: "")
}

@BindingAdapter("text")
fun EditText.setNonNullableIntText(value: Int){
    this.setIntText(value)
}
