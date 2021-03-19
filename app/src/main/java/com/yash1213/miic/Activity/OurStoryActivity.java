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
    private TextView tvArticle, tvMission, tvVision;
    private TextView textMail;

    private String vision = " To create industry leaders of tomorrow by empowering students through proper " +
            "training and career opportunity.";

    private String mission = " To groom students meticulously and empower their employability " +
            "potential by bridging the gap between industrial and academic learnings " +
            "through various training and growth opportunity.";

    private String article = " Mechanical Industrial Interaction Cell (MIIC) is a Non Government ," +
            " Non profit facilitation unit that is organising various activities and training " +
            "opportunities to enhance soft skills and technical skills amongst the students of" +
            " Mechanical department MSU , Baroda. This cell was founded by former Department " +
            "Representative Samkit Kothari and Prathmesh Jadhav in 2019 with the support of " +
            "entire mechanical department students and faculty. \n" +
            "\n" +
            "   We organise various activities which help students in placement process and in " +
            "future corporate life. Till now we had organized Group discussion , " +
            "Aptitude workshops , Mock Personal interview , speak on session , campus to corporate" +
            " workshops and will provide internships to students in future. After every successful" +
            " activity we plant a tree at our campus. So in future we want a big garden , not only of" +
            " trees but of success and accomplishments of students.\n";

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

        tvVision = findViewById(R.id.vision);
        tvVision.setText(vision);

        tvMission = findViewById(R.id.mission);
        tvMission.setText(mission);

        tvArticle = findViewById(R.id.article);
        tvArticle.setText(article);
    }
}