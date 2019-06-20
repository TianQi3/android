package com.humming.asc.sales.activity.plans;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.component.SquareCenterImageView;
import com.humming.asc.sales.model.ImageItem;
import com.humming.asc.sales.service.ICallback;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import me.nereo.imagechoose.MultiImageSelectorActivity;

public class DailyCallPhotoActivity extends AbstractActivity {

    private GridView imageGridView;
    private static final int MY_PERMISSIONS_REQUEST_READ = 5;
    private static final int REQUEST_IMAGE = 2;
    private Application app = Application.getInstance();
    private Toolbar toolbar;
    private ImageView imageChoose;
    private ArrayList<String> mSelectPath;
    private ArrayList<ImageItem> mSelectPaths;
    private ArrayList<ImageItem> finallyPathList;
    PhotoListArrayAdapter adapter;
    public String KEY_IMAGE = "fileName";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    public String KEY_NAME = "files";
    private ArrayList<ImageItem> lists;
    public static final String PHOTO_LISTS = "photo_list";
    public static final int ACTIVITY_PHOTO_RESULT = 12121;
    public static DisplayImageOptions mNormalImageOptions;
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    public static final String IMAGES_FOLDER = SDCARD_PATH + File.separator + "asc" + File.separator + "images" + File.separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_call_photo_editor);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initImageLoader(this);
        mSelectPaths = new ArrayList<ImageItem>();
        finallyPathList = app.getImageList();
        /*lists = getIntent().getStringArrayListExtra(PHOTO_LISTS);
        if(lists!=null){
            for (String p : lists) {
                ImageItem imageItem = new ImageItem();
                imageItem.setPath(p);
                imageItem.setSelect(false);
                mSelectPaths.add(imageItem);
                finallyPathList.add(imageItem);
            }
        }*/

        imageChoose = (ImageView) findViewById(R.id.content_daily_call_photo_editor_choose);
        imageChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DailyCallPhotoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //申请CAMERA的权限
                    ActivityCompat.requestPermissions(DailyCallPhotoActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                if (ContextCompat.checkSelfPermission(DailyCallPhotoActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(DailyCallPhotoActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ);
                } else {
                    int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                    // selectedMode = MultiImageSelectorActivity.MODE_MULTI;//裁切

                    boolean showCamera = true;//启用相机
                    boolean showText = false;//启用文本
                    int maxNum = 9;
                    Intent intent = new Intent(DailyCallPhotoActivity.this, MultiImageSelectorActivity.class);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);// 是否显示拍摄图片
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_TEXT, showText);//显示文本
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);// 最大可选择图片数量
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode); // 选择模式
                    if (mSelectPath != null && mSelectPath.size() > 0) {// 回显已经选择的图片
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                    }
                    startActivityForResult(intent, REQUEST_IMAGE);
                }
            }
        });

        imageGridView = (GridView) findViewById(R.id.content_daily_call_photo_editor_showimage);
        imageGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                finallyPathList.get(position).setSelect(true);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        if (finallyPathList != null) {
            adapter = new PhotoListArrayAdapter(this, finallyPathList);
            imageGridView.setAdapter(adapter);
        } else {
            finallyPathList = new ArrayList<ImageItem>();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mLoading.show();
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String p : mSelectPath) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.setPath(p);
                    imageItem.setSelect(false);
                    mSelectPaths.add(imageItem);
                }
                upLoadImage();


            } else {
                // if (HDApp.getInstance().getSingleChooseFile() != null && HDApp.getInstance().getSingleChooseFile().getTotalSpace() > 0) {
                //     Bitmap loacalBitmap = getLoacalBitmap(HDApp.getInstance().getSingleChooseFile());
                //      HDApp.getInstance().setSingleChooseFile(null);

                //   } else {
                //      Toast.makeText(this, "裁切完成 空文件", Toast.LENGTH_SHORT).show();
                //   }
            }
        }
    }

    public static Bitmap getLoacalBitmap(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class PhotoListArrayAdapter extends BaseAdapter {
        private Context context;
        public ArrayList<ImageItem> lists;
        private ArrayList<String> dates = new ArrayList<String>();

        public PhotoListArrayAdapter(Context context, ArrayList<ImageItem> list) {
            this.lists = list;
            this.context = context;
            for (int i = 0; i < list.size(); i++) {
                dates.add(list.get(i).getPath());
            }
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final SquareCenterImageView imageView = new SquareCenterImageView(DailyCallPhotoActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(lists.get(position).getPath(), imageView);
            mLoading.hide();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DailyCallPhotoActivity.this, DcImageDetailActivity.class);
                    intent.putExtra("images", (ArrayList<String>) dates);
                    intent.putExtra("position", position);
                    int[] location = new int[2];
                    imageView.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);

                    intent.putExtra("width", imageView.getWidth());
                    intent.putExtra("height", imageView.getHeight());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
            /*ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_daily_call_photo, null);
                holder.imageView = (ImageView) convertView.
                        findViewById(R.id.list_item_daily_call_photo_value);
                holder.imageViewDelete = (ImageView) convertView.
                        findViewById(R.id.list_item_daily_call_photo_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader imageLoader = new ImageLoader(Application.getInstance().getRequestQueue(), new BitmapCache());
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.imageView, R.drawable.ic_add, R.drawable.ic_add);
            imageLoader.get(lists.get(position).getPath(), listener,130,130,ImageView.ScaleType.CENTER_INSIDE);
            if (lists.get(position).getSelect()) {
                holder.imageViewDelete.setVisibility(View.VISIBLE);
                holder.imageViewDelete.setBackgroundResource(R.drawable.bg_oval_r);
                holder.imageViewDelete.setImageResource(R.drawable.ic_state_cancel);
               *//* Animation shake = AnimationUtils.loadAnimation(DailyCallPhotoActivity.this, R.anim.image_shake);
                shake.reset();
                shake.setFillAfter(false);
                shake.setFillBefore(true);
                convertView.startAnimation(shake);*//*
            } else {
                holder.imageViewDelete.setVisibility(View.GONE);
                mLoading.hide();
            }
            holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lists.remove(position);
                  //  mSelectPaths.remove(position);
                  //  finallyPathList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });*/
            return imageView;
        }
    }

    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    class ViewHolder {
        ImageView imageView;
        ImageView imageViewDelete;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_task_add_confirm:
                app.setImageList(finallyPathList);
                Bundle resultBundle = new Bundle();
                resultBundle.putString(
                        DailyCallPhotoActivity.PHOTO_LISTS, finallyPathList.size() + "");
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        ACTIVITY_PHOTO_RESULT,
                        resultIntent);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void upLoadImage() {
        final File file = new File(mSelectPaths.get(mSelectPaths.size() - 1).getPath());
        String image = getStringImage(convertToBitmap(mSelectPaths.get(mSelectPaths.size() - 1).getPath(), 130, 130));

        //Getting Image Name  http://asc-dp.oss-cn-hangzhou.aliyuncs.com/images/dailycall/E0790/1457330761219.jpg
        String name = file.getName();

        //Creating parameters
        Map<String, String> params = new Hashtable<String, String>();

        //Adding parameters
        params.put(KEY_IMAGE, image);
        params.put(KEY_NAME, name);

        Application.getDailyCallService().addImg(new ICallback<ResultVO>() {

            @Override
            public void onDataReady(ResultVO data) {
                if (data.getData() != null) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.setSelect(false);
                    imageItem.setPath(data.getData().toString());
                    finallyPathList.add(imageItem);
                    adapter = new PhotoListArrayAdapter(DailyCallPhotoActivity.this, finallyPathList);
                    imageGridView.setAdapter(adapter);
                }

            }

            @Override
            public void onError(Throwable throwable) {
            }
        }, mSelectPaths.get(mSelectPaths.size() - 1).getPath(), file, params);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /*private class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> cache;

        public BitmapCache() {
            cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return cache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
        }
    }*/
    private void initImageLoader(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCacheAware<String, Bitmap> memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        mNormalImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisc(true)
                .resetViewBeforeLoading(true).build();

        // This
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(mNormalImageOptions)
                .denyCacheImageMultipleSizesInMemory().discCache(new UnlimitedDiscCache(new File(IMAGES_FOLDER)))
                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(memoryCache)
                // .memoryCacheSize(memoryCacheSize)
                .tasksProcessingOrder(QueueProcessingType.LIFO).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ);
                    } else {
                        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                        // selectedMode = MultiImageSelectorActivity.MODE_MULTI;//裁切

                        boolean showCamera = true;//启用相机
                        boolean showText = false;//启用文本
                        int maxNum = 9;
                        Intent intent = new Intent(DailyCallPhotoActivity.this, MultiImageSelectorActivity.class);
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);// 是否显示拍摄图片
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_TEXT, showText);//显示文本
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);// 最大可选择图片数量
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode); // 选择模式
                        if (mSelectPath != null && mSelectPath.size() > 0) {// 回显已经选择的图片
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                        }
                        startActivityForResult(intent, REQUEST_IMAGE);
                    }
                } else {
                    Toast.makeText(DailyCallPhotoActivity.this, "请打开相机权限", Toast.LENGTH_LONG).show();

                }
                break;
            case MY_PERMISSIONS_REQUEST_READ:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                    // selectedMode = MultiImageSelectorActivity.MODE_MULTI;//裁切

                    boolean showCamera = true;//启用相机
                    boolean showText = false;//启用文本
                    int maxNum = 9;
                    Intent intent = new Intent(DailyCallPhotoActivity.this, MultiImageSelectorActivity.class);
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);// 是否显示拍摄图片
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_TEXT, showText);//显示文本
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);// 最大可选择图片数量
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode); // 选择模式
                    if (mSelectPath != null && mSelectPath.size() > 0) {// 回显已经选择的图片
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                    }
                    startActivityForResult(intent, REQUEST_IMAGE);

                } else {
                    // Permission Denied
                    //  Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                    return;
                }
                break;
        }
    }
}
