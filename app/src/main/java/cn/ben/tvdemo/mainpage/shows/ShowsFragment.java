package cn.ben.tvdemo.mainpage.shows;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ben.tvdemo.BaseFragment;
import cn.ben.tvdemo.R;
import cn.ben.tvdemo.data.Injection;
import cn.ben.tvdemo.data.tvtype.TVTypes;
import cn.ben.tvdemo.tvchannels.TVChannelsActivity;
import cn.ben.tvdemo.tvchannels.TVChannelsFragment;

@SuppressWarnings("WeakerAccess")
public class ShowsFragment extends BaseFragment implements ShowsContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.tv_type_grid)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_type_loading)
    TextView mLoadingView;

    private ShowsContract.Presenter mPresenter;
    private TVTypeAdapter mAdapter;

    public static ShowsFragment newInstance() {
        return new ShowsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ShowsPresenter(
                Injection.provideTVTypesRepository(),
                this,
                Injection.provideSchedulerProvider());
        mAdapter = new TVTypeAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shows_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showTVTypes(List<TVTypes.TVType> tvTypes) {
        // make sure order here
        Collections.sort(tvTypes);
        mAdapter.updateData(tvTypes);
    }

    @Override
    public void showTips(String reason) {
        Context context = getContext();
        if (context != null)
            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadingUI() {
        mSwipeRefreshLayout.setEnabled(false);
        if (mAdapter.getItemCount() <= 0)
            mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoadingUI() {
        mSwipeRefreshLayout.setEnabled(true);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshTVTypes();
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            mPresenter.onUserVisible();
        } else {
            mPresenter.onUserInvisible();
        }
    }

    class TVTypeAdapter extends RecyclerView.Adapter<TVTypeAdapter.ViewHolder> implements View.OnClickListener {
        private final List<TVTypes.TVType> mTVTypes;

        TVTypeAdapter() {
            mTVTypes = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_type_grid_item, parent, false);
            itemView.setOnClickListener(this);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mTVTypes.get(position).getName());
            holder.itemView.setTag(mTVTypes.get(position).getId());
        }

        @Override
        public int getItemCount() {
            return mTVTypes.size();
        }

        void updateData(List<TVTypes.TVType> tvTypes) {
            mTVTypes.clear();
            mTVTypes.addAll(tvTypes);
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {
            String id = (String) v.getTag();
            showTVChannels(id);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_type_grid_item_title)
            TextView mTextView;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void showTVChannels(String id) {
        Intent intent = new Intent(getContext(), TVChannelsActivity.class);
        intent.putExtra(TVChannelsFragment.ARGUMENT_TV_TYPE_ID, id);
        startActivity(intent);
    }
}
