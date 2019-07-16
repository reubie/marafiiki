package com.marafiki.android.apply_loan;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.marafiki.android.R;
import com.marafiki.android.helpers.PromptPopUpView;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.helpers.Utils;
import com.marafiki.android.interfaces.ToolbarListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by, Mbariah on 5/29/17.
 */
public class BorrowerActivity extends AppCompatActivity implements SaveAmountInterface, ToolbarListener {

    public static String AMOUNT_TAG = "BorrowFragment";
    public static int MIN_LOAN_AMOUNT = 100;

    private TextView title_textView;
    private Fragment mMain;
    private Button btn_finish;
    private BorrowerViewModel viewModel;
    private Map<String, Object> map = new HashMap<>();
    private int loanAmount = 0;
    private SharedPreferences prefs;
    private int status = 0;
    private String interest_rate;
    private MaterialDialog.Builder builder;
    private MaterialDialog termsMaterialDialog = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.CustomTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_blank_main);

        Toolbar toolbar = findViewById(R.id.toolbar_frag);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        findViewById(R.id.toolbar_close).setOnClickListener(view -> onBackPressed());
        title_textView = findViewById(R.id.toolbar_title);
        btn_finish = findViewById(R.id.bt_next);
        prefs = Utils.userDetails(this);

        builder = new MaterialDialog.Builder(this)
                .title("Confirm Loan Application")
                .customView(R.layout.confirm_loan_application, false)
                .cancelable(false)
                .positiveText(R.string.accept)
                .onPositive((dialog, which) -> processLoan())
                .negativeText(R.string.reject)
                .onNegative((dialog, which) -> dialog.dismiss());

        interest_rate = Utils.attemptDecrypt(prefs.getString("interest_rate", ""));
        String loan_limit = Utils.attemptDecrypt(prefs.getString("max_loan_amount", ""));

        //defaults
        map.put("loan_type_id", 2);
        map.put("lender_msisdn", "");

        if (savedInstanceState == null) {

            Bundle bundle = new Bundle();
            bundle.putString("interest_rate", interest_rate);
            bundle.putString("loan_limit", loan_limit);

            mMain = new BorrowFragment(this);
            mMain.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.newcontentContainer, mMain, AMOUNT_TAG)
                    .commit();
        } else {

            mMain = getSupportFragmentManager().findFragmentByTag(AMOUNT_TAG);
        }


        findViewById(R.id.bt_next).setOnClickListener(view -> applyLoan());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(BorrowerViewModel.class);

    }

    private void applyLoan() {

        if (loanAmount < MIN_LOAN_AMOUNT) {
            Toast.makeText(this, "You cannot borrow below the minimum amount of " + MIN_LOAN_AMOUNT, Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialDialog materialDialog = builder.build();

        View positive = materialDialog.getActionButton(DialogAction.POSITIVE);
        positive.setEnabled(false);

        CheckBox checkbox = materialDialog.getCustomView().findViewById(R.id.checkbox);
        TextView amount = materialDialog.getCustomView().findViewById(R.id.amount_payable);
        amount.setText(getString(R.string.amount_in_Kshs, (int) getPrincipleValue(loanAmount)));

        TextView checkBoxTerms = materialDialog.getCustomView().findViewById(R.id.checkboxTerms);

        String terms = " Terms and Conditions ";
        String policy = " Privacy Policy ";

        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append("I agree to the");
        spanText.append(terms);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                loadTerms();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(textPaint.linkColor);    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - terms.length(), spanText.length(), 0);

        spanText.append("and");
        spanText.append(policy);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                loadTerms();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(textPaint.linkColor);    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - policy.length(), spanText.length(), 0);

        checkBoxTerms.setMovementMethod(LinkMovementMethod.getInstance());
        checkBoxTerms.setText(spanText, TextView.BufferType.SPANNABLE);


        TextView due_date = materialDialog.getCustomView().findViewById(R.id.due_date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String from_date;

        if ((int) map.get("loan_type_id") == 1) {
            //7 days ago
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        } else if ((int) map.get("loan_type_id") == 2) {
            //2 weeks
            calendar.add(Calendar.DAY_OF_YEAR, 14);
        } else if ((int) map.get("loan_type_id") == 3) {
            //1 Months
            calendar.add(Calendar.DAY_OF_YEAR, 30);
        }else if ((int) map.get("loan_type_id") == 4) {
            //3 Months
            calendar.add(Calendar.DAY_OF_YEAR, 90);
        }
        from_date = Utils.simplifiedDate(calendar.getTime());
        due_date.setText(getString(R.string.plain_string, from_date));

        checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                materialDialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
            } else {
                materialDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
            }
        });

        materialDialog.show();

    }


    private void processLoan() {
        // Update the list when the data changes
        //{"loan_type":"7 days Loan", "access_type":"public", "amount":"1000", "lender_msisdn":"254726354124"}
        //add to existing map info
        map.put("amount", loanAmount);
        Utils.hideKeyboardinActivity(this);
        PromptPopUpView promptPopUpView = new PromptPopUpView(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("PROCESSING..", (dialogInterface, i) -> {
                    if (status == 0) {
                        dialogInterface.dismiss();
                    } else {
                        finish();
                    }
                })
                .setCancelable(false)
                .setView(promptPopUpView)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(R.color.colorBlackish));


        viewModel.sendLoanApplication(map).observe(BorrowerActivity.this, map -> {

            if (Objects.requireNonNull(map).containsKey(Status.SUCCESS)) {
                promptPopUpView.changeStatus(2, map.get(Status.SUCCESS));
                status = 1;

            } else if (Objects.requireNonNull(map).containsKey(Status.FAIL)) {
                promptPopUpView.changeStatus(1, map.get(Status.FAIL));
                status = 0;
            }

            button.setText(R.string.exit);
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.colorText));
        });

    }

    @Override
    public void setTitle(String title) {
        if (title != null) {
            title_textView.setText(title);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(savedInstanceState, AMOUNT_TAG, mMain);
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.newcontentContainer);
        if (fragment instanceof BorrowFragment) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit Loan Borrowing");
            builder.setMessage("This will take you back to main screen")
                    .setCancelable(true)
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                    })
                    .setPositiveButton("EXIT", (dialogInterface, i) -> finish());

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void saveAmounttoActivity(int am) {
        loanAmount = am;
    }

    @Override
    public void saveLoanDatatoActivity(Map<String, Object> loanData) {
        map.putAll(loanData);
    }

    @Override
    public int returnAmounttoFragment() {
        return loanAmount;
    }


    public void disableNext(boolean val) {
        if (!val) {
            btn_finish.setEnabled(true);
            btn_finish.setBackgroundColor(R.attr.defaultNextButtonColor);
        } else {
            btn_finish.setEnabled(false);
            btn_finish.setBackgroundColor(getResources().getColor(R.color.dark_lighter_gray));
        }
    }

    private double getPrincipleValue(double user_input) {
        if (user_input <= 0) {
            return 0;
        }
        user_input = user_input + (user_input * Double.parseDouble(interest_rate));
        return user_input;
    }


    private void loadTerms() {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(BorrowerActivity.this)
                .customView(R.layout.dialog_webview, false)
                .cancelable(false)
                .positiveText(R.string.dismiss)
                .onPositive((dialog, which) -> termsMaterialDialog.dismiss());
        termsMaterialDialog = builder.build();
        termsMaterialDialog.show();

        final WebView webView = termsMaterialDialog.getCustomView().findViewById(R.id.webview);
        try {
            // Load from changelog.html in the assets folder
            StringBuilder buf = new StringBuilder();
            InputStream json = BorrowerActivity.this.getResources().openRawResource(R.raw.terms);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            // Inject color values for WebView body background and links
            final int accentColor = ContextCompat.getColor(this, R.color.colorAccent);
            webView.loadData(buf.toString()
                            .replace("{style-placeholder}",
                                    "body { background-color: #fff; color: #000; }")
                    , "text/html", "UTF-8");
        } catch (Throwable e) {
            webView.loadData("<h1>Unable to load</h1><p>" + e.getLocalizedMessage() + "</p>", "text/html",
                    "UTF-8");
        }
    }
}
