package es.susangames.catan.auxiliarModel;

public class ChangePassword {
	
	private String userId;
	private String oldPassw;
	private String newPassw;
	
	public ChangePassword(String userId, String oldPassw, String newPassw) {
		this.userId = userId;
		this.oldPassw = oldPassw;
		this.newPassw = newPassw;
	}
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOldPassw() {
		return oldPassw;
	}
	public void setOldPassw(String oldPassw) {
		this.oldPassw = oldPassw;
	}
	public String getNewPassw() {
		return newPassw;
	}
	public void setNewPassw(String newPassw) {
		this.newPassw = newPassw;
	}
}
