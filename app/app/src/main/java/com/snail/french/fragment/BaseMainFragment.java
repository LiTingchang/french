package com.snail.french.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snail.french.FrenchApp;
import com.snail.french.R;
import com.snail.french.temp.DetailItem;
import com.snail.french.temp.DetailManager;
import com.snail.french.temp.OptionItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-11.
 */
public abstract class BaseMainFragment extends Fragment {


    @Bind(R.id.list_view)
    ExpandableListView listView;

    View headerView;

    List<DetailItem> detailItems;
    DetailAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, view);

        headerView = LayoutInflater.from(getContext()).inflate(R.layout.main_fragment_header, null, false);
        listView.addHeaderView(headerView);


        listView.setGroupIndicator(null);

        detailItems = new ArrayList<DetailItem>();

        for ( int i = 0; i < 20; i++ )
        {
            DetailItem detailItem = new DetailItem();
            detailItem.date = new Date();
            detailItem.optionItemList = DetailManager.getBodyOptionItem();

            detailItems.add(detailItem);
        }

        adapter = new DetailAdapter(getContext(), detailItems);

        listView.setAdapter(adapter);

//        for ( int i = 0; i < adapter.getGroupCount(); i++ )
//        {
//            listView.expandGroup(i);
//        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    abstract public void initData();


    private static class GroupViewHolder {
        TextView timeTextView;
    }

    private static class ChildViewHolder {
        TextView nameTextView;
        TextView valueTextView;
        TextView suggestTextView;
    }

    class DetailAdapter extends BaseExpandableListAdapter {

        private List<DetailItem> detailItems;
        private LayoutInflater inflater;

        public DetailAdapter(Context context, List<DetailItem> detailTtems) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.detailItems = detailTtems;

        }

        @Override
        public Object getChild( int groupPosition, int childPosition )
        {
            return detailItems.get(groupPosition).optionItemList.get(childPosition);
        }

        @Override
        public long getChildId( int groupPosition, int childPosition )
        {
            return childPosition;
        }

        @Override
        public View getChildView( int groupPosition, int childPosition, boolean isLastChild,
                                  View convertView, ViewGroup parent )
        {
            ChildViewHolder childViewHolder = null;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_child, parent, false);

                childViewHolder = new ChildViewHolder();

                childViewHolder.nameTextView= (TextView) convertView.findViewById(R.id.option_child_name);
                childViewHolder.valueTextView= (TextView) convertView.findViewById(R.id.option_child_value);
                childViewHolder.suggestTextView= (TextView) convertView.findViewById(R.id.option_child_suggest);

                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }

            final OptionItem optionItem = (OptionItem) getChild(groupPosition, childPosition);
            childViewHolder.nameTextView.setText(optionItem.name);
            // TODO string formate
            childViewHolder.valueTextView.setText(optionItem.value + optionItem.units);
            childViewHolder.suggestTextView.setText(optionItem.suggest);

            return convertView;
        }

        @Override
        public int getChildrenCount( int groupPosition )
        {
            return detailItems.get(groupPosition).optionItemList.size();
        }

        @Override
        public Object getGroup( int groupPosition )
        {
            return detailItems.get(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return detailItems.size();
        }

        @Override
        public long getGroupId( int groupPosition )
        {
            return groupPosition;
        }

        @Override
        public View getGroupView( int groupPosition, boolean isExpanded, View convertView,
                                  ViewGroup parent )
        {
            GroupViewHolder groupViewHolder = null;

            if(convertView == null) {
                groupViewHolder = new GroupViewHolder();

                convertView = inflater.inflate(R.layout.list_item_parent, parent, false);

                groupViewHolder.timeTextView = (TextView) convertView.findViewById(R.id.option_group_date);

                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }

            final Date date = ((DetailItem) getGroup(groupPosition)).date;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            groupViewHolder.timeTextView.setText(dateFormat.format(date));

            return convertView;
        }

        @Override
        public boolean hasStableIds()
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable( int groupPosition, int childPosition )
        {
            // TODO Auto-generated method stub
            return false;
        }

    }
}
