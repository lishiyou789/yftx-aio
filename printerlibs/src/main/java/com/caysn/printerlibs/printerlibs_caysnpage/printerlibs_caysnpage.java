package com.caysn.printerlibs.printerlibs_caysnpage;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.*;

public interface printerlibs_caysnpage extends Library {

    // static interface method need jdk1.8. here we use inner class to avoid this porblem.
    class GetLibraryPath_Helper {
        // can replaced by absolute path
        public static String GetLibraryPath() {
            // force call JNI_OnLoad
//            if (Platform.isAndroid())
                System.loadLibrary("PrinterLibs");
            return "PrinterLibs";
        }
    }
    printerlibs_caysnpage INSTANCE = Native.loadLibrary(GetLibraryPath_Helper.GetLibraryPath(), printerlibs_caysnpage.class);

    int ComDataBits_4 = 4;
    int ComDataBits_5 = 5;
    int ComDataBits_6 = 6;
    int ComDataBits_7 = 7;
    int ComDataBits_8 = 8;

    int ComParity_NoParity = 0;
    int ComParity_OddParity = 1;
    int ComParity_EvenParity = 2;
    int ComParity_MarkParity = 3;
    int ComParity_SpaceParity = 4;

    int ComStopBits_One = 0;
    int ComStopBits_OnePointFive = 1;
    int ComStopBits_Two = 2;

    int ComFlowControl_None = 0;
    int ComFlowControl_XonXoff = 1;
    int ComFlowControl_RtsCts = 2;
    int ComFlowControl_DtrDsr = 3;

    int PL_QUERYBUFFER_EMTPY = 0;
    int PL_QUERYBUFFER_FAILED_PORT_CLOSED = -1;
    int PL_QUERYBUFFER_FAILED_PORT_WRITEFAILED = -2;
    int PL_QUERYBUFFER_FAILED_PORT_READFAILED = -3;
    int PL_QUERYBUFFER_NOT_EMPTY = -4;

    int PL_PRINTRESULT_SUCCESS = 0;
    int PL_PRINTRESULT_PORT_CLOSED = -1;
    int PL_PRINTRESULT_PORT_WRITEFAILED = -2;
    int PL_PRINTRESULT_PORT_READFAILED = -3;
    int PL_PRINTRESULT_PRINTER_OFFLINE = -102;
    int PL_PRINTRESULT_PRINTER_NOPAPER = -103;
    int PL_PRINTRESULT_OTHER_RERROR = -101;

    class PL_PRINTERSTATUS_Helper {
        public static boolean PL_PRINTERSTATUS_QUERYFAILED(long status) { return (status == -1); }

        public static boolean PL_PRINTERSTATUS_DRAWER_OPENED(long status) { return (((status >> 0) & 0x04) == 0x00); }

        public static boolean PL_PRINTERSTATUS_OFFLINE(long status) { return (((status >> 0) & 0x08) == 0x08); }

        public static boolean PL_PRINTERSTATUS_COVERUP(long status) { return (((status >> 8) & 0x04) == 0x04); }

        public static boolean PL_PRINTERSTATUS_FEED_PRESSED(long status) { return (((status >> 8) & 0x08) == 0x08); }

        public static boolean PL_PRINTERSTATUS_NOPAPER(long status) { return (((status >> 8) & 0x20) == 0x20); }

        public static boolean PL_PRINTERSTATUS_ERROR_OCCURED(long status) { return (((status >> 8) & 0x40) == 0x40); }

        public static boolean PL_PRINTERSTATUS_CUTTER_ERROR(long status) { return (((status >> 16) & 0x08) == 0x08); }

        public static boolean PL_PRINTERSTATUS_UNRECOVERABLE_ERROR(long status) { return (((status >> 16) & 0x20) == 0x20); }

        public static boolean PL_PRINTERSTATUS_DEGREE_OR_VOLTAGE_OVERRANGE(long status) { return (((status >> 16) & 0x40) == 0x40); }

        public static boolean PL_PRINTERSTATUS_PAPER_NEAREND(long status) { return (((status >> 24) & 0x0C) == 0x0C); }

