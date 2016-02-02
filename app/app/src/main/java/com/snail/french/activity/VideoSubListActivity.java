package com.snail.french.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.model.video.CourseItem;
import com.snail.french.model.video.CourseResponse;
import com.snail.french.model.video.LessonItem;
import com.snail.french.model.video.LessonResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 16-2-1.
 */
public class VideoSubListActivity extends BaseActivity {

    private static final String EXT_COURSE_ID = "ext_course_id";

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.lesson_list_view)
    ListView lessonListView;

    private int courseId;

    private LessonAdapter adapter;

    private CourseItem courseItem;
    private List<LessonItem> lessonItems;

    private View headerView;
    private ImageView headerBg;
    private TextView headerPrice;
    private TextView headerBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        ButterKnife.bind(this);


        courseId = getIntent().getIntExtra(EXT_COURSE_ID, 0);

        if (courseId == 0) {
            ToastUtil.shortToast(this, "ID错误，加载失败");
            this.finish();
        }



        headerView = LayoutInflater.from(this).inflate(R.layout.lesson_header, null, false);
        headerPrice = (TextView) headerView.findViewById(R.id.lesson_header_price);
        headerBuy = (TextView) headerView.findViewById(R.id.lesson_header_buy);
        headerBg = (ImageView) headerView.findViewById(R.id.lesson_header_bg);

        lessonListView.addHeaderView(headerView);

        String action = "course/" + courseId + "/lesson/list";
        requestData(action);

    }


    void requestData(String action) {
        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(this))
                .get(action, null, new TypeReference<LessonResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<LessonResponse>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(LessonResponse response) {
                                if (response == null || response.lesson_list == null
                                        || response.course == null) {
                                    return;
                                }

                                courseItem = response.course;
                                lessonItems = response.lesson_list;

                                if (adapter == null) {
                                    adapter = new LessonAdapter(VideoSubListActivity.this,
                                            lessonItems, response.course.bought);
                                    lessonListView.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }

                                titlebar.setTitleText(courseItem.name);

                                ImageLoader.getInstance().displayImage(courseItem.thumb_url, headerBg);
                                headerPrice.setText(courseItem.price);
                                headerBuy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showBuyDialog();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(VideoSubListActivity.this, "网络请求失败，请退出重试");
                            }

                            @Override
                            public void onFinish() {

                            }
                        });
    }

    private void showBuyDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("请联系XXX购买开通");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private static class ViewHolder {
        ImageView play;
        TextView title;
        ImageView type;
    }

    class LessonAdapter extends BaseAdapter {

        private Context context;
        private List<LessonItem> lessonItemList;
        private boolean isBought;

        public LessonAdapter(Context context, List<LessonItem> lessonItemList, boolean isBought) {
            this.context = context;
            this.lessonItemList = lessonItemList;
            this.isBought = isBought;
        }

        @Override
        public int getCount() {
            return lessonItemList == null ? 0 : lessonItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return lessonItemList == null ? null : lessonItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.lesson_list_item, null);
                holder = new ViewHolder();
                holder.play = (ImageView) convertView.findViewById(R.id.lesson_image);
                holder.title = (TextView) convertView.findViewById(R.id.lesson_title);
                holder.type = (ImageView) convertView.findViewById(R.id.lesson_type);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final LessonItem lessonItem = lessonItemList.get(position);

            holder.title.setText(lessonItem.name);
            if (!isBought && !lessonItem.is_free) {
                holder.play.setImageResource(R.drawable.ic_lesson_locked);
                holder.type.setImageResource(R.drawable.ic_lesson_type_unable);
            } else {
                holder.play.setImageResource(R.drawable.ic_lesson_play);
                holder.type.setImageResource(R.drawable.ic_lesson_type_available);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isBought && !lessonItem.is_free) {
                        showBuyDialog();
                    } else {
                        ExoPlayerActivity.launch(VideoSubListActivity.this, lessonItem.video_url);
                    }
                }
            });


            return convertView;
        }
    }


    public static void launch(Context context, int id) {
        Intent intent = new Intent();
        intent.setClass(context, VideoSubListActivity.class);
        intent.putExtra(EXT_COURSE_ID, id);
        context.startActivity(intent);
    }
}
