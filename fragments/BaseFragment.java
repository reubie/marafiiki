package com.marafiki.android.fragments;

import androidx.fragment.app.Fragment;

/**
 * Created by Mbariah on 2/26/19.
 */
public class BaseFragment extends Fragment {

    public boolean getIsVisible() {
        if (getParentFragment() != null && getParentFragment() instanceof BaseFragment) {
            return isVisible() && ((BaseFragment) getParentFragment()).getIsVisible();
        } else {
            return isVisible();
        }
    }
}
