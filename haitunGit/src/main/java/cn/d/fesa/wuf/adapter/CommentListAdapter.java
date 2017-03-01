package cn.d.fesa.wuf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.bean.CommenData;
import cn.d.fesa.wuf.ui.CircleTransform;
import cn.d.fesa.wuf.util.Logger;


/**
 * Created by schwager on 2016/7/25.
 */
public class CommentListAdapter extends CommonAdapter<CommenData.CommentBean> {

    private List<CommenData.CommentBean> mList;

    public CommentListAdapter(Context mContext, List<CommenData.CommentBean> datas) {
        super(mContext, datas);
        this.mList = datas;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.commment_list_item, null, false);
            holder.itemImage = (ImageView) view.findViewById(R.id.comment_list_iv);
            holder.itemName = (TextView) view.findViewById(R.id.comment_list_tv_name);
            holder.itemComment = (TextView) view.findViewById(R.id.comment_list_tv_comment);
            holder.itemZan = (TextView) view.findViewById(R.id.comment_list_tv_zan);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (mList != null && mList.get(i) != null) {
            try {
                if (holder.itemImage != null) {
                    Picasso.with(mContext).load(mList.get(i).getAvatar()).resize(100, 100).error(R.drawable.ic_default_circle).transform(new CircleTransform()).into(holder.itemImage);
                }
                if (holder.itemName != null) {
                    holder.itemName.setText(mList.get(i).getNickname());
                }
                if (holder.itemComment != null) {
                    holder.itemComment.setText(mList.get(i).getContent());
                }
                if (holder.itemZan != null) {
                    holder.itemZan.setText(mList.get(i).getZan());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getInstance().e("qw", "CommentListAdapter.getView.有异常");
            }
        }
        return view;
    }

    class ViewHolder {
        TextView itemName;
        ImageView itemImage;
        TextView itemComment;
        TextView itemZan;
    }
}
