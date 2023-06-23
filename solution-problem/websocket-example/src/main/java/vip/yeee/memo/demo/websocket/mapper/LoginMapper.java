package vip.yeee.memo.demo.websocket.mapper;

import vip.yeee.memo.demo.websocket.po.Staff;

public interface LoginMapper {
	Staff getpwdbyname(String name);
	Staff getnamebyid(long id);
}
