package exam05.sec01.puzzle;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PaperSource;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class RootController implements Initializable{
	@FXML private List<ImageView> ImageViewList;
	@FXML private Button startBtn,loginBtn,cancleBtn,btnPlay, btnPause, btnStop;
	@FXML private MenuItem newstart;
	@FXML private Label score;
	@FXML private MediaView mediaView;
	@FXML private ProgressBar progressBar;
	@FXML private Label labelTime;
	@FXML private Slider sliderVolume;
	
	//�̹��� �ҷ����� �ڸ���
	Image original_image = new Image(getClass().getResource("images/main.png").toExternalForm(), 600, 600, false, true);
	PixelReader before_crop = original_image.getPixelReader();
	WritableImage cropped0 = new WritableImage(before_crop, 0, 0, 200,200);
	WritableImage cropped1 = new WritableImage(before_crop, 200, 0, 200,200);
	WritableImage cropped2 = new WritableImage(before_crop, 400, 0, 200,200);
	WritableImage cropped3 = new WritableImage(before_crop, 0, 200, 200,200);
	WritableImage cropped4 = new WritableImage(before_crop, 200, 200, 200,200);
	WritableImage cropped5 = new WritableImage(before_crop, 400, 200, 200,200);
	WritableImage cropped6 = new WritableImage(before_crop, 0, 400, 200,200);
	WritableImage cropped7 = new WritableImage(before_crop, 200, 400, 200,200);
	WritableImage cropped8 = new WritableImage(before_crop, 400, 400, 200,200);
	
	WritableImage[] cropped_arr = {cropped0,cropped1,cropped2,cropped3,cropped4,cropped5,cropped6,cropped7}; 
	
	//���õ� �׸� �ֺ� 4�� ���� �迭
	private int[] nb = new int[4];
	
	//���� �߻���
	Random random = new Random();
	
	private boolean endOfMedia; //����Ϸ� Ȯ���÷���
	
	//�ʱ�ȭ�κ�(�̺�Ʈ���� ��)
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//�÷��̾� ��ư �̹��� �ʱ�ȭ
		Image play = new Image(getClass().getResourceAsStream("images/play-button.png"), 15 , 15 , false , true);
		btnPlay.setGraphic(new ImageView(play));
		Image pause = new Image(getClass().getResourceAsStream("images/pause-button.png"), 15 , 15 , false, true);
		btnPause.setGraphic(new ImageView(pause));
		Image stop = new Image(getClass().getResourceAsStream("images/stop-button.png"), 15 , 15 , false , false);
		btnStop.setGraphic(new ImageView(stop));
		
		
		//�̹��� ���� �����
		makeCorrect();
		Image[] correct = new Image[8];
		
		//���� ���ϱ�
		for(int i = 0; i < makeCorrect().length; i++)
		{
			correct[i] = makeCorrect()[i];
		}
		
		//������� ��ü ����
		Media media = new Media(getClass().getResource("media/audio.mp3").toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mediaPlayer);
		
		mediaPlayer.setOnReady(new Runnable() {
			
			@Override
			public void run() {
				mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
					@Override
					public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue){
						double progress = mediaPlayer.getCurrentTime().toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
						progressBar.setProgress(progress);
						
						labelTime.setText((int)mediaPlayer.getCurrentTime().toSeconds() +"/"+ (int)mediaPlayer.getTotalDuration().toSeconds()+"sec");
					}
				});
				btnPlay.setDisable(false); btnPause.setDisable(true); btnStop.setDisable(true);
				if(mediaPlayer.isAutoPlay()){
					mediaView.setVisible(false);
				}
			}
		});
		mediaPlayer.setOnPlaying(()-> {
			btnPlay.setDisable(true); btnPause.setDisable(false); btnStop.setDisable(false);
		});
		mediaPlayer.setOnPaused(()->{
			btnPlay.setDisable(false); btnPause.setDisable(true); btnStop.setDisable(false);
		});
		mediaPlayer.setOnEndOfMedia(()->{
			progressBar.setProgress(1.0);
			endOfMedia = true;
			btnPlay.setDisable(false); btnPause.setDisable(true); btnStop.setDisable(true);
		});
		mediaPlayer.setOnStopped(()->{
			btnPlay.setDisable(false); btnPause.setDisable(true); btnStop.setDisable(true);
		});
		
		//��� �׼� ó��
		btnPlay.setOnAction(event -> {
			if(endOfMedia){
				mediaPlayer.stop();
				mediaPlayer.seek(mediaPlayer.getStartTime());
			}
			mediaPlayer.play();
			endOfMedia = false;
		});
		btnPause.setOnAction(event -> mediaPlayer.pause());
		btnStop.setOnAction(event-> mediaPlayer.stop());
		
		
		//�̹��� ����
		startBtn.setOnAction(event -> handleStartBtn(event));
		newstart.setOnAction(event -> handleStartBtn(event));
		
		//���� ���� �׼�
		sliderVolume.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaPlayer.setVolume(sliderVolume.getValue() / 100.0);
			}
		});
		
		//���� ��ġ �߰����� ����
		sliderVolume.setValue(50.0);
		
		//�̹����� Ŭ�� �̺�Ʈ
		for(ImageView image : ImageViewList){
			image.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					//�̹������� id ���� ���� �� ���ڸ� �߶� int������ ��ȯ
					ImageView iv = (ImageView) event.getSource();
					String before_id = iv.getId();
					int id = Integer.parseInt(before_id.substring(3));
					System.out.println(id);
					findNeighber(id);
					correct(correct);
				}
			});
		}
		
		//�α��� ��ư �̺�Ʈ ����
		loginBtn.setOnAction(event->handleLoginBtn(event));
	}
	
	
	//�α��ι�ư �ڵ鷯
	private void handleLoginBtn(ActionEvent event){
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(loginBtn.getScene().getWindow());
			dialog.setTitle("�߰�");
			
			Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
			
			//�α���â���� ��ҹ�ư�� �ǵ��ƿ�
			Button loginFormCancle = (Button) parent.lookup("#cancleBtn");
			loginFormCancle.setOnAction(e->dialog.close());
			
			//�α���â���� ȸ������ ��ư �Է½� �۵�
			Button joinBtn_login = (Button) parent.lookup("#signUpBtn");
			joinBtn_login.setOnAction(e -> {
				
				Stage dialog_join = new Stage(StageStyle.UTILITY);
				dialog_join.initModality(Modality.WINDOW_MODAL);
				dialog_join.initOwner(loginBtn.getScene().getWindow());
				dialog_join.setTitle("ȸ������");
				
				try {
					Parent parent_join = FXMLLoader.load(getClass().getResource("join.fxml"));
					Button joinFormCancle = (Button) parent_join.lookup("#cancleBtn");
					Button joinBtn_join = (Button) parent_join.lookup("#joinBtn");
					
					//ȸ������â ���Թ�ư�̺�Ʈ + ��ҹ�ư �̺�Ʈ
					joinBtn_join.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							System.out.println("�� ���Թ�ư");
							dialog_join.close();
						}
						
					});
					joinFormCancle.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							System.out.println("�� ��ҹ�ư");
							dialog_join.close();
						}
					});
					
					//ȸ������â scene ���� �� ���
					Scene scene = new Scene(parent_join);
					dialog_join.setScene(scene);
					dialog_join.show();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			});
			
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	//���� ����
	public void handleStartBtn(ActionEvent event) {
		System.out.println("���۹�ư ����");
		
		//�߸� �̹��� ����
		List<WritableImage> list = Arrays.asList(cropped_arr);
		Collections.shuffle(list);
		
		//�迭�� �ٽ� ��ȯ
		WritableImage[] s_cropped_arr = list.toArray(new WritableImage[list.size()]);

		//�ڸ� �̹����� FXML �̹�����id����Ʈ�� ���� => ���� �̹��� ��ġ
		for(int i = 0; i < s_cropped_arr.length; i++){
			
			System.out.println(s_cropped_arr[i]);
				ImageViewList.get(i).setImage(s_cropped_arr[i]);
				ImageViewList.get(i).setDisable(false); //�̹��� Ŭ�� ����
			}
		
		//�迭(3,3) ĭ�� �̹��� ����
		ImageViewList.get(8).setImage(null);
		}
		
	
	
	//�̿�ã��
	public void findNeighber(int id) {
		// ��
		nb[0] = id - 3;
		if (nb[0] < 0) {
			nb[0] = -1;
		}
		// �Ʒ�
		nb[1] = id + 3;
		if (nb[1] >= 9) {
			nb[1] = -1;
		}
		// ����
		nb[2] = id - 1;
		if (nb[2] < 0 || nb[2] % 3 == 2) {
			nb[2] = -1;
		}
		// ������
		nb[3] = id + 1;
		if (nb[3] % 3 == 0) {
			nb[3] = -1;
		}
		
		//�̹��� ����
		for(int i = 0; i < 4; i++){
			if (nb[i] >= 0 && ImageViewList.get(nb[i]).getImage() == null) {
				Image act;
				ImageView inact;
				act = ImageViewList.get(id).getImage(); //Ŭ���� �׸� 
				inact = ImageViewList.get(id); //Ŭ���� �̹�����
				ImageViewList.get(nb[i]).setImage(act); // Ŭ���� �׸��� null�̿��� �̹����信 ����
				inact.setImage(null); // Ŭ���� �̹����信 null ����
				inact.setDisable(true); //Ŭ�� �۵� X
				ImageViewList.get(nb[i]).setDisable(false);
				break; // ��Ȱ��ȭ���ִ°� �߰ߵǸ� �ٷ� Ż��
			}
		}
	}
	
	//�׸� ����
	public void correct(Image[] Image_arr){
		
		int count = 0;
		
		for(int i = 0; i < Image_arr.length; i++){
			if(Image_arr[i] == ImageViewList.get(i).getImage()){
				count++;
			}
			
			if(count == 8){
				System.out.println("����");
				
				for(int j = 0; j < 9; j++){
					ImageViewList.get(j).setImage(new Image(getClass().getResource("images/main.jpg").toExternalForm()));
				}
				
				break;
			}
		}
	}
	
	//���� �����
	public Image[] makeCorrect(){
		
		//�̹��� �迭�� �ڸ� �̹����� ���ʷ� �Է� �� �����ϴ� �Լ�
		Image[] correct_image = new Image[8];
		
		for(int i = 0; i < correct_image.length; i++){
			correct_image[i] = (Image) cropped_arr[i];
		}
			
		return correct_image;
		
	}

	
//	//��� �̺�Ʈ �ڵ鷯
//	public void play(ActionEvent event){
//		Media media = new Media(getClass().getResource("media/media.mp4").toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(media);
//		player.setMediaPlayer(mediaPlayer);
//		mediaPlayer.setAutoPlay(true);
//	}
//	
//	
//	//��� �޼ҵ�
//	public void play2(){
//		Media media = new Media(getClass().getResource("media/media.mp4").toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(media);
//		player.setMediaPlayer(mediaPlayer);
//		mediaPlayer.setAutoPlay(true);
//	}
}
