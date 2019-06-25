package com.winning.djj.expandablelistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * 作者|时间: djj on 2019/6/25 22:16
 * 博客地址: http://www.jianshu.com/u/dfbde65a03fc
 */
public class MainActivity extends AppCompatActivity {

    private List<Map<String, String>> parentList = new ArrayList<Map<String, String>>();
    private List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
    private ExpandableListView exListView;
    private Context context = this;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        exListView = (ExpandableListView) findViewById(R.id.expandable_list);
    }

    // 初始化数据
    private void initData() {
        for (int i = 0; i < 10; i++) {
            Map<String, String> groupMap = new HashMap<String, String>();
            groupMap.put("groupText", "item" + i);
            groupMap.put("isGroupCheckd", "No");
            parentList.add(groupMap);
        }
        for (int i = 0; i < 10; i++) {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            for (int j = 0; j < 4; j++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("childItem", "childItem" + j);
                map.put("isChecked", "No");
                list.add(map);
            }
            childData.add(list);
        }
        exListView.setGroupIndicator(null);
        adapter = new MyAdapter();
        exListView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            exListView.expandGroup(i);
        }
        //exListView.expandGroup(0);
        hashSet = new HashSet<String>();
    }

    /**
     * 记录正在选中的子listview的item条目 用hashset是为了去除重复值
     */
    private HashSet<String> hashSet;

    private void setListener() {
        exListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //存取已选定的集合
                hashSet = new HashSet<String>();

            }
        });
        // ExpandableListView的child的点击事件
        exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Map<String, String> map = childData.get(groupPosition).get(childPosition);
                String childChecked = map.get("isChecked");
                if ("No".equals(childChecked)) {
                    map.put("isChecked", "Yes");
                    hashSet.add("选定" + childPosition);
                } else {
                    map.put("isChecked", "No");
                    if (hashSet.contains("选定" + childPosition)) {
                        hashSet.remove("选定" + childPosition);
                    }
                }
                System.out.println("选定的长度==1" + hashSet.size());
                System.out.println("选定的长度==2" + childData.get(groupPosition).size());
                if (hashSet.size() == childData.get(groupPosition).size()) {
                    parentList.get(groupPosition).put("isGroupCheckd", "Yes");
                } else {
                    parentList.get(groupPosition).put("isGroupCheckd", "No");
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }


    /**
     * 适配adapter
     */

    private class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return parentList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            return childData.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return parentList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childData.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.expandable_child_item, null);
                holder.childText = (TextView) convertView.findViewById(R.id.tv_name);
                holder.childBox = (CheckBox) convertView.findViewById(R.id.cb_child);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.childText.setText(childData.get(groupPosition)
                    .get(childPosition).get("childItem"));
            String isChecked = childData.get(groupPosition).get(childPosition)
                    .get("isChecked");
            if ("No".equals(isChecked)) {
                holder.childBox.setChecked(false);
            } else {
                holder.childBox.setChecked(true);
            }
            return convertView;
        }

        @Override
        public View getGroupView(final int groupPosition,
                                 final boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.expandable_groups, null);
                holder.groupText = (TextView) convertView.findViewById(R.id.tv_group);
                holder.groupBox = (CheckBox) convertView.findViewById(R.id.cb_group);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.groupText.setText(parentList.get(groupPosition).get(
                    "groupText"));
            final String isGroupCheckd = parentList.get(groupPosition).get(
                    "isGroupCheckd");

            if ("No".equals(isGroupCheckd)) {
                holder.groupBox.setChecked(false);
            } else {
                holder.groupBox.setChecked(true);
            }

      /*
       * groupListView的点击事件
       */
            holder.groupBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox groupBox = (CheckBox) v.findViewById(R.id.cb_group);
                    if (!isExpanded) {
                        //展开某个group view
                        exListView.expandGroup(groupPosition);
                    } else {
                        //关闭某个group view
                        exListView.collapseGroup(groupPosition);
                    }

                    if ("No".equals(isGroupCheckd)) {
                        exListView.expandGroup(groupPosition);
                        groupBox.setChecked(true);
                        parentList.get(groupPosition).put("isGroupCheckd",
                                "Yes");
                        List<Map<String, String>> list = childData
                                .get(groupPosition);
                        for (Map<String, String> map : list) {
                            map.put("isChecked", "Yes");
                        }
                    } else {
                        groupBox.setChecked(false);
                        parentList.get(groupPosition)
                                .put("isGroupCheckd", "No");
                        List<Map<String, String>> list = childData
                                .get(groupPosition);
                        for (Map<String, String> map : list) {
                            map.put("isChecked", "No");
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    private class ViewHolder {
        TextView groupText, childText;
        CheckBox groupBox, childBox;
    }


//    private final static String TAG = "MainActivity";
//    /**
//     * 定义组数据
//     */
//    private List<String> groupDataList;
//    /**
//     * 定义组中的子数据
//     */
//    private List<List<String>> childDataList;
//
//    private ExpandableListView expandableListView;
//    private ExpandableAdapter adapter;
//
//    /**
//     * 0：不可选 1：未选中 2：半选 3：选中
//     */
//    private List<Map<Integer, Integer>> isSelectedList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        loadData();
//        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);
//
//        expandableListView.setGroupIndicator(null);
//        adapter = new ExpandableAdapter();
//        expandableListView.setAdapter(adapter);
//        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//                    @Override
//                    public void onGroupExpand(int groupPosition) {
//                        // 只展开一个
//                        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
//                            if (groupPosition != i) {
//                                expandableListView.collapseGroup(i);
//                            }
//                        }
//                    }
//                });
//
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//                    @Override
//                    public boolean onChildClick(ExpandableListView parent,
//                                                View v, int groupPosition, int childPosition,
//                                                long id) {
//                        Toast.makeText(
//                                MainActivity.this,
//                                "onChildClick===="
//                                        + adapter.getChild(groupPosition,
//                                        childPosition) + "===="
//                                        + v.getParent(), Toast.LENGTH_LONG).show();
//                        ViewHolder viewHolder = (ViewHolder) v.getTag();
//                        viewHolder.childBox.toggle();
//                        if (viewHolder.childBox.isChecked()) {
//                            isSelectedList.get(groupPosition).put(childPosition, 3);
//                        } else {
//                            isSelectedList.get(groupPosition).put(childPosition, 1);
//                        }
//                        int count = 0;
//                        for (int i = 0, size = isSelectedList
//                                .get(groupPosition).size(); i < size; i++) {
//                            if (isSelectedList.get(groupPosition).get(i) == 3) {
//                                count++;
//                            }
//                        }
//                        View view = (View) v.getParent();
//                        Log.d(TAG, "view=" + view.findViewById(R.id.cb_child));
//                        CheckBox ck = (CheckBox) view.findViewById(R.id.cb_child);
//                        if (count == isSelectedList.get(groupPosition).size()) {
//                            // ck.setBackgroundResource(R.drawable.btn_select);
//                            ck.setButtonDrawable(R.drawable.btn_select);
//                        } else if (count > 0) {
//                            // ck.setBackgroundResource(R.drawable.btn_half);
//                            ck.setButtonDrawable(R.drawable.btn_half);
//                        } else {
//                            // ck.setBackgroundResource(R.drawable.btn_unselect);
//                            ck.setButtonDrawable(R.drawable.btn_unselect);
//                        }
//                        adapter.notifyDataSetChanged();
//                        return false;
//                    }
//                });
//
//        isSelectedList = new ArrayList<Map<Integer, Integer>>();
//        for (int i = 0, icount = expandableListView.getCount(); i < icount; i++) {
//            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//            for (int j = 0, jcount = childDataList.get(i).size(); j < jcount; j++) {
//                map.put(j, 1);
//            }
//            isSelectedList.add(map);
//        }
//    }
//
//    /**
//     * 全部展开或关闭
//     *
//     * @param flag
//     */
//    private void showOrhide(boolean flag) {
//        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
//            if (flag) {
//                expandableListView.expandGroup(i);
//            } else {
//                expandableListView.collapseGroup(i);
//            }
//        }
//    }
//
//    private void loadData() {
//        groupDataList = new ArrayList<String>();
//        groupDataList.add("国家");
//        groupDataList.add("人物");
//        groupDataList.add("武器");
//
//        childDataList = new ArrayList<List<String>>();
//
//        List<String> child1 = new ArrayList<String>();
//        child1.add("魏国");
//        child1.add("蜀国");
//        child1.add("吴国");
//        childDataList.add(child1);
//
//        List<String> child2 = new ArrayList<String>();
//        child2.add("关羽");
//        child2.add("张飞");
//        child2.add("典韦");
//        child2.add("吕布");
//        child2.add("曹操");
//        child2.add("甘宁");
//        child2.add("郭嘉");
//        child2.add("周瑜");
//        childDataList.add(child2);
//
//        List<String> child3 = new ArrayList<String>();
//        child3.add("青龙偃月刀");
//        child3.add("丈八蛇矛枪");
//        child3.add("青钢剑");
//        child3.add("麒麟弓");
//        child3.add("银月枪");
//        childDataList.add(child3);
//    }
//
//    private class ExpandableAdapter extends BaseExpandableListAdapter {
//
//        @Override
//        public int getGroupCount() {
//            return groupDataList.size();
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
//            return childDataList.get(groupPosition).size();
//        }
//
//        @Override
//        public Object getGroup(int groupPosition) {
//            return groupDataList.get(groupPosition);
//        }
//
//        @Override
//        public Object getChild(int groupPosition, int childPosition) {
//            return childDataList.get(groupPosition).get(childPosition);
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//
//            Log.d(TAG, "getGroupView");
//            ViewHolder viewHolder = null;
//            if (null == convertView) {
//                convertView = View.inflate(MainActivity.this, R.layout.expandable_groups, null);
//                viewHolder = new ViewHolder();
//                viewHolder.groupText = (TextView) convertView.findViewById(R.id.tv_group);
//                viewHolder.groupBox = (CheckBox) convertView.findViewById(R.id.cb_group);
//                //viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//
//            viewHolder.groupText.setText(groupDataList.get(groupPosition));
//            viewHolder.groupBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView,
//                                                     boolean isChecked) {
//                            int flag = 0;
//                            if (isChecked) {
//                                flag = 3;
//                            } else {
//                                flag = 1;
//                            }
//                            for (int i = 0, size = isSelectedList.get(
//                                    groupPosition).size(); i < size; i++) {
//                                isSelectedList.get(groupPosition).put(i, flag);
//                            }
//                            notifyDataSetChanged();
//                        }
//                    });
//            int size = 0;
//            for (int i = 0, count = isSelectedList.get(groupPosition).size(); i < count; i++) {
//                if (isSelectedList.get(groupPosition).get(i) == 3) {
//                    size++;
//                }
//            }
//            if (size == isSelectedList.get(groupPosition).size()) {
//                // viewHolder.checkBox
//                // .setBackgroundResource(R.drawable.btn_select);
//                viewHolder.groupBox.setButtonDrawable(R.drawable.btn_select);
//            } else if (size > 0) {
//                // viewHolder.checkBox.setBackgroundResource(R.drawable.btn_half);
//                viewHolder.groupBox.setButtonDrawable(R.drawable.btn_half);
//            } else {
//                // viewHolder.checkBox
//                // .setBackgroundResource(R.drawable.btn_unselect);
//                viewHolder.groupBox.setButtonDrawable(R.drawable.btn_unselect);
//            }
//
//            // 判断isExpanded就可以控制是按下还是关闭，同时更换图片
////            if (isExpanded) {
////                viewHolder.imageView.setBackgroundResource(R.drawable.up);
////            } else {
////                viewHolder.imageView.setBackgroundResource(R.drawable.down);
////            }
//
//            return convertView;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, int childPosition,
//                                 boolean isLastChild, View convertView, ViewGroup parent) {
//
//            Log.d(TAG, "getChildView");
//            // TextView textView = null;
//            // if (null != convertView) {
//            // textView = (TextView) convertView;
//            // textView.setText(childDataList.get(groupPosition).get(
//            // childPosition));
//            // } else {
//            // textView = createView(childDataList.get(groupPosition).get(
//            // childPosition));
//            // }
//            // return textView;
//
//            ViewHolder viewHolder = null;
//            if (null == convertView) {
//                convertView = View.inflate(MainActivity.this, R.layout.expandable_child_item, null);
//                viewHolder = new ViewHolder();
//                viewHolder.childText = (TextView) convertView.findViewById(R.id.tv_name);
//                viewHolder.childBox = (CheckBox) convertView.findViewById(R.id.cb_child);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//
//            if (isSelectedList.get(groupPosition).get(childPosition) == 3) {
//                // viewHolder.checkBox
//                // .setBackgroundResource(R.drawable.btn_select);
//                // 更改CheckBox的外观
//                viewHolder.childBox.setButtonDrawable(R.drawable.btn_select);
//            } else {
//                // viewHolder.checkBox
//                // .setBackgroundResource(R.drawable.btn_unselect);
//                viewHolder.childBox.setButtonDrawable(R.drawable.btn_unselect);
//            }
//            viewHolder.childText.setText(childDataList.get(groupPosition).get(childPosition));
//            return convertView;
//        }
//
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return true;
//        }
//
//    }
//
//    private class ViewHolder {
//        TextView groupText, childText;
//        CheckBox groupBox, childBox;
//    }
}
