package com.marafiki.android.apply_loan;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.helpers.AppExecutors;
import com.marafiki.android.helpers.Status;
import com.marafiki.android.models.LenderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java8.util.stream.Collector;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class BorrowerViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<LenderModel>> mLenders = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<Status, String>> applyLoan = new MediatorLiveData<>();

    private ProjectRepository repo;
    private AppExecutors executors;

    public BorrowerViewModel(Application application) {
        super(application);

        repo = ((App) application).getRepository();
        executors = ((App) application).getExecutors();

    }

    /**
     * Expose the LiveData Products query so the UI can observe it
     */
    public LiveData<HashMap<Status, String>> sendLoanApplication(Map<String, Object> data) {
        //Add source and set to observe the changes from the source
        applyLoan.addSource(repo.applyLoan(data), applyLoan::setValue);
        return applyLoan;
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */

    public LenderModel fetchSelected(final ArrayList<String> lenderID) {

        List<LenderModel> lenderList = mLenders.getValue();

        /*Type listType = new TypeToken<List<LenderModel>>() {
        }.getType();

        Log.i(BorrowerViewModel.class.getSimpleName(), "selected: " + new Gson().toJson(lenderList, listType));*/

        return StreamSupport
                .stream(Objects.requireNonNull(lenderList))
                .filter(u -> u.getLenderIds().equals(lenderID.get(0)))
                .collect(toSingleton());
                //.findAny().get();

    }

    //custom collector
    private static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
