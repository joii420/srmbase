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

package com.step.gateway.runtime.service;

import com.step.api.runtime.model.TokenInfo;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.serviceproxy.ServiceExceptionMessageCodec;
/*
  Generated Proxy code - DO NOT EDIT
  @author Roger the Robot
*/

@SuppressWarnings({"unchecked", "rawtypes"})
public class GatewayAuthAsyncServiceVertxEBProxy implements GatewayAuthAsyncService {
    private Vertx _vertx;
    private String _address;
    private DeliveryOptions _options;
    private boolean closed;

    public GatewayAuthAsyncServiceVertxEBProxy(Vertx vertx, String address) {
        this(vertx, address, null);
    }

    public GatewayAuthAsyncServiceVertxEBProxy(Vertx vertx, String address, DeliveryOptions options) {
        this._vertx = vertx;
        this._address = address;
        this._options = options;
        try {
            this._vertx.eventBus().registerDefaultCodec(ServiceException.class, new ServiceExceptionMessageCodec());
        } catch (IllegalStateException ex) {
        }
    }

    @Override
    public void validTokenVerify(String token, Handler<AsyncResult<TokenInfo>> resultHandler) {
        if (closed) {
            resultHandler.handle(Future.failedFuture(new IllegalStateException("Proxy is closed")));
            return;
        }
        JsonObject _json = new JsonObject();
        _json.put("token", token);

        DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
        _deliveryOptions.addHeader("action", "validTokenVerify");
        _deliveryOptions.getHeaders().set("action", "validTokenVerify");
        _vertx.eventBus().<JsonObject>request(_address, _json, _deliveryOptions, res -> {
            if (res.failed()) {
                resultHandler.handle(Future.failedFuture(res.cause()));
            } else {
                resultHandler.handle(Future.succeededFuture(res.result().body() != null ? new TokenInfo((JsonObject) res.result().body()) : null));
            }
        });
    }

    @Override
    public void fileDownLoadAuthVerify(String token, String serverCode, Handler<AsyncResult<Boolean>> resultHandler) {
        if (closed) {
            resultHandler.handle(Future.failedFuture(new IllegalStateException("Proxy is closed")));
            return;
        }
        JsonObject _json = new JsonObject();
        _json.put("token", token);
        _json.put("serverCode", serverCode);

        DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
        _deliveryOptions.addHeader("action", "fileDownLoadAuthVerify");
        _deliveryOptions.getHeaders().set("action", "fileDownLoadAuthVerify");
        _vertx.eventBus().<Boolean>request(_address, _json, _deliveryOptions, res -> {
            if (res.failed()) {
                resultHandler.handle(Future.failedFuture(res.cause()));
            } else {
                resultHandler.handle(Future.succeededFuture(res.result().body()));
            }
        });
    }
//
//    @Override
//    public void httpRequestSignVerify(Handler<AsyncResult<String>> resultHandler) {
//        if (closed) {
//            resultHandler.handle(Future.failedFuture(new IllegalStateException("Proxy is closed")));
//            return;
//        }
//        JsonObject _json = new JsonObject();
//        DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
//        _deliveryOptions.addHeader("action", "httpRequestSignVerify");
//        _deliveryOptions.getHeaders().set("action", "httpRequestSignVerify");
//        _vertx.eventBus().<String>request(_address, _json, _deliveryOptions, res -> {
//            if (res.failed()) {
//                resultHandler.handle(Future.failedFuture(res.cause()));
//            } else {
//                resultHandler.handle(Future.succeededFuture(res.result().body()));
//            }
//        });
//    }

    @Override
    public void httpRequestSignVerify(String requestKey, String cryptographic, Handler<AsyncResult<Boolean>> resultHandler) {
        if (closed) {
            resultHandler.handle(Future.failedFuture(new IllegalStateException("Proxy is closed")));
            return;
        }
        JsonObject _json = new JsonObject();
        _json.put("requestKey", requestKey);
        _json.put("cryptographic", cryptographic);
        DeliveryOptions _deliveryOptions = (_options != null) ? new DeliveryOptions(_options) : new DeliveryOptions();
        _deliveryOptions.addHeader("action", "httpRequestSignVerify");
        _deliveryOptions.getHeaders().set("action", "httpRequestSignVerify");
        _vertx.eventBus().<Boolean>request(_address, _json, _deliveryOptions, res -> {
            if (res.failed()) {
                resultHandler.handle(Future.failedFuture(res.cause()));
            } else {
                resultHandler.handle(Future.succeededFuture(res.result().body()));
            }
        });
    }
}
