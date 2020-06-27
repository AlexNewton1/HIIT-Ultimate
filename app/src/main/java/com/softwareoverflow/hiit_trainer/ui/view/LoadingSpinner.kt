package com.softwareoverflow.hiit_trainer.ui.view

import android.content.Context
import android.view.MenuItem
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.softwareoverflow.hiit_trainer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LoadingSpinner {
    private lateinit var icon: MenuItem
    private lateinit var animation: RotateAnimation

    private const val minimumShowTime = 750L // Show the icon for a minimum of 1s
    private var canCancelAnimation = true
    private var pendingCancellation = false

    fun showLoadingIcon() {
        CoroutineScope(Dispatchers.Main).launch {
            icon.isVisible = true

            // Set the animation to non-cancellable. This will be unset when the animation finishes and
            // ensures the animation is present on screen long enough to avoid flashing up on screen
            canCancelAnimation = false
            icon.actionView.animation = animation
            icon.actionView.animation.startNow()
        }
    }

    fun hideLoadingIcon() {
            pendingCancellation = true

            if (canCancelAnimation)
                icon.actionView.clearAnimation()
    }

    fun initialise(icon: MenuItem, context: Context) {
        LoadingSpinner.icon = icon

        animation = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.interpolator = AccelerateInterpolator()
        animation.repeatCount = 1
        animation.duration = minimumShowTime

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // After completing a full animation, the animation can now be cancelled
                canCancelAnimation = true

                if (pendingCancellation) {
                    animation?.cancel()

                    icon.actionView.animation?.cancel()
                    icon.isVisible = false

                    icon.actionView.clearAnimation()

                    pendingCancellation = false
                    canCancelAnimation = false
                } else {
                    animation?.startNow()
                }
            }

            override fun onAnimationEnd(animation: Animation?) {}

            override fun onAnimationStart(animation: Animation?) {}
        })

        val actionView = ImageView(context)
        actionView.setImageResource(R.drawable.icon_loading)

        icon.actionView = actionView

    }
}