package com.djacoronel.goalmentum.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.presentation.presenters.MainPresenter
import kotlinx.android.synthetic.main.achieved_goal_item.view.*

/**
 * Created by djacoronel on 10/7/17.
 */
class AchievedGoalItemAdapter(
        val mView: MainPresenter.View
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mGoals = mutableListOf<Goal>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(goal: Goal) = with(itemView) {
            itemView.achieved_goal_card_text.text = goal.description
            itemView.achieved_duration_text.text = goal.duration

            val achievedCount = goal.achievedMilestone.toString() + "M " + goal.achievedWork.toString() + "W"
            itemView.achieved_milestone_work_count.text = achievedCount

            itemView.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflate(R.layout.achieved_goal_item))
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ViewHolder).bind(mGoals[position])
    }

    override fun getItemCount() = mGoals.size

    fun showGoals(goals: List<Goal>) {
        val achievedGoals = goals.filter { it.achieved == true }
        mGoals.clear()
        mGoals.addAll(achievedGoals)
        notifyDataSetChanged()
    }
}