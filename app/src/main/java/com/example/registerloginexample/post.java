package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.registerloginexample.databinding.ActivityPostBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Map;

public class post extends AppCompatActivity {

    //    private ActivityMainlistBinding binding;
    private ActivityPostBinding binding;

    String Title;  //제목
    String writer;  //저자
    String Content;  //소개글
    int Price;  //가격
    int PostNum; //게시글 고유 번호
    String userID;  //현재 로그인 한 사용자의 아이디
    String URL = "http://meanzoo.dothome.co.kr/Contentinsert.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        Title = intent.getStringExtra("Title");
        writer = intent.getStringExtra("Writer");
        Price = intent.getIntExtra("Price", -1);
        PostNum = intent.getIntExtra("POSTNUM", -1);
        Content = intent.getStringExtra("Content");
        userID = intent.getStringExtra("userID");

//        Log.i("postAct","가격" + Price);
//        Log.i("postAct","writer" + writer);
        Log.i("postAct", "포스트 " + PostNum);
        Log.i("postAct", "포스트 " + userID);

        binding.bookText.setText(Title);
        binding.authorText.setText(writer);
        binding.pricePost.setText("" + Price);
        binding.contentPost.setText(Content);

        final ArrayList<String> midList = new ArrayList<String>();
        ListView list = (ListView) findViewById(R.id.postlist);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, midList);
        list.setAdapter(adapter);

        final EditText edtItem = (EditText) findViewById(R.id.edtItem);
        Button btnSend = (Button) findViewById(R.id.btnSend);

        String text = "test";
        String post = "test";

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(jsonObject + "==========================jsonobject ==============================");

                    JSONArray jsondate = jsonObject.getJSONArray("response");
                    System.out.println(jsondate);
                    for (int i = 0; i < jsondate.length(); i++) {
                        midList.add(jsondate.getJSONObject(i).getString("COMMENT"));
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        postCommentRequest postCommentRequest = new postCommentRequest(post, PostNum, userID, text, responseListener);
        RequestQueue queue = Volley.newRequestQueue(post.this);
        queue.add(postCommentRequest);

        binding.postChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "수정", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "포스트를 수정합니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Update.class);
                                intent.putExtra("Title", Title);
                                intent.putExtra("Writer", writer);
                                intent.putExtra("Price", Price);
                                intent.putExtra("Content", Content);
                                intent.putExtra("POSTNUM", PostNum);
                                intent.putExtra("userID", userID);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), "본인의 게시물이 아닙니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                postRequest postRequest = new postRequest(PostNum, userID, responseListener);
//                postRequest.setShouldCache(false);

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(postRequest);
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
                startActivity(intent);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "뒤로갑니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("ddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
                midList.add(edtItem.getText().toString());
                adapter.notifyDataSetChanged();
                String text = edtItem.getText().toString();
                String post = "insert";

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };

                postCommentRequest postCommentRequest = new postCommentRequest(post, PostNum, userID, text, responseListener);
                RequestQueue queue = Volley.newRequestQueue(post.this);
                queue.add(postCommentRequest);
            }
        });


    }
}