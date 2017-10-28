package com.djacoronel.goalmentum.presentation.ui.adapters

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.AddWorkPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.SimpleWorkRecyclerClickListener
import kotlinx.android.synthetic.main.activity_add_work.view.*
import kotlinx.android.synthetic.main.work_item.view.*

/**
 * Created by djacoronel on 10/10/17.
 */
class ExpandedWorkItemAdapter(val mView: AddWorkPresenter.View, val milestoneId: Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        SimpleWorkRecyclerClickListener {
    val mWorks = mutableListOf<Work>()

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

    class NormalViewHolder(itemView: View, private val mListener: SimpleWorkRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(work: Work) = with(itemView) {
            itemView.work_card_text.text = work.description

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

    override fun onClickAddWork(workDescription: String) {
    }

    override fun onClickEditWork(position: Int) {
    }

    override fun onClickDeleteWork(position: Int) {
    }

    override fun onClickToggleWork(position: Int) {
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
        workToBeUpdated?.description = work.description
        notifyItemChanged(mWorks.indexOf(workToBeUpdated))
    }

    fun deleteWork(work: Work) {
        val workToBeDeleted = mWorks.find { it.id == work.id }
        val index = mWorks.indexOf(workToBeDeleted)
        mWorks.removeAt(index)
        notifyItemRemoved(index)
    }
}