package com.dangong.oksan.activity;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dangong.oksan.R;
import com.dangong.oksan.api.ApiUtils;
import com.dangong.oksan.callback.ApiCallBack;
import com.dangong.oksan.constants.Constants;
import com.dangong.oksan.model.ScannerModel;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.common.Scanner;
import com.mylhyl.zxing.scanner.result.URIResult;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 扫描
 */
public class ScannerActivity extends DeCodeActivity {

    public static final int EXTRA_LASER_LINE_MODE_0 = 0;
    public static final int EXTRA_LASER_LINE_MODE_1 = 1;
    public static final int EXTRA_LASER_LINE_MODE_2 = 2;
    public static final String SCANNER_KEY = "SCANNER_KEY";
    public static final int TYPE_OPEN_SAN_CANG = 1;//开伞仓锁
    public static final int TYPE_REMOVE = 2;//撤机
    public static final int TYPE_ADDSHOP = 3;//添加商铺
    public static final int TYPE_OPEN_SAN_CAO = 4;//开伞槽


    public static final int APPLY_READ_EXTERNAL_STORAGE = 0x111;
    @BindView(R.id.scanner_view)
    ScannerView mScannerView;
    @BindView(R.id.title_back_iv)
    ImageView titleBackIv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.own_info_iv)
    ImageView ownInfoIv;

    private Result mLastResult;
    private int scannerType = 0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_scanner;
    }

    @Override
    public String setTitle() {
        return getString(R.string.scanner_borrow);
    }

    @Override
    public void init() {
        super.init();

        initScannerView();
    }

    @Override
    public void initView() {
        super.initView();
        scannerType = getIntent().getIntExtra(SCANNER_KEY, 0);
        if (scannerType == TYPE_ADDSHOP) {
            setTitle("新增商铺");
        } else if (scannerType == TYPE_REMOVE) {
            setTitle("撤机");
        } else if (scannerType == TYPE_OPEN_SAN_CAO) {
            setTitle("开伞槽");
        } else {
            setTitle("扫码借伞");
        }
    }

    private void initScannerView() {
        mScannerView.setOnScannerCompletionListener(this);
        int laserMode = 0;
        int scanMode = 1;
        showThumbnail = false;
        mScannerView.setMediaResId(R.raw.beep);//设置扫描成功的声音
        mScannerView.setDrawText("将二维码放入框内，即可自动扫描", true);
        mScannerView.setDrawTextColor(Color.WHITE);
        mScannerView.setLaserColor(getResources().getColor(R.color.main_color));
        mScannerView.setLaserFrameBoundColor(getResources().getColor(R.color.main_color));

        if (scanMode == 1) {
            //二维码
            mScannerView.setScanMode(Scanner.ScanMode.QR_CODE_MODE);
        } else if (scanMode == 2) {
            //一维码
            mScannerView.setScanMode(Scanner.ScanMode.PRODUCT_MODE);
        }

        //显示扫描成功后的缩略图
        mScannerView.isShowResThumbnail(showThumbnail);
//        //全屏识别
//        mScannerView.isScanFullScreen(true);
//        //隐藏扫描框
//        mScannerView.isHideLaserFrame(false);
//        mScannerView.isScanInvert(true);//扫描反色二维码
//        mScannerView.setCameraFacing(CameraFacing.FRONT);
//        mScannerView.setLaserMoveSpeed(1);//速度

//        mScannerView.setLaserFrameTopMargin(100);//扫描框与屏幕上方距离
//        mScannerView.setLaserFrameSize(400, 400);//扫描框大小
//        mScannerView.setLaserFrameCornerLength(25);//设置4角长度
//        mScannerView.setLaserLineHeight(5);//设置扫描线高度
//        mScannerView.setLaserFrameCornerWidth(5);

        switch (laserMode) {
            case EXTRA_LASER_LINE_MODE_0:
                mScannerView.setLaserLineResId(R.mipmap.wx_scan_line);//线图
                break;
            case EXTRA_LASER_LINE_MODE_1:
                mScannerView.setLaserGridLineResId(R.mipmap.zfb_grid_scan_line);//网格图
                mScannerView.setLaserFrameBoundColor(0xFF26CEFF);//支付宝颜色
                break;
            case EXTRA_LASER_LINE_MODE_2:
                mScannerView.setLaserColor(Color.RED);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == APPLY_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  PickPictureTotalActivity.gotoActivity(ScannerActivity.this);
            } else {
                Toast.makeText(ScannerActivity.this, "请给予权限", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {
        mScannerView.onResume();
        resetStatusView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mLastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void restartPreviewAfterDelay(long delayMS) {
        mScannerView.restartPreviewAfterDelay(delayMS);
        resetStatusView();
    }

    private void resetStatusView() {
        mLastResult = null;
    }

    @Override
    public void onResultActivity(Result result, ParsedResultType type, Bundle bundle) {
        super.onResultActivity(result, type, bundle);
        switch (type) {
            case TEXT:

                ToastUtils.showShort(bundle.getString(Scanner.Scan.RESULT));
                break;
            case URI:
                URIResult uriResult = (URIResult) bundle.getSerializable(Scanner.Scan.RESULT);
                String[] split = uriResult.getUri().split("/");
                if (split.length != 0) {

                    scanner(split[split.length - 1]);
                } else {
                    ToastUtils.showShort("扫码失败！");
                }

                break;
        }
    }

    private void scanner(final String url) {
        switch (scannerType){
            case TYPE_ADDSHOP:
                ApiUtils.scanner(url, new ApiCallBack() {
                    @Override
                    public void success(Object response) {
                        ScannerModel model = ((ScannerModel) response);
                        Constants.SITEID = model.getResult().getSiteId();
                        Constants.SNCODE = model.getResult().getSiteNum();
                        Constants.UNIQUECODE = url;
                        ActivityUtils.startActivity(AddShopActivity.class);
                        finish();
                    }

                    @Override
                    public void fail() {
                        finish();
                    }
                });
                break;
            case TYPE_OPEN_SAN_CAO:
                ApiUtils.opensancao(url, new ApiCallBack() {
                    @Override
                    public void success(Object response) {
                        ToastUtils.showLong((String)response);
                        ActivityUtils.startActivity(AddShopActivity.class);
                        finish();
                    }

                    @Override
                    public void fail() {

                    }
                });
                break;
        }

    }

    @OnClick(R.id.title_back_iv)
    public void onViewClicked() {
        finish();
    }
}
