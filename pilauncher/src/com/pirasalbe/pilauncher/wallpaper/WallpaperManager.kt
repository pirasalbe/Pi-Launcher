package com.pirasalbe.pilauncher.wallpaper

import android.app.Activity
import android.content.SharedPreferences
import com.android.launcher3.R
import com.android.launcher3.Utilities
import com.android.launcher3.Workspace
import com.android.launcher3.dragndrop.DragLayer

const val SCROLLING_WALLPAPER_PREFERENCE_KEY = "pref_scrollingWallpaper"

/**
 * Manage wallpaper related settings
 */
class WallpaperManager : SharedPreferences.OnSharedPreferenceChangeListener {

    private var mActivity: Activity
    private var mSharedPrefs: SharedPreferences
    private var mWorkspace: Workspace

    constructor(activity: Activity) {
        mActivity = activity
        var dragLayer: DragLayer = activity.findViewById(R.id.drag_layer)
        mWorkspace = dragLayer.findViewById(R.id.workspace)
        mSharedPrefs = Utilities.getPrefs(activity)
        mSharedPrefs.registerOnSharedPreferenceChangeListener(this)

        notifyChange()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, s: String?) {
        notifyChange()
    }

    /**
     * Notify new values
     */
    private fun notifyChange() {
        var scrollingWallpaper = mSharedPrefs.getBoolean(SCROLLING_WALLPAPER_PREFERENCE_KEY,
                mActivity.resources.getBoolean(R.bool.scrolling_wallpaper))

        if (scrollingWallpaper) {
            mWorkspace.unlockWallpaperFromDefaultPageOnNextLayout();
        } else {
            mWorkspace.lockWallpaperToDefaultPage();
        }
    }

    fun destroy() {
        mSharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
    }

}