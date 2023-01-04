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

package com.hippo.ehviewer.client;

import com.hippo.ehviewer.Settings;

public class EhRequest {

    EhClient.Task task;
    private int mMethod;
    private Object[] mArgs;
    private EhClient.Callback mCallback;
    private EhConfig mEhConfig;
    private boolean mCancel = false;

    public int getMethod() {
        return mMethod;
    }

    public EhRequest setMethod(int method) {
        mMethod = method;
        return this;
    }

    public Object[] getArgs() {
        return mArgs;
    }

    public EhRequest setArgs(Object... args) {
        mArgs = args;
        return this;
    }

    public EhClient.Callback getCallback() {
        return mCallback;
    }

    public EhRequest setCallback(EhClient.Callback callback) {
        mCallback = callback;
        return this;
    }

    public EhConfig getEhConfig() {
        return mEhConfig != null ? mEhConfig : Settings.getEhConfig();
    }

    public EhRequest setEhConfig(EhConfig ehConfig) {
        mEhConfig = ehConfig;
        return this;
    }

    public void cancel() {
        if (!mCancel) {
            mCancel = true;
            if (task != null) {
                task.stop();
                task = null;
            }
        }
    }

    public boolean isCancelled() {
        return mCancel;
    }
}