        public static boolean PL_PRINTERSTATUS_PAPER_TAKEOUT(long status) { return (((status >> 24) & 0x04) == 0x04); }
    }

    int PrintDensity_Light = 0;
    int PrintDensity_Normal = 1;
    int PrintDensity_Dark = 2;

    int DrawAlignment_Left = -1;
    int DrawAlignment_HCenter = -2;
    int DrawAlignment_Right = -3;
    int DrawAlignment_Top = -1;
    int DrawAlignment_VCenter = -2;
    int DrawAlignment_Bottom = -3;

    int PageModeDrawDirection_LeftToRight = 0;
    int PageModeDrawDirection_BottomToTop = 1;
    int PageModeDrawDirection_RightToLeft = 2;
    int PageModeDrawDirection_TopToBottom = 3;

    int MultiByteModeEncoding_GBK = 0;
    int MultiByteModeEncoding_UTF8 = 1;
    int MultiByteModeEncoding_BIG5 = 3;
    int MultiByteModeEncoding_ShiftJIS = 4;
    int MultiByteModeEncoding_EUCKR = 5;

    int AsciiTextFontType_A = 0;
    int AsciiTextFontType_B = 1;
    int AsciiTextFontType_C = 2;
    int AsciiTextFontType_D = 3;
    int AsciiTextFontType_E = 4;

    int TextUnderline_None = 0;
    int TextUnderline_One = 1;
    int TextUnderline_Two = 2;

    int CharacterSet_USA = 0;
    int CharacterSet_FRANCE = 1;
    int CharacterSet_GERMANY = 2;
    int CharacterSet_UK = 3;
    int CharacterSet_DENMARK_I = 4;
    int CharacterSet_SWEDEN = 5;
    int CharacterSet_ITALY = 6;
    int CharacterSet_SPAIN_I = 7;
    int CharacterSet_JAPAN = 8;
    int CharacterSet_NORWAY = 9;
    int CharacterSet_DENMARK_II = 10;
    int CharacterSet_SPAIN_II = 11;
    int CharacterSet_LATIN = 12;
    int CharacterSet_KOREA = 13;
    int CharacterSet_SLOVENIA = 14;
    int CharacterSet_CHINA = 15;

    int CharacterCodepage_CP437 = 0;
    int CharacterCodepage_KATAKANA = 1;
    int CharacterCodepage_CP850 = 2;
    int CharacterCodepage_CP860 = 3;
    int CharacterCodepage_CP863 = 4;
    int CharacterCodepage_CP865 = 5;
    int CharacterCodepage_WCP1251 = 6;
    int CharacterCodepage_CP866 = 7;
    int CharacterCodepage_MIK = 8;
    int CharacterCodepage_CP755 = 9;
    int CharacterCodepage_IRAN = 10;
    int CharacterCodepage_CP862 = 15;
    int CharacterCodepage_WCP1252 = 16;
    int CharacterCodepage_WCP1253 = 17;
    int CharacterCodepage_CP852 = 18;
    int CharacterCodepage_CP858 = 19;
    int CharacterCodepage_IRAN_II = 20;
    int CharacterCodepage_LATVIAN = 21;
    int CharacterCodepage_CP864 = 22;
    int CharacterCodepage_ISO_8859_1 = 23;
    int CharacterCodepage_CP737 = 24;
    int CharacterCodepage_WCP1257 = 25;
    int CharacterCodepage_THAI = 26;
    int CharacterCodepage_CP720 = 27;
    int CharacterCodepage_CP855 = 28;
    int CharacterCodepage_CP857 = 29;
    int CharacterCodepage_WCP1250 = 30;
    int CharacterCodepage_CP775 = 31;
    int CharacterCodepage_WCP1254 = 32;
    int CharacterCodepage_WCP1255 = 33;
    int CharacterCodepage_WCP1256 = 34;
    int CharacterCodepage_WCP1258 = 35;
    int CharacterCodepage_ISO_8859_2 = 36;
    int CharacterCodepage_ISO_8859_3 = 37;
    int CharacterCodepage_ISO_8859_4 = 38;
    int CharacterCodepage_ISO_8859_5 = 39;
    int CharacterCodepage_ISO_8859_6 = 40;
    int CharacterCodepage_ISO_8859_7 = 41;
    int CharacterCodepage_ISO_8859_8 = 42;
    int CharacterCodepage_ISO_8859_9 = 43;
    int CharacterCodepage_ISO_8859_15 = 44;
    int CharacterCodepage_THAI_2 = 45;
    int CharacterCodepage_CP856 = 46;
    int CharacterCodepage_CP874 = 47;
    int CharacterCodepage_TCVN3 = 48;

