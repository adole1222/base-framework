package com.adole.base;//package com.adole.utils;
//
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Intercepts;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Signature;
//
//import java.util.Properties;
//
//@Intercepts(value = {
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class})})
//public class SqlStatementInterceptor implements Interceptor {
//
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object[] args = invocation.getArgs();
////        MappedStatement mappedStatement = (MappedStatement) args[0];
//
//        return null;
//    }
//
//    public Object plugin(Object o) {
//        return null;
//    }
//
//    public void setProperties(Properties properties) {
//
//    }
//
//    public String getSql() {
//        String sql = "";
//
//        return "";
//    }
//}
