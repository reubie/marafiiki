/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.marafiki.android.approve_loans;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;

import java.util.List;

public class LoaneeViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<LendModel>> pendingLoans = new MediatorLiveData<>();
    private final MediatorLiveData<List<LendModel>> publicLoans = new MediatorLiveData<>();

    private ProjectRepository repo;

    public LoaneeViewModel(Application application) {
        super(application);
        repo = ((App) application).getRepository();
    }

    LiveData<List<LendModel>> getPendingLoans() {
        pendingLoans.addSource(repo.getPendingLoans("agency-loans"), pendingLoans::setValue);
        return pendingLoans;
    }

    LiveData<List<LendModel>> getPendingPublicLoans() {
        publicLoans.addSource(repo.getPendingLoans("public-loans"), publicLoans::setValue);
        return publicLoans;
    }

}
