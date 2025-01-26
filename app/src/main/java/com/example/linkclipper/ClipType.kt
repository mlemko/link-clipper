package com.example.linkclipper

/**
 * Dictates the type of action done by a [ClipAction].
 */
enum class ClipType(val value: Int) {
    /**
     * Removes specific query parameters from the link.
     */
    REMOVE_SI(0),

    /**
     * Removes the query and all parameters from the link.
     */
    REMOVE_QUERY(1);

    companion object {
        fun getByInt(value: Int) = ClipType.entries.firstOrNull { it.value == value }
    }
}