# Carousel Adapter
Ready to use material 3 carousel adaper for recycler view (based on M3 Carousel). No need to create all the boilerplate code, just attach to recyclerView and use it.

Latest Version 👉  [![](https://jitpack.io/v/kevintorch/CarouselAdapter.svg)](https://jitpack.io/#kevintorch/CarouselAdapter)


## Demo

https://github.com/kevintorch/CarouselAdapter/assets/36185881/f43f7410-c5b3-442c-9994-287034b528ff


### Installation

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```
Step 2. Add the dependency

```groovy
dependencies {
    implementation "com.github.kevintorch:CarouselAdapter:1.0.1"
}
```

### Usage

```kotlin
 class MainActivity : AppCompatActivity() { 
  
     private val carouselAdapter = CarouselAdapter() 
     private val images = listOf( 
         "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg", 
         "https://plus.unsplash.com/premium_photo-1664304519683-edde298ccc9d?w=800&auto=format&fit" + 
                 "=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw1fHx8ZW58MHx8fHx8", 
         "https://images.unsplash.com/photo-1716040313180-aa8df510ccfb?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxMXx8fGVufDB8fHx8fA%3D%3D") 
  
     override fun onCreate(savedInstanceState: Bundle?) { 
         super.onCreate(savedInstanceState) 
         setContentView(R.layout.activity_main) 
  
         val recyclerView = findViewById<RecyclerView>(R.id.recyclerView) 
  
         carouselAdapter.attachToRecyclerView(recyclerView) 
  
         val imageItems = images.mapIndexed { i, it -> ImageItem(it, "Image $i") } 
  
         carouselAdapter.submitList(imageItems) 
     } 
 }
```
