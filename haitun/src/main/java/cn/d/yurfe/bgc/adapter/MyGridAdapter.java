package cn.d.yurfe.bgc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.d.yurfe.bgc.util.Logger;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.d.yurfe.bgc.R;
import cn.d.yurfe.bgc.bean.MovieData;
import cn.d.yurfe.bgc.util.UIUtils;

/**
 * 需要自己写ViewHolder
 * Created by schwager on 2016/6/22.
 */
public class MyGridAdapter extends CommonAdapter<MovieData.ConEntity> {

    private String mType;

    public MyGridAdapter(Context context, List<MovieData.ConEntity> datas, String type) {
        super(context, datas);
        this.mType = type;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null, false);
            holder.itemImage = (ImageView) view.findViewById(R.id.gv_item_image);
            holder.itemName = (TextView) view.findViewById(R.id.gv_item_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (datas != null && datas.get(i) != null) {
            try {
                if (holder.itemImage != null) {
                    Picasso.with(mContext).load(datas.get(i).getPic()).into(holder.itemImage);
                    holder.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doMyWork(i, datas.get(i).getDianying_id());
                        }
                    });
                }
                if (holder.itemName != null) {
                    holder.itemName.setText(datas.get(i).getTitle());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void doMyWork(int i, String id) {
        UIUtils.gotoComentActivity(datas.get(i).getVideo(), datas.get(i).getTitle(), mType, id);
    }


    class ViewHolder {
        TextView itemName;
        ImageView itemImage;
    }
}
