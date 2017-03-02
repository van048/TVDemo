package cn.ben.tvdemo;

import android.support.v4.app.Fragment;

// http://www.jianshu.com/p/850556d33f63
public abstract class BaseFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();

        if (getUserVisibleHint()) {
            onVisibilityChangedToUser(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getUserVisibleHint()) {
            onVisibilityChangedToUser(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isResumed()) {
            onVisibilityChangedToUser(isVisibleToUser);
        }
    }

    protected abstract void onVisibilityChangedToUser(boolean isVisibleToUser);
}
