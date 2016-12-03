package exam05.sec01.puzzle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
	
	//이미지 불러오고 자르기
//	Image original_image = new Image(getClass().getResource("images/main.png").toExternalForm(), 600, 600, false, true);
//	PixelReader before_crop = original_image.getPixelReader();
//	WritableImage cropped0 = new WritableImage(before_crop, 0, 0, 200,200);
//	WritableImage cropped1 = new WritableImage(before_crop, 200, 0, 200,200);
//	WritableImage cropped2 = new WritableImage(before_crop, 400, 0, 200,200);
//	WritableImage cropped3 = new WritableImage(before_crop, 0, 200, 200,200);
//	WritableImage cropped4 = new WritableImage(before_crop, 200, 200, 200,200);
//	WritableImage cropped5 = new WritableImage(before_crop, 400, 200, 200,200);
//	WritableImage cropped6 = new WritableImage(before_crop, 0, 400, 200,200);
//	WritableImage cropped7 = new WritableImage(before_crop, 200, 400, 200,200);
//	WritableImage cropped8 = new WritableImage(before_crop, 400, 400, 200,200);
//	WritableImage[] cropped_arr = {cropped0,cropped1,cropped2,cropped3,cropped4,cropped5,cropped6,cropped7}; 
//	
	//선택된 그림 주변 4개 저장 배열
	private int[] nb = new int[4];
	
	//정답지
	private Image[] correct = new Image[8];
	//배치할 이미지
	private Image[] setImage = new Image[8]; 
	
	//랜덤 발생기
	Random random = new Random();
	
	private boolean endOfMedia; //재생완료 확인플래그
	
	//초기화부분(이벤트생성 등)
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//플레이어 버튼 이미지 초기화
		Image play = new Image(getClass().getResourceAsStream("images/play-button.png"), 15 , 15 , false , true);
		btnPlay.setGraphic(new ImageView(play));
		Image pause = new Image(getClass().getResourceAsStream("images/pause-button.png"), 15 , 15 , false, true);
		btnPause.setGraphic(new ImageView(pause));
		Image stop = new Image(getClass().getResourceAsStream("images/stop-button.png"), 15 , 15 , false , false);
		btnStop.setGraphic(new ImageView(stop));
		
		//이미지 정답 만들기
		
		Image[] local_correct = new Image[8];
		
		
		//정답 비교하기
		for(int i = 0; i < cutImage().length; i++)
		{
			setImage[i] = cutImage()[i];
			local_correct[i] = setImage[i];
			System.out.println("난 이니셜라이즈 correct " + setImage[i]);
		}
		
		//이미지뷰 클릭 이벤트
		for(ImageView image : ImageViewList){
			image.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					//이미지뷰의 id 값을 얻은 뒤 숫자만 잘라서 int형으로 변환
					ImageView iv = (ImageView) event.getSource();
					String before_id = iv.getId();
					int id = Integer.parseInt(before_id.substring(3));
					findNeighber(id);
					correct(local_correct);
				}
			});
		}
		
		//음악재생 객체 생성
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
		
		//재생 액션 처리
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
		
		
		//이미지 삽입
//		startBtn.setOnAction(event -> handleStartBtn(event));
		newstart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
					System.out.println("시작버튼 눌림");
					
					//잘린 이미지 섞기
					List<Image> list = Arrays.asList(setImage);
					Collections.shuffle(list);
					for(Image h : list){
						System.out.println("shuffle " + h);
					}
					
					//배열로만 섞기
