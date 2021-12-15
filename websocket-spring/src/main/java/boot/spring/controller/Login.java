package boot.spring.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import boot.spring.po.MSG;
import boot.spring.po.User;
import boot.spring.service.LoginService;



@Controller
public class Login {
	@Autowired
	LoginService loginservice;
	
	@RequestMapping("/loginvalidate")
	public String loginvalidate(@RequestParam("username") String username,@RequestParam("password") String pwd,HttpSession httpSession){
		if(username==null)
			return "login";
		String realpwd=loginservice.getpwdbyname(username);
		if(realpwd!=null&&pwd.equals(realpwd))
		{
			long uid=loginservice.getUidbyname(username);
			httpSession.setAttribute("uid", uid);
			return "chatroom";
		}else
			return "fail";
	}
	
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession httpSession){
		return "login";
	}
	
	@RequestMapping(value="/currentuser",method = RequestMethod.GET)
	@ResponseBody
	public User currentuser(HttpSession httpSession){
		Long uid = (Long)httpSession.getAttribute("uid");
		String name = loginservice.getnamebyid(uid);
		return new User(uid, name);
	}
  }
