package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.constant.FrenchKind;
import com.snail.french.constant.NameConstants;
import com.snail.french.manager.ExerciseManager;
import com.snail.french.manager.StatusResponseManager;
import com.snail.french.model.status.PItem;
import com.snail.french.model.status.Status;
import com.snail.french.model.status.StatusResponse;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class ReportActivity extends BaseActivity {

    @Bind(R.id.list_view)
    ExpandableListView listView;
    @Bind(R.id.titlebar)
    CommonTitle titlebar;

    private View headerView;
    private TextView allScoreTV;
    private TextView scoreTV;
    private TextView daysTV;
    private TextView numbersTV;
    private TextView accuracyTV;
    private TextView levelTV;

    private StatusResponse mStatusResponse;
    private ReportAdapter mReportAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        mStatusResponse = StatusResponseManager.getInstance()
                .get(ExerciseManager.getInstance().getFrenchKind());

        if (mStatusResponse == null) {
            ToastUtil.shortToast(this, "暂无数据报告");
            this.finish();
        }

        titlebar.setTitleText(ExerciseManager.getInstance().getFrenchKind().getName() + "数据报告");

        headerView = LayoutInflater.from(this).inflate(R.layout.report_header, null, false);
        allScoreTV = (TextView) headerView.findViewById(R.id.report_all_score);
        scoreTV = (TextView) headerView.findViewById(R.id.report_score);
        daysTV = (TextView) headerView.findViewById(R.id.report_days);
        numbersTV = (TextView) headerView.findViewById(R.id.report_numbers);
        accuracyTV = (TextView) headerView.findViewById(R.id.report_accuracy);
        levelTV = (TextView) headerView.findViewById(R.id.report_level);

        listView.setGroupIndicator(null);
        listView.addHeaderView(headerView);


        allScoreTV.setText("总分数：" + mStatusResponse.total_score);
        scoreTV.setText(String.valueOf(mStatusResponse.forcast_score));
        daysTV.setText(mStatusResponse.exercise_days + "\n练习天数");
        numbersTV.setText(mStatusResponse.exercise_question_number + "\n答题量");
        accuracyTV.setText(mStatusResponse.accuracy + "%\n正确率");
        levelTV.setText(mStatusResponse.level);

        listView.setAdapter(new ReportAdapter(this, mStatusResponse));
    }

    private static class GroupViewHolder {
        TextView title;
        TextView count;
    }

    private static class ChildViewHolder {
        TextView title;
        TextView count;
    }


    class ReportAdapter extends BaseExpandableListAdapter {

        private StatusResponse statusResponses;
        private LayoutInflater inflater;

        public ReportAdapter(Context context, StatusResponse statusResponse) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.statusResponses = statusResponse;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return statusResponses.pItemList.get(groupPosition).statusList.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.report_list_item_child, parent, false);

                childViewHolder = new ChildViewHolder();

                childViewHolder.title = (TextView) convertView.findViewById(R.id.child_title);
                childViewHolder.count = (TextView) convertView.findViewById(R.id.child_count);

                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }

            final Status status = (Status) getChild(groupPosition, childPosition);
            String title = NameConstants.getName(StringUtils.isEmpty(status.subType) ? status.type : status.subType);
            childViewHolder.title.setText(title);

            String answerAccuracy;
            if(status.total_quesstion_num == 0 || status.exercise_question_number == 0){
                answerAccuracy =  "正确率 0.00%";
            } else {
                DecimalFormat decimalFormat=new DecimalFormat("0.00");
                answerAccuracy = "正确率"
                        + decimalFormat.format((double)status.correct_num / status.exercise_question_number * 100) + "%";
            }
            childViewHolder.count.setText(answerAccuracy);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            // 专四 阅读 作文 不展开
            if (ExerciseManager.getInstance().getFrenchKind() == FrenchKind.TEM4) {
                String pName = statusResponses.pItemList.get(groupPosition).name;
                if ("R".equals(pName) || "C".equals(pName)) {
                    return 0;
                }
            }

            return statusResponses.pItemList.get(groupPosition).statusList.size();
        }


        @Override
        public Object getGroup(int groupPosition) {
            return statusResponses.pItemList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return statusResponses.pItemList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            GroupViewHolder groupViewHolder = null;

            if (convertView == null) {
                groupViewHolder = new GroupViewHolder();

                convertView = inflater.inflate(R.layout.report_list_item_parent, parent, false);

                groupViewHolder.title = (TextView) convertView.findViewById(R.id.parent_title);
                groupViewHolder.count = (TextView) convertView.findViewById(R.id.parent_count);

                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }

            final PItem item = (PItem) getGroup(groupPosition);
            groupViewHolder.title.setText(NameConstants.getName(item.name));
            groupViewHolder.count.setText("共" + item.total_quesstion_num
                    + "道/答对" + item.correct_num + "道");

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    public static void launch(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, ReportActivity.class);
        context.startActivity(intent);

    }
}
