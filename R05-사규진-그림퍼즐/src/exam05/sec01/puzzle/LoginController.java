package exam05.sec01.puzzle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
		//�α��� ��ư
		loginBtn.setOnAction(e->handLoginBtn(e));
		cancleBtn.setOnAction(e -> cancleBtn(e));
		idField.setPromptText("ID�� �Է��ϼ���");
		pwdField.setPromptText("PASSWORD�� �Է��ϼ���");
	}

	//�α��� ��ư �ڵ鷯
	public void handLoginBtn(ActionEvent event) {
			
			System.out.println("�� ����");
			
			//ȿ���� ��ü ����
			Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
			MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
			
			String idValue = new String();
			String pwdValue = new String();
			String idPwd = "admin";
			
			try {
				 idValue = idField.getText();
				 pwdValue = pwdField.getText();
				
			} catch (NullPointerException e1) {
				System.out.println("�ƹ��͵� ���»���");
			}
			
			if(idValue.length() == 0 && pwdValue.length() == 0){
				errorLabel.setText("���̵� ��й�ȣ�� �Է��ϼ���");
				errorLabel.setTextFill(Color.rgb(255, 0, 0));
				effectPlayer.play();
			} else if(idValue.equals(idPwd) && pwdValue.equals(idPwd)){
				errorLabel.setTextFill(Color.rgb(21, 117 , 84));
				errorLabel.setText("���� �Ǿ����ϴ�..");
				
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("root.fxml"));
					Scene scene = new Scene(root);
					Stage primaryStage = new Stage();
					primaryStage.setTitle("�������");
					primaryStage.setScene(scene);
					idField.clear();
					pwdField.clear();
					errorLabel.setText("");
					//ȿ���� ��ü ����
					Media mediaEffect1 = new Media(getClass().getResource("media/Windows.mp3").toString());
					MediaPlayer effectPlayer1 = new MediaPlayer(mediaEffect1);
					effectPlayer1.play();
					Button gameViewCloseBtn = (Button) root.lookup("#cancleBtn");
					gameViewCloseBtn.setOnAction(e -> {
						primaryStage.close();
					});
					primaryStage.setOnCloseRequest(e -> {
						primaryStage.close();
						System.out.println("���� ����");
					});
					primaryStage.show();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} else {
				errorLabel.setTextFill(Color.rgb(255, 0, 0));
				errorLabel.setText("�������� �ʴ� ���̵��Դϴ�.");
				effectPlayer.play();
			}
	}
	
	//��� ��ư �ڵ鷯
	public void  cancleBtn(ActionEvent event) {
		
	}
}
