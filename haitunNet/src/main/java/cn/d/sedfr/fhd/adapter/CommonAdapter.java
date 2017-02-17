package cn.d.sedfr.fhd.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

import cn.d.sedfr.fhd.bean.PrivateVideoData;

/**
 * Created by Administrator on 2016/1/18.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    public List<T> datas;
    public Context mContext;

    public CommonAdapter(Context mContext, List<T> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    public void addList(List<T> list) {
        if (datas != null) {
            datas.addAll(list);
        }
    }

    public void clearAll() {
        if (datas != null) {
            datas.clear();
        }
    }

    @Override
    public int getCount() {
        return datas.size() > 0 ? datas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
