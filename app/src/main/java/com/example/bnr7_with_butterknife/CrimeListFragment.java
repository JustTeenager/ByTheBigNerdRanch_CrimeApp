package com.example.bnr7_with_butterknife;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment that contains a RecyclerView with an adapter&VH
 * Also checks and updates info about crimes that user made in {@link CrimeActivity}
 */

public class CrimeListFragment extends Fragment {
    @BindView(R.id.crimeRecyclerView)
    RecyclerView mCrimeRecyclerView;
    private Unbinder unbinder;
    private int mItemToUpdate=-1;

    private CrimeAdapter mCrimeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(); //to update the info right after the user clicked "back" on CrimeFragment
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateUI(){
        if (mCrimeAdapter==null) {
            mCrimeAdapter = new CrimeAdapter((LinkedHashMap<UUID, Crime>) CrimeLab.getCrimeLab(getActivity()).getCrimes());
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        }
        else if (mItemToUpdate>-1) {
            mCrimeAdapter.notifyItemChanged(mItemToUpdate);
            mItemToUpdate=-1;
        }
        else mCrimeAdapter.notifyDataSetChanged();
    }

    //so cool Kappa
    public static CrimeListFragment newInstance(){
        CrimeListFragment fragment=new CrimeListFragment();
        return fragment;
    }


    class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //binds another crime every time
        private Crime mCrime;

        @BindView(R.id.CrimeTitle)
        TextView mCrimeTitle;
        @BindView(R.id.CrimeDate)
        TextView mCrimeDate;
        @BindView(R.id.crime_solved)
        ImageView mCrimeSolved;

        public CrimeHolder(View v) {
            super(v);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,v);//butterknife needs the same View that we used for a super();
        }

        //go for details of crime
        @Override
        public void onClick(View v) {
            Intent intent=CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            mItemToUpdate=this.getAdapterPosition();
            startActivity(intent);
        }

        private void bind(Crime crime){
            mCrime = crime;
            mCrimeTitle.setText(mCrime.getTitle());
            mCrimeDate.setText(DateFormat.format("yyyy/MM/dd HH:mm",mCrime.getDate()));
            mCrimeSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }


    private class  CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private LinkedHashMap<UUID,Crime> mCrimes;
        public CrimeAdapter(Map<UUID,Crime> crimes) {
            mCrimes= (LinkedHashMap<UUID, Crime>) crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v=LayoutInflater.from(getActivity()).inflate(R.layout.crime_list_item,parent,false);
            CrimeHolder holder=new CrimeHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            holder.bind((Crime) CrimeLab.getCrimeLab(getActivity()).getCrimes().values().toArray()[position]);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}