package com.heyganhana.richtext;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ScrollView;

/**
 * @author zhangdi
 * @date 19-5-28 下午4:24
 * @description
 */
public class RichText extends ScrollView{

    private MainContainer mScrollViewContainer;



    private Button addImage;


    public RichText(Context context){
        this(context,null);
    }

    public RichText(Context context, AttributeSet attrs){
        this(context, attrs,0);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        initUI(context);

    }

    private void initUI(Context context){
        mScrollViewContainer = new MainContainer(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,Gravity.TOP);
        this.addView(mScrollViewContainer,params);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
    }

    public void addImage(Uri uri){
        if (uri != null){
            mScrollViewContainer.addImage(uri);
        }
    }

    public String getText(){
        return mScrollViewContainer.getText();
    }
}
