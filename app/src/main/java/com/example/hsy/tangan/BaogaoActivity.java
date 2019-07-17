package com.example.hsy.tangan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hsy.tangan.ui.TitleLayout;

import java.util.Calendar;
import java.util.List;

public class BaogaoActivity extends AppCompatActivity implements View.OnClickListener {

    List<Tangan> tangans;

    TitleLayout titleLayout;
    TextView xuetangT, yinshiT, yundongT;
    boolean aaaa = true;

    //3.9-6.1mmol
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baogao);
        tangans = ((App) getApplication()).getTangans();
        titleLayout = findViewById(R.id.baogaotitle);

        titleLayout.changeIs(this);
        xuetangT = findViewById(R.id.xuetangT);
        yundongT = findViewById(R.id.yundongT);
        yinshiT = findViewById(R.id.yinshiT);
        if (tangans == null || tangans.size() == 0) {
            Toast.makeText(this, "暂时没有数据", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        ri();

    }

    public void zhou() {
        float t = 0, z = 0, d = 0, y = 0, x = 0;

        titleLayout.changeIsName("日报");
        int l = tangans.size() - 1;


        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DAY_OF_MONTH, 7);
        String s = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        int a = 0, b = 0, c = 0;

        for (int i = 0; l - i >= 0; i++) {
            Tangan tangan = tangans.get(l - i);
            if (tangan.getA().compareTo(s) > 0) {
                if (tangan.getXuetang() > 0) {
                    x += tangan.getXuetang();
                    a++;
                }
                if (tangan.getYundong() > 0) {
                    y += tangan.getYundong();
                    b++;
                }
                if (tangan.getDanbaizhi() > 0) {
                    t += tangan.getTanglei();
                    z += tangan.getZhifang();
                    d += tangan.getDanbaizhi();
                    c++;
                }
            } else {
                break;
            }

        }
        if(a==0)
            a=1;
        if(b==0)
            b=1;
        if(c==0)
            c=1;
        x=x/a;
        y=y/b;

        t=t/c;
        z=t/c;
        d=d/c;

        String xuetang = "", yundong = "", yinshi = "";

        xuetang = "本周平均血糖为" + x + "mmol; ";
        if (x > 8) {

            xuetang += "血糖略高，减少糖量摄入哦";

        } else if (x < 4.4) {
            xuetang += "血糖略低，注意多摄入高糖食物哦";
        } else {
            xuetang += "血糖数据合格";
        }

        yundong = "本周平均每天运动" + y + "千卡";
        if (y < 32) {

            yundong += "运动量还不够，需要继续加油哦";
        } else if (y > 96) {
            yundong += "运动量过多，运动也要注意适量哦";
        } else {
            yundong += "运动量适宜，继续保持哦";
        }

        float qwe = d + z + t;
        if (qwe == 0)
            qwe = 1;
        yinshi = "平均饮食比例为:    \n          糖类 " + (d / qwe) + "\n          脂肪" + (z / qwe) + "\n          蛋白质" + (d / qwe);
        if (t / qwe < 0.40) {
            yinshi += "\n          糖类食物摄入过少，推荐多使用淀粉食物; ";
        } else if (t / qwe > 0.75) {
            yinshi += "\n          糖类食物摄入过多，减少淀粉的摄入";
        }

        if (z / qwe > 0.3)
            yinshi += "\n          脂肪类食物摄入过多";

        if (d / qwe < 0.15) {
            yinshi += "\n          蛋白质类食物摄入过少,多吃蛋肉类食物";
        } else if (d / qwe > 0.30) {
            yinshi += "\n          蛋白质类食物摄入过多";
        }

        xuetangT.setText("      " + xuetang);
        yinshiT.setText("      " + yinshi);
        yundongT.setText("      " + yundong);
    }

    public void ri() {
        titleLayout.changeIsName("周报");
        String xuetang1 = "", xuetang2 = "", yundong = "没有今日运动数据哦", yinshi = "";
        Tangan tangan;
        Tangan t = new Tangan();

        int l = tangans.size() - 1;
        for (int i = 0; l - i >= 0 && i < 3; i++) {
            tangan = tangans.get(l - i);

            if (tangan.getA().equals(t.getA())) {
                int q = tangan.getZao();
                if (tangan.getXuetang() > 0) {
                    if (tangan.getXuetang() < 4.4) {

                        switch (q) {
                            case 1:
                                xuetang1 += "早晨 ";
                                break;
                            case 2:
                                xuetang1 += "中午 ";
                                break;
                            case 3:
                                xuetang1 += "晚上 ";
                                break;
                        }

                    } else if (tangan.getXuetang() > 8) {
                        switch (q) {
                            case 1:
                                xuetang2 += "早晨 ";
                                break;
                            case 2:
                                xuetang2 += "中午 ";
                                break;
                            case 3:
                                xuetang2 += "晚上 ";
                                break;
                        }

                    }

                }
                if (tangan.getDanbaizhi() > 0) {
                    int j = yinshi.length();
                    switch (q) {
                        case 1:
                            yinshi += "早晨 ";
                            break;
                        case 2:
                            yinshi += "中午 ";
                            break;
                        case 3:
                            yinshi += "晚上 ";
                            break;
                    }
                    float a, b, c;
                    a = tangan.getTanglei();
                    b = tangan.getZhifang();
                    c = tangan.getDanbaizhi();
                    float d = a + b + c;
                    a = a / d;
                    b = b / d;
                    //   Log.e("糖类比例",a+"a"+b);
                    if (a < 0.40) {
                        yinshi += "糖类食物摄入过少";
                    } else if (a > 0.75) {
                        yinshi += "糖类食物摄入过多";
                    } else if (b > 0.3)
                        yinshi += "脂肪类食物摄入过多";
                    c = c / d;
                    if (c < 0.15) {
                        yinshi += "蛋白质类食物摄入过少";
                    } else if (c > 0.30) {
                        yinshi += "蛋白质类食物摄入过多";
                    }
                    if (yinshi.length() == j + 3) {
                        yinshi = yinshi.substring(0, j);
                    }
                }
                if (tangan.getYundong() > 0) {
                    if (tangan.getYundong() < 32) {

                        int j = (int) ((32 - tangan.getYundong()) / 1.2 + 1);
                        yundong = "今日运动量还不够，可再进行" + j + "分钟跑步";
                    } else if (tangan.getYundong() > 96) {
                        yundong = "今日运动量过多，运动也要注意适量哦";
                    } else {
                        yundong = "今日运动量适宜，继续保持哦";
                    }
                }

            }
        }
        if (xuetang2.length() > 0)
            xuetang2 += "血糖略高，减少糖量摄入哦";
        if (xuetang1.length() > 0)
            xuetang1 += "血糖略低，注意多摄入高糖食物哦";
        if (yinshi.length() == 0)
            yinshi = "今日饮食数据适宜";
        if (xuetang1.length() == 0 && xuetang2.length() == 0) {
            xuetang1 = "今日血糖数据适宜";
        }

        if (xuetang1.length() == 0) {
            xuetangT.setText("      " + xuetang2);
        } else
            xuetangT.setText("      " + xuetang1 + "\n" + xuetang2);
        yinshiT.setText("       " + yinshi);
        yundongT.setText("      " + yundong);

    }

    @Override
    public void onClick(View v) {

        if (aaaa) {
            zhou();
        } else ri();
        aaaa = !aaaa;
    }
}
