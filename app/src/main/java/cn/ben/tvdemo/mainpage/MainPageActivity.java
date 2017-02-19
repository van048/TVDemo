package cn.ben.tvdemo.mainpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.ben.tvdemo.R;
import cn.ben.tvdemo.favorite.FavoriteFragment;
import cn.ben.tvdemo.favorite.FavoritePresenter;
import cn.ben.tvdemo.settings.SettingsFragment;
import cn.ben.tvdemo.settings.SettingsPresenter;
import cn.ben.tvdemo.shows.ShowsFragment;
import cn.ben.tvdemo.shows.ShowsPresenter;

import static cn.ben.tvdemo.mainpage.MainPageActivity.FragmentPosition.FAV_FRAGMENT_POS;
import static cn.ben.tvdemo.mainpage.MainPageActivity.FragmentPosition.SETTINGS_FRAGMENT_POS;
import static cn.ben.tvdemo.mainpage.MainPageActivity.FragmentPosition.SHOWS_FRAGMENT_POS;

public class MainPageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    private int prevSelectedMenuItemPos = -1;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == prevSelectedMenuItemPos) return;
        if (prevSelectedMenuItemPos >= 0 && prevSelectedMenuItemPos < mBottomNavigationView.getMenu().size())
            mBottomNavigationView.getMenu().getItem(prevSelectedMenuItemPos).setChecked(false);
        if (position >= 0 && position < mBottomNavigationView.getMenu().size())
            mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        prevSelectedMenuItemPos = position;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    enum FragmentPosition {
        SHOWS_FRAGMENT_POS,
        FAV_FRAGMENT_POS,
        SETTINGS_FRAGMENT_POS
    }

    private ShowsPresenter mShowsPresenter;
    private FavoritePresenter mFavoritePresenter;
    private SettingsPresenter mSettingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_act);

        setupActionBar();
        setupViewPager();
        bindBottomNavigationWithPager();

        if (savedInstanceState != null) {
            // TODO: 2017/2/19 load previously saved state
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
    }

    private void bindBottomNavigationWithPager() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setupViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        MainPagePagerAdapter viewPagerAdapter = new MainPagePagerAdapter(getSupportFragmentManager());

        ShowsFragment showsFragment = (ShowsFragment) viewPagerAdapter.getItem(SHOWS_FRAGMENT_POS);
        if (showsFragment == null) {
            showsFragment = ShowsFragment.newInstance();
            viewPagerAdapter.setFragment(SHOWS_FRAGMENT_POS, showsFragment);
        }
        mShowsPresenter = new ShowsPresenter(showsFragment); // TODO: 2017/2/19

        FavoriteFragment favoriteFragment = (FavoriteFragment) viewPagerAdapter.getItem(FAV_FRAGMENT_POS);
        if (favoriteFragment == null) {
            favoriteFragment = FavoriteFragment.newInstance();
            viewPagerAdapter.setFragment(FAV_FRAGMENT_POS, favoriteFragment);
        }
        mFavoritePresenter = new FavoritePresenter(favoriteFragment); // TODO: 2017/2/19

        SettingsFragment settingsFragment = (SettingsFragment) viewPagerAdapter.getItem(SETTINGS_FRAGMENT_POS);
        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
            viewPagerAdapter.setFragment(SETTINGS_FRAGMENT_POS, settingsFragment);
        }
        mSettingsPresenter = new SettingsPresenter(settingsFragment); // TODO: 2017/2/19

        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

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
}