    int BarcodeReadableTextFontType_Standard = 0;
    int BarcodeReadableTextFontType_Small = 1;

    int BarcodeReadableTextPosition_None = 0;
    int BarcodeReadableTextPosition_AboveBarcode = 1;
    int BarcodeReadableTextPosition_BelowBarcode = 2;
    int BarcodeReadableTextPosition_AboveAndBelowBarcode = 3;

    int PosBarcodeType_UPCA = 0x41;
    int PosBarcodeType_UPCE = 0x42;
    int PosBarcodeType_EAN13 = 0x43;
    int PosBarcodeType_EAN8 = 0x44;
    int PosBarcodeType_CODE39 = 0x45;
    int PosBarcodeType_ITF = 0x46;
    int PosBarcodeType_CODEBAR = 0x47;
    int PosBarcodeType_CODE93 = 0x48;
    int PosBarcodeType_CODE128 = 0x49;

    int QRCodeECC_L = 1;
    int QRCodeECC_M = 2;
    int QRCodeECC_Q = 3;
    int QRCodeECC_H = 4;

    int ImagePixelsFormat_MONO = 1;
    int ImagePixelsFormat_MONOLSB = 2;
    int ImagePixelsFormat_GRAY8 = 3;
    int ImagePixelsFormat_BYTEORDERED_RGB24 = 4;
    int ImagePixelsFormat_BYTEORDERED_BGR24 = 5;
    int ImagePixelsFormat_BYTEORDERED_ARGB32 = 6;
    int ImagePixelsFormat_BYTEORDERED_RGBA32 = 7;
    int ImagePixelsFormat_BYTEORDERED_ABGR32 = 8;
    int ImagePixelsFormat_BYTEORDERED_BGRA32 = 9;

    int ImageBinarizationMethod_Dithering = 0;
    int ImageBinarizationMethod_Thresholding = 1;

    String CaysnPage_LibraryVersion();

    int CaysnPage_EnumComA(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    //public int CaysnPage_EnumComW(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    class CaysnPage_EnumCom_Helper {
        public static String[] CaysnPage_EnumComA() {
            IntByReference pcbNeeded = new IntByReference();
            INSTANCE.CaysnPage_EnumComA(null, 0, pcbNeeded);
            if (pcbNeeded.getValue() > 0) {
                byte[] pBuf = new byte[pcbNeeded.getValue()];
                if (pBuf != null) {
                    INSTANCE.CaysnPage_EnumComA(pBuf, pBuf.length, null);
                    String s = new String(pBuf);
                    String[] ss = s.split("\0");
                    return ss;
                }
            }
            return null;
        }
    }

    int CaysnPage_EnumUsbVidPidA(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    //public int CaysnPage_EnumUsbVidPidW(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    class CaysnPage_EnumUsbVidPid_Helper {
        public static String[] CaysnPage_EnumUsbVidPidA() {
            IntByReference pcbNeeded = new IntByReference();
            INSTANCE.CaysnPage_EnumUsbVidPidA(null, 0, pcbNeeded);
            if (pcbNeeded.getValue() > 0) {
                byte[] pBuf = new byte[pcbNeeded.getValue()];
                if (pBuf != null) {
                    INSTANCE.CaysnPage_EnumUsbVidPidA(pBuf, pBuf.length, null);
                    String s = new String(pBuf);
                    String[] ss = s.split("\0");
                    return ss;
                }
            }
            return null;
        }
    }

