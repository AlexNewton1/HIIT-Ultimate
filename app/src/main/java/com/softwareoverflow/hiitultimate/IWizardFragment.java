package com.softwareoverflow.hiitultimate;

/**
 * Handles Fragments responsible for a 'done' or 'back'/'cancel' approach
 */
public interface IWizardFragment {

    /**
     * Informs the fragment that the user has finished with the current visible fragment
     * by clicking the 'next' or 'done' or equivalent button
     */
    void onWizardStepComplete();

    /**
     * A simple boolean to indicate to the caller if any changes have been made
     * by the user on the current fragment.
     *
     * @return - true if the user has current unsaved changes. false otherwise
     */
    boolean hasUnsavedChanges();
}
