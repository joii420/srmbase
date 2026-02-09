package com.step.api.runtime.camel;

import com.step.api.runtime.model.TransmitParam;

import java.util.Map;

/**
 * @author : Sun
 * @date : 2023/1/3  14:42
 */
public interface CamelAPI {

    String HttpPostToOne(String endPoint, String args);
    Object HttpPostByMapOrXml(String endPoint,Map<String,Object> map,String xml) throws Exception;

    Object HttpPostByTransmit(String endPoint, TransmitParam d) throws Exception;
}
