package cn.ben.tvdemo.shows;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ben.tvdemo.R;
import cn.ben.tvdemo.data.tvtype.TVTypes;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShowsFragment extends Fragment implements ShowsContract.View {
    private ShowsContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private TVTypeAdapter mAdapter;

    public static ShowsFragment newInstance() {
        return new ShowsFragment();
    }

    @Override
    public void setPresenter(@NonNull ShowsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shows_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.tv_type_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new TVTypeAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void changeLoadingUI(boolean shown) {
        // TODO: 2017/2/23
    }

    @Override
    public void showTVTypes(List<TVTypes.TVType> tvTypes) {
        mAdapter.updateData(tvTypes);
    }

    @Override
    public void showErrorUI(String reason) {
        // TODO: 2017/2/23
        Context context = getContext();
        if (context != null)
            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
    }

    private class TVTypeAdapter extends RecyclerView.Adapter<TVTypeAdapter.ViewHolder> {
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
            final TextView mTextView;

            ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.tv_type_grid_item_title);
            }
        }
    }
}
