package com.pirasalbe.pilauncher.settings

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup.PreferencePositionCallback
import androidx.preference.PreferenceScreen
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.LauncherFiles
import com.android.launcher3.R
import com.android.launcher3.Utilities
import com.android.launcher3.config.FeatureFlags
import com.android.launcher3.settings.NotificationDotsPreference
import com.android.launcher3.settings.PreferenceHighlighter
import com.android.launcher3.uioverrides.plugins.PluginManagerWrapper
import com.android.launcher3.util.SecureSettingsObserver


private const val DEVELOPER_OPTIONS_KEY = "pref_developer_options"
private const val FLAGS_PREFERENCE_KEY = "flag_toggler"
private const val NOTIFICATION_DOTS_PREFERENCE_KEY = "pref_icon_badging"

/** Hidden field Settings.Secure.ENABLED_NOTIFICATION_LISTENERS  */
private const val NOTIFICATION_ENABLED_LISTENERS = "enabled_notification_listeners"
const val EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key"
const val EXTRA_SHOW_FRAGMENT_ARGS = ":settings:show_fragment_args"
private const val DELAY_HIGHLIGHT_DURATION_MILLIS = 600
const val SAVE_HIGHLIGHTED_KEY = "android:preference_highlighted"

/**
 * Settings activity for Launcher. Currently implements the following setting: Allow rotation
 */
class SettingsActivity : FragmentActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, PreferenceFragmentCompat.OnPreferenceStartScreenCallback, OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val args = Bundle()
            val prefKey = intent.getStringExtra(EXTRA_FRAGMENT_ARG_KEY)
            if (!TextUtils.isEmpty(prefKey)) {
                args.putString(EXTRA_FRAGMENT_ARG_KEY, prefKey)
            }
            val fm = supportFragmentManager
            val f = fm.fragmentFactory.instantiate(classLoader,
                    getString(R.string.settings_fragment_name))
            f.arguments = args
            // Display the fragment as the main content.
            fm.beginTransaction().replace(android.R.id.content, f).commit()
        }
        Utilities.getPrefs(applicationContext).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {}
    private fun startFragment(fragment: String, args: Bundle, key: String): Boolean {
        if (Utilities.ATLEAST_P && supportFragmentManager.isStateSaved) {
            // Sometimes onClick can come after onPause because of being posted on the handler.
            // Skip starting new fragments in that case.
            return false
        }
        val fm = supportFragmentManager
        val f = fm.fragmentFactory.instantiate(classLoader, fragment)
        f.arguments = args
        if (f is DialogFragment) {
            f.show(supportFragmentManager, key)
        } else {
            fm.beginTransaction().replace(android.R.id.content, f).addToBackStack(key).commit()
        }
        return true
    }

    override fun onPreferenceStartFragment(
            preferenceFragment: PreferenceFragmentCompat, pref: Preference): Boolean {
        return startFragment(pref.fragment, pref.extras, pref.key)
    }

    override fun onPreferenceStartScreen(caller: PreferenceFragmentCompat, pref: PreferenceScreen): Boolean {
        val args = Bundle()
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.key)
        return startFragment(getString(R.string.settings_fragment_name), args, pref.key)
    }

    /**
     * This fragment shows the launcher preferences.
     */
    class LauncherSettingsFragment : PreferenceFragmentCompat() {
        private var mNotificationDotsObserver: SecureSettingsObserver? = null
        protected var mHighLightKey: String? = null
        protected var mPreferenceHighlighted = false
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            var rootKey: String? = rootKey
            val args = arguments
            mHighLightKey = args?.getString(EXTRA_FRAGMENT_ARG_KEY)
            if (rootKey == null && !TextUtils.isEmpty(mHighLightKey)) {
                rootKey = getParentKeyForPref(mHighLightKey)
            }
            if (savedInstanceState != null) {
                mPreferenceHighlighted = savedInstanceState.getBoolean(SAVE_HIGHLIGHTED_KEY)
            }
            preferenceManager.sharedPreferencesName = LauncherFiles.SHARED_PREFERENCES_KEY
            setPreferencesFromResource(R.xml.pilauncher_preferences, rootKey)
            val screen = preferenceScreen
            for (i in screen.preferenceCount - 1 downTo 0) {
                val preference = screen.getPreference(i)
                if (!initPreference(preference)) {
                    screen.removePreference(preference)
                }
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putBoolean(SAVE_HIGHLIGHTED_KEY, mPreferenceHighlighted)
        }

        protected fun getParentKeyForPref(key: String?): String? {
            return null
        }

        /**
         * Initializes a preference. This is called for every preference. Returning false here
         * will remove that preference from the list.
         */
        protected fun initPreference(preference: Preference): Boolean {
            when (preference.key) {
                NOTIFICATION_DOTS_PREFERENCE_KEY -> {
                    if (!Utilities.ATLEAST_OREO ||
                            !resources.getBoolean(R.bool.notification_dots_enabled)) {
                        return false
                    }

                    // Listen to system notification dot settings while this UI is active.
                    mNotificationDotsObserver = SecureSettingsObserver.newNotificationSettingsObserver(
                            activity, preference as NotificationDotsPreference)
                    mNotificationDotsObserver!!.register()
                    // Also listen if notification permission changes
                    mNotificationDotsObserver!!.getResolver().registerContentObserver(
                            Settings.Secure.getUriFor(NOTIFICATION_ENABLED_LISTENERS), false,
                            mNotificationDotsObserver!!)
                    mNotificationDotsObserver!!.dispatchOnChange()
                    return true
                }
                FLAGS_PREFERENCE_KEY ->                     // Only show flag toggler UI if this build variant implements that.
                    return FeatureFlags.showFlagTogglerUi(context)
                DEVELOPER_OPTIONS_KEY ->                     // Show if plugins are enabled or flag UI is enabled.
                    return FeatureFlags.showFlagTogglerUi(context) ||
                            PluginManagerWrapper.hasPlugins(context)
            }
            return true
        }

        override fun onResume() {
            super.onResume()
            if (isAdded && !mPreferenceHighlighted) {
                val highlighter = createHighlighter()
                if (highlighter != null) {
                    requireView().postDelayed(highlighter, DELAY_HIGHLIGHT_DURATION_MILLIS.toLong())
                    mPreferenceHighlighted = true
                } else {
                    requestAccessibilityFocus(listView)
                }
            }
        }

        private fun createHighlighter(): PreferenceHighlighter? {
            if (TextUtils.isEmpty(mHighLightKey)) {
                return null
            }
            val screen = preferenceScreen ?: return null
            val list = listView
            val callback = list.adapter as PreferencePositionCallback?
            val position = callback!!.getPreferenceAdapterPosition(mHighLightKey)
            return if (position >= 0) PreferenceHighlighter(list, position) else null
        }

        private fun requestAccessibilityFocus(rv: RecyclerView) {
            rv.post {
                if (!rv.hasFocus() && rv.childCount > 0) {
                    rv.getChildAt(0)
                            .performAccessibilityAction(AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS, null)
                }
            }
        }

        override fun onDestroy() {
            if (mNotificationDotsObserver != null) {
                mNotificationDotsObserver!!.unregister()
                mNotificationDotsObserver = null
            }
            super.onDestroy()
        }
    }
}
