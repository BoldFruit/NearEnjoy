package com.example.lib_common.linkage.multi_link;

import com.example.lib_common.linkage.multi_link.thr_link.ContentWrapper;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;

/**
 * Time:2020/5/17 15:56
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 多级联动
 */
public class MultiLinkController {
    private ArrayList<WheelView> linkedList;
    private WheelView.OnWheelViewListener listener;
    private LinkedChangeListener linkedChangeListener;

    public interface LinkedChangeListener {
        /**
         * 获得下一级改变后的数据
         * @param s:本级的级数
         * @param id:变动的级数的id
         * @return
         */
        List<String> getChangedResult(int s, int id);


        List<ContentWrapper> getChangedWrapperResult(int s, int id);

    }

    public MultiLinkController(LinkedChangeListener linkedChangeListener) {
        this.linkedChangeListener = linkedChangeListener;
        linkedList = new ArrayList<>();
        //这里，务必注意，十分危险，要头脑清晰，我反正是不能清楚地分析了
        //试错试出来的
        listener = (selectedIndex, item, s, id) -> {
            int tempId = id;
            int tempS = s;
            if (s < linkedList.size() - 1) {
                if (!(linkedChangeListener == null)) {
                    for (int i = s + 1; i <= linkedList.size() - 1; i++) {
                        List<Integer> ids = new ArrayList<>();
                        List<ContentWrapper> changedWrapperResult = linkedChangeListener.getChangedWrapperResult(tempS, tempId);
                        linkedList.get(i).setRealItems(changedWrapperResult);
                        for (ContentWrapper wrapper: changedWrapperResult) {
                            ids.add(wrapper.getId());
                        }
                        tempId = ids.get(0);
                        tempS++;
                    }
                }
            }
        };
    }



    /**
     * 双重绑定
     * @param wheelView
     */
    public void addWheelView(WheelView wheelView) {
        linkedList.add(wheelView);
        wheelView.setSeries(linkedList.size() - 1);
        wheelView.setOnWheelViewListener(listener);
    }
}
