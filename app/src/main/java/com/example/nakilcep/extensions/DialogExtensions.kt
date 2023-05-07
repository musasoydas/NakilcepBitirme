package com.example.nakilcep.extensions

import android.app.AlertDialog
import android.content.Context

fun Context.showDialog(
    icon: Int,
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setIcon(icon)
    builder.setMessage(message)
    builder.setPositiveButton(positiveButtonText) { dialog, which ->
        onPositiveClick()
    }
    builder.setNegativeButton(negativeButtonText) { dialog, which ->
        onNegativeClick()
    }
    val dialog = builder.create()
    dialog.show()
}