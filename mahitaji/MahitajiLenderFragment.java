package com.marafiki.android.mahitaji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.marafiki.android.R;
import com.marafiki.android.databinding.FragmentMahitajiLenderListBinding;

public class MahitajiLenderFragment extends Fragment {

    private FragmentMahitajiLenderListBinding mBinding;

    private MahitajiAdapter mahitajiAdapter;

    public MahitajiLenderFragment() {
        // super();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MahitajiActivity) requireActivity()).setTitle("MAHITAJI LOAN");

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mahitaji_lender_list, container, false);

        mahitajiAdapter = new MahitajiAdapter(this);
        //mLoaneeAdapter = new NewLoansAdapter(mProductClickCallback);

        //layout manager set
        mBinding.mahitajiLendersList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.mahitajiLendersList.setAdapter(mahitajiAdapter);

        return mBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MahitajiViewModel viewModel =
                ViewModelProviders.of(this).get(MahitajiViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(MahitajiViewModel viewModel) {
        // Update the list when the data changes

        viewModel.getMahitajiData().observe(this, mahitajiModels -> {

            if (mahitajiModels != null) {
                mahitajiAdapter.setMahitaji(mahitajiModels);
                mBinding.progressbar.setVisibility(View.GONE);
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            mBinding.executePendingBindings();
        });
    }
}