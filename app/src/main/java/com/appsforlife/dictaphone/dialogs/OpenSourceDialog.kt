package com.appsforlife.dictaphone.dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog.Builder
import com.appsforlife.dictaphone.R

class OpenSourceDialog(
    private val activity: Activity
) {
    fun createOpenSourceDialog() {
        val builder = Builder(
            activity
        )
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_dialog_open_source, null)
        builder.setView(view)
        val dialog = builder.create()
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable())
        }

        view.findViewById<View>(R.id.tv_icon).setOnClickListener {
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://icon-icons.com/ru/"))
            )
        }

        view.findViewById<View>(R.id.tv_android_asset).setOnClickListener {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://romannurik.github.io/AndroidAssetStudio/index.html")
                )
            )
        }

        view.findViewById<View>(R.id.tv_flaticon).setOnClickListener { v: View? ->
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/"))
            )
        }


        view.findViewById<View>(R.id.tv_lottie).setOnClickListener { v: View? ->
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://lottiefiles.com/"))
            )
        }

        dialog.show()
    }

}