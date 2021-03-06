package exam05.sec01.puzzle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	@FXML private Button loginBtn, cancleBtn;
	@FXML private TextField idField, pwdField;
	@FXML private Label errorLabel;
	@FXML private MediaView effectView;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//로그인 버튼
		loginBtn.setOnAction(e->handLoginBtn(e));
		idField.setPromptText("ID를 입력하세요");
		pwdField.setPromptText("PASSWORD를 입력하세요");
	}

	//로그인 버튼 핸들러
	public void handLoginBtn(ActionEvent event) {
			
			System.out.println("나 눌림");
			
			//효과음 객체 생성
			Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
			MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
			
			String idValue = new String();
			String pwdValue = new String();
			String idPwd = "admin";
			
			try {
				 idValue = idField.getText();
				 pwdValue = pwdField.getText();
				
			} catch (NullPointerException e1) {
				System.out.println("아무것도 없는상태");
			}
			
			if(idValue.length() == 0 && pwdValue.length() == 0){
				errorLabel.setText("아이디와 비밀번호를 입력하세요");
				errorLabel.setTextFill(Color.rgb(255, 0, 0));
				effectPlayer.play();
			} else if(idValue.equals(idPwd) && pwdValue.equals(idPwd)){
				errorLabel.setTextFill(Color.rgb(21, 117 , 84));
				errorLabel.setText("인증 되었습니다..");
				
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("root.fxml"));
					Scene scene = new Scene(root);
					Stage primaryStage = new Stage();
					primaryStage.setTitle("퍼즐게임");
					primaryStage.setScene(scene);
					idField.clear();
					pwdField.clear();
					errorLabel.setText("");
					//효과음 객체 생성
					Media mediaEffect1 = new Media(getClass().getResource("media/Windows.mp3").toString());
					MediaPlayer effectPlayer1 = new MediaPlayer(mediaEffect1);
					effectPlayer1.play();
					Button gameViewCloseBtn = (Button) root.lookup("#cancleBtn");
					gameViewCloseBtn.setOnAction(e -> {
						Platform.exit();
						primaryStage.close();
					});
					primaryStage.setOnCloseRequest(e -> {
						Platform.exit();
						primaryStage.close();
						System.out.println("게임 종료");
					});
					primaryStage.show();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} else if (idValue.equals(idPwd) && !pwdValue.equals(idPwd)) {
				errorLabel.setTextFill(Color.rgb(255, 0, 0));
				errorLabel.setText("비밀번호를 다시입력해주세요.");
				effectPlayer.play();
			} else if(!idValue.equals(idPwd) && !(pwdValue.length() == 0)){
				errorLabel.setTextFill(Color.rgb(255, 0, 0));
				errorLabel.setText("존재하지 않는 회원정보입니다.");
				effectPlayer.play();
			} else {
				errorLabel.setTextFill(Color.rgb(255, 0, 0));
				errorLabel.setText("비밀번호를 입력하세요");
				effectPlayer.play();
			}
	}
	
}
