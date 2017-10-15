package pers.victor.getapk.ui

import android.Manifest
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_app_list.*
import pers.victor.ext.copy
import pers.victor.ext.findColor
import pers.victor.getapk.R
import pers.victor.getapk.adapter.AppListAdapter
import pers.victor.getapk.entity.AppEntity
import pers.victor.getapk.util.ThreadPool
import java.io.File


/**
 * Created by Victor on 2017/10/14. (ง •̀_•́)ง
 */
class AppListFragment : Fragment() {
    private var isSystemApp = false
    private val data = arrayListOf<AppEntity>()
    private val rxPermissions by lazy { RxPermissions(activity) }
    private val optionDialog by lazy {
        OptionDialog(activity).apply {
            click { dataIndex, which ->
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe { granted ->
                            if (granted) {
                                operate(dataIndex, which)
                            } else {
                                snack(activity, "权限不足")
                            }
                        }
            }
        }
    }

    private val map: (PackageInfo) -> AppEntity = {
        val appName = it.applicationInfo.loadLabel(activity.packageManager).toString()
        val packageName = it.packageName
        val versionName = it.versionName
        val appIcon = it.applicationInfo.loadIcon(activity.packageManager)
        AppEntity(appName, packageName, versionName, appIcon)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_app_list, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isSystemApp = arguments.getBoolean("systemApp")

        rv_app.layoutManager = LinearLayoutManager(activity)
        val adapter = AppListAdapter(data)
        adapter.onItemClick = { optionDialog.show(it) }
        rv_app.adapter = adapter

        sr_app.setColorSchemeColors(findColor(R.color.colorAccent))
        sr_app.setOnRefreshListener { loadPackages() }

        sr_app.post { sr_app.isRefreshing = true }
        loadPackages()
    }

    private fun loadPackages() {
        ThreadPool.run {
            val packages = activity.packageManager.getInstalledPackages(0)
            data.clear()
            data.addAll(if (isSystemApp) {
                packages.filter { (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 }
            } else {
                packages.filter { (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            }.map(map).sortedBy { it.appName })

            ThreadPool.main {
                rv_app.adapter.notifyDataSetChanged()
                sr_app.isRefreshing = false
            }
        }
    }

    private fun operate(index: Int, which: Int) {
        val item = data[index]
        val apkFile = File(activity.packageManager.getApplicationInfo(item.packageName, 0).sourceDir)
        val destFile = File(Environment.getExternalStorageDirectory().absolutePath + "/${item.appName}_v${item.versionName}.apk")
        when (which) {
            0 -> copyApk(apkFile, destFile)
            1 -> copyApk(apkFile, destFile) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "*/*"
                val uri: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(activity, "pers.victor.getapk.fileprovider", destFile)
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    uri = Uri.fromFile(destFile)
                }
                share.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(share, null))
            }
        }
    }

    private fun copyApk(apkFile: File, destFile: File, complete: (() -> Unit)? = null) {
        ThreadPool.run {
            if (!apkFile.exists()) {
                ThreadPool.main { snack(activity, "apk不存在") }
                return@run
            }
            if (destFile.exists()) {
                destFile.delete()
            }
            destFile.createNewFile()
            apkFile.copy(destFile)
            ThreadPool.main {
                snack(activity, "复制成功")
                complete?.invoke()
            }
        }
    }
}