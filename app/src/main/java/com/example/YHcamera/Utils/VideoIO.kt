package com.example.YHcamera.Utils

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

//视频存取
class VideoIO (val context: Context){
    private val IdInformationFile="video_id"

    //创建data/com.example.YHcamera/files目录下的MP4文件
    public fun makeVideoFile(): File {
        val id=getLastId()
        val builder=StringBuilder()
        val path=builder.append("video").append(id).append(".mp4").toString()
        updateLastId(id.toInt()+1)
        return File(context.filesDir,path)
    }

    //更新id
    private fun updateLastId(id:Int){
        val Strsaver= StringSaver(context)
        Strsaver.save(IdInformationFile,id.toString())
    }

    //获取最新视频id
    public fun getLastId(): String {
        val content=StringBuilder()
        try {
            val input=context.openFileInput(IdInformationFile)
            val reader= BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    content.append(it)
                }
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        if(content.isEmpty()){return "0"}
        return content.toString()
    }

    //获取指定id的文件路径
    public fun getFileDir(seq:Int): String {
        val id=getLastId()
        val builder=StringBuilder()
        val path=builder.append("video").append(id).append(".mp4").toString()
        return path
    }
}