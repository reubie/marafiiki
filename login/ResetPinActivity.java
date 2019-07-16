package com.marafiki.android.login;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.databinding.ActivityResetPinBinding;

import java.util.HashMap;
import java.util.Objects;


public class ResetPinActivity extends AppCompatActivity implements View.OnClickListener {

    //private EditText editTextEmail , editTextMsisdn;

    private ProjectRepository repo;
    Context context;
    //private Toolbar toolbar;
    private ActivityResetPinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) Objects.requireNonNull(this).getApplicationContext()).getRepository();
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_pin);
        binding.userMsisdn.getText().toString();
        binding.email.getText().toString();
        binding.buttonOk.setOnClickListener(this::onClick);

        initToolbar();


        findViewById(R.id.buttonOk).setOnClickListener(this);

    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset PIN");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void resetPassword() {

        String email = binding.email.getText().toString().trim();
        String msisdn = binding.userMsisdn.getText().toString().trim();


        if (email.isEmpty()) {
            binding.email.setError("Password is required");
            binding.email.requestFocus();
            return;
        }


        if (msisdn.isEmpty()) {
            binding.userMsisdn.setError("Mobile number is required");
            binding.userMsisdn.requestFocus();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        repo.resetPassword(status -> {

            if (status) {

                binding.constraintLayout.setVisibility(View.GONE);
                binding.contentContainer.setVisibility(View.VISIBLE);

                Bundle args = new Bundle();
                args.putString("email", email);
                args.putString("msisdn", msisdn);


                Fragment configFragment = new TemporaryOTPFragment();
                configFragment.setArguments(args);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, configFragment)
                        .commit();

                Toast.makeText(ResetPinActivity.this, "PIN changed Successfully. Log in again to continue",
                        Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(ResetPinActivity.this, "Authentication failed, check your password.",
                        Toast.LENGTH_SHORT).show();

            }

        }, map);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOk:
                //resetPassword();
                Log.i("TAG", "onClick: ");


//            switch (view.getId()) {
//
//                case R.id.buttonReset:
//                        return;
//
//                    if (first_page.getVisibility() == View.VISIBLE) {
//                        first_page.setVisibility(View.GONE);
//                        second_page.setVisibility(VISIBLE);
//                    } else {
//                        first_page.setVisibility(View.VISIBLE);
//                        second_page.setVisibility(View.GONE);
//                    }
//
//
//                    break;
//                case R.id.buttonReset:
//
//                        return;
//
                resetPassword();
        }
    }


}

