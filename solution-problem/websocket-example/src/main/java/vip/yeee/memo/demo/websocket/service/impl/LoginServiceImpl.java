package vip.yeee.memo.demo.websocket.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import vip.yeee.memo.demo.websocket.mapper.LoginMapper;
import vip.yeee.memo.demo.websocket.service.LoginService;
import vip.yeee.memo.demo.websocket.po.Staff;

@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=5)
@Service("loginservice")
public class LoginServiceImpl implements LoginService {
	@Autowired
    LoginMapper loginmapper;
	
	public String getpwdbyname(String name) {
		Staff s=loginmapper.getpwdbyname(name);
		if(s!=null)
		return s.getPassword();
		else
		return null;
	}
	
	public Long getUidbyname(String name) {
		Staff s=loginmapper.getpwdbyname(name);
		if(s!=null)
			return (long) s.getStaff_id();
			else
			return null;
	}
	
	public String getnamebyid(long id) {
		Staff s=loginmapper.getnamebyid(id);
		if(s!=null)
			return s.getUsername();
			else
			return null;
	}
	
	

}
