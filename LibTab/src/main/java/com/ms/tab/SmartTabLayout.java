package com.ms.tab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ms.tab.util.Utils;

public class SmartTabLayout extends HorizontalScrollView {

    private static final boolean DEFAULT_DISTRIBUTE_EVENLY = false;
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TITLE_OFFSET_AUTO_CENTER = -1;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final boolean TAB_VIEW_TEXT_ALL_CAPS = true;
    private static final boolean TAB_VIEW_TEXT_BOLD = false;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private static final int TAB_VIEW_TEXT_COLOR = 0xFC000000;
    private static final int TAB_VIEW_TEXT_MIN_WIDTH = 0;
    private static final boolean TAB_CLICKABLE = true;

    protected final SmartTabIndicator tabStrip;
    private int titleOffset;
    private int tabViewBackgroundResId;
    private boolean tabViewTextAllCaps;
    private boolean allBold, checkedBold;
    private ColorStateList tabViewTextColors;
    private float tabViewTextSize, tabViewCheckedTextSize;
    private int tabViewTextHorizontalPadding;
    private int tabViewTextMinWidth;
    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener viewPagerPageChangeListener;
    private OnScrollChangeListener onScrollChangeListener;
    private TabProvider tabProvider;
    private InternalTabClickListener internalTabClickListener;
    private OnTabClickListener onTabClickListener;
    private boolean distributeEvenly;

    public SmartTabLayout(Context context) {
        this(context, null);
    }

    public SmartTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final float density = dm.density;

