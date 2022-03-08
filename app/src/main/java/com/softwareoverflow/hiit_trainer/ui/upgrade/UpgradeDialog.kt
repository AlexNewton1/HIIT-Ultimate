package com.softwareoverflow.hiit_trainer.ui.upgrade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.view.FadedDialogBase
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

        view.cancelButton.setOnClickListener { findNavController().navigateUp() }

        return view
    }
}