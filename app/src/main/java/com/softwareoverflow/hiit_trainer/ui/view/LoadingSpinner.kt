package com.softwareoverflow.hiit_trainer.ui.view

import android.content.Context
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.softwareoverflow.hiit_trainer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO come back to this and work out how to handle saving / loading from databases....
object LoadingSpinner {
    private lateinit var icon: MenuItem
    private lateinit var animation: RotateAnimation

    private const val minimumShowTime = 1000L // Show the icon for a minimum of 1s
    private var canCancelAnimation = true
    private var pendingCancellation = false

    fun showLoadingIcon() {
        CoroutineScope(Dispatchers.Main).launch {
            icon.isVisible = true

            // Set the animation to non-cancellable. This will be unset when the animation finishes and
            // ensures the animation is present on screen long enough to avoid flashing up on screen
            canCancelAnimation = false
            icon.actionView.animation =
                animation
            icon.actionView.animation.startNow()
        }
    }

    fun hideLoadingIcon() {
        CoroutineScope(Dispatchers.Main).launch{
            icon.actionView.animation?.cancel()
            icon.isVisible = false

            if(canCancelAnimation)
                icon.actionView.clearAnimation()
            else
                pendingCancellation = true
        }
    }

    fun initialise(icon: MenuItem, context: Context) {
        LoadingSpinner.icon = icon

        animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
            0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
        animation.repeatCount = Animation.INFINITE;
        animation.repeatMode = Animation.INFINITE;
        animation.duration =
            minimumShowTime;

        animation.setAnimationListener(object:  Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // After completing a full animation, the animation can now be cancelled
                canCancelAnimation = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                if(pendingCancellation && canCancelAnimation){
                    animation?.cancel()

                    pendingCancellation = false
                    canCancelAnimation = false
                }
            }

            override fun onAnimationStart(animation: Animation?) {}
        })

        val actionView = ImageView(context)
        actionView.setImageResource(R.drawable.icon_loading)

        icon.actionView = actionView

    }
}