    interface on_netprinter_discovered_a_callback extends Callback {
        void on_netprinter_discovered_a(String local_ip, String discovered_mac, String discovered_ip, String discovered_name, Pointer private_data);
    }
    void CaysnPage_EnumNetPrinterA(int timeout, IntByReference cancel, on_netprinter_discovered_a_callback callback, Pointer data);

    interface on_btdevice_discovered_a_callback extends Callback {
        void on_btdevice_discovered_a(String device_name, String device_address, Pointer private_data);
    }
    void CaysnPage_EnumBtDeviceA(int timeout, IntByReference cancel, on_btdevice_discovered_a_callback callback, Pointer data);

    void CaysnPage_EnumBleDeviceA(int timeout, IntByReference cancel, on_btdevice_discovered_a_callback callback, Pointer data);

    Pointer CaysnPage_OpenComA(String name, int baudrate, int databits, int parity, int stopbits, int flowcontrol);
    Pointer CaysnPage_OpenComW(WString name, int baudrate, int databits, int parity, int stopbits, int flowcontrol);

    Pointer CaysnPage_OpenTcpA(String ip, short port, int timeout);
    Pointer CaysnPage_OpenTcpW(WString ip, short port, int timeout);

    Pointer CaysnPage_OpenTcpBindInterfaceA(String ip, short port, String bind_local_addr, int timeout);
    Pointer CaysnPage_OpenTcpBindInterfaceW(WString ip, short port, WString bind_local_addr, int timeout);

    Pointer CaysnPage_OpenUsbVidPid(short vid, short pid);
    Pointer CaysnPage_OpenUsbVidPidStringA(String name);
    Pointer CaysnPage_OpenUsbVidPidStringW(WString name);

    Pointer CaysnPage_OpenBT2ByConnectA(String address);
    Pointer CaysnPage_OpenBT2ByConnectW(WString address);

    Pointer CaysnPage_OpenBT2ByListenA(int timeout, byte[] address);

    Pointer CaysnPage_OpenBT4ByConnectA(String address);
    Pointer CaysnPage_OpenBT4ByConnectW(WString address);

    Pointer CaysnPage_OpenFileNewA(String name);
    Pointer CaysnPage_OpenFileNewW(WString name);

    Pointer CaysnPage_OpenFileAppendA(String name);
    Pointer CaysnPage_OpenFileAppendW(WString name);

    Pointer CaysnPage_OpenMemory(int nMemorySpaceSize);

    Pointer CaysnPage_MemoryData(Pointer handle);
    class CaysnPage_MemoryData_Helper {
        public static byte[] CaysnPage_MemoryByteArray(Pointer handle) {
            Pointer pdata = INSTANCE.CaysnPage_MemoryData(handle);
            int data_size = INSTANCE.CaysnPage_MemoryDataLength(handle);
            if (pdata != Pointer.NULL) {
                byte[] buffer = pdata.getByteArray(0, data_size);
                return buffer;
            }
            return null;
        }
    }

    int CaysnPage_MemoryDataLength(Pointer handle);

    void CaysnPage_ClearMemoryData(Pointer handle);

    interface on_bytes_writed_callback extends Callback {
        // callback not support byte[]
        void on_bytes_writed(Pointer buffer, int count, Pointer private_data);
    }
    int CaysnPage_SetWritedEvent(Pointer handle, on_bytes_writed_callback callback, Pointer private_data);

    interface on_bytes_readed_callback extends Callback {
        // callback not support byte[]
        void on_bytes_readed(Pointer buffer, int count, Pointer private_data);
    }
    int CaysnPage_SetReadedEvent(Pointer handle, on_bytes_readed_callback callback, Pointer private_data);

    interface on_port_closed_callback extends Callback {
        void on_port_closed(Pointer private_data);
    }
    int CaysnPage_SetClosedEvent(Pointer handle, on_port_closed_callback callback, Pointer private_data);

    int CaysnPage_Write(Pointer handle, byte[] buffer, int count, int timeout);

