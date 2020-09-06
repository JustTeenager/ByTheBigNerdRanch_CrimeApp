package com.example.bnr7_with_butterknife;

import android.content.Context;
import android.view.MenuItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Singleton Class that provides a lot of methods for working with list of {@link Crime}
 */

public class CrimeLab {
    private static CrimeLab mCrimeLab;
    private static LinkedHashMap<UUID,Crime> mCrimes;

    private CrimeLab(Context context){
        mCrimes=new LinkedHashMap<>();
    }

    public static CrimeLab getCrimeLab(Context context){
        if (mCrimeLab == null) mCrimeLab=new CrimeLab(context);
        return mCrimeLab;
    }

    public Map<UUID,Crime> getCrimes(){
        return (LinkedHashMap<UUID,Crime>) mCrimes;
    }


    /**
     * adds new Crime to the list
     * @return the id of new crime so we dont need to create a crime on Controller side
     * @see CrimeListFragment#onOptionsItemSelected(MenuItem)
     */
    public UUID addCrime(){
        Crime crime=new Crime();
        mCrimes.put(crime.getId(),crime);
        return crime.getId();
    }

    public void deleteCrime(Crime crime){
        mCrimes.remove(crime.getId());
    }

    public Crime getCrimeById(UUID id){
        return mCrimes.get(id);
    }
}
