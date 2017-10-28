package com.djacoronel.goalmentum.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.AddWorkPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AddWorkPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.adapters.ExpandedWorkItemAdapter
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.activity_add_work.*


class AddWorkActivity : AppCompatActivity(), AddWorkPresenter.View {

    lateinit var mAdapter: ExpandedWorkItemAdapter
    lateinit var mAddWorkPresenter: AddWorkPresenter
    var milestoneId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work)

        milestoneId = intent.getLongExtra("extra_milestone_id_key", -1)

        mAddWorkPresenter = AddWorkPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )

        val customEditText = input_item_text

        add_item_button.setOnClickListener {
            onClickAddWork(customEditText.text.toString())
        }

        expanded_milestone_card_text.setOnClickListener { finish() }
        collapse_button.setOnClickListener { finish() }

        mAdapter = ExpandedWorkItemAdapter(this, milestoneId)
        work_recycler.layoutManager = LinearLayoutManager(this)
        work_recycler.adapter = mAdapter

        mAddWorkPresenter.getMilestoneById(milestoneId)
        mAddWorkPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }

    override fun onMilestoneRetrieved(milestone: Milestone) {
        expanded_milestone_card_text.text = milestone.description

        if (milestone.achieved == true)
            expanded_achieved_icon.visibility = View.VISIBLE
        else
            expanded_achieved_icon.visibility = View.GONE
    }


    override fun showWorks(milestoneId: Long, works: List<Work>) {
        mAdapter.showWorks(works)
        if(works.isEmpty())
            input_item_text.requestFocus()
    }

    override fun onClickAddWork(workDescription: String) {
        mAddWorkPresenter.addNewWork(milestoneId, workDescription)
    }

    override fun onWorkAdded(work: Work) {
        mAdapter.addWork(work)
    }
}