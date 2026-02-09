package com.step.gateway.runtime.service;
//

import com.step.api.runtime.annotation.MicroServer;
import com.step.api.runtime.common.MicroRegister;
import com.step.api.runtime.model.TokenInfo;
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
@MicroServer(MicroRegister.GATEWAY)
public interface GatewayAuthAsyncService {

    /**
     * 令牌有效性校验
     */
    void validTokenVerify(String token, Handler<AsyncResult<TokenInfo>> resultHandler);

    /**
     * 文件下载鉴权
     */
    void fileDownLoadAuthVerify(String token, String serverCode, Handler<AsyncResult<Boolean>> resultHandler);

//    /**
//     * 校验请求签名是否有效,即网关会在请求头中设置一个安全参数
//     * <p>
//     * //     * @param cryptographic 签名key
//     */
//    void httpRequestSignVerify(Handler<AsyncResult<String>> resultHandler);

    /**
     * 校验请求签名是否有效,即网关会在请求头中设置一个安全参数
     *
     * @param cryptographic 签名key
     */
    void httpRequestSignVerify(String requestKey, String cryptographic, Handler<AsyncResult<Boolean>> resultHandler);

}

