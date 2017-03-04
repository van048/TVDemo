package cn.ben.tvdemo.tvshows;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ben.tvdemo.R;

public class TVShowsActivity extends AppCompatActivity {

    @BindView(R.id.shows_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.shows_view_pager)
    ViewPager mViewPager;

    private static final int SHOWS_TAB_COUNT = 7;
    private final String[] DAY_NAME = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private int mCurrentDay;
    private DailyShowsAdapter mAdapter;
    private String mCode;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvshows_act);
        ButterKnife.bind(this);

        mCode = getIntent().getStringExtra(TVShowsFragment.ARGUMENT_CHANNEL_CODE_KEY);
        mName = getIntent().getStringExtra(TVShowsFragment.ARGUMENT_CHANNEL_NAME_KEY);

        setupActionBar();
        setupTabLayout();
        setupTabContent();
    }

    private void setupTabContent() {
        mAdapter = new DailyShowsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupTabLayout() {
        Calendar calendar = Calendar.getInstance();
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        mCurrentDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (isFirstSunday) {
            mCurrentDay -= 1;
            if (mCurrentDay == 0) {
                mCurrentDay = 7;
            }
        }

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private int calActualDay(int inc) {
        return (mCurrentDay + inc > 7) ? mCurrentDay + inc - 7 : mCurrentDay + inc;
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(mName);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DailyShowsAdapter extends FragmentPagerAdapter {
        DailyShowsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TVShowsFragment.newInstance(mCode, position);
        }

        @Override
        public int getCount() {
            return SHOWS_TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return DAY_NAME[calActualDay(position)];
        }
    }
}
