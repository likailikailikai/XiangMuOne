package myapplicatio.atguigu.com.startallvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startAllVideo(View view) {
        Intent intent = new Intent();
//            //第一参数：播放路径
//            //第二参数：路径对应的类型
 //      intent.setDataAndType(Uri.parse("http://10.0.2.2:8080/yellow.mp4"), "video/*");
//        intent.setDataAndType(Uri.parse("http://192.168.1.30:8080/yellow.mp4"), "video/*");
 //       intent.setDataAndType(Uri.parse("http://192.168.191.1:8080/yellow.mp4"), "video/*");
//        intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2016/12/29/mp4/161229134943070513_480.mp4"), "video/*");
//        intent.setDataAndType(Uri.parse("http://192.168.1.49:8080/likai/T-ara---LittleApple_1.mp4"), "video/*");
 //       intent.setDataAndType(Uri.parse("http://192.168.1.49:8080/likai/budejie1.mp4"), "video/*");
        intent.setDataAndType(Uri.parse("http://192.168.1.49:8080/likai/hanban_xiaopingguo.mp4"), "video/*");
        startActivity(intent);
    }
    public void startFirstVideo(View view){
        Intent intent = new Intent();

        intent.setDataAndType(Uri.parse("http://192.168.1.49:8080/likai/shizaijiongtu.mp4"), "video/*");
        startActivity(intent);
    }
}
