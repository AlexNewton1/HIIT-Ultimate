package com.softwareoverflow.hiit_trainer.ui.upgrade

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.FadedDialogBase
import com.softwareoverflow.hiit_trainer.ui.view.binding_adapter.setIconByName
import kotlinx.android.synthetic.main.dialog_upgrade.view.*


class UpgradeDialog : FadedDialogBase() {

    private lateinit var billingViewModel: BillingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_upgrade, container, false)

        billingViewModel = BillingViewModel(requireActivity().application)

        view.upgradeDialogIcon.setColor(
            requireContext().resources.getColor(R.color.colorAccent, requireActivity().theme)
        )
        view.upgradeDialogIcon.setIconByName("icon_pro")

        view.upgradeButton.setOnClickListener {
            billingViewModel.purchasePro(requireActivity())
        }

        // TODO fix bullet points
        val string = SpannableString(requireContext().getString(R.string.upgrade_to_pro_benefits))
        string.setSpan(BulletSpan(40, resources.getColor(R.color.colorPrimary, requireActivity().theme)), 10, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.upgradeBenefits.setText(string, TextView.BufferType.SPANNABLE)

        view.cancelButton.setOnClickListener { findNavController().navigateUp() }

        return view
    }
}