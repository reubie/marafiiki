package com.marafiki.android.interfaces;

import androidx.annotation.NonNull;

/**
 * Created by Mbariah on 8/6/18.
 */
public interface ProgressInterface<Result> {
    void onResult(@NonNull Result data);
}
