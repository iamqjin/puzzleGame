package exam05.sec01.puzzle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class RootController implements Initializable{
	@FXML private List<ImageView> ImageViewList;
	@FXML private Button startBtn,btnPlay, btnPause, btnStop;
	@FXML private MenuItem selectBtn;
	@FXML private Label score, stateLabel;
	@FXML private MediaView mediaView, effectView;
	@FXML private ProgressBar progressBar;
	@FXML private Label labelTime,clickLabel;
	@FXML private Slider sliderVolume;
	@FXML private ImageView correctView;
	
	//���õ� �׸� �ֺ� 4�� ���� �迭
	private int[] nb = new int[4];
	
	//��ġ�� �̹���
	private Image[] setImage = new Image[8]; 
	
	private boolean endOfMedia; //����Ϸ� Ȯ���÷���
	private Stage primaryStage;
	
	//���ù��� �̹���
	private Image selectedImage;
	
	//Ŭ��Ƚ��
	private int click;
	
	//�ʱ�ȭ�κ�(�̺�Ʈ���� ��)
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//�α��λ���ǥ��
		stateLabel.setText("admin ���� ����");
		
		//�÷��̾� ��ư �̹��� �ʱ�ȭ
		Image play = new Image(getClass().getResourceAsStream("images/play-button.png"), 15 , 15 , false , true);
		btnPlay.setGraphic(new ImageView(play));
		Image pause = new Image(getClass().getResourceAsStream("images/pause-button.png"), 15 , 15 , false, true);
		btnPause.setGraphic(new ImageView(pause));
		Image stop = new Image(getClass().getResourceAsStream("images/stop-button.png"), 15 , 15 , false , false);
		btnStop.setGraphic(new ImageView(stop));
		
		//�̹����� Ŭ�� �̺�Ʈ
		for(ImageView image : ImageViewList){
			image.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					
					
					//ȿ���� ��ü ����
					Media mediaEffect = new Media(getClass().getResource("media/switch2.mp3").toString());
					MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
					if(setImage[0] == null){
						System.out.println("��� ����");
					} else {
						//���� Ŭ���� Ƚ��
						++click;
						clickLabel.setText("���� Ŭ���� Ƚ�� \n" + click);
						effectPlayer.play();
					}
					//�̹������� id ���� ���� �� ���ڸ� �߶� int������ ��ȯ
					ImageView iv = (ImageView) event.getSource();
					String before_id = iv.getId();
					int id = Integer.parseInt(before_id.substring(3));
					findNeighber(id);
					correct();
				}
			});
		}
		
		//������� ��ü ����
		Media media = new Media(getClass().getResource("media/audio.mp3").toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		
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
//					mediaView.setVisible(false);
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
			//ȿ���� ��ü ����
			Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
			MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
			effectPlayer.play();
			
			if(endOfMedia){
				mediaPlayer.stop();
				mediaPlayer.seek(mediaPlayer.getStartTime());
			}
			mediaPlayer.play();
			endOfMedia = false;
		});
		btnPause.setOnAction(event -> {
			//ȿ���� ��ü ����
			Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
			MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
			effectPlayer.play();
			mediaPlayer.pause();
		});
		btnStop.setOnAction(event-> {
			//ȿ���� ��ü ����
			Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
			MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
			effectPlayer.play();
			mediaPlayer.stop();
			});
		
		
		//�̹��� ����
		selectBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				FileChooser fileChooser = new FileChooser();
			      fileChooser.getExtensionFilters().addAll(
			            new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
			            );
			      try {
			    	  File selectedFile = fileChooser.showOpenDialog(primaryStage);
			    	  String imagePath = selectedFile.getPath();
			    	  Image selectImage = new Image("file:" + imagePath,600 ,600 ,false ,true);
			    	  correctView.setImage(selectImage);
			    	  //������ �̹����� �������� selectedImage�� ����
			    	  selectedImage = selectImage;
			    	  
			    	  PixelReader before_crop = selectedImage.getPixelReader();
			  		  WritableImage cropped0 = new WritableImage(before_crop, 0, 0, 200,200);
			  		  WritableImage cropped1 = new WritableImage(before_crop, 200, 0, 200,200);
			  	  	  WritableImage cropped2 = new WritableImage(before_crop, 400, 0, 200,200);
			  	 	  WritableImage cropped3 = new WritableImage(before_crop, 0, 200, 200,200);
			  		  WritableImage cropped4 = new WritableImage(before_crop, 200, 200, 200,200);
			  		  WritableImage cropped5 = new WritableImage(before_crop, 400, 200, 200,200);
			  		  WritableImage cropped6 = new WritableImage(before_crop, 0, 400, 200,200);
			  		  WritableImage cropped7 = new WritableImage(before_crop, 200, 400, 200,200);
			  		  WritableImage[] cropped_arr = {cropped0,cropped1,cropped2,cropped3,cropped4,cropped5,cropped6,cropped7}; 
			  		  for(int i = 0; i < cropped_arr.length; i++){
			  			setImage[i] = (Image) cropped_arr[i];
			  		}
			    	  
			    	  if(selectedFile != null){
			    		  System.out.println("���ϼ��õ�");
			    		  stateLabel.setText("�̹����� ���õǾ����ϴ�.");
			    	  }
				} catch (NullPointerException e) {
					System.out.println("���ϼ��þȵ�");
					stateLabel.setText("�̹����� �������ּ���");
				}
			}
		});
		
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
					System.out.println("���۹�ư ����");
					//���۰� ���ÿ� Ŭ���� �� �ʱ�ȭ
					click = 0;
					clickLabel.setText("���� Ŭ���� Ƚ�� \n" +click);
					//ȿ���� ��ü ����
					Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
					MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
					effectPlayer.play();
					
					Image[] local_image = new Image[8];
					
					if(setImage[0] == null){
						System.out.println("���õ� �̹��� ����");;
						stateLabel.setText("�̹����� �������ּ���.");
					} else if (setImage[0] != null ){
						stateLabel.setText("���۵Ǿ����ϴ�.");
						System.out.println(setImage[0]);
						System.out.println("���õ� �̹��� ����");
						for(int i = 0; i < setImage.length; i++){
							local_image[i] = setImage[i];
						}
							
							//�߸� �̹��� ����
							List<Image> list = Arrays.asList(local_image);
							Collections.shuffle(list);
							
							//�迭�� �ٽ� ��ȯ
							Image[] s_cropped_arr = list.toArray(new Image[list.size()]);
							
							//�ڸ� �̹����� FXML �̹�����id����Ʈ�� ���� => ���� �̹��� ��ġ
							for(int j = 0; j < s_cropped_arr.length; j++){
//							System.out.println(s_cropped_arr[i]);
								ImageViewList.get(j).setImage(s_cropped_arr[j]);
								ImageViewList.get(j).setDisable(false); //�̹��� Ŭ�� ����
							}
							
							//�迭(3,3) ĭ�� �̹��� ����
							ImageViewList.get(8).setImage(null);
						}
						
					}
					
		});
		
		//���� ���� �׼�
		sliderVolume.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaPlayer.setVolume(sliderVolume.getValue() / 100.0);
			}
		});
		
		//���� ��ġ �߰����� ����
		sliderVolume.setValue(50.0);
		
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
	public void correct(){
		int count = 0;
		
		if(setImage[0] == null){
			System.out.println("����üũ�̹��� ����");
		} else if(setImage[0] != null){
			System.out.println("����üũ �̹��� ����");
			for(int i = 0; i < setImage.length; i++){
//			System.out.println("correct�Լ�" + Image_arr[i]);
				if(setImage[i] == ImageViewList.get(i).getImage()){
					count++;
				}
				if(count == 8){
					stateLabel.setText("�����Դϴ�.");
					
					for(int j = 0; j < 9; j++){
						ImageViewList.get(j).setImage(new Image(getClass().getResource("images/correct.jpg").toExternalForm()));
					}
					
					break;
				}
			}
			
		}
		if(setImage[0] != null){
			stateLabel.setText("���� ���� �׸� �� \n " + count);
		}
	}
}
