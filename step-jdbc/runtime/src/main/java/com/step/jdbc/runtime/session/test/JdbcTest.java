package com.step.jdbc.runtime.session.test;

import cn.hutool.core.thread.ThreadUtil;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.param.JdbcParam;
import com.step.jdbc.runtime.session.JdbcSession;
import com.step.jdbc.runtime.session.JdbcSessionBuilder;
import com.step.jdbc.runtime.session.JdbcSessionPool;
import com.step.jdbc.runtime.session.support.UpdateCondition;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author : Sun
 * @date : 2024/4/23  19:45
 */
public class JdbcTest {

    public static void main(String[] args) {
        JdbcParam postgres = JdbcParam.postgres("172.16.18.40", 5431, "pmsprd", "pmsdemo", "pmsdemo");
        JdbcOption jdbcOption = new JdbcOption();
//        jdbcOption.setTransaction(true);
//        jdbcOption.setLog(true);
//        JdbcSession jdbcSession = JdbcSessionBuilder.buildSession(postgres, jdbcOption);
//        jdbcSession.executeUnCommit("update t1 set omsaaent  = 'WZZZ' where omsaa01 = '2205024'");
//        try {
//            jdbcSession.executeUnCommit("update t1 set omsaaentaa  = 'WZZZ' where omsaa01 = '2205024'");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        jdbcSession.executeUnCommit("update t1 set omsaastus  = 'N' where omsaa01 = '2205024'");
//        jdbcSession.commit();
//        JdbcSessionPool jdbcSessionPool = JdbcSessionBuilder.buildSessionPool(postgres, jdbcOption, 5);
//        String testSql = "select * from test_insert where id = '14'";
//        for (int i = 0; i < 100; i++) {
//            jdbcSessionPool.useSession(session -> {
//                session.queryUnClose(testSql);
//            });
//        }
//        String testSql2 = "select * from test_insert where id = '%s' for update nowait";
//        for (int i = 14; i < 17; i++) {
//            final int id = i;
//            jdbcSessionPool.useSession(session -> {
//                session.queryUnCommit(String.format(testSql2, id));
//            });
//        }
        JdbcSession jdbcSession = JdbcSessionBuilder.buildSession(postgres);
        TestInsert testInsert = new TestInsert();
        testInsert.setId(14L);
        testInsert.setAge("14");
        testInsert.setTtt(LocalDateTime.now());
        TestInsert testInsert2 = new TestInsert();
        testInsert2.setId(15L);
        testInsert2.setName("jjj");
        testInsert2.setAge("15");
        testInsert2.setTtt(LocalDateTime.now());
        jdbcSession.insertUnCommit(List.of(testInsert, testInsert2));

        jdbcSession.commit();
//
//        TestInsert testInsert3 = new TestInsert();
//        testInsert3.setId(16L);
//        testInsert3.setName("jjj");
//        testInsert3.setAge("15");
//        testInsert3.setTtt(LocalDateTime.now());
//        List<Map<String, Object>> maps = jdbcSession.executeBatchUnCommit(session -> {
//            session.insert(testInsert3);
//            return session.query("select * from test_insert");
//        });
//        TestInsert testInsert4 = new TestInsert();
//        testInsert4.setAge("update");
//        UpdateCondition<TestInsert> update1 = new UpdateCondition<>(testInsert4, testInsert3);
//        TestInsert u2 = new TestInsert();
//        u2.setName("sdfsdfasdasd");
//        UpdateCondition<TestInsert> update2 = new UpdateCondition<>(u2, testInsert2);
//        jdbcSession.updateUnClose(update1, update2);
//        List<Map<String, Object>> map2 = jdbcSession.executeBatchUnClose(session -> {
//            return session.query("select * from test_insert");
//        });
//        System.out.println("11");
    }


}
