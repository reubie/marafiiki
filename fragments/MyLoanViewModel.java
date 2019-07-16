package com.marafiki.android.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.NonNull;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.models.LoanBook;
import com.marafiki.android.models.RequestList;

/**
 * Created by Mbariah on 7/26/18.
 */
public class MyLoanViewModel extends AndroidViewModel {

    private ProjectRepository repo;

    public MyLoanViewModel(@NonNull Application application) {
        super(application);
        repo = ((App) application).getRepository();
    }

    LiveData<LoanBook> getActiveLoans() {
        MediatorLiveData<LoanBook> loanproducts = new MediatorLiveData<>();
        loanproducts.addSource(repo.getActiveLoan(), loanproducts::setValue);
        return loanproducts;
    }

    LiveData<RequestList> getPendingLoanRequest() {
        MediatorLiveData<RequestList> pendingloanrequest = new MediatorLiveData<>();
        pendingloanrequest.addSource(repo.getLatestLoanRequests(), pendingloanrequest::setValue);
        return pendingloanrequest;
    }

}
