package com.hanul.team1.triplan.ysh.objectbox;

import android.app.Application;

import com.hanul.team1.triplan.BuildConfig;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class App extends Application {
    public static final boolean EXTERNAL_DIR = false;
    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
        if(BuildConfig.DEBUG){
            new AndroidObjectBrowser(boxStore).start(this);
        }
    }
    public BoxStore getBoxStore(){
        return boxStore;
    }
}
