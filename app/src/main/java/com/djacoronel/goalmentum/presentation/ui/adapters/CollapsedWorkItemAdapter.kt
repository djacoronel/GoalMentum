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
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.WorkRecyclerClickListener
import kotlinx.android.synthetic.main.work_item.view.*


/**
 * Created by djacoronel on 10/10/17.
 */
class CollapsedWorkItemAdapter(val mView: ViewGoalPresenter.View, val milestoneId: Long, val mWorks: List<Work>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        WorkRecyclerClickListener {

    override fun getItemCount() = mWorks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NormalViewHolder(parent.inflate(R.layout.work_item), this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as NormalViewHolder).bind(mWorks[position])

    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    class NormalViewHolder(itemView: View, private val mListener: WorkRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(work: Work) = with(itemView) {
            itemView.work_card_text.text = work.description
            itemView.setOnLongClickListener {
                val popup = PopupMenu(context, this, Gravity.END)
                popup.menuInflater.inflate(R.menu.menu_view_goal, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.title) {
                        "Edit" -> mListener.onClickEditWork(adapterPosition)
                        "Delete" -> mListener.onClickDeleteWork(adapterPosition)
                    }
                    true
                }
                popup.show()
                true
            }

            itemView.finish_button.setOnClickListener {
                mListener.onClickToggleWork(adapterPosition)
            }

            if (work.achieved == true) {
                itemView.finish_button.setImageResource(R.drawable.ic_check_black_24dp)
                val textView = itemView.work_card_text
                textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                itemView.finish_button.setImageResource(R.drawable.ic_dash_black_24dp)
                val textView = itemView.work_card_text
                textView.paintFlags = 0
            }
        }
    }

    override fun onClickAddWork() {
    }

    override fun onClickEditWork(position: Int) {
    }

    override fun onClickDeleteWork(position: Int) {
    }

    override fun onClickToggleWork(position: Int) {
        mView.onClickToggleWork(mWorks[position])
    }

    fun updateWork(work: Work){
        val workToBeUpdated = mWorks.find { it.id == work.id }
        workToBeUpdated?.let {
            it.description = work.description
            it.achieved = work.achieved
            it.dateAchieved = work.dateAchieved
        }
        notifyItemChanged(mWorks.indexOf(workToBeUpdated))
    }
}