package com.example.YHcamera

import android.graphics.*
import android.media.ExifInterface
import android.os.Bundle
import android.view.ScaleGestureDetector
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.YHcamera.Utils.PhotoIO
import java.io.IOException

//浏览照片的Activity
class ViewPhotos : AppCompatActivity() {
    //当前应展示的照片id、照片的最大id
    private var seq:Int = 0
    private var Maxseq:Int=0
    private lateinit var imageView:ImageView
    private lateinit var gestureDetector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_photos)
        supportActionBar?.hide()

        //初始化访问的照片id
        seq= PhotoIO(this@ViewPhotos).getLastId().toInt()-1
        Maxseq=seq

        viewPhoto()


        findViewById<ImageButton>(R.id.next_photo_btn).apply {
            setOnClickListener {
                if(seq>0){
                    seq -= 1
                    viewPhoto()
                }else{
                    Toast.makeText(this@ViewPhotos,"这是最新的照片啦~",Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<ImageButton>(R.id.pre_photo_btn).apply {
            setOnClickListener {
                if(seq<Maxseq){
                    seq += 1
                    viewPhoto()
                }else{
                    Toast.makeText(this@ViewPhotos,"没有更多照片啦~",Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    //刷新ImageView
    public fun viewPhoto(){
        imageView=findViewById<ImageView>(R.id.photoView)
        imageView.setImageBitmap(getPhoto())
    }


    //以Bitmap格式读取照片
    public fun getPhoto(): Bitmap? {

        var builder=StringBuilder()
        val fileName=builder.append("pic").append(seq.toString()).append(".jpg").toString()



        var builder2=StringBuilder()
        val path= builder2.append(filesDir).append(fileName).toString()

        val fin=openFileInput(fileName)

        val data=fin.readBytes()
        var array= BitmapFactory.decodeByteArray(data,0,data.size)
        array=rotatePhoto(90,array)

        return array
    }

    //照片旋转
    private fun rotatePhoto(angle:Int,bitmap:Bitmap): Bitmap? {
        val matrix=Matrix()
        matrix.postRotate((angle.toFloat()))
        return Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }
}