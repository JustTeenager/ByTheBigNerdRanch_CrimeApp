package com.example.bnr7_with_butterknife;

import android.content.Context;

import java.util.Date;
import java.util.UUID;

/**
 * Model class with info about date/time,solved or not and id
 * get mostly used with {@link CrimeLab#getCrimeLab(Context)} ()}
 * @author Kotov&BigNerdRanch
 */


public class Crime {
    private UUID mUUID;
    private String mTitle;
    private boolean mSolved;
    private Date mDate;

    public Crime(){
        this.mDate=new Date();
        this.mUUID=UUID.randomUUID();
    }

    public UUID getId() {
        return mUUID;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
