package pers.victor.getapk.ui

import android.app.Activity
import android.support.design.widget.Snackbar

/**
 * Created by Victor on 2017/10/15. (ง •̀_•́)ง
 */

fun snack(activity: Activity, text: String) = Snackbar.make(activity.window.decorView, text, Snackbar.LENGTH_SHORT).show()