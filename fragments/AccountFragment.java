package com.marafiki.android.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marafiki.android.App;
import com.marafiki.android.R;
import com.marafiki.android.databinding.FragmentAccountBinding;
import com.marafiki.android.helpers.Utils;
import com.marafiki.android.login.PinChangeActivity;
import com.marafiki.android.settings.SettingsActivity;


public class AccountFragment extends Fragment {

    private FragmentAccountBinding mBinding;
    private SharedPreferences prefs;

    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        prefs = Utils.userDetails(App.getContext());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String fullName = Utils.attemptDecrypt(prefs.getString("full_name", ""));
        String phone = Utils.attemptDecrypt(prefs.getString("username", ""));
        String string_user_type = Utils.attemptDecrypt(prefs.getString("user_type_id", ""));
        int user_type = Integer.parseInt(string_user_type);

        mBinding.fullNamesText.setText(requireActivity().getString(R.string.plain_string, fullName));
        mBinding.msisdnText.setText(requireActivity().getString(R.string.plain_string, "+" + phone));
        mBinding.idText.setText(requireActivity().getString(R.string.plain_string, Utils.attemptDecrypt(prefs.getString("id_number", ""))));
        mBinding.emailText.setText(requireActivity().getString(R.string.plain_string, Utils.attemptDecrypt(prefs.getString("email", ""))));

        if (user_type == 1) {
            //Lender
            mBinding.userTypeText.setText(requireActivity().getString(R.string.plain_string, "Lender"));
        } else {
            //Borrower
            mBinding.userTypeText.setText(requireActivity().getString(R.string.plain_string, "Borrower"));
        }

        mBinding.settingsImage.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        mBinding.changePin.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireActivity(), PinChangeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("msisdn", Utils.attemptDecrypt(prefs.getString("username", "")));
            startActivity(intent);
        });


        mBinding.contactUs.setOnClickListener(view13 -> {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:0703012300"));
            startActivity(i);
        });

        mBinding.faq.setOnClickListener(view14 -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(getString(R.string.url_faq)));
            startActivity(i);
        });


        //subscribeUi(viewModel);
    }


/*

    private void subscribeUi(AccountViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getAccount().observe(this, accountsModel -> {
            if (accountsModel != null) {
                mBinding.setAccount(accountsModel);
                int type = accountsModel.getUser_type();
                if(type == 1){
                    //Lender
                    mBinding.leftFieldText.setText(R.string.gained_interest);
                    mBinding.rightFieldText.setText(R.string.loaned_amount);
                }else {
                    mBinding.leftFieldText.setText(R.string.loan_limit);
                    mBinding.rightFieldText.setText(R.string.pending_loans);
                }
            }
            mBinding.executePendingBindings();
        });
    }

*/

}
