package com.rosi.masts.mvc.view.android.settings

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.rosi.masts.BuildConfig
import com.rosi.masts.R
import com.rosi.masts.di.dependencyProvider
import com.rosi.masts.mvc.model.settings.Settings
import com.rosi.masts.mvc.view.android.activity.main.MainActivity
import com.rosi.masts.utils.Logger

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var logger: Logger
    private lateinit var settings: Settings

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val dependencyProvider = context.applicationContext.dependencyProvider
        logger = dependencyProvider.logger
        settings = dependencyProvider.settings
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<ListPreference>(getString(R.string.settings_key_ui_language))?.let { languageListPreference ->
            setLanguageListPreferenceData(languageListPreference)
            languageListPreference.setOnPreferenceClickListener { preference ->
                false
            }
        }

        findPreference<Preference>(getString(R.string.settings_key_version))?.let { preference ->
            preference.title = BuildConfig.VERSION_NAME
        }
    }

    private fun setLanguageListPreferenceData(languageListPreference: ListPreference) {
        languageListPreference.entries = resources.getStringArray(R.array.ui_languages_entries)
        languageListPreference.setDefaultValue("en")
        languageListPreference.entryValues = resources.getStringArray(R.array.ui_languages_values)

        languageListPreference.setOnPreferenceChangeListener { preference, newValue ->
            val oldValue = settings.getUILanguage()
            if (oldValue != newValue) {
                view?.let {
                    Snackbar.make(it, getString(R.string.restart_to_apply_changes), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.restart)) {
                            (requireActivity() as MainActivity).restartApplication()
                        }
                        .show()
                }
            }
            true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setBackgroundColor(resources.getColor(R.color.cardBackgroundColor, activity?.theme))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var myEditTextPreference: EditTextPreference? = findPreference(getString(R.string.settings_key_input_key_debounce_millis))

        myEditTextPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
    }

    companion object {
        val supportedLanguages = listOf("en", "iw")
    }
}