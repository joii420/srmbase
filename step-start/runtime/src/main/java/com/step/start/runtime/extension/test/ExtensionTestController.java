package com.step.start.runtime.extension.test;

import com.step.api.runtime.common.R;
import com.step.start.runtime.extension.ExtendLoader;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.util.Map;

/**
 * @author : Sun
 * @date : 2023/8/28  14:52
 */
@Path("extend/test")
public class ExtensionTestController {

    private ExtendLoader extendClass;

    public ExtensionTestController() {
        this.extendClass = new ExtendLoader("D:\\test\\");
    }

    @Path("test1")
    @GET
    public R<String> test1(@QueryParam("aaa") String aaa) {
        extendClass.execute("extend.java.Test1", Map.of("aaa", aaa));
        return R.data("");
    }

    @Path("reload")
    @GET
    public R<String> reload() {
        extendClass.reload();
        extendClass.execute("extend.java.Test1", Map.of("aaa", "bbbb"));
        return R.data("");
    }
}
