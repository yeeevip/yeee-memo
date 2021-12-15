package boot.spring.po;

public class User {
	Long uid;
	
	String name;
	
	public User() {
		super();
	}

	public User(Long uid, String name) {
		super();
		this.uid = uid;
		this.name = name;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
