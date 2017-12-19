public class Account {
	
	private String name;
	private String password;
	private boolean logged;
	private boolean locked;
	private int lockedNum;

	public Account(String name,String password,String logged, String locked, String lockedNum) {
		this.name = name;
		this.password = password;
		this.logged = Boolean.parseBoolean(logged);
		this.locked = Boolean.parseBoolean(locked);
		this.lockedNum = Integer.parseInt(lockedNum);
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public int incLockedNum(){
		return ++lockedNum;
	}

	public int getLockedNum(){
		return lockedNum;
	}

	public void setLockedNum(int i){
		lockedNum=i;
	}

}
