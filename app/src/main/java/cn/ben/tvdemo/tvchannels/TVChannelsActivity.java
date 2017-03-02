package cn.ben.tvdemo.tvchannels;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.ben.tvdemo.R;

public class TVChannelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvchannels_act);

        String tvTypeId = getIntent().getStringExtra(TVChannelsFragment.ARGUMENT_TV_TYPE_ID);
        TVChannelsFragment tvChannelsFragment = (TVChannelsFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tvChannelsFragment == null) {
            tvChannelsFragment = TVChannelsFragment.newInstance(tvTypeId);
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, tvChannelsFragment).commit();
        }
    }
}
