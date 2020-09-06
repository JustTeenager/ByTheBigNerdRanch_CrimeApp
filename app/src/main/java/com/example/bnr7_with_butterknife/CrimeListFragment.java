package com.example.bnr7_with_butterknife;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

    @BindView(R.id.emptyText)
    TextView mEmptyTextView;

    private Unbinder unbinder;
    private int mItemToUpdate=-1;
    private boolean mIsSubtitleShown=false;
    private static final String KEY_TO_TITLEVIS="titlevisState";

    private CrimeAdapter mCrimeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //save the subtitle state when configuration is changing
        if (savedInstanceState!=null){
            mIsSubtitleShown=savedInstanceState.getBoolean(KEY_TO_TITLEVIS);
        }

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_TO_TITLEVIS,mIsSubtitleShown);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addCrime:{
                Intent intent = CrimePagerActivity.newIntent(getActivity(),CrimeLab.getCrimeLab(getActivity()).addCrime());
                startActivity(intent);
                return true;
            }

            case R.id.subtitle:{
                //changes the subtitle vis and text
                mIsSubtitleShown=!mIsSubtitleShown;
                updateTitle();
                item.setTitle(mIsSubtitleShown ? getResources().getString(R.string.hide_subtitle) : getResources().getString(R.string.show_subtitle));
                getActivity().invalidateOptionsMenu();
                return true;
            }

            default: {return super.onOptionsItemSelected(item);}
        }
    }

    private void updateEmptyTitleVis(){
        if (CrimeLab.getCrimeLab(getActivity()).getCrimes().size()!=0)
            mEmptyTextView.setVisibility(TextView.GONE);
        else mEmptyTextView.setVisibility(TextView.VISIBLE);
    }

    private void updateTitle(){
        String title=getResources().getQuantityString(R.plurals.crime_format,CrimeLab.getCrimeLab(getActivity()).getCrimes().size(),CrimeLab.getCrimeLab(getActivity()).getCrimes().size());
        if (!mIsSubtitleShown) title=null;
        AppCompatActivity activity= (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(); //to update the info right after the user clicked "back" on CrimeFragment
        updateEmptyTitleVis();
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
            // idk how can i do it right
            // the problem is when i click delete on CrimeFragment the adapter do not know
            // what exactly i did:changed element or deleted it
            // so i decided to notify both
            //and in somehow worked out lol
            mCrimeAdapter.notifyItemRemoved(mItemToUpdate);
            mCrimeAdapter.notifyItemRangeChanged(mItemToUpdate,CrimeLab.getCrimeLab(getActivity()).getCrimes().size());
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
            ButterKnife.bind(this,v);
            //so we can touch our Holders
            itemView.setOnClickListener(this);
           //butterknife needs the same View that we used for a super();
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