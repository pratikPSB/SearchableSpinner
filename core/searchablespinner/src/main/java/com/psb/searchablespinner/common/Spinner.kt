package com.psb.searchablespinner.common

data class Spinner(
    var id: Int = 0,
    var name: String = "",
    var isSelected: Boolean = false,
    var parentId: Int = 0
)