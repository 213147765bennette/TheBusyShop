package com.ikhokha.techcheck.util

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader

import com.google.firebase.storage.StorageReference
import java.io.InputStream


/**
 * Created by Bennette Molepo on 16/06/2022.
 */
@GlideModule
class ShopGlideModule: AppGlideModule(){

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}