package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.cp.CPSummaryResultVO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.CalendarTotalResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallQueryResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.component.main.plans.MonthDateView;
import com.humming.asc.sales.model.BackRefreshEvent;
import com.humming.asc.sales.model.DailyCallDate;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DailyCallDateActivity extends AbstractActivity {

    private ListView listView;
    private DailyCallService dailyCallService;
    private ArrayList<DailyCallVO> currentlist;
    private CPSummaryVO summary;
    private Application mAPP;
    private MainActivity.MyHandler mHandler;
    private MonthDateView monthDateView;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private List<String> dateList;
    private List<DailyCallDate> DCDateList;
    private int[] itemPageResArray;
    private String dates = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_call_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoading.show();
        mAPP = (Application) getApplication();
        // 获得共享变量实例
        mHandler = mAPP.getHandler();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.content_daily_call_date__listViews);
        dailyCallService = Application.getDailyCallService();
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        itemPageResArray = new int[]{R.layout.list_item_daily_call, R.layout.list_item_view_page_right};
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        queryDailyCallDateTotal();
        int month = monthDateView.getmSelMonth() + 1;
        StringBuilder sb = null;
        String months = "";
        if (month < 10) {
            sb = new StringBuilder();
            sb.append("0" + month);
            months = sb.toString();
        } else {
            months = month + "";
        }
        StringBuilder sbDay = null;
        String days = "";
        int day = monthDateView.getmSelDay();
        if (day < 10) {
            sbDay = new StringBuilder();
            sbDay.append("0" + day);
            days = sbDay.toString();
        } else {
            days = day + "";
        }
        dates = monthDateView.getmSelYear() + "-" + months + "-" + days;
        queryDailyCall(dates);
        tv_date = (TextView) findViewById(R.id.date_text);
        tv_week = (TextView) findViewById(R.id.week_text);
        tv_today = (TextView) findViewById(R.id.tv_today);
        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthDateView.setTodayToView();
            }
        });
        monthDateView.setDateClick(new MonthDateView.DateClick() {

            @Override
            public void onClickOnDate() {
                int month = monthDateView.getmSelMonth() + 1;
                Log.v("xxxx", "month:" + month + "day:" + monthDateView.getmSelDay());
                StringBuilder sb = null;
                String months = "";
                if (month < 10) {
                    sb = new StringBuilder();
                    sb.append("0" + month);
                    months = sb.toString();
                } else {
                    months = month + "";
                }
                StringBuilder sbDay = null;
                String days = "";
                int day = monthDateView.getmSelDay();
                if (day < 10) {
                    sbDay = new StringBuilder();
                    sbDay.append("0" + day);
                    days = sbDay.toString();
                } else {
                    days = day + "";
                }
                dates = monthDateView.getmSelYear() + "-" + months + "-" + days;
                queryDailyCall(dates);
            }
        });
    }

    //查询所有有dailycall的日期
    private void queryDailyCallDateTotal() {
        dailyCallService.queryCalendarTotal(new ICallback<CalendarTotalResultVO>() {
            @Override
            public void onDataReady(CalendarTotalResultVO data) {
                DCDateList = new ArrayList<DailyCallDate>();
                dateList = data.getData();
                List<Integer> monthlist = new ArrayList<Integer>();
                for (String date : dateList) {
                    String flag = date.substring(5, 7);
                    if ("0".equals(date.substring(5, 6))) {
                        flag = date.substring(6, 7);
                    } else {

                    }
                    monthlist.add(Integer.parseInt(flag));
                }
                //去除重复
                HashSet h = new HashSet(monthlist);
                monthlist.clear();
                monthlist.addAll(h);
                for (int s : monthlist) {
                    List<Integer> list = new ArrayList<Integer>();
                    for (String date : dateList) {
                        String flag = date.substring(5, 7);
                        if ("0".equals(date.substring(5, 6))) {
                            flag = date.substring(6, 7);
                        } else {

                        }
                        if (Integer.parseInt(flag) == s) {
                            String detail = date.substring(8, 10);
                            if ("0".equals(date.substring(8, 9))) {
                                detail = date.substring(9, 10);
                            } else {

                            }
                            list.add(Integer.parseInt(detail));
                        } else {

                        }
                    }
                    DCDateList.add(new DailyCallDate(s, list));
                }
                monthDateView.setDaysHasThingList(DCDateList);
                monthDateView.setTextView(tv_date, tv_week);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    //dailyCall 查询
    public void queryDailyCall(final String date) {
        dailyCallService.queryCalendarDetail(new ICallback<DailyCallQueryResultVO>() {
            @Override
            public void onDataReady(DailyCallQueryResultVO result) {
                mLoading.hide();
                currentlist = (ArrayList<DailyCallVO>) result.getData();
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, currentlist, itemPageResArray, 0, "");
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_daily_call, menu);
        menu.findItem(R.id.action_daily_call_search).setVisible(false);
        menu.findItem(R.id.action_daily_call_condition).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //add dailyCall
        if (id == R.id.action_daily_call_add) {
            Application.getInstance().setDailyCallDetail4Edit(null);
            Intent intent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
            intent.putExtra(DailyCallEditorActivity.DAILY_CALL_DATE, dates + " " + "00:00");
            intent.putExtra(DailyCallEditorActivity.IFDAILY_CALL_LIST, "false");
            startActivity(intent);
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class ViewHolder {
        ImageView type;
        ImageView assocType;
        TextView name;
        TextView subject;
        TextView meetcount;
        ImageView state;
        TextView upd;
        Button btnComment;
        View view;
        TextView commentsCount;
        TextView salesName;
    }

    class MyArrayAdapter extends AbstractItemPagerArrayAdapter<DailyCallVO, ViewHolder> {
        private View.OnClickListener onbtnCommentsClickListener;
        private View.OnClickListener onbtnItemClickListener;

        public MyArrayAdapter(Context context, int resource, final List<DailyCallVO> items, int[] itemPageResArray, int defaultPageIndex, final String types) {
            super(context, resource, items, itemPageResArray, defaultPageIndex, types);
            onbtnCommentsClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Intent intentComment = new Intent(getBaseContext(), DailyCallCommentsListActivity.class);
                    intentComment.putExtra(DailyCallCommentsListActivity.ACCOUNT_NAME, items.get(position).getAccountName());
                    intentComment.putExtra(DailyCallCommentsListActivity.SUBJECT, items.get(position).getSubject());
                    intentComment.putExtra(DailyCallCommentsListActivity.CALL_PLAN, items.get(position).getMeetingContent());
                    intentComment.putExtra(DailyCallCommentsListActivity.DAILY_CALL_ID, items.get(position).getRowId());
                    startActivity(intentComment);
                    updateViewPagerState(position, 0, true);
                    notifyDataSetChanged();
                }
            };
            onbtnItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = Integer.parseInt(v.getTag().toString());
                    dailyCallService.queryByRowId(new ICallback<DailyCallDetailResultVO>() {

                        @Override
                        public void onDataReady(DailyCallDetailResultVO data) {

                            DailyCallDetailVO data1 = data.getData();
                            data1.setAccountName(currentlist.get(position).getAccountName());
                            Application.getInstance().setDailyCallDetail4Edit(data1);
                            Intent intent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
                            intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, data1.getAssocType());
                            if ("Key Account".equals(data1.getAssocType())) {
                                //  intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, data1.getRowId());
                                intent.putExtra(DailyCallEditorActivity.TASK_ID, data1.getTaskId());
                            } else if ("Other Account".equals(data1.getAssocType())) {
                                intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, data1.getOtherAccountId());
                            } else {
                                intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, data1.getTargetLeadId());
                                intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, "Leads");
                            }
                            Log.v("-----accountName:-----",data1.getAccountName());
                            intent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME,data1.getAccountName());
                        //    intent.putExtra(DailyCallEditorActivity.IFDAILY_CALL_LIST, "true");
                            intent.putExtra(DailyCallEditorActivity.IFDAILY_CALL_LIST, "false");
                            startActivity(intent);
                            updateViewPagerState(position, 0, true);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, currentlist.get(position).getRowId());
                }
            };
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(final int position, ViewHolder viewHolder, List<View> itemPages) {
            View rightView = itemPages.get(1);
            View centerView = itemPages.get(0);
            viewHolder.view = centerView;
            viewHolder.btnComment = (Button) rightView.findViewById(R.id.right_task);
            viewHolder.commentsCount = (TextView) centerView.findViewById(R.id.list_item_daily_call_comments_count);
            viewHolder.assocType = (ImageView) centerView
                    .findViewById(R.id.list_item_daily_call_assoc_type);
            viewHolder.name = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_account_name);
            viewHolder.subject = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_subject);
            viewHolder.meetcount = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_meetcount);
            viewHolder.state = (ImageView) centerView
                    .findViewById(R.id.list_item_daily_call_state);
            viewHolder.type = (ImageView) centerView
                    .findViewById(R.id.list_item_daily_call_state_type);
            viewHolder.upd = (TextView) centerView.findViewById(R.id.list_item_daily_call_up_date);
            viewHolder.salesName = (TextView) centerView.findViewById(R.id.list_item_daily_call_sales_name);
            viewHolder.btnComment.setOnClickListener(onbtnCommentsClickListener);
            viewHolder.view.setOnClickListener(onbtnItemClickListener);
        }


        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    //无滑动
                    if (position == 1) {

                        //  return 0.24f;
                        return 0f;

                    } else {
                        return 1f;
                    }
                }
            };
            return adapter;
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, DailyCallVO dailyCallVO) {
            String assocType = dailyCallVO.getAssocType().toLowerCase();
            String name = dailyCallVO.getAccountName();
            viewHolder.view.setTag(position);
            int assocTypeImgRes = R.drawable.icon_l;
            viewHolder.btnComment.setVisibility(View.VISIBLE);
            viewHolder.btnComment.setText("Comments");
            viewHolder.btnComment.setTag(position);
            String state = dailyCallVO.getStatus();
            int stateImgRes = R.drawable.ic_state_progress;
            int stateBgRes = R.drawable.bg_oval_y;
            if ("planned".equalsIgnoreCase(state)) {

            } else if ("completed".equalsIgnoreCase(state)) {
                stateImgRes = R.drawable.ic_state_completed;
                stateBgRes = R.drawable.bg_oval_g;
            } else {
                stateImgRes = R.drawable.ic_state_cancel;
                stateBgRes = R.drawable.bg_oval_r;
            }

            String type = dailyCallVO.getType().toLowerCase();
            int typeImgRes = R.drawable.ic_type_visit;
            if ("email/sms".equalsIgnoreCase(type)) {
                typeImgRes = R.drawable.ic_type_mail;
            } else if ("key account".equalsIgnoreCase(type)) {
                typeImgRes = R.drawable.ic_type_phone;
            }
            if ("key account".equalsIgnoreCase(assocType)) {
                assocTypeImgRes = R.drawable.icon_k;
            } else if ("other account".equalsIgnoreCase(assocType)) {
                assocTypeImgRes = R.drawable.icon_o;
            } else if ("leads".equalsIgnoreCase(assocType)) {
                assocTypeImgRes = R.drawable.icon_l;
            } else {
                assocTypeImgRes = R.drawable.icon_l;
            }
            String subject = dailyCallVO.getSubject();
            viewHolder.assocType.setImageResource(assocTypeImgRes);
            viewHolder.name.setText(name);
            viewHolder.subject.setText(subject);
            viewHolder.meetcount.setText(dailyCallVO.getMeetingContent());
            viewHolder.state.setImageResource(stateImgRes);
            viewHolder.state.setBackgroundResource(stateBgRes);
            viewHolder.type.setImageResource(typeImgRes);
            viewHolder.upd.setText(dailyCallVO.getLastUpd());
            viewHolder.salesName.setText(dailyCallVO.getSaleName());
            viewHolder.commentsCount.setText(dailyCallVO.getCommentsCount() + "");
        }
    }


    public void initDailyCall() {
        dailyCallService.getSummary(new ICallback<CPSummaryResultVO>() {
            @Override
            public void onDataReady(CPSummaryResultVO result) {
                summary = result.getData();
                Application.getInstance().setSummary(summary);
                mHandler.msgContent = "返回的数据";
                mHandler.sendEmptyMessage(0x123);
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }


    //eventBus回调(Item Cost 返回的回调)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(BackRefreshEvent backRefreshEvent) {
        queryDailyCallDateTotal();
        int month = monthDateView.getmSelMonth() + 1;
        StringBuilder sb = null;
        String months = "";
        if (month < 10) {
            sb = new StringBuilder();
            sb.append("0" + month);
            months = sb.toString();
        } else {
            months = month + "";
        }
        StringBuilder sbDay = null;
        String days = "";
        int day = monthDateView.getmSelDay();
        if (day < 10) {
            sbDay = new StringBuilder();
            sbDay.append("0" + day);
            days = sbDay.toString();
        } else {
            days = day + "";
        }
        dates = monthDateView.getmSelYear() + "-" + months + "-" + days;
        queryDailyCall(dates);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            initDailyCall();
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
