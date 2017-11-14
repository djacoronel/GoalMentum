package com.djacoronel.goalmentum.presentation.ui.adapters

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.CollapsedWorkRecyclerClickListener
import kotlinx.android.synthetic.main.work_item.view.*

/**
 * Created by djacoronel on 10/10/17.
 */

class CollapsedWorkItemAdapter(val mView: GoalPresenter.View, val mWorks: List<Work>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        CollapsedWorkRecyclerClickListener {

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

    class ViewHolder(itemView: View, private val mListener: CollapsedWorkRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(work: Work) = with(itemView) {
            work_item_text.text = work.description

            achieve_button.setOnClickListener { mListener.onClickToggleWork(adapterPosition) }

            if (work.achieved == true) {
                achieve_button.setImageResource(R.drawable.ic_check_black_24dp)
                work_item_text.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                achieve_button.setImageResource(R.drawable.ic_dash_black_24dp)
                work_item_text.paintFlags = 0
            }
        }
    }

    override fun onClickToggleWork(position: Int) {
        mView.onClickToggleWork(mWorks[position])
    }

    fun updateWork(work: Work) {
        val workToBeUpdated = mWorks.find { it.id == work.id }
        workToBeUpdated?.let {
            it.description = work.description
            it.achieved = work.achieved
            it.dateAchieved = work.dateAchieved
        }
        notifyItemChanged(mWorks.indexOf(workToBeUpdated))

        if (mWorks.all { it.achieved==true })
            mView.onDisplayedWorksAchieved(work.assignedMilestone)
    }
}