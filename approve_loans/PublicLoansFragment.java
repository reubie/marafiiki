package com.marafiki.android.approve_loans;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marafiki.android.R;
import com.marafiki.android.databinding.FragmentLoaneeBinding;

import java.util.Objects;

public class PublicLoansFragment extends Fragment {

    private FragmentLoaneeBinding mBinding;
    private RecyclerViewAdapter mAdapter;
    private Context mContext;

    //Empty Constructor
    public PublicLoansFragment() {
        // Required empty public constructor
    }

    public static PublicLoansFragment newInstance() {

        Bundle args = new Bundle();
        PublicLoansFragment fragment = new PublicLoansFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mContext instanceof ApproveLoansActivity)
            ((ApproveLoansActivity) requireActivity()).setToolbarTitle("Approve Public Loans");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_loanee, container, false);

        mAdapter = new RecyclerViewAdapter(requireActivity());
        //((requireActivity()) mContext).setToolbarTitle("Approve Public Loans");

        //layout manager set
        mBinding.loaneeList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.loaneeList.setAdapter(mAdapter);

        //hide error
        mBinding.setShowEmptyView(false);

        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(mBinding.progressbar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));
            mBinding.progressbar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            mBinding.progressbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }

        return mBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoaneeViewModel viewModel = ViewModelProviders.of(requireActivity()).get(LoaneeViewModel.class);
        subscribeUi(viewModel);
    }


    private void subscribeUi(LoaneeViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getPendingPublicLoans().observe(requireActivity(), loanModels -> {
            if (loanModels != null && loanModels.size() > 0) {
                mAdapter.setModelList(loanModels);
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


}
