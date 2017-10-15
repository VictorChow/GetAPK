package pers.victor.getapk.util

import java.io.DataOutputStream
import java.io.File


/**
 * Created by Victor on 2017/10/15. (ง •̀_•́)ง
 */

object RootUtil {

    fun isPhoneRoot(): Boolean {
        var bool = false
        try {
            bool = File("/system/bin/su").exists() || File("/system/xbin/su").exists()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bool
    }

    fun exec(command: String): Boolean {
        if (!isPhoneRoot()) {
            return false
        }
        var process: Process? = null
        var os: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process!!.outputStream)

            os.writeBytes(command + "\n")
            os.flush()

            os.writeBytes("exit\n")
            os.flush()
            process.waitFor()

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                os?.close()
                process?.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }
}
