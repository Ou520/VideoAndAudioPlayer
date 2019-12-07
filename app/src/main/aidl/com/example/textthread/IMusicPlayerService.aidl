// IMusicPlayerService.aidl
package com.example.textthread;


//写一个接口
interface IMusicPlayerService {
    /*-----------------------------音乐播放器的常用方法--------------------------------------------------*/
        //根据位置打开对应的音频文件的方法
        void openAudio(int position);
        //播放音乐的方法
        void start();

        //暂停播放音乐的方法
        void pause();

        //停止播放音乐的方法
        void stop();

        //得到当前音频的播放进度的方法
        int getCurrentPosition();

        //得到音频的总时长的方法
        int getDuration();

        //得到音频的艺术家（作者）的方法
        String getArtist();

        //得到歌曲的名字的方法
        String getName();

        //得到歌曲的路径的方法
        String getAudioPath();

        //播放上一个音频
        void pre();

        //播放下一音频
        void next();

        //设置播放模式
        void setPlayMode(int playmode);

        //得到播放模式
       int getPlayMode();

       //是否正在播放
       boolean isPlaying();

       //拖动音频
       void seekTo(int position);
}
