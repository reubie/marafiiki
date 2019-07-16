package com.marafiki.android.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marafiki.android.App;
import com.marafiki.android.BuildConfig;
import com.marafiki.android.PermissionScreen;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.database.AppDatabase;
import com.marafiki.android.helpers.AppExecutors;
import com.marafiki.android.helpers.Utils;
import com.marafiki.android.interfaces.FragmentInterface;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Mbariah on 9/5/18.
 */

public class PinFragment extends Fragment {

    public final static String permissions = "PERMISSIONS";
    TextView initialText, forgotPin, notYou;
    ImageView backBtn;
    ProgressBar progressBar;
    Button button;
    public static String[] PERMISSIONS = {
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private ProjectRepository repo;
    private String msisdn;
    private SharedPreferences prefs;

    public PinFragment() {
    } // Required empty public constructor

    public static PinFragment newInstance() {
        PinFragment f = new PinFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
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
            Log.i(OTPFragment.class.getSimpleName(), "MSISDN: " + msisdn);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_account
        return inflater.inflate(R.layout.activity_register_pin, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = Utils.userDetails(requireActivity());

        initialText = view.findViewById(R.id.initial_text);
        progressBar = view.findViewById(R.id.progressbar);
        button = view.findViewById(R.id.button);
        forgotPin = view.findViewById(R.id.forgot_pin);
        notYou = view.findViewById(R.id.not_you);
        backBtn = view.findViewById(R.id.back_btn);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }

        notYou.setText(requireActivity().getString(R.string.not_you, msisdn));
        button.setOnClickListener(this::onClick);
        forgotPin.setOnClickListener(this::onClick);
        notYou.setOnClickListener(this::onClick);
        backBtn.setOnClickListener(this::onClick);
    }

    private EditText getPINInput() {
        return (EditText) Objects.requireNonNull(getView()).findViewById(R.id.pin_input);
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
            case R.id.forgot_pin:
                Intent intent = new Intent(requireActivity(), ResetPinActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;

            case R.id.back_btn:
            case R.id.not_you:

                AppDatabase mDb = AppDatabase.getInstance(App.getContext());
                AppExecutors executors = ((App) App.getContext()).getExecutors();

                //remove all notifications if any
                NotificationManager notificationManager = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Objects.requireNonNull(notificationManager).cancelAll();

                //delete all shared prefs
                prefs.edit().clear().apply();

                executors.diskIO().execute(() -> {

                    //clear all db's
                    mDb.notificationDAO().deleteAll();
                    mDb.myLoansDAO().deleteAll();
                    mDb.collectionDAO().deleteAll();
                    mDb.loanPeriodDao().deleteAll();

                    //AppDatabase.destroyInstance();
                });


                Fragment phoneFragment = new PhoneFragment();
                FragmentInterface fc = (FragmentInterface) requireActivity();
                Objects.requireNonNull(fc).replaceFragment(phoneFragment);

                break;

        }
    }

    public void makeNetworkCall(EditText input) {

        String formatted_password = input.getText().toString().trim();
        @SuppressLint("HardwareIds") String deviceUniqueIdentifier = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);


        HashMap<String, Object> map = new HashMap<>();
        map.put("password", formatted_password);
        map.put("username", msisdn);
        map.put("device_id", deviceUniqueIdentifier);

        repo.loginUser(token -> {
            /*
                JsonArray jsonArr = new JsonParser().parse(new Gson().toJson(data)).getAsJsonArray();
                for (int i = 0; i < jsonArr.size(); i++)
                {
                    JsonElement jsonObj = jsonArr.get(i).getAsJsonObject();
                    System.out.println(jsonObj);
                }
            */

            try {
                if (token.getBoolean("status")) {

                    System.out.println(token);

                    SharedPreferences.Editor editor = Utils.userDetails(requireActivity()).edit();
                    editor.putString("token", Utils.attemptEncrypt(token.getString("token")));
                    editor.putString("userId", Utils.attemptEncrypt(token.getString("userId")));
                    editor.apply();


                    Bundle args = new Bundle();
                    args.putBoolean(permissions, Utils.hasPermissions(requireActivity(), PERMISSIONS));
                    Fragment permissionFragment = PermissionScreen.newInstance();
                    permissionFragment.setArguments(args);

                    FragmentInterface fc = (FragmentInterface) requireActivity();
                    Objects.requireNonNull(fc).replaceFragment(permissionFragment);


                }else {

                    Toast.makeText(requireActivity(), token.getString("message"), Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);
                    button.setEnabled(true);
                    button.setBackgroundResource(R.drawable.blue_round_corner_rect);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, map);

    }


}

