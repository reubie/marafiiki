package com.marafiki.android.approve_loans;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.marafiki.android.R;
import com.marafiki.android.databinding.ItemRecyclerSectionBinding;
import com.marafiki.android.databinding.ListItemAgencyLoanBinding;
import com.marafiki.android.helpers.SectionedRecyclerViewAdapter;
import com.marafiki.android.models.Header;
import com.marafiki.android.models.PendingLoans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class AgencyRecyclerViewAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private List<? extends LendModel> modelList;
    private Context mContext;

    AgencyRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getSectionCount() {
        return modelList == null ? 0 : modelList.size();
    }

    @Override
    public int getItemCount(int section) {
        return modelList == null ? 0 : modelList.get(section).getSingleItemArrayList().size();
    }

    public void setModelList(final List<? extends LendModel> newModelList) {
        if (modelList == null) {
            modelList = newModelList;
            notifyItemRangeInserted(0, newModelList.size());
        } else {
            notifyDataSetChanged();
            modelList = newModelList;
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;

        /* if (BuildConfig.DEBUG) {
            Log.i(RecyclerViewAdapter.class.getSimpleName(), "Header: " + modelList.get(section).getHeading());
        }*/

        Header header_title = new Header();
        header_title.setTitle(modelList.get(section).getHeading());

        sectionViewHolder.sectionBinding.setHeader(header_title);
        sectionViewHolder.sectionBinding.executePendingBindings();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        //Called by RecyclerView to load the data at the specified position.
        List<PendingLoans> itemsInSection = modelList.get(section).getSingleItemArrayList();

        ViewHolder itemViewHolder = (ViewHolder) holder;
        itemViewHolder.binding.setLoan(itemsInSection.get(relativePosition));

        //Calendar cal = Calendar.getInstance();
        //long inSec = (cal.getTime().getTime() - itemsInSection.get(relativePosition).getDateRequested().getTime()) / 1000;
        String days = null;
        int loan_type = Objects.requireNonNull(itemsInSection.get(relativePosition).getLoanTypeId());
        String rating = Objects.requireNonNull(itemsInSection.get(relativePosition).getRating());
        int color;

        if (loan_type == 1) {
            days = "7";
        } else if (loan_type == 2) {
            days = "14";
        } else if (loan_type == 3) {
            days = "30";
        } else if (loan_type == 4) {
            days = "90";
        }

        itemViewHolder.binding.period.setText(mContext.getString(R.string.period_in_days, days));

        if (rating.equalsIgnoreCase("AA")) {
            itemViewHolder.binding.color.setBackgroundColor(mContext.getResources().getColor(R.color.colorPerfect));
            color = R.color.colorPerfect;
        } else if (rating.equalsIgnoreCase("BB")) {
            itemViewHolder.binding.color.setBackgroundColor(mContext.getResources().getColor(R.color.colorGood));
            color = R.color.colorPerfect;
        } else if (rating.equalsIgnoreCase("CC")) {
            itemViewHolder.binding.color.setBackgroundColor(mContext.getResources().getColor(R.color.colorNormal));
            color = R.color.colorPerfect;
        } else if (rating.equalsIgnoreCase("DD")) {
            itemViewHolder.binding.color.setBackgroundColor(mContext.getResources().getColor(R.color.colorBad));
            color = R.color.colorPerfect;
        } else if (rating.equalsIgnoreCase("EE")) {
            itemViewHolder.binding.color.setBackgroundColor(mContext.getResources().getColor(R.color.colorWorst));
            color = R.color.colorPerfect;
        } else {
            itemViewHolder.binding.color.setBackgroundColor(mContext.getResources().getColor(R.color.colorDanger));
            color = R.color.colorPerfect;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(days));
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
        itemViewHolder.binding.dateDue.setText(mContext.getString(R.string.due_date, formatter.format(calendar.getTime())));

        itemViewHolder.binding.executePendingBindings();

        //SLIDE LAYOUT
        int noOfChildTextViews = itemViewHolder.linearLayout_childItems.getChildCount();
        int noOfChild = Objects.requireNonNull(itemsInSection.get(relativePosition).getChildData()).size();

        //if (BuildConfig.DEBUG) {
        //    Log.i(AgencyRecyclerViewAdapter.class.getSimpleName(), "onBindViewHolder SIZE: " + noOfChild);
        //}
        if (noOfChild < noOfChildTextViews) {
            for (int index = noOfChild; index < noOfChildTextViews; index++) {
                TextView currentTextView = (TextView) itemViewHolder.linearLayout_childItems.getChildAt(index);
                currentTextView.setVisibility(View.GONE);
            }
        }

        for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {
            final int viewIndex = textViewIndex;

            //if (BuildConfig.DEBUG) {
            // Log.i(AgencyRecyclerViewAdapter.class.getSimpleName(), "onBindViewHolder: " + itemsInSection.get(relativePosition).getChildData().get(viewIndex).getProductName());
            //}
            if (itemViewHolder.linearLayout_childItems.getChildAt(textViewIndex) == null) {
                /* if (BuildConfig.DEBUG) {
                    Log.i(AgencyRecyclerViewAdapter.class.getSimpleName(), "Child at index " + textViewIndex + " is null ");
                }*/
                return;
            }

            TextView currentTextView = (TextView) itemViewHolder.linearLayout_childItems.getChildAt(textViewIndex);
            currentTextView.setText(mContext.getString(R.string.plain_entry, Objects.requireNonNull(itemsInSection.get(relativePosition).getChildData().get(viewIndex).getProductName()) + "", itemsInSection.get(relativePosition).getChildData().get(viewIndex).getLoanInterestRate() * 100f));
            currentTextView.setOnClickListener(view -> ((LoanProductClickCallback) mContext).onLoanProductClicked(Objects.requireNonNull(itemsInSection.get(relativePosition).getChildData()).get(viewIndex), color));
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, boolean header) {
        //Called to create a new view and it’s ViewHolder
        if (header) {
            ItemRecyclerSectionBinding sectionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recycler_section,
                    parent, false);
            return new SectionViewHolder(sectionBinding);

        } else {

            ListItemAgencyLoanBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_agency_loan,
                    parent, false);
            return new ViewHolder(binding);

        }
    }

    // SectionViewHolder Class for Sections
    class SectionViewHolder extends RecyclerView.ViewHolder {

        final ItemRecyclerSectionBinding sectionBinding;

        SectionViewHolder(ItemRecyclerSectionBinding sectionBinding) {
            super(sectionBinding.getRoot());
            this.sectionBinding = sectionBinding;
        }
    }

    // ItemViewHolder Class for Items in each Section
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ListItemAgencyLoanBinding binding;
        private LinearLayout linearLayout_childItems;
        private Context context;
        private CardView _parentName;

        ViewHolder(ListItemAgencyLoanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            context = itemView.getContext();
            _parentName = itemView.findViewById(R.id.item_view);
            linearLayout_childItems = itemView.findViewById(R.id.ll_child_items);
            linearLayout_childItems.setVisibility(View.GONE);

            int intMaxNoOfChild = 7;

            for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) {
                TextView textView = new TextView(context);
                textView.setId(indexView);
                textView.setPadding(0, 10, 0, 10);
                textView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout_childItems.addView(textView, layoutParams);
            }
            _parentName.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item_view) {
                if (linearLayout_childItems.getVisibility() == View.VISIBLE) {
                    linearLayout_childItems.setVisibility(View.GONE);
                } else {
                    linearLayout_childItems.setVisibility(View.VISIBLE);
                }
            } /*else {
                TextView textViewClicked = (TextView) view;
                Toast.makeText(context, "" + textViewClicked.getText().toString(), Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}
