package com.kevintorch.carouseladapter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.kevintorch.carsousel_adapter.CarouselAdapter
import com.kevintorch.carsousel_adapter.CarouselStrategy
import com.kevintorch.carsousel_adapter.ImageItem

class MainActivity : AppCompatActivity() {

    private val carouselAdapter = CarouselAdapter()
    private val images = listOf(
        "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
        "https://plus.unsplash.com/premium_photo-1664304519683-edde298ccc9d?w=800&auto=format&fit" +
                "=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw1fHx8ZW58MHx8fHx8",
        "https://images.unsplash.com/photo-1716040313180-aa8df510ccfb?w=800&auto=format&fit=crop" +
                "&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxMXx8fGVufDB8fHx8fA%3D%3D",
        "https://images.unsplash.com/photo-1716180971265-308eb0c1a0bd?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDEzfGJvOGpRS1RhRTBZfHxlbnwwfHx8fHw%3D")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        carouselAdapter.setStrategy(CarouselStrategy.FullScreen)
        carouselAdapter.attachToRecyclerView(recyclerView)

        val imageItems = images.mapIndexed { i, it -> ImageItem(it, "Image $i") }

        carouselAdapter.submitList(imageItems)
    }
}