package com.health2world.aio.printer;

import android.text.TextUtils;
import android.widget.TextView;

import com.health2world.aio.R;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

import static com.health2world.aio.config.AppConfig.*;

/**
 * @author Administrator
 * @date 2019/1/11/0011.
 */

class PrinterAdapter extends BaseQuickAdapter<PrinterDevice, BaseViewHolder> {


    PrinterAdapter(List<PrinterDevice> data) {
        super(R.layout.item_printer, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PrinterDevice item) {

        TextView printerIp = helper.getView(R.id.printer_ip);
//        ImageView printerImg = helper.getView(R.id.printer_img);

        if (item != null)
            switch (item.getPrinterType()) {
                case PRINTER_USB: {
                    //TODO 获取USB打印机设备信息
                    printerIp.setText("USB打印机");
                    break;
                }

                case PRINTER_WIFI: {
                    if (!TextUtils.isEmpty(item.getPrinterIP())) {
                        printerIp.setText(item.getPrinterIP());
                        helper.setImageResource(R.id.printer_img, R.mipmap.content_pic_print);
                    }
                    break;
                }

                default:
                    break;

            }

    }
}
