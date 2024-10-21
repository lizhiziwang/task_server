//package com.zsh.task.cache;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.core.Cursor;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ScanOptions;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.Resource;
//import java.io.Serializable;
//import java.util.*;
//
//public abstract class BaseCache<T> implements Cache<T> {
//    private static final Logger log = LoggerFactory.getLogger(BaseCache.class);
//    protected int index;
//    private static int BATCH_SIZE = 600;
//    @Resource
//    protected RedisTemplate<String, Serializable> redisTemplate;
//    public static final int MAX_BYTE_SIZE = 22000;
//
//    public BaseCache(int index) {
//        this.index = index;
//    }
//
//    public BaseCache() {
//    }
//
//    public Long incr(String key) {
//        return this.incr(key, (Long)null);
//    }
//
//    public Long incr(String key, final Long expires) {
//        final String cacheKey = this.getKey(key);
//        Long curIncr = (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                byte[] kByte = BaseCache.this.redisTemplate.getStringSerializer().serialize(cacheKey);
//                Long curIn = connection.incr(kByte);
//                if (null != expires) {
//                    connection.expire(kByte, expires);
//                }
//
//                return curIn;
//            }
//        });
//        return curIncr;
//    }
//
//    public Boolean setCacheStr(String key, String value) {
//        return this.setCacheStr(key, value, (Long)null);
//    }
//
//    public Boolean setCacheStr(String key, final String value, final Long expires) {
//        final String cacheKey = this.getKey(key);
//        Boolean result = (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                byte[] kByte = BaseCache.this.redisTemplate.getStringSerializer().serialize(cacheKey);
//                byte[] vByte = BaseCache.this.redisTemplate.getStringSerializer().serialize(value);
//                return null == expires ? connection.set(kByte, vByte) : connection.setEx(kByte, expires, vByte);
//            }
//        });
//        return result;
//    }
//
//    public Boolean setCache(String key, T value) {
//        return this.setCache(key, value, (Long)null);
//    }
//
//    public Boolean setCache(String key, final T value, final Long expires) {
//        final String cacheKey = this.getKey(key);
//        Boolean result = (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                byte[] kByte = BaseCache.this.redisTemplate.getStringSerializer().serialize(cacheKey);
//                byte[] vByte = BaseCache.this.redisTemplate.getStringSerializer().serialize(JSON.toJSONString(value, new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue}));
//                return null == expires ? connection.set(kByte, vByte) : connection.setEx(kByte, expires, vByte);
//            }
//        });
//        return result;
//    }
//
//    public String getCache(String key) {
//        if (StringUtils.isBlank(key)) {
//            return null;
//        } else {
//            final String cacheKey = this.getKey(key);
//            String result = (String)this.redisTemplate.execute(new RedisCallback<String>() {
//                public String doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] k = serializer.serialize(cacheKey);
//                    byte[] val = connection.get(k);
//                    if (val == null) {
//                        return null;
//                    } else {
//                        String jsonStr = (String)serializer.deserialize(val);
//                        return jsonStr;
//                    }
//                }
//            });
//            return result;
//        }
//    }
//
//    public T getCache(String key, final Class<T> c) {
//        final String cacheKey = this.getKey(key);
//        T result = this.redisTemplate.execute(new RedisCallback<T>() {
//            public T doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] k = serializer.serialize(cacheKey);
//                byte[] val = connection.get(k);
//                if (val == null) {
//                    return null;
//                } else {
//                    String jsonStr = (String)serializer.deserialize(val);
//                    return JSON.parseObject(jsonStr, c);
//                }
//            }
//        });
//        return result;
//    }
//
//    public List<T> getCache(final Class<T> c, String... keys) {
//        if (c != null && keys != null && keys.length > 0) {
//            final String[] cacheKeys = new String[keys.length];
//
//            for(int i = 0; i < cacheKeys.length; ++i) {
//                cacheKeys[i] = this.getKey(keys[i]);
//            }
//
//            List<T> result = (List)this.redisTemplate.execute(new RedisCallback<List<T>>() {
//                public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[][] bKeys = new byte[cacheKeys.length][];
//
//                    for(int ix = 0; ix < cacheKeys.length; ++ix) {
//                        bKeys[ix] = serializer.serialize(cacheKeys[ix]);
//                    }
//
//                    List<byte[]> bValList = connection.mGet(bKeys);
//                    if (bValList != null && !bValList.isEmpty()) {
//                        List<T> l = new ArrayList();
//
//                        for(int i = 0; i < bValList.size(); ++i) {
//                            byte[] bval = (byte[])bValList.get(i);
//                            if (bval != null && bval.length != 0) {
//                                String jsonStr = (String)serializer.deserialize(bval);
//                                l.add(JSON.parseObject(jsonStr, c));
//                            }
//                        }
//
//                        return l;
//                    } else {
//                        return null;
//                    }
//                }
//            });
//            return result;
//        } else {
//            return null;
//        }
//    }
//
//    public Boolean setBitYes(String key, long offset) {
//        return this.setBitYes(key, offset, (Long)null);
//    }
//
//    public Boolean setBitYes(String key, final long offset, final Long expires) {
//        if (StringUtils.isBlank(key)) {
//            return false;
//        } else {
//            final String cacheKey = this.getKey(key);
//            Boolean result = (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                    byte[] kByte = BaseCache.this.redisTemplate.getStringSerializer().serialize(cacheKey);
//                    Boolean result = connection.setBit(kByte, offset, true);
//                    if (null != expires) {
//                        connection.expire(kByte, expires);
//                    }
//
//                    return result;
//                }
//            });
//            return result;
//        }
//    }
//
//    public Boolean setBitNo(String key, long offset) {
//        return this.setBitNo(key, offset, (Long)null);
//    }
//
//    public Boolean setBitNo(String key, final long offset, final Long expires) {
//        if (StringUtils.isBlank(key)) {
//            return false;
//        } else {
//            final String cacheKey = this.getKey(key);
//            Boolean result = (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                    byte[] kByte = BaseCache.this.redisTemplate.getStringSerializer().serialize(cacheKey);
//                    Boolean result = connection.setBit(kByte, offset, false);
//                    if (null != expires) {
//                        connection.expire(kByte, expires);
//                    }
//
//                    return result;
//                }
//            });
//            return result;
//        }
//    }
//
//    public Long hashIncrBy(String key, String field, Long increment) {
//        return this.hashIncrBy(key, field, increment, (Long)null);
//    }
//
//    public Long hashIncrBy(String key, final String field, final Long increment, final Long expires) {
//        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(field) && null != increment) {
//            final String cacheKey = this.getKey(key);
//            return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//                public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] kByte = serializer.serialize(cacheKey);
//                    byte[] fByte = serializer.serialize(field);
//                    Long result = connection.hIncrBy(kByte, fByte, increment);
//                    if (null != expires) {
//                        connection.expire(kByte, expires);
//                    }
//
//                    return result;
//                }
//            });
//        } else {
//            return 0L;
//        }
//    }
//
//    public Long hashLen(String key) {
//        if (StringUtils.isBlank(key)) {
//            return null;
//        } else {
//            final String cacheKey = this.getKey(key);
//            Long result = (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//                public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] k = serializer.serialize(cacheKey);
//                    return connection.hLen(k);
//                }
//            });
//            return result;
//        }
//    }
//
//    public boolean hashExist(String key, final String field) {
//        final String cacheKey = this.getKey(key);
//        return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[] bField = serializer.serialize(field);
//                return connection.hExists(bKey, bField);
//            }
//        });
//    }
//
//    public int hashDel(String key, final String field) {
//        final String cacheKey = this.getKey(key);
//        Long result = (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[] bField = serializer.serialize(field);
//                return connection.hDel(bKey, new byte[][]{bField});
//            }
//        });
//        return result.intValue();
//    }
//
//    public boolean hashSet(String key, String field, T value) {
//        return this.hashSet(key, field, value, (Long)null);
//    }
//
//    public boolean hashSet(String key, final String field, final T value, final Long expires) {
//        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(field) && null != value) {
//            final String cacheKey = this.getKey(key);
//            return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] kByte = serializer.serialize(cacheKey);
//                    byte[] fByte = serializer.serialize(field);
//                    byte[] vByte = serializer.serialize(JSON.toJSONString(value));
//                    boolean result = connection.hSet(kByte, fByte, vByte);
//                    if (null != expires) {
//                        connection.expire(kByte, expires);
//                    }
//
//                    return result;
//                }
//            });
//        } else {
//            return false;
//        }
//    }
//
//    public void hashMSet(String key, Map<String, T> map) {
//        this.hashMSet(key, map, (Long)null);
//    }
//
//    public void hashMSet(String key, Map<String, T> map, Long expires) {
//        this.hashMSet(key, map, expires, 500);
//    }
//
//    public void hashMSet(String key, final Map<String, T> map, final Long expires, int batchSize) {
//        if (!StringUtils.isBlank(key) && null != map && !map.isEmpty()) {
//            if (batchSize < 0) {
//                batchSize = 5;
//            }
//
//            final String cacheKey = this.getKey(key);
//            int finalBatchSize = batchSize;
//            this.redisTemplate.execute(new RedisCallback<Boolean>() {
//                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] kByte = serializer.serialize(cacheKey);
//                    int i = 1;
//                    int size = 1;
//                    Map<byte[], byte[]> hashes = new HashMap(finalBatchSize);
//                    Set<Map.Entry<String, T>> entrySet = map.entrySet();
//                    String entryValue = null;
//                    Iterator<Map.Entry<String, T>> it = entrySet.iterator();
//
//                    while(true) {
//                        do {
//                            if (!it.hasNext()) {
//                                BaseCache.log.debug("Redis提交数据大小{}", hashes.size());
//                                if (hashes.size() > 0) {
//                                    connection.hMSet(kByte, hashes);
//                                }
//
//                                if (null != expires) {
//                                    connection.expire(kByte, expires);
//                                }
//
//                                return true;
//                            }
//
//                            Map.Entry<String, T> entry = (Map.Entry)it.next();
//                            byte[] keyMap = serializer.serialize(entry.getKey());
//                            if (entry.getValue() instanceof String) {
//                                entryValue = (String)entry.getValue();
//                            } else {
//                                entryValue = JSON.toJSONString(entry.getValue());
//                            }
//
//                            byte[] valueMap = serializer.serialize(entryValue);
//                            hashes.put(keyMap, valueMap);
//                            size += valueMap.length;
//                            ++i;
//                            BaseCache.log.debug("Redis提交数据大小{}", ((byte[])hashes.get(keyMap)).length);
//                        } while(i != finalBatchSize && size <= 22000);
//
//                        connection.hMSet(kByte, hashes);
//                        hashes.clear();
//                        i = 1;
//                        size = 0;
//                    }
//                }
//            });
//        }
//    }
//
//    public void hashMSetStr(String key, Map<String, String> map) {
//        this.hashMSetStr(key, map, (Long)null);
//    }
//
//    public void hashMSetStr(String key, final Map<String, String> map, final Long expires) {
//        if (!StringUtils.isBlank(key) && null != map && !map.isEmpty()) {
//            final String cacheKey = this.getKey(key);
//            this.redisTemplate.execute(new RedisCallback<Boolean>() {
//                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] kByte = serializer.serialize(cacheKey);
//                    Map<byte[], byte[]> hashes = new HashMap(map.size());
//                    Set<Map.Entry<String, String>> entrySet = map.entrySet();
//                    Iterator<Map.Entry<String, String>> it = entrySet.iterator();
//
//                    while(it.hasNext()) {
//                        Map.Entry<String, String> entry = (Map.Entry)it.next();
//                        byte[] keyMap = serializer.serialize(entry.getKey());
//                        byte[] valueMap = serializer.serialize(entry.getValue());
//                        hashes.put(keyMap, valueMap);
//                    }
//
//                    connection.hMSet(kByte, hashes);
//                    if (null != expires) {
//                        connection.expire(kByte, expires);
//                    }
//
//                    return true;
//                }
//            });
//        }
//    }
//
//    public T hashGet(String key, final String field, final Class<T> c) {
//        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(field)) {
//            final String cacheKey = this.getKey(key);
//            T result = this.redisTemplate.execute(new RedisCallback<T>() {
//                public T doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] k = serializer.serialize(cacheKey);
//                    byte[] f = serializer.serialize(field);
//                    byte[] v = connection.hGet(k, f);
//                    if (v == null) {
//                        return null;
//                    } else {
//                        String jsonStr = (String)serializer.deserialize(v);
//                        return JSON.parseObject(jsonStr, c);
//                    }
//                }
//            });
//            return result;
//        } else {
//            return null;
//        }
//    }
//
//    public String hashGetStr(String key, final String field) {
//        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(field)) {
//            final String cacheKey = this.getKey(key);
//            String result = (String)this.redisTemplate.execute(new RedisCallback<String>() {
//                public String doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] k = serializer.serialize(cacheKey);
//                    byte[] f = serializer.serialize(field);
//                    byte[] v = connection.hGet(k, f);
//                    if (v == null) {
//                        return null;
//                    } else {
//                        String str = (String)serializer.deserialize(v);
//                        return str;
//                    }
//                }
//            });
//            return result;
//        } else {
//            return null;
//        }
//    }
//
//    public Map<String, T> hashGetAll(String key, final Class<T> c) {
//        if (StringUtils.isBlank(key)) {
//            return null;
//        } else {
//            final String cacheKey = this.getKey(key);
//            Map<String, T> result = (Map)this.redisTemplate.execute(new RedisCallback<Map<String, T>>() {
//                public Map<String, T> doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] k = serializer.serialize(cacheKey);
//                    Cursor<Map.Entry<byte[], byte[]>> entryCursor = connection.hScan(k, ScanOptions.scanOptions().match("*").build());
//                    if (entryCursor == null) {
//                        return null;
//                    } else {
//                        Map<String, T> map = new HashMap();
//
//                        while(entryCursor.hasNext()) {
//                            Map.Entry<byte[], byte[]> entry = (Map.Entry)entryCursor.next();
//                            String mapKey = (String)serializer.deserialize((byte[])entry.getKey());
//                            T mapValue = JSON.parseObject((String)serializer.deserialize((byte[])entry.getValue()), c);
//                            map.put(mapKey, mapValue);
//                        }
//
//                        return map;
//                    }
//                }
//            });
//            return result;
//        }
//    }
//
//    public Map<String, String> hashGetAll(String key) {
//        if (StringUtils.isBlank(key)) {
//            return null;
//        } else {
//            final String cacheKey = this.getKey(key);
//            Map<String, String> result = (Map)this.redisTemplate.execute(new RedisCallback<Map<String, String>>() {
//                public Map<String, String> doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] k = serializer.serialize(cacheKey);
//                    Cursor<Map.Entry<byte[], byte[]>> entryCursor = connection.hScan(k, ScanOptions.scanOptions().match("*").build());
//                    if (entryCursor == null) {
//                        return null;
//                    } else {
//                        Map<String, String> map = new HashMap();
//
//                        while(entryCursor.hasNext()) {
//                            Map.Entry<byte[], byte[]> entry = (Map.Entry)entryCursor.next();
//                            String mapKey = (String)serializer.deserialize((byte[])entry.getKey());
//                            map.put(mapKey, serializer.deserialize((byte[])entry.getValue()));
//                        }
//
//                        return map;
//                    }
//                }
//            });
//            return result;
//        }
//    }
//
//    public long listRpush(String key, final T value) {
//        if (value == null) {
//            return 0L;
//        } else {
//            final String cacheKey = this.getKey(key);
//            return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//                public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] bKey = serializer.serialize(cacheKey);
//                    byte[][] bVal = new byte[][]{serializer.serialize(JSON.toJSONString(value))};
//                    return connection.rPush(bKey, bVal);
//                }
//            });
//        }
//    }
//
//    public long listRpush(String key, final List<T> values) {
//        if (CollectionUtils.isEmpty(values)) {
//            return 0L;
//        } else {
//            final String cacheKey = this.getKey(key);
//            long l = 0L;
//
//            try {
//                l = (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//                    public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                        RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                        byte[] bKey = serializer.serialize(cacheKey);
//                        if (values.size() == 1) {
//                            byte[][] bValx = new byte[][]{serializer.serialize(JSON.toJSONString(values.get(0)))};
//                            return connection.rPush(bKey, bValx);
//                        } else {
//                            long result = 0L;
//                            int i = 1;
//
//                            List data;
//                            byte[][] bVal;
//                            int j;
//                            for(data = null; i * BaseCache.BATCH_SIZE <= values.size(); ++i) {
//                                data = values.subList((i - 1) * BaseCache.BATCH_SIZE, i * BaseCache.BATCH_SIZE);
//                                bVal = new byte[data.size()][];
//
//                                for(j = 0; j < data.size(); ++j) {
//                                    bVal[j] = serializer.serialize(JSON.toJSONString(data.get(j)));
//                                }
//
//                                result += connection.rPush(bKey, bVal);
//                            }
//
//                            data = values.subList((i - 1) * BaseCache.BATCH_SIZE, values.size());
//                            if (data != null && !data.isEmpty()) {
//                                bVal = new byte[data.size()][];
//
//                                for(j = 0; j < data.size(); ++j) {
//                                    bVal[j] = serializer.serialize(JSON.toJSONString(data.get(j)));
//                                }
//
//                                result += connection.rPush(bKey, bVal);
//                            }
//
//                            return result;
//                        }
//                    }
//                });
//            } catch (Exception var7) {
//                log.warn("KEY[{}]listRpush操作异常{}", key, var7);
//            }
//
//            return l;
//        }
//    }
//
//    public T listBrpopLpush(String key, final String tempKey, final int timeout, final Class<T> c) {
//        final String cacheKey = this.getKey(key);
//        return this.redisTemplate.execute(new RedisCallback<T>() {
//            public T doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[] tempKeyx = serializer.serialize(tempKey);
//                byte[] bVal = connection.bRPopLPush(timeout, bKey, tempKeyx);
//                return bVal != null && bVal.length > 0 ? JSON.parseObject((String)serializer.deserialize(bVal), c) : null;
//            }
//        });
//    }
//
//    public Boolean listLpush(String key, T value) {
//        return this.listLpush(key, value, (Long)null);
//    }
//
//    public Boolean listLpush(String key, final T value, final Long expires) {
//        final String cacheKey = this.getKey(key);
//        return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[] bValue = serializer.serialize(JSONObject.toJSONString(value));
//                connection.lPush(bKey, new byte[][]{bValue});
//                if (null != expires) {
//                    connection.expire(bKey, expires);
//                }
//
//                return true;
//            }
//        });
//    }
//
//    public T listRpop(String key, final Class<T> c) {
//        final String cacheKey = this.getKey(key);
//        return this.redisTemplate.execute(new RedisCallback<T>() {
//            public T doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[] bVal = connection.rPop(bKey);
//                return bVal != null && bVal.length > 0 ? JSON.parseObject((String)serializer.deserialize(bVal), c) : null;
//            }
//        });
//    }
//
//    public Long listLlen(String key) {
//        final String cacheKey = this.getKey(key);
//        return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                return connection.lLen(bKey);
//            }
//        });
//    }
//
//    public List<T> listAll(String key, final Class<T> c) {
//        final String cacheKey = this.getKey(key);
//        List<T> result = (List)this.redisTemplate.execute(new RedisCallback<List<T>>() {
//            public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] k = serializer.serialize(cacheKey);
//                List<byte[]> val = connection.lRange(k, 0L, -1L);
//                if (val != null && val.size() > 0) {
//                    List<T> jsonList = new ArrayList();
//                    Iterator var6 = val.iterator();
//
//                    while(var6.hasNext()) {
//                        byte[] v = (byte[])var6.next();
//                        String jsonStr = (String)serializer.deserialize(v);
//                        jsonList.add(JSON.parseObject(jsonStr, c));
//                    }
//
//                    return jsonList;
//                } else {
//                    return null;
//                }
//            }
//        });
//        return result;
//    }
//
//    public List<T> listLrange(String key, final long s, final long e, final Class<T> c) {
//        final String cacheKey = this.getKey(key);
//        List<T> result = (List)this.redisTemplate.execute(new RedisCallback<List<T>>() {
//            public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] k = serializer.serialize(cacheKey);
//                List<byte[]> val = connection.lRange(k, s, e);
//                if (val != null && val.size() > 0) {
//                    List<T> jsonList = new ArrayList();
//                    Iterator var6 = val.iterator();
//
//                    while(var6.hasNext()) {
//                        byte[] v = (byte[])var6.next();
//                        String jsonStr = (String)serializer.deserialize(v);
//                        jsonList.add(JSON.parseObject(jsonStr, c));
//                    }
//
//                    return jsonList;
//                } else {
//                    return null;
//                }
//            }
//        });
//        return result;
//    }
//
//    public T listLindex(String key, final long i, final Class<T> c) {
//        final String cacheKey = this.getKey(key);
//        return this.redisTemplate.execute(new RedisCallback<T>() {
//            public T doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] k = serializer.serialize(cacheKey);
//                byte[] val = connection.lIndex(k, i);
//                if (val == null) {
//                    return null;
//                } else {
//                    String jsonStr = (String)serializer.deserialize(val);
//                    return JSON.parseObject(jsonStr, c);
//                }
//            }
//        });
//    }
//
//    public long setSadd(String key, String value) {
//        return this.setSadd(key, (String)value, (Long)null);
//    }
//
//    public long setSadd(String key, String value, Long expires) {
//        if (StringUtils.isBlank(value)) {
//            return 0L;
//        } else {
//            List<String> sadds = new ArrayList();
//            sadds.add(value);
//            return this.setSadd(key, (List)sadds, expires);
//        }
//    }
//
//    public long setSadd(String key, List<String> values) {
//        return this.setSadd(key, (List)values, (Long)null);
//    }
//
//    public <T> long setSadd(String key, final List<T> values, final Long expires) {
//        final String cacheKey = this.getKey(key);
//        return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[][] bVal = new byte[values.size()][];
//
//                for(int i = 0; i < values.size(); ++i) {
//                    String s = JSON.toJSONString(values.get(i));
//                    bVal[i] = serializer.serialize(s);
//                }
//
//                long r = connection.sAdd(bKey, bVal);
//                if (null != expires) {
//                    connection.expire(bKey, expires);
//                }
//
//                return r;
//            }
//        });
//    }
//
//    public <T> long setSrem(String key, T value) {
//        List<T> values = new ArrayList();
//        values.add(value);
//        return this.setSrem(key, (List)values);
//    }
//
//    public <T> long setSrem(String key, final List<T> values) {
//        final String cacheKey = this.getKey(key);
//        return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[][] bVal = new byte[values.size()][];
//
//                for(int i = 0; i < values.size(); ++i) {
//                    bVal[i] = serializer.serialize(JSON.toJSONString(values.get(i)));
//                }
//
//                return connection.sRem(bKey, bVal);
//            }
//        });
//    }
//
//    public long setScard(String key) {
//        final String cacheKey = this.getKey(key);
//        return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                return connection.sCard(bKey);
//            }
//        });
//    }
//
//    public <T> List<T> setSmembers(String key, final Class<T> t) {
//        final String cacheKey = this.getKey(key);
//        List<T> list = (List)this.redisTemplate.execute(new RedisCallback<List<T>>() {
//            public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                Set<byte[]> valuesByte = connection.sMembers(bKey);
//                if (valuesByte != null && !valuesByte.isEmpty()) {
//                    List<T> list = new ArrayList();
//                    Iterator<byte[]> it = valuesByte.iterator();
//
//                    while(it.hasNext()) {
//                        String v = (String)serializer.deserialize((byte[])it.next());
//                        T t1 = JSON.parseObject(v, t);
//                        list.add(t1);
//                    }
//
//                    return list;
//                } else {
//                    return null;
//                }
//            }
//        });
//        return list;
//    }
//
//    public <T> boolean setSisMember(String key, final T value) {
//        final String cacheKey = this.getKey(key);
//        return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[] vByte = serializer.serialize(JSON.toJSONString(value));
//                return connection.sIsMember(bKey, vByte);
//            }
//        });
//    }
//
//    public <T> boolean zSetAdd(String key, final double score, final T value, final Long expires) {
//        final String cacheKey = this.getKey(key);
//        return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                byte[] bVal = serializer.serialize(JSON.toJSONString(value));
//                boolean r = connection.zAdd(bKey, score, bVal);
//                if (null != expires) {
//                    connection.expire(bKey, expires);
//                }
//
//                return r;
//            }
//        });
//    }
//
//    public <T> Long zSetAdd(String key, final Map<T, Double> value, final Long expires) {
//        if (value != null && !value.isEmpty()) {
//            final String cacheKey = this.getKey(key);
//            return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//                public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                    long i = 0L;
//                    RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                    byte[] bKey = serializer.serialize(cacheKey);
//
//                    for(Iterator var6 = value.entrySet().iterator(); var6.hasNext(); ++i) {
//                        Map.Entry<T, Double> entry = (Map.Entry)var6.next();
//                        byte[] bVal = serializer.serialize(JSON.toJSONString(entry.getKey()));
//                        double score = (Double)entry.getValue();
//                        boolean r = connection.zAdd(bKey, score, bVal);
//                    }
//
//                    if (null != expires) {
//                        connection.expire(bKey, expires);
//                    }
//
//                    return i;
//                }
//            });
//        } else {
//            return 0L;
//        }
//    }
//
//    public <T> Long zSetSize(String key) {
//        final String cacheKey = this.getKey(key);
//        return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                return connection.zCard(bKey);
//            }
//        });
//    }
//
//    public <T> List<T> zSetRange(String key, final long startIndex, final long stopIndex, final Class<T> c) {
//        final String cacheKey = this.getKey(key);
//        return (List)this.redisTemplate.execute(new RedisCallback<List<T>>() {
//            public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                Set<byte[]> set = connection.zRange(bKey, startIndex, stopIndex);
//                if (set == null) {
//                    return null;
//                } else {
//                    List<T> result = new ArrayList();
//                    set.forEach((b) -> {
//                        String data = (String)serializer.deserialize(b);
//                        T t = JSON.parseObject(data, c);
//                        result.add(t);
//                    });
//                    return result;
//                }
//            }
//        });
//    }
//
//    public boolean exists(String key) {
//        final String cacheKey = this.getKey(key);
//        return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] bKey = serializer.serialize(cacheKey);
//                return connection.exists(bKey);
//            }
//        });
//    }
//
////    public Boolean delCache(List<String> keys) {
////        RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
////        byte[][] cacheKeys = new byte[keys.size()][];
////
////        for(int i = 0; i < keys.size(); ++i) {
////            cacheKeys[i] = serializer.serialize(this.getKey((String)keys.get(i)));
////        }
////
////        return (Boolean)this.redisTemplate.execute((connection) -> {
////            Long delResult = connection.del(cacheKeys);
////            return Objects.nonNull(delResult) && delResult > 0L;
////        });
////    }
//
////    public Boolean delUnlink(String key) {
////        String cacheKey = this.getKey(key);
////        return (Boolean)this.redisTemplate.execute((connection) -> {
////            RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
////            byte[] k = serializer.serialize(cacheKey);
////            Long delResult = connection.unlink(new byte[][]{k});
////            return Objects.nonNull(delResult) && delResult > 0L;
////        });
////    }
////
////    public Boolean delUnlink(List<String> keys) {
////        RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
////        byte[][] cacheKeys = new byte[keys.size()][];
////
////        for(int i = 0; i < keys.size(); ++i) {
////            cacheKeys[i] = serializer.serialize(this.getKey((String)keys.get(i)));
////        }
////
////        return (Boolean)this.redisTemplate.execute((connection) -> {
////            Long delResult = connection.unlink(cacheKeys);
////            return Objects.nonNull(delResult) && delResult > 0L;
////        });
////    }
//
//    public Boolean flushAll() {
//        Boolean result = (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                connection.flushAll();
//                return true;
//            }
//        });
//        return result;
//    }
//
//    public Boolean expire(String key, final long seconds) {
//        final String cacheKey = this.getKey(key);
//        Boolean result = (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                byte[] kByte = BaseCache.this.redisTemplate.getStringSerializer().serialize(cacheKey);
//                return connection.expire(kByte, seconds);
//            }
//        });
//        return result;
//    }
//
//    public List<String> keys(final String pattarn) {
//        List<String> list = (List)this.redisTemplate.execute(new RedisCallback<List<String>>() {
//            public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] k = serializer.serialize(pattarn);
//                Set<byte[]> valuesByte = connection.keys(k);
//                if (valuesByte != null && !valuesByte.isEmpty()) {
//                    List<String> list = new ArrayList();
//                    Iterator<byte[]> it = valuesByte.iterator();
//
//                    while(it.hasNext()) {
//                        String key = (String)serializer.deserialize((byte[])it.next());
//                        list.add(key);
//                    }
//
//                    return list;
//                } else {
//                    return null;
//                }
//            }
//        });
//        return list;
//    }
//
//    public long ttl(String key) {
//        final String cacheKey = this.getKey(key);
//        long ttl = (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
//            public Long doInRedis(RedisConnection connection) throws DataAccessException {
//                RedisSerializer<String> serializer = BaseCache.this.redisTemplate.getStringSerializer();
//                byte[] k = serializer.serialize(cacheKey);
//                long ttl = connection.ttl(k);
//                return ttl;
//            }
//        });
//        return ttl;
//    }
//
//    public Boolean deleteCache(String key){
//        return this.redisTemplate.delete(key);
//    }
//    public Boolean deleteCache(String ... keys){
//        Long delete = this.redisTemplate.delete(Arrays.asList(keys));
//        return delete!= null&&delete>0L;
//    }
//}
