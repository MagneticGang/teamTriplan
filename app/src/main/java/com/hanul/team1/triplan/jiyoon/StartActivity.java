package com.hanul.team1.triplan.jiyoon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ggs.gnuserdto.GNUserDTO;
import com.hanul.team1.triplan.ggs.retrofit.GNUserClient;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.hanul.team1.triplan.ggs.ggs_NLogInActivity;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    //선언부
    ViewPager pager;
    SessionCallback callback;
    SharedPreferences sp;
    Button NLogInBtn;   //일반 로그인 버튼

    //구글
    GoogleSignInClient mGoogleSignInClient;
    SignInButton gsignInBtn;
    private static final int RC_SIGN_IN = 1001;
    String Guserid;
    String Gnickname;

    //로그
    public static final String TAG = "CKLOG";
    public static final String SORRY = "서버가 응답하지 않습니다.\n관리자에게 문의하세요!";

    //onCreate 시작
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jy_activity_main);

        //일반 로그인 시작

        NLogInBtn = findViewById(R.id.NLogInBtn);
        NLogInBtn.setOnClickListener(new View.OnClickListener() {   //버튼 누르면 일반 로그인 창으로
            @Override
            public void onClick(View v) {
                Intent toNLogIn = new Intent(StartActivity.this, ggs_NLogInActivity.class);
                startActivity(toNLogIn);
            }
        });

        //일반 로그인 끝


        //구글 시작

        //구글 로그인 클라 생성
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("1039848715741-lrpb28mq92bdk7sqs7v4bb53vv2jl6o5.apps.googleusercontent.com")//클라이언트 ID를 가지고 IdToken을 요청하기.
                        .requestEmail()
                        .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //구글 로그아웃
        mGoogleSignInClient.signOut();

        //로그인 버튼 찾기
        gsignInBtn = findViewById(R.id.sign_in_google_btn);

        //로그인 버튼에 이벤트
        gsignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gSignIn();
            }
        });

        //로그인 하면 SharedPreferences에 유저 정보를 저장하도록 나중에!

        //구글 끝


        //카톡 시작

        /**카카오톡 로그아웃 요청**/
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });//카톡 로그아웃 요청 끝

        //카톡 끝

        //이미지 슬라이드 시작
        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());

        Fragment1 fragment1 = new Fragment1();
        adapter.addItem(fragment1);

        Fragment2 fragment2 = new Fragment2();
        adapter.addItem(fragment2);

        Fragment3 fragment3 = new Fragment3();
        adapter.addItem(fragment3);

        pager.setAdapter(adapter);


        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        //이미지 슬라이드 끝


    }
    //onCreate 끝

    //onActivityResult()
    //Intent 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //구글 로그인 시작

        //Intent 결과 처리
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGSignInResult(task);
        }

        //구글 로그인 끝

        //카톡 로그인 시작

        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        //카톡 로그인 끝
        super.onActivityResult(requestCode, resultCode, data);

    }//onActivityResult

    //onDestroy
    //자원회수.
    //앱이 닫힐 때 AsyncTask가 실행중이라면 종료한다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //카톡 로그인 AsyncTask 종료
        KakaoChk kakaoChk = new KakaoChk();
        if(kakaoChk.getStatus() == KakaoChk.Status.RUNNING)kakaoChk.cancel(true);
    }//onDestroy

    //구글 로그인 시작

    //구글 로그인에 성공하여 자체 서버DB에 저장된 유저의 정보를 SP에 저장..
    private void saveGUserInfoToSP(){

//    sp에 구글 로그인한 유저의 정보를 저장. 메소드로 따로 뺄 것.
//    원래 보안상 절대 이메일로 유저를 구분하지 마라고 한다. 그런데 아직 구현하기 힘들기 때문에 이메일로 구분함..
        sp = getSharedPreferences("userProfile", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();                             //전에 로그인한 유저가 있으면 삭제.
        editor.putString("userid", Guserid);
        editor.putString("nickname", Gnickname);
        editor.commit();

        //로그인 성공 시 이동.
        Intent intent = new Intent(StartActivity.this, SuccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //구글 로그인 유저 회갑 처리. 네트워킹 AsyncTask
    private class AGUserSignUp extends AsyncTask<Call, Void, String>{

        @Override
        protected String doInBackground(Call... params) {

            Call<ResponseBody> call = params[0];//
            try{
                Response<ResponseBody> res = call.execute();
                return res.body().string().trim();

            }catch (Exception e){
                Toast.makeText(StartActivity.this, SORRY, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "서버와 통신실패: "+e.getLocalizedMessage());
                return null;
            }
        }

        //구글 로그인 유저 회갑 요청 시 UI 변경
        @Override
        protected void onPostExecute(String result) {

            Log.d(TAG, "서버 응답: "+ result);

            switch (result != null ? Integer.parseInt(result) : 0){
                case 1:
                    Toast.makeText(StartActivity.this, Gnickname+" 님, 회원 가입을 축하드립니다!", Toast.LENGTH_SHORT).show();
                    saveGUserInfoToSP();    //SP저장 및 화면전환
                    break;
                case -1:
                    Toast.makeText(StartActivity.this, "환영합니다, "+ Gnickname + " 님!", Toast.LENGTH_SHORT).show();
                    saveGUserInfoToSP();    //SP저장 및 화면전환
                    break;
                default:
                    Toast.makeText(StartActivity.this, SORRY, Toast.LENGTH_SHORT).show();
                    ActivityCompat.finishAffinity(StartActivity.this);//앱 종료
                    break;
            }
        }
    }//AGUserSignUp

    //유저 정보를 DTO로, DTO를 JSON String으로, JSON String을 Byte[]로
    private byte[] infoToByte(String userid, String nickname){

        GNUserDTO dto = new GNUserDTO(userid, nickname);
        Gson g = new GsonBuilder().create();
        byte[] infotobytes = null;
        try{
            infotobytes = g.toJson(dto).getBytes("utf-8");
            Log.d(TAG, "파싱 결과: " + infotobytes);
        }catch (Exception e){
            Toast.makeText(this, SORRY, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "파싱 실패: " + e.getLocalizedMessage()); }
        return infotobytes;
    }

    //구글 로그인 성공 시 처리
    //구글로그인하면 유저 값을 가져와 저장 처리 및 환영인사.
    private void loggedGUserSave(GoogleSignInAccount account){

        //접근
        Guserid = account.getEmail();
        Gnickname = account.getDisplayName();
        Log.d(TAG, "구글로그인한 유저의 정보: "+ Guserid + ", " + Gnickname);

        //DB에 저장
        //먼저 id와 닉네임에 해당하는 값을 DTO로.
        byte[] infobytes = infoToByte(Guserid, Gnickname);

        //byte[]로 네트워킹 시작
        GNUserClient gnUserClient = GNUserClient.retrofit.create(GNUserClient.class);
        Call<ResponseBody> call = gnUserClient.GUserSignUp(infobytes);
        try{
            //AsyncTask에서 retrofit으로 네트워킹.
            new AGUserSignUp().execute(call);
        }catch (Exception e){
            Log.e(TAG, "구글 로그인 유저 회원가입 실패. AsyncTask 실행 시 에러: " + e.getLocalizedMessage());
            Toast.makeText(this, SORRY, Toast.LENGTH_LONG).show();
        }

    }//loggedGUserSave

    //구글 로그인 Intent 처리
    private void handleGSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            //구글 로긴 성공 시 account에 로그인 유저 정보 담김
            GoogleSignInAccount account = completedTask.getResult(ApiException.class); //이 값이 있는 게 UI 변경 조건이 될 예정.
            //이 다음부터는 구글 로그인 후, 구글로부터 아이디를 특정할 수 있는 IdToken값을 받아
            //SharedPreferences에 저장하는 처리. 파베가 아닌 자체 서버를 사용할 예정이다.

            //구글 로그인 후 처리
            loggedGUserSave(account);

        } catch (ApiException e) {
            Log.w(TAG, "구글 로그인 실패 원인 코드: "+e.getStatusCode());
            //코드내용: https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInStatusCodes

            //모든 Activity 종료
            Toast.makeText(this, SORRY, Toast.LENGTH_LONG).show();
            ActivityCompat.finishAffinity(StartActivity.this);
        }
    }

    //로그인 버튼 이벤트 메소드
    private void gSignIn(){
        //구글 로그인창 띄우기
        Intent gSignIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(gSignIntent, RC_SIGN_IN);//구글 로그인 인텐트 응답 코드는 1001
    }

    //구글 로그인 끝

    //이미지 슬라이드 시작
    class MainPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }//이미지 슬라이드 끝

    //카톡 로그인 시작

    //카톡 로그인 성공/실패 처리 메소드
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

                    final String uuid = String.valueOf(userProfile.getId());
                    final String nickname = userProfile.getNickname();

                    Log.e(TAG , "userid로 들어갈 값: " + uuid);
                    Log.e(TAG , "nickname로 들어갈 값: " + nickname);

                    //DTO > JSON > byte[]
                    final byte[] bytes = DTOtoJSON(uuid, nickname);
                    //
                    kakaoLogin(bytes);

                    Toast.makeText(StartActivity.this, "안녕하세요, " + nickname + " 님!", Toast.LENGTH_SHORT).show();

                    //로그인 성공했으면 SP에 값 저장.
                    sp = getSharedPreferences("userProfile", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();                             //전에 로그인한 유저가 있으면 삭제.
                    editor.putString("userid", uuid);
                    editor.putString("nickname", nickname);
                    editor.commit();

                    //로그인 성공 시 이동.
                    Intent intent = new Intent(StartActivity.this, SuccessActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);//이동 전에 켜져있던 activity 클리어.
                    startActivity(intent);
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
            // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
            //로그아웃 했을 때도 뜸.
        }
    }

    //카톡 로그인에 쓸 DTOtoJSON
    public byte[] DTOtoJSON(String uuid, String nickname) {
        MemberDTO dto = new MemberDTO(uuid, nickname);

        Gson gson = new GsonBuilder().create();

        byte[] byteJson = null;

        try {
            byteJson = gson.toJson(dto).getBytes("utf-8");

            Log.d(TAG, gson.toJson(dto));   //파싱 결과
            Log.d(TAG, byteJson + "");    //byte코드로 인코딩 결과

        } catch (Exception e) {
            Log.d(TAG, "변환 불가능");
        }
        return byteJson;
    }

    //카톡 로그인 AsyncTask를 실행하는 메소드.
    public void kakaoLogin(byte[] bytes) {

        UserClient userClient = UserClient.retrofit.create(UserClient.class);

        Call<ResponseBody> call = userClient.sendUuid(bytes);
        try {
            new KakaoChk().execute(call);
        } catch (Exception e) {
            Log.e(TAG , e.getLocalizedMessage());
        }
    }

    //카톡 로그인 AsyncTask
    private class KakaoChk extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call... params) {
            try {
                Call<ResponseBody> call = params[0];
                Response<ResponseBody> response = call.execute();

                return response.body().string().trim();
            }catch (Exception e){
                Log.e(TAG, e.getLocalizedMessage());
            }
            return null;
        }//
    }

    //카톡 로그인 끝



}