package cn.ben.tvdemo.mainpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import cn.ben.tvdemo.R;
import cn.ben.tvdemo.data.tvchannel.source.TVChannelsRepository;
import cn.ben.tvdemo.data.tvtype.source.TVTypesRepository;
import cn.ben.tvdemo.mainpage.favorite.FavoriteFragment;
import cn.ben.tvdemo.mainpage.settings.SettingsFragment;
import cn.ben.tvdemo.mainpage.shows.ShowsFragment;

import static cn.ben.tvdemo.mainpage.MainPageActivity.FragmentPosition.FAV_FRAGMENT_POS;
import static cn.ben.tvdemo.mainpage.MainPageActivity.FragmentPosition.SETTINGS_FRAGMENT_POS;
import static cn.ben.tvdemo.mainpage.MainPageActivity.FragmentPosition.SHOWS_FRAGMENT_POS;

@SuppressWarnings("WeakerAccess")
public class MainPageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_nav)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;

    private int mPrevSelectedMenuItemPos = -1;
    private boolean mCreatedFromSavedInstance;

    enum FragmentPosition {
        SHOWS_FRAGMENT_POS,
        FAV_FRAGMENT_POS,
        SETTINGS_FRAGMENT_POS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // TODO: 2017/2/19 load previously saved state
            mCreatedFromSavedInstance = true;
        } else {
            mCreatedFromSavedInstance = false;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_act);
        ButterKnife.bind(this);

        setupToolbar();
        setupViewPager();
        setupBottomNavigationView();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupViewPager() {
        MainPagePagerAdapter viewPagerAdapter = new MainPagePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);
    }

    private void setupBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TVTypesRepository.destroyInstance();
        TVChannelsRepository.destroyInstance();
    }

    // BottomNavigationView.OnNavigationItemSelectedListener start
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_shows:
                mViewPager.setCurrentItem(SHOWS_FRAGMENT_POS.ordinal());
                break;
            case R.id.menu_item_fav:
                mViewPager.setCurrentItem(FAV_FRAGMENT_POS.ordinal());
                break;
            case R.id.menu_item_settings:
                mViewPager.setCurrentItem(SETTINGS_FRAGMENT_POS.ordinal());
                break;
            default:
                break;
        }
        return true;
    }
    // BottomNavigationView.OnNavigationItemSelectedListener end

    // ViewPager.OnPageChangeListener start
    @OnPageChange(value = R.id.viewpager, callback = OnPageChange.Callback.PAGE_SELECTED)
    public void onPageSelected(int position) {
        if (position == mPrevSelectedMenuItemPos) return;
        if (mPrevSelectedMenuItemPos >= 0 && mPrevSelectedMenuItemPos < mBottomNavigationView.getMenu().size())
            mBottomNavigationView.getMenu().getItem(mPrevSelectedMenuItemPos).setChecked(false);
        if (position >= 0 && position < mBottomNavigationView.getMenu().size())
            mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        mPrevSelectedMenuItemPos = position;
    }
    // ViewPager.OnPageChangeListener end

    class MainPagePagerAdapter extends FragmentPagerAdapter {

        MainPagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == SHOWS_FRAGMENT_POS.ordinal()) {
                return ShowsFragment.newInstance();
            } else if (position == FAV_FRAGMENT_POS.ordinal()) {
                return FavoriteFragment.newInstance();
            } else if (position == SETTINGS_FRAGMENT_POS.ordinal()) {
                return SettingsFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return FragmentPosition.values().length;
        }
    }
}
