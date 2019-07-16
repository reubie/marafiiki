package com.marafiki.android.approve_loans;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.JsonArray;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.R;
import com.marafiki.android.helpers.PromptPopUpView;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.interfaces.TitleInterface;
import com.marafiki.android.models.ApplyLoanModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;

public class ApproveLoansActivity extends AppCompatActivity implements LoanProductClickCallback, TitleInterface {

    private int type = 0;
    Fragment mMain;
    private ProjectRepository repo;
    MaterialDialog materialDialog = null;
    private MaterialDialog termsMaterialDialog = null;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_loans);
        repo = ((App) Objects.requireNonNull(this).getApplicationContext()).getRepository();

        Bundle getBundle = getIntent().getExtras();
        if (getBundle != null) {
            type = getBundle.getInt("user_type");
        }

        initToolbar();

        if (savedInstanceState == null) {
            if(type == 0){
                mMain = PrivateLoansFragment.newInstance();
            }else {
                mMain = PublicLoansFragment.newInstance();
            }
        } else {
            mMain = getSupportFragmentManager().findFragmentByTag(String.valueOf(type));
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.newcontentContainer, mMain, String.valueOf(type))
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLoanProductClicked(ApplyLoanModel applyLoanModel, int color) {

        hud = KProgressHUD.create(ApproveLoansActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Kindly wait")
                .setDetailsLabel("Loading")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", applyLoanModel.getCustomerID());

        repo.fetchCustomerSummary(status -> {

            if (hud.isShowing()) {
                hud.dismiss();
            }

            if (status.containsKey(Status.SUCCESS)) {

                //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(NewCase.this, R.style.AlertDialogCustom));
                MaterialDialog.Builder builder = new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                        .title("Confirm Loan Application")
                        .customView(R.layout.confirm_loan_application_lender, true)
                        .theme(Theme.LIGHT)
                        .cancelable(false)
                        .positiveColorRes(color)
                        .neutralColorRes(color)
                        .negativeColorRes(color)
                        .positiveText(R.string.accept)
                        .onPositive((dialog, which) -> processLoan(applyLoanModel))
                        .negativeText(R.string.reject)
                        .onNegative((dialog, which) -> materialDialog.dismiss());

                materialDialog = builder.build();

                String time = "--";
                int id = Objects.requireNonNull(applyLoanModel.getLoanID());
                if (id == 1) {
                    time = "7 Days";
                } else if (id == 2) {
                    time = "14 Days";
                } else if (id == 3) {
                    time = "30 Days";
                } else if (id == 4) {
                    time = "90 Days";
                }

                View positive = materialDialog.getActionButton(DialogAction.POSITIVE);
                positive.setEnabled(false);

                TextView name = materialDialog.getCustomView().findViewById(R.id.user_name);
                name.setText(Objects.requireNonNull(getString(R.string.plain_string, applyLoanModel.getFirstName() + " " + applyLoanModel.getLastName())));

                TextView score = materialDialog.getCustomView().findViewById(R.id.score_value);
                score.setText(Objects.requireNonNull(getString(R.string.mobipesa_score_value, applyLoanModel.getCreditScore())));

                float progress_value = (Objects.requireNonNull(applyLoanModel.getCreditScore()) / 850f) * 100f;

                SeekBar seekBar = materialDialog.getCustomView().findViewById(R.id.scorebar);
                seekBar.setMax(100);
                seekBar.setProgress((int) progress_value);
                seekBar.setEnabled(false);
                seekBar.refreshDrawableState();

                JsonArray array = status.get(Status.SUCCESS);
                String paid_loans = array.get(0).getAsJsonObject().get("total_paid_loans").getAsString();
                String unhealthy_loans = array.get(0).getAsJsonObject().get("total_unhealthy_loans").getAsString();

                TextView amount = materialDialog.getCustomView().findViewById(R.id.loan_amount);
                amount.setText(Objects.requireNonNull(getString(R.string.plain_int_kes, applyLoanModel.getAmountRequested())));

                TextView totalLoans = materialDialog.getCustomView().findViewById(R.id.loans_taken);
                totalLoans.setText(Objects.requireNonNull(getString(R.string.plain_string, paid_loans)));

                TextView defaultedLoans = materialDialog.getCustomView().findViewById(R.id.loans_defaulted);
                defaultedLoans.setText(Objects.requireNonNull(getString(R.string.plain_string, unhealthy_loans)));

                TextView loanPeriod = materialDialog.getCustomView().findViewById(R.id.loan_period);
                loanPeriod.setText(Objects.requireNonNull(getString(R.string.plain_string, time)));

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

                CheckBox checkbox = materialDialog.getCustomView().findViewById(R.id.checkbox);
                checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b) {
                        materialDialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                    } else {
                        materialDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                    }
                });

                materialDialog.show();

            } else {
                Toast.makeText(this, "Network error. Check your internet connection and try again", Toast.LENGTH_LONG).show();
            }

        }, map);


    }


    private void processLoan(ApplyLoanModel applyLoanModel) {

        PromptPopUpView promptPopUpView = new PromptPopUpView(this);

        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(this))
                .setPositiveButton("PROCESSING..", (dialogInterface, i) -> finish())
                .setCancelable(false)
                .setView(promptPopUpView)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(R.color.colorBlackish));


        HashMap<String, Object> map = new HashMap<>();
        map.put("product_id", applyLoanModel.getP_id());
        map.put("request_id", applyLoanModel.getR_id());

        repo.approveLoans(data -> {
            if (data) {
                //success
                promptPopUpView.changeStatus(2, "Loan Approval was successful");

            } else {
                //error
                promptPopUpView.changeStatus(1, "Failed. Something went wrong.\nTry again later");
            }

            button.setText(R.string.exit);
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.colorText));
        }, map);
    }

    @Override
    public void setToolbarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }
    private void loadTerms() {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(ApproveLoansActivity.this)
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
            InputStream json = ApproveLoansActivity.this.getResources().openRawResource(R.raw.terms);
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
