package com.example.YHcamera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.YHcamera.Utils.VideoIO

//浏览视频的Activity
class ViewVideos : AppCompatActivity() {
    private var seq:Int=0
    private var Maxseq:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_vedios)
        supportActionBar?.hide()

        //初始化访问的视频id
        seq= VideoIO(this).getLastId().toInt()-1
        Maxseq=seq

        showVideo()

        findViewById<ImageButton>(R.id.next_video_btn).apply {
            setOnClickListener {
                if(seq>0){
                    seq -= 1
                    showVideo()
                }else{
                    Toast.makeText(this@ViewVideos,"这是最新的录像啦~", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<ImageButton>(R.id.pre_video_btn).apply {
            setOnClickListener {
                if(seq<Maxseq){
                    seq += 1
                    showVideo()
                }else{
                    Toast.makeText(this@ViewVideos,"没有更多录像啦~", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


    private fun showVideo(){
        val videoView=findViewById<VideoView>(R.id.videoView)
        val builder=StringBuilder()

        val path=builder.append(filesDir.absolutePath).append("/video").append(seq.toString()).append(".mp4").toString()


        videoView.setVideoPath(path)

        val mediaController=MediaController(this)
        videoView.setMediaController(mediaController)

        videoView.requestFocus()
    }


}