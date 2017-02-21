package cn.ben.tvdemo.mainpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.ben.tvdemo.R;
import cn.ben.tvdemo.data.tvtype.source.TVTypesRepository;
import cn.ben.tvdemo.data.tvtype.source.local.TVTypesLocalDataSource;
import cn.ben.tvdemo.data.tvtype.source.remote.TVTypesRemoteDataSource;
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
    }

    @Override
    public void onPageSelected(int position) {
        if (position == prevSelectedMenuItemPos) return;
        if (prevSelectedMenuItemPos >= 0 && prevSelectedMenuItemPos < mBottomNavigationView.getMenu().size())
            mBottomNavigationView.getMenu().getItem(prevSelectedMenuItemPos).setChecked(false);
        if (position >= 0 && position < mBottomNavigationView.getMenu().size())
            mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        prevSelectedMenuItemPos = position;
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

        // http://stackoverflow.com/questions/14035090/how-to-get-existing-fragments-when-using-fragmentpageradapter/41345283#41345283
        viewPagerAdapter.startUpdate(mViewPager);

        ShowsFragment showsFragment = (ShowsFragment) viewPagerAdapter.instantiateItem(mViewPager, SHOWS_FRAGMENT_POS);
        mShowsPresenter = new ShowsPresenter(TVTypesRepository.getInstance(TVTypesRemoteDataSource.getInstance(), TVTypesLocalDataSource.getInstance(this)), showsFragment); // TODO: 2017/2/19

        FavoriteFragment favoriteFragment = (FavoriteFragment) viewPagerAdapter.instantiateItem(mViewPager, FAV_FRAGMENT_POS);
        mFavoritePresenter = new FavoritePresenter(favoriteFragment); // TODO: 2017/2/19

        SettingsFragment settingsFragment = (SettingsFragment) viewPagerAdapter.instantiateItem(mViewPager, SETTINGS_FRAGMENT_POS);
        mSettingsPresenter = new SettingsPresenter(settingsFragment); // TODO: 2017/2/19

        viewPagerAdapter.finishUpdate(mViewPager);
        // finish

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

        Object instantiateItem(ViewPager container, MainPageActivity.FragmentPosition position) {
            return instantiateItem(container, position.ordinal());
        }
    }
}
