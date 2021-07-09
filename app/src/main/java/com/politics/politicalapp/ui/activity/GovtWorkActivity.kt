package com.politics.politicalapp.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.GovtWorkNewsAdapter
import kotlinx.android.synthetic.main.activity_govt_work.*

class GovtWorkActivity : ExtendedToolbarActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: GovtWorkNewsAdapter

    override val layoutId: Int
        get() = R.layout.activity_govt_work

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.govt_work))
        setupList()
        setupDistrictSpinner()
    }

    private fun setupList() {

        layoutManager = LinearLayoutManager(this)
        rvNewsPortal.layoutManager = layoutManager

        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        govtWorkNewsAdapter = GovtWorkNewsAdapter(
            {
                //callIntent(this, it.contact_no!!)
            }, {
                //browserIntent(this, it.website!!)
            }
        )
        govtWorkNewsAdapter.setItem(stringList)
        rvNewsPortal.adapter = govtWorkNewsAdapter
    }

    private fun setupDistrictSpinner() {
        val cityList: ArrayList<String> = ArrayList()
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_black_item,
            cityList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spDistrict.adapter = adapter

        spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
        }
    }

}