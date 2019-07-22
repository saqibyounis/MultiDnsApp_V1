package com.kamel.tivi.multiviewfragment.model;

import android.support.v4.app.Fragment;

public class MultiViewModel {
    String url;
    Fragment fragment;

    public MultiViewModel(String url, Fragment fragment) {
        this.url = url;
        this.fragment = fragment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
