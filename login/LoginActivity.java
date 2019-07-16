package com.marafiki.android.login;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.marafiki.android.App;
import com.marafiki.android.R;
import com.marafiki.android.helpers.ActivityUtils;
import com.marafiki.android.helpers.Utils;
import com.marafiki.android.interfaces.FragmentInterface;

public class LoginActivity extends AppCompatActivity implements FragmentInterface {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_blank_login);
        prefs = Utils.userDetails(App.getContext());
        getDeviceIdentifier();

    }


    @SuppressLint("HardwareIds")
    public void getDeviceIdentifier() {
        String deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (prefs.getString("session_token", "").equals("")) {
            //EMPTY SO PLACE IT
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("session_token", Utils.attemptEncrypt(deviceUniqueIdentifier)); //user type
            editor.apply();
        }

        init(deviceUniqueIdentifier);

    }

    private void init(String deviceID) {

        String username = Utils.attemptDecrypt(prefs.getString("username", ""));
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (!prefs.getString("token", "").equals("") && username != null) {

            Bundle args = new Bundle();
            args.putString("msisdn", username);
            args.putString("session_token", deviceID);

            Fragment pinFragment = PinFragment.newInstance();
            pinFragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.contentContainer, pinFragment)
                    .commit();

        } else {

            fragmentManager.beginTransaction()
                    .replace(R.id.contentContainer, new PhoneFragment())
                    .commit();
        }

    }

    @Override
    public void replaceFragment(Fragment fragment) {
        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentContainer);
    }
}


