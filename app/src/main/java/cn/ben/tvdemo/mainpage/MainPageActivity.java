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

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_nav)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;

    private int prevSelectedMenuItemPos = -1;
    private boolean mRefreshShowsWhenInit;

    private ShowsPresenter mShowsPresenter;
    private FavoritePresenter mFavoritePresenter;
    private SettingsPresenter mSettingsPresenter;

    enum FragmentPosition {
        SHOWS_FRAGMENT_POS,
        FAV_FRAGMENT_POS,
        SETTINGS_FRAGMENT_POS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // TODO: 2017/2/19 load previously saved state
            mRefreshShowsWhenInit = false;
        } else {
            mRefreshShowsWhenInit = true;
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
    }

    private void setupBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setupViewPager() {
        MainPagePagerAdapter viewPagerAdapter = new MainPagePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);

        // http://stackoverflow.com/questions/14035090/how-to-get-existing-fragments-when-using-fragmentpageradapter/41345283#41345283
        viewPagerAdapter.startUpdate(mViewPager);

        ShowsFragment showsFragment = (ShowsFragment) viewPagerAdapter.instantiateItem(mViewPager, SHOWS_FRAGMENT_POS);
        mShowsPresenter = new ShowsPresenter(TVTypesRepository.getInstance(TVTypesRemoteDataSource.getInstance(), TVTypesLocalDataSource.getInstance(this)), showsFragment, mRefreshShowsWhenInit); // TODO: 2017/2/19

        FavoriteFragment favoriteFragment = (FavoriteFragment) viewPagerAdapter.instantiateItem(mViewPager, FAV_FRAGMENT_POS);
        mFavoritePresenter = new FavoritePresenter(favoriteFragment); // TODO: 2017/2/19

        SettingsFragment settingsFragment = (SettingsFragment) viewPagerAdapter.instantiateItem(mViewPager, SETTINGS_FRAGMENT_POS);
        mSettingsPresenter = new SettingsPresenter(settingsFragment); // TODO: 2017/2/19

        viewPagerAdapter.finishUpdate(mViewPager);
        // finish

        mViewPager.addOnPageChangeListener(this);
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

        Object instantiateItem(ViewPager container, MainPageActivity.FragmentPosition position) {
            return instantiateItem(container, position.ordinal());
        }
    }
}
