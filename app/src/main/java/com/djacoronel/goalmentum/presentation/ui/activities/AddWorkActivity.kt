package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.AddWorkPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AddWorkPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.adapters.SimpleWorkItemAdapter
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.activity_add_work.*


class AddWorkActivity : AppCompatActivity(), AddWorkPresenter.View {

    lateinit var mAdapter: SimpleWorkItemAdapter
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


        val customEditText = input_item_text as CustomEditText
        customEditText.requestFocus()
        add_item_button.setOnClickListener {
            onClickAddWork(customEditText.text.toString())
        }

        mAdapter = SimpleWorkItemAdapter(this, milestoneId)
        work_recycler.layoutManager = LinearLayoutManager(this)
        work_recycler.adapter = mAdapter

        mAddWorkPresenter.getAllWorkByAssignedMilestone(milestoneId)
        showKeyboard()
    }

    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(message: String) {
    }


    override fun showWorks(milestoneId: Long, works: List<Work>) {
        mAdapter.showWorks(works)
    }

    override fun onClickAddWork(workDescription: String) {
        mAddWorkPresenter.addNewWork(milestoneId, workDescription)
    }

    override fun onWorkAdded(work: Work) {
        mAdapter.addWork(work)
    }

}