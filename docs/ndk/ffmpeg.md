# FFmpeg

```shell
#!/bin/bash

make clean
set -e

# 提供一些参考
# https://gist.github.com/HJHL/be1d587385ed43b00321d7965ebe4194
# https://gist.github.com/CaiJingLong/feefa3b63e5b94f5ad1807825861e0e0
# https://medium.com/@ilja.kosynkin/building-ffmpeg-4-0-for-android-with-clang-642e4911c31e
# https://cloud.tencent.com/developer/article/1773965

############################# 配置区 ################################
archbit=64
android_api=31

# 在命令行 ./configure -h 选择功能
export ADDITIONAL_CONFIGURE_FLAG=""
export ADDITIONAL_CONFIGURE_FLAG="$ADDITIONAL_CONFIGURE_FLAG"

############################# 运行部分 ################################

if [ $archbit -eq 64 ]; then
	echo "build for 64bit"
	ARCH=aarch64
	CPU=armv8-a
	API=$android_api
	PLATFORM=aarch64
	ANDROID=android
	# 取自 .cxx 目录里的 build.ninja 文件里，目的是使用同AS一样的编译选项
	CFLAGS="-g -DANDROID -fdata-sections -ffunction-sections -funwind-tables -fstack-protector-strong -no-canonical-prefixes -D_FORTIFY_SOURCE=2 -Wformat -Werror=format-security   -O2 -g -DNDEBUG -fPIC"
	LDFLAGS="-lc"

else
	echo "build for 32bit"
	ARCH=arm
	CPU=armv7-a
	API=16
	PLATFORM=armv7a
	ANDROID=androideabi
	CFLAGS="-mfloat-abi=softfp -march=$CPU"
	LDFLAGS="-Wl,--fix-cortex-a8"
fi

# 要改
export NDK=/Users/yangyang/Library/Android/sdk/ndk/24.0.8215888
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/darwin-x86_64/bin
export SYSROOT=$NDK/toolchains/llvm/prebuilt/darwin-x86_64/sysroot
export CROSS_PREFIX=$TOOLCHAIN/$ARCH-linux-$ANDROID-
export CC=$TOOLCHAIN/$PLATFORM-linux-$ANDROID$API-clang
export CXX=$TOOLCHAIN/$PLATFORM-linux-$ANDROID$API-clang++
export PREFIX=$(pwd)/android/$CPU

# 检查一下选项
function build_android {
	./configure \
		--prefix=$PREFIX \
		--cross-prefix=$CROSS_PREFIX \
		--target-os=android \
		--arch=$ARCH \
		--cpu=$CPU \
		--cc=$CC \
		--cxx=$CXX \
		--nm=$TOOLCHAIN/llvm-nm \
		--strip=$TOOLCHAIN/llvm-strip \
		--enable-cross-compile \
		--sysroot=$SYSROOT \
		--extra-cflags="$CFLAGS" \
		--extra-ldflags="$LDFLAGS" \
		--extra-ldexeflags=-pie \
		--enable-runtime-cpudetect \
		--disable-static \
		--enable-shared \
		--disable-programs \
		--disable-debug \
		--disable-doc \
		--enable-avfilter \
		--enable-decoders \
		--enable-postproc \
		--enable-gpl \
		$ADDITIONAL_CONFIGURE_FLAG

	make
	make install
}
build_android

```