package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.politics.politicalapp.R
import com.politics.politicalapp.adapter.DharasabhyoAdapter
import kotlinx.android.synthetic.main.activity_dharasabhyo.*

class DharasabhyoListActivity : ExtendedToolbarActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var govtWorkNewsAdapter: DharasabhyoAdapter

    override val layoutId: Int
        get() = R.layout.activity_dharasabhyo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle(getString(R.string.dharasabhyo))
        setupList()
        setupDistrictSpinner()
    }

    private fun setupList() {
        layoutManager = GridLayoutManager(this, 2)
        rvNewsPortal.layoutManager = layoutManager

        val stringList: ArrayList<String> = ArrayList()
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")
        stringList.add("S")

        govtWorkNewsAdapter = DharasabhyoAdapter(
            {
                //callIntent(this, it.contact_no!!)
                startActivity(Intent(this, DharasabhyoDetailActivity::class.java))
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