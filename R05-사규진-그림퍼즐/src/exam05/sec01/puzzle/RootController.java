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
	
	
	//�̹��� �ҷ����� �ڸ���
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
	
	//���� �߻���
	Random random = new Random();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//�̹��� ���� �����
		makeCorrect();
		Image[] correct = new Image[9];
		
		//���� ���ϱ�
		for(int i = 0; i < makeCorrect().length; i++)
		{
			correct[i] = makeCorrect()[i];
		}
		
		//�̹��� ����
		startBtn.setOnAction(event -> handleStartBtn(event));
		
		newstart.setOnAction(event -> handleStartBtn(event));
		
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
//					System.out.println(event.getSource());
					
					findNeighber(id);
					correct(correct);
					
				}
				
			});
		}
		
		//Ŭ���� �ҽ� ���� �ٸ����
//		if(event.getSource() == ImageViewList.get(0)){
//			System.out.println("����");
//		} else {
//			System.out.println("�ٸ�");
//		}
		
		//�̹��� ��ȯ
//				Image temp_image = img0.getImage();
//				img1.setImage(temp_image);

	}
	
	//�̹��� �ڸ���
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
	
	//���� ����
	public void handleStartBtn(ActionEvent event) {
		System.out.println("���۹�ư ����");
		
		//�߸� �̹��� ����
		List<WritableImage> list = Arrays.asList(cropped_arr);
		Collections.shuffle(list);
		
		//�迭�� �ٽ� ��ȯ
		WritableImage[] s_cropped_arr = list.toArray(new WritableImage[list.size()]);

		
		
//		s_cropped_arr[img_ran_num] = null;
		
		//�ڸ� �̹����� FXML �̹�����id����Ʈ�� ���� => ���� �̹��� ��ġ
		for(int i = 0; i < s_cropped_arr.length; i++){
			
			System.out.println(s_cropped_arr[i]);
//			if(s_cropped_arr[i] == null){
//				System.out.println("�� 8��");
//				ImageViewList.get(8).setImage(s_cropped_arr[i]);
//			} else {
//				System.out.println("�� i��");
				ImageViewList.get(i).setImage(s_cropped_arr[i]);
				ImageViewList.get(i).setDisable(false); //�̹��� Ŭ�� ����
			}
		//Ư���� ĭ�� �۵� �� �̹��� ����
		int img_ran_num = random.nextInt(9);
//		ImageViewList.get(img_ran_num).setDisable(true); //�̹��� Ŭ�� �Ұ�
		ImageViewList.get(img_ran_num).setImage(null);
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
		Image[] correct_image = new Image[9];
		
		for(int i = 0; i < correct_image.length; i++){
			correct_image[i] = (Image) cropped_arr[i];
		}
			
		return correct_image;
		
	}

	//��� �̺�Ʈ �ڵ鷯
//	public void play(ActionEvent event){
//		Media media = new Media(getClass().getResource("media/media.mp4").toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(media);
//		player.setMediaPlayer(mediaPlayer);
//		mediaPlayer.setAutoPlay(true);
//	}
	
	
	//��� �޼ҵ�
//	public void play2(){
//		Media media = new Media(getClass().getResource("media/media.mp4").toString());
//		MediaPlayer mediaPlayer = new MediaPlayer(media);
//		player.setMediaPlayer(mediaPlayer);
//		mediaPlayer.setAutoPlay(true);
//	}
}
