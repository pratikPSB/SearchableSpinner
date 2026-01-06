package com.psb.searchablespinner

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.psb.searchablespinner.common.Spinner
import com.psb.searchablespinner.common.SpinnerListener
import com.psb.searchablespinner.databinding.ActivitySpinnerBinding

class SpinnerActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySpinnerBinding.inflate(layoutInflater) }

    private val userList = ArrayList<Spinner>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
                topMargin = insets.top
            }

            WindowInsetsCompat.CONSUMED
        }

        setUserList()
    }

    private fun setUserList() {
        if (userList.isNotEmpty())
            userList.clear()

        userList.add(
            Spinner(
                0,
                "--Select--"
            )
        )
        for (i in 1..26) {
            userList.add(
                Spinner(
                    i,
                    "User $i"
                )
            )
        }

        setUserListAdapter()
    }

    private fun setUserListAdapter() {
        binding.spUser.setData(this@SpinnerActivity, userList, object : SpinnerListener {
            override fun setOnItemClickListener(position: Int) {
                Toast.makeText(this@SpinnerActivity, "${userList[position].id}", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        binding.spUser.setSelectedItemById(2)
    }
}