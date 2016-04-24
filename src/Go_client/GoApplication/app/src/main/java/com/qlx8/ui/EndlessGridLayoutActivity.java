package com.qlx8.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.ExStaggeredGridLayoutManager;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.HeaderSpanSizeLookup;
import com.cundong.recyclerview.RecyclerViewUtils;
import com.qlx8.R;
import com.qlx8.utils.NetworkUtils;
import com.qlx8.utils.RecyclerViewStateUtils;
import com.qlx8.weight.LoadingFooter;
import com.qlx8.weight.SampleHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 带HeaderView的分页加载GridLayout RecyclerView
 */
public class EndlessGridLayoutActivity extends AppCompatActivity {


    SwipeRefreshLayout id_swipe_ly;

    /**
     * 服务器端一共多少条数据
     */
    private static final int TOTAL_COUNTER = 64;

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    /**
     * 已经获取到多少条数据了
     */
    private int mCurrentCounter = 0;

    private RecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;


    ArrayList<String> dataList;

    private boolean isPull = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        id_swipe_ly = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);


        id_swipe_ly.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.color_00);


        //init data
        dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add("item" + i);
        }

        id_swipe_ly.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPull = true;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> tmpList = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                            tmpList.add("tmpList" + i);
                        }
                        refresh(tmpList);
                        id_swipe_ly.setRefreshing(false);
                        isPull = false;
                    }
                }, 2000);
            }
        });


        mCurrentCounter = dataList.size();

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.addAll(dataList);

        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        //setLayoutManager
        ExStaggeredGridLayoutManager manager = new ExStaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));


//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        GridLayoutManager manager = new GridLayoutManager(this, 2);
//        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
        mRecyclerView.setLayoutManager(manager);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<String> list) {
        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();
    }

    private void refresh(ArrayList<String> list) {
        mDataAdapter.rmonve();
        mDataAdapter.addAll(list);
        mCurrentCounter = list.size();

    }


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            if (isPull) {
                return;
            }

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if (state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            if (mCurrentCounter < TOTAL_COUNTER) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(EndlessGridLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(EndlessGridLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private static class PreviewHandler extends Handler {

        private WeakReference<EndlessGridLayoutActivity> ref;

        PreviewHandler(EndlessGridLayoutActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final EndlessGridLayoutActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    int currentSize = activity.mDataAdapter.getItemCount();
                    //模拟组装10个数据
                    ArrayList<String> newList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        newList.add("item" + (currentSize + i));
                    }

                    activity.addItems(newList);
                    RecyclerViewStateUtils.setFooterViewState(activity.mRecyclerView, LoadingFooter.State.Normal);
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    RecyclerViewStateUtils.setFooterViewState(activity, activity.mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, activity.mFooterClick);
                    break;
            }
        }
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(EndlessGridLayoutActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    /**
     * 模拟请求网络
     */
    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if (NetworkUtils.isNetAvailable(EndlessGridLayoutActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    private class DataAdapter extends RecyclerView.Adapter {

        private LayoutInflater mLayoutInflater;
        private ArrayList<String> mDataList = new ArrayList<>();
        private int largeCardHeight, smallCardHeight;
        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            largeCardHeight = (int)context.getResources().getDisplayMetrics().density * 300;
            smallCardHeight = (int)context.getResources().getDisplayMetrics().density * 200;
        }

        private void addAll(ArrayList<String> list) {
            int lastIndex = this.mDataList.size();
            if (this.mDataList.addAll(list)) {
                notifyItemRangeInserted(lastIndex, list.size());
            }
        }

        private void rmonve() {
            mDataList.clear();
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.sample_item_card, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            String item = mDataList.get(position);

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(item);
            viewHolder.textView.setTag(position+"");
            //修改高度，模拟交错效果
            viewHolder.cardView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;

        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;
            private CardView cardView;
            public ViewHolder(final View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.info_text);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = Integer.valueOf (textView.getTag().toString());
                        String text = mDataList.get(RecyclerViewUtils.getAdapterPosition(mRecyclerView, ViewHolder.this));
//                        dataList.remove(pos);
//                        notifyItemRemoved(pos);
////                        notifyItemInserted(pos);
//                        notifyItemChanged(pos);
//                        Toast.makeText(EndlessGridLayoutActivity.this, text, Toast.LENGTH_SHORT).show();
                        showBasic();
                    }
                });
            }
        }
    }


    public void showBasic() {
        new MaterialDialog.Builder(this)
                .title("确认提示")
                .content("你确定要这样做啊,这样可不好啊!!!")
                .positiveText("取消")
                .negativeText("确定")
                .cancelable(false)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .show();
    }
}