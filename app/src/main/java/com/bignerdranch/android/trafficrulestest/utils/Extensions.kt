package com.bignerdranch.android.trafficrulestest.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.bignerdranch.android.trafficrulestest.R

inline fun <reified T : Activity> Context.startActivity(noinline extra: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extra()
    startActivity(intent)
}

fun Context.startDialog(title: String, message: String, positiveCallback: () -> Unit, positiveBtn: String = "Начать") {
    val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setPositiveButton(positiveBtn) { dialog, _ ->
        dialog.cancel()
        positiveCallback()
    }
    builder.setNegativeButton("Отмена") { dialog, _ ->
        dialog.cancel()
    }
    builder.show()
}

fun Context.startConfirmationDialog(title: String, message: String, positiveCallback: () -> Unit, negativeCallback: () -> Unit) {
    val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setPositiveButton("Выйти") { dialog, _ ->
        dialog.cancel()
        positiveCallback()
    }
    builder.setNegativeButton("Да") { dialog, _ ->
        dialog.cancel()
        negativeCallback()
    }
    builder.show()
}

fun Context.startExplainingDialog(title: String = "Подсказка", message: String) {
    val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setPositiveButton("Понятно") { dialog, _ ->
        dialog.cancel()
    }
    builder.show()
}