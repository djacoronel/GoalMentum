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
import com.db.chart.renderer.AxisRenderer

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
import java.util.*


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
        analyzeGoalsPresenter.getWeeklyBarGraph()

        return view
    }

    override fun onWeeklyLineGraphRetrieved(dataPoints: List<Point>) {
        val currentWeekSet = LineSet()
        val lastWeekSet = LineSet()

        for (points in dataPoints) {
            currentWeekSet.addPoint(points)
            lastWeekSet.addPoint(points)
        }

        val today = Calendar.getInstance()
        val todayIndex = today.get(Calendar.DAY_OF_WEEK) - 1

        with(currentWeekSet) {
            beginAt(0)
            endAt(todayIndex)
            setDotsRadius(10f)
            setFill(colorPrimaryDark)
            color = colorAccent
        }

        with(lastWeekSet) {
            beginAt(todayIndex - 1)
            endAt(7)
            setDotsRadius(10f)
            setFill(colorPrimaryDark)
            color = Color.LTGRAY
            setDashed(floatArrayOf(10f, 10f))
        }

        val entries = currentWeekSet.entries
        for (i in 0..entries.lastIndex) {
            if (i < todayIndex) entries[i].color = colorAccent
            else entries[i].color = Color.LTGRAY
        }

        view?.let {
            with(it.line_chart) {
                addData(currentWeekSet)
                addData(lastWeekSet)
                setLabelsColor(Color.WHITE)
                setFontSize(30)
                setYLabels(AxisRenderer.LabelPosition.INSIDE)
                show(Animation(400))
            }
        }
    }

    override fun onWeeklyBarGraphRetrieved(dataBars: List<Bar>) {
        val barSet = BarSet()

        for (bar in dataBars) {
            barSet.addBar(bar)
        }

        barSet.color = colorAccent

        view?.let {
            with(it.bar_chart) {
                addData(barSet)
                setLabelsColor(Color.WHITE)
                setFontSize(30)
                setBarSpacing(8f)
                setYLabels(AxisRenderer.LabelPosition.INSIDE)
                show(Animation(400))
            }
        }
    }

    override fun onAnalysisRetrieved() {
    }
}
