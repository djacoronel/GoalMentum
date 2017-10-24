package com.djacoronel.goalmentum.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.GoalPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.activities.AddGoalActivity
import com.djacoronel.goalmentum.presentation.ui.activities.ViewGoalActivity
import com.djacoronel.goalmentum.presentation.ui.adapters.AchievedGoalItemAdapter
import com.djacoronel.goalmentum.presentation.ui.adapters.GoalItemAdapter
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.fragment_active_goals.*
import kotlinx.android.synthetic.main.fragment_active_goals.view.*


/**
 * Created by djacoronel on 10/7/17.
 */
class AchievedGoalsFragment : Fragment(), GoalPresenter.View {

    private lateinit var mGoalPresenter: GoalPresenter
    private lateinit var mAdapter: AchievedGoalItemAdapter

    fun newInstance(): AchievedGoalsFragment {
        return AchievedGoalsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_active_goals, container, false)
        init(view)

        return view
    }

    private fun init(view: View) {
        // setup recycler view adapter
        mAdapter = AchievedGoalItemAdapter(this)

        // setup recycler view
        view.goal_recycler.layoutManager = LinearLayoutManager(activity)
        view.goal_recycler.layoutManager.isAutoMeasureEnabled = false
        view.goal_recycler.adapter = mAdapter

        // instantiate the presenter
        mGoalPresenter = GoalPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl(),
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )
    }

    override fun showGoals(goals: List<Goal>) {
        mAdapter.showGoals(goals)
        view?.let { runLayoutAnimation(it.goal_recycler) }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onClickViewGoal(goal: Goal) {
        val intent = Intent(context, ViewGoalActivity::class.java)
        intent.putExtra(EXTRA_GOAL_ID, goal.id)
        intent.putExtra(EXTRA_GOAL_DESC, goal.description)
        startActivity(intent)
    }

    override fun onClickAddGoal() {
    }

    override fun onGoalAdded(goal: Goal) {
    }

    override fun onClickEditGoal(goal: Goal) {
    }

    override fun onGoalUpdated(goal: Goal) {
    }

    override fun onClickDeleteGoal(goal: Goal) {
    }

    override fun onGoalDeleted(goal: Goal) {
    }


    companion object {
        const val EXTRA_GOAL_ID = "extra_goal_id_key"
        const val EXTRA_GOAL_DESC = "extra_goal_desc_key"
        const val EDIT_GOAL_REQUEST = 1
        const val ADD_GOAL_REQUEST = 0
    }

    override fun onResume() {
        super.onResume()
        mGoalPresenter.resume()
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(message: String) {
    }
}