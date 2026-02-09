/*
* Copyright 2014 Red Hat, Inc.
*
* Red Hat licenses this file to you under the Apache License, version 2.0
* (the "License"); you may not use this file except in compliance with the
* License. You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/

package com.step.api.runtime.exception.service;

import com.step.api.runtime.exception.ResInfo;
import com.step.api.runtime.exception.Result;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;

import java.util.List;
import java.util.stream.Collectors;
/*
  Generated Proxy code - DO NOT EDIT
  @author Roger the Robot
*/

@SuppressWarnings({"unchecked", "rawtypes"})
public class ResultAsyncVertxEBProxy implements ResultAsync {
  private Vertx _vertx;
  private String _address;
  private DeliveryOptions _options;
  private boolean closed;

  public ResultAsyncVertxEBProxy(Vertx vertx, String address) {
    this(vertx, address, null);
  }

  public ResultAsyncVertxEBProxy(Vertx vertx, String address, DeliveryOptions options) {
    this._vertx = vertx;
    this._address = address;
    this._options = options;
    try {
      this._vertx.eventBus().registerDefaultCodec(ServiceException.class, new ServiceExceptionMessageCodec());
    } catch (IllegalStateException ex) {
    }
  }

  @Override
  public void getResult(ResInfo info, String lang, Handler<AsyncResult<Result>> resultHandler){
    if (closed) {
      resultHandler.handle(Future.failedFuture(new IllegalStateException("Proxy is closed")));
      return;
    }
    JsonObject _json = new JsonObject();
    _json.put("info", info != null ? info.toJson() : null);
    _json.put("lang", lang);

    DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
    _deliveryOptions.addHeader("action", "getResult");
    _deliveryOptions.getHeaders().set("action", "getResult");
    _vertx.eventBus().<JsonObject>request(_address, _json, _deliveryOptions, res -> {
      if (res.failed()) {
        resultHandler.handle(Future.failedFuture(res.cause()));
      } else {
        resultHandler.handle(Future.succeededFuture(res.result().body() != null ? new Result((JsonObject)res.result().body()) : null));
      }
    });
  }
  @Override
  public void getResults(List<ResInfo> infoList, String lang, Handler<AsyncResult<List<Result>>> resultHandler){
    if (closed) {
      resultHandler.handle(Future.failedFuture(new IllegalStateException("Proxy is closed")));
      return;
    }
    JsonObject _json = new JsonObject();
    _json.put("infoList", new JsonArray(infoList == null ? java.util.Collections.emptyList() : infoList.stream().map(v -> v != null ? v.toJson() : null).collect(Collectors.toList())));
    _json.put("lang", lang);

    DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
    _deliveryOptions.addHeader("action", "getResults");
    _deliveryOptions.getHeaders().set("action", "getResults");
    _vertx.eventBus().<JsonArray>request(_address, _json, _deliveryOptions, res -> {
      if (res.failed()) {
        resultHandler.handle(Future.failedFuture(res.cause()));
      } else {
        resultHandler.handle(Future.succeededFuture(res.result().body().stream()
          .map(v -> v != null ? new Result((JsonObject)v) : null)
          .collect(Collectors.toList())));
      }
    });
  }
}
