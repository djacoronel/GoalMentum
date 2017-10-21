package com.djacoronel.goalmentum.presentation.ui.adapters

import android.graphics.Paint
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter
import com.djacoronel.goalmentum.presentation.ui.listeners.WorkRecyclerClickListener
import kotlinx.android.synthetic.main.work_item.view.*
import android.view.*
import kotlinx.android.synthetic.main.input_recyler_item.view.*


/**
 * Created by djacoronel on 10/10/17.
 */
class WorkItemAdapter(val mView: ViewGoalPresenter.View, val milestoneId: Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        WorkRecyclerClickListener {
    val mWorks = mutableListOf<Work>()

    private enum class ViewType {
        NORMAL_CARD, INPUT_CARD
    }

    override fun getItemCount() = mWorks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.INPUT_CARD.ordinal)
            InputViewHolder(parent.inflate(R.layout.input_recyler_item), this)
        else
            NormalViewHolder(parent.inflate(R.layout.work_item), this)
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
            itemView.input_recycler_text.text = "add new task"
            itemView.setOnClickListener {
                mListener.onClickAddWork()
            }
        }
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
        mView.onClickAddWork(milestoneId)
    }

    override fun onClickEditWork(position: Int) {
        mView.onClickEditWork(mWorks[position])
    }

    override fun onClickDeleteWork(position: Int) {
        mView.onClickDeleteWork(mWorks[position])
    }

    override fun onClickToggleWork(position: Int) {
        mView.onClickToggleWork(mWorks[position])
        notifyItemChanged(position)
    }


    fun showWorks(works: List<Work>) {
        mWorks.clear()
        mWorks.addAll(works)
        val inputWorkEntry = Work(0, "Input Work")
        mWorks.add(inputWorkEntry)
        notifyDataSetChanged()
    }

    fun addWork(work: Work){
        mWorks.add(mWorks.lastIndex,work)
        notifyItemInserted(mWorks.indexOf(work))
    }

    fun updateWork(work: Work){
        val workToBeUpdated = mWorks.find { it.id == work.id }
        workToBeUpdated?.description = work.description
        notifyItemChanged(mWorks.indexOf(workToBeUpdated))
    }

    fun deleteWork(work: Work){
        val workToBeDeleted = mWorks.find { it.id == work.id }
        val index = mWorks.indexOf(workToBeDeleted)
        mWorks.removeAt(index)
        notifyItemRemoved(index)
    }
}