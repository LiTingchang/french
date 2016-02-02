package com.snail.french.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.snail.french.R;
import com.snail.french.model.video.CourseItem;
import com.snail.french.model.video.CourseResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 16-2-2.
 */
public abstract class BaseCourseFragment extends Fragment {


    @Bind(R.id.course_list_view)
    ListView courseListView;

    private CourseAdapter adapter;

    private List<CourseItem> courseItemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_course_base, container, false);
        ButterKnife.bind(this, view);

        requestData(getRequestAction());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getType());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getType());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    abstract String getType();

    abstract String getRequestAction();

    void requestData(String action) {
        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(getActivity()))
                .get(action, null, new TypeReference<CourseResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<CourseResponse>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(CourseResponse response) {
                                setData(response);
                            }

                            @Override
                            public void onFailure(String message) {

                            }

                            @Override
                            public void onFinish() {

                            }
                        });
    }

    void setData(CourseResponse courseResponse) {
        if(!isAdded()) {
            return;
        }

        if(courseResponse == null || courseResponse.course_list == null) {
            return;
        }

        courseItemList = courseResponse.course_list;

        if(adapter == null) {
            adapter = new CourseAdapter(getContext(), courseItemList);
            courseListView.setAdapter(adapter);

            courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    CourseItem courseItem = courseItemList.get(i);

                    // TODO　跳转
                }
            });

        } else {
            adapter.notifyDataSetChanged();
        }
    }


    private static class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView price;
    }

    class CourseAdapter extends BaseAdapter {

        private Context context;
        private List<CourseItem> courseItemList;

        public CourseAdapter(Context context, List<CourseItem> courseItemList) {
            this.context = context;
            this.courseItemList = courseItemList;
        }

        @Override
        public int getCount() {
            return courseItemList == null ? 0 : courseItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return courseItemList == null ? null : courseItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.course_list_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.course_image);
                holder.title = (TextView) convertView.findViewById(R.id.course_title);
                holder.price = (TextView) convertView.findViewById(R.id.course_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final CourseItem courseItem = courseItemList.get(position);

            ImageLoader.getInstance().displayImage(courseItem.thumb_url, holder.imageView);
            holder.title.setText(courseItem.name);
            holder.price.setText(courseItem.price);

            return convertView;
        }
    }
}
