package com.djacoronel.goalmentum.presentation.ui.adapters

import android.graphics.Paint
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.MilestonePresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.ExpandedWorkRecyclerClickListener
import kotlinx.android.synthetic.main.work_item.view.*

/**
 * Created by djacoronel on 10/10/17.
 */
class ExpandedWorkItemAdapter(val mView: MilestonePresenter.View, val milestoneId: Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        ExpandedWorkRecyclerClickListener {
    val mWorks = mutableListOf<Work>()

    override fun getItemCount() = mWorks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflate(R.layout.work_item), this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(mWorks[position])
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    class ViewHolder(itemView: View, private val mListener: ExpandedWorkRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(work: Work) = with(itemView) {
            placeholder_item_text.text = work.description

            plus_icon.setOnClickListener { mListener.onClickToggleWork(adapterPosition) }
            setOnLongClickListener { createAndShowPopupMenu(); true }

            if (work.achieved == true) {
                plus_icon.setImageResource(R.drawable.ic_check_black_24dp)
                placeholder_item_text.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                plus_icon.setImageResource(R.drawable.ic_dash_black_24dp)
                placeholder_item_text.paintFlags = 0
            }
        }

        fun createAndShowPopupMenu() {
            val popup = PopupMenu(itemView.context, itemView, Gravity.END)
            popup.menuInflater.inflate(R.menu.menu_view_goal, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Edit" -> mListener.onClickEditWork(adapterPosition)
                    "Delete" -> mListener.onClickDeleteWork(adapterPosition)
                }
                true
            }
            popup.show()
        }
    }

    override fun onClickEditWork(position: Int) {
        mView.onClickEditWork(mWorks[position])
    }

    override fun onClickDeleteWork(position: Int) {
        mView.onClickDeleteWork(mWorks[position].id)
    }

    override fun onClickToggleWork(position: Int) {
        mView.onClickToggleWork(mWorks[position])
    }

    fun showWorks(works: List<Work>) {
        mWorks.clear()
        mWorks.addAll(works)
        notifyDataSetChanged()
    }

    fun addWork(work: Work) {
        mWorks.add(work)
        notifyItemInserted(mWorks.indexOf(work))
    }

    fun updateWork(work: Work) {
        val workToBeUpdated = mWorks.find { it.id == work.id }
        workToBeUpdated?.let {
            it.description = work.description
            it.achieved = work.achieved
            it.dateAchieved = work.dateAchieved
        }
        notifyItemChanged(mWorks.indexOf(workToBeUpdated))
    }

    fun deleteWork(workId: Long) {
        val workToBeDeleted = mWorks.find { it.id == workId }
        val index = mWorks.indexOf(workToBeDeleted)
        mWorks.removeAt(index)
        notifyItemRemoved(index)
    }
}