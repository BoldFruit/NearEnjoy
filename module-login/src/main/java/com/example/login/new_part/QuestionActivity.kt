package com.example.login.new_part

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.base.utils.ToastUtil
import com.example.base.view.activity.MvvmNetworkActivity
import com.example.lib_common.button.RelativeButton
import com.example.lib_common.linkage.multi_link.WheelView
import com.example.lib_common.linkage.multi_link.thr_link.LinkSchoolDialog
import com.example.lib_common.linkage.multi_link.thr_link.SchoolData
import com.example.login.R
import com.example.login.databinding.QuestionActivityQuestionBinding
import kotlin.collections.ArrayList

class QuestionActivity : MvvmNetworkActivity<QuestionActivityQuestionBinding, QuestionViewModel>() {
    override fun getLayoutId(): Int {
       return R.layout.question_activity_question
    }

    override fun getViewModel(): Class<out ViewModel> {
       return QuestionViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun initParameters() {

    }

    private fun setMainVisibility(isVisible: Boolean) {
        mViewDataBinding.questionMainPart.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setAnimationVisibility(isVisible: Boolean) {
        mViewDataBinding.questionAnimationView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    override fun initDataAndView() {

        mViewModel.getSchools()

        mViewModel.schools.observe(this, Observer {
//            var list = ArrayList<SchoolData>()
//            list.addAll(it)
//            var qin_city2 = SchoolData.CityListBean()
//            qin_city2.name = "唐山市"
//            qin_city2.id = 22
//            var qin_school = SchoolData.CityListBean.SchoolListBean()
//            qin_school.name = "唐山铁道学院"
//            qin_school.id = 89
//            var qin_school_list = ArrayList<SchoolData.CityListBean.SchoolListBean>()
//            qin_school_list.add(qin_school)
//            qin_city2.schoolList = qin_school_list
//            list[0].cityList.add(qin_city2)
//            var data1 = SchoolData()
//            data1.name = "山东省"
//            data1.id = 128
//            var sd_city1 = SchoolData.CityListBean()
//            sd_city1.name = "菏泽市"
//            sd_city1.id = 79
//            var sd_school = SchoolData.CityListBean.SchoolListBean()
//            sd_school.name = "菏泽学院"
//            sd_school.id = 83
//            var sd_school2 = SchoolData.CityListBean.SchoolListBean()
//            sd_school2.name = "曹州学院"
//            sd_school2.id = 111
//            var sd_school_list = ArrayList<SchoolData.CityListBean.SchoolListBean>()
//            sd_school_list.add(sd_school)
//            sd_school_list.add(sd_school2)

//            var sd_city_list = ArrayList<SchoolData.CityListBean>()
//            sd_city1.schoolList = sd_school_list
//            sd_city_list.add(sd_city1)
//            data1.cityList = sd_city_list
//            list.add(data1)


            val dialog = LinkSchoolDialog(this, it)
            dialog.show()
        })

        mViewModel.isLoading.observe(this, Observer {
            when(it) {
                QuestionViewModel.LoadingType.LOADING -> {
                    //todo:删除错误页面
                    setMainVisibility(false)
                    setAnimationVisibility(true)
                    mViewDataBinding.questionAnimationView.playAnimation()
                }
                QuestionViewModel.LoadingType.DONE -> {
                    //todo:删除错误页面
                    mViewDataBinding.questionRlBtn.setType(RelativeButton.BtnType.ENABLE)
                    mViewDataBinding.questionTxtError.visibility = View.GONE
                    mViewDataBinding.questionAnimationView.cancelAnimation()
                    setMainVisibility(true)
                    setAnimationVisibility(false)
                }
                else -> {
                    //todo:显示错误页面
                }
            }
        })

        //获得题目
        mViewModel.getQuestions()

        mViewModel.result.observe(this, Observer {
            clearQuestion(true)
            val list: ArrayList<QuestionBean> = ArrayList()
            list.add(it[0])
            list.add(it[0])
            list.add(it[0])
            buildQuestion(list)
        })

        mViewDataBinding.questionRlBtn.setType(RelativeButton.BtnType.ENABLE)
        mViewDataBinding.questionRlBtn.setOnClickListener {
            if (!(mViewDataBinding.questionFirstQuestionsGroup.isAllSelected
                            && mViewDataBinding.questionSecondQuestionsGroup.isAllSelected
                            && mViewDataBinding.questionThirdQuestionsGroup.isAllSelected)) {
                ToastUtil.show(this, "填完再提交哦（。＾▽＾）")
                return@setOnClickListener
            }
            if(it is RelativeButton) {

                when(it.currentType) {
                    RelativeButton.BtnType.ENABLE -> {
                        if (mViewDataBinding.questionFirstQuestionsGroup.answerResult && mViewDataBinding.questionSecondQuestionsGroup.answerResult && mViewDataBinding.questionThirdQuestionsGroup.answerResult) {
                            //todo：登陆注册
                            ToastUtil.show(this, "答案正确")
                            //todo: 目前还没写登陆页面，所以验证答案成功后，依然还在这个页面，所以不能清除内容
                            //todo: 等到写好了，改成true
                            clearQuestion(false)
                        } else {
                            mViewDataBinding.questionRlBtn.setType(RelativeButton.BtnType.ERROR)
                            mViewDataBinding.questionTxtError.visibility = View.VISIBLE
                        }
                    }

                    RelativeButton.BtnType.ERROR -> {
                        mViewDataBinding.questionRlBtn.setType(RelativeButton.BtnType.PROGRESSING)
                        mViewModel.getQuestions()
                    }
                    else -> {

                    }
                }
            }

        }

    }

    /**
     * @param isClearContent Boolean
     */
    private fun clearQuestion(isClearContent: Boolean) {
        mViewDataBinding.questionFirstQuestionsGroup.clearQuestions(isClearContent)
        mViewDataBinding.questionSecondQuestionsGroup.clearQuestions(isClearContent)
        mViewDataBinding.questionThirdQuestionsGroup.clearQuestions(isClearContent)
    }

    private fun buildQuestion(list: List<QuestionBean>) {
        val tempList = ArrayList<Int>()
        tempList.add(0)
        tempList.add(1)
        tempList.add(2)
        tempList.add(3)

        mViewDataBinding.questionFirstQuestionsGroup.setQuestionTitle("Q1:"+list[0].question)
        mViewDataBinding.questionSecondQuestionsGroup.setQuestionTitle("Q2:"+list[1].question)
        mViewDataBinding.questionThirdQuestionsGroup.setQuestionTitle("Q3:"+list[2].question)
        val splits = list[0].option.split("#")
        tempList.shuffle()
        for (i in tempList) {
            mViewDataBinding.questionFirstQuestionsGroup
                    .addQuestion(splits[i], i == 0)
        }
        mViewDataBinding.questionFirstQuestionsGroup.buildUp()
        tempList.shuffle()
        for (i in tempList) {
            mViewDataBinding.questionSecondQuestionsGroup
                    .addQuestion(splits[i], i == 0)
        }
        mViewDataBinding.questionSecondQuestionsGroup.buildUp()
        tempList.shuffle()
        for (i in tempList) {
            mViewDataBinding.questionThirdQuestionsGroup
                    .addQuestion(splits[i], i == 0)
        }
        mViewDataBinding.questionThirdQuestionsGroup.buildUp()
    }


}
