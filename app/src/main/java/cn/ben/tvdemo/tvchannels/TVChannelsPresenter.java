package cn.ben.tvdemo.tvchannels;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class TVChannelsPresenter implements TVChannelsContract.Presenter {
    private final String mTVTypeId;
    private final TVChannelsContract.View mTVChannelsView;

    public TVChannelsPresenter(@NonNull String id, @NonNull TVChannelsContract.View view) {
        mTVTypeId = checkNotNull(id);
        mTVChannelsView = checkNotNull(view);
    }

    @Override
    public void onUserVisible() {

    }

    @Override
    public void onUserInvisible() {

    }
}
