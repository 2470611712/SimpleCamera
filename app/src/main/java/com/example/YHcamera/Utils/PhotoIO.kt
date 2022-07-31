package com.example.YHcamera.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.media.ImageReader
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer

//照片的存取
class PhotoIO (private val context:Context){

    private val IdInformationFile="photo_id"
    private var seq=getLastId().toInt()-1


    //保存照片到data/com.example.YHcamera/文件夹，并且在photo_id文件中保存次序信息
    public fun save(reader:ImageReader){
        val image: Image = reader.acquireLatestImage()

        val buffer: ByteBuffer = image.planes[0].buffer
        val data : ByteArray = ByteArray(buffer.remaining())
        buffer.get(data)

        val id=getLastId()
        val builder=StringBuilder()
        val fileName=builder.append("pic").append(id).append(".jpg").toString()

        val output=context.openFileOutput(fileName, Context.MODE_PRIVATE)
        output.use {
            it.write(data,0,data.size)
        }


        updateLastId(id.toInt()+1)

        image.close()
    }

    //获取最新的照片
    public fun openLast(): Bitmap? {
        var builder=StringBuilder()
        val fileName=builder.append("pic").append((getLastId().toInt()-1).toString()).append(".jpg").toString()

        val fin=context.openFileInput(fileName)
        val data=fin.readBytes()
        var array= BitmapFactory.decodeByteArray(data,0,data.size)
        array=rotatePhoto(90,array)
        return array
    }



    //更新照片的最大id
    private fun updateLastId(id:Int){
        val Strsaver= StringSaver(context)
        Strsaver.save(IdInformationFile,id.toString())
    }

    //获取最新照片id
    public fun getLastId(): String {
        var content=StringBuilder()
        try {
            val input=context.openFileInput(IdInformationFile)
            val reader=BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    content.append(it)
                }
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
        if(content.isEmpty()){return "0"}
        return content.toString()
    }

    //Bitmap旋转
    private fun rotatePhoto(angle:Int,bitmap:Bitmap): Bitmap? {
        val matrix= Matrix()
        matrix.postRotate((angle.toFloat()))
        return Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }
}