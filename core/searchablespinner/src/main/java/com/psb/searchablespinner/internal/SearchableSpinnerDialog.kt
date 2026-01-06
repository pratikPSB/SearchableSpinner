package com.psb.searchablespinner.internal

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psb.searchablespinner.SearchableSpinnerView
import com.psb.searchablespinner.common.internal.Constants
import com.psb.searchablespinner.common.internal.DialogListener
import com.psb.searchablespinner.common.Spinner
import com.psb.searchablespinner.common.SpinnerListener
import com.psb.searchablespinner.databinding.DialogSpinnerBinding
import java.util.Locale

internal class SearchableSpinnerDialog {

    private lateinit var dialog: Dialog
    private lateinit var context: Context
    private lateinit var dialogSpinnerBinding: DialogSpinnerBinding
    private lateinit var searchableSpinnerListAdapter: SearchableSpinnerListAdapter
    private lateinit var spinnerListener: SpinnerListener
    private lateinit var searchableSpinnerView: SearchableSpinnerView
    private var list = ArrayList<Spinner>()
    private var lastSelectionIndex: Int = 0
    private var selectedItemIndex: Int = 0
    private lateinit var dialogListener: DialogListener

    fun setSpinnerDialog(
        context: Context,
        searchableSpinnerView: SearchableSpinnerView,
        list: ArrayList<Spinner>,
        spinnerListener: SpinnerListener
    ): Dialog {
        this.context = context
        dialogSpinnerBinding = DialogSpinnerBinding.inflate(LayoutInflater.from(context))
        this.spinnerListener = spinnerListener
        this.searchableSpinnerView = searchableSpinnerView
        this.list = list

        for (i in 0 until list.size) {
            list[i].parentId = i
        }

        dialogSpinnerBinding.apply {
            if (searchableSpinnerView.showSearchBar) {
                searchView.visibility = View.VISIBLE
                searchView.queryHint = searchableSpinnerView.searchHintText
            } else {
                searchView.visibility = View.GONE
            }

            dialogListener = object : DialogListener {
                override fun saveChanges() {
                    dialog.dismiss()
                    for (i in 0 until list.size) {
                        if (list[i].isSelected) {
                            selectedItemIndex = i
                            break
                        }
                    }
                    setPreviousSelectedItemIndex(selectedItemIndex)
                }
            }
            rvList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            searchableSpinnerListAdapter = SearchableSpinnerListAdapter(
                context, list, searchableSpinnerView.showTick, dialogListener
            )
            rvList.adapter = searchableSpinnerListAdapter

            searchableSpinnerView.setOnClickListener {
                Log.d(Constants.TAG, "on Load : $lastSelectionIndex")
                show()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(Constants.TAG, "search..")

                    if (!TextUtils.isEmpty(newText)) {
                        val query: String = newText.toString().lowercase(Locale.getDefault()).trim()
                        val searchList = ArrayList<Spinner>()

                        for (i in 0 until list.size) {
                            if (list[i].name.lowercase(Locale.getDefault()).contains(query)) {
                                searchList.add(list[i])
                            }
                        }

                        dialogSpinnerBinding.rvList.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        searchableSpinnerListAdapter = SearchableSpinnerListAdapter(
                            context,
                            searchList,
                            searchableSpinnerView.showTick,
                            dialogListener,
                            true
                        )
                        dialogSpinnerBinding.rvList.adapter = searchableSpinnerListAdapter
                        searchableSpinnerListAdapter.setMainListForSearch(list)
                    } else {
                        dialogSpinnerBinding.rvList.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        searchableSpinnerListAdapter = SearchableSpinnerListAdapter(
                            context, list, searchableSpinnerView.showTick, dialogListener
                        )
                        dialogSpinnerBinding.rvList.adapter = searchableSpinnerListAdapter
                    }
                    return true
                }
            })
        }

        val alertDialog = AlertDialog.Builder(context).apply {
            setTitle(searchableSpinnerView.dialogTitle)
            setView(dialogSpinnerBinding.root)
            setCancelable(searchableSpinnerView.setCancelable)
            setOnDismissListener {
                dialogSpinnerBinding.searchView.setQuery("", true)
            }
            setNegativeButton(searchableSpinnerView.negativeButtonText) { dialog, _ ->
                dialog.dismiss()
            }
        }
        dialog = alertDialog.create()

        setPreviousSelectedItemIndex(0)
        return dialog
    }

    private fun show() {
        for (i in 0 until list.size) {
            list[i].isSelected = false
        }
        if (list.size >= lastSelectionIndex + 1) {
            list[lastSelectionIndex].isSelected = true
        }

        dialogSpinnerBinding.apply {
            rvList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            searchableSpinnerListAdapter = SearchableSpinnerListAdapter(
                context, list, searchableSpinnerView.showTick, dialogListener
            )
            rvList.adapter = searchableSpinnerListAdapter
        }

        dialog.show()
        Log.d(Constants.TAG, "show() - list size ${searchableSpinnerListAdapter.itemCount}")
    }

    fun dismiss() {
        dialog.dismiss()
    }

    private fun setSelection(index: Int) {
        if (list.size >= index + 1) {
            searchableSpinnerView.text = list[index].name
        }
        searchableSpinnerListAdapter.setSelection(index)
        spinnerListener.setOnItemClickListener(index)
    }

    fun setPreviousSelectedItemIndex(index: Int) {
        lastSelectionIndex = index
        setSelection(index)
    }

    fun getSelectedItemIndex(): Int {
        return lastSelectionIndex
    }

    fun getSelectedItemId(): Int {
        return list[lastSelectionIndex].id
    }

    fun getSelectedItemName(): String {
        return list[lastSelectionIndex].name
    }

    fun setSelectedItemByName(selectedItemName: String?) {
        var position = 0
        for (i in 0 until list.size) {
            if (list[i].name == selectedItemName) {
                position = i
                break
            }
        }
        setPreviousSelectedItemIndex(position)
    }

    fun setSelectedItemById(selectedItemId: Int) {
        var position = 0
        for (i in 0 until list.size) {
            if (list[i].id == selectedItemId) {
                position = i
                break
            }
        }
        setPreviousSelectedItemIndex(position)
    }
}