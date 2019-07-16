package com.marafiki.android.mahitaji;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.marafiki.android.BuildConfig;
import com.marafiki.android.R;
import com.marafiki.android.apply_loan.BorrowerActivity;
import com.marafiki.android.interfaces.ToolbarListener;

import java.util.ArrayList;
import java.util.Objects;

public class MahitajiActivity extends AppCompatActivity implements ToolbarListener {

    public static String AMOUNT_TAG = "MahitajiFragment";
    public static String CONFIRMATION_FRAGMENT = "ConfirmationFragment";
    public static String LENDER_FRAGMENT = "LenderFragment";
    private Fragment mMain;
    private Button button;
    private TextView title_textView;
    public ArrayList<String> mMahitaji = new ArrayList<>();
    public static String KEY_LIST_ID = "INTEREST_RATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.CustomTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.container_blank_main);

        Toolbar toolbar = findViewById(R.id.toolbar_frag);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        findViewById(R.id.toolbar_close).setOnClickListener(view -> onBackPressed());

        title_textView = findViewById(R.id.toolbar_title);
        button = findViewById(R.id.bt_next);


        if (savedInstanceState == null) {

            mMain = new MahitajiFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.newcontentContainer, mMain, AMOUNT_TAG)
                    .commit();
        } else {

            if (BuildConfig.DEBUG) {
                Log.i(BorrowerActivity.class.getSimpleName(), "onCreate: Revised");
            }
            mMain = getSupportFragmentManager().getFragment(savedInstanceState, AMOUNT_TAG);
        }

        //findViewById(R.id.bt_prev).setVisibility(View.INVISIBLE);
    }


    @Override
    public void setTitle(String title) {

    }
}