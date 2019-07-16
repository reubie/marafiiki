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

package com.marafiki.android.mahitaji;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.marafiki.android.R;
import com.marafiki.android.databinding.FragmentMahitajiLenderItemBinding;
import com.marafiki.android.models.Mahitaji;

import java.util.List;

public class MahitajiAdapter extends RecyclerView.Adapter<MahitajiAdapter.MahitajiViewHolder> {

    private Fragment fetchFragment;
    private List<? extends Mahitaji> mMahitaji;

    public MahitajiAdapter(Fragment fetchMahitajiFragment) {
        this.fetchFragment = fetchMahitajiFragment;
    }

    public void setMahitaji(final List<? extends Mahitaji> mahitaji) {
        if (mMahitaji == null) {
            mMahitaji = mahitaji;
            notifyItemRangeInserted(0, mahitaji.size());
        } else {
            mMahitaji = mahitaji;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MahitajiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentMahitajiLenderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_mahitaji_lender_item,
                parent, false);

        return new MahitajiViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MahitajiViewHolder holder, int position) {
        holder.binding.setMahitaji(mMahitaji.get(position));

        /*if (mCallback.returnMahitaji() != null &&
                mCallback.returnMahitaji().contains(mMahitaji.get(position).getLender_count())) {
            holder.binding.setIsClicked(true);
        }*/

        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mMahitaji == null ? 0 : mMahitaji.size();
    }

    static class MahitajiViewHolder extends RecyclerView.ViewHolder {

        final FragmentMahitajiLenderItemBinding binding;

        private MahitajiViewHolder(FragmentMahitajiLenderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
