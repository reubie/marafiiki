package com.marafiki.android.create_loans;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marafiki.android.R;
import com.marafiki.android.databinding.FragmentConfirmBinding;
import com.marafiki.android.models.StatusSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Created by Mbariah on 14/2/19.
 */


public class ConfigFragment extends Fragment {

    FragmentConfirmBinding binding;
    int access_type;
    int loan_type;
    float lender_interest_rate;

    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_account
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm, container, false);

        Bundle getBundle = getArguments();
        if (getBundle != null) {
            access_type = getBundle.getInt("access_type");
            loan_type = getBundle.getInt("loan_type");
            lender_interest_rate = getBundle.getFloat("lender_interest_rate");
            Log.i("TAG", "ACCESS TYPE: " + access_type);
        }

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initSpinners();
        initFilters();
    }

    private void initSpinners() {


        List<StatusSpinner> graceList = new ArrayList<>();
        graceList.add(new StatusSpinner("0 Day", 0, 0)); //1 day
        graceList.add(new StatusSpinner("1 Days", 1, 1)); //7 days
        graceList.add(new StatusSpinner("2 Days", 2, 2)); //14 days
        graceList.add(new StatusSpinner("5 Days", 5, 3)); //30 days
        graceList.add(new StatusSpinner("7 Days", 7, 4)); //90 days

        binding.gracePeriodSpinner.setItems(StreamSupport.stream(graceList)
                .filter(u -> Objects.requireNonNull(u.getType()).equals(loan_type))
                .map(StatusSpinner::getName)
                .collect(Collectors.toCollection(ArrayList::new)));

        binding.gracePeriodSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            StatusSpinner data = graceList.get((int) id);
            System.out.println(data.getId());
        });


        List<StatusSpinner> rollOverList = new ArrayList<>();
        rollOverList.add(new StatusSpinner("1 Time", 1, 2));
        rollOverList.add(new StatusSpinner("1 Time", 1, 1));
        rollOverList.add(new StatusSpinner("2 Times", 2, 1));
        rollOverList.add(new StatusSpinner("3 Times", 3, 1));
        rollOverList.add(new StatusSpinner("4 Times", 4, 1));
        rollOverList.add(new StatusSpinner("5 Times", 5, 1));
        rollOverList.add(new StatusSpinner("6 Times", 6, 1));

        binding.rolloverSpinner.setItems(StreamSupport.stream(rollOverList)
                .filter(u -> Objects.requireNonNull(u.getType()).equals(access_type))
                .map(StatusSpinner::getName)
                .collect(Collectors.toCollection(ArrayList::new)));

        binding.rolloverSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            StatusSpinner data = rollOverList.get((int) id);
            System.out.println(data.getId());
        });


    }


    private void initToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Customize Loan");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> requireActivity().onBackPressed());

    }


    private void initFilters() {

        if (access_type == 2) {
            //public
            binding.paybillAmountInput.setEnabled(false);
            binding.dcAmountInput.setText(requireActivity().getString(R.string.amount_in_percent, 10f));
            binding.dcAmountInput.setEnabled(false);
            binding.rolloverAmountInput.setText(requireActivity().getString(R.string.amount_in_percent, lender_interest_rate));
            binding.rolloverAmountInput.setEnabled(false);

            binding.crbCheckbox.setEnabled(false);
            binding.debtCollectionCheckbox.setEnabled(false);

        }else {
            //private
        }
    }

}

