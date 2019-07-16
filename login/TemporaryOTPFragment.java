package  com.marafiki.android.login;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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



public class TemporaryOTPFragment extends Fragment {

    TextView initialText;
    ProgressBar progressBar;
    Button button;
    private ProjectRepository repo;
    private String msisdn;

    public TemporaryOTPFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ((App) requireActivity().getApplicationContext()).getRepository();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            msisdn = bundle.getString("msisdn");
        }

        if (BuildConfig.DEBUG) {
            Log.i(OTPFragment.class.getSimpleName(), "onCreate: " + msisdn);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_account
        return inflater.inflate(R.layout.fragment_temporary_ot, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialText = view.findViewById(R.id.initial_text);
        progressBar = view.findViewById(R.id.progressbar);
        button = view.findViewById(R.id.button_next);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }

        button.setOnClickListener(this::onClick);

    }

    private EditText getOTPInput() {
        return (EditText) Objects.requireNonNull(getView()).findViewById(R.id.otp_input);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next:

                //Validation
                if (Utils.checkIfEmptyString(getOTPInput().getText().toString().trim()) ||
                        getOTPInput().getText().toString().trim().length() < 4) {
                    getOTPInput().setError("Invalid input");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                button.setBackgroundResource(R.drawable.disabled_round_corner_rect);

                makeNetworkCall(getOTPInput());

                break;
        }
    }

    public void makeNetworkCall(EditText input) {

        String otp = input.getText().toString().trim();

        HashMap<String, Object> map = new HashMap<>();
        map.put("msisdn",Utils.getProperPhoneNumber(msisdn,"254"));
        map.put("token", otp);

        repo.verifyOTP(data -> {
            if (data) {

                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                button.setBackgroundResource(R.drawable.blue_round_corner_rect);

                Bundle args = new Bundle();
                args.putString("token", otp);

                Fragment fragment = new ResetPinFragment();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentContainer, fragment).commit();

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

