package com.pirasalbe.pilauncher.entity.app

import android.content.Intent
import android.graphics.drawable.Drawable

/**
 * Information about the app
 */
class AppInfo {
    var label: CharSequence
    var packageName: CharSequence
    var icon: Drawable
    var intent: Intent

    constructor(
        label: CharSequence,
        packageName: CharSequence,
        icon: Drawable,
        intent: Intent
    ) {
        this.label = label
        this.packageName = packageName
        this.icon = icon
        this.intent = intent
    }
}