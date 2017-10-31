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
import kotlinx.android.synthetic.main.card_bar_graph.view.*
import kotlinx.android.synthetic.main.card_line_graph.view.*
import kotlinx.android.synthetic.main.fragment_analyze_goals.view.*


class AnalyzeGoalsFragment : Fragment() {

    fun newInstance(): AnalyzeGoalsFragment {
        return AnalyzeGoalsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_analyze_goals, container, false)

        val colorPrimary = ContextCompat.getColor(context,R.color.colorPrimary)
        val colorPrimaryDark = ContextCompat.getColor(context,R.color.colorPrimaryDark)
        val colorAccent = ContextCompat.getColor(context,R.color.colorAccent)

        val lineSet = LineSet()
        with(lineSet){
            addPoint(Point("Mon", 1f))
            addPoint(Point("Tue", 5f))
            addPoint(Point("Wed", 3f))
            addPoint(Point("Thu", 6f))
            addPoint(Point("Fri", 1f))
            addPoint(Point("Sat", 2f))
            addPoint(Point("Sun", 4f))

            setDotsRadius(10f)
            setDotsColor(colorAccent)
            color = colorAccent
            setFill(colorPrimaryDark)
        }
        

        with(view.line_chart){
            addData(lineSet)
            setLabelsColor(Color.WHITE)
            setStep(1)
            setBorderSpacing(40)
            setFontSize(30)
            show(Animation(400))
        }


        val barSet = BarSet()
        with(barSet){
            addBar(Bar("Goal1", 1f))
            addBar(Bar("Goal2", 5f))
            addBar(Bar("Goal3", 3f))
            addBar(Bar("Goal4", 6f))
            addBar(Bar("Goal5", 1f))
            addBar(Bar("Goal6", 2f))
            addBar(Bar("Goal7", 4f))
            setColor(colorAccent)
        }

        with(view.bar_chart){
            addData(barSet)
            setLabelsColor(Color.WHITE)
            setStep(1)
            setBorderSpacing(40)
            setFontSize(30)
            show(Animation(400))
        }
        return view
    }
}
