package com.politics.politicalapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.politics.politicalapp.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        setupDistrictSpinner()
        setupCitySpinner()

        ibRegister.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun setupDistrictSpinner() {
        val cityList: ArrayList<String> = ArrayList()
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")
        cityList.add("તમારો જિલ્લો પસંદ કરો")

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item,
            cityList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_district.adapter = adapter

        sp_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
        }
    }

    private fun setupCitySpinner() {
        val cityList: ArrayList<String> = ArrayList()
        cityList.add("તમારું શહેર પસંદ કરો")
        cityList.add("તમારું શહેર પસંદ કરો")
        cityList.add("તમારું શહેર પસંદ કરો")
        cityList.add("તમારું શહેર પસંદ કરો")

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            R.layout.simple_spinner_dropdown_item,
            cityList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_city.adapter = adapter

        sp_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
        }
    }
}