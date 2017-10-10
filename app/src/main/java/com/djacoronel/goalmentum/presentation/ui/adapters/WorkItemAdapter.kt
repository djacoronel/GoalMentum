package com.djacoronel.goalmentum.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.WorkRecyclerClickListener
import kotlinx.android.synthetic.main.input_work_item.view.*
import kotlinx.android.synthetic.main.work_item.view.*

/**
 * Created by djacoronel on 10/10/17.
 */
class WorkItemAdapter(val mView: ViewGoalPresenter.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), WorkRecyclerClickListener {
    val mWorks = mutableListOf(Work(0, "Input Work"))

    private enum class ViewType {
        NORMAL_CARD, INPUT_CARD
    }

    override fun getItemCount() = mWorks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.INPUT_CARD.ordinal)
            InputViewHolder(parent.inflate(R.layout.input_work_item), this)
        else
            NormalViewHolder(parent.inflate(R.layout.input_work_item), this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is InputViewHolder -> holder.bind()
            is NormalViewHolder -> holder.bind(mWorks[position])
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mWorks.lastIndex)
            ViewType.INPUT_CARD.ordinal
        else
            ViewType.NORMAL_CARD.ordinal
    }

    class InputViewHolder(itemView: View, private val mListener: WorkRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind() = with(itemView) {
            itemView.add_work_button.setOnClickListener {
                mListener.onClickAddWork(adapterPosition)
            }
        }
    }

    class NormalViewHolder(itemView: View, private val mListener: WorkRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(work:Work) = with (itemView){
            itemView.work_card_text.text = work.description
        }
    }

    override fun onClickAddWork(position: Int) {

    }

    override fun onLongClickWork(position: Int) {

    }

    override fun onClickFinishWork(position: Int) {

    }
}