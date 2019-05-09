package com.health2world.aio.printer;

import java.io.Serializable;

/**
 * 打印机设备
 * @author Runnlin
 * @date 2019/1/11/0011.
 */

public class PrinterDevice implements Serializable {
    //打印机IP
    private String printerIP;
    //打印机类型  "UsbPrinter", "WifiPrinter"
    private String printerType;
    //打印机已连接
    private boolean isConnected;
    //打印机返回信息
    private String message;

    public PrinterDevice(String pPrinterIP, String type, boolean pIsConnected) {
        this.printerIP = pPrinterIP;
        this.printerType = type;
        this.isConnected = pIsConnected;
    }

    public String getPrinterIP() {
        return printerIP;
    }

    public void setPrinterIP(String pPrinterIP) {
        printerIP = pPrinterIP;
    }

    public String getPrinterType() {
        return printerType;
    }

    public void setPrinterType(String pPointerType) {
        printerType = pPointerType;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean pConnected) {
        isConnected = pConnected;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String pMessage) {
        message = pMessage;
    }
}
