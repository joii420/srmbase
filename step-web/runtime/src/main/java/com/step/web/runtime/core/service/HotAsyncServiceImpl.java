package com.step.web.runtime.core.service;


import com.step.api.runtime.core.BaseAsyncService;
import com.step.logger.LOGGER;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;

import jakarta.enterprise.context.ApplicationScoped;
import java.net.HttpURLConnection;
import java.net.URL;


@ApplicationScoped
public class HotAsyncServiceImpl implements HotAsyncService, BaseAsyncService {

    @Override
    public void closeHot(String host, String port, Handler<AsyncResult<Boolean>> resultHandler) {
        hotClose(host, port);
        Future.succeededFuture(true).onComplete(resultHandler);
    }

    private void hotClose(String host, String port) {
        try {
            LOGGER.info(Thread.currentThread().getName() + ":准备关闭 ================> (3)", new Object[0]);
            Thread.sleep(1000L);
            LOGGER.info("准备关闭 ================> (2)", new Object[0]);
            Thread.sleep(1000L);
            LOGGER.info("准备关闭 ================> (1)", new Object[0]);
            Thread.sleep(1000L);
            URL url = new URL(String.format("http://%s:%s/q/dev/io.quarkus.quarkus-vertx-http/tests/toggle-live-reload", host, port));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(HttpMethod.POST.name());
            con.setRequestProperty("Content-Type", "application/x-www-from-urlencoded");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            if (con.getResponseCode() == 200) {
                LOGGER.info("关闭成功 ================>", new Object[0]);
            } else {
                LOGGER.info("关闭失败 ================>", new Object[0]);
            }
        } catch (Exception var5) {
            LOGGER.error(var5, "close hot deploy failed to request!", new Object[0]);
        }
    }
}

