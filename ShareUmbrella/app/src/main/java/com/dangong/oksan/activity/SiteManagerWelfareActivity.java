package com.dangong.oksan.activity;

import android.view.View;

import com.dangong.oksan.R;
import com.dangong.oksan.activity.base.BaseActivity;

public class SiteManagerWelfareActivity extends BaseActivity {



    @Override
    public int getLayoutId() {
        return R.layout.activity_guide_welfare;
    }

    @Override
    public String setTitle() {
        return getString(R.string.guide_welfare);
    }

    @Override
    public void init() {
        super.init();
        ownInfoIv.setVisibility(View.VISIBLE);

    }

}
