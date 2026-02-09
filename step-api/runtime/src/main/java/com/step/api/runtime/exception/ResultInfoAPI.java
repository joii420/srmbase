package com.step.api.runtime.exception;

import java.util.List;

public interface ResultInfoAPI {

    Result getResult(ResInfo info,String lang );

    List<Result> getResults(List<ResInfo> infoList,String lang);

}
