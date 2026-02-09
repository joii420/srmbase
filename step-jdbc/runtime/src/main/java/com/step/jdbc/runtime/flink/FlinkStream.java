package com.step.jdbc.runtime.flink;

import com.step.jdbc.runtime.flink.exception.FlinkException;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.CloseableIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Sun
 * @date : 2023/8/29  9:56
 */
public class FlinkStream<T> {

    private StreamExecutionEnvironment env;
    private DataStreamSource<T> dataStreamSource;
    private final static int DEFAULT_PARALLELISM = 4;
    private boolean isUsed = false;
    private boolean isClose = false;

    private FlinkStream() {

    }

    public static <T> FlinkStream<T> of(List<T> datasource) {
        FlinkStream<T> stream = new FlinkStream<>();
        try {
            stream.env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(DEFAULT_PARALLELISM);
            //主表的内容先成数据源流
            stream.dataStreamSource = stream.env.fromCollection(datasource);
            return stream;
        } catch (Exception e) {
            throw new FlinkException("Stream创建失败:" + e.getMessage());
        }
    }

    public void setParallelism(int parallelism) {
        checkClose();
        checkUsed();
        env.setParallelism(parallelism);
    }

    public DataStream<T> start() {
        checkClose();
        checkUsed();
        this.isUsed = true;
        try {
            return this.dataStreamSource.filter(o -> true);
        } catch (Exception e) {
            this.close();
            throw new FlinkException(e.getMessage());
        }
    }

    public <O> List<O> collect(DataStream<O> dataStream) {
        checkClose();
        List<O> result = new ArrayList<>();
        try {
            CloseableIterator<O> mapCloseableIterator = dataStream.executeAndCollect();
            mapCloseableIterator.forEachRemaining(result::add);
            return result;
        } catch (Exception e) {
            throw new FlinkException(e.getMessage());
        } finally {
            this.close();
        }
    }

    private void checkClose() {
        if (isClose) {
            throw new FlinkException("Env 已被关闭");
        }
    }

    private void checkUsed() {
        if (isUsed) {
            this.close();
            throw new FlinkException("Stream 只能使用一次");
        }
    }

    private synchronized void close() {
        if (!isClose) {
            this.isClose = true;
            try {
                this.env.close();
            } catch (Exception e) {
                throw new FlinkException("Env 关闭失败: " + e.getMessage());
            }
        }
    }

}
