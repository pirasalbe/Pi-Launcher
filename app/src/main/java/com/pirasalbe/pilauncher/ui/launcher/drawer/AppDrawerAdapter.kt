package com.pirasalbe.pilauncher.ui.launcher.drawer

import android.content.Context
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
class AppDrawerAdapter : RecyclerView.Adapter<AppDrawerAdapter.ViewHolder>() {

    /**
     * List of all apps
     */
    private val appsList: MutableList<AppInfo> = arrayListOf()

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

            // start selected app
            context.startActivity(appsList[adapterPosition].intent)
        }

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // defining the view
        val appInfo: AppInfo = appsList[position]

        viewHolder.textView.text = appInfo.label
        viewHolder.img.setImageDrawable(appInfo.icon)
    }

    override fun getItemCount(): Int {
        // define the number of elements in the view
        return appsList.size
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

    /**
     * Adds an app to the list and notify for changes
     * @param appInfo App to add
     */
    fun addApp(appInfo: AppInfo) {
        this.appsList.add(appInfo)
    }

    fun update() {
        this.appsList.sortBy { it.label.toString().toLowerCase() }
        this.notifyItemRangeChanged(0, this.appsList.size)
    }

}