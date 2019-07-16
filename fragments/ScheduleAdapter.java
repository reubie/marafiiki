package com.marafiki.android.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.marafiki.android.R;
import com.marafiki.android.databinding.ListItemScheduleBinding;
import com.marafiki.android.models.PunitiveCharges;
import com.marafiki.android.models.RepaymentScheduleItem;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<? extends RepaymentScheduleItem> mSchedule;
    private Context mContext;

    public ScheduleAdapter(Context context) {
        mContext = context;
    }

    public void setSchedule(final List<? extends RepaymentScheduleItem> mScheduleList) {
        if (mSchedule == null) {
            mSchedule = mScheduleList;
            notifyItemRangeInserted(0, mScheduleList.size());
        } else {
            mSchedule = mScheduleList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemScheduleBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.list_item_schedule,
                parent, false);
        return new ScheduleViewHolder(binding);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        holder.binding.setSchedule(mSchedule.get(position));
        holder.binding.roundedNumber.setTitleText(String.valueOf(position + 1));

        int total_punitive = 0;

        List<PunitiveCharges> punitiveCharges = mSchedule.get(position).getPunitiveCharges();
        if (punitiveCharges != null) {
            for (PunitiveCharges p : punitiveCharges) {
                total_punitive += p.getAmount();
                total_punitive -= p.getAmountPaid();
            }
        }

        float paymentsMade = (mSchedule.get(position).getPrinAmount()
                + mSchedule.get(position).getAgentInt()
                + mSchedule.get(position).getLenderInt());

        float balance = paymentsMade -
                (
                    mSchedule.get(position).getAgentIntPaid()
                    + mSchedule.get(position).getLenderIntPaid()
                    + mSchedule.get(position).getPrinAmountPaid()
                    + total_punitive
                );

        if (balance == 0) {
            holder.binding.roundedNumber.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey));
            holder.binding.date.setText(mContext.getString(R.string.plain_string, "PAID"));
            holder.binding.amount.setText(mContext.getString(R.string.amount_in_fKshs, paymentsMade));
            holder.binding.amount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.binding.date.setText(mContext.getString(R.string.plain_string, mSchedule.get(position).getGroupDate()));
            holder.binding.amount.setText(mContext.getString(R.string.amount_in_fKshs, balance));
        }

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSchedule == null ? 0 : mSchedule.size();
    }


    static class ScheduleViewHolder extends RecyclerView.ViewHolder {

        final ListItemScheduleBinding binding;

        private ScheduleViewHolder(ListItemScheduleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

