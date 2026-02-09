package com.step.web.runtime.core.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author : Sun
 * @date : 2023/3/10  14:45
 */
@ProxyGen
public interface HotAsyncService {

    void closeHot(String host, String port, Handler<AsyncResult<Boolean>> resultHandler);
}
