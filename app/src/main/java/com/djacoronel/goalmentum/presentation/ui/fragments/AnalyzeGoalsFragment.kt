package com.djacoronel.goalmentum.presentation.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.db.chart.animation.Animation
import com.db.chart.model.Bar
import com.db.chart.model.BarSet
import com.db.chart.model.LineSet
import com.db.chart.model.Point

import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.presentation.presenters.AnalyzeGoalsPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AnalyzeGoalPresenterImpl
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.card_bar_graph.view.*
import kotlinx.android.synthetic.main.card_line_graph.view.*
import kotlinx.android.synthetic.main.fragment_analyze_goals.view.*


class AnalyzeGoalsFragment : Fragment(), AnalyzeGoalsPresenter.View {
    var colorPrimary: Int = 0
    var colorPrimaryDark: Int = 0
    var colorAccent: Int = 0

    fun newInstance(): AnalyzeGoalsFragment {
        return AnalyzeGoalsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_analyze_goals, container, false)

        colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
        colorPrimaryDark = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        colorAccent = ContextCompat.getColor(context, R.color.colorAccent)

        val analyzeGoalsPresenter = AnalyzeGoalPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl(),
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )

        analyzeGoalsPresenter.getWeeklyLineGraph()

        val barSet = BarSet()
        with(barSet) {
            addBar(Bar("Goal1", 1f))
            addBar(Bar("Goal2", 5f))
            addBar(Bar("Goal3", 3f))
            addBar(Bar("Goal4", 6f))
            addBar(Bar("Goal5", 1f))
            addBar(Bar("Goal6", 2f))
            addBar(Bar("Goal7", 4f))
            setColor(colorAccent)
        }

        with(view.bar_chart) {
            addData(barSet)
            setLabelsColor(Color.WHITE)
            setStep(1)
            setBorderSpacing(40)
            setFontSize(30)
            show(Animation(400))
        }
        return view
    }

    override fun onWeeklyLineGraphRetrieved(lineSet: LineSet) {
        with(lineSet) {
            setDotsRadius(10f)
            setDotsColor(colorAccent)
            setFill(colorPrimaryDark)
            color = colorAccent
        }

        view?.let {
            with(it.line_chart) {
                addData(lineSet)
                setLabelsColor(Color.WHITE)
                setFontSize(30)
                show(Animation(400))
            }
        }
    }

    override fun onWeeklyBarGraphRetrieved() {
    }

    override fun onAnalysisRetrieved() {
    }
}
