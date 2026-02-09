package com.step.web.runtime.model;

import com.step.api.runtime.core.BaseAsyncService;

/**
 * @author : Sun
 * @date : 2023/2/2  10:01
 */
public class Deploy {

    private BaseAsyncService asyncService;
    private Class interFace;

    public Deploy(BaseAsyncService asyncService, Class interFace) {
        this.asyncService = asyncService;
        this.interFace = interFace;
    }

    public BaseAsyncService getAsyncService() {
        return asyncService;
    }

    public Class getInterFace() {
        return interFace;
    }
}
