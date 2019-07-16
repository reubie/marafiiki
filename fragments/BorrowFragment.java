package com.marafiki.android.fragments;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marafiki.android.App;
import com.marafiki.android.R;
import com.marafiki.android.agents.AgentActivity;
import com.marafiki.android.apply_loan.BorrowerActivity;
import com.marafiki.android.databinding.FragmentBorrowBinding;
import com.marafiki.android.helpers.Utils;
import com.marafiki.android.invites.InviteActivity;
import com.marafiki.android.notifications.NotificationActivity;
import com.marafiki.android.notifications.NotificationsViewModel;

import java.util.Objects;

/**
 * Created by Mbariah on 5/11/17.
 */

public class BorrowFragment extends Fragment {

    private FragmentBorrowBinding mBinding;
    private SharedPreferences prefs;

    public BorrowFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_borrow, container, false);
        prefs = Utils.userDetails(App.getContext());
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String fullName = Utils.attemptDecrypt(prefs.getString("full_name", ""));
        mBinding.accountName.setText(requireActivity().getString(R.string.plain_string, fullName));

        String phone = Utils.attemptDecrypt(prefs.getString("username", ""));
        mBinding.accountPhone.setText(requireActivity().getString(R.string.plain_string, phone));

        String loan_limit = Utils.attemptDecrypt(prefs.getString("max_loan_amount", ""));
        mBinding.loanLimitBalance.setText(requireActivity().getString(R.string.plain_string_kes, loan_limit));

        mBinding.pay.setOnClickListener(ant -> {
            Intent intent = new Intent(getContext(), InviteActivity.class);
            startActivity(intent);
        });

        mBinding.notificationsImage.setOnClickListener(ant -> {
            Intent intent = new Intent(getContext(), NotificationActivity.class);
            startActivity(intent);
        });

        mBinding.borrow.setOnClickListener(ant -> {
            Intent intent = new Intent(getContext(), BorrowerActivity.class);
            startActivity(intent);
        });


        String user_type = Utils.attemptDecrypt(prefs.getString("is_agent", ""));
        if (Objects.requireNonNull(user_type).equals("true")) {
            mBinding.agents.setVisibility(View.VISIBLE);
            mBinding.agentsText.setVisibility(View.VISIBLE);

            mBinding.agents.setOnClickListener(ant -> {
                Intent intent = new Intent(getContext(), AgentActivity.class);
                startActivity(intent);
            });
        }

        subscribeUi();

    }

    private void subscribeUi() {
        // Update the counter when the data changes
        final NotificationsViewModel viewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);

        viewModel.getunreadCount().observe(this, integer -> mBinding.badge.setNumber(Objects.requireNonNull(integer)));
    }

}
