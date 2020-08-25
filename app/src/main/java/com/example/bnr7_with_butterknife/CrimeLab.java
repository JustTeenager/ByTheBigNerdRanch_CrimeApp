package com.example.bnr7_with_butterknife;

import android.content.Context;
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
        for (int i=0;i<100;i++){
            Crime crime=new Crime();
            crime.setSolved(i%2==0);
            crime.setTitle("Crime #"+i);
            mCrimes.put(crime.getId(),crime);
        }
    }

    public static CrimeLab getCrimeLab(Context context){
        if (mCrimeLab == null) mCrimeLab=new CrimeLab(context);
        return mCrimeLab;
    }

    public Map<UUID,Crime> getCrimes(){
        return (LinkedHashMap<UUID,Crime>) mCrimes;
    }

    public Crime getCrimeById(UUID id){
        return mCrimes.get(id);
    }
}
