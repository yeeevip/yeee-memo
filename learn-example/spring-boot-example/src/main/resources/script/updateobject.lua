
-- 测试

local oldKey = KEYS[1]
local newKey = KEYS[2]

local value = redis.call('GET', oldKey)

local replacements = {
    ['"exchangeUserId":null'] = '"exchangeUserId":' .. ARGV[1],
    ['"exchangeUser":null'] = '"exchangeUser":' .. ARGV[2],
    ['"exchangeTime":null'] = '"exchangeTime":["cn.hutool.core.date.DateTime",' .. ARGV[3] ..']'
}

local replacedValue = value

for key, value in pairs(replacements) do
    replacedValue = string.gsub(replacedValue, key, value)
end

redis.call('SET', newKey, replacedValue)

return replacedValue