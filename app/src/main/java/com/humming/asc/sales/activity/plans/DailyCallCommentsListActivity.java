package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.humming.asc.dp.presentation.ro.cp.dailycall.AddDailyCallCommentRO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.CommentsResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.CommentsVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.TextEditorActivity;
import com.humming.asc.sales.activity.TextEditorData;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.List;

public class DailyCallCommentsListActivity extends AbstractActivity {
    public static String dailyCallId;
    private ListView commentsListview;
    private List<CommentsVO> lists;
    private int[] itemPageResArray;
    private TextView accountName, subject, callPlan;
    private View addMyComments, back;
    public static final String ACCOUNT_NAME = "account_name";
    public static final String SUBJECT = "subject";
    public static final String CALL_PLAN = "call_plan";
    public static final String DAILY_CALL_ID = "daily_call_id";
    private String Delete = "delete";
    private DailyCallService dailyCallService;
    private Context context = Application.getInstance().getBaseContext();
    private MyArrayAdapter adapter;
    public static final int RESULT_CODE = 1123;
    public static final String ACTIVITY_COMMENTS_VALUE = "comments_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_call_comments_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemPageResArray = new int[]{R.layout.list_item_daily_call_comments, R.layout.list_item_view_page_right};
        commentsListview = (ListView) findViewById(R.id.content_daily_call_comments_list__listView);
        addMyComments = findViewById(R.id.content_daily_call_comments_list__add_comments);
        accountName = (TextView) findViewById(R.id.content_daily_call_comments_list__account_name);
        subject = (TextView) findViewById(R.id.content_daily_call_comments_list__subject);
        callPlan = (TextView) findViewById(R.id.content_daily_call_comments_list__call_plan);
        addMyComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorData textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.text_edit));
                textEditorData.setHint(context.getString(R.string.add_my_comments));
                textEditorData.setSingleLine(false);
                Application.getInstance().setTextEditorData(textEditorData);
                Intent editeIntent4 = new Intent(getBaseContext(), TextEditorActivity.class);
                startActivityForResult(editeIntent4, TextEditorActivity.ACTIVITY_EDIT_COMMENT_ADD_RESULT);
            }
        });
        accountName.setText(getIntent().getStringExtra(DailyCallCommentsListActivity.ACCOUNT_NAME));
        subject.setText(getIntent().getStringExtra(DailyCallCommentsListActivity.SUBJECT));
        callPlan.setText(getIntent().getStringExtra(DailyCallCommentsListActivity.CALL_PLAN));
        dailyCallService = Application.getDailyCallService();
        dailyCallId = getIntent().getStringExtra(DailyCallCommentsListActivity.DAILY_CALL_ID);
        dailyCallService.queryByDcIdComments(new ICallback<CommentsResultVO>() {

            @Override
            public void onDataReady(CommentsResultVO data) {
                lists = data.getData();
                adapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, lists, itemPageResArray, 0, Delete);
                commentsListview.setAdapter(adapter);

            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, dailyCallId);
        back = findViewById(R.id.content_daily_call_comments_list__back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyCallService.queryByRowId(new ICallback<DailyCallDetailResultVO>() {

                    @Override
                    public void onDataReady(DailyCallDetailResultVO data) {
                        DailyCallDetailVO data1 = data.getData();
                        Application.getInstance().setDailyCallDetail4Edit(data1);
                        data1.setAccountName(accountName.getText().toString());
                        Intent intent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
                        intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, data1.getAssocType());
                        intent.putExtra(DailyCallEditorActivity.TASK_ID, data1.getTaskId());
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }, dailyCallId);
            }
        });
    }


    class ViewHolder {
        TextView commentDate;
        TextView CommentName;
        TextView CommentCon;
        ImageView img;
        Button btnCompetitor;
        Button btnUntarget;
        Button btnDelete;
        Button btnDailyCall;
    }

    private class MyArrayAdapter extends AbstractItemPagerArrayAdapter<CommentsVO, ViewHolder> {
        private View.OnClickListener onItemClickListener;
        private String types;

        public MyArrayAdapter(Context context, int resource, List<CommentsVO> items, int[] itemPageResArray, int defaultPageIndex, String types) {
            super(context, resource, items, itemPageResArray, defaultPageIndex, types);
            this.types = types;
            onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    dailyCallService.deleteComments(new ICallback<ResultVO>() {
                        @Override
                        public void onDataReady(ResultVO data) {
                            lists.remove(Integer.parseInt(String.valueOf(v.getTag())));
                            adapter.notifyDataSetChanged();

                            Toast.makeText(getBaseContext(), "delete 成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, lists.get(Integer.parseInt(String.valueOf(v.getTag()))).getRowId());
                    updateViewPagerState(Integer.parseInt(String.valueOf(v.getTag())), 0, true);
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    if (position == 1) {
                        return 0.24f;
                    } else {
                        return 1f;
                    }
                }
            };
            return adapter;
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(int position, ViewHolder viewHolder, List<View> itemPages) {
            View rightView = itemPages.get(1);
            View centerView = itemPages.get(0);
            viewHolder.btnCompetitor = (Button) rightView.findViewById(R.id.right_competitor);
            viewHolder.btnDailyCall = (Button) rightView.findViewById(R.id.right_dailycall);
            viewHolder.btnDelete = (Button) rightView.findViewById(R.id.right_task);
            viewHolder.btnUntarget = (Button) rightView.findViewById(R.id.right_untarget);
            viewHolder.img = (ImageView) centerView
                    .findViewById(R.id.list_item_daily_call_comments_img);
            viewHolder.commentDate = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_comments__date);
            viewHolder.CommentName = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_comments__name);
            viewHolder.CommentCon = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_comments_content);
            viewHolder.btnDelete.setOnClickListener(onItemClickListener);
        }


        @Override
        protected void setItemData(int position, ViewHolder viewHolder, CommentsVO itemData) {
            viewHolder.btnUntarget.setVisibility(View.GONE);
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.btnDailyCall.setVisibility(View.GONE);
            viewHolder.btnCompetitor.setVisibility(View.GONE);
            viewHolder.CommentName.setText(itemData.getAccountName());
            viewHolder.commentDate.setText(itemData.getLastUpd());
            viewHolder.CommentCon.setText(itemData.getComments());
            viewHolder.btnDelete.setText("Delete");
            viewHolder.btnDelete.setTag(position);
            ImageLoader imageLoader = new ImageLoader(Application.getInstance().getRequestQueue(), new BitmapCache());
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.img, R.drawable.ic_add, R.drawable.ic_add);
            imageLoader.get(itemData.getHeadImg(), listener);

        }

    }

    private class BitmapCache implements ImageLoader.ImageCache {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle;
        switch (requestCode) {
            case TextEditorActivity.ACTIVITY_EDIT_COMMENT_ADD_RESULT:
                resultBundle = data.getExtras();
                final String text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                AddDailyCallCommentRO addComment = new AddDailyCallCommentRO();
                addComment.setComments(text);
                addComment.setDailyCallId(dailyCallId);
                dailyCallService.addComments(new ICallback<ResultVO>() {
                    @Override
                    public void onDataReady(ResultVO data) {
                        /*CommentsVO c = new CommentsVO();
                        Date date = new Date();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        c.setComments(text);
                        c.setRowId(dailyCallId);
                        c.setLastUpd(df.format(date));
                        if ("".equals(lists.get(0).getHeadImg())) {
                            c.setHeadImg("");
                        } else {
                            c.setHeadImg(lists.get(0).getHeadImg());
                        }
                        lists.add(0, c);
                        adapter.notifyDataSetChanged();*/
                        dailyCallService.queryByDcIdComments(new ICallback<CommentsResultVO>() {

                            @Override
                            public void onDataReady(CommentsResultVO data) {
                                lists = data.getData();
                                adapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                                        R.layout.list_item_view_pager_delete, lists, itemPageResArray, 0, Delete);
                                commentsListview.setAdapter(adapter);

                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }
                        }, dailyCallId);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }, addComment);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Bundle resultBundle = new Bundle();
                resultBundle.putString(
                        DailyCallCommentsListActivity.ACTIVITY_COMMENTS_VALUE,
                        lists.size() + "");
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        DailyCallCommentsListActivity.RESULT_CODE,
                        resultIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bundle resultBundle = new Bundle();
            resultBundle.putString(
                    DailyCallCommentsListActivity.ACTIVITY_COMMENTS_VALUE,
                    lists.size() + "");
            Intent resultIntent = new Intent()
                    .putExtras(resultBundle);
            setResult(
                    DailyCallCommentsListActivity.RESULT_CODE,
                    resultIntent);
            finish();
        }

        return false;

    }
}
