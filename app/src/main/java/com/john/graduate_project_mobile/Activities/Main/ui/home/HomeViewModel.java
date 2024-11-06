package com.john.graduate_project_mobile.Activities.Main.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Integer> backId;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(null);
        backId = new MutableLiveData<>();
        backId.setValue(null);
    }

    public LiveData<String> getData() {
        return mText;
    }

    public void setData(String license) {
        mText.setValue(license);
    }

    public LiveData<Integer> getID() {
        return backId;
    }

    public void setID(int id) {
        backId.setValue(id);
    }
}