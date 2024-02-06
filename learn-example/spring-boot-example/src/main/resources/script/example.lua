
-- POOL
local availablePoolKey = KEYS[1]
-- POOL_ITEM
local poolPrizeKey = KEYS[2]
-- POOL_ITEM_COUNT
local prizeCountKey = KEYS[3]

-- get POOL
local pools = redis.call("ZREVRANGE", availablePoolKey, 0, 0)
if not pools or #pools == 0 then
return nil
end

local pool = pools[1]

-- get POOL_ITEM
local prize = redis.call("SRANDMEMBER", poolPrizeKey .. pool)

if not prize then

-- del POOL
redis.call("ZREM", availablePoolKey, pool)
-- again get POOL
pools = redis.call("ZREVRANGE", availablePoolKey, 0, 0)
if not pool or #pool == 0 then
return nil
end

pool = pools[1]

end

-- check POOL_ITEM_COUNT
local count = tonumber(redis.call("HINCRBY", prizeCountKey .. pool, prize, -1))
if count >= 0 then

if count == 0 then
-- del POOL_ITEM
redis.call("SREM", poolPrizeKey .. pool, prize)
end

return pool .. "##" .. prize
else
return nil
end