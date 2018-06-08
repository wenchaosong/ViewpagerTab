package com.tab;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.ms.tab.SmartTabLayout;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartTabLayout tabLayout = (SmartTabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        NewsTabAdapter vpAdapter = new NewsTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpAdapter);
        tabLayout.setViewPager(viewPager);
    }
}
