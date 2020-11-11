package com.example.lib_common.checkpoint.single_check_point;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.lib_common.R;
import com.example.lib_common.checkpoint.base_single_check.BaseSingleCheckController;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Time:2020/5/6 13:49
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class SimpleFourCheck extends ConstraintLayout {

    private ConstraintLayout fourCheckView;
    private TextView questionTitle;
    private SingleCheckPoint pointFir;
    private SingleCheckPoint pointSec;
    private SingleCheckPoint pointThi;
    private SingleCheckPoint pointFou;
    private ArrayList<QuestionWrapper> questionWrappers = new ArrayList<>();
    private BaseSingleCheckController controller;

    public SimpleFourCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleFourCheck(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        controller = new BaseSingleCheckController();
        fourCheckView = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.common_checkpoint_layout_simple_four_items, this);
        questionTitle = fourCheckView.findViewById(R.id.common_check_point_tv_question_group_title);
        pointFir = fourCheckView.findViewById(R.id.common_check_point_scp_fir);
        pointSec = fourCheckView.findViewById(R.id.common_check_point_scp_sec);
        pointThi = fourCheckView.findViewById(R.id.common_check_point_scp_thi);
        pointFou = fourCheckView.findViewById(R.id.common_check_point_scp_fou);
    }

    public SimpleFourCheck addQuestion(String content, boolean isTrue) {
        if (questionWrappers.size() < 4) {
            questionWrappers.add(new QuestionWrapper(isTrue, content));
        } else {
            throw new IllegalArgumentException("the number of questions shouldn't exceed 4!");
        }
        return this;
    }


    public void buildUp() {
        if (questionWrappers.size() < 4) {
            throw new IllegalArgumentException("there are not enough questions to build the question group");
        }

        pointFir.setContent(questionWrappers.get(0).getContent());
        pointSec.setContent(questionWrappers.get(1).getContent());
        pointThi.setContent(questionWrappers.get(2).getContent());
        pointFou.setContent(questionWrappers.get(3).getContent());
        controller.addCheck(pointFir)
                .addCheck(pointSec)
                .addCheck(pointThi)
                .addCheck(pointFou);

    }

    /**
     * 获得该四个小题最终选项的结果
     * @return 结果是对还是错
     */
    public boolean getAnswerResult() {
        int temp = controller.getCurrentTag();
        if (temp == -1) {
            return false;
        }
        return questionWrappers.get(temp).isTrue;
    }

    /**
     * 获得完整的个人答案
     * @return 获得个人选中的未知，对错，以及题目内容
     */
    public QuestionWrapper getCompleteAnswerResult() {
        QuestionWrapper result = questionWrappers.get(controller.getCurrentTag());
        result.setPosition(controller.getCurrentTag());
        return result;
    }

    /**
     * 随便有一个选的就行
     * @return
     */
    public boolean isAllSelected() {
        return controller.getCurrentTag() != -1;
    }


    public SimpleFourCheck setQuestionTitle(String title) {
        if (questionTitle != null) {
            questionTitle.setText(title);
        }
        return this;
    }

    public void clearQuestions(boolean isClearContent) {
        if (isClearContent) {
            questionWrappers.clear();
        }
        controller.clearQuestion();
    }
    public static class QuestionWrapper {
        private boolean isTrue;
        private String content;
        private int position = -1;

        QuestionWrapper(boolean isTrue, String content) {
            this.isTrue = isTrue;
            this.content = content;
        }

        public boolean isTrue() {
            return isTrue;
        }

        public String getContent() {
            return content;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }
}
