package com.kevintorch.carsousel_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.kevintorch.androidktx.lerp
import com.kevintorch.carsousel_adapter.databinding.CarouselImageViewBinding
import com.kevintorch.carsousel_adapter.ext.load


interface CarouselItemClickListener {
    fun invoke(view: View, position: Int)
}

data class ImageItem(
    var imageUrl: String? = null,
    var imageTitle: String? = null
)

class ImageItemDiffUtilCallback : DiffUtil.ItemCallback<ImageItem>() {
    override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem == newItem
    }

}

class CarouselAdapter : ListAdapter<ImageItem, CarouselAdapter.CarouselViewHolder>(
    ImageItemDiffUtilCallback()
), AdapterItemListener {

    private val carouselSnapHelper = CarouselSnapHelper()
    private var onItemClickListener: CarouselItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: CarouselItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = this
        if (recyclerView.layoutManager !is CarouselLayoutManager) {
            recyclerView.layoutManager = CarouselLayoutManager()
        }
        carouselSnapHelper.attachToRecyclerView(recyclerView)
    }

    fun getSnapHelper(): SnapHelper {
        return carouselSnapHelper
    }

    override fun onItemClicked(holder: RecyclerView.ViewHolder, position: Int) {
        onItemClickListener?.invoke(holder.itemView, position)
    }

    class CarouselViewHolder(private val binding: CarouselImageViewBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(item: ImageItem? = null) {
            if (item == null) return
            binding.imageView.load(
                url = item.imageUrl,
            )
            binding.overlay.isVisible = !item.imageTitle.isNullOrBlank()
            binding.title.text = item.imageTitle
            binding.root.setOnMaskChangedListener {
                val lerp = lerp(1F, 0F, 0F, 80F, it.left)
                binding.title.alpha = lerp
                binding.overlay.alpha = lerp
                binding.title.translationX = it.left
                val z = lerp(24F, 0F, 0F, 150F, it.left)
                binding.root.translationZ = z
            }
            binding.root.setOnClickListener {
                notifyClick()
            }
        }

        private fun notifyClick() {
            if (bindingAdapterPosition == RecyclerView.NO_POSITION) return
            if (bindingAdapter is AdapterItemListener) {
                (bindingAdapter as AdapterItemListener).onItemClicked(this, bindingAdapterPosition)
            }
        }

        companion object {
            fun create(parent: ViewGroup): CarouselViewHolder {
                val binding = CarouselImageViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CarouselViewHolder(binding)
            }
        }
    }
}