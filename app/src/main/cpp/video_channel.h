//
// Created by 杨洋 on 31/5/2022.
//

#ifndef LEARNS_ANDROID_VIDEO_CHANNEL_H
#define LEARNS_ANDROID_VIDEO_CHANNEL_H

#include <pthread.h>
#include "base_channel.h"
#include "java_call_helper.h"

typedef void (*RenderFrame)(uint8_t *, int, int, int);

class VideoChannel: public BaseChannel {
private:
    int fps;
    pthread_t pid_video_play;
    pthread_t pid_synchronize;
    RenderFrame renderFrame;

public:
    AudioChannel *audioChannel = 0;

public:
    VideoChannel(int id, JavaCallHelper *helper, AVCodecContext *ctx, AVRational base, int fps);

    virtual ~VideoChannel();

    virtual void play();

    virtual void stop();

    void decodePacket();

    void syncFrame();

    void setRenderCallback(RenderFrame renderFrame)
};


#endif //LEARNS_ANDROID_VIDEO_CHANNEL_H
