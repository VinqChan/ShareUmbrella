package com.dangong.oksan.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dangong.oksan.R;
import com.dangong.oksan.activity.base.BaseActivity;
import com.dangong.oksan.callback.ApiCallBack;
import com.dangong.oksan.constants.Constants;
import com.dangong.oksan.util.permission.PermissionCenter;
import com.dangong.oksan.api.ApiUtils;
import com.dangong.oksan.util.permission.PermissionCallBack;
import com.lljjcoder.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    private static final String KEY_PHONE = "login_phone_num";
    private static final String KEY_PASSWORD= "login_password";
    @BindView(R.id.login_phone_num_tv)
    EditText loginPhoneNumTv;
    @BindView(R.id.login_clear_btn)
    ImageButton loginClearBtn;
    @BindView(R.id.login_pwd_tv)
    EditText loginPwdTv;
    @BindView(R.id.phone_login_btn)
    Button phoneLoginBtn;
    @BindView(R.id.verfycode_login_btn)
    Button verfycodeLoginBtn;
    @BindView(R.id.login_edit_layout)
    LinearLayout loginEditLayout;
    @BindView(R.id.forget_pwd_tv)
    TextView forgetPwdTv;
    @BindView(R.id.register_tv)
    TextView registerTv;
    @BindView(R.id.title_back_iv)
    ImageView titleBackIv;
    @BindView(R.id.title_tv)
    TextView titleTv;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public String setTitle() {
        return getString(R.string.login);
    }

    @Override
    public void initView() {
        super.initView();
        titleBackIv.setVisibility(View.GONE);
        String phone = SPUtils.getInstance().getString(KEY_PHONE);
        String password = SPUtils.getInstance().getString(KEY_PASSWORD);
        if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)){
            loginPhoneNumTv.setText(phone);
            loginPwdTv.setText(password);
        }
        WindowManager wm = (WindowManager) (getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        Log.e(TAG, " height *  width: "+ dm.heightPixels  +" * "+dm.widthPixels);
    }

    @Override
    public void init() {
        super.init();

        PermissionCenter.getInstance().checkPermission(this, new PermissionCallBack() {

            @Override
            public void onSuccess(String permission) {

            }

            @Override
            public void onfail(String permission) {

            }
        }, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    @OnClick({R.id.login_clear_btn, R.id.phone_login_btn, R.id.verfycode_login_btn, R.id.forget_pwd_tv, R.id.register_tv, R.id.title_back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_clear_btn:

                break;
            case R.id.phone_login_btn:
                 login();
                break;
            case R.id.verfycode_login_btn:
               ActivityUtils.startActivity(VerfyCodeLoginActivity.class);
               break;
            case R.id.forget_pwd_tv:
                ActivityUtils.startActivity(ForgetPasswordActivity.class);
                break;
            case R.id.register_tv:
                ActivityUtils.startActivity(RegisterActivity.class);
                break;
        }
    }

    private void login() {

        final String phoneNum = loginPhoneNumTv.getText().toString().trim();
        final String password = loginPwdTv.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showShort("请输入手机号码！");
            return;
        }
        if (!RegexUtils.isMobileExact(phoneNum)) {
            ToastUtils.showShort("请输入正确的手机号码！");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        startLoading();
        ApiUtils.pswlogin(phoneNum, password, new ApiCallBack() {
            @Override
            public void success(Object response) {
                stopLoading();
                //ToastUtils.showShort("登录成功！");
               // Constants.loginInfo.setRoleId(Constants.ROLEID_MANAGER); //TODO test
                if(Constants.loginInfo.getRoleId() .equals(Constants.ROLEID_GUIDE)){//导游
                    if(Constants.loginInfo.getRoleId().equals(Constants.CHECKSTATE_YES)){//审核通过
                        ActivityUtils.startActivity(AddShopMapActivity.class);
                    }else {
                        ActivityUtils.startActivity(MainActivity.class);
                    }
                }else  if(Constants.loginInfo.getRoleId() .equals(Constants.ROLEID_MANAGER)){//管理人员
                    Bundle bundle = new Bundle();
                    bundle.putInt(ManagerAndMaintainMainActivity.TYPE_KEY, ManagerAndMaintainMainActivity.TYPE_MANAGER);
                    ActivityUtils.startActivity(bundle, ManagerAndMaintainMainActivity.class);
                }else if(Constants.loginInfo.getRoleId() .equals(Constants.ROLEID_STAFF)){//维护人员
                    ActivityUtils.startActivity(MaintainMainActivity.class);
                }
                SPUtils.getInstance().put(KEY_PHONE,phoneNum);
                SPUtils.getInstance().put(KEY_PASSWORD,password);
            }

            @Override
            public void fail() {
                stopLoading();
            }
        });
    }

    @Override
    protected void onDestroy(

    ) {
        super.onDestroy();
    }

}
