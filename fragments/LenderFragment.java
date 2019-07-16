package com.marafiki.android.fragments;

import android.Manifest;
import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.agents.AgentActivity;
import com.marafiki.android.approve_loans.ApproveLoansActivity;
import com.marafiki.android.create_loans.LoansActivity;
import com.marafiki.android.databinding.FragmentLenderBinding;
import com.marafiki.android.helpers.PromptPopUpView;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.helpers.Utils;
import com.marafiki.android.interfaces.ClickCallback;
import com.marafiki.android.notifications.NotificationActivity;
import com.marafiki.android.notifications.NotificationsViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import permissions.dispatcher.NeedsPermission;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Mbariah on 5/11/17.
 */

public class LenderFragment extends Fragment {

    private FragmentLenderBinding mBinding;
    private SharedPreferences prefs;
    MaterialDialog materialDialog = null;
    private ProjectRepository repo;
    private Float payAmount = 0f;
    private Float withdrawAmount = 0f;

    private boolean isBalanceVisible;
    public static final int PICK_IMAGE = 1;

    private ClickCallback mItemClickListener = new ClickCallback() {
        @Override
        public void onButtonClick() {

            if (!isBalanceVisible) {

                String balance = Utils.attemptDecrypt(prefs.getString("user_balance", ""));
                mBinding.accountBalance.setText(requireActivity().getString(R.string.plain_float_comma_kes, Float.parseFloat(balance)));
                mBinding.accountBalance.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                isBalanceVisible = true;

                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        String blank = getResources().getString(R.string.hidden_amount);
                        mBinding.accountBalance.setText(blank);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        {
                            Drawable leftDrawable = AppCompatResources
                                    .getDrawable(requireActivity(), R.drawable.ic_lock);
                            mBinding.accountBalance.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
                        }
                        else
                        {
                            //Safely create our VectorDrawable on pre-L android versions.
                            Drawable leftDrawable = VectorDrawableCompat
                                    .create(requireActivity().getResources(), R.drawable.ic_lock, null);
                            mBinding.accountBalance.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
                        }
                        isBalanceVisible = false;
                    }
                }.start();

            }

        }
    };

    public LenderFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lender, container, false);
        mBinding.setCallback(mItemClickListener);
        prefs = Utils.userDetails(App.getContext());
        repo = ((App) requireActivity().getApplicationContext()).getRepository();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set animations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ViewGroup) view.findViewById(R.id.constraintLayout)).getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
        }

        String fullName = Utils.attemptDecrypt(prefs.getString("full_name", ""));
        mBinding.accountName.setText(requireActivity().getString(R.string.plain_string, fullName));

        String phone = Utils.attemptDecrypt(prefs.getString("username", ""));
        mBinding.accountPhone.setText(requireActivity().getString(R.string.plain_string, phone));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Drawable leftDrawable = AppCompatResources
                    .getDrawable(requireActivity(), R.drawable.ic_lock);
            mBinding.accountBalance.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        }
        else
        {
            //Safely create our VectorDrawable on pre-L android versions.
            Drawable leftDrawable = VectorDrawableCompat
                    .create(this.getResources(), R.drawable.ic_lock, null);
            mBinding.accountBalance.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        }

        Picasso.get().load(R.drawable.account).resize(100, 100).into(mBinding.accountImage);

        mBinding.accountImage.setOnClickListener(view12 -> fetchImage());

        mBinding.notificationsImage.setOnClickListener(view1 -> {

            Intent intent = new Intent(getContext(), NotificationActivity.class);
            startActivity(intent);
        });


        mBinding.publicLoans.setOnClickListener(view2 -> {
            Intent intent = new Intent(getContext(), ApproveLoansActivity.class);
            intent.putExtra("user_type", 1);
            startActivity(intent);
        });

        mBinding.agencyLoans.setOnClickListener(view3 -> {
            Intent intent = new Intent(getContext(), ApproveLoansActivity.class);
            intent.putExtra("user_type", 0);
            startActivity(intent);
        });

        mBinding.createLoan.setOnClickListener(view4 -> {
            Intent intent = new Intent(getContext(), LoansActivity.class);
            startActivity(intent);
        });

        mBinding.agents.setOnClickListener(view5 -> {
            Intent intent = new Intent(getContext(), AgentActivity.class);
            startActivity(intent);
        });

        mBinding.topup.setOnClickListener(view5 -> {

            MaterialDialog.Builder builder = new MaterialDialog.Builder(requireActivity())
                    .customView(R.layout.confirm_topup, false)
                    .positiveText(R.string.top_up)
                    .onPositive((dialog, which) -> {
                        if (payAmount.intValue() > 1) {
                            makeNetworkCall("stk-push", payAmount.intValue());
                        } else {
                            Toast.makeText(requireActivity(), "Check all fields and try again", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .negativeText(R.string.cancel);

            materialDialog = builder.build();

            TextView advice = materialDialog.getCustomView().findViewById(R.id.top_up_advice);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                advice.setText(Html.fromHtml(getString(R.string.top_up_heading), Html.FROM_HTML_MODE_LEGACY));
            } else {
                advice.setText(Html.fromHtml(getString(R.string.top_up_heading)));
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
                        payAmount = Float.parseFloat(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            RadioButton customAmount = materialDialog.getCustomView().findViewById(R.id.custom_amount);
            customAmount.setText(getString(R.string.plain_string, "Enter Amount"));

            RadioGroup radioGroup = materialDialog.getCustomView().findViewById(R.id.loanGroup);
            radioGroup.setOnCheckedChangeListener((r, i) -> {
                switch (i) {
                    case R.id.custom_amount:
                        materialDialog.getCustomView().findViewById(R.id.custom_amount_input).setVisibility(View.VISIBLE);
                        break;
                }
            });


            materialDialog.show();

        });


        mBinding.withdraw.setOnClickListener(view5 -> {

            MaterialDialog.Builder builder = new MaterialDialog.Builder(requireActivity())
                    .customView(R.layout.confirm_withdraw, false)
                    .positiveText(R.string.withdraw)
                    .onPositive((dialog, which) -> {
                        if (withdrawAmount.intValue() > 1) {
                            makeNetworkCall("withdraw", withdrawAmount.intValue());
                        } else {
                            Toast.makeText(requireActivity(), "Check all fields and try again", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .negativeText(R.string.cancel);

            materialDialog = builder.build();

            TextView advice = materialDialog.getCustomView().findViewById(R.id.withdraw_advice);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                advice.setText(Html.fromHtml(getString(R.string.withdraw_heading), Html.FROM_HTML_MODE_LEGACY));
            } else {
                advice.setText(Html.fromHtml(getString(R.string.withdraw_heading)));
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
                        withdrawAmount = Float.parseFloat(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            RadioButton customAmount = materialDialog.getCustomView().findViewById(R.id.custom_amount);
            customAmount.setText(getString(R.string.plain_string, "Enter Amount"));

            RadioGroup radioGroup = materialDialog.getCustomView().findViewById(R.id.loanGroup);
            radioGroup.setOnCheckedChangeListener((r, i) -> {
                switch (i) {
                    case R.id.custom_amount:
                        materialDialog.getCustomView().findViewById(R.id.custom_amount_input).setVisibility(View.VISIBLE);
                        break;
                }
            });


            materialDialog.show();

        });


        subscribeUi();

    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    private void fetchImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void subscribeUi() {
        // Update the counter when the data changes
        final NotificationsViewModel viewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);

        viewModel.getunreadCount().observe(this, integer -> mBinding.badge.setNumber(Objects.requireNonNull(integer)));
    }

    public void makeNetworkCall(String url, int amount) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("amount", amount);
        map.put("description", "url");

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

        repo.makePayments(url, map).observe(this, map1 -> {

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == PICK_IMAGE) {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = Utils.getPathFromURI(selectedImageUri, requireActivity());
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in ImageView
                    Picasso.get()
                            .load(selectedImageUri)
                            .placeholder(R.drawable.account)
                            .error(R.drawable.account)
                            .resize(100, 100)
                            .centerCrop()
                            .into(mBinding.accountImage);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }


}
