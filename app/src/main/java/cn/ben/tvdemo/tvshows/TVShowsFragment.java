package cn.ben.tvdemo.tvshows;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ben.tvdemo.BaseFragment;
import cn.ben.tvdemo.R;
import cn.ben.tvdemo.data.Injection;

public class TVShowsFragment extends BaseFragment implements TVShowsContract.View {

    public static final String ARGUMENT_CHANNEL_CODE_KEY = "CHANNEL_CODE_KEY";
    private static final String ARGUMENT_INC_KEY = "INC_KEY";
    public static final String ARGUMENT_CHANNEL_NAME_KEY = "CHANNEL_NAME_KEY";

    @BindView(R.id.shows_recycler_view)
    RecyclerView mRecyclerView;

    private TVShowsContract.Presenter mPresenter;
    private ShowsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TVShowsPresenter(
                getArguments().getString(ARGUMENT_CHANNEL_CODE_KEY, ""),
                this,
                Injection.provideSchedulerProvider());
        mAdapter = new ShowsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tvshows_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            mPresenter.onUserVisible();
        } else {
            mPresenter.onUserInvisible();
        }
    }

    public static TVShowsFragment newInstance(String code, int inc) {
        TVShowsFragment tvShowsFragment = new TVShowsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CHANNEL_CODE_KEY, code);
        bundle.putInt(ARGUMENT_INC_KEY, inc);
        tvShowsFragment.setArguments(bundle);
        return tvShowsFragment;
    }

    private class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
