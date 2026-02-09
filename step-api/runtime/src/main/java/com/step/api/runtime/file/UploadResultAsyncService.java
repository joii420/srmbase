package com.step.api.runtime.file;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * description :
 *
 * @author : Joii-AutoAsyncService
 * 2022-11-04 09:34:41
 */
@ProxyGen
public interface UploadResultAsyncService {

    /**
     * 文件上传
     */
    void uploadResult(FileParam fileParam, Handler<AsyncResult<Boolean>> resultHandler);

}
