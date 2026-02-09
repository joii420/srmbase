package com.step.jdbc.runtime.txedit.service;
//


import com.step.api.runtime.annotation.MicroServer;
import com.step.api.runtime.common.MicroRegister;
import com.step.jdbc.runtime.txedit.model.TransactionServerStatus;
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
@MicroServer(MicroRegister.CENTRAL)
public interface TxDeployAsyncService {
    /**
     * 事务服务器部署
     */
    void deployInit(TransactionServerStatus serverStatus, Handler<AsyncResult<Boolean>> resultHandler);

}

