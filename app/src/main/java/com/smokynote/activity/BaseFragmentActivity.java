package com.smokynote.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.yandex.metrica.YandexMetrica;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public abstract class BaseFragmentActivity extends SherlockFragmentActivity {

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.onResumeActivity(this);
    }

    @Override
    protected void onPause() {
        YandexMetrica.onPauseActivity(this);
        super.onPause();
    }
}
