package cn.d.exds.ase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.d.exds.ase.R;
import cn.d.exds.ase.base.BaseApplication;
import cn.d.exds.ase.bean.PrivateVideoData;
import cn.d.exds.ase.bean.Users;
import cn.d.exds.ase.bean.Videos;
import cn.d.exds.ase.ui.CircleTransform;
import cn.d.exds.ase.util.Logger;
import cn.d.exds.ase.util.SharedPreferencesUtils;
import cn.d.exds.ase.util.UIUtils;
import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * Created by BGFVG on 2017/1/23.
 */

public class PrivateVideoActivityAdapter extends CommonAdapter<PrivateVideoData.ConBean> {

    private boolean mHavapay;

    public PrivateVideoActivityAdapter(Context mContext, List<PrivateVideoData.ConBean> datas, int pagePos, boolean havePay, MyPageAdapterClickListener myPageAdapterClickListener) {
        super(mContext, datas);
        this.mMyPageAdapterClickListener = myPageAdapterClickListener;
        this.mPagePos = pagePos;
        this.mHavapay = havePay;
    }

    private GridView mListView;
    private int mPagePos;

    public void setGrdiView(GridView grdiView) {
        this.mListView = grdiView;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = UIUtils.inflate(R.layout.privatevideo_gridview_item);
            holder.tv_Name = (TextView) view.findViewById(R.id.video_item_tv_name);
            holder.tv_Title = (TextView) view.findViewById(R.id.video_item_tv_title);
            holder.tv_Time = (TextView) view.findViewById(R.id.video_item_tv_time);
            holder.iv_Head = (ImageView) view.findViewById(R.id.video_item_iv_head);
            holder.iv_Desc = (ImageView) view.findViewById(R.id.video_item_iv_desc);
            holder.video_iv_playicon = (ImageView) view.findViewById(R.id.video_iv_playicon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (datas != null && datas.get(i) != null) {
            try {
                if (holder.iv_Head != null) {
                    Picasso.with(mContext).load(datas.get(i).getHeadImg()).resize(60, 60).placeholder(R.drawable.head_default).transform(new CircleTransform()).into(holder.iv_Head);
                }
                if (holder.tv_Title != null) {
                    holder.tv_Title.setText(datas.get(i).getShareTItle());
                }
                if (holder.tv_Name != null) {
                    holder.tv_Name.setText(datas.get(i).getNickName());
                }
                if (holder.tv_Time != null) {
                    holder.tv_Time.setText(datas.get(i).getRiqi());
                }
                if (holder.iv_Desc != null) {
                    if (mHavapay) {
                        Picasso.with(mContext).load(datas.get(i).getImgAddress()).into(holder.iv_Desc);
                    } else {
                        //judgeIfPayedBySP(i, holder);
                        judgeIfPayedByLitePal(i, holder);
                    }
                    holder.iv_Desc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mMyPageAdapterClickListener != null) {
                                mMyPageAdapterClickListener.click(datas.get(i).getUserId(), datas.get(i).getVideoId(), datas.get(i)
                                        .getVideoNum(), datas.get(i).getNickName(), datas.get(i).getVideoPlayAddress(), i, datas.get(i).getImgAddress(), mPagePos);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void judgeIfPayedByLitePal(final int i, final ViewHolder holder) {
        new Thread() {
            @Override
            public void run() {
                int countUser = DataSupport.where("userId=?", datas.get(i).getUserId()).count(Users.class);
                int countVideo = DataSupport.where("videoId=?", datas.get(i).getVideoId()).count(Videos.class);
                Handler handler = BaseApplication.getHandler();
                if (handler != null) {
                    if (countUser > 0 | countVideo > 0) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(mContext).load(datas.get(i).getImgAddress()).placeholder(R.drawable.desc_default)
                                        .error(R.drawable.ic_default).into(holder.iv_Desc);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(mContext).load(datas.get(i).getImgAddress()).placeholder(R.drawable.desc_default)
                                        .error(R.drawable.ic_default_blur).resize(200, 200)
                                        .transform(new BlurTransformation(mContext, 20, 1)).into(holder.iv_Desc);
                            }
                        });
                    }
                }
            }
        }.start();
    }

    private void judgeIfPayedBySP(int i, ViewHolder holder) {
        if (SharedPreferencesUtils.getBoolean(UIUtils.getContext(), "u" + datas.get(i).getUserId(), false) || SharedPreferencesUtils.getBoolean(UIUtils.getContext(), "v" + datas.get(i).getVideoId(), false)) {
            Picasso.with(mContext).load(datas.get(i).getImgAddress()).into(holder.iv_Desc);
        } else {
            Picasso.with(mContext).load(datas.get(i).getImgAddress()).placeholder(R.drawable.desc_default).resize(200, 200)
                    .transform(new BlurTransformation(mContext, 20, 1)).into(holder.iv_Desc);
        }
    }

    private MyPageAdapterClickListener mMyPageAdapterClickListener;

    public interface MyPageAdapterClickListener {
        void click(String userId, String videoId, int videoNum, String name, String url, int pos, String desc, int mPagePos);
    }

    class ViewHolder {
        TextView tv_Name;
        TextView tv_Title;
        TextView tv_Time;
        ImageView iv_Head;
        ImageView iv_Desc;
        ImageView video_iv_playicon;
    }

    @SuppressLint("HandlerLeak")
    private Handler han = new Handler() {
        public void handleMessage(Message msg) {
            updateItem(msg.arg1);
        }
    };

    /**
     * update listview 单条数据
     */
    private boolean mFlag;
    private String mUserId;

    public void updateItemDataNew(int pos, String desc, String userId, boolean flag) {
        this.mFlag = flag;
        this.mUserId = userId;
        Logger.getInstance().e("qw", "PrivateVideoActivityAdapter.updateItemDataNew.");
        Message msg = Message.obtain();
        int ids = -1;
        msg.arg1 = pos;
        // 更新mDataList对应位置的数据
        // handle刷新界面
        han.sendMessage(msg);
    }

    /**
     * 刷新指定item
     *
     * @param index item在listview中的位置
     */
    private void updateItem(int index) {
        if (mListView == null) {
            return;
        }
        // 获取当前可以看到的item位置
        int visiblePosition = mListView.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        // View view = listview.getChildAt(index - visiblePosition + 1);
        // 获取点击的view
        if (!mFlag) {
            flushItem(index, visiblePosition);
        } else {
            for (int i = 0; i < datas.size(); i++) {
                if (i >= visiblePosition) {
                    if (TextUtils.equals(datas.get(i).getUserId(), mUserId)) {
                        flushItem(i, visiblePosition);
                    }
                }
            }
        }
    }

    private void flushItem(int index, int visiblePosition) {
        View view = mListView.getChildAt(index - visiblePosition);
        if (view != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.video_item_iv_desc);
            // 获取mDataList.set(ids, item);更新的数据
            PrivateVideoData.ConBean data = (PrivateVideoData.ConBean) getItem(index);
            // 重新设置界面显示数据
            Picasso.with(mContext).load(data.getImgAddress()).into(imageView);
        }
    }
}
