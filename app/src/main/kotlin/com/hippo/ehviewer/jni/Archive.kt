@file:RequiresApi(Build.VERSION_CODES.P)

package com.hippo.ehviewer.jni

import android.os.Build
import androidx.annotation.RequiresApi
import java.nio.ByteBuffer

external fun releaseByteBuffer(buffer: ByteBuffer)
external fun openArchive(fd: Int, size: Long): Int
external fun extractToByteBuffer(index: Int): ByteBuffer?
external fun extractToFd(index: Int, fd: Int)
external fun getFilename(index: Int): String
external fun needPassword(): Boolean
external fun providePassword(str: String): Boolean
external fun closeArchive()
