//package com.zsh.task.cache;
//
//import java.util.List;
//import java.util.Map;
//
//interface Cache<T> {
//
//    Long incr(String var1);
//
//    Long incr(String var1, Long var2);
//
//    Boolean setCacheStr(String var1, String var2);
//
//    Boolean setCacheStr(String var1, String var2, Long var3);
//
//    Boolean setCache(String var1, T var2);
//
//    Boolean setCache(String var1, T var2, Long var3);
//
//    T getCache(String var1, Class<T> var2);
//
//    List<T> getCache(Class<T> var1, String... var2);
//
//    Long hashLen(String var1);
//
//    boolean hashExist(String var1, String var2);
//
//    int hashDel(String var1, String var2);
//
//    boolean hashSet(String var1, String var2, T var3);
//
//    void hashMSet(String var1, Map<String, T> var2);
//
//    void hashMSetStr(String var1, Map<String, String> var2);
//
//    T hashGet(String var1, String var2, Class<T> var3);
//
//    String hashGetStr(String var1, String var2);
//
//    Map<String, T> hashGetAll(String var1, Class<T> var2);
//
//    Map<String, String> hashGetAll(String var1);
//
//    long listRpush(String var1, List<T> var2);
//
//    T listBrpopLpush(String var1, String var2, int var3, Class<T> var4);
//
//    T listRpop(String var1, Class<T> var2);
//
//    List<T> listAll(String var1, Class<T> var2);
//
//    List<T> listLrange(String var1, long var2, long var4, Class<T> var6);
//
//    T listLindex(String var1, long var2, Class<T> var4);
//
//    long setSadd(String var1, String var2);
//
//    long setSadd(String var1, String var2, Long var3);
//
//    long setSadd(String var1, List<String> var2);
//
//    <T> long setSadd(String var1, List<T> var2, Long var3);
//
//    <T> long setSrem(String var1, T var2);
//
//    <T> long setSrem(String var1, List<T> var2);
//
//    long setScard(String var1);
//
//    <T> List<T> setSmembers(String var1, Class<T> var2);
//
//    <T> boolean setSisMember(String var1, T var2);
//
//    <T2> boolean zSetAdd(String var1, double var2, T2 var4, Long var5);
//
//    <T2> Long zSetAdd(String var1, Map<T2, Double> var2, Long var3);
//
//    <T2> Long zSetSize(String var1);
//
//    <T2> List<T2> zSetRange(String var1, long var2, long var4, Class<T2> var6);
//
//    boolean exists(String var1);
//
//    Boolean delCache(String var1);
//
//    Boolean delCache(List<String> var1);
//
//    Boolean delUnlink(String var1);
//
//    Boolean delUnlink(List<String> var1);
//
//    Boolean flushAll();
//
//    Boolean expire(String var1, long var2);
//
//    List<String> keys(String var1);
//
//    long ttl(String var1);
//
//    String getKey(String var1);
//}
