package com.orbits.queuingsystem

import android.app.Fragment
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.io.File

class CompanyLogo : Fragment() {
    var companyLogo: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        companyLogo = inflater.inflate(R.layout.activity_company_logo, container, true)
        return companyLogo
    }

    override fun onStart() {
        super.onStart()
        setCompanyLogo()
    }

    // load company logo from sdcard.
    fun setCompanyLogo() {
        val imageInSD = findImageWithExtensions("/Queue_Config Files/logo")
        val imgPath = File(imageInSD)
        if (imgPath.exists()) {
            val bitmap = BitmapFactory.decodeFile(imageInSD)
            val myImageView = companyLogo!!.findViewById<View>(R.id.imgCompanyLogo) as ImageView
            myImageView.setImageBitmap(bitmap)
        }
    }

    companion object {
        fun findImageWithExtensions(basePath: String): String? {
            val extensions = arrayOf(".png", ".jpg", ".jpeg", ".gif")
            for (ext in extensions) {
                val filePath: String = MainActivity.Companion.sdcardPath + basePath + ext
                val file = File(filePath)
                if (file.exists()) {
                    return filePath
                }
            }
            return null // No image file found with the given extensions
        }
    }
}
