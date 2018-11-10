/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wugx_autils.http.net.converter;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.TypeAdapter;
import com.wugx_autils.http.net.common.BasicBean;
import com.wugx_autils.http.net.exception.NoDataExceptionException;
import com.wugx_autils.http.net.exception.ServerResponseException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import static com.wugx_autils.http.net.common.Constants.CODE_ERROR;
import static com.wugx_autils.http.net.common.Constants.CODE_SUCCESS;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, Object> {

    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        try {

            BasicBean response = (BasicBean) adapter.fromJson(value.charStream());
            if (response.code.equals(CODE_SUCCESS)) {
                if (response.getData() != null) {
                    return response.getData();
                }else{
                    return response;
                }
//                else throw new NoDataExceptionException();
            } else if (response.code.equals(CODE_ERROR)) {
                throw new ServerResponseException(response.getCode(), response.getMsg());
            } else {
                LogUtils.d("response exception>>>>" + response.code);
                // TODO: 2018/11/9 other
                throw new NoDataExceptionException();
            }

        } finally {
            value.close();
        }
    }
}
