package com.dangong.oksan.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dangong.oksan.R;
import com.dangong.oksan.util.DimensUtil;


/**
 * Created by Vinchan on 2018/6/7.
 */
public class CommonDialog extends BaseDialog {


    TextView numOfJcsTv;
    TextView numOfJdsTv;
    TextView numOfJdssTv;
    TextView numOfBqmcdsTv;
    TextView numOfBqmddsTv;
    Button cancleBtn;
    Button confirmBtn;
    TextView titleJcsTv;
    TextView titleJdsTv;
    TextView titleJdssTv;

    private int width;
    private EnumDialogType dialogType;
    private Context context;
    private View customView;
    private Listener listener;
    private boolean isAutoClose = true;
    private TextView tvTipContent;

    /**
     * 构造函数 默认宽度为：屏幕宽度 * 5 / 6
     *
     * @param context    上下文对象
     * @param dialogType 对话框类型 枚举EnumDialogType
     */
    public CommonDialog(Activity context, EnumDialogType dialogType) {
        this(context, dialogType, DimensUtil.getDisplayWidth(context) * 5 / 6);
    }

    /**
     * 构造函数
     *
     * @param context    上下文对象
     * @param dialogType 对话框类型 枚举EnumDialogType
     * @param width      自定义宽度
     */
    public CommonDialog(Activity context, EnumDialogType dialogType, int width) {
        super(context);
        this.context = context;
        this.dialogType = dialogType;
        this.width = width;
        init();
    }


    @Override
    public void init() {
        super.init();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);

    }

    @Override

    public void initView() {
        super.initView();
        customView = LayoutInflater.from(context).inflate(R.layout.dialog_site_report, null);
        getWindow().setContentView(customView);

        numOfJcsTv = customView.findViewById(R.id.num_of_jcs_tv);
        numOfJdsTv = customView.findViewById(R.id.num_of_jds_tv);
        numOfJdssTv = customView.findViewById(R.id.num_of_jdss_tv);
        numOfBqmcdsTv = customView.findViewById(R.id.num_of_bqmcds_tv);
        numOfBqmddsTv = customView.findViewById(R.id.num_of_bqmcds_tv);
        titleJcsTv = customView.findViewById(R.id.title_jcs_tv);
        titleJdsTv = customView.findViewById(R.id.title_jds_tv);
        titleJdssTv = customView.findViewById(R.id.title_jdss_tv);

        cancleBtn = customView.findViewById(R.id.cancle_btn);
        confirmBtn = customView.findViewById(R.id.confirm_btn);
        cancleBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        if (dialogType == EnumDialogType.CUT) {
            titleJcsTv.setText("减长伞");
            titleJdsTv.setText("减短伞");
            titleJdssTv.setText("减登山伞");
        }else {
            titleJcsTv.setText("加长伞");
            titleJdsTv.setText("加短伞");
            titleJdssTv.setText("加登山伞");
        }
    }

    public String getLongNum() {
        return numOfJcsTv.getText().toString();
    }

    public String getShortNum() {
        return numOfJdsTv.getText().toString();
    }

    public String getMountainNum() {
        return numOfJdssTv.getText().toString();
    }

    public void setLongNum(String num) {
        numOfJcsTv.setText(num);
    }

    public void setShortNum(String num) {
        numOfJdsTv.setText(num);
    }

    public void setMountainNum(String num) {
        numOfJdssTv.setText(num);
    }

    public void setnumOfBqmcds(String num) {
        numOfBqmcdsTv.setText(num);
    }

    public void setnumOfBqmdds(String num) {
        numOfBqmddsTv.setText(num);
    }

    /**
     * 设置提示内容
     */

    public void setTipContent(String tipContent) {
        tvTipContent.setText(tipContent);
    }

    /**
     * 获取Diolog布局页面 可根据业务需要修改布局元素属性
     */
    public View getCustomView() {
        return customView;
    }

    /**
     * 点击对话窗操作时是否默认u自动关闭对话框 初始化为默认关闭
     */
    public void isAutoClose(boolean isAutoClose) {
        this.isAutoClose = isAutoClose;
    }

    /**
     * 设置时间监听
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle_btn:
                if (listener != null) {
                    listener.cancle();
                }
                dismiss();
                break;
            case R.id.confirm_btn:
                if (listener != null) {
                    listener.sure();
                }
                dismiss();
                break;
        }
    }


    /**
     * 事件监听
     */
    public interface Listener {
        /**
         * 点击确认监听回掉
         */
        void sure();

        /**
         * 点击取消或提示按钮监听回掉
         */
        void cancle();
    }


    /**
     * 确认操作
     */
    private void sure() {
        if (listener != null) {
            listener.sure();
        }
        if (isAutoClose) {
            dismiss();
        }
    }

    /**
     * 取消操作
     */
    private void cancle() {
        if (listener != null) {
            listener.cancle();
        }
        if (isAutoClose) {
            dismiss();
        }
    }

    /**
     * 商品所在仓库发货类型枚举
     */
    public enum EnumDialogType {
        ADD, CUT
    }
}

