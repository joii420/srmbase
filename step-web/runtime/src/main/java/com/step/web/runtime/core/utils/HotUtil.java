package com.step.web.runtime.core.utils;

import com.step.api.runtime.utils.HostConfig;
import com.step.logger.LOGGER;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @author : Sun
 * @date : 2023/1/14  13:43
 */
@Singleton
public class HotUtil {

    @Inject
    HostConfig hostConfig;
    private boolean hotDeploy = false;
    private int hotDeployCount = 1;
    @Inject
    Vertx vertx;

    public void hotDeploy() {
        if (!this.hotDeploy && 1 == hotDeployCount++) {
            String host = hostConfig.getHost();
            String port = hostConfig.getPort();
            this.hotDeploy = true;
            LOGGER.info("%s:%s closing hot deploy !!!", host, port);
//            new Thread(() -> {
                hotClose(host, port);
//            }).start();
        }
    }

    private void hotClose(String host, String port) {
        try {
            Thread.sleep(1000);
            LOGGER.info("准备关闭 ================> (3)");
            Thread.sleep(1000);
            LOGGER.info("准备关闭 ================> (2)");
            Thread.sleep(1000);
            LOGGER.info("准备关闭 ================> (1)");
            WebClient webClient = WebClient.create(vertx);
            webClient.post(Integer.parseInt(port), host, "/q/dev/io.quarkus.quarkus-vertx-http/tests/toggle-live-reload").send().onSuccess(x->{
                LOGGER.info("关闭热部署成功");
            });
//            URL url = new URL(String.format("http://%s:%s/q/dev/io.quarkus.quarkus-vertx-http/tests/toggle-live-reload", host, port));
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod(HttpMethod.POST.name());
//            con.setRequestProperty("Content-Type", "application/x-www-from-urlencoded");
//            con.setDoInput(true);
//            con.setDoOutput(true);
//            con.setUseCaches(false);
//            if (con.getResponseCode() == 200) {
//                LOGGER.info("关闭成功 ================>");
//            } else {
//                LOGGER.info("关闭失败 ================>");
//            }
        } catch (Exception e) {
            LOGGER.error(e, "close hot deploy failed to request!");
        }
    }
}
