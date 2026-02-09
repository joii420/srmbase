package com.step.camel.runtime;

import com.step.api.runtime.camel.CamelAPI;
import com.step.api.runtime.model.TransmitParam;
import jakarta.ws.rs.HttpMethod;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.POST;

import java.util.Map;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.log;

@Singleton
public class HttpPosts implements CamelAPI {

    @Inject
    FluentProducerTemplate fluentProducerTemplate;
    @Inject
    ProducerTemplate producerTemplate;
    @Inject
    ConsumerTemplate consumerTemplate;
    public static final String Point = "direct:start";

    @Override
    public String HttpPostToOne(String endPoint, String argsBodys) {
        return fluentProducerTemplate.to(endPoint)
                .to(log(Thread.currentThread().getStackTrace()[1].getMethodName()).showExchangePattern(true).showBody(true))
                .withBody(argsBodys)
                .withHeader(Exchange.HTTP_METHOD, HttpMethod.POST)
                .request(String.class);
    }

    @Override
    public Object HttpPostByMapOrXml(String endPoint, Map<String, Object> map, String xml) throws Exception {
        CamelContext context = new DefaultCamelContext();
        String flag;
        if (StringUtils.isEmpty(xml) && !ObjectUtils.isEmpty(map)) {
            flag = "1";
        } else if (!StringUtils.isEmpty(xml) && ObjectUtils.isEmpty(map)) {
            flag = "2";
        } else {
            throw new Exception("no body");
        }
        Object o = null;
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(Point)
                            .to(log("RequestVo-Camel-log").showExchangePattern(true).showBody(true))
                            .process(new Processor() {
                                @Override
                                public void process(Exchange exchange) throws Exception {
                                    exchange.setPattern(ExchangePattern.InOut);
                                    Object body = exchange.getIn().getBody();
                                    exchange.getOut().setBody(body);
                                    exchange.getMessage().setHeader("flag", flag);
                                }
                            }).choice().when(header("flag").isEqualTo("1"))
                            .marshal().json(JsonLibrary.Jackson)
                            .to(endPoint)
                            .unmarshal().json().process(new Processor() {
                                @Override
                                public void process(Exchange exchange) throws Exception {
                                    Object body = exchange.getIn().getBody();
                                }
                            })
                            .to(log("Respone-Camel-log").showExchangePattern(true).showBody(true))
                            .when(header("flag").isEqualTo(2))
                            .marshal().jaxb()
                            .to(endPoint)
                            .unmarshal().jaxb()
                            .to(log("Respone-Camel-log").showExchangePattern(true).showBody(true))
                            .endChoice();
                }
            });
            context.start();
            ProducerTemplate pTemplate = context.createProducerTemplate();
            o = pTemplate.sendBody(Point, ExchangePattern.InOut, map);
            context.close();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public Object HttpPostByTransmit(String endPoint, TransmitParam d) throws Exception {
        CamelContext context = new DefaultCamelContext();
        String flag = "1";
        Object o = null;
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(Point)
                            .to(log("RequestVo-Camel-log").showExchangePattern(true).showBody(true))
                            .process(new Processor() {
                                @Override
                                public void process(Exchange exchange) throws Exception {
                                    exchange.setPattern(ExchangePattern.InOut);
                                    Object body = exchange.getIn().getBody();
                                    exchange.getOut().setBody(body);
                                    exchange.getMessage().setHeader("flag", flag);
                                }
                            })
                            .marshal().json(JsonLibrary.Jackson)
                            .to(endPoint).unmarshal().json();
                }
            });
            context.start();
            ProducerTemplate pTemplate = context.createProducerTemplate();
            o = pTemplate.sendBody(Point, ExchangePattern.InOut, d);
            context.close();
            return o;
        } catch (Exception e) {
            Exception exception = ((CamelExecutionException) e).getExchange().getException();
            String responseBody = ((HttpOperationFailedException) exception).getResponseBody();
            return "Exception:" + responseBody;
        } finally {
            context.close();
        }
    }


}
