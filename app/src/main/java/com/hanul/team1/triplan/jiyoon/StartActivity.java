package com.hanul.team1.triplan.jiyoon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanul.team1.triplan.R;
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

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {
    ViewPager pager;
    SessionCallback callback;

    public static final String TAG = "TESTLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jy_activity_main);

        /**카카오톡 로그아웃 요청**/
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });

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

    }

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

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

                    final String uuid = userProfile.getUUID();
                    final String nickname = userProfile.getNickname();

                    Log.e(TAG , uuid + "");
                    Log.e(TAG , nickname + "");

                    //DTO > JSON > byte[]
                    final byte[] bytes = DTOtoJSON(uuid, nickname);
                    //
                    onValidationSucceeded(bytes);

                    //로그인 성공 시 이동.
                    Intent intent = new Intent(StartActivity.this, SuccessActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
            // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
        }
    }

    //DTOtoJSON
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

    public void onValidationSucceeded(byte[] bytes) {

        UserClient userClient = UserClient.retrofit.create(UserClient.class);

        Call<ResponseBody> call = userClient.sendUuid(bytes);
        try {
            new KakaoChk().execute(call);
        } catch (Exception e) {
            Log.e(TAG , e.getLocalizedMessage());
        }
    }

    //AsyncTask
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
        }

        @Override
        protected void onPostExecute(final String result) {
            Log.d(TAG, result);

            switch (result != null? Integer.parseInt(result) : 0){
                case 1 :
                    Toast.makeText(StartActivity.this, "환영합니다.", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(R.id.successLinear), "환영합니다.", Snackbar.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(StartActivity.this, "안녕하세요.", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(R.id.successLinear), "중복된 아이디", Snackbar.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(StartActivity.this, "서버 응답이 없습니다.", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(R.id.successLinear), "서버 응답이 없습니다", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }//Post
    }//AsyncTask

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KakaoChk kakaoChk = new KakaoChk();
        if(kakaoChk.getStatus() == KakaoChk.Status.RUNNING)kakaoChk.cancel(true);
    }
}