package com.john.graduate_project_mobile.Activities.Main.ui.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyProfileViewModel extends ViewModel {
    private final MutableLiveData<String> name;
    private final MutableLiveData<String> email;
    private final MutableLiveData<String> phone;
    private final MutableLiveData<String> age;

    public MyProfileViewModel() {
        name = new MutableLiveData<>();
        name.setValue("Test T");
        email = new MutableLiveData<>();
        email.setValue("test");
        phone = new MutableLiveData<>();
        phone.setValue("6912345678");
        age = new MutableLiveData<>();
        age.setValue("20");
    }

    public LiveData<String> getName() {
        return name;
    }

    public void setName(String s) {
        name.setValue(s);
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void setEmail(String s) {
        email.setValue(s);
    }
    public LiveData<String> getPhone() {
        return phone;
    }

    public void setPhone(String s) {
        phone.setValue(s);
    }

    public LiveData<String> getAge() {
        return age;
    }

    public void setAge(String s) {
        age.setValue(s);
    }

}
