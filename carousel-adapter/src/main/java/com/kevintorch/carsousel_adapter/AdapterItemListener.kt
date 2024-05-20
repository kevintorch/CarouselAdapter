package com.kevintorch.carsousel_adapter

import androidx.recyclerview.widget.RecyclerView

interface AdapterItemListener {
    fun onItemClicked(holder: RecyclerView.ViewHolder, position: Int)
}