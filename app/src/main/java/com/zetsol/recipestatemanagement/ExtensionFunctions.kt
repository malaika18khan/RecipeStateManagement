package com.zetsol.recipestatemanagement

import android.content.Context
import android.widget.Toast


fun Any.showToast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
    return Toast.makeText(context, this.toString(), duration).show()
}