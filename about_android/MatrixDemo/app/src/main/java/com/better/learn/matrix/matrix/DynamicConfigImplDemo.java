/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author zhoushaotao
 * Created by zhoushaotao on 2018/10/9.
 */

package com.better.learn.matrix.matrix;

import com.tencent.mrs.plugin.IDynamicConfig;

public class DynamicConfigImplDemo implements IDynamicConfig {
    private static final String TAG = "Matrix.DynamicConfigImplDemo";

    public DynamicConfigImplDemo() {

    }

    public boolean isFPSEnable() {
        return true;
    }

    public boolean isTraceEnable() {
        return true;
    }

    public boolean isMatrixEnable() {
        return true;
    }

    @Override
    public String get(String key, String defStr) {
        //TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.
        return defStr;
    }


    @Override
    public int get(String key, int defInt) {
        //TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.
        return defInt;

    }

    @Override
    public long get(String key, long defLong) {
        //TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.
        return defLong;
    }


    @Override
    public boolean get(String key, boolean defBool) {
        //TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.

        return defBool;
    }

    @Override
    public float get(String key, float defFloat) {
        //TODO here return default value which is inside sdk, you can change it as you wish. matrix-sdk-key in class MatrixEnum.

        return defFloat;
    }

}
