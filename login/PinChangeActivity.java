package com.marafiki.android.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.helpers.Utils;

import java.util.HashMap;
import java.util.Objects;

public class PinChangeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextOldPassword, editTextNewPassword;
    private ProjectRepository repo;
    Context context;
    String msisdn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_change);
        repo = ((App) Objects.requireNonNull(this).getApplicationContext()).getRepository();
        context = this;

        Bundle getBundle = getIntent().getExtras();
        if (getBundle != null) {
            msisdn = getBundle.getString("msisdn");
            Log.i("TAG", "onCreate: " + msisdn);
        }


        editTextOldPassword = findViewById(R.id.old_password);
        editTextNewPassword = findViewById(R.id.new_password);
        toolbar = findViewById(R.id.toolbar);
        initToolbar();


        findViewById(R.id.buttonOk).setOnClickListener(this);

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Change PIN");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void changePassword() {

        String oldPassword = editTextOldPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            editTextOldPassword.setError("Password is required");
            editTextOldPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            editTextNewPassword.setError("Password is required");
            editTextNewPassword.requestFocus();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("msisdn", msisdn);
        map.put("oldPassword", oldPassword);
        map.put("newPassword", newPassword);

        repo.changePassword(status -> {

            if (status) {
                SharedPreferences prefs = Utils.userDetails(context);
                //delete all
                prefs.edit().clear().apply();

                Intent intent = new Intent(context, LoginActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Toast.makeText(PinChangeActivity.this, "PIN changed Successfully. Log in again to continue",
                        Toast.LENGTH_LONG).show();
            } else {
                //error
                Toast.makeText(PinChangeActivity.this, "Authentication failed, check your password.",
                        Toast.LENGTH_SHORT).show();

            }

        }, map);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOk:
                changePassword();
                Log.i("TAG", "onClick: ");




        }
    }
}