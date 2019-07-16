package com.marafiki.android.create_loans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marafiki.android.BuildConfig;
import com.marafiki.android.R;
import com.marafiki.android.databinding.ActivityMyLoansBinding;

import java.util.Objects;

/**
 * Created by Mbariah on 5/29/17.
 */

public class LoansActivity extends AppCompatActivity {

    private ActivityMyLoansBinding mBinding;
    private NewLoansAdapter mNewLoansAdapter;
    private LoansViewModel viewModel;
    private Context context;

   /*private final NotificationClickCallback notificationClickCallback = new NotificationClickCallback() {
        @Override
        public void onClick(Notifications notifs) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                Toast.makeText(LoansActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }

            viewModel.setReadNotification(String.valueOf(notifs.getId()));


        }
    };*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_loans);

        initToolbar();

        mBinding.addButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, NewLoansActivity.class);
            startActivity(intent);
        });

        mNewLoansAdapter = new NewLoansAdapter(this);

        //layout manager set
        mBinding.myLoanList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.myLoanList.setAdapter(mNewLoansAdapter);

        viewModel = ViewModelProviders.of(this).get(LoansViewModel.class);

        subscribeUi(viewModel);

    }

    private void subscribeUi(LoansViewModel viewModel) {

        viewModel.getMyLoans().observe(this, loanTypeEntities -> {
            if (BuildConfig.DEBUG) {
                Log.i(LoansActivity.class.getSimpleName(), "onChanged: ");
            }

            if (loanTypeEntities != null) {
                mBinding.setIsLoading(false);
                mNewLoansAdapter.setMyLoanList(loanTypeEntities);
            } else {
                mBinding.setIsLoading(true);
            }

            mBinding.executePendingBindings();
        });
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbarFrag);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.my_loans);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mBinding.toolbarFrag.setNavigationOnClickListener(view -> finish());
    }

}
