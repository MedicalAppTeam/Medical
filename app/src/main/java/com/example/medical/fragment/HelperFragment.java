package com.example.medical.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.medical.bean.HelperBean;
import com.example.medical.inter.DialogListener;
import com.example.medical.R;
import com.example.medical.inter.WritePadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ${liyan} on 2017/5/8.
 */

public class HelperFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HelperFragment";

    private Bitmap mSignBitmap;
    private String signPath;

    private RadioButton ask,chat,chinese,western;


    private LinearLayout chinese_linearLayout,western_linearLayout,chat_linearLayout,ask_linearLayout;
    private LinearLayout chinese_linearLayout_linearLayout,western_linearLayout_linearLayout;
    private ImageView chinese_img,western_img;

    private TextView name,sex,age,content;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_helper,container,false);
        name= (TextView) view.findViewById(R.id.fragment_helper_name);
        sex= (TextView) view.findViewById(R.id.fragment_helper_sex);
        age= (TextView) view.findViewById(R.id.fragment_helper_age);
        content= (TextView) view.findViewById(R.id.fragment_helper_symptom);

        ask= (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_ask);
        chat= (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_chat);
        chinese= (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_chinese);
        western= (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_western);

        western_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_western_medical_linearLayout);
        chinese_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_chinese_linearLayout);
        chat_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_chat_linearLayout);
        ask_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_ask_linearLayout);
        chinese_img= (ImageView) view.findViewById(R.id.fragment_helper_chinese_linearLayout_linearLayout_image);
        western_img= (ImageView) view.findViewById(R.id.fragment_helper_western_medical_linearLayout_linearLayout_image);

        chinese_linearLayout_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_chinese_linearLayout_linearLayout);
        western_linearLayout_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_western_medical_linearLayout_linearLayout);


        chinese_linearLayout_linearLayout.setOnClickListener(signListener);
        chinese_img.setOnClickListener(signListener);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ask.setOnClickListener(this);
        chat.setOnClickListener(this);
        chinese.setOnClickListener(this);
        western.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_helper_radioButton_ask:
                ask_linearLayout.setVisibility(View.VISIBLE);
                chat_linearLayout.setVisibility(View.GONE);
                chinese_linearLayout .setVisibility(View.GONE);
                western_linearLayout.setVisibility(View.GONE);
                break;
            case R.id.fragment_helper_radioButton_chat:
                chat_linearLayout.setVisibility(View.VISIBLE);
                ask_linearLayout.setVisibility(View.GONE);
                chinese_linearLayout.setVisibility(View.GONE);
                western_linearLayout.setVisibility(View.GONE);
                break;
            case R.id.fragment_helper_radioButton_chinese:
                chinese_linearLayout.setVisibility(View.VISIBLE);
                ask_linearLayout.setVisibility(View.GONE);
                chat_linearLayout.setVisibility(View.GONE);
                western_linearLayout.setVisibility(View.GONE);
                break;
            case R.id.fragment_helper_radioButton_western:
                western_linearLayout.setVisibility(View.VISIBLE);
                ask_linearLayout.setVisibility(View.GONE);
                chat_linearLayout.setVisibility(View.GONE);
                western_linearLayout.setVisibility(View.GONE);
                break;
        }
    }

    private View.OnClickListener signListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WritePadDialog writeTabletDialog = new WritePadDialog(
                    getContext(),R.style.SignBoardDialog, new DialogListener() {
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = createFile();

                    //对图片进行压缩
							/*BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 15;
							options.inTempStorage = new byte[5 * 1024];
							Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);
*/
                    Bitmap zoombm = getCompressBitmap(signPath);
                    //ivSign.setImageBitmap(mSignBitmap);
                    chinese_img.setImageBitmap(zoombm);

                }
            });
            writeTabletDialog.show();
        }
    };

    /**
     * 创建手写签名文件
     *
     * @return
     */
    private String createFile() {
        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory()
                    + File.separator;
            _path = sign_dir + System.currentTimeMillis() + ".png";
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }

    /**
     * 根据图片路径获取图片的压缩图
     * @param filePath
     * @return
     */
    public Bitmap getCompressBitmap(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); //此时返回bm为空
        if(bitmap == null){
        }
        //计算缩放比
        int simpleSize = (int)(options.outHeight / (float)200);
        if (simpleSize <= 0)
            simpleSize = 1;
        options.inSampleSize = simpleSize;
        options.inJustDecodeBounds = false;
        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap=BitmapFactory.decodeFile(filePath,options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w+"   "+h);
        return bitmap;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HelperBean helperBean){
        name.setText(helperBean.getName());
        sex.setText(helperBean.getSex());
        age.setText(helperBean.getAge()+"");
        content.setText(helperBean.getContent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



}
