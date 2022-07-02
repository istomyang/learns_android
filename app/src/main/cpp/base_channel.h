//
// Created by 杨洋 on 31/5/2022.
//

#ifndef LEARNS_ANDROID_BASE_CHANNEL_H
#define LEARNS_ANDROID_BASE_CHANNEL_H

#include "f_macro.h"
#include "java_call_helper.h"
#include "safe_queue.h"

extern "C" {
#include "lib/ffmpeg/include/libavcodec/avcodec.h"
#include "lib/ffmpeg/include/libavutil/rational.h"
#include "lib/ffmpeg/include/libavutil/frame.h"
//#include <libavcodec/avcodec.h>
};

class BaseChannel {
public:
    SafeQueue<AVPacket *> packet_queue_; // 存储音频或视频编码数据
    SafeQueue<AVFrame *> frame_queue_; // 储音频或视频解码数据（原始数据）

    volatile int channelId_;
    volatile bool isPlay_;
    AVRational *timeBase_; // 分数
    AVCodecContext *avCodecContext_; // 编解码器上下文，编码和解码时必须用到的结构体，包含编解码器类型、视频宽高、音频通道数和采样率等信息；
    JavaCallHelper *javaCallHelper_;

public:
    BaseChannel(int id, JavaCallHelper *helper, AVCodecContext *ctx, AVRational *rational):
        channelId_(id),
        javaCallHelper_(helper),
        avCodecContext_(ctx),
        timeBase_(rational) {
        packet_queue_.setReleaseHandle(releaseAvPacket);
        frame_queue_.setReleaseHandle(releaseAvFrame)
    }

private:
    static void releaseAvFrame(AVFrame *&frame) {
        if (frame) {
            av_frame_free(&frame);
            frame = 0;
        }
    }

    static void releaseAvPacket(AVPacket *&packet) {
        if (packet) {
            av_packet_free(&packet);
            packet = 0;
        }
    }
};

#endif //LEARNS_ANDROID_BASE_CHANNEL_H