    int CaysnPage_Read(Pointer handle, byte[] buffer, int count, int timeout);

    int CaysnPage_ReadUntilByte(Pointer handle, byte[] buffer, int count, int timeout, byte breakByte);

    void CaysnPage_SkipAvailable(Pointer handle);

    void CaysnPage_FlushBuffer(Pointer handle);

    void CaysnPage_Close(Pointer handle);

    int CaysnPage_QueryPrinterBufferEmpty(Pointer handle, int timeout);

    int CaysnPage_QueryPrinterStatus(Pointer handle, int timeout);

    int CaysnPage_QueryPrintResult(Pointer handle, int timeout);

    int CaysnPage_KickOutDrawer(Pointer handle, int nDrawerIndex, int nHighLevelTime, int nLowLevelTime);

    int CaysnPage_Beep(Pointer handle, int nBeepCount, int nBeepMs);

    int CaysnPage_FeedAndHalfCutPaper(Pointer handle);

    int CaysnPage_FullCutPaper(Pointer handle);

    int CaysnPage_HalfCutPaper(Pointer handle);

    int CaysnPage_ResetPrinter(Pointer handle);

    int CaysnPage_SetPrinter(Pointer handle, int setType, byte[] buffer, int count);

    int CaysnPage_SetPrintSpeed(Pointer handle, int nSpeed);

    int CaysnPage_SetPrintDensity(Pointer handle, int nDensity);

    int CaysnPage_SetPrintHeatPara(Pointer handle, int nMaxHeatDots, int nHeatOnTime, int nHeatOffTime);

    int CaysnPage_PrintSelfTestPage(Pointer handle);

    int CaysnPage_SetMovementUnit(Pointer handle, int nHorizontalMovementUnit, int nVerticalMovementUnit);

    int CaysnPage_SelectPageMode(Pointer handle);

    int CaysnPage_SelectPageModeEx(Pointer handle, int nHorizontalMovementUnit, int nVerticalMovementUnit, int x, int y, int width, int height);

    int CaysnPage_ExitPageMode(Pointer handle);

    int CaysnPage_PrintPage(Pointer handle);

    int CaysnPage_ClearPage(Pointer handle);

    int CaysnPage_SetPageArea(Pointer handle, int x, int y, int width, int height);

    int CaysnPage_SetPageModeDrawDirection(Pointer handle, int nDirection);

    int CaysnPage_SetHorizontalAbsolutePrintPosition(Pointer handle, int nPosition);

    int CaysnPage_SetHorizontalRelativePrintPosition(Pointer handle, int nPosition);

    int CaysnPage_SetVerticalAbsolutePrintPosition(Pointer handle, int nPosition);

    int CaysnPage_SetVerticalRelativePrintPosition(Pointer handle, int nPosition);

    int CaysnPage_FeedLine(Pointer handle, int numLines);

    int CaysnPage_FeedDot(Pointer handle, int numDots);

    int CaysnPage_DrawTextA(Pointer handle, String str);

    int CaysnPage_DrawTextInUTF8W(Pointer handle, WString str);

    int CaysnPage_DrawTextInGBKW(Pointer handle, WString str);

    int CaysnPage_DrawTextInBIG5W(Pointer handle, WString str);

    int CaysnPage_DrawTextInShiftJISW(Pointer handle, WString str);

    int CaysnPage_DrawTextInEUCKRW(Pointer handle, WString str);

    int CaysnPage_SetTextScale(Pointer handle, int nWidthScale, int nHeightScale);

    int CaysnPage_SetAsciiTextFontType(Pointer handle, int nFontType);

    int CaysnPage_SetTextBold(Pointer handle, int nBold);

    int CaysnPage_SetTextUnderline(Pointer handle, int nUnderline);

    int CaysnPage_SetTextWhiteOnBlack(Pointer handle, int nWhiteOnBlack);

    int CaysnPage_SetTextLineHeight(Pointer handle, int nLineHeight);

    int CaysnPage_SetAsciiTextCharRightSpacing(Pointer handle, int nSpacing);

