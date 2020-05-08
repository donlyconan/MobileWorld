package com.team.mobileworld.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.team.mobileworld.R;

import java.text.DecimalFormat;

import maes.tech.intentanim.CustomIntent;

public class FeedbackActivity extends AppCompatActivity {
    private static final String EMAIL = "mobileworld.oscar@gmail.com";
    private static final int RQ_FEEDBACK = 100;

    EditText inpform, inpemailto,inpcontent;
    RatingBar ratingBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //Anh xa phan tu
        inpform = findViewById(R.id.inp_from);
        inpemailto = findViewById(R.id.inp_to);
        inpcontent = findViewById(R.id.inp_content);
        ratingBar = findViewById(R.id.ratingBar);
        toolbar = findViewById(R.id.toolbar);

        inpemailto.setText(EMAIL);
        inpform.setText(MainActivity.getUser().getEmail());
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(e->finish());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.it_send: {
                if(ratingBar.getRating() > 0.0f && inpcontent.getText().toString().length() > 0) {
                    String myemail = inpform.getText().toString();
                    String emailrp = inpemailto.getText().toString();
                    String title = String.format("Phải hồi từ người dùng %s.", MainActivity.getUser().getFullname());
                    String content = inpcontent.getText().toString();
                    DecimalFormat decimal = new DecimalFormat("#,##");
                    content = String.format("-Xếp hạng trải nghiệm: %s Sao\n-%s",decimal.format(ratingBar.getRating()), content);
                    final Intent intent = createIntentEmail(myemail, new String[]{emailrp}, title, content);
                    startActivityForResult(Intent.createChooser(intent, title), RQ_FEEDBACK);
                } else
                    LoginActivity.showToast(getBaseContext(), "Bạn đã bỏ trống vài phần!");

            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Intent createIntentEmail(String myemail, String[] emailrecipent, String title, String body) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + myemail));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, emailrecipent);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        return intent;
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
    }
}