        int tabBackgroundResId = NO_ID;
        boolean textAllCaps = TAB_VIEW_TEXT_ALL_CAPS;
        ColorStateList textColors;
        int textSize = TAB_VIEW_TEXT_SIZE_SP;
        int checkedTextSize;
        boolean isCheckedBold = TAB_VIEW_TEXT_BOLD;
        boolean isAllBold = TAB_VIEW_TEXT_BOLD;
        int textHorizontalPadding = (int) (TAB_VIEW_PADDING_DIPS * density);
        int textMinWidth = (int) (TAB_VIEW_TEXT_MIN_WIDTH * density);
        boolean distributeEvenly = DEFAULT_DISTRIBUTE_EVENLY;
        int customTabLayoutId = NO_ID;
        int customTabTextViewId = NO_ID;
        boolean clickable = TAB_CLICKABLE;
        int titleOffset = (int) (TITLE_OFFSET_DIPS * density);

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.stl_SmartTabLayout, defStyle, 0);
        tabBackgroundResId = a.getResourceId(
                R.styleable.stl_SmartTabLayout_stl_defaultTabBackground, tabBackgroundResId);
        textAllCaps = a.getBoolean(
                R.styleable.stl_SmartTabLayout_stl_defaultTabTextAllCaps, textAllCaps);
        textColors = a.getColorStateList(
                R.styleable.stl_SmartTabLayout_stl_defaultTabTextColor);
        textSize = a.getDimensionPixelSize(
                R.styleable.stl_SmartTabLayout_stl_defaultTabTextSize, textSize);
        checkedTextSize = a.getDimensionPixelSize(
                R.styleable.stl_SmartTabLayout_stl_checkedTabTextSize, textSize);
        isCheckedBold = a.getBoolean(
                R.styleable.stl_SmartTabLayout_stl_checkedTextBold, isCheckedBold);
        isAllBold = a.getBoolean(
                R.styleable.stl_SmartTabLayout_stl_allTextBold, isAllBold);
        textHorizontalPadding = a.getDimensionPixelSize(
                R.styleable.stl_SmartTabLayout_stl_defaultTabTextHorizontalPadding, textHorizontalPadding);
        textMinWidth = a.getDimensionPixelSize(
                R.styleable.stl_SmartTabLayout_stl_defaultTabTextMinWidth, textMinWidth);
        customTabLayoutId = a.getResourceId(
                R.styleable.stl_SmartTabLayout_stl_customTabTextLayoutId, customTabLayoutId);
        customTabTextViewId = a.getResourceId(
                R.styleable.stl_SmartTabLayout_stl_customTabTextViewId, customTabTextViewId);
        distributeEvenly = a.getBoolean(
                R.styleable.stl_SmartTabLayout_stl_distributeEvenly, distributeEvenly);
        clickable = a.getBoolean(
                R.styleable.stl_SmartTabLayout_stl_clickable, clickable);
        titleOffset = a.getLayoutDimension(
                R.styleable.stl_SmartTabLayout_stl_titleOffset, titleOffset);
        a.recycle();

        this.titleOffset = titleOffset;
        this.tabViewBackgroundResId = tabBackgroundResId;
        this.tabViewTextAllCaps = textAllCaps;
        this.tabViewTextColors = (textColors != null)
                ? textColors
                : ColorStateList.valueOf(TAB_VIEW_TEXT_COLOR);
        this.tabViewTextSize = textSize;
        this.tabViewCheckedTextSize = checkedTextSize;
        this.checkedBold = isCheckedBold;
        this.allBold = isAllBold;
        this.tabViewTextHorizontalPadding = textHorizontalPadding;
        this.tabViewTextMinWidth = textMinWidth;
        this.internalTabClickListener = clickable ? new InternalTabClickListener() : null;
        this.distributeEvenly = distributeEvenly;

        if (customTabLayoutId != NO_ID) {
            setCustomTabView(customTabLayoutId, customTabTextViewId);
        }

        this.tabStrip = new SmartTabIndicator(context, attrs);

        if (distributeEvenly && tabStrip.isIndicatorAlwaysInCenter()) {
            throw new UnsupportedOperationException(
                    "'distributeEvenly' and 'indicatorAlwaysInCenter' both use does not support");
        }

        // Make sure that the Tab Strips fills this View
        setFillViewport(!tabStrip.isIndicatorAlwaysInCenter());

        addView(tabStrip, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChanged(l, oldl);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (tabStrip.isIndicatorAlwaysInCenter() && tabStrip.getChildCount() > 0) {
            View firstTab = tabStrip.getChildAt(0);
            View lastTab = tabStrip.getChildAt(tabStrip.getChildCount() - 1);
            int start = (w - Utils.getMeasuredWidth(firstTab)) / 2 - Utils.getMarginStart(firstTab);
            int end = (w - Utils.getMeasuredWidth(lastTab)) / 2 - Utils.getMarginEnd(lastTab);
            tabStrip.setMinimumWidth(tabStrip.getMeasuredWidth());
            ViewCompat.setPaddingRelative(this, start, getPaddingTop(), end, getPaddingBottom());
            setClipToPadding(false);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // Ensure first scroll
        if (changed && viewPager != null) {
            scrollToTab(viewPager.getCurrentItem(), 0);
        }
    }

    /**
     * Set the behavior of the Indicator scrolling feedback.
     */
    public void setIndicationInterpolator(SmartTabIndicationInterpolator interpolator) {
        tabStrip.setIndicationInterpolator(interpolator);
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p>
     * If you only require simple customisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        tabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Set the color used for styling the tab text. This will need to be called prior to calling
     *
     * @param color to use for tab text
     */
    public void setDefaultTabTextColor(int color) {
        tabViewTextColors = ColorStateList.valueOf(color);
    }

    /**
     * Sets the colors used for styling the tab text. This will need to be called prior to calling
     *
     * @param colors ColorStateList to use for tab text
     */
    public void setDefaultTabTextColor(ColorStateList colors) {
        tabViewTextColors = colors;
    }

    /**
     * Set the same weight for tab
     */
    public void setDistributeEvenly(boolean distributeEvenly) {
        this.distributeEvenly = distributeEvenly;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        tabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setDividerColors(int... colors) {
        tabStrip.setDividerColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link SmartTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        viewPagerPageChangeListener = listener;
    }

    /**
     * Set {@link OnScrollChangeListener} for obtaining values of scrolling.
     *
     * @param listener the {@link OnScrollChangeListener} to set
     */
    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        onScrollChangeListener = listener;
    }

    /**
     * Set {@link OnTabClickListener} for obtaining click event.
     *
     * @param listener the {@link OnTabClickListener} to set
     */
    public void setOnTabClickListener(OnTabClickListener listener) {
        onTabClickListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link android.widget.TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        tabProvider = new SimpleTabProvider(getContext(), layoutResId, textViewId);
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param provider {@link TabProvider}
     */
    public void setCustomTabView(TabProvider provider) {
        tabProvider = provider;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        tabStrip.removeAllViews();

        this.viewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * Returns the view at the specified position in the tabs.
     *
     * @param position the position at which to get the view from
     * @return the view at the specified position or null if the position does not exist within the
     * tabs
     */
    public View getTabAt(int position) {
        return tabStrip.getChildAt(position);
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(CharSequence title, boolean checked) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(title);
        textView.setTextColor(tabViewTextColors);
        if (checked) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabViewCheckedTextSize);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabViewTextSize);
        }
        if (allBold) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            if (checkedBold && checked) {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        if (tabViewBackgroundResId != NO_ID) {
            textView.setBackgroundResource(tabViewBackgroundResId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(tabViewTextAllCaps);
        }

        textView.setPadding(
                tabViewTextHorizontalPadding, 0,
                tabViewTextHorizontalPadding, 0);

        if (tabViewTextMinWidth > 0) {
            textView.setMinWidth(tabViewTextMinWidth);
        }

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = viewPager.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {

            final View tabView = (tabProvider == null)
                    ? createDefaultTabView(adapter.getPageTitle(i), i == viewPager.getCurrentItem())
                    : tabProvider.createTabView(tabStrip, i, adapter);

            if (tabView == null) {
                throw new IllegalStateException("tabView is null.");
            }

            if (distributeEvenly) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            if (internalTabClickListener != null) {
                tabView.setOnClickListener(internalTabClickListener);
            }

            if (i == viewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }

            tabStrip.addView(tabView);

        }
    }

    private void scrollToTab(int tabIndex, float positionOffset) {
        final int tabStripChildCount = tabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        final boolean isLayoutRtl = Utils.isLayoutRtl(this);
        View selectedTab = tabStrip.getChildAt(tabIndex);
        int widthPlusMargin = Utils.getWidth(selectedTab) + Utils.getMarginHorizontally(selectedTab);
        int extraOffset = (int) (positionOffset * widthPlusMargin);

        if (tabStrip.isIndicatorAlwaysInCenter()) {

            if (0f < positionOffset && positionOffset < 1f) {
                View nextTab = tabStrip.getChildAt(tabIndex + 1);
                int selectHalfWidth = Utils.getWidth(selectedTab) / 2 + Utils.getMarginEnd(selectedTab);
                int nextHalfWidth = Utils.getWidth(nextTab) / 2 + Utils.getMarginStart(nextTab);
                extraOffset = Math.round(positionOffset * (selectHalfWidth + nextHalfWidth));
            }

            View firstTab = tabStrip.getChildAt(0);
            int x;
            if (isLayoutRtl) {
                int first = Utils.getWidth(firstTab) + Utils.getMarginEnd(firstTab);
                int selected = Utils.getWidth(selectedTab) + Utils.getMarginEnd(selectedTab);
                x = Utils.getEnd(selectedTab) - Utils.getMarginEnd(selectedTab) - extraOffset;
                x -= (first - selected) / 2;
            } else {
                int first = Utils.getWidth(firstTab) + Utils.getMarginStart(firstTab);
                int selected = Utils.getWidth(selectedTab) + Utils.getMarginStart(selectedTab);
                x = Utils.getStart(selectedTab) - Utils.getMarginStart(selectedTab) + extraOffset;
                x -= (first - selected) / 2;
            }

            scrollTo(x, 0);
            return;

        }

        int x;
        if (titleOffset == TITLE_OFFSET_AUTO_CENTER) {

            if (0f < positionOffset && positionOffset < 1f) {
                View nextTab = tabStrip.getChildAt(tabIndex + 1);
                int selectHalfWidth = Utils.getWidth(selectedTab) / 2 + Utils.getMarginEnd(selectedTab);
                int nextHalfWidth = Utils.getWidth(nextTab) / 2 + Utils.getMarginStart(nextTab);
                extraOffset = Math.round(positionOffset * (selectHalfWidth + nextHalfWidth));
            }

            if (isLayoutRtl) {
                x = -Utils.getWidthWithMargin(selectedTab) / 2 + getWidth() / 2;
                x -= Utils.getPaddingStart(this);
            } else {
                x = Utils.getWidthWithMargin(selectedTab) / 2 - getWidth() / 2;
                x += Utils.getPaddingStart(this);
            }

        } else {

            if (isLayoutRtl) {
                x = (tabIndex > 0 || positionOffset > 0) ? titleOffset : 0;
            } else {
                x = (tabIndex > 0 || positionOffset > 0) ? -titleOffset : 0;
            }

        }

        int start = Utils.getStart(selectedTab);
        int startMargin = Utils.getMarginStart(selectedTab);
        if (isLayoutRtl) {
            x += start + startMargin - extraOffset - getWidth() + Utils.getPaddingHorizontally(this);
        } else {
            x += start - startMargin + extraOffset;
        }

        scrollTo(x, 0);

    }

    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

        /**
         * @return return the color of the divider drawn to the right of {@code position}.
         */
        int getDividerColor(int position);

    }

    /**
     * Interface definition for a callback to be invoked when the scroll position of a view changes.
     */
    public interface OnScrollChangeListener {

        /**
         * Called when the scroll position of a view changes.
         *
         * @param scrollX    Current horizontal scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         */
        void onScrollChanged(int scrollX, int oldScrollX);
    }

    /**
     * Interface definition for a callback to be invoked when a tab is clicked.
     */
    public interface OnTabClickListener {

        /**
         * Called when a tab is clicked.
         *
         * @param position tab's position
         */
        void onTabClicked(int position);
    }

    /**
     * Create the custom tabs in the tab layout. Set with
     */
    public interface TabProvider {

        /**
         * @return Return the View of {@code position} for the Tabs
         */
        View createTabView(ViewGroup container, int position, PagerAdapter adapter);

    }

    private static class SimpleTabProvider implements TabProvider {

        private final LayoutInflater inflater;
        private final int tabViewLayoutId;
        private final int tabViewTextViewId;

        private SimpleTabProvider(Context context, int layoutResId, int textViewId) {
            inflater = LayoutInflater.from(context);
            tabViewLayoutId = layoutResId;
            tabViewTextViewId = textViewId;
        }

        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            View tabView = null;
            TextView tabTitleView = null;

            if (tabViewLayoutId != NO_ID) {
                tabView = inflater.inflate(tabViewLayoutId, container, false);
            }

            if (tabViewTextViewId != NO_ID && tabView != null) {
                tabTitleView = (TextView) tabView.findViewById(tabViewTextViewId);
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            if (tabTitleView != null) {
                tabTitleView.setText(adapter.getPageTitle(position));
            }

            return tabView;
        }

    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        private int scrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = tabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            tabStrip.onViewPagerPageChanged(position, positionOffset);

            scrollToTab(position, positionOffset);

            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            scrollState = state;

            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
                tabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            for (int i = 0, size = tabStrip.getChildCount(); i < size; i++) {
                tabStrip.getChildAt(i).setSelected(position == i);
                if (tabStrip.getChildAt(i) instanceof TextView) {
                    if (position == i) {
                        ((TextView) tabStrip.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_PX, tabViewCheckedTextSize);
                    } else {
                        ((TextView) tabStrip.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_PX, tabViewTextSize);
                    }
                    if (allBold) {
                        ((TextView) tabStrip.getChildAt(i)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    } else {
                        if (checkedBold && position == i) {
                            ((TextView) tabStrip.getChildAt(i)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        } else {
                            ((TextView) tabStrip.getChildAt(i)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        }
                    }
                }
            }

            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class InternalTabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                if (v == tabStrip.getChildAt(i)) {
                    if (onTabClickListener != null) {
                        onTabClickListener.onTabClicked(i);
                    }
                    viewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

}
