package ca.tnoah.frc.scouting.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<String> search = new MutableLiveData<>("");

    public MutableLiveData<String> getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search.setValue(search);
    }
}
