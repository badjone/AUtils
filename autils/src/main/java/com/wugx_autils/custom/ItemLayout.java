package com.wugx_autils.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.wugx_autils.R;


/**
 * 带左侧图标，右边文字，点击效果的item
 *
 *
 * @author wugx
 * @data 2017/12/5.
 */

public class ItemLayout extends LinearLayout {
    private ImageView imgLayoutItemIcon;
    private TextView tvLayoutItemTitle;
    private TextView tvLayoutItemRight;

    private View v;
    private TypedArray a;
    private ImageView mImgLayoutItemMore;

    public ItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        a = context.obtainStyledAttributes(attrs,
                R.styleable.ItemLayout);
        init();
    }

    private String title, rightTxt;
    private int icon;
    private boolean isShowMoreIcon;

    private void init() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        v = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_select, null);
        initView();
        initAttr();
        addView(v, layoutParams);
        a.recycle();
    }

    private void initAttr() {
        title = a.getString(R.styleable.ItemLayout_leftTitle);
        rightTxt = a.getString(R.styleable.ItemLayout_rightTxt);
        icon = a.getResourceId(R.styleable.ItemLayout_icon, 0);
        isShowMoreIcon = a.getBoolean(R.styleable.ItemLayout_isShowMoreIcon, false);

        if (icon != 0) {
            imgLayoutItemIcon.setVisibility(VISIBLE);
            imgLayoutItemIcon.setImageResource(icon);
        } else {
            imgLayoutItemIcon.setVisibility(GONE);
        }
        tvLayoutItemTitle.setText(!StringUtils.isEmpty(title) ? title : getContext().getResources().getString(R.string.app_name));
        if (!StringUtils.isEmpty(rightTxt)) {
            tvLayoutItemRight.setText(rightTxt);
        }
        mImgLayoutItemMore.setVisibility(isShowMoreIcon ? VISIBLE : GONE);
    }

    private void initView() {
        imgLayoutItemIcon = v.findViewById(R.id.img_layout_item_icon);
        tvLayoutItemTitle = v.findViewById(R.id.tv_layout_item_title);
        tvLayoutItemRight = v.findViewById(R.id.tv_layout_item_right);
        mImgLayoutItemMore = v.findViewById(R.id.img_layout_item_more);
    }

    public void setTitle(String title) {
        this.title = title;
        tvLayoutItemTitle.setText(!StringUtils.isEmpty(title) ? title : getContext().getResources().getString(R.string.app_name));
    }

    public void setRightTxt(String rightTxt) {
        this.rightTxt = rightTxt;
        if (!StringUtils.isEmpty(rightTxt)) {
            tvLayoutItemRight.setText(rightTxt);
        }
    }

    public String getRightTxt() {
        return rightTxt;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        if (icon != 0) {
            imgLayoutItemIcon.setVisibility(VISIBLE);
            imgLayoutItemIcon.setImageResource(icon);
        } else {
            imgLayoutItemIcon.setVisibility(GONE);
        }
    }
}
