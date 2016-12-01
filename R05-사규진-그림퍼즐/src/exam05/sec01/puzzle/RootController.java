package exam05.sec01.puzzle;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;

public class RootController implements Initializable{
	@FXML private List<ImageView> ImageViewList;
	@FXML private Button startBtn;
	@FXML private MenuItem newstart;
	
	
	//이미지 불러오고 자르기
	Image original_image = new Image(getClass().getResource("images/main.jpg").toExternalForm(), 600, 600, false, true);
	PixelReader before_crop = original_image.getPixelReader();
	WritableImage cropped = new WritableImage(before_crop, 0, 0, 200,200);
	WritableImage cropped1 = new WritableImage(before_crop, 200, 0, 200,200);
	WritableImage cropped2 = new WritableImage(before_crop, 400, 0, 200,200);
	WritableImage cropped3 = new WritableImage(before_crop, 0, 200, 200,200);
	WritableImage cropped4 = new WritableImage(before_crop, 200, 200, 200,200);
	WritableImage cropped5 = new WritableImage(before_crop, 400, 200, 200,200);
	WritableImage cropped6 = new WritableImage(before_crop, 0, 400, 200,200);
	WritableImage cropped7 = new WritableImage(before_crop, 200, 400, 200,200);
	WritableImage cropped8 = new WritableImage(before_crop, 400, 400, 200,200);
	
	WritableImage[] cropped_arr = {cropped,cropped1,cropped2,cropped3,cropped4,cropped5,cropped6,cropped7,cropped8}; 
	
	
	
	private int[] nb = new int[4];
	
	//랜덤 발생기
	Random random = new Random();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//이미지 정답 만들기
		makeCorrect();
		Image[] correct = new Image[9];
		
		//정답 비교하기
		for(int i = 0; i < makeCorrect().length; i++)
		{
			correct[i] = makeCorrect()[i];
		}
		
		//이미지 삽입
		startBtn.setOnAction(event -> handleStartBtn(event));
		
		newstart.setOnAction(event -> handleStartBtn(event));
		
		//이미지뷰 클릭 이벤트
		for(ImageView image : ImageViewList){
			image.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					//이미지뷰의 id 값을 얻은 뒤 숫자만 잘라서 int형으로 변환
					ImageView iv = (ImageView) event.getSource();
					String before_id = iv.getId();
					int id = Integer.parseInt(before_id.substring(3));
					System.out.println(id);
//					System.out.println(event.getSource());
					
					findNeighber(id);
					correct(correct);
					
				}
				
			});
		}
		
		//클릭된 소스 같고 다름방식
//		if(event.getSource() == ImageViewList.get(0)){
//			System.out.println("같음");
//		} else {
//			System.out.println("다름");
//		}
		
		//이미지 변환
//				Image temp_image = img0.getImage();
//				img1.setImage(temp_image);

	}
	
	//이미지 자르기
//	public WritableImage[] cutter(){
//		Image original_image = new Image(getClass().getResource("images/main.jpg").toExternalForm(), 300, 300, false, true);
//		PixelReader before_crop = original_image.getPixelReader();
//		WritableImage cropped = new WritableImage(before_crop, 0, 0, 100,100);
//		WritableImage cropped1 = new WritableImage(before_crop, 100, 0, 100,100);
//		WritableImage cropped2 = new WritableImage(before_crop, 200, 0, 100,100);
//		WritableImage cropped3 = new WritableImage(before_crop, 0, 100, 100,100);
//		WritableImage cropped4 = new WritableImage(before_crop, 100, 100, 100,100);
//		WritableImage cropped5 = new WritableImage(before_crop, 200, 100, 100,100);
//		WritableImage cropped6 = new WritableImage(before_crop, 0, 200, 100,100);
//		WritableImage cropped7 = new WritableImage(before_crop, 100, 200, 100,100);
//		WritableImage cropped8 = new WritableImage(before_crop, 200, 200, 100,100);
//		
//		WritableImage[] cropped_arr = {cropped,cropped1,cropped2,cropped3,cropped4,cropped5,cropped6,cropped7,cropped8}; 
//		
//		return cropped_arr;
//	}
	
	//게임 시작
	public void handleStartBtn(ActionEvent event) {
		System.out.println("시작버튼 눌림");
		
		//잘린 이미지 섞기
		List<WritableImage> list = Arrays.asList(cropped_arr);
		Collections.shuffle(list);
		
		//배열로 다시 변환
		WritableImage[] s_cropped_arr = list.toArray(new WritableImage[list.size()]);

		
		
//		s_cropped_arr[img_ran_num] = null;
		
		//자른 이미지를 FXML 이미지뷰id리스트에 대입 => 섞인 이미지 배치
		for(int i = 0; i < s_cropped_arr.length; i++){
			
			System.out.println(s_cropped_arr[i]);
//			if(s_cropped_arr[i] == null){
//				System.out.println("난 8번");
//				ImageViewList.get(8).setImage(s_cropped_arr[i]);
//			} else {
//				System.out.println("난 i번");
				ImageViewList.get(i).setImage(s_cropped_arr[i]);
				ImageViewList.get(i).setDisable(false); //이미지 클릭 가능
			}
		//특정한 칸만 작동 및 이미지 삭제
		int img_ran_num = random.nextInt(9);
//		ImageViewList.get(img_ran_num).setDisable(true); //이미지 클릭 불가
		ImageViewList.get(img_ran_num).setImage(null);
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
		
		int count = 0;
		
		for(int i = 0; i < Image_arr.length; i++){
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
	}
	
	//정답 만들기
	public Image[] makeCorrect(){
		
		//이미지 배열에 자른 이미지를 차례로 입력 후 리턴하는 함수
		Image[] correct_image = new Image[9];
		
		for(int i = 0; i < correct_image.length; i++){
			correct_image[i] = (Image) cropped_arr[i];
		}
			
		return correct_image;
		
	}

	//재생 이벤트 핸들러
//	public void play(ActionEvent event){
//		Media media = new Media(getClass().getResource("media/media.mp4").toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(media);
//		player.setMediaPlayer(mediaPlayer);
//		mediaPlayer.setAutoPlay(true);
//	}
	
	
	//재생 메소드
//	public void play2(){
//		Media media = new Media(getClass().getResource("media/media.mp4").toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(media);
//		player.setMediaPlayer(mediaPlayer);
//		mediaPlayer.setAutoPlay(true);
//	}
}
