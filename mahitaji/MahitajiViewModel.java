package com.marafiki.android.mahitaji;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.marafiki.android.App;
import com.marafiki.android.ProjectRepository;
import com.marafiki.android.helpers.AppExecutors;
import com.marafiki.android.models.MahitajiModel;

import java.util.List;

public class MahitajiViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<MahitajiModel>> mObservableMahitajiModel = new MediatorLiveData<>();

    private ProjectRepository repo;
    private AppExecutors executors;

    public MahitajiViewModel(Application application) {
        super(application);

        repo = ((App) application).getRepository();
        executors = ((App) application).getExecutors();
    }

    public LiveData<List<MahitajiModel>> getMahitajiData() {
        // set by default null, until we get data from the source.
        mObservableMahitajiModel.setValue(null);
        //Add source and set to observe the changes from the source
        //mObservableMahitajiModel.addSource(repo.getMahitaji(executors), mObservableMahitajiModel::setValue);

        return mObservableMahitajiModel;
    }

}
