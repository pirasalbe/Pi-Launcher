package com.pirasalbe.pilauncher.ui.launcher.drawer

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pirasalbe.pilauncher.R
import com.pirasalbe.pilauncher.entity.app.AppInfo
import com.pirasalbe.pilauncher.ui.gesture.OnSwipeTouchListener


/**
 * App Drawer
 */
class AppDrawerFragment : Fragment() {

    /**
     * Recycler view adapter
     */
    private val appDrawerAdapter: AppDrawerAdapter = AppDrawerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // gets the app list
        AppListThread().execute()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout of the app drawer
        val view: View = inflater.inflate(R.layout.fragment_app_drawer, container, false)

        // get the recycler view and set the adapter
        configureRecyclerView(view)

        // subscribe to swipe events
        configureSwipeActions(view)

        return view;
    }

    /**
     * Obtains the app list
     */
    inner class AppListThread : AsyncTask<Void?, Void?, Boolean>() {

        override fun doInBackground(vararg params: Void?): Boolean {
            // preparing variables to get the app list
            val packageManager: PackageManager = activity!!.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)

            // getting the app list and filling my list
            val apps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
            for (app in apps) {
                val label: CharSequence = app.loadLabel(packageManager)
                val packageName: CharSequence = app.activityInfo.packageName
                val icon: Drawable = app.activityInfo.loadIcon(packageManager)
                val appIntent: Intent? =
                    packageManager.getLaunchIntentForPackage(packageName.toString())

                appDrawerAdapter.addApp(AppInfo(label, packageName, icon, appIntent!!))
            }

            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            appDrawerAdapter.update()
        }

    }

    /**
     * Configures the recycler view
     * @param view View reference
     */
    private fun configureRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.AppDrawer)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = appDrawerAdapter
    }

    /**
     * Subscibes to swipe actions
     * @param view View reference
     */
    private fun configureSwipeActions(view: View) {
        // subscribe swipe
        view.setOnTouchListener(object : OnSwipeTouchListener(activity!!) {
            override fun onSwipeRight() {
                view.findNavController()
                    .navigate(R.id.action_appDrawerFragment_to_categoriesFragment)
            }

            override fun onSwipeLeft() {
            }

            override fun onSwipeTop() {
            }

            override fun onSwipeBottom() {
            }

        });
    }

}