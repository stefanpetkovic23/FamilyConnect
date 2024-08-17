package com.example.familyconnectv2.decorators

import android.view.View
import android.view.animation.Animation
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AnimationFacade (
    private val addEventButton: FloatingActionButton,
    private val addActivityButton: FloatingActionButton,
    private val addGroupActivityButton: FloatingActionButton,
    private val rotateOpen: Animation,
    private val rotateClose: Animation,
    private val fromBottom: Animation,
    private val toBottom: Animation
    ) {
    private var clicked = false

    fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            addActivityButton.startAnimation(fromBottom)
            addGroupActivityButton.startAnimation(fromBottom)
            addEventButton.startAnimation(rotateOpen)
        } else {
            addActivityButton.startAnimation(toBottom)
            addGroupActivityButton.startAnimation(toBottom)
            addEventButton.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            addActivityButton.visibility = View.VISIBLE
            addGroupActivityButton.visibility = View.VISIBLE
        } else {
            addActivityButton.visibility = View.INVISIBLE
            addGroupActivityButton.visibility = View.INVISIBLE
        }
    }
}

