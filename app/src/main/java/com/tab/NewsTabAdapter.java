package com.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by songwenchao
 * on 2018/6/6 0006.
 * <p>
 * 类名
 * 需要 --
 * 可以 --
 */
public class NewsTabAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"旅游", "美食", "职场", "生活"};

    public NewsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return NewsTabFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
