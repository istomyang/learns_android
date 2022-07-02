# CMake是个一个开源的跨平台自动化建构系统，用来管理软件建置的程序，并不依赖于某特定编译器，并可支持多层目录、多个应用程序与多个库。

# https://cmake.org/cmake/help/latest/guide/tutorial/index.html
# https://developer.android.com/studio/projects/configure-cmake

# 坑：
# + Cannot specify link libraries for target "INTERFACE" which is not built by this project. 等等
#    好好检查路径对不对，错误具有很大的迷惑性。

# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("learns_android")

# FFmpeg
# https://cloud.tencent.com/developer/article/1773965
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
message("11111111111")
message(${ANDROID_ABI}) # arm64-v8a
message(${CMAKE_SOURCE_DIR}) #/Users/yangyang/Learns/MyProjects/learns_android/app/src/main/cpp
set(ffmpeg_lib_dir ${CMAKE_SOURCE_DIR}/lib/ffmpeg/${ANDROID_ABI})
set(ffmpeg_head_dir ${CMAKE_SOURCE_DIR}/lib/ffmpeg/include/)

include_directories(${ffmpeg_head_dir}) # 添加头文件目录

add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavutil.so)
add_library(swresample SHARED IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libswresample.so)
add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavcodec.so)
add_library(avfilter SHARED IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavfilter.so)
add_library(swscale SHARED IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libswscale.so)
add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavformat.so)
add_library(avdevice SHARED IMPORTED)
set_target_properties(avdevice PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libavdevice.so)
add_library(postproc SHARED IMPORTED)
set_target_properties(postproc PROPERTIES IMPORTED_LOCATION ${ffmpeg_lib_dir}/libpostproc.so)

# 或许可以直接设置链接地址，不需要上面的。
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/src/main/cpp/libs/${ANDROID_ABI}")

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

# 为了让 CMake 能够在编译时找到头文件
#add_library( main SHARED main.cpp)

# 创建一个变量 source_file 它的值就是src/main/cpp/ 所有的.cpp文件
file(GLOB files *.cpp)
add_library( files SHARED ${ffmpeg_files})

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
#        main

        files

#        src_lib_ffmpeg

        # FFmpeg.so
        avutil
        swresample
        avcodec
        avfilter
        swscale
        avformat
        avdevice
        postproc

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        )