package pers.victor.getapk.adapter

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import pers.victor.ext.no
import pers.victor.ext.yes
import pers.victor.getapk.ui.AppListFragment

/**
 * Created by Victor on 2017/10/14. (ง •̀_•́)ง
 */
class AppPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = AppListFragment().apply {
        val bundle = Bundle()
        bundle.putBoolean("systemApp", position == 1)
        arguments = bundle
    }

    override fun getCount() = 2

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
    }

    override fun getPageTitle(position: Int) = (position == 0).yes { "三方应用" }.no { "系统应用" }
}