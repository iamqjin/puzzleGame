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
		loginBtn.setOnAction(e->handLoginBtn(e));
	}

	//로그인 버튼 핸들러
	public void handLoginBtn(ActionEvent event) {
		try {
			System.out.println("나 눌림");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
