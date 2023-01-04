/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hippo.ehviewer.preference

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable.Creator
import android.util.AttributeSet
import androidx.preference.Preference
import com.hippo.ehviewer.EhApplication.Companion.downloadManager
import com.hippo.ehviewer.EhApplication.Companion.okHttpClient
import com.hippo.ehviewer.EhDB
import com.hippo.ehviewer.R
import com.hippo.ehviewer.Settings
import com.hippo.ehviewer.client.EhEngine.fillGalleryListByApi
import com.hippo.ehviewer.client.EhUrl
import com.hippo.ehviewer.client.data.GalleryInfo
import com.hippo.ehviewer.download.DownloadManager
import com.hippo.ehviewer.spider.SpiderInfo
import com.hippo.ehviewer.spider.SpiderQueen
import com.hippo.ehviewer.ui.scene.BaseScene
import com.hippo.unifile.UniFile
import com.hippo.util.ExceptionUtils
import com.hippo.yorozuya.IOUtils
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import java.io.IOException
import java.io.InputStream
import java.util.Collections

class RestoreDownloadPreference constructor(
    context: Context, attrs: AttributeSet?
) : TaskPreference(context, attrs) {
    override fun onCreateTask(): Task {
        return RestoreTask(context)
    }

    private class RestoreTask(context: Context) : Task(context) {
        private val mManager: DownloadManager = downloadManager
        private val mHttpClient: OkHttpClient = okHttpClient
        private fun getRestoreItem(file: UniFile?): RestoreItem? {
            if (null == file || !file.isDirectory) {
                return null
            }
            val siFile = file.findFile(SpiderQueen.SPIDER_INFO_FILENAME) ?: return null
            var `is`: InputStream? = null
            return try {
                `is` = siFile.openInputStream()
                val spiderInfo = SpiderInfo.read(`is`) ?: return null
                val gid = spiderInfo.gid
                if (mManager.containDownloadInfo(gid)) {
                    return null
                }
                val token = spiderInfo.token
                val restoreItem = RestoreItem()
                restoreItem.gid = gid
                restoreItem.token = token
                restoreItem.dirname = file.name
                restoreItem
            } catch (e: IOException) {
                null
            } finally {
                IOUtils.closeQuietly(`is`)
            }
        }

        override fun doInBackground(vararg params: Void): Any? {
            val dir = Settings.getDownloadLocation() ?: return null
            val restoreItemList: MutableList<RestoreItem> = ArrayList()
            val files = dir.listFiles() ?: return null
            for (file in files) {
                val restoreItem = getRestoreItem(file)
                if (null != restoreItem) {
                    restoreItemList.add(restoreItem)
                }
            }
            return if (0 == restoreItemList.size) {
                Collections.EMPTY_LIST
            } else try {
                runBlocking {
                    fillGalleryListByApi(
                        mHttpClient,
                        ArrayList(restoreItemList),
                        EhUrl.getReferer()
                    )
                }
            } catch (e: Throwable) {
                ExceptionUtils.throwIfFatal(e)
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(o: Any) {
            if (o !is List<*>) {
                mActivity.showTip(R.string.settings_download_restore_failed, BaseScene.LENGTH_SHORT)
            } else {
                val list = o as List<RestoreItem>
                if (list.isEmpty()) {
                    mActivity.showTip(
                        R.string.settings_download_restore_not_found,
                        BaseScene.LENGTH_SHORT
                    )
                } else {
                    var count = 0
                    var i = 0
                    val n = list.size
                    while (i < n) {
                        val item = list[i]
                        // Avoid failed gallery info
                        if (null != item.title) {
                            // Put to download
                            mManager.addDownload(item, null)
                            // Put download dir to DB
                            EhDB.putDownloadDirname(item.gid, item.dirname)
                            count++
                        }
                        i++
                    }
                    showTip(
                        mActivity.getString(R.string.settings_download_restore_successfully, count),
                        BaseScene.LENGTH_SHORT
                    )
                    val preference: Preference? = preference
                    if (null != preference) {
                        val context = preference.context
                        if (context is Activity) {
                            context.setResult(Activity.RESULT_OK)
                        }
                    }
                }
            }
            super.onPostExecute(o)
        }
    }

    private class RestoreItem : GalleryInfo {
        var dirname: String? = null

        constructor()
        constructor(`in`: Parcel) : super(`in`) {
            dirname = `in`.readString()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(dirname)
        }

        companion object {
            @JvmField
            val CREATOR: Creator<RestoreItem> = object : Creator<RestoreItem> {
                override fun createFromParcel(source: Parcel): RestoreItem {
                    return RestoreItem(source)
                }

                override fun newArray(size: Int): Array<RestoreItem?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}