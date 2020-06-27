package com.softwareoverflow.hiit_trainer.ui.settings

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.consent.UserConsentManager
import com.softwareoverflow.hiit_trainer.ui.upgrade.AdsManager

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_screen, rootKey)

        val personalizedAds = findPreference<SwitchPreference>(getString(R.string.key_personalized_ads_enabled))!!
        personalizedAds.isVisible = !AdsManager.hasUserUpgraded

        personalizedAds.setOnPreferenceChangeListener { _, newValue ->
            UserConsentManager.setPersonalizedAds(requireContext(), newValue == true)
            true
        }

        val analytics = findPreference<SwitchPreference>(getString(R.string.key_analytics_enabled))!!
        analytics.setOnPreferenceChangeListener { _, newValue ->
            UserConsentManager.setAnalytics(requireContext(), newValue == true)
            true
        }

        val preparationSetTime = findPreference<DropDownPreference>(getString(R.string.key_preparation_set_time))!!
        val preparationSetEnabled =
            findPreference<SwitchPreference>(getString(R.string.key_preparation_set_enabled))!!

        preparationSetTime.isVisible = preparationSetEnabled.isChecked
        preparationSetEnabled.setOnPreferenceChangeListener { _, newValue ->
            preparationSetTime.isVisible = newValue == true

            true
        }

        val finalSecondsBeep = findPreference<MultiSelectListPreference>(resources.getString(R.string.key_final_seconds_vocal))!!

        val values = resources.getStringArray(R.array.final_seconds_beep_entry_values).toHashSet()
        finalSecondsBeep.summary = getValueSummary(finalSecondsBeep.getPersistedStringSet(values))

        finalSecondsBeep.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = getValueSummary(newValue as Set<*>)

            true
        }
    }

    private fun getValueSummary(values: Set<*>): String {
        return if (values.isEmpty())
            getString(R.string.final_seconds_beep_summary)
        else {
            val sb = StringBuilder()

            val iterator = values.iterator()
            while (iterator.hasNext()) {
                sb.append(iterator.next()).append("s") // Append s for seconds

                if (iterator.hasNext())
                    sb.append(", ")
            }

            sb.toString()
        }
    }


}