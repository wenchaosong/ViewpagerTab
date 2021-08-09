# ViewpagerTab

[![](https://jitpack.io/v/wenchaosong/ViewpagerTab.svg)](https://jitpack.io/#wenchaosong/ViewpagerTab)
[![](https://img.shields.io/github/stars/wenchaosong/ViewpagerTab.svg)](https://github.com/wenchaosong/ViewpagerTab)

在原项目[SmartTabLayout](https://github.com/ogaclejapan/SmartTabLayout) 基础上
增加三角形 tab 属性

## 使用步骤

#### Step 1

Gradle
```
dependencies{
    implementation 'com.ms:viewpagertab-androidx:1.1.0'
}
```
或者引用本地lib
```groovy
compile project(':library')
```

#### Step 2

```xml
<!--<com.ms.tab.SmartTabLayout
        android:id="@+id/tab_layout"
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:background="#fff"
        app:stl_checkedTabTextSize="15sp"
        <!-- 选中粗体 -->
        app:stl_checkedTextBold="true"
        app:stl_allTextBold="true"
        app:stl_clickable="true"
        <!-- 字母大写 -->
        app:stl_defaultTabTextAllCaps="true"
        app:stl_defaultTabTextColor="@color/item_tab"
        app:stl_customTabTextLayoutId="@layout/tab_layout"
        app:stl_defaultTabTextHorizontalPadding="30dp"
        app:stl_defaultTabTextMinWidth="50dp"
        app:stl_defaultTabTextSize="14sp"
        <!-- 均分 -->
        app:stl_distributeEvenly="false"
        <!-- tab分割线 -->
        app:stl_dividerColor="#d2d2d2"
        app:stl_dividerThickness="1dp"
        <!-- 绘制本身还是子View -->
        app:stl_drawDecorationAfterTab="false"
        <!-- indicator居中 -->
        app:stl_indicatorAlwaysInCenter="false"
        app:stl_indicatorColor="#849EEA"
        app:stl_indicatorCornerRadius="0dp"
        <!-- indicator位置,居中就是横线 -->
        app:stl_indicatorGravity="center"
        <!-- 绘制本身还是子View -->
        app:stl_indicatorInFront="false"
        app:stl_indicatorInterpolation="smart"
        app:stl_indicatorThickness="2dp"
        <!-- 三角形indicator -->
        app:stl_indicatorTrig="true"
        app:stl_indicatorTrigHeight="5dp"
        app:stl_indicatorTrigWidth="8dp"
        app:stl_indicatorWidth="auto"
        app:stl_indicatorWithoutPadding="true"
        <!-- 上横线 -->
        app:stl_overlineColor="#849EEA"
        app:stl_overlineThickness="0dp"
        app:stl_titleOffset="auto_center"
        <!-- 下滑线 -->
        app:stl_underlineColor="#e1e1e1"
        app:stl_underlineThickness="0.5dp" />-->
```

#### Step 3
```java
        SmartTabLayout tabLayout = (SmartTabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(vpAdapter);
        tabLayout.setViewPager(viewPager);
```
