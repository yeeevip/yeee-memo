--- -1 failed
--- 1 success
---
local key = KEYS[1]
local requestId = KEYS[2]
local ttl = tonumber(KEYS[3])
local result = redis.call('setnx', key, requestId)
if result == 1 then
    -- PEXPIRE：以毫秒的形式指定过期时间
    redis.call('pexpire', key, ttl)
else
    result = -1
    -- 如果value相同，则认为是同一个线程的请求，则认为重入锁
    local value = redis.call('get', key)
    if (value == requestId) then
        result = 1
        redis.call('pexpire', key, ttl)
    end
end
-- 如果获取锁成功，则返回1
return result