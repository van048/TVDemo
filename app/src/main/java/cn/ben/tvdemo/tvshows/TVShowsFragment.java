package cn.ben.tvdemo.tvshows;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ben.tvdemo.BaseFragment;
import cn.ben.tvdemo.R;
import cn.ben.tvdemo.data.Injection;
import cn.ben.tvdemo.data.tvshow.TVShows;
import cn.ben.tvdemo.util.TimeUtil;

public class TVShowsFragment extends BaseFragment implements TVShowsContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARGUMENT_CHANNEL_CODE_KEY = "CHANNEL_CODE_KEY";
    public static final String ARGUMENT_CHANNEL_NAME_KEY = "CHANNEL_NAME_KEY";
    private static final String ARGUMENT_INC_KEY = "INC_KEY";
    @BindView(R.id.shows_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.shows_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.shows_loading)
    TextView mLoadingTextView;

    private TVShowsContract.Presenter mPresenter;
    private ShowsAdapter mAdapter;
    private String mChannelCode;
    private int mFragmentPos;

    public static TVShowsFragment newInstance(String code, int inc) {
        TVShowsFragment tvShowsFragment = new TVShowsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CHANNEL_CODE_KEY, code);
        bundle.putInt(ARGUMENT_INC_KEY, inc);
        tvShowsFragment.setArguments(bundle);
        return tvShowsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChannelCode = getArguments().getString(ARGUMENT_CHANNEL_CODE_KEY);
        mFragmentPos = getArguments().getInt(ARGUMENT_INC_KEY);

        mPresenter = new TVShowsPresenter(
                mChannelCode,
                mFragmentPos,
                Injection.provideTVShowsRepository(),
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            mPresenter.onUserVisible();
        } else {
            mPresenter.onUserInvisible();
        }
    }

    @Override
    public void showTips(String message) {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoadingUI() {
        if (mAdapter.getItemCount() <= 0) {
            mSwipeRefreshLayout.setEnabled(false);
            mLoadingTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stopLoadingUI() {
        mSwipeRefreshLayout.setEnabled(true);
        mLoadingTextView.setVisibility(View.GONE);
    }

    @Override
    public void showTVShows(List<TVShows.TVShow> shows) {
        mAdapter.updateData(shows);
    }

    @Override
    public void stopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void openUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            showTips("No Live Available");
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }

    @Override
    public void addReminder(TVShows.TVShow tvShow) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(TimeUtil.string2Date(tvShow.getTime(), TimeUtil.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE));
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, tvShow.getCName() + "\n" + tvShow.getPName())
                .putExtra(CalendarContract.Events.DESCRIPTION, "url: " + tvShow.getPUrl());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshTVShows();
    }

    class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> implements View.OnClickListener {
        private final List<TVShows.TVShow> mShows = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shows_item, parent, false);
            itemView.setOnClickListener(this);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mNameTextView.setText(mShows.get(position).getPName());
            holder.mTimeTextView.setText(mShows.get(position).getTime());
            holder.itemView.setTag(mShows.get(position));
        }

        @Override
        public int getItemCount() {
            return mShows.size();
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            mPresenter.setupClickAlertBuilder(getContext(), builder, (TVShows.TVShow) v.getTag());
            builder.show();
        }

        void updateData(List<TVShows.TVShow> shows) {
            mShows.clear();
            mShows.addAll(shows);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.shows_name)
            TextView mNameTextView;
            @BindView(R.id.shows_time)
            TextView mTimeTextView;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
