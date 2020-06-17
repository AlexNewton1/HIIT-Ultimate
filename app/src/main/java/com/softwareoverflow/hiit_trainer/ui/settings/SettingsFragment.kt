package com.softwareoverflow.hiit_trainer.ui.settings

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.softwareoverflow.hiit_trainer.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_screen, rootKey)

        val preparationSetTime = findPreference<DropDownPreference>("key_preparation_set_time")!!
        val preparationSetEnabled =
            findPreference<SwitchPreference>("key_preparation_set_enabled")!!

        preparationSetTime.isVisible = preparationSetEnabled.isChecked
        preparationSetEnabled.setOnPreferenceChangeListener { preference, newValue ->
            preparationSetTime.isVisible = newValue == true

            true
        }

        val finalSecondsBeep = findPreference<MultiSelectListPreference>("key_final_seconds_beep")!!

        val values = resources.getStringArray(R.array.final_seconds_beep_entry_values).toHashSet()
        finalSecondsBeep.summary = getValueSummary(finalSecondsBeep.getPersistedStringSet(values))

        finalSecondsBeep.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = getValueSummary(newValue as Set<*>)

            true
        }
    }

    private fun getValueSummary(values: Set<*>): String {
        if (values.isEmpty())
            return getString(R.string.final_seconds_beep_summary)
        else {
            val sb = StringBuilder()

            val iterator = values.iterator()
            while (iterator.hasNext()) {
                sb.append(iterator.next()).append("s") // Append s for seconds

                if (iterator.hasNext())
                    sb.append(", ")
            }

            return sb.toString()
        }
    }


}