package com.marafiki.android.login;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marafiki.android.App;
import com.marafiki.android.BuildConfig;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.helpers.Utils;

import java.util.HashMap;
import java.util.Objects;



public class ResetPinFragment extends Fragment {

    TextView initialText;
    ProgressBar progressBar;
    Button button;
    Context context;
    private ProjectRepository repo;
    private String msisdn, device_id, otp;

    public ResetPinFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) requireActivity().getApplicationContext()).getRepository();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            msisdn = bundle.getString("msisdn");
            otp = bundle.getString("otp");

        }

        if (BuildConfig.DEBUG) {
            Log.i(OTPFragment.class.getSimpleName(), "onCreate: " + msisdn);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_account
        return inflater.inflate(R.layout.fragment_reset_pin, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialText = view.findViewById(R.id.initial_text);
        progressBar = view.findViewById(R.id.progressbar);
        button = view.findViewById(R.id.button);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }

        button.setOnClickListener(this::onClick);

    }

    private EditText getPINInput() {
        return (EditText) Objects.requireNonNull(getView()).findViewById(R.id.reset_pin_input);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:

                //Validation
                if (Utils.checkIfEmptyString(getPINInput().getText().toString().trim()) ||
                        getPINInput().getText().toString().trim().length() < 4) {
                    getPINInput().setError("Invalid input");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                button.setBackgroundResource(R.drawable.disabled_round_corner_rect);

                makeNetworkCall(getPINInput());

                break;

        }
    }

    public void makeNetworkCall(EditText input) {

        String formatted_password = input.getText().toString().trim();

        HashMap<String, Object> map = new HashMap<>();
        map.put("newPassword", formatted_password);
        map.put("msisdn",msisdn);
        map.put("token", otp);


        repo.reset(status -> {

            if (status) {

                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                //error
                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                button.setBackgroundResource(R.drawable.blue_round_corner_rect);
                Toast.makeText(requireActivity(), "Network Error. Try again", Toast.LENGTH_SHORT).show();


            }

        }, map);

    }
}
