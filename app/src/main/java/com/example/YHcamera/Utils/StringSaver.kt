package com.example.YHcamera.Utils

import android.content.Context
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

//保存字符串至data/com.example.YHcamera/files中的文件下
class StringSaver (val context: Context){
    public fun save(fileName:String,str:String){
        try {
            val output=context.openFileOutput(fileName,Context.MODE_PRIVATE)
            val writer=BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write(str)
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}