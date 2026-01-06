package com.psb.searchablespinner

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.psb.searchablespinner.common.Spinner
import com.psb.searchablespinner.common.SpinnerListener
import com.psb.searchablespinner.internal.SearchableSpinnerDialog

/**
 * A custom AppCompatTextView that opens a searchable dialog for single item selection.
 */
class SearchableSpinnerView : AppCompatTextView {

    /**
     * Determines whether to show the search bar in the dialog. Default is true.
     * Controlled by `app:showSearchBar` attribute in XML.
     */
    var showSearchBar = true

    /**
     * Determines whether the dialog is cancelable. Default is false.
     * Controlled by `app:setCancelable` attribute in XML.
     */
    var setCancelable = false

    /**
     * Determines whether to show a tick next to the selected item. Default is false.
     * Controlled by `app:showTick` attribute in XML.
     */
    var showTick = false

    /**
     * The title of the dialog.
     * Controlled by `app:dialogTitle` attribute in XML.
     */
    var dialogTitle = ""

    /**
     * The hint text for the search view in the dialog.
     * Controlled by `app:dialogSearchHint` attribute in XML.
     */
    var searchHintText = ""

    /**
     * The text for the negative button in the dialog.
     * Controlled by `app:negativeButtonText` attribute in XML.
     */
    var negativeButtonText = ""

    /**
     * Lazily initializes the [com.psb.searchablespinner.internal.SearchableSpinnerDialog].
     */
    private val dialog by lazy { SearchableSpinnerDialog() }

    /**
     * Default constructor.
     * @param context The context for this view.
     */
    constructor(context: Context) : super(context) {
        initView()
    }

    /**
     * Constructor that is called when inflating a view from XML.
     * @param context The context for this view.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinnerView)
        try {
            for (i in 0 until typedArray.indexCount) {
                when (val attr = typedArray.getIndex(i)) {
                    R.styleable.SearchableSpinnerView_showSearchBar -> {
                        showSearchBar = typedArray.getBoolean(attr, true)
                    }

                    R.styleable.SearchableSpinnerView_setCancelable -> {
                        setCancelable = typedArray.getBoolean(attr, false)
                    }

                    R.styleable.SearchableSpinnerView_dialogTitle -> {
                        dialogTitle =
                            typedArray.getString(attr) ?: context.getString(R.string.select_item)
                    }

                    R.styleable.SearchableSpinnerView_dialogSearchHint -> {
                        searchHintText =
                            typedArray.getString(attr) ?: context.getString(R.string.search_hint)
                    }

                    R.styleable.SearchableSpinnerView_negativeButtonText -> {
                        negativeButtonText =
                            typedArray.getString(attr) ?: context.getString(R.string.close)
                    }

                    R.styleable.SearchableSpinnerView_showTick -> {
                        showTick = typedArray.getBoolean(attr, false)
                    }
                }
            }
        } finally {
            typedArray.recycle()
            initView()
        }
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a theme attribute.
     * @param context The context for this view.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view.
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    /**
     * Initializes the view with default properties.
     */
    private fun initView() {
        this.apply {
            gravity = Gravity.CENTER_VERTICAL
            isSingleLine = true
            compoundDrawablePadding = 5
            ellipsize = TextUtils.TruncateAt.END
        }
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0, 0,
            R.drawable.ic_drop_down_arrow_black, 0
        )
    }

    /**
     * Sets the data for the spinner dialog.
     * @param context The context.
     * @param list The list of items to display in the spinner.
     * @param listener The listener for item click events.
     */
    fun setData(context: Context, list: ArrayList<Spinner>, listener: SpinnerListener) {
        dialog.setSpinnerDialog(context, this, list, listener)
    }

    /**
     * Sets the selected item in the spinner by its ID.
     * @param index The ID of the item to select.
     */
    fun setSelectedItemById(index: Int) {
        dialog.setSelectedItemById(index)
    }

    /**
     * Sets the selected item in the spinner by its name.
     * @param selectedItemName The name of the item to select.
     */
    fun setSelectedItemByName(selectedItemName: String?) {
        dialog.setSelectedItemByName(selectedItemName)
    }

    /**
     * Gets the ID of the currently selected item.
     * @return The ID of the selected item.
     */
    fun getSelectedItemId(): Int {
        return dialog.getSelectedItemId()
    }

    /**
     * Gets the name of the currently selected item.
     * @return The name of the selected item.
     */
    fun getSelectedItemName(): String {
        return dialog.getSelectedItemName()
    }

    /**
     * Gets the index of the currently selected item.
     * @return The index of the selected item.
     */
    fun getSelectedItemIndex(): Int {
        return dialog.getSelectedItemIndex()
    }

    /**
     * Sets the previously selected item index. This is used to restore the selection.
     * @param index The index of the previously selected item.
     */
    fun setPreviousSelectedItemIndex(index: Int) {
        dialog.setPreviousSelectedItemIndex(index)
    }

    /**
     * Dismisses the spinner dialog.
     */
    fun dismiss() {
        dialog.dismiss()
    }
}
