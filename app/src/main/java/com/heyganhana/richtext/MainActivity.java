package com.heyganhana.richtext;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnKeyListener{
    Button addImage;
    private Button importText;
    private int GALLERY_REQUEST_CODE =0;
    RichText mRichText;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRichText = findViewById(R.id.rich_text);
        addImage = findViewById(R.id.add_image);
        addImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST_CODE);*/
                mRichText.addImage(Uri.parse("content://media/external/images/media/34"));

            }
        });

        importText = findViewById(R.id.import_text);
        importText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("zhangdi","mRichText.string:"+mRichText.getText());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && requestCode == 0){
            Log.d("zhangdi","data.uri = "+data.getData());
            mRichText.addImage(data.getData());
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){
        Log.d("zhangdi","main keyCode = "+keyCode);
        return false;
    }

}
