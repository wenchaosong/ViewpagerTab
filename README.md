# ViewpagerTab

[![](https://jitpack.io/v/wenchaosong/ViewpagerTab.svg)](https://jitpack.io/#wenchaosong/ViewpagerTab)
[![Hex.pm](https://img.shields.io/github/stars/wenchaosong/ViewpagerTab.svg)](https://github.com/wenchaosong/ViewpagerTab)

在原项目[SmartTabLayout](https://github.com/ogaclejapan/SmartTabLayout) 基础上
增加三角形 tab 属性

## 使用步骤

#### Step 1

Gradle
```groovy
dependencies{
    compile 'com.github.wenchaosong:ViewpagerTab:2.0.2'
}
```
或者引用本地lib
```groovy
compile project(':library')
```

#### Step 2

```xml
<com.ms.tab.SmartTabLayout
            android:id="@+id/tab_layout"
            android:layout_width="match_parent"
            android:layout_height="45dp"
            android:background="#fff"
            app:stl_clickable="true"
            app:stl_customTabTextLayoutId="@layout/activity_daikuan_tab"
            app:stl_defaultTabTextColor="@color/news_item_tab"
            app:stl_defaultTabTextHorizontalPadding="30dp"
            app:stl_defaultTabTextSize="14sp"
            app:stl_distributeEvenly="false"
            app:stl_dividerThickness="0dp"
            app:stl_drawDecorationAfterTab="false"
            app:stl_indicatorAlwaysInCenter="false"
            app:stl_indicatorColor="#849EEA"
            app:stl_indicatorCornerRadius="0dp"
            app:stl_indicatorGravity="bottom"
            app:stl_indicatorTrig="true"
            app:stl_indicatorTrigHeight="5dp"
            app:stl_indicatorTrigWidth="8dp"
            app:stl_indicatorInFront="true"
            app:stl_indicatorInterpolation="smart"
            app:stl_indicatorThickness="2dp"
            app:stl_indicatorWidth="25dp"
            app:stl_indicatorWithoutPadding="true"
            app:stl_underlineColor="#e1e1e1"
            app:stl_underlineThickness="0.5dp" />
```

#### Step 3
```java
        SmartTabLayout tabLayout = (SmartTabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(vpAdapter);
        tabLayout.setViewPager(viewPager);
```
