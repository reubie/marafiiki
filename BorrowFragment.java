package com.marafiki.android.apply_loan;

import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marafiki.android.R;
import com.marafiki.android.databinding.FragmentAmountBinding;
import com.marafiki.android.helpers.Utils;

import java.util.HashMap;
import java.util.Map;

public class BorrowFragment extends Fragment {

    private BorrowerActivity mCallback;
    FragmentAmountBinding mBinding;
    Map<String, Object> loanDataMap = new HashMap<>();
    private String interestRate;
    private String maxLoan;

    //Empty Constructor
    public BorrowFragment() {
        //super();
    }

    @SuppressLint("ValidFragment")
    public BorrowFragment(BorrowerActivity mCallback) {
        this.mCallback = mCallback;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_amount, container, false);
        mCallback.setTitle("Apply for a loan");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            interestRate = bundle.getString("interest_rate");
            maxLoan = bundle.getString("loan_limit");
        }

        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }


    private void init() {

        //to ensure not null values on initial load
        float min_value = 500;
        mCallback.saveAmounttoActivity((int) min_value);

        mBinding.period7Days.setText(getString(R.string.period_days, 7));
        mBinding.period14Days.setText(getString(R.string.period_days, 14));
        mBinding.period30Days.setText(getString(R.string.period_days, 30));
        mBinding.period90Days.setText(getString(R.string.period_days, 90));

        mBinding.principalAmount.setText(getString(R.string.amount_in_Kshs, (int) getPrincipleValue(min_value)));
        mBinding.loanLimit.setText(getString(R.string.plain_float_comma_kes, Float.parseFloat(maxLoan)));

        //editText
        mBinding.borrowAmount.setText(getString(R.string.plain_int, (int) min_value));

        mBinding.borrowAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //ensure only integers are displayed
                if (!Utils.checkIfEmptyString(charSequence.toString())) {
                    mBinding.principalAmount.setText(getString(R.string.amount_in_Kshs, (int) getPrincipleValue(Float.parseFloat(charSequence.toString()))));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!Utils.checkIfEmptyString(editable.toString()) && Integer.parseInt(editable.toString()) > 0) {
                    mCallback.saveAmounttoActivity(Integer.parseInt(editable.toString()));
                }

            }
        });

        mBinding.loanPeriodGroup.setOnCheckedChangeListener((radioGroup, checkedID) -> {
            switch (checkedID) {
                case R.id.period_7_days:
                    loanDataMap.put("loan_type_id", 1);
                    mCallback.saveLoanDatatoActivity(loanDataMap);
                    break;
                case R.id.period_14_days:
                    loanDataMap.put("loan_type_id", 2);
                    mCallback.saveLoanDatatoActivity(loanDataMap);
                    break;
                case R.id.period_30_days:
                    loanDataMap.put("loan_type_id", 3);
                    mCallback.saveLoanDatatoActivity(loanDataMap);
                    break;
                case R.id.period_90_days:
                    loanDataMap.put("loan_type_id", 4);
                    mCallback.saveLoanDatatoActivity(loanDataMap);
                    break;
            }
            //makeNetworkRequest(radioGroup);
        });

    }

    private double getPrincipleValue(double user_input) {
        if (user_input <= 0) {
            return 0;
        }
        user_input = user_input + (user_input * Double.parseDouble(interestRate));
        return user_input;
    }


}
