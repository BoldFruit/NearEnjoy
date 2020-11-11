package com.example.mvvm_simple.goods_detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.lib_common.util.DensityUtil
import com.example.mvvm_simple.R
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

/**
 * Time:2020/4/18 16:52
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 要着重注意图片数量为8或者9的时候
 * 由于瀑布流的原因，导致了图片排序并不是正常的顺序
 * 类似于Grid的部分，如数量小于8时，item的顺序是从左到右数
 * 然而数量为8或者9时，排版变成了S型，这时候就要进行相应的
 * 调整
 * 然而，无论是哪种顺序，最后那个1：1的图片都应该出现在第二个
 * 2：3图片的下方，所以为了解决这个问题，我们在第二个2：3图片
 * 的下方添加一个空的layout，虽然有东西，但是我们点不到也看不
 * 到它，成功占位
 *
 * 另外一个问题就是左右间距的问题，我们需要判断图片的位置，然后
 * 再决定是在其左边还是右边添加padding(为啥用padding?因为margin
 * 在recyclerview的item之间不起作用）
 */
class DetailImagesAdapter(var dataList: ArrayList<String>): RecyclerView.Adapter<DetailImagesAdapter.DetailImageViewHolder>() {

    //重新构造对象，防止对源对象的破坏
    private var imagesList = ArrayList<String>()
    interface DetailImageItemClickListener {
        fun onClick(position: Int, url: String, imageView: ImageView)
    }
    private var listener: DetailImageItemClickListener? = null

    init {
        imagesList.addAll(dataList)
        if (imagesList.size > 9) {
            throw IllegalArgumentException("the number of pictures shouldn't exceed 10")
        }
        if (imagesList.size == 8 || imagesList.size == 9) {
            imagesList.add(imagesList.size - 1, "")
        }
    }

