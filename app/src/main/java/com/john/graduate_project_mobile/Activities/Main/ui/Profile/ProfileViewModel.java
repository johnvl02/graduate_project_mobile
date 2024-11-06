package com.john.graduate_project_mobile.Activities.Main.ui.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<String> username;

    public ProfileViewModel() {
        username = new MutableLiveData<>();
        username.setValue("Test T");
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public void setUsername(String s) {
        username.setValue(s);
    }
}
