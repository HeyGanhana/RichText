package com.heyganhana.richtext;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * @author zhangdi
 * @date 19-5-29 下午4:23
 * @description
 */
public class CustomEditText extends EditText{

    private DeleteCallback mDeleteCallback;

    public CustomEditText(Context context){
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs){
        return new MMInputConnection(super.onCreateInputConnection(outAttrs),true);
    }

    class MMInputConnection extends InputConnectionWrapper{

        /**
         * Initializes a wrapper.
         *
         * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
         * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
         * has {@code null} in {@code target}.</p>
         *
         * @param target  the {@link InputConnection} to be proxied.
         * @param mutable set {@code true} to protect this object from being reconfigured to target
         *                another {@link InputConnection}.  Note that this is ignored while the target is {@code null}.
         */
        public MMInputConnection(InputConnection target, boolean mutable){
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event){
            Log.e("zhangdi","event.keycode:"+event.getKeyCode());
            if(mDeleteCallback != null)
                mDeleteCallback.onDelete(event);
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength){
            Log.e("zhangdi","deleteSurroundingText beforeLength:"+beforeLength+",afterLength:"+afterLength);
            //在删除时，输入框无内容，或者删除以后输入框无内容
            if (beforeLength == 1 || afterLength == 0 || beforeLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN ,KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public void setDeleteCallback(DeleteCallback deleteCallback){
        this.mDeleteCallback = deleteCallback;
    }


    interface DeleteCallback{
        void onDelete(KeyEvent event);
    }
}
