package com.orbits.queuingsystem.helper

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.WheelView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orbits.queuingsystem.R
import com.orbits.queuingsystem.databinding.LayoutSelectCounterDialogBinding
import com.orbits.queuingsystem.helper.Global.getDimension
import com.orbits.queuingsystem.helper.interfaces.AlertDialogInterface
import com.orbits.queuingsystem.helper.interfaces.WheelViewEvent


object Dialogs {

    var customDialog: Dialog? = null
    var pairingDialog: Dialog? = null
    var codeDialog: Dialog? = null
    var selectCounterDialog: Dialog? = null



    fun showSelectCounterDialog(
        activity: Activity,
        dataList : ArrayList<CounterListDataModel> ?= null,
        alertDialogInterface: AlertDialogInterface,
    ) {
        try {
            var webId = ""
            var serviceId = ""
            var name = ""
            selectCounterDialog = Dialog(activity)
            selectCounterDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            selectCounterDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val binding: LayoutSelectCounterDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.layout_select_counter_dialog, null, false
            )
            selectCounterDialog?.setContentView(binding.root)
            val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
            lp.copyFrom(selectCounterDialog?.window?.attributes)
            lp.width = getDimension(activity as Activity, 200.00)
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            selectCounterDialog?.window?.attributes = lp
            selectCounterDialog?.setCanceledOnTouchOutside(true)
            selectCounterDialog?.setCancelable(true)


            val options: MutableList<String> = ArrayList()
            options.addAll(dataList?.map { it.id } as ArrayList<String>)

            println("here is options list  ::::: $options")

            val middleAdapter =
                ArrayAdapter(activity, android.R.layout.simple_spinner_item, options)
            middleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCounterSelection.adapter = middleAdapter

            binding.spinnerCounterSelection.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    webId = dataList[position].id ?: ""
                    serviceId = dataList[position].serviceId ?: ""
                    name = dataList[position].counterType ?: ""
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected
                }
            }


            /*binding.edtCounter.setOnClickListener {
                showWheelView(
                    activity,
                    arrayListData = dataList?.map { it.id } as ArrayList<String>
                ) { value ->
                    dataList.forEach { it.isSelected = false }
                    dataList[value].isSelected = true
                    binding.edtCounter.setText("Counter ${dataList[value].id}")

                }
            }*/



            binding.btnAlertPositive.setOnClickListener {
                if (name.isEmpty()){
                    Toast.makeText(activity,"Please select Counter", Toast.LENGTH_SHORT).show()
                }
                else{
                    selectCounterDialog?.dismiss()
                    alertDialogInterface.onCounterSelected(webId,name,serviceId)
                }
            }
            selectCounterDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun showWheelView(
        activity: Activity,
        arrayListData: ArrayList<String>,
        listener: WheelViewEvent
    ) {
        val parentView = activity.layoutInflater.inflate(R.layout.layout_bottom_sheet_picker, null)
        val bottomSheerDialog = BottomSheetDialog(activity)
        bottomSheerDialog.setContentView(parentView)
        bottomSheerDialog.setCanceledOnTouchOutside(false)
        bottomSheerDialog.setCancelable(false)
        val wheelView = parentView.findViewById(R.id.wheelView) as WheelView
        val txtDone = parentView.findViewById(R.id.txtDone) as TextView
        val txtCancel = parentView.findViewById(R.id.txtCancel) as TextView

        try {
            if (arrayListData.isNotEmpty()) {
                wheelView.setItems(arrayListData)
                txtCancel.setOnClickListener {
                    bottomSheerDialog.dismiss()
                }
                txtDone.setOnClickListener {
                    bottomSheerDialog.dismiss()
                    listener.onDoneClicked(wheelView.seletedIndex)
                }
                bottomSheerDialog.show()
            }
        } catch (e: Exception) {
        }
    }


}
