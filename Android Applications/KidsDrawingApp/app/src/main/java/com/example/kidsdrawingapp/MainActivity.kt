package com.example.kidsdrawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView?= null
    private var mImageButtonCurrentPaint: ImageButton?= null
    private var customProgressDialog: Dialog?= null

    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode== RESULT_OK && result.data!== null)
            {
                val imageBackground: ImageView= findViewById(R.id.iv_background)
                imageBackground.setImageURI(result.data?.data)
            }
        }

    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                var permissionName= it.key
                var isGranted= it.value

                if(isGranted)
                {
                    Toast.makeText(this, "Permission for External Storage is Granted", Toast.LENGTH_LONG).show()

                    var pickIntent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)
                }
                else
                {
                    if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE)
                    {
                        Toast.makeText(this, "Ops Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView= findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(20.toFloat())

        var linearLayoutPaintColors= findViewById<LinearLayout>(R.id.ll_paint_color)
        mImageButtonCurrentPaint= linearLayoutPaintColors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )

        val ib_brush: ImageButton = findViewById(R.id.ib_brush)
        ib_brush.setOnClickListener{
            showBrushSizeChooserDialog()
        }

        val ib_undu: ImageButton = findViewById(R.id.ib_undo)
        ib_undu.setOnClickListener{
            drawingView?.onClickUndo()
        }

        val ib_save: ImageButton = findViewById(R.id.ib_save)
        ib_save.setOnClickListener{
            if(isReadStorageAllowed())
            {
                lifecycleScope.launch{
                    showCustomDialog()
                    val flDrawingView: FrameLayout= findViewById(R.id.fl_drawing_view_container)
                    val myBitmap: Bitmap= getBitmapFromView(flDrawingView)
                    saveBimapFile(myBitmap)
                }
            }
        }

        val ibGallery: ImageButton= findViewById(R.id.ib_gallery)
        ibGallery.setOnClickListener{
            requestStoragePermission()
        }
    }

    private fun showBrushSizeChooserDialog(){

        val brushDialog= Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size: ")

        val smallBtn: ImageButton= brushDialog.findViewById(R.id.ib_small_brush)
        smallBtn.setOnClickListener{
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }

        val mediumBtn: ImageButton= brushDialog.findViewById(R.id.ib_medium_brush)
        mediumBtn.setOnClickListener{
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }

        val largeBtn: ImageButton= brushDialog.findViewById(R.id.ib_large_brush)
        largeBtn.setOnClickListener{
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }

    fun paintClicked(view: View){

        if(view!== mImageButtonCurrentPaint){
            val imageButton= view as ImageButton
            val colorTag= imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
            )

            mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_normal)
            )

            mImageButtonCurrentPaint= view
        }
    }

    private  fun isReadStorageAllowed(): Boolean
    {
        var result= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result== PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            showRationaleDialog("Kids Drawing App", "Kids Drawing App needs to Access your External Storage")
        }
        else
        {
            requestPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE))

        }
    }

    private fun showRationaleDialog( title: String, message: String)
    {
        val builder: AlertDialog.Builder= AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("Cancel"){ dialog, _->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun getBitmapFromView(view: View): Bitmap
    {
        val returnedBitmap= Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas= Canvas(returnedBitmap)
        val bgDrawable= view.background
        if(bgDrawable!= null)
        {
            bgDrawable.draw(canvas)
        }
        else
        {
            canvas.drawColor(Color.WHITE)
        }

        view.draw(canvas)
        return returnedBitmap
    }

    private suspend fun saveBimapFile(mBitmap: Bitmap?): String
    {
        var result= ""
        withContext(Dispatchers.IO)
        {
            if(mBitmap!= null)
            {
                try
                {
                    val bytes= ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

                    val f= File(externalCacheDir?.absoluteFile.toString()+ File.separator +"Kids Drawing App_"+
                            System.currentTimeMillis()/ 1000 + ".jpg")

                    val fo= FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    fo.close()

                    result= f.absolutePath

                    runOnUiThread{
                        cancelCustomDialog()
                        if(result.isNotEmpty())
                        {
                            Toast.makeText(this@MainActivity, "File saved successfully: $result", Toast.LENGTH_LONG).show()
                            shareImape(result)
                        }
                        else
                        {
                            Toast.makeText(this@MainActivity, "Something went wrong while saving the file", Toast.LENGTH_LONG).show()
                        }
                    }
                }catch (e: Exception)
                {
                    result= ""
                    e.printStackTrace()
                }

            }
        }
        return result
    }

    private fun showCustomDialog()
    {
        customProgressDialog= Dialog(this)
        customProgressDialog?.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog?.show()
    }

    private fun cancelCustomDialog()
    {
        if(customProgressDialog!= null)
        {
            customProgressDialog?.dismiss()
            customProgressDialog= null
        }
    }

    private fun shareImape(resut: String)
    {
        MediaScannerConnection.scanFile(this, arrayOf(resut), null)
        {
            path, uri ->
            var shareIntent= Intent()
            shareIntent.action= Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type= "image/png"
            startActivity(Intent.createChooser(shareIntent, "share"))
        }
    }
}