
-- 奖池
local availablePoolKey = KEYS[1]
-- 奖池奖项
local poolPrizeKey = KEYS[2]
-- 奖池奖项已使用数
local prizeCountKey = KEYS[3]
-- 奖池奖项总数
local prizeAllCountKey = KEYS[4]

-- 活动天数
local activityDays = tonumber(ARGV[1])
-- 当前已进行天数
local days = tonumber(ARGV[2])

-- 获取POOL
local pools = redis.call("ZREVRANGE", availablePoolKey, 0, 0)
if not pools or #pools == 0 then
return nil
end

local pool = pools[1]

-- 从POOL获取
local prize = redis.call("SRANDMEMBER", poolPrizeKey .. pool)

if not prize then

-- 移除该组
redis.call("ZREM", availablePoolKey, pool)
-- 再获取POOL
pools = redis.call("ZREVRANGE", availablePoolKey, 0, 0)
if not pools or #pools == 0 then
return nil
end

pool = pools[1]

end


-- 发放类型-总数
local typeCount = redis.call('hget', prizeAllCountKey .. pool, prize)

if not typeCount then
return nil
end

local a, b = typeCount:match("([^%-]+)%-(.+)")

local investType = tonumber(a)
local totalCount = tonumber(b)

-- 当天应发放奖项总数
local count = totalCount

if investType == 2 then
-- 如果按天发放，则按天算
count = math.floor(count / activityDays * days)
end

-- 奖项已使用数+1
local usedCount = tonumber(redis.call("HINCRBY", prizeCountKey .. pool, prize, 1))
-- 剩余数
local surplus = count - usedCount
if surplus >= 0 then

if surplus == 0 then
-- 删除奖项
redis.call("SREM", poolPrizeKey .. pool, prize)
end

-- 返回奖池ID 和 奖项ID
return pool .. "##" .. prize
else
return nil
end