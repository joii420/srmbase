package com.step.api.runtime.exception.service;

import com.step.api.runtime.annotation.MicroServer;
import com.step.api.runtime.common.MicroRegister;
import com.step.api.runtime.exception.ResInfo;
import com.step.api.runtime.exception.Result;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

/**
 * @author : Sun
 * @date : 2023/2/1  9:10
 */
@ProxyGen
@MicroServer(MicroRegister.CENTRAL)
public interface ResultAsync {

    void getResult(ResInfo info,String lang, Handler<AsyncResult<Result>> resultHandler);

    void getResults(List<ResInfo> infoList,String lang, Handler<AsyncResult<List<Result>>> resultHandler);
}
