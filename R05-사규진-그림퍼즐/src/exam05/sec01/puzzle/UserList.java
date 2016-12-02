package exam05.sec01.puzzle;

import javafx.beans.property.SimpleStringProperty;

public class UserList {
	private SimpleStringProperty id;
	private SimpleStringProperty password;
	
	
	public UserList(){
		this.id = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
	}
	
	public UserList(String id, String password){
		this.id = new SimpleStringProperty(id);
		this.password = new SimpleStringProperty(password);
	}
	
	public String getId() { return id.get();}
	public void setId(String id) { this.id.set(id);}
	
	public String getPwd() {return password.get();}
	public void setPwd(String password) { this.password.set(password);}
	
}
