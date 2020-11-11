package com.example.mvvm_simple.view.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mvvm_simple.R
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia

/**
 * @author DuLong
 * @since 2020/4/20
 * email 798382030@qq.com
 */
class IdleGoodsAdapter: RecyclerView.Adapter<IdleGoodsAdapter.IdleGoodsViewHolder>() {

    companion object {
        const val TYPE_ADD_PIC = 1
        const val TYPE_PICTURE = 2
    }

    /**
     * 监听事件
     */
    private var mOnAddPicClickListener: OnAddPicClickListener? = null
    private var mItemClickListener: OnItemClickListener? = null
    private var mItemLongClickListener: OnItemLongClickListener? = null
    private var data: MutableList<LocalMedia> = ArrayList()

    //最大图片数
    private var selectMax = 9

    /**
     * 删除
     */
    fun delete(position: Int) {
        try {
            if (position != RecyclerView.NO_POSITION && data.size > position) {
                data.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, data.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getData(): MutableList<LocalMedia> {
        return if(data == null) ArrayList<LocalMedia>() else data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdleGoodsViewHolder {
        return IdleGoodsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_idle_photo, parent, false))
    }

    override fun getItemCount(): Int {
        return if (hasExtraRow()) data.size + 1 else data.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: IdleGoodsViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ADD_PIC) {
            holder.delImg.visibility = View.GONE
            holder.img.setOnClickListener(View.OnClickListener {
                mOnAddPicClickListener?.onAddPic()
            })
        } else {
            holder.delImg.visibility = View.VISIBLE
            holder.delImg.setOnClickListener(View.OnClickListener {
                val index: Int = holder.getAdapterPosition()
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                if (index != RecyclerView.NO_POSITION && data.size > index) {
                    data.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, data.size)
                }
            })
            val data = data[position]
            if (data == null || TextUtils.isEmpty(data.path)) {
                return
            }
            val chooseModel: Int = data.chooseModel
            val path: String
            path = if (data.isCut && !data.isCompressed) {
                // 裁剪过
                data.cutPath
            } else if (data.isCompressed || data.isCut && data.isCompressed) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                data.compressPath
            } else {
                // 原图
                data.path
            }

            /**
             * todo:: 后续扩展视频等
             */
            if (chooseModel == PictureMimeType.ofImage()) {
                Glide.with(holder.itemView.context)
                        .load(if (PictureMimeType.isContent(path) && !data.isCut && !data.isCompressed) Uri.parse(path) else path)
                        .apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_placehodler).diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(holder.img)
            }

            /**
             * 设置监听事件
             */
            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener {
                    mItemClickListener!!.onItemClick(it, holder.adapterPosition)
                }
            }

            if (mItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener {
                    mItemLongClickListener!!.onItemLongClick(holder, holder.adapterPosition, it)
                    true
                }
            }
        }
    }


    private fun hasExtraRow(): Boolean {
        return data.size < selectMax
    }

    class IdleGoodsViewHolder(val item: View): RecyclerView.ViewHolder(item){

        var img: ImageView = item.findViewById(R.id.img)
        var delImg: ImageView = item.findViewById(R.id.img_delete)

    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_ADD_PIC
        } else {
            TYPE_PICTURE
        }
    }

    fun setOnAddPicClickListener(listener: OnAddPicClickListener) {
        this.mOnAddPicClickListener = listener;
    }

    fun setOnItemClickListener(l: OnItemClickListener?) {
        mItemClickListener = l
    }

    fun setItemLongClickListener(l: OnItemLongClickListener) {
        mItemLongClickListener = l
    }

    fun remove(position: Int) {
        if (data != null) {
            data.removeAt(position)
        }
    }

    /**
     * 设置最大选择图片数量
     */
    fun setSelectMax(num: Int) {
        this.selectMax = num;
    }

    /**
     * 设置数据
     */
    fun setList(data: ArrayList<LocalMedia>) {
        this.data = data
    }

    interface OnAddPicClickListener {
        fun onAddPic()
    }
    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }
    interface OnItemLongClickListener {
        fun onItemLongClick(holder: RecyclerView.ViewHolder, position: Int, v: View)
    }
}