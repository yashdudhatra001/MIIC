package com.yash1213.miic.Activity;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yash1213.miic.R;

public class OurStoryActivity extends AppCompatActivity {

    private ImageView imageBack;
    private TextView tvArticle, tvSamkit, tvPrathmesh;
    private TextView textMail;

    private String smkit = "Started with a dream which turned reality! MIIC is an unique kind of initiative which holds limitless potential and opportunities. The journey from starting it to nourishing it has been a great delight. I wish and strongly hope that its existence will contribute and prosper learnings for years to come!\n" +
            "\n" +
            "~ Samkit Kothari, Founder & President, MIIC";
    private String prathmesh = "If you have to bring a change, then be that change first. \n MIIC is a change that will sure help us to grow better!"
            +"\n\n"+ "~ Prathmesh Jadhav, Co-Founder & Associate Head Coordinator, MIIC";

    private String article = "Mechanical Industrial Interaction Cell (MIIC) is a non-profit and non-political organization established in June 2019, initiated and founded by Samkit Kothari, a former Department Representative and Prathmesh Jadhav (Batch 17-21) with the support of students and teachers of Mechanical Dept, FTE MSU Baroda, with a vision to create industry leaders of tomorrow by empowering students with proper training and career opportunities and with a mission to groom students meticulously and empower their employability potential by bridging the gap between industrial and academic learnings through various training and growth opportunities for the students of Mechanical Engg, Faculty of Technology and Engineering, The Maharaja Sayajirao University of Baroda.\n" +
            "\n" +
            "MIIC is a facilitation unit that is responsible for organizing various activities and training opportunities for enhancing soft skills, interpersonal skills as well as technical skills interacting with various industrial, technical and educational organisations, ensuring that the students get enough exposure of  opportunities. We organise various activities which help students in placement process as well as in future corporate life. Till now we have organized various group discussion, aptitude workshops, expert sessions, industrial visits, mock personal interviews, speak on sessions, campus to corporate workshops and also provide opportunities for training and internships to the students. \n" +
            "\n" +
            "MIIC's tagline \"Redefining Possibilities\" gives students a confidence to stand up on ones own potential and to give a positive start.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_our_story);

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textMail = findViewById(R.id.textMail);
        Linkify.addLinks(textMail,Linkify.EMAIL_ADDRESSES);
        //textMail.setMovementMethod(LinkMovementMethod.getInstance());

        tvArticle = findViewById(R.id.article);
        tvArticle.setText(article);

        tvSamkit = findViewById(R.id.tv_samkit);
        tvSamkit.setText(smkit);

        tvPrathmesh = findViewById(R.id.tv_prathmesh);
        tvPrathmesh.setText(prathmesh);

    }
}