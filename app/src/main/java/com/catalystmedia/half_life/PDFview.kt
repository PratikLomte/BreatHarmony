package com.catalystmedia.half_life

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_p_d_fview.*

class PDFview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_d_fview)
        val inputStream = resources.openRawResource(R.raw.ic_pdf_yoga)
        pdfView.fromStream(inputStream)
            .load();


    }
}