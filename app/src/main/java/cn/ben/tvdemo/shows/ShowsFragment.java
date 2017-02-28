package cn.ben.tvdemo.shows;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ben.tvdemo.R;
import cn.ben.tvdemo.data.tvtype.TVTypes;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShowsFragment extends Fragment implements ShowsContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.tv_type_grid)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ShowsContract.Presenter mPresenter;
    private TVTypeAdapter mAdapter;
    private boolean mStarted = false;

    public static ShowsFragment newInstance() {
        return new ShowsFragment();
    }

    @Override
    public void setPresenter(@NonNull ShowsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mPresenter != null) {
            if (getUserVisibleHint()) {
                mStarted = true;
                mPresenter.onVisible();
            } else {
                if (mStarted) {
                    // actually leave this page
                    mStarted = false;
                    mPresenter.onInvisible();
                }
            }
        }
    }

    @Override
    public void showTVTypes(List<TVTypes.TVType> tvTypes) {
        mAdapter.updateData(tvTypes);
    }

    @Override
    public void showTips(String reason) {
        // TODO: 2017/2/23
        Context context = getContext();
        if (context != null)
            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshTVTypes();
    }

    class TVTypeAdapter extends RecyclerView.Adapter<TVTypeAdapter.ViewHolder> {
        private List<TVTypes.TVType> mTVTypes;

        TVTypeAdapter() {
            mTVTypes = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_type_grid_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mTVTypes.get(position).getName());
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

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_type_grid_item_title)
            TextView mTextView;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
