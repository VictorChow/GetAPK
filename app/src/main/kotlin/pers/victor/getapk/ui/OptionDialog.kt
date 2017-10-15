package pers.victor.getapk.ui

import android.app.Activity
import android.support.v7.app.AlertDialog
import pers.victor.getapk.R

/**
 * Created by Victor on 2017/10/15. (ง •̀_•́)ง
 */
class OptionDialog(context: Activity) : AlertDialog.Builder(context) {
    private var dataIndex = 0
    private var which = 0
    private var dialog: AlertDialog? = null
    private lateinit var click: (Int, Int) -> Unit

    init {
        setTitle(R.string.app_name)
        setSingleChoiceItems(arrayOf("复制到sdcard", "复制并分享"), 0, { _, which -> this.which = which })
        setNegativeButton("取消", null)
        setPositiveButton("确定", { _, _ -> click(dataIndex, which) })

    }

    fun click(block: (dataIndex: Int, which: Int) -> Unit) {
        click = block
    }

    fun show(dataIndex: Int) {
        this.dataIndex = dataIndex
        if (dialog == null) {
            dialog = super.show()
        } else {
            dialog!!.show()
        }
    }

}