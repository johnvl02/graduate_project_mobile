package com.john.graduate_project_mobile.Activities.Main.ui.Rent;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RentViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Integer> backId;

    public RentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(null);
        backId = new MutableLiveData<>();
        backId.setValue(null);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String license) {
        mText.setValue(license);
    }

    public LiveData<Integer> getID() {
        return backId;
    }

    public void setID(int id) {
        backId.setValue(id);
    }
}