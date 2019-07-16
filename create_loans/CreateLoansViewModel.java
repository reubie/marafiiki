package com.marafiki.android.create_loans;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.helpers.AppExecutors;
import com.marafiki.android.helpers.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mbariah on 7/26/18.
 */
public class CreateLoansViewModel extends AndroidViewModel {


    private ProjectRepository repo;
    private AppExecutors executors;

    public CreateLoansViewModel(@NonNull Application application) {
        super(application);
        repo = ((App) application).getRepository();
        executors = ((App) application).getExecutors();

    }

    /**
     * Expose the LiveData Products query so the UI can observe it
     */
    public LiveData<HashMap<Status, String>> sendLoanProduct(Map<String, Object> data) {
        //Add source and set to observe the changes from the source
        return repo.createLoanProduct(data, executors);
    }



}
