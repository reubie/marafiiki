package com.marafiki.android.create_loans;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.NonNull;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.database.AppDatabase;
import com.marafiki.android.database.entity.LoanTypeEntity;
import com.marafiki.android.helpers.AppExecutors;

import java.util.List;

/**
 * Created by Mbariah on 7/26/18.
 */
public class LoansViewModel extends AndroidViewModel {

    private LiveData<List<LoanTypeEntity>> mObservable = new MediatorLiveData<>();
    private MediatorLiveData<List<LoanTypeEntity>> loanproducts;
    private AppExecutors executors;
    private AppDatabase mDb;
    private ProjectRepository repo;


    public LoansViewModel(@NonNull Application application) {
        super(application);

        repo = ((App) application).getRepository();
        mDb = AppDatabase.getInstance(this.getApplication());
        executors = ((App) application).getExecutors();

        loanproducts = new MediatorLiveData<>();
        loanproducts.addSource(repo.fetchLoanProduct(executors), loanproducts::setValue);
    }

    public LiveData<List<LoanTypeEntity>> getMyLoans() {

        mObservable = mDb.myLoansDAO().livefetchAll();
        return mObservable;
    }

//    public void saveLoan(LoanTypeEntity my_loan) {
//
//        executors.diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                mDb.myLoansDAO().insertSingleCollectible(my_loan);
//            }
//        });
//
//    }

}
