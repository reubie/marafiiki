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

package com.marafiki.android.create_loans;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.marafiki.android.BuildConfig;
import com.marafiki.android.R;
import com.marafiki.android.database.entity.LoanTypeEntity;
import com.marafiki.android.databinding.ListItemMyloansBinding;

import java.util.List;

public class NewLoansAdapter extends RecyclerView.Adapter<NewLoansAdapter.MyloansViewHolder> {

    private List<? extends LoanTypeEntity> myLoanList;
    //private NotificationClickCallback notificationClickCallback;
    private Context mContext;


    public NewLoansAdapter(Context context) {
        //this.notificationClickCallback = clickCallback;
        mContext = context;

    }

    public void setMyLoanList(final List<? extends LoanTypeEntity> loanList) {
        if (myLoanList == null) {
            myLoanList = loanList;
            notifyItemRangeInserted(0, loanList.size());
        } else {
            myLoanList = loanList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyloansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemMyloansBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_myloans,
                parent, false);
        return new MyloansViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyloansViewHolder holder, int position) {
        holder.binding.setLoantype(myLoanList.get(position));

        String x = "";
        int id = 1;
        try {
            id = myLoanList.get(position).getLoanTypeId();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.i(NewLoansAdapter.class.getSimpleName(), "onBindViewHolder: " + e);
            }
        }

        if (id == 1) {
            x = "7 Days";
        } else if (id == 2) {
            x = "14 Days";
        } else if (id == 3) {
            x = "30 Days";
        } else if (id == 4) {
            x = "90 Days";
        }
        holder.binding.loanClass.setText(mContext.getString(R.string.duration, x));
        //holder.binding.setCallback(notificationClickCallback);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return myLoanList == null ? 0 : myLoanList.size();
    }

    static class MyloansViewHolder extends RecyclerView.ViewHolder {

        final ListItemMyloansBinding binding;

        private MyloansViewHolder(ListItemMyloansBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
