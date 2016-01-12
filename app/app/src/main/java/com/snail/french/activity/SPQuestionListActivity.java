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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.constant.FrenchKind;
import com.snail.french.constant.NameConstants;
import com.snail.french.constant.QuestionListConfig;
import com.snail.french.manager.ExerciseManager;
import com.snail.french.manager.StatusResponseManager;
import com.snail.french.model.spquestion.SPPQuestion;
import com.snail.french.model.spquestion.SPQuestion;
import com.snail.french.model.status.PItem;
import com.snail.french.model.status.Status;
import com.snail.french.model.status.StatusResponse;
import com.snail.french.utils.JsonParseUtil;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class SPQuestionListActivity extends BaseActivity {

    @Bind(R.id.list_view)
    ExpandableListView listView;
    @Bind(R.id.titlebar)
    CommonTitle titlebar;

    private ReportAdapter mReportAdapter;

    List<SPPQuestion> questionList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        questionList = JSON.parseArray(QuestionListConfig.ERROR_TCF, SPPQuestion.class);

        titlebar.setTitleText(ExerciseManager.getInstance().getFrenchKind().getName());

        listView.setGroupIndicator(null);

        listView.setAdapter(new ReportAdapter(this, questionList));

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int groupPosition, int childPosition, long id) {

                SPQuestion spQuestion = questionList.get(groupPosition).child.get(childPosition);
                TestActivity.launch(SPQuestionListActivity.this, spQuestion.type, spQuestion.title);
                return false;
            }
        });
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

        private List<SPPQuestion> questionList;
        private LayoutInflater inflater;

        public ReportAdapter(Context context, List<SPPQuestion> questionList) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.questionList = questionList;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return questionList.get(groupPosition).child.get(childPosition);
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

            final SPQuestion spQuestion = (SPPQuestion) getChild(groupPosition, childPosition);
            childViewHolder.title.setText(spQuestion.title);

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            // 专四 阅读 作文 不展开
//            if (ExerciseManager.getInstance().getFrenchKind() == FrenchKind.TEM4) {
//                String pName = questionList.get(groupPosition).title;
//                if ("R".equals(pName) || "C".equals(pName)) {
//                    return 0;
//                }
//            }

            return questionList.get(groupPosition).child == null ? 0 : questionList.get(groupPosition).child.size();
        }


        @Override
        public Object getGroup(int groupPosition) {
            return questionList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return questionList == null ? 0 :questionList.size();
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

            final SPPQuestion sppQuestion = (SPPQuestion) getGroup(groupPosition);
            groupViewHolder.title.setText(sppQuestion.title);
            groupViewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TestActivity.launch(SPQuestionListActivity.this, sppQuestion.type, sppQuestion.title);
                }
            });

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
        intent.setClass(context, SPQuestionListActivity.class);
        context.startActivity(intent);

    }
}
