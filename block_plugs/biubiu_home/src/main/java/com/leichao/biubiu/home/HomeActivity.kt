package com.leichao.biubiu.home

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.leichao.common.BaseActivity
import com.leichao.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : BaseActivity() {

    private lateinit var mBeanList : ArrayList<Int>
    private lateinit var mAdapter : BlogListAdapter

    override fun initView() {
        setContentView(R.layout.activity_home)
        StatusBarUtil.setFullTranslucent(this)
    }

    override fun initData() {
        home_rv.layoutManager = GridLayoutManager(this, 4)
        home_rv.itemAnimator = DefaultItemAnimator()
        mBeanList = ArrayList()
        mAdapter = BlogListAdapter(this, mBeanList)
        home_rv.adapter = mAdapter

        for (i in 1..24) {
            if (i <= 7)
                mBeanList.add(i)
            else
                mBeanList.add(0)
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun initEvent() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            val restore = ArrayList<IntArray>()

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val position = viewHolder.adapterPosition
                if (mBeanList[position] == 0) {
                    return makeMovementFlags(0, 0);
                }
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                if (mBeanList[to] != 0) {
                    val emptyIndex = findEmptyIndex(from, to)
                    if (emptyIndex >= 0) {
                        move(emptyIndex, to)
                        swap(from, to)
                        for (intArray in restore) move(intArray[0], intArray[1])
                        restore.clear()
                        restore.add(0, intArrayOf(to, emptyIndex))
                    } else {
                        move(from, to)
                        restore.add(0, intArrayOf(to, from))
                    }
                } else {
                    swap(from, to)
                    for (intArray in restore) move(intArray[0], intArray[1])
                    restore.clear()
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mBeanList.removeAt(viewHolder.adapterPosition)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (viewHolder?.itemView != null) {// 当选中某个item时,将整个itemView变大
                    viewHolder.itemView.scaleX = 1.3F
                    viewHolder.itemView.scaleY = 1.3F
                }
            }

            override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                restore.clear()
                if (viewHolder.itemView != null) {// 松开时,itemView恢复原状
                    viewHolder.itemView.scaleX = 1F
                    viewHolder.itemView.scaleY = 1F
                }
            }
        }).attachToRecyclerView(home_rv)
    }

    /**
     * 将from位置的元素移动到to位置，其他元素会整体向前或向后
     */
    fun move(from: Int, to: Int) {
        mBeanList.add(to, mBeanList.removeAt(from))
        mAdapter.notifyItemMoved(from, to)
    }

    /**
     * 交换from位置元素和to位置元素的位置，其他元素的位置保持不变
     */
    fun swap(from: Int, to: Int) {
        Collections.swap(mBeanList, from, to)
        mAdapter.notifyItemMoved(from, to)
        mAdapter.notifyItemMoved(if (from < to) to - 1 else to + 1, from)
    }

    /**
     * 当from与to位置不相邻时，寻找to位置向前不小于或向后不大于from位置的、离to位置元素最近的空位置
     */
    private fun findEmptyIndex(from: Int, to: Int): Int {
        if (Math.abs(from - to) != 1 && mBeanList.size > from && mBeanList.size > to) {
            // 先向前找
            for(index in if (from <= to) to downTo from else to downTo 0) {
                if (mBeanList[index] == 0) return index
            }
            // 再向后找
            for(index in if (from >= to) to..from else to until mBeanList.size - 1) {
                if (mBeanList[index] == 0) return index
            }
        }
        return -1
    }

}