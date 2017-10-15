package pers.victor.getapk.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/**
 * Created by Victor on 2017/10/14. (ง •̀_•́)ง
 */

object ThreadPool {
    private val pool = Executors.newFixedThreadPool(8)
    private val handler = Handler(Looper.getMainLooper())

    fun run(block: () -> Unit) = pool.execute(block)

    fun main(block: () -> Unit) = handler.post(block)
}