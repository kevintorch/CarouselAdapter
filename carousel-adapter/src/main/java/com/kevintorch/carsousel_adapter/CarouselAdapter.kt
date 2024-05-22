package com.kevintorch.carsousel_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselLayoutManager.ALIGNMENT_CENTER
import com.google.android.material.carousel.CarouselLayoutManager.ALIGNMENT_START
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
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

internal class ImageItemDiffUtilCallback : DiffUtil.ItemCallback<ImageItem>() {
    override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem == newItem
    }

}

enum class CarouselStrategy {
    FullScreen,
    MultiBrowse,
    Hero,
}

@IntDef(value = [CarouselLayoutManager.VERTICAL, CarouselLayoutManager.HORIZONTAL])
annotation class Orientation {}

@IntDef(value = [ALIGNMENT_START, ALIGNMENT_CENTER])
annotation class Alignment {}

class CarouselAdapter : ListAdapter<ImageItem, CarouselAdapter.CarouselViewHolder>(
    ImageItemDiffUtilCallback()
), AdapterItemListener {

    companion object {
        @JvmStatic
        val ALIGNMENT_START = 0

        @JvmStatic
        val ALIGNMENT_CENTER = 1
    }


    private val carouselLayoutManager = CarouselLayoutManager()
    private val carouselSnapHelper = CarouselSnapHelper()
    private var onItemClickListener: CarouselItemClickListener? = null

    fun setOrientation(@Orientation orientation: Int) {
        carouselLayoutManager.orientation = orientation
    }

    fun setCarouselAlignment(@Alignment alignment: Int) {
        carouselLayoutManager.carouselAlignment = alignment
    }

    fun setStrategy(strategy: CarouselStrategy) {
        val carouselStrategy = when (strategy) {
            CarouselStrategy.FullScreen  -> FullScreenCarouselStrategy()
            CarouselStrategy.MultiBrowse -> MultiBrowseCarouselStrategy()
            CarouselStrategy.Hero        -> HeroCarouselStrategy()
        }
        carouselLayoutManager.setCarouselStrategy(carouselStrategy)
    }

    fun setOnItemClickListener(onItemClickListener: CarouselItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (onItemClickListener != null) {
            holder.setClickable()
        }
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = this
        if (recyclerView.layoutManager !is CarouselLayoutManager) {
            recyclerView.layoutManager = carouselLayoutManager.apply {
                setCarouselStrategy(HeroCarouselStrategy())
            }
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
        private var onItemClickListener: View.OnClickListener? = null

        init {
            onItemClickListener = View.OnClickListener { notifyClick() }
        }

        fun setOnClickListener(onItemClickListener: View.OnClickListener) {
            this.onItemClickListener = onItemClickListener
            setClickable()
        }

        fun setClickable() {
            itemView.setOnClickListener(onItemClickListener)
        }

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
        }

        private fun notifyClick() {
            if (bindingAdapterPosition == RecyclerView.NO_POSITION || bindingAdapter !is AdapterItemListener) return
            (bindingAdapter as AdapterItemListener).onItemClicked(this, bindingAdapterPosition)
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