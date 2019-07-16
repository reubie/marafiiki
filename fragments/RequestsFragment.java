package com.marafiki.android.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marafiki.android.BuildConfig;
import com.marafiki.android.R;
import com.marafiki.android.databinding.FragmentRequestsRecyclerViewBinding;
import com.marafiki.android.models.StatusSpinner;
import com.marafiki.android.transactions.LoanStatementActivity;
import com.marafiki.android.transactions.RequestsRecyclerViewAdapter;
import com.marafiki.android.transactions.RequestsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;


public class RequestsFragment extends BaseFragment {

    FragmentRequestsRecyclerViewBinding mBinding;
    RequestsRecyclerViewAdapter mAdapter;
    RequestsViewModel viewModel;

    public RequestsFragment() {
        // Required empty public constructor
    }

    private final LoanClickCallback loanClickCallback = loanBook -> {

        Intent intent = new Intent(getContext(), LoanStatementActivity.class);
        intent.putExtra("referenceNumber", loanBook.getRefNo());
        startActivity(intent);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(RequestsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_requests_recycler_view, container, false);
        mBinding.setShowEmptyView(false);

        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(mBinding.progressbar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));
            mBinding.progressbar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            mBinding.progressbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
        mAdapter = new RequestsRecyclerViewAdapter(requireActivity(), loanClickCallback);

        //layout manager set
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.swipeRefreshRecyclerList.setEnabled(false);

        initFilters();

        return mBinding.getRoot();

    }


    private void initFilters() {

        List<StatusSpinner> statusList = new ArrayList<>();
        statusList.add(new StatusSpinner("Active Loans", 5, 1));
        statusList.add(new StatusSpinner("Loans Repaid", 7, 1));
        statusList.add(new StatusSpinner("Defaulted", 6, 1));
        statusList.add(new StatusSpinner("All", 0, 0));

        mBinding.statusSpinner.setItems(StreamSupport.stream(statusList)
                .map(StatusSpinner::getName)
                .collect(Collectors.toCollection(ArrayList::new)));

        mBinding.statusSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            StatusSpinner data = statusList.get((int) id);
            System.out.println(data.getId());
            fetchFilters(viewModel, data.getId(), data.getType());
        });

        List<StatusSpinner> balanceList = new ArrayList<>();
        balanceList.add(new StatusSpinner("Last 7 Days", 1, 2));
        balanceList.add(new StatusSpinner("Last 1 Month", 2, 2));
        balanceList.add(new StatusSpinner("Last 3 Month", 3, 2));
        balanceList.add(new StatusSpinner("All", 0, 0));

        mBinding.repaymentSpinner.setItems(StreamSupport.stream(balanceList)
                .map(StatusSpinner::getName)
                .collect(Collectors.toCollection(ArrayList::new)));

        mBinding.repaymentSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            StatusSpinner data = balanceList.get((int) id);
            System.out.println(data.getId());
            fetchFilters(viewModel, data.getId(), data.getType());
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            if (BuildConfig.DEBUG) {
                Log.i(RequestsFragment.class.getSimpleName(), "RequestFragment:  IS HIDDEN");
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.i(RequestsFragment.class.getSimpleName(), "RequestFragment: IS VISIBLE");
            }
            subscribeUi();
        }
    }


    private void subscribeUi() {

        if (BuildConfig.DEBUG) {
            Log.i(RequestsFragment.class.getSimpleName(), "subscribeUi: NETWORK CALLED");
        }

        mBinding.progressbar.setVisibility(View.VISIBLE);

        // Update the list when the data changes
        viewModel.getRequests().observe(this, requestLists -> {
            if (requestLists != null && requestLists.size() > 0) {
                mAdapter.setModelList(requestLists);
                mBinding.setIsLoading(false);
                mBinding.progressbar.setVisibility(View.GONE);
                mBinding.setShowEmptyView(false);
            } else {
                mBinding.setIsLoading(false);
                mBinding.progressbar.setVisibility(View.GONE);
                mBinding.setShowEmptyView(true);
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            mBinding.executePendingBindings();
        });
    }


    private void fetchFilters(final RequestsViewModel viewModel, int value_id, int type_id) {

        mBinding.progressbar.setVisibility(View.VISIBLE);

        viewModel.getRequestsWithFilters(type_id, value_id).observe(this, requestLists -> {
            if (requestLists != null && requestLists.size() > 0) {
                mAdapter.setModelList(requestLists);
                mBinding.setIsLoading(false);
                mBinding.progressbar.setVisibility(View.GONE);
                mBinding.setShowEmptyView(false);
            } else {
                mBinding.setIsLoading(false);
                mBinding.progressbar.setVisibility(View.GONE);
                mBinding.setShowEmptyView(true);
            }
            mBinding.executePendingBindings();
        });
    }


}
