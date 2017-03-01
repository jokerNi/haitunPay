package cn.d.fesa.wuf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.d.fesa.wuf.R;
import cn.d.fesa.wuf.bean.CommenData;
import cn.d.fesa.wuf.util.Logger;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;


/**
 * Created by schwager on 2016/7/25.
 */
public class CommentTopAdapter extends CommonAdapter<CommenData.TopVideoBean> {

    private List<CommenData.TopVideoBean> mList;

    private OnItemVideoClickListener mOnItemVideoClickListener;
    public ViewHolder mHolder;

    public void setOnitemVideoClick(OnItemVideoClickListener onitemVideoClick) {
        this.mOnItemVideoClickListener = onitemVideoClick;
    }

    public CommentTopAdapter(Context mContext, List<CommenData.TopVideoBean> datas) {
        super(mContext, datas);
        this.mList = datas;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            mHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.commment_top_item, null, false);
            mHolder.itemImage = (ImageView) view.findViewById(R.id.comment_iv_top_item_image);
            mHolder.itemName = (TextView) view.findViewById(R.id.comment_tv_top_item_name);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        if (mList != null && mList.get(i) != null) {
            try{
                if (mHolder.itemImage != null) {
                    Picasso.with(mContext).load(mList.get(i).getPic()).transform(transformation).into(mHolder.itemImage);
                    mHolder.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mOnItemVideoClickListener != null) {
                                mOnItemVideoClickListener.onItemClick((mList.get(i).getDianying_id()));
                            }
                        }
                    });
                }
                if (mHolder.itemName != null) {
                    mHolder.itemName.setText(mList.get(i).getTitle());
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
    Transformation transformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            if (mHolder.itemImage==null){
                return source;
            }
            int targetWidth = mHolder.itemImage.getWidth();
            if (source.getWidth() == 0) {
                return source;
            }
            //如果图片小于设置的宽度，则返回原图
            if (source.getWidth() < targetWidth) {
                return source;
            } else {
                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                if (targetHeight != 0 && targetWidth != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            }
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };
}
