package cn.d.yurfe.bgc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.bean.CommenData;
import cn.d.yurfe.bgc.util.Logger;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by schwager on 2016/7/25.
 */
public class CommentTopAdapter extends CommonAdapter<CommenData.Top_videoEntity> {

    private List<CommenData.Top_videoEntity> mList;

    private OnItemVideoClickListener mOnItemVideoClickListener;

    public void setOnitemVideoClick(OnItemVideoClickListener onitemVideoClick) {
        this.mOnItemVideoClickListener = onitemVideoClick;
    }

    public CommentTopAdapter(Context mContext, List<CommenData.Top_videoEntity> datas) {
        super(mContext, datas);
        this.mList = datas;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.commment_top_item, null, false);
            holder.itemImage = (ImageView) view.findViewById(R.id.comment_iv_top_item_image);
            holder.itemName = (TextView) view.findViewById(R.id.comment_tv_top_item_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (mList != null && mList.get(i) != null) {
            try{
                if (holder.itemImage != null) {
                    Picasso.with(mContext).load(mList.get(i).getPic()).into(holder.itemImage);
                    holder.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mOnItemVideoClickListener != null) {
                                mOnItemVideoClickListener.onItemClick((mList.get(i).getDianying_id()));
                            }
                        }
                    });
                }
                if (holder.itemName != null) {
                    holder.itemName.setText(mList.get(i).getTitle());
                }
            }catch(Exception e){
                e.printStackTrace();
                Logger.getInstance().e("qw", "CommentTopAdapter.getView.有异常");
            }
        }
        return view;
    }

    class ViewHolder {

        TextView itemName;
        ImageView itemImage;
    }

    public interface OnItemVideoClickListener {
        void onItemClick(String msg);
    }
}
