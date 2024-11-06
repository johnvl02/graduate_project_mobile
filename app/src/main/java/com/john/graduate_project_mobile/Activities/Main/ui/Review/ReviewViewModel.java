package com.john.graduate_project_mobile.Activities.Main.ui.Review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReviewViewModel extends ViewModel {

    private final MutableLiveData<String> license;
    private final MutableLiveData<String> renter;
    private final MutableLiveData<String> date;
    private final MutableLiveData<Boolean> reviewCar;

    public ReviewViewModel() {
        license = new MutableLiveData<>();
        license.setValue(null);
        renter = new MutableLiveData<>();
        renter.setValue(null);
        date = new MutableLiveData<>();
        date.setValue(null);
        reviewCar = new MutableLiveData<>();
        reviewCar.setValue(false);
    }

    public LiveData<String> getLicense() {
        return license;
    }

    public void setLicense(String val) {
        license.setValue(val);
    }

    public LiveData<String> getRenter() {
        return renter;
    }

    public void setRenter(String val) {
        renter.setValue(val);
    }

    public LiveData<String> getDate() {
        return date;
    }

    public void setDate(String val) {
        date.setValue(val);
    }

    public LiveData<Boolean> getReviewCar() {
        return reviewCar;
    }

    public void setReviewCar(boolean val) {
        reviewCar.setValue(val);
    }
}