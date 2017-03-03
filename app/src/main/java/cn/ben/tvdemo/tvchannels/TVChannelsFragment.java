package cn.ben.tvdemo.tvchannels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ben.tvdemo.BaseFragment;
import cn.ben.tvdemo.R;
import cn.ben.tvdemo.data.Injection;
import cn.ben.tvdemo.data.tvchannel.TVChannels;
import cn.ben.tvdemo.tvshows.TVShowsActivity;
import cn.ben.tvdemo.tvshows.TVShowsFragment;

public class TVChannelsFragment extends BaseFragment implements TVChannelsContract.View, SwipeRefreshLayout.OnRefreshListener {
    public static final String ARGUMENT_TV_TYPE_ID = "TV_TYPE_ID";

    @BindView(R.id.channels_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.channels_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TVChannelsAdapter mAdapter;
    private TVChannelsContract.Presenter mPresenter;

    public static TVChannelsFragment newInstance(String id) {
        TVChannelsFragment tvChannelsFragment = new TVChannelsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TV_TYPE_ID, id);
        tvChannelsFragment.setArguments(bundle);
        return tvChannelsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TVChannelsPresenter(
                getArguments().getString(ARGUMENT_TV_TYPE_ID, ""),
                Injection.provideTVChannelsRepository(),
                this,
                Injection.provideSchedulerProvider()
        );
        mAdapter = new TVChannelsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tv_channels_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        // increase the distance to avoid incorrect operation
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int spinnerOffsetEnd = (int) (64 * metrics.density);
        mSwipeRefreshLayout.setDistanceToTriggerSync(spinnerOffsetEnd * 4);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            mPresenter.onUserVisible();
        } else {
            mPresenter.onUserInvisible();
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshTVChannels();
    }

    @Override
    public void showTips(String m) {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showTVChannels(List<TVChannels.TVChannel> tvChannels) {
        mAdapter.updateData(tvChannels);
    }

    @Override
    public void stopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public class TVChannelsAdapter extends RecyclerView.Adapter<TVChannelsAdapter.ViewHolder> implements View.OnClickListener {
        private final List<TVChannels.TVChannel> mChannelList;

        private TVChannelsAdapter() {
            mChannelList = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            itemView.setOnClickListener(this);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mChannelList.get(position).getChannelName());
            holder.itemView.setTag(mChannelList.get(position).getRel());
        }

        @Override
        public int getItemCount() {
            return mChannelList.size();
        }

        void updateData(List<TVChannels.TVChannel> tvChannels) {
            mChannelList.clear();
            mChannelList.addAll(tvChannels);
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {
            String code = (String) v.getTag();
            showTVShows(code);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(android.R.id.text1)
            TextView mTextView;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void showTVShows(String code) {
        Intent intent = new Intent(getContext(), TVShowsActivity.class);
        intent.putExtra(TVShowsFragment.ARGUMENT_CHANNEL_CODE_KEY, code);
        startActivity(intent);
    }
}
