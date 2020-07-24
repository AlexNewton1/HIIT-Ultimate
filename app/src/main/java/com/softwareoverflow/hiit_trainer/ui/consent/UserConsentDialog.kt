package com.softwareoverflow.hiit_trainer.ui.consent

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.view.FadedDialogBase
import kotlinx.android.synthetic.main.dialog_user_consent.view.*

class UserConsentDialog : FadedDialogBase() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_user_consent, container, false)

        isCancelable = false

        requireDialog().setCanceledOnTouchOutside(false)
        requireDialog().setCancelable(false)

        view.userConsentDetailsText.movementMethod = LinkMovementMethod.getInstance()

        view.userConsentAgreeButton.setOnClickListener {
            UserConsentManager.userGaveConsent(requireContext())

            findNavController().navigate(R.id.action_userConsentDialog_to_homeScreenFragment)
        }

        return view
    }


}