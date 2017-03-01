package cn.d.fesa.wuf.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.bean.FragmentBData;
import cn.d.fesa.wuf.bean.PrivateVideoData;
import cn.d.fesa.wuf.ui.CircleTransform;
import cn.d.fesa.wuf.util.Logger;
import cn.d.fesa.wuf.util.SharedPreferencesUtils;
import cn.d.fesa.wuf.util.UIUtils;
import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * Created by BGFVG on 2017/1/23.
 */

public class FragmentBPageAdapter extends CommonAdapter<FragmentBData.ConBean> {


    public FragmentBPageAdapter(Context mContext, List<FragmentBData.ConBean> datas) {
        super(mContext, datas);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = UIUtils.inflate(R.layout.fragment_b_gridview_item);
            holder.tv_Name = (TextView) view.findViewById(R.id.video_item_tv_name);
            holder.iv_Head = (ImageView) view.findViewById(R.id.video_item_iv_head);
            holder.fragment_b_rel = (RelativeLayout) view.findViewById(R.id.fragment_b_rel);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (datas != null && datas.get(i) != null) {
            try {
                if (holder.iv_Head != null) {
                    Picasso.with(mContext).load(datas.get(i).getHeadImg()).resize(60, 60).placeholder(R.drawable.head_default).transform(new CircleTransform()).into(holder.iv_Head);
                    holder.iv_Head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoVideoUserActivity(datas.get(i).getUserId(), datas.get(i).getNickName());
                        }
                    });
                }
                if (holder.tv_Name != null) {
                    holder.tv_Name.setText(datas.get(i).getNickName());
                }
                if (holder.fragment_b_rel != null) {
                    holder.fragment_b_rel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoVideoUserActivity(datas.get(i).getUserId(), datas.get(i).getNickName());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void gotoVideoUserActivity(String userId, String userName) {
        UIUtils.gotoVideoUserActivity(userId, userName, true);
    }

    class ViewHolder {
        TextView tv_Name;
        ImageView iv_Head;
        RelativeLayout fragment_b_rel;
    }
}
