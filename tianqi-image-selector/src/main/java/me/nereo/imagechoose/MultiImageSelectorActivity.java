package me.nereo.imagechoose;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import me.nereo.imagechoose.utils.FileUtils;
import me.nereo.multi_image_selector.R;
import song.image.crop.Crop;
import song.image.crop.HDApp;

/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback {

    /**
     * 最大图片选择次数，int类型，默认9
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，默认多选
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，默认显示
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    public static final String EXTRA_SHOW_TEXT = "show_text";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * 默认选择集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 裁切
     */
    public static final int MODE_CROP = 2;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;

    private int mode = 1;


    private ArrayList<String> resultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity_default);

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        boolean isShowText = intent.getBooleanExtra(EXTRA_SHOW_TEXT, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShowCamera);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_TEXT, isShowText);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();

        // 返回按钮
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 完成按钮
        mSubmitButton = (Button) findViewById(R.id.commit);
        if (resultList == null || resultList.size() <= 0) {
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        } else {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
            mSubmitButton.setEnabled(true);
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultList != null && resultList.size() > 0) {
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }
    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if (resultList.size() > 0) {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
            if (!mSubmitButton.isEnabled()) {
                mSubmitButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
        } else {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
        }
        // 当为选择图片时候的状态
        if (resultList.size() == 0) {
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            Intent data = new Intent();
            if (mode == MODE_CROP) {
                File tmpFile = FileUtils.createTmpFile(this);
                HDApp.getInstance().setSingleChooseFile(tmpFile);
                new Crop(Uri.fromFile(imageFile))
                        .output(Uri.fromFile(tmpFile))
                        .withWidth(640)
                        .start(this);
                finish();
            } else {
                resultList.add(imageFile.getAbsolutePath());
                data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
