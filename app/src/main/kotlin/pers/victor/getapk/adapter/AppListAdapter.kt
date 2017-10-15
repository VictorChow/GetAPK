package pers.victor.getapk.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_app_list.*
import pers.victor.ext.click
import pers.victor.ext.inflate
import pers.victor.getapk.R
import pers.victor.getapk.entity.AppEntity

/**
 * Created by Victor on 2017/10/14. (ง •̀_•́)ง
 */
class AppListAdapter(private val data: List<AppEntity>) : RecyclerView.Adapter<AppListAdapter.Holder>() {

    var onItemClick: ((Int) -> Unit)? = null
    var optionClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = Holder(inflate(R.layout.item_app_list, parent))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data[position]
        holder.iv_item_app.setImageDrawable(item.appIcon)
        holder.tv_item_app_name.text = item.appName
        holder.tv_item_app_package.text = item.packageName
        holder.tv_item_app_version_name.text = item.versionName
        holder.cv_item_body.click { onItemClick?.invoke(position) }
        holder.iv_item_app_option.click { optionClick?.invoke(position) }
    }

    class Holder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer
}