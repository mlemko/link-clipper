package com.example.linkclipper

/**
 * Dictates the type of action done by a [ClipAction].
 */
enum class ClipType() {
    /**
     * Removes specific query parameters from the link.
     */
    REMOVE_SI,

    /**
     * Removes the query and all parameters from the link.
     */
    REMOVE_QUERY
}