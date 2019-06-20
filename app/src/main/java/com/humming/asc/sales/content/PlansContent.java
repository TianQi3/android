package com.humming.asc.sales.content;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.humming.asc.dp.presentation.vo.cp.CPSummaryVO;
import com.humming.asc.dp.presentation.vo.cp.DCSummaryInfoVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.plans.CommentsListActivity;
import com.humming.asc.sales.activity.plans.DailyCallListActivity;
import com.humming.asc.sales.activity.plans.LeadsListActivity;
import com.humming.asc.sales.activity.plans.TaskListActivity;
import com.humming.asc.sales.component.PageIndicator;
import com.humming.asc.sales.component.RectItem;
import com.humming.asc.sales.component.main.plans.PlanChart;
import com.humming.asc.sales.model.KeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 1/7/16.
 */
public class PlansContent extends LinearLayout {
    private final RectItem taskItem;
    private final RectItem leadsItem;
    private final RectItem commentsItem;
    private final RectItem plansItem;
    private View view;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private PlanChart planChart1;
    private PlanChart planChart2;
    private List<View> list;

    public PlansContent(Context context) {
        this(context, null);
    }

    public PlansContent(final Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_plans, this);

        taskItem = (RectItem) view.findViewById(R.id.content_planns__task_item);
        leadsItem = (RectItem) view.findViewById(R.id.content_planns__leads_item);
        commentsItem = (RectItem) view.findViewById(R.id.content_planns__comments_item);
        plansItem = (RectItem) view.findViewById(R.id.content_planns__plans_item);

        taskItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Application app = Application.getInstance();
                Intent intent = new Intent(app.getBaseContext(), TaskListActivity.class);
                app.getCurrentActivity().startActivity(intent);
            }
        });

        leadsItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getBaseContext(), LeadsListActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
            }
        });
        commentsItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getBaseContext(), CommentsListActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
            }
        });

        viewPager = (ViewPager) view.findViewById(R.id.content_planns__viewpager);
        PageIndicator indicator = (PageIndicator) view.findViewById(R.id.content_planns__indicator);

        indicator.setCount(2);

        planChart1 = new PlanChart(context);
        planChart2 = new PlanChart(context);

        View.OnClickListener onPlanChartClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application app = Application.getInstance();
                Intent intent = new Intent(app.getBaseContext(), DailyCallListActivity.class);
                app.getCurrentActivity().startActivity(intent);
            }
        };
        planChart1.setOnClickListener(onPlanChartClick);
        planChart2.setOnClickListener(onPlanChartClick);
        list = new ArrayList<View>();
        list.add(planChart1);
        list.add(planChart2);
        adapter = new PlanChartPagerAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(indicator);
    }

    public Handler mHandlers = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    int totalcount = list.size();//autoChangeViewPager.getChildCount();
                    int currentItem = viewPager.getCurrentItem();
                    int toItem = currentItem + 1 == totalcount ? 0 : currentItem + 1;
                    viewPager.setCurrentItem(toItem, true);
                    //每两秒钟发送一个message，用于切换viewPager中的图片
                    this.sendEmptyMessageDelayed(1, 3000);
            }
        }
    };

    public void setInfoData(DCSummaryInfoVO info) {
        int completed = info.getDcCompleted();
        int planned = info.getDcPlanned();
        int canceled = info.getDcCanceled();
        int keyAccount = info.getDcKeyAcc();
        int otherAccount = info.getDcOtherAcc();
        int leads = info.getDcLeads();
        int total = info.getDcTotal();
        List<KeyValue> keyValueList1 = new ArrayList<>();
        Resources res = Application.getInstance().getBaseContext().getResources();
        keyValueList1.add(new KeyValue(res.getString(R.string.label_completed), completed));
        keyValueList1.add(new KeyValue(res.getString(R.string.label_planned), planned));
        keyValueList1.add(new KeyValue(res.getString(R.string.label_canceled), canceled));
        planChart1.setData(total, keyValueList1);
        List<KeyValue> keyValueList2 = new ArrayList<>();
        keyValueList2.add(new KeyValue(res.getString(R.string.label_key_account), keyAccount));
        keyValueList2.add(new KeyValue(res.getString(R.string.label_other_account), otherAccount));
        keyValueList2.add(new KeyValue(res.getString(R.string.label_leads), leads));
        planChart2.setData(total, keyValueList2);
    }

    public void setSummaryData(CPSummaryVO summary) {
        int taskTotal = summary.getTaskTotal();
        int taskClose = summary.getTaskClose();
        int leadsTotal = summary.getLeadsTotal();
        int leadsTarget = summary.getLeadsTarget();
        int commentsTotal = summary.getCommentsTotal();
        int annualPlanTotal = summary.getAnnualPlanTotal();
        int monthlyPlanTotal = summary.getMonthlyPlanTotal();
        taskItem.setTopValue(String.valueOf(taskTotal));
        taskItem.setBottomValue(String.valueOf(taskClose));
        leadsItem.setTopValue(String.valueOf(leadsTarget));
        leadsItem.setBottomValue(String.valueOf(leadsTotal));
        commentsItem.setTopValue(String.valueOf(commentsTotal));
        plansItem.setTopValue(String.valueOf(annualPlanTotal));
        plansItem.setBottomValue(String.valueOf(monthlyPlanTotal));
    }

    public class PlanChartPagerAdapter extends PagerAdapter {
        private final List<View> listView;

        public PlanChartPagerAdapter(List<View> viewList) {
            super();
            this.listView = viewList;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View x = listView.get(position);
            collection.addView(x, 0);
            return x;
        }

        @Override
        public int getCount() {
            return listView.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }
}
