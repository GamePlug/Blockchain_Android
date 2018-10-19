package com.leichao.biubiu.home.app.home

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.leichao.biubiu.home.app.AppInfo
import com.leichao.biubiu.home.app.AppManager
import java.util.*

class HomeDragCallback(private val mAdapter: HomeAdapter) : ItemTouchHelper.Callback() {

    private val mBeanList = AppManager.getHomeAppList()
    private val restore = ArrayList<IntArray>()

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val position = viewHolder.adapterPosition
        val app = mBeanList[position]
        return if (app.type == AppInfo.Type.EMPTY) {
            makeMovementFlags(0, 0)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
            makeMovementFlags(dragFlags, 0)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        val fromApp = mBeanList[from]
        val toApp = mBeanList[to]
        when {
            toApp.plugin.appName == "回收站"
                    && toApp.type == AppInfo.Type.SYSTEM
                    && fromApp.type != AppInfo.Type.SYSTEM -> {
                for (intArray in restore) move(intArray[0], intArray[1])
                restore.clear()
                fromApp.uninstallApp()
            }
            toApp.type == AppInfo.Type.EMPTY -> {
                swap(from, to)
                for (intArray in restore) move(intArray[0], intArray[1])
                restore.clear()
            }
            else -> {
                move(from, to)
                restore.add(0, intArrayOf(to, from))
            }
        }
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        // 当选中某个item时,将整个itemView变大
        viewHolder?.itemView?.scaleX = 1.3F
        viewHolder?.itemView?.scaleY = 1.3F
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        restore.clear()
        // 松开时,itemView恢复原状
        viewHolder.itemView.scaleX = 1F
        viewHolder.itemView.scaleY = 1F
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    /**
     * 将from位置的元素删除，其他元素位置不变
     */
    private fun delete(from: Int) {
        mBeanList.removeAt(from)
        mBeanList.add(from, AppManager.getEmptyApp())
        mAdapter.notifyDataSetChanged()
    }

    /**
     * 将from位置的元素移动到to位置，其他元素会整体向前或向后
     */
    private fun move(from: Int, to: Int) {
        mBeanList.add(to, mBeanList.removeAt(from))
        mAdapter.notifyItemMoved(from, to)
    }

    /**
     * 交换from位置元素和to位置元素的位置，其他元素的位置保持不变
     */
    private fun swap(from: Int, to: Int) {
        Collections.swap(mBeanList, from, to)
        mAdapter.notifyItemMoved(from, to)
        mAdapter.notifyItemMoved(if (from < to) to - 1 else to + 1, from)
    }

}