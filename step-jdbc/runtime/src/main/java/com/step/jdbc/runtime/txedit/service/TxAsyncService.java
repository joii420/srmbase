package com.step.jdbc.runtime.txedit.service;
//


import com.step.jdbc.runtime.txedit.model.LockParam;
import com.step.jdbc.runtime.txedit.model.TxInfo;
import com.step.jdbc.runtime.txedit.model.TxR;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


/**
 * 服务类
 *
 * @author Auto
 * @date 2022-07-25T13:40:43.489
 */
@ProxyGen
public interface TxAsyncService {
    void beginEdit(LockParam txInfo, Handler<AsyncResult<TxR>> resultHandler);

    void updateData(TxInfo txInfo, Handler<AsyncResult<TxR>> resultHandler);

    void exitEdit(TxInfo txInfo, Handler<AsyncResult<TxR>> resultHandler);
}

