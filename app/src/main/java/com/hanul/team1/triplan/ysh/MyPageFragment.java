package com.hanul.team1.triplan.ysh;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.jiyoon.StartActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;


public class MyPageFragment extends Fragment {      //implements Validator.ValidationListener

    //선언
    TextView TVnick;
    Button BtnSuggest, BtnPwdReset, BtnLogout, BtnSignout;
    SharedPreferences sp;
    private Validator validator;

    //유효
//    LinearLayout LL;
//    @NotEmpty(message = "비밀번호를 입력해주세요.")
//    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS, message = "비밀번호는 6자 이상, 알파벳 대·소문자,\n 숫자, 특수기호를 포함해야됩니다.")
//    EditText PwdET;
//    @ConfirmPassword(message = "비밀번호가 일치하지 않습니다.")
//    EditText PwdCFET;

    //비번 입력 ET 생성
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        final EditText pwd = new EditText(getActivity());
//        pwd.setTag();
//        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sh_fragment_mypage, container, false);

        //찾기
        TVnick = rootView.findViewById(R.id.TVnick);
        BtnSuggest = rootView.findViewById(R.id.BtnSuggest);
        BtnPwdReset = rootView.findViewById(R.id.BtnPwdReset);
        BtnLogout = rootView.findViewById(R.id.BtnLogout);
        BtnSignout = rootView.findViewById(R.id.BtnSignout);



        //annotation으로부터 입력값을 받는다
//        validator = new Validator(getActivity());
//        //결과물을 받는다.
//        validator.setValidationListener(this);

        //TV에 닉네임 출력
        sp = getActivity().getSharedPreferences("userProfile",Activity.MODE_PRIVATE);
        String nickname = sp.getString("nickname", "서버와 통신실패!");
        TVnick.setText(nickname);
        TVnick.append(" 's\nMy Page");

        //비밀번호 재설정 버튼
        BtnPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //비번 재설정 dialog
//                DLPwdReset();
            }
        });

        //로그아웃 버튼
        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLconfirm();//dialog로 확인 취소
            }
        });

        return rootView;
    }//onCreateView

//유효성 검사 통과
//    @Override
//    public void onValidationSucceeded() {
//        Toast.makeText(getActivity(), "유효한 비밀번호입니다", Toast.LENGTH_SHORT).show();
//    }
////유효성 검사 미통과
//    @Override
//    public void onValidationFailed(List<ValidationError> errors) {
//        for (ValidationError error : errors){
//            View view = error.getView();
//            String message = error.getCollatedErrorMessage(getActivity());
//            if(view instanceof EditText){((EditText)view).setError(message);}
//            else{Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();}
//        }
//    }

    //비번 재설정 버튼 누르면 비번 입력 다이얼로그창이 뜨기
//    private void DLPwdReset(){

//        //dialog에 적용할 외부 xml
////        final LinearLayout LLPwd = (LinearLayout) getLayoutInflater().inflate(R.layout.ggs_dialog_pwdvalidator, null);
//
//
//        //dialog 생성
//        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
//            .setTitle("비밀번호 재설정")
//            .setView()
//            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//
//                }
//            })
//            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }).create();
//        dialog.show();//띄우기
//
//
//    }//DLPwdReset

    //로그아웃 버튼 누르면 로그아웃 할거냐고 물어보는 dialog창 띄우기
    private void DLconfirm(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle("안내")
            .setMessage("로그아웃 하시겠습니까?")
            .setIcon(R.drawable.airplane)
                //예
            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //sp clear
                    sp = getActivity().getSharedPreferences("userProfile",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();

                    //로그인 화면으로
                    Toast.makeText(getActivity(), "이용해 주셔서 고맙습니다 ^^♥", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), StartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            })
                //아니요
            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();//닫기
                }
        }).show();

    }//DLconfirm


}//c
