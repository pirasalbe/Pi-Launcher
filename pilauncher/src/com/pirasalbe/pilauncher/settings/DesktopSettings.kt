package com.pirasalbe.pilauncher.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.launcher3.LauncherFiles
import com.android.launcher3.R
import com.android.launcher3.SessionCommitReceiver
import com.android.launcher3.Utilities
import com.android.launcher3.states.RotationHelper

/**
 * Desktop related settings
 */
class DesktopSettings : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = LauncherFiles.SHARED_PREFERENCES_KEY
        setPreferencesFromResource(R.xml.pilauncher_desktop_settings, rootKey)
        for (i in preferenceScreen.preferenceCount - 1 downTo 0) {
            val preference = preferenceScreen.getPreference(i)
            if (!initPreference(preference)) {
                preferenceScreen.removePreference(preference)
            }
        }
    }

    /**
     * Initializes a preference. This is called for every preference. Returning false here
     * will remove that preference from the list.
     */
    protected fun initPreference(preference: Preference): Boolean {
        when (preference.key) {
            SessionCommitReceiver.ADD_ICON_PREFERENCE_KEY -> return Utilities.ATLEAST_OREO
            RotationHelper.ALLOW_ROTATION_PREFERENCE_KEY -> {
                if (resources.getBoolean(R.bool.allow_rotation)) {
                    // Launcher supports rotation by default. No need to show this setting.
                    return false
                }
                // Initialize the UI once
                preference.setDefaultValue(RotationHelper.getAllowRotationDefaultValue())
                return true
            }
        }
        return true
    }
}