//					int ran_num;
//					for(int i = 0; i < correct.length; i++){
//						ran_num = (int)(Math.random()*8);
//						setImage = correct[i];
//						correct[i] = correct[ran_num];
//						correct[ran_num] = setImage;
//					}
					
					//배열로 다시 변환
					Image[] s_cropped_arr = list.toArray(new Image[list.size()]);

					//자른 이미지를 FXML 이미지뷰id리스트에 대입 => 섞인 이미지 배치
					for(int i = 0; i < s_cropped_arr.length; i++){
						
							System.out.println(s_cropped_arr[i]);
							ImageViewList.get(i).setImage(s_cropped_arr[i]);
							ImageViewList.get(i).setDisable(false); //이미지 클릭 가능
						}
					
					//배열(3,3) 칸만 이미지 삭제
					ImageViewList.get(8).setImage(null);
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
		
		
		
		//로그인 버튼 이벤트 생성
		loginBtn.setOnAction(event->handleLoginBtn(event));
	}
	
	//이미지 불러오고 자르기
	public Image[] cutImage(){
//		Image[] cropped_image = new Image[8];
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
//			WritableImage cropped8 = new WritableImage(before_crop, 400, 400, 200,200); 한칸 비우기 때문에 안씀
		WritableImage[] cropped_arr = {cropped0,cropped1,cropped2,cropped3,cropped4,cropped5,cropped6,cropped7}; 
		for(int i = 0; i < cropped_arr.length; i++){
			correct[i] = (Image) cropped_arr[i];
		}
			
		return correct;
	}
	
	//로그인버튼 핸들러
	private void handleLoginBtn(ActionEvent event){
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(loginBtn.getScene().getWindow());
			dialog.setTitle("추가");
			
			Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
			
			//로그인창에서 취소버튼시 되돌아옴
			Button loginFormCancle = (Button) parent.lookup("#cancleBtn");
			loginFormCancle.setOnAction(e->dialog.close());
			
			//로그인창에서 회원가입 버튼 입력시 작동
			Button joinBtn_login = (Button) parent.lookup("#signUpBtn");
			joinBtn_login.setOnAction(e -> {
				
				Stage dialog_join = new Stage(StageStyle.UTILITY);
				dialog_join.initModality(Modality.WINDOW_MODAL);
				dialog_join.initOwner(loginBtn.getScene().getWindow());
				dialog_join.setTitle("회원가입");
				
				try {
					Parent parent_join = FXMLLoader.load(getClass().getResource("join.fxml"));
					Button joinFormCancle = (Button) parent_join.lookup("#cancleBtn");
					Button joinBtn_join = (Button) parent_join.lookup("#joinBtn");
					
					//회원가입창 가입버튼이벤트 + 취소버튼 이벤트
					joinBtn_join.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							System.out.println("난 가입버튼");
							dialog_join.close();
						}
						
					});
					joinFormCancle.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							System.out.println("난 취소버튼");
							dialog_join.close();
						}
					});
					
					//회원가입창 scene 생성 후 띄움
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

	
	//게임 시작
//	public void handleStartBtn(ActionEvent event) {
//		System.out.println("시작버튼 눌림");
//		
//		//잘린 이미지 섞기
//		List<WritableImage> list = Arrays.asList(cropped_arr);
//		Collections.shuffle(list);
//		
//		//배열로 다시 변환
//		WritableImage[] s_cropped_arr = list.toArray(new WritableImage[list.size()]);
//
//		//자른 이미지를 FXML 이미지뷰id리스트에 대입 => 섞인 이미지 배치
//		for(int i = 0; i < s_cropped_arr.length; i++){
//			
//			System.out.println(s_cropped_arr[i]);
//				ImageViewList.get(i).setImage(s_cropped_arr[i]);
//				ImageViewList.get(i).setDisable(false); //이미지 클릭 가능
//			}
//		
//		//배열(3,3) 칸만 이미지 삭제
//		ImageViewList.get(8).setImage(null);
//	}
		
	
	
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
	public void correct(Image[] Image_arr){
		System.out.println("나 correct함수");
		
		int count = 0;
		for(int i = 0; i < Image_arr.length; i++){
			
			System.out.println("correct함수" + Image_arr[i]);
			
			if(Image_arr[i] == ImageViewList.get(i).getImage()){
				count++;
			}
			
			if(count == 8){
				System.out.println("정답");
				
				for(int j = 0; j < 9; j++){
					ImageViewList.get(j).setImage(new Image(getClass().getResource("images/main.jpg").toExternalForm()));
				}
				
				break;
			}
		}
		System.out.println("정답수" + count);
		
	}
	
	//정답 만들기
//	public Image[] makeCorrect(){
//		
//		//이미지 배열에 자른 이미지를 차례로 입력 후 리턴하는 함수
//		Image[] correct_image = new Image[8];
//		
//		for(int i = 0; i < correct_image.length; i++){
//			correct_image[i] = (Image) cropped_arr[i];
//		}
//			
//		return correct_image;
//	}
	
	
}
