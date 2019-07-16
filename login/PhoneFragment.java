package com.marafiki.android.login;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.helpers.Utils;
import com.marafiki.android.interfaces.FragmentInterface;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Mbariah on 9/5/18.
 */
public class PhoneFragment extends Fragment {

    TextView initialText;
    ProgressBar progressBar;
    Button button;
    private ProjectRepository repo;

    public PhoneFragment() {}  // Required empty public constructor

    public static PhoneFragment newInstance() {
        PhoneFragment f = new PhoneFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) requireActivity().getApplicationContext()).getRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_account
        return inflater.inflate(R.layout.activity_register_phone, container, false);
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

    private EditText getPhoneInput() {
        return (EditText) getView().findViewById(R.id.phone_input);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:

                //Validation
                if (Utils.checkIfEmptyString(getPhoneInput().getText().toString().trim()) ||
                        getPhoneInput().getText().toString().trim().length() < 9) {
                    getPhoneInput().setError("Invalid input");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                button.setBackgroundResource(R.drawable.disabled_round_corner_rect);
                makeNetworkCall(getPhoneInput());

                break;
        }
    }

    public void makeNetworkCall(EditText input) {

        //format msisdn
        String formatted_msisdn = Utils.getProperPhoneNumber(input.getText().toString().trim(), "254");
        @SuppressLint("HardwareIds") String deviceUniqueIdentifier = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);


        HashMap<String, Object> map = new HashMap<>();
        map.put("msisdn", formatted_msisdn);
        map.put("device_id", deviceUniqueIdentifier);

        repo.checkifMember(data -> {
            if (data == Status.SUCCESS) {

                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                button.setBackgroundResource(R.drawable.blue_round_corner_rect);

                Bundle args = new Bundle();
                args.putString("msisdn", formatted_msisdn);
                args.putString("session_token", deviceUniqueIdentifier);

                Fragment enterFragment = PinFragment.newInstance();
                enterFragment.setArguments(args);

                FragmentInterface fc = (FragmentInterface) requireActivity();
                Objects.requireNonNull(fc).replaceFragment(enterFragment);

            }else if(data == Status.FAIL) {

                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                button.setBackgroundResource(R.drawable.blue_round_corner_rect);

                Bundle args = new Bundle();
                args.putString("msisdn", formatted_msisdn);
                Fragment otpFragment = new OTPFragment();
                otpFragment.setArguments(args);

                FragmentInterface fc = (FragmentInterface) requireActivity();
                Objects.requireNonNull(fc).replaceFragment(otpFragment);
            }else {
                //Status.CONNECTION_ERROR

                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                button.setBackgroundResource(R.drawable.blue_round_corner_rect);

                Toast.makeText(requireActivity(), "Network Error. Try again", Toast.LENGTH_SHORT).show();
            }
        }, map);


    }


}

