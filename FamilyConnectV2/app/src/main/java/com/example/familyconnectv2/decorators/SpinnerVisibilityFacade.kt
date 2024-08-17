package com.example.familyconnectv2.decorators

import android.view.View
import android.widget.Spinner




class SpinnerVisibilityFacade {
    private var spinner: Spinner? = null

    fun SpinnerVisibilityFacade(spinner: Spinner?) {
        this.spinner = spinner
    }

    fun showSpinner() {
        spinner!!.visibility = View.VISIBLE
    }

    fun hideSpinner() {
        spinner!!.visibility = View.GONE
    }
}