    fun setClickListener(listener: DetailImageItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        if (imagesList.size == 0) {
            throw NullPointerException("图片数据不能为空！")
        }
        return imagesList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImagesAdapter.DetailImageViewHolder {
        return DetailImageViewHolder(getLayoutView(viewType, parent), itemCount)
    }

    override fun onBindViewHolder(holder: DetailImagesAdapter.DetailImageViewHolder, position: Int) {
        holder.bindView(position, imagesList[position])
        holder.itemView.setOnClickListener {
            var realPosition = position
            if (position == dataList.size) {
                realPosition = position - 1
            }
            listener?.onClick(realPosition, imagesList[realPosition], holder.imageView)
        }
    }

    private fun getLayoutView(type: Int, parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(getLayout(type), parent, false)
    }

    private fun getLayout(position: Int): Int {
       return when(itemCount) {
            1 -> {
                R.layout.layout_detail_img_3_4
            }

            2 -> {
                when(position) {
                    0 -> R.layout.layout_detail_img_4_3
                    else -> R.layout.layout_detail_img_3_4
                }
            }

            3 -> {
                when(position) {
                    0,1 -> {
                        R.layout.layout_detail_img_4_3
                    }
                    else -> {
                        R.layout.layout_detail_img_3_4
                    }
                }
            }

            4 -> {
                R.layout.layout_detail_img_4_3
            }

            5, 7 -> {
                when(position) {
                    0, 1, 2 -> {
                       R.layout.layout_detail_img_4_3
                    }
                    else -> {
                        R.layout.layout_detail_img_1_1
                    }
                }
            }
            6 -> {
                when(position) {
                    0, 1, 2, 3 -> {
                        R.layout.layout_detail_img_4_3
                    }
                    else -> {
                        R.layout.layout_detail_img_1_1
                    }
                }
            }
            8+1 -> {
                when(position) {
                    0, 1, 2 -> {
                        R.layout.layout_detail_img_4_3
                    }
                    3 -> {
                        R.layout.layout_detail_img_2_3_double_padding
                    }
                    6 -> {
                        R.layout.layout_detail_img_2_3
                    }
                    7 -> {
                        R.layout.layout_paging_null_footer
                    }
                    else -> {
                        R.layout.layout_detail_img_1_1
                    }
                }
            }
            //10 = 9 + 1
            else -> {
                when(position) {
                    0, 1, 2, 3 -> {
                        R.layout.layout_detail_img_4_3
                    }

                    4 -> {
                        R.layout.layout_detail_img_2_3_double_padding
                    }

                    7 -> {
                        R.layout.layout_detail_img_2_3
                    }

                    8 -> {
                        R.layout.layout_paging_null_footer
                    }

                    else -> {
                        R.layout.layout_detail_img_1_1
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class DetailImageViewHolder(var view: View, var itemCount: Int): RecyclerView.ViewHolder(view) {
        lateinit var imageView: ImageView

        enum class ProportionStyle {
            //宽高比
            STYLE_1_1,

            STYLE_3_4,

            STYLE_4_3,

            STYLE_2_3,

            STYLE_2_3_PLUS
        }

        enum class PositionStyle {
            LEFT,

            RIGHT,

            NONE
        }

        /**
         * 根据图片数量以及图片位置，选择不同的方案
         */
        fun bindView(position: Int, url: String) {
            when(itemCount) {
                1 -> {
                   setProportionImg(url, true, ProportionStyle.STYLE_3_4)
                }

                2 -> {
                    when(position) {
                        0 -> setProportionImg(url, true, ProportionStyle.STYLE_4_3)
                        else -> setProportionImg(url, true, ProportionStyle.STYLE_3_4)
                    }
                }

                3 -> {
                    when(position) {
                        0,1 -> {
                            setProportionImg(url, true, ProportionStyle.STYLE_4_3)
                        }
                        else -> {
                            setProportionImg(url, true, ProportionStyle.STYLE_3_4)
                        }
                    }
                }

                4 -> {
                    setProportionImg(url, true, ProportionStyle.STYLE_4_3)
                }

                5, 7 -> {
                    when(position) {
                        0, 1, 2 -> {
                            setProportionImg(url, true, ProportionStyle.STYLE_4_3)
                        }
                        else -> {
                            setProportionImg(url, false, ProportionStyle.STYLE_1_1, true,
                                    positionStyle = if (position % 2 == 0) PositionStyle.RIGHT else PositionStyle.LEFT)
                        }
                    }
                }
                6 -> {
                    when(position) {
                        0, 1, 2, 3 -> {
                            setProportionImg(url, true, ProportionStyle.STYLE_4_3)
                        }
                        else -> {
                            setProportionImg(url, false, ProportionStyle.STYLE_1_1, true,
                                    positionStyle = if (position % 2 == 0) PositionStyle.LEFT else PositionStyle.RIGHT)
                        }
                    }
                }
                8+1 -> {
                    when(position) {
                        0, 1, 2 -> {
                            setProportionImg(url, true, ProportionStyle.STYLE_4_3)
                        }
                        3 -> {
                            setProportionImg(url, false, ProportionStyle.STYLE_2_3_PLUS, true, PositionStyle.LEFT)
                        }

                        6 -> {
                            setProportionImg(url, false, ProportionStyle.STYLE_2_3, true, PositionStyle.LEFT)
                        }

                        4, 5, 8 -> {
                            setProportionImg(url, false, ProportionStyle.STYLE_1_1, true, PositionStyle.RIGHT)
                        }
                    }
                }
                //10
                else -> {
                    when(position) {
                        0, 1, 2, 3 -> {
                            setProportionImg(url, true, ProportionStyle.STYLE_4_3)
                        }

                        4 -> {
                            setProportionImg(url, false, ProportionStyle.STYLE_2_3_PLUS, true, PositionStyle.LEFT)
                        }

                        7 -> {
                            setProportionImg(url, false, ProportionStyle.STYLE_2_3, true, PositionStyle.LEFT)
                        }

                        5, 6, 9 -> {
                            setProportionImg(url, false, ProportionStyle.STYLE_1_1, true, PositionStyle.RIGHT)
                        }
                    }
                }
            }
        }


        private fun setProportionImg(url: String, isFullSpan: Boolean, style: ProportionStyle) {
            setProportionImg(url, isFullSpan, style, false, PositionStyle.NONE)
        }

        /**
         * 设置span，padding分割，显示图片
         */
        private fun setProportionImg(url: String, isFullSpan: Boolean, style: ProportionStyle, isSetPadding: Boolean, positionStyle: PositionStyle) {
           if (isFullSpan) {
               if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                   (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
               } else {
                   throw IllegalArgumentException("wrong layout manager!!!")
               }
           }
            if (isSetPadding) {
                when(positionStyle) {
                    PositionStyle.LEFT -> itemView.setPadding(
                            itemView.paddingLeft,
                            itemView.paddingTop,
                            DensityUtil.dip2px(4f, itemView.context),
                            itemView.paddingBottom
                    )
                    else -> itemView.setPadding(
                            DensityUtil.dip2px(4f, itemView.context),
                            itemView.paddingTop,
                            itemView.paddingRight,
                            itemView.paddingBottom
                    )
                }
            }
            imageView = view.findViewById(getProportionView(style))
            Glide.with(itemView.context)
                    .load(url)
                    .into(imageView)
        }

        /**
         * 根据比例类型返回不同类型的Image
         */
        private fun getProportionView(style: ProportionStyle): Int {
            return when(style) {
                ProportionStyle.STYLE_1_1 -> R.id.goods_detail_1_1_img
                ProportionStyle.STYLE_3_4 -> R.id.goods_detail_3_4_img
                ProportionStyle.STYLE_4_3 -> R.id.goods_detail_4_3_img
                ProportionStyle.STYLE_2_3 -> R.id.goods_detail_2_3_img
                else -> R.id.goods_detail_2_3_double_img
            }
        }

    }

}