package com.heyganhana.richtext;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.service.autofill.CustomDescription;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import static android.text.Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL;

/**
 * @author zhangdi
 * @date 19-5-28 下午4:48
 * @description
 */
public class MainContainer extends LinearLayout implements View.OnFocusChangeListener, CustomEditText.DeleteCallback{


    private CustomEditText currentEditText;
    //private LinearLayout mContainer;
    private LinearLayout.LayoutParams linearParams_PARENT;

    private int defaultParentTopMargin = 5;
    private int defaultParentBottomMargin = 5;
    private int defaultParentLeftMargin = 5;
    private int defaultParentRightMargin = 5;

    public MainContainer(Context context){
        this(context,null);
    }

    public MainContainer(Context context, AttributeSet attrs){
        this(context, attrs,0);
    }

    public MainContainer(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        initUI(context);
    }

    private void initUI(Context context){
        linearParams_PARENT = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearParams_PARENT.gravity = Gravity.CENTER_HORIZONTAL;
        linearParams_PARENT.topMargin = defaultParentTopMargin;
        linearParams_PARENT.bottomMargin = defaultParentBottomMargin;
        linearParams_PARENT.leftMargin = defaultParentLeftMargin;
        linearParams_PARENT.rightMargin = defaultParentRightMargin;
        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(linearParams_PARENT);

        currentEditText = createEditText(context);
        this.addView(currentEditText);
        //this.addView(mContainer);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        super.onLayout(changed, l, t, r, b);
    }

    private CustomEditText createEditText(Context context){
        CustomEditText newEditText = new CustomEditText(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        newEditText.setLayoutParams(params);
        newEditText.setBackground(null);
        newEditText.requestFocus();
        newEditText.setOnFocusChangeListener(this);
        newEditText.setSingleLine(false);
        newEditText.setLineSpacing(0,1.5f);
        //newEditText.addTextChangedListener(editTextWatcher);
        newEditText.setDeleteCallback(this);
        return newEditText;
    }


    private ImageView createImage(Context context){
        ImageView newImageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        newImageView.setLayoutParams(params);

        return newImageView;
    }

    public void addImage(Uri uri){

        String subString = preProcessAddImage();
        ImageView imageView = generateLoadingImage(uri);

        this.addView(imageView,linearParams_PARENT);
        Log.d("zhangdi","add imageView");
        CustomEditText editText = createEditText(getContext());
        currentEditText = editText;
        editText.setText(subString);
        editText.setSelection(subString.length());
        this.addView(editText);

        Log.d("zhangdi","add editText:"+editText);
    }
    public void addImage(File imageFile){
        addImage(Uri.fromFile(imageFile));

    }

    public void addImage(String path){
        addImage(Uri.fromFile(new File(path)));
    }

    private String preProcessAddImage(){
        int editIndex = currentEditText.getSelectionStart();
        String currentText = currentEditText.getText().toString();
        String subString = "";
        Log.d("zhangdi","editIndex:"+editIndex);
        if(currentText != null && currentText.length() == 0){//如果当前edittext没有内容
            this.removeView(currentEditText);
        }else if(editIndex > 0){//光标在中间或者结尾
            if(editIndex > 0 && editIndex < currentText.length() - 1){
                subString = currentText.substring(editIndex);
                currentEditText.getText().delete(editIndex,currentText.length());
            }else{//在结尾
                //do nothing  不做处理，此时添加image,直接添加
            }
        }else{//editText index  == 0 光标在刚开始的位置,并且光标后面有内容
            subString = currentText;
            this.removeView(currentEditText);
        }
        return subString;
    }

    private ImageView generateLoadingImage(Uri uri){
        final ImageView imageView = createImage(getContext());
        final ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"Rotation",0f,359f);
        animator.setDuration(1200);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_static_loading)
                .error(R.drawable.ic_loading_error)
                .skipMemoryCache(true)
                .fitCenter();
        imageView.setTag(R.id.loading_image_key,null);
        Glide.with(getContext())
                .load(uri)
                .apply(options)
                .listener(new RequestListener<Drawable>(){
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource){
                        Log.e("zhangdi","image load failed exception:"+e);
                        animator.cancel();
                        imageView.setRotation(0f);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource){
                        Log.e("zhangdi","image load onResourceReady");
                        animator.cancel();
                        return false;
                    }
                })
                .into(imageView);
        imageView.setTag(R.id.loading_image_key,uri);
        return imageView;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus){
        CustomEditText editText = (CustomEditText) v;
        if(hasFocus)currentEditText = editText;
    }


    public void deleteImage(){
        int currentEditIndex = getCurrentEditIndex();
        if(currentEditIndex > 0){
            View preView = getChildAt(currentEditIndex -1);
            if(preView instanceof ImageView){
                ImageView image = (ImageView) preView;
                Uri uri = (Uri) image.getTag(R.id.loading_image_key);
                this.removeView(image);
            }
        }
        combineEditText();
    }

    private void combineEditText(){
        int currentEditIndex = getCurrentEditIndex();
        if(currentEditIndex > 0 ){
            View preEdit = getChildAt(currentEditIndex-1);
            if(preEdit instanceof CustomEditText){

                CustomEditText preE = (CustomEditText) preEdit;
                String currentText = currentEditText.getText().toString();
                this.removeView(currentEditText);
                preE.append(currentText);
                currentEditText = preE;
            }
        }
    }

    private int getCurrentEditIndex(){
        return indexOfChild(currentEditText);
    }

    @Override
    public void onDelete(KeyEvent event){
        if(event.getKeyCode() == KeyEvent.KEYCODE_DEL && currentEditText.getText().toString().length() == 0){
            deleteImage();
        }
    }


    public String getText(){
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int count = getChildCount();
        for(int i = 0;i < count;i++){
            View child = getChildAt(i);
            if(child instanceof CustomEditText){
                CustomEditText childEdit = (CustomEditText) child;
                ssb.append(childEdit.getText().toString());
            }else if(child instanceof ImageView){
                ImageView childImage = (ImageView) child;
                Uri imageUri = (Uri) childImage.getTag(R.id.loading_image_key);
                ssb.append("<img src=\"");
                ssb.append(imageUri.toString());
                ssb.append("\"/>");
            }
        }
        //Log.i("zhangdi","html:"+Html.toHtml(ssb));
        return ssb.toString();
    }
}
