package cn.ben.tvdemo.tvchannels;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ben.tvdemo.BaseFragment;
import cn.ben.tvdemo.R;

public class TVChannelsFragment extends BaseFragment implements TVChannelsContract.View {
    public static final String ARGUMENT_TV_TYPE_ID = "TV_TYPE_ID";

    @BindView(R.id.channels_recycler_view)
    RecyclerView mRecyclerView;

    private TVChannelsAdapter mAdapter;
    private TVChannelsContract.Presenter mPresenter;

    public static TVChannelsFragment newInstance(String id) {
        TVChannelsFragment tvChannelsFragment = new TVChannelsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TV_TYPE_ID, id);
        tvChannelsFragment.setArguments(bundle);
        return tvChannelsFragment;
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TVChannelsPresenter(getArguments().getString(ARGUMENT_TV_TYPE_ID, ""), this);
        mAdapter = new TVChannelsAdapter();
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            mPresenter.onUserInvisible();
        } else {
            mPresenter.onUserInvisible();
        }
    }

    private class TVChannelsAdapter extends RecyclerView.Adapter<TVChannelsAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_channels_list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
