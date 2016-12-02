package exam05.sec01.puzzle;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class LoginController implements Initializable{
	@FXML private StackPane login;
	@FXML private Button loginBtn, cancleBtn;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("login 이니셜라이즈드");
		loginBtn.setOnAction(e->handLoginBtn(e));
	}

	//로그인 버튼 핸들러
	public void handLoginBtn(ActionEvent event) {
		try {
			System.out.println("나 눌림");
			
//			AnchorPane root = (AnchorPane) loginBtn.getScene().getRoot();
//			root.getChildren().remove(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
