package com.marafiki.android.mahitaji;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.marafiki.android.BuildConfig;
import com.marafiki.android.R;
import com.marafiki.android.apply_loan.BorrowFragment;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import java8.util.stream.StreamSupport;

public class MahitajiFragment extends Fragment  {


    TextView amount_text;
    private float min_value = 0;
    private String saveValue;
    private float max_value = 1500;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            if (BuildConfig.DEBUG) {
                Log.i(BorrowFragment.class.getSimpleName(), "onActivityCreated: " + saveValue);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (BuildConfig.DEBUG) {
            Log.i(BorrowFragment.class.getSimpleName(), "onSaveInstanceState: " + saveValue);
        }
        saveValue = amount_text.getText().toString();
        //Save the fragment's state here
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_amount, container, false);
        ((MahitajiActivity) requireActivity()).setTitle("Mahitaji Loan");


        List<Integer> intList = IntStreams.rangeClosed((int) min_value, (int) max_value)
                .filter(i -> i % 2 == 0) // filter out odd numbers
                .filter(i -> i % 100 == 0) // filter out not hundred's numbers
                .boxed()
                .collect(Collectors.toList());


        List<String> newList = StreamSupport.stream(com.marafiki.android.helpers.Utils.stepsList((int) min_value, (int) max_value, 7))
                .map(Object::toString)
                .collect(Collectors.toList());

        //final CharSequence[] charSequenceItems = newList.toArray(new CharSequence[newList.size()]);

        amount_text = view.findViewById(R.id.amount);


        //ensure only integers are displayed
        amount_text.setText(getString(R.string.amount_in_Kshs, (int) 5));




        return view;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


}