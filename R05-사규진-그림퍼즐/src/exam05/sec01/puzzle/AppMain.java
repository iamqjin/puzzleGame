package exam05.sec01.puzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AppMain extends Application{
	
	
	//�����Լ�
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("�α���");
		primaryStage.setScene(scene);
		Button loginCancleButton = (Button) root.lookup("#cancleBtn");
		loginCancleButton.setOnAction(e -> {
			primaryStage.close();
		});
		
		primaryStage.setOnCloseRequest(event -> System.out.println("���� ����"));
		primaryStage.show();
	}
}
