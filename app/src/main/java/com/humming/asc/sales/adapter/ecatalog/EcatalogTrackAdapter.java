package com.humming.asc.sales.adapter.ecatalog;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.ecatalog.EcatalogWebViewActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.dto.ecatalogResponse.bean.ecatalog.EcatalogStatusBean;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Ztq on 2018/06/20.
 * 报价单跟踪
 */

public class EcatalogTrackAdapter extends BaseAdapter<EcatalogStatusBean> {

    public EcatalogTrackAdapter(List<EcatalogStatusBean> data) {
        super(R.layout.list_item_ecatalog_track, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EcatalogStatusBean item, int position) {
        if (position == 0) {
            float density = Application.getInstance().getCurrentActivity().getResources().getDisplayMetrics().density;
            int left = (int) (20 * density + 0.5f);// 4.9->4, 4.1->4, 四舍五ru
            int right = (int) (20 * density + 0.5f);// 4.9->4, 4.1->4, 四舍五ru
            int top = (int) (30 * density + 0.5f);// 4.9->4, 4.1->4, 四舍五ru
            helper.getConvertView().setPadding(left, top, right, 0);
        } else {

        }
        TextView contentTv = helper.getView(R.id.item_ecatalog_track_content);
        SpannableStringBuilder spannableStringBuilder = checkAutoLink(item.getDes());
        contentTv.setText(spannableStringBuilder);
        contentTv.setMovementMethod(LinkMovementMethod.getInstance());
        helper.setText(R.id.item_ecatalog_track_time, item.getElseTime())
                .setText(R.id.item_ecatalog_track_date, item.getHourTime())
                .setText(R.id.item_ecatalog_track_name, item.getName());
        Glide.with(helper.getConvertView().getContext())
                .load(item.getIcon())
                .into((ImageView) helper.getView(R.id.item_ecatalog_track_img));

    }


    private SpannableStringBuilder checkAutoLink(String url) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(url);
        Matcher matcher = Patterns.WEB_URL.matcher(url);
        while (matcher.find()) {
            setClickableSpan(spannableStringBuilder, matcher);
        }
        return spannableStringBuilder;
    }

    /*//设置超链接点击
    private SpannableStringBuilder checkAutoLink(String url) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(url);
        Pattern pattern = Pattern.compile("([\\s>])([\\w]+?://[\\w\\\\x80-\\\\xff\\#$%&~/.\\-;:=,?@\\[\\]+]*)");
        Matcher matcher = pattern.matcher(spannableStringBuilder);
        while (matcher.find()) {
            setClickableSpan(spannableStringBuilder, matcher);
        }
        Pattern pattern2 = Pattern.compile("([\\s>])((www|ftp)\\.[\\w\\\\x80-\\\\xff\\#$%&~/.\\-;:=,?@\\[\\]+]*)");
        matcher.reset();
        matcher = pattern2.matcher(spannableStringBuilder);
        while (matcher.find()) {
            setClickableSpan(spannableStringBuilder, matcher);
        }
        return spannableStringBuilder;
    }*/


    //给符合的设置自定义点击事件
    private void setClickableSpan(SpannableStringBuilder clickableHtmlBuilder, final Matcher matcher) {
        int start = matcher.start();
        int end = matcher.end();
        final String url = matcher.group();
        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), EcatalogWebViewActivity.class);
                intent.putExtra("ecatalog_url", url);
                Application.getInstance().getCurrentActivity().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.link_color));
        clickableHtmlBuilder.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    }
}
