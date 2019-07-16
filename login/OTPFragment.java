package com.marafiki.android.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.helpers.Utils;
import com.marafiki.android.interfaces.FragmentInterface;
import com.poovam.pinedittextfield.PinField;

import java.util.HashMap;
import java.util.Objects;


/**
 * Created by Mbariah on 9/5/18.
 */
public class OTPFragment extends Fragment {

    private TextView timer;
    private TextView resend;
    private CountDownTimer countDownTimer;
    private String msisdn;

    private ProjectRepository repo;

    public OTPFragment() {
        // Required empty public constructor
    }

//    public static OTPFragment newInstance() {
//
//        Bundle args = new Bundle();
//        OTPFragment fragment = new OTPFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) requireActivity().getApplicationContext()).getRepository();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            msisdn = bundle.getString("msisdn");

        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("msisdn", msisdn);

        repo.generateOTP(data -> {
            if (data != Status.SUCCESS) {

                Toast.makeText(requireActivity(), "Failed sending OTP. Try again", Toast.LENGTH_SHORT).show();

                Fragment phoneFragment = PhoneFragment.newInstance();
                FragmentInterface fc = (FragmentInterface) requireActivity();
                Objects.requireNonNull(fc).replaceFragment(phoneFragment);
            }
        }, map);

       /* SmsRetrieverClient client = SmsRetriever.getClient(App.getContext());

        // Starts SmsRetriever, waits for ONE matching SMS message until timeout
        // (5 minutes).
        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task.
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                Log.d("TAG", "Successfully started retriever");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Failed to start retriever");
            }
        });*/
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_account
        return inflater.inflate(R.layout.activity_register_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        timer = view.findViewById(R.id.timer_secs);
        resend = view.findViewById(R.id.resend_otp);
        TextView user = view.findViewById(R.id.prompt);
        PinField otp = view.findViewById(R.id.otp);
        otp.requestFocus();
        resend.setEnabled(false);
        resend.setOnClickListener(this::onClick);
        user.setText(getString(R.string.code_prompt, msisdn));
        countDownTimer = new CountDownTimer(61000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                timer.setVisibility(View.GONE);
                resend.setEnabled(true);
                resend.setTextColor(ContextCompat.getColor(App.getContext(), R.color.colorPrimary));
            }
        }.start();

        otp.setOnTextCompleteListener(s -> {
            makeNetworkRequest(s);
            return false;
        });

        //Show keyboard immediately
        InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void makeNetworkRequest(String pin) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("msisdn", msisdn);
        map.put("token", pin.trim());

        repo.verifyOTP(this::onResult, map);
    }


    @Override
    public void onStop() {
        super.onStop();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Utils.hideKeyboardinFragments(App.getContext(), getView());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resend_otp:

                HashMap<String, Object> map = new HashMap<>();
                map.put("msisdn", msisdn);

                repo.generateOTP(data -> {
                    if (data != Status.SUCCESS) {

                        Toast.makeText(requireActivity(), "Failed sending OTP. Try again", Toast.LENGTH_SHORT).show();

                        Fragment phoneFragment = PhoneFragment.newInstance();
                        FragmentInterface fc = (FragmentInterface) requireActivity();
                        Objects.requireNonNull(fc).replaceFragment(phoneFragment);
                    }
                }, map);

                break;
        }
    }

    private void onResult(Boolean data) {
        if (data) {
            //FragmentInterface fc = (FragmentInterface) requireActivity();
            //Objects.requireNonNull(fc).replaceFragment(new PinFragment());
            Intent intent = new Intent(getContext(), RegistrationActivity.class);
            intent.putExtra("msisdn", msisdn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Verification failed. Enter correct OTP or Resend OTP", Toast.LENGTH_LONG).show();
        }
    }
}

