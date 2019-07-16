package com.marafiki.android.create_loans;

import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.marafiki.android.BuildConfig;
import com.marafiki.android.R;
import com.marafiki.android.databinding.AddNewLoanBinding;
import com.marafiki.android.helpers.PromptPopUpView;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.helpers.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewLoansActivity extends AppCompatActivity {

    private AddNewLoanBinding binding;
    private CreateLoansViewModel viewModel;

    private int loan_type_id = 0;
    private int access_type_id = 0;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.CustomTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.add_new_loan);
        initToolbar();
        binding.scrollViewLayout.setVisibility(View.VISIBLE);
        binding.contentContainer.setVisibility(View.GONE);

        initSpinners();

        binding.defaultsCheckbox.setChecked(true);
        binding.defaultsCheckbox.setOnCheckedChangeListener((buttonView, b) -> {

            if (!checkError()) {
                binding.defaultsCheckbox.setChecked(true);
                return;
            }

            if (!buttonView.isChecked()) {

                binding.scrollViewLayout.setVisibility(View.GONE);
                binding.contentContainer.setVisibility(View.VISIBLE);

                Bundle args = new Bundle();
                args.putInt("access_type", access_type_id);
                args.putInt("loan_type", loan_type_id);
                args.putFloat("lender_interest_rate", Float.parseFloat(binding.interestAmountInput.getText().toString()));

                Fragment configFragment = new ConfigFragment();
                configFragment.setArguments(args);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, configFragment)
                        .commit();
            }
        });

        binding.toolbarClose.setOnClickListener(view -> onBackPressed());

        binding.toolbarSave.setOnClickListener(v -> makeNetworkRequest());

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(CreateLoansViewModel.class);

    }

    private void initSpinners() {

        binding.accessTypeSpinner.setItems("Public Loan", "Private Loan");
        binding.accessTypeSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            //Snackbar.make(view, "Material Clicked " + item + " " + id, Snackbar.LENGTH_LONG).show();
            if (id == 0) {
                //public
                access_type_id = 2;
            }else if(id == 1){
                //private
                access_type_id = 1;
            }
        });

        binding.loanProductSpinner.setItems("7 days Loan", "14 days Loan", "30 days Loan", "90 days Loan");
        binding.loanProductSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            //Snackbar.make(view, "Material Clicked " + item+ " " + id, Snackbar.LENGTH_LONG).show();
            if (id == 0) {
                loan_type_id = 1;
            }else if(id == 1){
                loan_type_id = 2;
            }else if(id == 2){
                loan_type_id = 3;
            }else if(id == 3){
                loan_type_id = 4;
            }
        });

    }

    private void makeNetworkRequest() {

        hud = KProgressHUD.create(NewLoansActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Kindly wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        if (!checkError()) {
            hud.dismiss();
            return;
        }

        SharedPreferences prefs = Utils.userDetails(this);

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", Utils.attemptDecrypt(prefs.getString("user_id", "")));
        map.put("status_id", 1);
        map.put("loan_type_id", loan_type_id);
        map.put("access_type_id", access_type_id);
        map.put("min_amount", Integer.parseInt(binding.minAmountInput.getText().toString().trim()));
        map.put("max_amount", Integer.parseInt(binding.maxAmountInput.getText().toString().trim()));
        map.put("loan_product_name", binding.header.getText().toString());
        map.put("interest_rate", (Float.parseFloat(binding.interestAmountInput.getText().toString())) / 100f);

        //unknown_display as disabled
        map.put("notification_cycle", "0,1");
        map.put("repayment_cycle",7);
        map.put("sender_id", "Mobipesa");

        map.put("paybill", "972254"); //Public fixed.(disabled)/ Private Not fixed but crb inactive if custom
        map.put("crb_listing", 1); //Checkbox
        map.put("debt_collection", 1); //Checkbox
        map.put("dc_rate", 0.10); //Private (Optional) 1% - 30% / Public -> fixed at 10%(disabled)
        map.put("grace_period", 3); //Dropdown
        map.put("rollover_no", 1); //Public fixed at 1 / Private max at 6 (Dropdown)
        map.put("rollover_rate", 0.1); //Private must be below 30% / Public fixed at selected interest rate

        saveLoanProducts(map);
    }


    private void saveLoanProducts(Map<String, Object> map) {

        PromptPopUpView promptPopUpView = new PromptPopUpView(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("PROCESSING..", (dialogInterface, i) -> finish())
                .setCancelable(false)
                .setView(promptPopUpView)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(R.color.colorBlackish));
        hud.dismiss();

        viewModel.sendLoanProduct(map).observe(this, map1 -> {

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
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentContainer);
        if (fragment instanceof ConfigFragment) {

            if (BuildConfig.DEBUG) {
                Log.i(NewLoansActivity.class.getSimpleName(), "onBackPressed: FRAGMENT");
            }
            binding.scrollViewLayout.setVisibility(View.VISIBLE);
            binding.contentContainer.setVisibility(View.GONE);

            if (getSupportFragmentManager().findFragmentById(R.id.contentContainer) != null) {
                getSupportFragmentManager()
                        .beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.contentContainer)).commit();
            }

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit Loan Creation?");
            builder.setMessage("This will take you back to main screen")
                    .setCancelable(true)
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                    })
                    .setPositiveButton("EXIT", (dialogInterface, i) -> finish());

            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private boolean checkError() {

        String title = binding.header.getText().toString().trim();
        String minAmount = binding.minAmountInput.getText().toString().trim();
        String maxAmount = binding.maxAmountInput.getText().toString().trim();
        String interest = binding.interestAmountInput.getText().toString().trim();

        if (Utils.checkIfEmptyString(title)) {
            binding.header.setError("Loan Product Title is required");
            binding.header.requestFocus();
            return false;
        } else
            binding.header.setError(null);

        if (Utils.checkIfEmptyString(minAmount)) {
            binding.minAmountInput.setError("Minimum Amount is required");
            binding.minAmountInput.requestFocus();
            return false;
        } else
            binding.minAmountInput.setError(null);


        if (Utils.checkIfEmptyString(maxAmount)) {
            binding.maxAmountInput.setError("Maximum Amount is required");
            binding.maxAmountInput.requestFocus();
            return false;
        } else
            binding.maxAmountInput.setError(null);


        if (Utils.checkIfEmptyString(interest)) {
            binding.interestAmountInput.setError("Interest Rate is required");
            binding.interestAmountInput.requestFocus();
            return false;
        } else if (Integer.parseInt(binding.interestAmountInput.getText().toString()) < 1 || Integer.parseInt(binding.interestAmountInput.getText().toString()) > 30) {
            binding.interestAmountInput.setError("Interest rate cannot be less than 1% or greater than 30%");
            binding.interestAmountInput.requestFocus();
            return false;
        } else {
            binding.interestAmountInput.setError(null);
        }


        if (loan_type_id == 0) {
            Snackbar.make(findViewById(R.id.relativeLayout), "Loan Period cannot be empty", Snackbar.LENGTH_LONG).show();
            binding.loanProductSpinner.requestFocus();
            return false;
        }
        if (access_type_id == 0) {
            Snackbar.make(findViewById(R.id.relativeLayout), "Access Type cannot be empty", Snackbar.LENGTH_LONG).show();
            binding.accessTypeSpinner.requestFocus();
            return false;
        }


        return true;
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbarFrag);
        //Objects.requireNonNull(getSupportActionBar()).setTitle("Approve Loans");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

}

