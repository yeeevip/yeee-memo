
-- KEY1
local availablePoolKey = KEYS[1]
-- KEY2
local poolPrizeKey = KEYS[2]
-- KEY3
local prizeCountKey = KEYS[3]
-- KEY4
local prizeAllCountKey = KEYS[4]

-- activityDays
local activityDays = tonumber(ARGV[1])
-- days
local days = tonumber(ARGV[2])

-- get
local pools = redis.call("ZREVRANGE", availablePoolKey, 0, 0)
if not pools or #pools == 0 then
return nil
end

local pool = pools[1]

-- get item
local prize = redis.call("SRANDMEMBER", poolPrizeKey .. pool)

if not prize then

-- del POOL
redis.call("ZREM", availablePoolKey, pool)
-- again get
pools = redis.call("ZREVRANGE", availablePoolKey, 0, 0)
if not pools or #pools == 0 then
return nil
end

pool = pools[1]

end


-- 类型-总数
local typeCount = tonumber(redis.call('hget', prizeAllCountKey .. pool, prize))
local parts = {} -- 存储分割后的部分
for part in string.gmatch(data, "([^%-]+)") do
table.insert(parts, part)
end

local investType = tonumber(parts[1])
local totalCount = tonumber(parts[2])

-- 总数
local count = totalCount

if investType == 2 then
-- 计算
count = math.floor(count / activityDays * days)
end

-- 已使用数+1
local usedCount = tonumber(redis.call("HINCRBY", prizeCountKey .. pool, prize, 1))
-- 剩余
local surplus = count - usedCount
if surplus >= 0 then

if surplus == 0 then
-- 删除
redis.call("SREM", poolPrizeKey .. pool, prize)
end

return pool .. "##" .. prize
else
return nil
end