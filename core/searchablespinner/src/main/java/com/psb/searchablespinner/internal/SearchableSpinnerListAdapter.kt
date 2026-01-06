package com.psb.searchablespinner.internal

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.psb.searchablespinner.common.internal.DialogListener
import com.psb.searchablespinner.common.Spinner
import com.psb.searchablespinner.common.internal.Utilities
import com.psb.searchablespinner.databinding.ItemSpinnerListBinding

internal class SearchableSpinnerListAdapter(
    private var context: Context,
    private var list: ArrayList<Spinner>,
    private var showTick: Boolean,
    private var listener: DialogListener,
    private var isSearchList: Boolean = false
) : RecyclerView.Adapter<SearchableSpinnerListAdapter.ViewHolder>() {
    private var mainList = ArrayList<Spinner>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSpinnerListBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.absoluteAdapterPosition >= 0) {
            holder.apply {
                binding.apply {
                    tvText.text = list[holder.absoluteAdapterPosition].name

                    if (holder.absoluteAdapterPosition == itemCount - 1)
                        view.visibility = View.INVISIBLE
                    else
                        view.visibility = View.VISIBLE

                    ivCheck.setColorFilter(Utilities.fetchPrimaryColor(context))

                    if (showTick) {
                        if (list[holder.absoluteAdapterPosition].isSelected) {
                            ivCheck.visibility = View.VISIBLE
                        } else {
                            ivCheck.visibility = View.GONE
                        }
                    } else {
                        ivCheck.visibility = View.GONE
                        if (list[holder.absoluteAdapterPosition].isSelected) {
                            itemView.setBackgroundColor("#dedede".toColorInt())
                        } else {
                            itemView.setBackgroundColor(Color.WHITE)
                        }
                    }
                }
                itemView.setOnClickListener {
                    if (holder.absoluteAdapterPosition >= 0) {

                        for (i in 0 until list.size) {
                            list[i].isSelected = false
                        }
                        list[holder.absoluteAdapterPosition].isSelected = true

                        if (isSearchList) {
                            if (mainList.size >= (list[holder.absoluteAdapterPosition].parentId + 1)) {
                                for (i in 0 until mainList.size) {
                                    mainList[i].isSelected = false
                                }
                                mainList[list[holder.absoluteAdapterPosition].parentId].isSelected =
                                    true
                            }
                        }

                        notifyDataSetChanged()
                        listener.saveChanges()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(viewBinding: ItemSpinnerListBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        var binding = viewBinding
    }

    fun setSelection(index: Int) {
        if (list.size >= index + 1) {
            for (i in 0 until list.size) {
                list[i].isSelected = false
            }
            list[index].isSelected = true
            notifyDataSetChanged()
        }
    }

    fun setMainListForSearch(mainList: ArrayList<Spinner>) {
        this.mainList = mainList
    }
}