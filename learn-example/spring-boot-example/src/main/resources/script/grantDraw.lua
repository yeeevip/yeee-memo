
-- 奖池
local availablePoolKey = KEYS[1]
-- 奖池奖项
local poolPrizeKey = KEYS[2]
-- 奖池奖项数
local prizeCountKey = KEYS[2]

-- 获取POOL
local pool = redis.call("ZREVRANGE", availableKey, 0, 0)[1]
if not pool or #pool == 0 then
return nil
end

-- 从POOL获取
local prize = redis.call("SRANDMEMBER", poolPrizeKey .. ":" .. pool)

if not prize then

-- 移除该组
redis.call("ZREM", availablePoolKey, pool)
-- 再获取POOL
pool = redis.call("ZREVRANGE", availableKey, 0, 0)
if not pool or #pool == 0 then
return nil
end

end

-- 判断奖品数量
local count = redis.call("HINCRBY", poolPrizeKey .. ":" .. pool, prize, -1)
if tonumber(count) >= 0 then
return pool .. "##" .. prize
else
-- 删除奖项
redis.call("SREM", poolPrizeKey .. ":" .. pool, prize)
return nil
end