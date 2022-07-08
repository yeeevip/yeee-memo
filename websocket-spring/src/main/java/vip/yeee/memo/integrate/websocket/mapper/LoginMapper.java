package vip.yeee.memo.integrate.websocket.mapper;

import vip.yeee.memo.integrate.websocket.po.Staff;

public interface LoginMapper {
	Staff getpwdbyname(String name);
	Staff getnamebyid(long id);
}
