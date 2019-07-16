package com.marafiki.android.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.helpers.PromptPopUpView;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.helpers.Utils;

import java.util.HashMap;
import java.util.Objects;

import static android.view.View.VISIBLE;
import static com.marafiki.android.App.getContext;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextInputLayout textInputfirstName, textInputlastName, textInputemail, textInputid;
    private ProjectRepository repo;
    private Group first_page, second_page;
    private EditText editTextpassword;
    private String msisdn;
    private int user_type = 3;
    String address;

    private static final String[] paths =
            {"Baringo County",
            "Bomet County",
            "Bungoma County",
            "Busia County",
            "Elgeyo Marakwet County",
            "Embu County",
            "Garissa County",
            "Homa Bay County",
            "Isiolo County",
            "Kajiado County",
            "Kakamega County",
            "Kericho County",
            "Kiambu County",
            "Kilifi County",
            "Kirinyaga County",
            "Kisii County",
            "Kisumu County",
            "Kitui County",
            "Kwale County",
            "Laikipia County",
            "Lamu County",
            "Machakos County",
            "Makueni County",
            "Mandera County",
            "Meru County",
            "Migori County",
            "Marsabit County",
            "Mombasa County",
            "Muranga County",
            "Nairobi County",
            "Nakuru County",
            "Nandi County",
            "Narok County",
            "Nyamira County",
            "Nyandarua County",
            "Nyeri County",
            "Samburu County",
            "Siaya County",
            "Taita Taveta County",
            "Tana River County",
            "Tharaka Nithi County",
            "Trans Nzoia County",
            "Turkana County",
            "Uasin Gishu County",
            "Vihiga County",
            "Wajir County",
            "West Pokot County"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        repo = ((App) Objects.requireNonNull(this).getApplicationContext()).getRepository();

        Bundle getBundle = getIntent().getExtras();
        if (getBundle != null) {
            msisdn = getBundle.getString("msisdn");
        }


        textInputfirstName = findViewById(R.id.first_name);
        textInputlastName = findViewById(R.id.last_name);
        textInputemail = findViewById(R.id.email);
        textInputid = findViewById(R.id.id);
        editTextpassword = findViewById(R.id.pin_input);
        MaterialSpinner spinner = findViewById(R.id.spinner);

        first_page = findViewById(R.id.first_page);
        second_page = findViewById(R.id.second_page);


        findViewById(R.id.buttonNext).setOnClickListener(this);
        findViewById(R.id.buttonFinish).setOnClickListener(this);


        spinner.setItems(paths);
        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            address = item;
        });
    }


    public void onRadioButtonClick(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){

            case R.id.as_lender:
                if (checked)
                    user_type = 1;
                break;

            case R.id.as_borrower:
                if (checked)
                    user_type = 3;
                break;
            default:
                break;
        }

    }

    private void registerCall() {

        SharedPreferences prefs = Utils.userDetails(this);
        String firstName = textInputfirstName.getEditText().getText().toString().trim();
        String lastName = textInputlastName.getEditText().getText().toString().trim();
        String email = textInputemail.getEditText().getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String id = textInputid.getEditText().getText().toString().trim();
        String session_token = Utils.attemptDecrypt(prefs.getString("session_token", ""));

        HashMap<String, Object> map = new HashMap<>();
        map.put("msisdn", msisdn);
        map.put("first_name", firstName);
        map.put("last_name", lastName);
        map.put("id_number", id);
        map.put("address", address);
        map.put("email", email);
        map.put("language_id", 1);
        map.put("password", password);
        map.put("access_type_id", 2); //public
        map.put("user_type_id", user_type);
        map.put("device_id", session_token);

        PromptPopUpView promptPopUpView = new PromptPopUpView(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("PROCESSING..", (dialogInterface, i) -> finish())
                .setCancelable(false)
                .setView(promptPopUpView)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(R.color.colorBlackish));

        repo.customersRegister(result -> {

            if (Objects.requireNonNull(result).containsKey(Status.SUCCESS)) {
                promptPopUpView.changeStatus(2, result.get(Status.SUCCESS));
            } else if (Objects.requireNonNull(result).containsKey(Status.FAIL)) {
                promptPopUpView.changeStatus(1, result.get(Status.FAIL));
            }

            button.setText(R.string.exit);
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.colorText));
            button.setOnClickListener(view -> {

                if (Objects.requireNonNull(result).containsKey(Status.SUCCESS)) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else if (Objects.requireNonNull(result).containsKey(Status.FAIL)) {
                    dialog.dismiss();
                }
            });

        }, map);

    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonNext:

                if (!checkError())
                    return;

                if (first_page.getVisibility() == View.VISIBLE) {
                    first_page.setVisibility(View.GONE);
                    second_page.setVisibility(VISIBLE);
                } else {
                    first_page.setVisibility(View.VISIBLE);
                    second_page.setVisibility(View.GONE);
                }


                break;
            case R.id.buttonFinish:

                if (!confirmFields())
                    return;

                registerCall();
        }
    }

    private boolean confirmFields() {

        String password = editTextpassword.getText().toString().trim();

        if (Utils.checkIfEmptyString(password)) {
            editTextpassword.setError("Password is mandatory");
            editTextpassword.requestFocus();
            return false;
        } else
            editTextpassword.setError(null);


        return true;
    }

    private boolean checkError() {

        String firstName = textInputfirstName.getEditText().getText().toString().trim();
        String lastName = textInputlastName.getEditText().getText().toString().trim();
        String email = textInputemail.getEditText().getText().toString().trim();
        String id = textInputid.getEditText().getText().toString().trim();

        if (Utils.checkIfEmptyString(firstName)) {
            textInputfirstName.setError("FirstName is required");
            textInputfirstName.requestFocus();
            return false;
        } else
            textInputfirstName.setError(null);

        if (Utils.checkIfEmptyString(lastName)) {
            textInputlastName.setError("LastName is required");
            textInputlastName.requestFocus();
            return false;
        } else
            textInputlastName.setError(null);


        if (Utils.checkIfEmptyString(id)) {
            textInputid.setError("ID is mandatory");
            textInputid.requestFocus();
            return false;
        } else
            textInputid.setError(null);


        if (Utils.checkIfEmptyString(email)) {
            textInputemail.setError("Email is mandatory");
            textInputemail.requestFocus();
            return false;
        } else
            textInputemail.setError(null);


        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
  
