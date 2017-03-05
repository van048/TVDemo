package cn.ben.tvdemo.tvchannels;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ben.tvdemo.R;

public class TVChannelsActivity extends AppCompatActivity {

    @BindView(R.id.channels_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvchannels_act);
        ButterKnife.bind(this);

        setupActionBar();

        String tvTypeId = getIntent().getStringExtra(TVChannelsFragment.ARGUMENT_TV_TYPE_ID);
        TVChannelsFragment tvChannelsFragment = (TVChannelsFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tvChannelsFragment == null) {
            tvChannelsFragment = TVChannelsFragment.newInstance(tvTypeId);
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, tvChannelsFragment).commit();
        }
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(getIntent().getStringExtra(TVChannelsFragment.ARGUMENT_TV_TYPE_NAME));
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
}
