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
	
	//선택된 그림 주변 4개 저장 배열
	private int[] nb = new int[4];
	
	//배치할 이미지
	private Image[] setImage = new Image[8]; 
	
	private boolean endOfMedia; //재생완료 확인플래그
	private Stage primaryStage;
	
	//선택받을 이미지
	private Image selectedImage;
	
	//클릭횟수
	private int click;
	
	//초기화부분(이벤트생성 등)
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//로그인상태표시
		stateLabel.setText("admin 님이 접속");
		
		//플레이어 버튼 이미지 초기화
		Image play = new Image(getClass().getResourceAsStream("images/play-button.png"), 15 , 15 , false , true);
		btnPlay.setGraphic(new ImageView(play));
		Image pause = new Image(getClass().getResourceAsStream("images/pause-button.png"), 15 , 15 , false, true);
		btnPause.setGraphic(new ImageView(pause));
		Image stop = new Image(getClass().getResourceAsStream("images/stop-button.png"), 15 , 15 , false , false);
		btnStop.setGraphic(new ImageView(stop));
		
		//이미지뷰 클릭 이벤트
		for(ImageView image : ImageViewList){
			image.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					
					
					//효과음 객체 생성
					Media mediaEffect = new Media(getClass().getResource("media/switch2.mp3").toString());
					MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
					if(setImage[0] == null){
						System.out.println("재생 ㄴㄴ");
					} else {
						//현재 클릭한 횟수
						++click;
						clickLabel.setText("현재 클릭한 횟수 \n" + click);
						effectPlayer.play();
					}
					//이미지뷰의 id 값을 얻은 뒤 숫자만 잘라서 int형으로 변환
					ImageView iv = (ImageView) event.getSource();
					String before_id = iv.getId();
					int id = Integer.parseInt(before_id.substring(3));
					findNeighber(id);
					correct();
				}
			});
		}
		
		//음악재생 객체 생성
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
		
		//재생 액션 처리
		btnPlay.setOnAction(event -> {
			//효과음 객체 생성
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
			//효과음 객체 생성
			Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
			MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
			effectPlayer.play();
			mediaPlayer.pause();
		});
		btnStop.setOnAction(event-> {
			//효과음 객체 생성
			Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
			MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
			effectPlayer.play();
			mediaPlayer.stop();
			});
		
		
		//이미지 삽입
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
			    	  //선택한 이미지를 전역변수 selectedImage로 대입
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
			    		  System.out.println("파일선택됨");
			    		  stateLabel.setText("이미지가 선택되었습니다.");
			    	  }
				} catch (NullPointerException e) {
					System.out.println("파일선택안됨");
					stateLabel.setText("이미지를 선택해주세요");
				}
			}
		});
		
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
					System.out.println("시작버튼 눌림");
					//시작과 동시에 클릭한 수 초기화
					click = 0;
					clickLabel.setText("현재 클릭한 횟수 \n" +click);
					//효과음 객체 생성
					Media mediaEffect = new Media(getClass().getResource("media/switch3.mp3").toString());
					MediaPlayer effectPlayer = new MediaPlayer(mediaEffect);
					effectPlayer.play();
					
					Image[] local_image = new Image[8];
					
					if(setImage[0] == null){
						System.out.println("선택된 이미지 없음");;
						stateLabel.setText("이미지를 선택해주세요.");
					} else if (setImage[0] != null ){
						stateLabel.setText("시작되었습니다.");
						System.out.println(setImage[0]);
						System.out.println("선택된 이미지 있음");
						for(int i = 0; i < setImage.length; i++){
							local_image[i] = setImage[i];
						}
							
							//잘린 이미지 섞기
							List<Image> list = Arrays.asList(local_image);
							Collections.shuffle(list);
							
							//배열로 다시 변환
							Image[] s_cropped_arr = list.toArray(new Image[list.size()]);
							
							//자른 이미지를 FXML 이미지뷰id리스트에 대입 => 섞인 이미지 배치
							for(int j = 0; j < s_cropped_arr.length; j++){
//							System.out.println(s_cropped_arr[i]);
								ImageViewList.get(j).setImage(s_cropped_arr[j]);
								ImageViewList.get(j).setDisable(false); //이미지 클릭 가능
							}
							
							//배열(3,3) 칸만 이미지 삭제
							ImageViewList.get(8).setImage(null);
						}
						
					}
					
		});
		
		//음악 볼륨 액션
		sliderVolume.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaPlayer.setVolume(sliderVolume.getValue() / 100.0);
			}
		});
		
		//볼륨 위치 중간으로 셋팅
		sliderVolume.setValue(50.0);
		
	}
	
	//이웃찾기
	public void findNeighber(int id) {
		// 위
		nb[0] = id - 3;
		if (nb[0] < 0) {
			nb[0] = -1;
		}
		// 아래
		nb[1] = id + 3;
		if (nb[1] >= 9) {
			nb[1] = -1;
		}
		// 왼쪽
		nb[2] = id - 1;
		if (nb[2] < 0 || nb[2] % 3 == 2) {
			nb[2] = -1;
		}
		// 오른쪽
		nb[3] = id + 1;
		if (nb[3] % 3 == 0) {
			nb[3] = -1;
		}
		
		//이미지 변경
		for(int i = 0; i < 4; i++){
			if (nb[i] >= 0 && ImageViewList.get(nb[i]).getImage() == null) {
				Image act;
				ImageView inact;
				act = ImageViewList.get(id).getImage(); //클릭된 그림 
				inact = ImageViewList.get(id); //클릭된 이미지뷰
				ImageViewList.get(nb[i]).setImage(act); // 클릭된 그림을 null이였던 이미지뷰에 넣음
				inact.setImage(null); // 클릭된 이미지뷰에 null 넣음
				inact.setDisable(true); //클릭 작동 X
				ImageViewList.get(nb[i]).setDisable(false);
				break; // 비활성화되있는게 발견되면 바로 탈출
			}
		}
	}
	
	//그림 정답
	public void correct(){
		int count = 0;
		
		if(setImage[0] == null){
			System.out.println("정답체크이미지 없음");
		} else if(setImage[0] != null){
			System.out.println("정답체크 이미지 있음");
			for(int i = 0; i < setImage.length; i++){
//			System.out.println("correct함수" + Image_arr[i]);
				if(setImage[i] == ImageViewList.get(i).getImage()){
					count++;
				}
				if(count == 8){
					stateLabel.setText("정답입니다.");
					
					for(int j = 0; j < 9; j++){
						ImageViewList.get(j).setImage(new Image(getClass().getResource("images/correct.jpg").toExternalForm()));
					}
					
					break;
				}
			}
			
		}
		if(setImage[0] != null){
			stateLabel.setText("현재 맞은 그림 수 \n " + count);
		}
	}
}
