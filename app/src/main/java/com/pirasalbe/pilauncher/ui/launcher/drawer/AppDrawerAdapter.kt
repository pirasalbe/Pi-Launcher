package com.pirasalbe.pilauncher.ui.launcher.drawer

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pirasalbe.pilauncher.R
import com.pirasalbe.pilauncher.entity.app.AppInfo

/**
 * Obtains the apps and manage the drawer
 */
class AppDrawerAdapter : RecyclerView.Adapter<AppDrawerAdapter.ViewHolder> {

    /**
     * List of all apps
     */
    private var appsList: MutableList<AppInfo>? = null

    constructor(context: Context) {
        appsList = ArrayList()

        // preparing variables to get the app list
        val packageManager: PackageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        // getting the app list and filling my list
        val apps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        for (app in apps) {
            val appInfo = AppInfo()
            appInfo.label = app.loadLabel(packageManager)
            appInfo.packageName = app.activityInfo.packageName
            appInfo.icon = app.activityInfo.loadIcon(packageManager)
            appsList!!.add(appInfo)
        }
    }

    /**
     * View for each row
     */
    inner class ViewHolder : RecyclerView.ViewHolder,
        View.OnClickListener {
        val textView: TextView
        val img: ImageView

        constructor(itemView: View) : super(itemView) {
            // get row elements
            textView = itemView.findViewById(R.id.AppName)
            img = itemView.findViewById(R.id.AppIcon)
            // add listener on click
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val context: Context = view.context

            val launchIntent: Intent? = context.packageManager
                .getLaunchIntentForPackage(appsList!![adapterPosition].packageName.toString())

            context.startActivity(launchIntent)
        }

    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // defining the view
        val appInfo: AppInfo = appsList!![position]

        viewHolder.textView.text = appInfo.label
        viewHolder.img.setImageDrawable(appInfo.icon)
    }

    override fun getItemCount(): Int {
        // define the number of elements in the view
        return appsList!!.size
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // adding rows to drawerActivity
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.app_item, parent, false)
        return ViewHolder(view)
    }

}