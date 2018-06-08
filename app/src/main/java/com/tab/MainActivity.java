package com.tab;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.ms.tab.util.Bundler;
import com.ms.tab.fragment.FragmentPagerItemAdapter;
import com.ms.tab.fragment.FragmentPagerItems;
import com.ms.tab.SmartTabLayout;

public class MainActivity extends FragmentActivity {

    private String[] mTitles = new String[]{"旅游", "美食", "职场", "生活"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartTabLayout tabLayout = (SmartTabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        for (int i = 0; i < mTitles.length; i++) {
            creator.add(mTitles[i], NewsTabFragment.class, new Bundler().putString("type", "1").get());
        }
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), creator.create());
        NewsTabAdapter vpAdapter = new NewsTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpAdapter);
        tabLayout.setViewPager(viewPager);
    }
}
