package com.marafiki.android.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.marafiki.android.App;
import com.marafiki.android.BuildConfig;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.apply_loan.BorrowerActivity;
import com.marafiki.android.databinding.FragmentActiveLoanBinding;
import com.marafiki.android.helpers.PromptPopUpView;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.helpers.Utils;

import java.util.HashMap;
import java.util.Objects;


public class MyLoanFragment extends BaseFragment {

    private FragmentActiveLoanBinding mBinding;
    private ScheduleAdapter scheduleAdapter;
    Context mContext;
    private float max_value;
    MaterialDialog materialDialog = null;
    Float loanAmount;
    private ProjectRepository repo;
    private MyLoanViewModel viewModel;

    public MyLoanFragment() {
        //super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MyLoanViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_active_loan, container, false);
        repo = ((App) requireActivity().getApplicationContext()).getRepository();
        scheduleAdapter = new ScheduleAdapter(requireActivity());
        mContext = requireActivity();

        SharedPreferences prefs = Utils.userDetails(requireActivity());
        max_value = Float.parseFloat(Utils.attemptDecrypt(prefs.getString("max_loan_amount", "")));

        //layout manager set
        mBinding.loanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.loanRecyclerView.setAdapter(scheduleAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            if (BuildConfig.DEBUG) {
                Log.i(MyLoanFragment.class.getSimpleName(), "LoanHistory:  IS HIDDEN");
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.i(MyLoanFragment.class.getSimpleName(), "LoanHistory: IS VISIBLE");
            }
            subscribeUi();
        }
    }

    private void subscribeUi() {
        // Update the list when the data changes

        mBinding.setIsLoading(true);
        mBinding.loanLimitAmount.setText(getString(R.string.amount_in_Kshs, (int) max_value));
        mBinding.btnBorrow.setOnClickListener(view -> {

            Intent intent = new Intent(requireActivity(), BorrowerActivity.class);
            startActivity(intent);

        });
        viewModel.getActiveLoans().observe(this, loanBookModel -> {
            if (loanBookModel != null) {
                mBinding.setLoanBook(loanBookModel);
                mBinding.setIsLoading(false);

                mBinding.setIsEmpty(false);
                mBinding.setIsActiveLoanVisible(true);
                mBinding.setIsLoanRequestVisible(false);
                scheduleAdapter.setSchedule(loanBookModel.getRepaymentSchedule());

                loanAmount = loanBookModel.getLoanBalance();

                mBinding.payInstallment.setOnClickListener(view -> {

                    MaterialDialog.Builder builder = new MaterialDialog.Builder(requireActivity())
                            .customView(R.layout.confirm_loan_repayment, false)
                            .positiveText(R.string.pay_now)
                            .onPositive((dialog, which) -> {

                                if (loanAmount.intValue() > 1) {
                                    makeNetworkCall(loanAmount.intValue());
                                } else {
                                    Toast.makeText(requireActivity(), "Check all fields and try again", Toast.LENGTH_SHORT).show();
                                }

                            })
                            .negativeText(R.string.cancel);

                    materialDialog = builder.build();

                    TextView advice = materialDialog.getCustomView().findViewById(R.id.clear_loan_advice);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        advice.setText(Html.fromHtml(getString(R.string.clear_loan), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        advice.setText(Html.fromHtml(getString(R.string.clear_loan)));
                    }

                    EditText customerInputPay = materialDialog.getCustomView().findViewById(R.id.custom_amount_input);
                    customerInputPay.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            //ensure only integers are displayed
                            if (!Utils.checkIfEmptyString(charSequence.toString())) {
                                loanAmount = Float.parseFloat(charSequence.toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    RadioButton fullAmount = materialDialog.getCustomView().findViewById(R.id.full_amount);
                    fullAmount.setText(getString(R.string.pay_full, loanAmount));

                    RadioButton customAmount = materialDialog.getCustomView().findViewById(R.id.custom_amount);
                    customAmount.setText(getString(R.string.plain_string, "Enter Amount"));
                    RadioGroup radioGroup = materialDialog.getCustomView().findViewById(R.id.loanGroup);
                    radioGroup.setOnCheckedChangeListener((r, i) -> {
                        switch (i) {
                            case R.id.full_amount:
                                Log.i(MyLoanFragment.class.getSimpleName(), "Amount: " + loanBookModel.getLoanBalance());
                                materialDialog.getCustomView().findViewById(R.id.custom_amount_input).setVisibility(View.GONE);
                                loanAmount = loanBookModel.getLoanBalance();
                                break;
                            case R.id.custom_amount:
                                materialDialog.getCustomView().findViewById(R.id.custom_amount_input).setVisibility(View.VISIBLE);
                                break;

                        }
                    });


                    materialDialog.show();

                });

            } else {

                fetchPendingLoanRequest(viewModel);
            }

            mBinding.executePendingBindings();
        });
    }

    private void fetchPendingLoanRequest(MyLoanViewModel viewModel) {

        viewModel.getPendingLoanRequest().observe(this, loanRequestModel -> {
            if (loanRequestModel != null) {
                mBinding.setLoanRequest(loanRequestModel);

                mBinding.setIsEmpty(false);
                mBinding.setIsActiveLoanVisible(false);
                mBinding.setIsLoanRequestVisible(true);

            } else {
                //show loan limit
                mBinding.setIsEmpty(true);
                mBinding.setIsLoanRequestVisible(false);
                mBinding.setIsActiveLoanVisible(false);

            }

            mBinding.setIsLoading(false);
            mBinding.executePendingBindings();
        });

    }

    public void makeNetworkCall(int amount) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("amount", amount);
        map.put("description", "payments");

        PromptPopUpView promptPopUpView = new PromptPopUpView(requireActivity());

        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setPositiveButton("PROCESSING..", (dialogInterface, i) -> {
                })
                .setCancelable(false)
                .setView(promptPopUpView)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(R.color.colorBlackish));

        repo.makePayments("stk-push",map).observe(this, map1 -> {

            if (Objects.requireNonNull(map1).containsKey(Status.SUCCESS)) {
                promptPopUpView.changeStatus(2, map1.get(Status.SUCCESS));

            } else if (Objects.requireNonNull(map1).containsKey(Status.FAIL)) {
                promptPopUpView.changeStatus(1, map1.get(Status.FAIL));
            }

            button.setText(R.string.exit);
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.colorText));
        });

    }

}





