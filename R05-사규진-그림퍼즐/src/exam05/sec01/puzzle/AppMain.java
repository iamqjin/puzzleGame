package exam05.sec01.puzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application{
	
	
	//메인함수
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("root.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("퍼즐게임");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> System.out.println("게임 종료"));
		primaryStage.show();
	}
}
