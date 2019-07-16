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
import com.marafiki.android.databinding.FragmentRecyclerViewBinding;
import com.marafiki.android.models.StatusSpinner;
import com.marafiki.android.transactions.LoanHistoryRecyclerViewAdapter;
import com.marafiki.android.transactions.LoanStatementActivity;
import com.marafiki.android.transactions.StatementViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;


public class LoanHistoryFragment extends BaseFragment {

    FragmentRecyclerViewBinding mBinding;
    private final LoanClickCallback loanClickCallback = loanBook -> {

        Intent intent = new Intent(getContext(), LoanStatementActivity.class);
        intent.putExtra("referenceNumber", loanBook.getRefNo());
        startActivity(intent);
    };
    StatementViewModel viewModel;
    LoanHistoryRecyclerViewAdapter mStatementAdapter;

    public LoanHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(StatementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_view, container, false);
        mBinding.setShowEmptyView(false);

        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(mBinding.progressbar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));
            mBinding.progressbar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            mBinding.progressbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
        mStatementAdapter = new LoanHistoryRecyclerViewAdapter(requireActivity(), loanClickCallback);

        //layout manager set
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerView.setAdapter(mStatementAdapter);

        initFilters();

        return mBinding.getRoot();

    }

    private void initFilters() {

        List<StatusSpinner> periodList = new ArrayList<>();
        periodList.add(new StatusSpinner("Last 7 Days", 1, 1));
        periodList.add(new StatusSpinner("Last 1 Month", 2, 1));
        periodList.add(new StatusSpinner("Last 3 Month", 3, 1));
        periodList.add(new StatusSpinner("All", 0, 0));

        mBinding.periodSpinner.setItems(StreamSupport.stream(periodList)
                .map(StatusSpinner::getName)
                .collect(Collectors.toCollection(ArrayList::new)));

        mBinding.periodSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            StatusSpinner data = periodList.get((int) id);
            System.out.println(data.getId());
            fetchFilters(viewModel, data.getId(), data.getType());
        });

       /*
        List<StatusSpinner> accountList = new ArrayList<>();
        accountList.add(new StatusSpinner("M-PESA", 1, 1));
        accountList.add(new StatusSpinner("APPLICATION FEES", 2, 1));
        accountList.add(new StatusSpinner("LOAN", 7, 1));
        accountList.add(new StatusSpinner("LOAN REPAYMENT", 8, 1));
        accountList.add(new StatusSpinner("LENDER INTEREST", 6, 1));
        accountList.add(new StatusSpinner("AGENT INTEREST", 9, 1));
        accountList.add(new StatusSpinner("DEBT COLLECTION", 12, 1));
        accountList.add(new StatusSpinner("ALL", 0, 0));

        mBinding.accountSpinner.setItems(StreamSupport.stream(accountList)
                .map(StatusSpinner::getName)
                .collect(Collectors.toCollection(ArrayList::new)));

        mBinding.accountSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            StatusSpinner data = accountList.get((int) id);
            System.out.println(data.getId());
            fetchFilters(viewModel, data.getId(), data.getType());
        });
        */


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            if (BuildConfig.DEBUG) {
                Log.i(LoanHistoryFragment.class.getSimpleName(), "LoanHistory:  IS HIDDEN");
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.i(LoanHistoryFragment.class.getSimpleName(), "LoanHistory: IS VISIBLE");
            }
            subscribeUi();
        }
    }


    private void subscribeUi() {

        // Update the list when the data changes

        viewModel.getLoanHistory().observe(this, statementLists -> {
            if (statementLists != null && statementLists.size() > 0) {
                mStatementAdapter.setModelList(statementLists);
                mBinding.setIsLoading(false);
                mBinding.progressbar.setVisibility(View.GONE);
            }else {
                mBinding.setIsLoading(false);
                mBinding.progressbar.setVisibility(View.GONE);
                mBinding.setShowEmptyView(true);

            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            mBinding.executePendingBindings();
        });
    }

    private void fetchFilters(final StatementViewModel viewModel, int value_id, int type_id) {

        mBinding.progressbar.setVisibility(View.VISIBLE);

        viewModel.getLoanHistoryWithFilters(type_id, value_id).observe(this, requestLists -> {
            if (requestLists != null && requestLists.size() > 0) {
                mStatementAdapter.setModelList(requestLists);
                mBinding.setIsLoading(false);
                mBinding.progressbar.setVisibility(View.GONE);
            } else {
                mBinding.setIsLoading(false);
                mBinding.progressbar.setVisibility(View.GONE);
                mBinding.setShowEmptyView(true);

            }
            mBinding.executePendingBindings();
        });
    }




  /*  @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setAdapter();

        swipeRefreshRecyclerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Do your stuff on refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (swipeRefreshRecyclerList.isRefreshing())
                            swipeRefreshRecyclerList.setRefreshing(false);
                    }
                }, 5000);

            }
        });


    }*/



    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(requireActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        //changing edittext color
        EditText searchEdit = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!Character.isLetterOrDigit(source.charAt(i)))
                        return "";
                }


                return null;


            }
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<StatementModel> filterList = new ArrayList<StatementModel>();
               *//* if (s.length() > 0) {
                    for (int i = 0; i < modelList.size(); i++) {
                        if (modelList.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterList.add(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }

                } else {
                    mAdapter.updateList(modelList);
                }*//*
                return false;
            }
        });

    }*/

/*
    private void setAdapter() {


        ArrayList<StatementModel> singleItemList = new ArrayList<>();

        singleItemList.add(new StatementModel("Android", "Hello " + " Android"));
        singleItemList.add(new StatementModel("Beta", "Hello " + " Beta"));
        singleItemList.add(new StatementModel("Cupcake", "Hello " + " Cupcake"));
        singleItemList.add(new StatementModel("Donut", "Hello " + " Donut"));
        singleItemList.add(new StatementModel("Eclair", "Hello " + " Eclair"));



        modelList.add(new StatementModel("Android", "Hello " + " Android", singleItemList));
        modelList.add(new StatementModel("Apple", "Hello " + " Beta", singleItemList));
        modelList.add(new StatementModel("BlackBerry", "Hello " + " Cupcake", singleItemList));
        modelList.add(new StatementModel("Motorola", "Hello " + " Donut", singleItemList));
        modelList.add(new StatementModel("Alcatel", "Hello " + " Eclair", singleItemList));
        modelList.add(new StatementModel("OnePlus", "Hello " + " Froyo", singleItemList));


        mAdapter = new RecyclerViewAdapter(requireActivity(), modelList);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        scrollListener = new RecyclerViewScrollListener() {

            public void onEndOfScrollReached(RecyclerView rv) {

                Toast.makeText(requireActivity(), "End of the RecyclerView reached. Do your pagination stuff here", Toast.LENGTH_SHORT).show();

                scrollListener.disableScrollListener();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
          */
    /*      Note: The below two methods should be used wisely to handle the pagination enable and disable states based on the use case.
                     1. scrollListener.disableScrollListener(); - Should be called to disable the scroll state.
                     2. scrollListener.enableScrollListener(); - Should be called to enable the scroll state.
    }
    */


}
