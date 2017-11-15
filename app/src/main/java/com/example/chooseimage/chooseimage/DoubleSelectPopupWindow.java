package com.example.chooseimage.chooseimage;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import java.util.Arrays;

/**
 * Created by libo on 2017/4/27.
 */
public class DoubleSelectPopupWindow extends PopupWindow{
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private TextView tvCancel;
    private TextView tvTitle;
    private ICallIntListener intListener;

    public DoubleSelectPopupWindow(Context context){
        this.context = context;
        view = ((Activity)context).getLayoutInflater().from(context).inflate(R.layout.doubleselect_popupwindow,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.PopupAnimation);
        setContentView(view);
    }

    public void init(String title,String[] menu) {

        tvCancel = (TextView) view.findViewById(R.id.tv_popup_cancel);
        tvTitle = (TextView) view.findViewById(R.id.tv_popup_title);
        tvTitle.setText(title);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_popup);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CommonAdapter commonAdapter = new CommonAdapter<String>(context,R.layout.item_address_textview, Arrays.asList(menu)) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_popup_place,s);
            }
        };
        recyclerView.setAdapter(commonAdapter);
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intListener.callBack(position);
                dismiss();
            }
            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnSelectListener(ICallIntListener intListener){
        this.intListener = intListener;
    }

    public interface ICallIntListener {
        void callBack(int index);
    }

}