    int CaysnPage_SetKanjiTextCharSpacing(Pointer handle, int nLeftSpacing, int nRightSpacing);

    int CaysnPage_SetSingleByteMode(Pointer handle);

    int CaysnPage_SetCharacterSet(Pointer handle, int nCharacterSet);

    int CaysnPage_SetCharacterCodepage(Pointer handle, int nCharacterCodepage);

    int CaysnPage_SetMultiByteMode(Pointer handle);

    int CaysnPage_SetMultiByteEncoding(Pointer handle, int nEncoding);

    int CaysnPage_SetUserCharacterPatternFromFileA(Pointer handle, byte ch, String pszFile);
    int CaysnPage_SetUserCharacterPatternFromFileW(Pointer handle, byte ch, WString pszFile);

    int CaysnPage_SetUserCharacterPatternFromData(Pointer handle, byte ch, byte[] data, int data_size);
    class CaysnPage_SetUserCharacterPattern_Helper {
        public static int CaysnPage_SetUserCharacterPatternFromBitmap(Pointer handle, byte ch, Bitmap bitmap) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPage_SetUserCharacterPatternFromData(handle, ch, data, data.length);
            }
            return result;
        }
    }

    int CaysnPage_SetUserCharacterPatternFromPixels(Pointer handle, byte ch, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format);

    int CaysnPage_ClearUserCharacterPattern(Pointer handle, byte ch);

    int CaysnPage_SetUserCharacterEnable(Pointer handle, int enable);

    int CaysnPage_SetUserKanjiPatternFromFileA(Pointer handle, byte c1, byte c2, String pszFile);
    int CaysnPage_SetUserKanjiPatternFromFileW(Pointer handle, byte c1, byte c2, WString pszFile);

    int CaysnPage_SetUserKanjiPatternFromData(Pointer handle, byte c1, byte c2, byte[] data, int data_size);
    class CaysnPage_SetUserKanjiPattern_Helper {
        public static int CaysnPage_SetUserKanjiPatternFromBitmap(Pointer handle, byte c1, byte c2, Bitmap bitmap) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPage_SetUserKanjiPatternFromData(handle, c1, c2, data, data.length);
            }
            return result;
        }
    }

    int CaysnPage_SetUserKanjiPatternFromPixels(Pointer handle, byte c1, byte c2, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format);

    int CaysnPage_DrawBarcodeA(Pointer handle, int nBarcodeType, String str);
    int CaysnPage_DrawBarcodeW(Pointer handle, int nBarcodeType, WString str);

    int CaysnPage_SetBarcodeUnitWidth(Pointer handle, int nBarcodeUnitWidth);

    int CaysnPage_SetBarcodeHeight(Pointer handle, int nBarcodeHeight);

    int CaysnPage_SetBarcodeReadableTextFontType(Pointer handle, int nFontType);

    int CaysnPage_SetBarcodeReadableTextPosition(Pointer handle, int nTextPosition);

    int CaysnPage_DrawQRCodeA(Pointer handle, int nVersion, int nECCLevel, String str);
    int CaysnPage_DrawQRCodeW(Pointer handle, int nVersion, int nECCLevel, WString str);

    int CaysnPage_DrawQRCodeUseEpsonCmdA(Pointer handle, int nQRCodeUnitWidth, int nECCLevel, String str);
    int CaysnPage_DrawQRCodeUseEpsonCmdW(Pointer handle, int nQRCodeUnitWidth, int nECCLevel, WString str);

    int CaysnPage_DrawPDF417BarcodeUseEpsonCmdA(Pointer handle, int columnCount, int rowCount, int unitWidth, int rowHeight, int nECCLevel, int dataProcessingMode, String str);
    int CaysnPage_DrawPDF417BarcodeUseEpsonCmdW(Pointer handle, int columnCount, int rowCount, int unitWidth, int rowHeight, int nECCLevel, int dataProcessingMode, WString str);

    int CaysnPage_GetImageSizeFromFileA(String pszFile, IntByReference depth, IntByReference width, IntByReference height);
    int CaysnPage_GetImageSizeFromFileW(WString pszFile, IntByReference depth, IntByReference width, IntByReference height);

    int CaysnPage_GetImageSizeFromData(byte[] data, int data_size, IntByReference depth, IntByReference width, IntByReference height);

    int CaysnPage_DrawEpsonTM88IVImageUseGS8CmdFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int CaysnPage_DrawEpsonTM88IVImageUseGS8CmdFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int CaysnPage_DrawEpsonTM88IVImageUseGS8CmdFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class CaysnPage_DrawEpsonTM88IVImageUseGS8Cmd_Helper {
        public static int CaysnPage_DrawEpsonTM88IVImageUseGS8CmdFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPage_DrawEpsonTM88IVImageUseGS8CmdFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int CaysnPage_DrawEpsonTM88IVImageUseGS8CmdFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int CaysnPage_EnableBlackMarkMode(Pointer handle);

    int CaysnPage_DisableBlackMarkMode(Pointer handle);

    int CaysnPage_SetBlackMarkMaxFindLength(Pointer handle, int maxFindLength);

    int CaysnPage_FindBlackMark(Pointer handle);

    int CaysnPage_SetBlackMarkPaperPrintPosition(Pointer handle, int position);

    int CaysnPage_SetBlackMarkPaperCutPosition(Pointer handle, int position);

    int CaysnPage_FullCutBlackMarkPaper(Pointer handle);

    int CaysnPage_HalfCutBlackMarkPaper(Pointer handle);

    int CaysnPage_DrawRect(Pointer handle, int x, int y, int width, int height, int color);

    int CaysnPage_DrawBox(Pointer handle, int x, int y, int width, int height, int borderwidth, int bordercolor);

    int CaysnPage_DrawTextSpecifyPositionA(Pointer handle, int x, int y, String str);

    int CaysnPage_DrawTextSpecifyPositionInUTF8W(Pointer handle, int x, int y, WString str);

    int CaysnPage_DrawTextSpecifyPositionInGBKW(Pointer handle, int x, int y, WString str);

    int CaysnPage_DrawTextSpecifyPositionInBIG5W(Pointer handle, int x, int y, WString str);

    int CaysnPage_DrawTextSpecifyPositionInShiftJISW(Pointer handle, int x, int y, WString str);

    int CaysnPage_DrawTextSpecifyPositionInEUCKRW(Pointer handle, int x, int y, WString str);

    int CaysnPage_DrawBarcodeSpecifyPositionA(Pointer handle, int x, int y, int nBarcodeType, String str);
    int CaysnPage_DrawBarcodeSpecifyPositionW(Pointer handle, int x, int y, int nBarcodeType, WString str);

    int CaysnPage_DrawQRCodeSpecifyPositionA(Pointer handle, int x, int y, int nVersion, int nECCLevel, String str);
    int CaysnPage_DrawQRCodeSpecifyPositionW(Pointer handle, int x, int y, int nVersion, int nECCLevel, WString str);

    int CaysnPage_DrawQRCodeUseEpsonCmdSpecifyPositionA(Pointer handle, int x, int y, int nQRCodeUnitWidth, int nECCLevel, String str);
    int CaysnPage_DrawQRCodeUseEpsonCmdSpecifyPositionW(Pointer handle, int x, int y, int nQRCodeUnitWidth, int nECCLevel, WString str);

    int CaysnPage_DrawImageSpecifyPositionFromFileA(Pointer handle, int x, int y, int dstw, int dsth, String pszFile, int binaryzation_method);
    int CaysnPage_DrawImageSpecifyPositionFromFileW(Pointer handle, int x, int y, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int CaysnPage_DrawImageSpecifyPositionFromData(Pointer handle, int x, int y, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class CaysnPage_DrawImageSpecifyPosition_Helper {
        public static int CaysnPage_DrawImageSpecifyPositionFromBitmap(Pointer handle, int x, int y, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPage_DrawImageSpecifyPositionFromData(handle, x, y, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int CaysnPage_DrawImageSpecifyPositionFromPixels(Pointer handle, int x, int y, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

}

