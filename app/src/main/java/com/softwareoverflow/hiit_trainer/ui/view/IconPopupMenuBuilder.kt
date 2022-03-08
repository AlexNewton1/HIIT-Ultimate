package com.softwareoverflow.hiit_trainer.ui.view

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import java.lang.reflect.Field
import java.lang.reflect.Method

class IconPopupMenuBuilder(private val context: Context, private val view: View) {

    private var menuRes : Int = 0
    private var listener: PopupMenu.OnMenuItemClickListener? = null

    fun setMenuResource(res: Int){
        menuRes = res
    }

    fun setOnMenuItemClickListener(listener: PopupMenu.OnMenuItemClickListener) {
        this.listener = listener
    }

    fun build() : PopupMenu {
        val menu = PopupMenu(context, view)

        try {
            val fields: Array<Field> = menu.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper: Any? = field.get(menu)

                    if(menuPopupHelper != null) {
                        val classPopupHelper =
                            Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if(menuRes != 0)
            menu.inflate(menuRes)

        if(listener != null)
            menu.setOnMenuItemClickListener(listener)

        return menu;
    }
}