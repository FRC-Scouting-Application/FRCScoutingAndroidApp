package ca.tnoah.frc.scouting.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<String> search = new MutableLiveData<>("");
    private final MutableLiveData<String> page = new MutableLiveData<>("teams");

    public MutableLiveData<String> getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search.setValue(search);
    }

    public MutableLiveData<String> getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page.setValue(page);
    }
}
