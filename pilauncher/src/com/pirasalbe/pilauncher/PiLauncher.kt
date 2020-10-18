package com.pirasalbe.pilauncher

import android.os.Bundle
import com.android.launcher3.Launcher
import com.pirasalbe.pilauncher.wallpaper.WallpaperManager

class PiLauncher : Launcher() {

    private var mWallpaperManager: WallpaperManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mWallpaperManager = WallpaperManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mWallpaperManager!!.destroy()
    }
}