package com.caysn.printerlibs.printerlibs_basic;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.*;

public interface printerlibs_basic extends Library {

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
    printerlibs_basic INSTANCE = Native.loadLibrary(GetLibraryPath_Helper.GetLibraryPath(), printerlibs_basic.class);

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

    int PL_CHECKRESULT_SUCCESS_ISKNOWNPRINTER = 0;
    int PL_CHECKRESULT_FAILED_PORT_CLOSED = -1;
    int PL_CHECKRESULT_FAILED_PORT_WRITEFAILED = -2;
    int PL_CHECKRESULT_FAILED_PORT_READFAILED = -3;
    int PL_CHECKRESULT_FAILED_PRINTER_NORESPONSE = -4;
    int PL_CHECKRESULT_SUCCESS_UNKNOWNPRINTER = -104;
    int PL_CHECKRESULT_FAILED_OTHER_RERROR = -101;

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

    int PosAlignment_Left = 0;
    int PosAlignment_HCenter = 1;
    int PosAlignment_Right = 2;

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

    int ImageCompressionMethod_None = 0;
    int ImageCompressionMethod_Level1 = 1;
    int ImageCompressionMethod_Level2 = 2;

    int LabelRotation_0 = 0;
    int LabelRotation_90 = 1;
    int LabelRotation_180 = 2;
    int LabelRotation_270 = 3;

    int PL_LABEL_TEXT_STYLE_BOLD = (1<<0);
    int PL_LABEL_TEXT_STYLE_UNDERLINE = (1<<1);
    int PL_LABEL_TEXT_STYLE_HIGHLIGHT = (1<<2);
    int PL_LABEL_TEXT_STYLE_STRIKETHROUGH = (1<<3);
    int PL_LABEL_TEXT_STYLE_ROTATION_0 = (0<<4);
    int PL_LABEL_TEXT_STYLE_ROTATION_90 = (1<<4);
    int PL_LABEL_TEXT_STYLE_ROTATION_180 = (2<<4);
    int PL_LABEL_TEXT_STYLE_ROTATION_270 = (3<<4);
    class PL_LABEL_TEXT_STYLE_Helper {
        public static int PL_LABEL_TEXT_STYLE_WIDTH_ENLARGEMENT(int n) { return ((n)<<8); }

        public static int PL_LABEL_TEXT_STYLE_HEIGHT_ENLARGEMENT(int n) { return ((n)<<12); }
    }

    int LabelBarcodeType_UPCA = 0;
    int LabelBarcodeType_UPCE = 1;
    int LabelBarcodeType_EAN13 = 2;
    int LabelBarcodeType_EAN8 = 3;
    int LabelBarcodeType_CODE39 = 4;
    int LabelBarcodeType_ITF = 5;
    int LabelBarcodeType_CODEBAR = 6;
    int LabelBarcodeType_CODE93 = 7;
    int LabelBarcodeType_CODE128 = 8;
    int LabelBarcodeType_CODE11 = 9;
    int LabelBarcodeType_MSI = 10;
    int LabelBarcodeType_128M = 11;
    int LabelBarcodeType_EAN128 = 12;
    int LabelBarcodeType_25C = 13;
    int LabelBarcodeType_39C = 14;
    int LabelBarcodeType_39 = 15;
    int LabelBarcodeType_EAN13PLUS2 = 16;
    int LabelBarcodeType_EAN13PLUS5 = 17;
    int LabelBarcodeType_EAN8PLUS2 = 18;
    int LabelBarcodeType_EAN8PLUS5 = 19;
    int LabelBarcodeType_POST = 20;
    int LabelBarcodeType_UPCAPLUS2 = 21;
    int LabelBarcodeType_UPCAPLUS5 = 22;
    int LabelBarcodeType_UPCEPLUS2 = 23;
    int LabelBarcodeType_UPCEPLUS5 = 24;
    int LabelBarcodeType_CPOST = 25;
    int LabelBarcodeType_MSIC = 26;
    int LabelBarcodeType_PLESSEY = 27;
    int LabelBarcodeType_ITF14 = 28;
    int LabelBarcodeType_EAN14 = 29;

    int LabelColor_White = 0;
    int LabelColor_Black = 1;

    String Pos_LibraryVersion();

    int Pos_EnumComA(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    //public int Pos_EnumComW(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    class Pos_EnumCom_Helper {
        public static String[] Pos_EnumComA() {
            IntByReference pcbNeeded = new IntByReference();
            INSTANCE.Pos_EnumComA(null, 0, pcbNeeded);
            if (pcbNeeded.getValue() > 0) {
                byte[] pBuf = new byte[pcbNeeded.getValue()];
                if (pBuf != null) {
                    INSTANCE.Pos_EnumComA(pBuf, pBuf.length, null);
                    String s = new String(pBuf);
                    String[] ss = s.split("\0");
                    return ss;
                }
            }
            return null;
        }
    }

    int Pos_EnumUsbVidPidA(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    //public int Pos_EnumUsbVidPidW(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    class Pos_EnumUsbVidPid_Helper {
        public static String[] Pos_EnumUsbVidPidA() {
            IntByReference pcbNeeded = new IntByReference();
            INSTANCE.Pos_EnumUsbVidPidA(null, 0, pcbNeeded);
            if (pcbNeeded.getValue() > 0) {
                byte[] pBuf = new byte[pcbNeeded.getValue()];
                if (pBuf != null) {
                    INSTANCE.Pos_EnumUsbVidPidA(pBuf, pBuf.length, null);
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
    void Pos_EnumNetPrinterA(int timeout, IntByReference cancel, on_netprinter_discovered_a_callback callback, Pointer data);

    interface on_btdevice_discovered_a_callback extends Callback {
        void on_btdevice_discovered_a(String device_name, String device_address, Pointer private_data);
    }
    void Pos_EnumBtDeviceA(int timeout, IntByReference cancel, on_btdevice_discovered_a_callback callback, Pointer data);

    void Pos_EnumBleDeviceA(int timeout, IntByReference cancel, on_btdevice_discovered_a_callback callback, Pointer data);

    Pointer Pos_OpenComA(String name, int baudrate, int databits, int parity, int stopbits, int flowcontrol);
    Pointer Pos_OpenComW(WString name, int baudrate, int databits, int parity, int stopbits, int flowcontrol);

    Pointer Pos_OpenTcpA(String ip, short port, int timeout);
    Pointer Pos_OpenTcpW(WString ip, short port, int timeout);

    Pointer Pos_OpenTcpBindInterfaceA(String ip, short port, String bind_local_addr, int timeout);
    Pointer Pos_OpenTcpBindInterfaceW(WString ip, short port, WString bind_local_addr, int timeout);

    Pointer Pos_OpenUsbVidPid(short vid, short pid);
    Pointer Pos_OpenUsbVidPidStringA(String name);
    Pointer Pos_OpenUsbVidPidStringW(WString name);

    Pointer Pos_OpenBT2ByConnectA(String address);
    Pointer Pos_OpenBT2ByConnectW(WString address);

    Pointer Pos_OpenBT2ByListenA(int timeout, byte[] address);

    Pointer Pos_OpenBT4ByConnectA(String address);
    Pointer Pos_OpenBT4ByConnectW(WString address);

    Pointer Pos_OpenFileNewA(String name);
    Pointer Pos_OpenFileNewW(WString name);

    Pointer Pos_OpenFileAppendA(String name);
    Pointer Pos_OpenFileAppendW(WString name);

    Pointer Pos_OpenMemory(int nMemorySpaceSize);

    Pointer Pos_MemoryData(Pointer handle);
    class Pos_MemoryData_Helper {
        public static byte[] Pos_MemoryByteArray(Pointer handle) {
            Pointer pdata = INSTANCE.Pos_MemoryData(handle);
            int data_size = INSTANCE.Pos_MemoryDataLength(handle);
            if (pdata != Pointer.NULL) {
                byte[] buffer = pdata.getByteArray(0, data_size);
                return buffer;
            }
            return null;
        }
    }

    int Pos_MemoryDataLength(Pointer handle);

    void Pos_ClearMemoryData(Pointer handle);

    interface on_bytes_writed_callback extends Callback {
        // callback not support byte[]
        void on_bytes_writed(Pointer buffer, int count, Pointer private_data);
    }
    int Pos_SetWritedEvent(Pointer handle, on_bytes_writed_callback callback, Pointer private_data);

    interface on_bytes_readed_callback extends Callback {
        // callback not support byte[]
        void on_bytes_readed(Pointer buffer, int count, Pointer private_data);
    }
    int Pos_SetReadedEvent(Pointer handle, on_bytes_readed_callback callback, Pointer private_data);

    interface on_port_closed_callback extends Callback {
        void on_port_closed(Pointer private_data);
    }
    int Pos_SetClosedEvent(Pointer handle, on_port_closed_callback callback, Pointer private_data);

    int Pos_Write(Pointer handle, byte[] buffer, int count, int timeout);

    int Pos_Read(Pointer handle, byte[] buffer, int count, int timeout);

    int Pos_ReadUntilByte(Pointer handle, byte[] buffer, int count, int timeout, byte breakByte);

    void Pos_SkipAvailable(Pointer handle);

    void Pos_FlushBuffer(Pointer handle);

    void Pos_Close(Pointer handle);

    int Pos_CheckPrinter(Pointer handle, int timeout);

    int Pos_QueryPrinterBufferEmpty(Pointer handle, int timeout);

    int Pos_QueryPrinterStatus(Pointer handle, int timeout);

    int Pos_QueryPrintResult(Pointer handle, int timeout);

    int Pos_KickOutDrawer(Pointer handle, int nDrawerIndex, int nHighLevelTime, int nLowLevelTime);

    int Pos_Beep(Pointer handle, int nBeepCount, int nBeepMs);

    int Pos_FeedAndHalfCutPaper(Pointer handle);

    int Pos_FullCutPaper(Pointer handle);

    int Pos_HalfCutPaper(Pointer handle);

    int Pos_ResetPrinter(Pointer handle);

    int Pos_SetPrinter(Pointer handle, int setType, byte[] buffer, int count);

    int Pos_SetPrintSpeed(Pointer handle, int nSpeed);

    int Pos_SetPrintDensity(Pointer handle, int nDensity);

    int Pos_SetPrintHeatPara(Pointer handle, int nMaxHeatDots, int nHeatOnTime, int nHeatOffTime);

    int Pos_PrintSelfTestPage(Pointer handle);

    int Pos_SetMovementUnit(Pointer handle, int nHorizontalMovementUnit, int nVerticalMovementUnit);

    int Pos_SetPrintAreaLeftMargin(Pointer handle, int nLeftMargin);

    int Pos_SetPrintAreaWidth(Pointer handle, int nWidth);

    int Pos_SelectPageMode(Pointer handle);

    int Pos_SelectPageModeEx(Pointer handle, int nHorizontalMovementUnit, int nVerticalMovementUnit, int x, int y, int width, int height);

    int Pos_ExitPageMode(Pointer handle);

    int Pos_PrintPage(Pointer handle);

    int Pos_ClearPage(Pointer handle);

    int Pos_SetPageArea(Pointer handle, int x, int y, int width, int height);

    int Pos_SetPageModeDrawDirection(Pointer handle, int nDirection);

    int Pos_SetHorizontalAbsolutePrintPosition(Pointer handle, int nPosition);

    int Pos_SetHorizontalRelativePrintPosition(Pointer handle, int nPosition);

    int Pos_SetVerticalAbsolutePrintPosition(Pointer handle, int nPosition);

    int Pos_SetVerticalRelativePrintPosition(Pointer handle, int nPosition);

    int Pos_SetAlignment(Pointer handle, int nAlignment);

    int Pos_FeedLine(Pointer handle, int numLines);

    int Pos_FeedDot(Pointer handle, int numDots);

    int Pos_PrintTextA(Pointer handle, String str);

    int Pos_PrintTextInUTF8W(Pointer handle, WString str);

    int Pos_PrintTextInGBKW(Pointer handle, WString str);

    int Pos_PrintTextInBIG5W(Pointer handle, WString str);

    int Pos_PrintTextInShiftJISW(Pointer handle, WString str);

    int Pos_PrintTextInEUCKRW(Pointer handle, WString str);

    int Pos_SetTextScale(Pointer handle, int nWidthScale, int nHeightScale);

    int Pos_SetAsciiTextFontType(Pointer handle, int nFontType);

    int Pos_SetTextBold(Pointer handle, int nBold);

    int Pos_SetTextUnderline(Pointer handle, int nUnderline);

    int Pos_SetTextUpsideDown(Pointer handle, int nUpsideDown);

    int Pos_SetTextWhiteOnBlack(Pointer handle, int nWhiteOnBlack);

    int Pos_SetTextRotate(Pointer handle, int nRotate);

    int Pos_SetTextLineHeight(Pointer handle, int nLineHeight);

    int Pos_SetAsciiTextCharRightSpacing(Pointer handle, int nSpacing);

    int Pos_SetKanjiTextCharSpacing(Pointer handle, int nLeftSpacing, int nRightSpacing);

    int Pos_SetSingleByteMode(Pointer handle);

    int Pos_SetCharacterSet(Pointer handle, int nCharacterSet);

    int Pos_SetCharacterCodepage(Pointer handle, int nCharacterCodepage);

    int Pos_SetMultiByteMode(Pointer handle);

    int Pos_SetMultiByteEncoding(Pointer handle, int nEncoding);

    int Pos_SetUserCharacterPatternFromFileA(Pointer handle, byte ch, String pszFile);
    int Pos_SetUserCharacterPatternFromFileW(Pointer handle, byte ch, WString pszFile);

    int Pos_SetUserCharacterPatternFromData(Pointer handle, byte ch, byte[] data, int data_size);
    class Pos_SetUserCharacterPattern_Helper {
        public static int Pos_SetUserCharacterPatternFromBitmap(Pointer handle, byte ch, Bitmap bitmap) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_SetUserCharacterPatternFromData(handle, ch, data, data.length);
            }
            return result;
        }
    }

    int Pos_SetUserCharacterPatternFromPixels(Pointer handle, byte ch, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format);

    int Pos_ClearUserCharacterPattern(Pointer handle, byte ch);

    int Pos_SetUserCharacterEnable(Pointer handle, int enable);

    int Pos_SetUserKanjiPatternFromFileA(Pointer handle, byte c1, byte c2, String pszFile);
    int Pos_SetUserKanjiPatternFromFileW(Pointer handle, byte c1, byte c2, WString pszFile);

    int Pos_SetUserKanjiPatternFromData(Pointer handle, byte c1, byte c2, byte[] data, int data_size);
    class Pos_SetUserKanjiPattern_Helper {
        public static int Pos_SetUserKanjiPatternFromBitmap(Pointer handle, byte c1, byte c2, Bitmap bitmap) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_SetUserKanjiPatternFromData(handle, c1, c2, data, data.length);
            }
            return result;
        }
    }

    int Pos_SetUserKanjiPatternFromPixels(Pointer handle, byte c1, byte c2, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format);

    int Pos_PrintBarcodeA(Pointer handle, int nBarcodeType, String str);
    int Pos_PrintBarcodeW(Pointer handle, int nBarcodeType, WString str);

    int Pos_SetBarcodeUnitWidth(Pointer handle, int nBarcodeUnitWidth);

    int Pos_SetBarcodeHeight(Pointer handle, int nBarcodeHeight);

    int Pos_SetBarcodeReadableTextFontType(Pointer handle, int nFontType);

    int Pos_SetBarcodeReadableTextPosition(Pointer handle, int nTextPosition);

    int Pos_PrintQRCodeA(Pointer handle, int nVersion, int nECCLevel, String str);
    int Pos_PrintQRCodeW(Pointer handle, int nVersion, int nECCLevel, WString str);

    int Pos_PrintQRCodeUseEpsonCmdA(Pointer handle, int nQRCodeUnitWidth, int nECCLevel, String str);
    int Pos_PrintQRCodeUseEpsonCmdW(Pointer handle, int nQRCodeUnitWidth, int nECCLevel, WString str);

    int Pos_PrintDoubleQRCodeA(Pointer handle, int nQRCodeUnitWidth, int nQR1Position, int nQR1Version, int nQR1ECCLevel, String strQR1, int nQR2Position, int nQR2Version, int nQR2ECCLevel, String strQR2);
    int Pos_PrintDoubleQRCodeW(Pointer handle, int nQRCodeUnitWidth, int nQR1Position, int nQR1Version, int nQR1ECCLevel, WString strQR1, int nQR2Position, int nQR2Version, int nQR2ECCLevel, WString strQR2);

    int Pos_PrintPDF417BarcodeUseEpsonCmdA(Pointer handle, int columnCount, int rowCount, int unitWidth, int rowHeight, int nECCLevel, int dataProcessingMode, String str);
    int Pos_PrintPDF417BarcodeUseEpsonCmdW(Pointer handle, int columnCount, int rowCount, int unitWidth, int rowHeight, int nECCLevel, int dataProcessingMode, WString str);

    int Pos_GetImageSizeFromFileA(String pszFile, IntByReference depth, IntByReference width, IntByReference height);
    int Pos_GetImageSizeFromFileW(WString pszFile, IntByReference depth, IntByReference width, IntByReference height);

    int Pos_GetImageSizeFromData(byte[] data, int data_size, IntByReference depth, IntByReference width, IntByReference height);

    int Pos_PrintRasterImageFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Pos_PrintRasterImageFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Pos_PrintRasterImageFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Pos_PrintRasterImage_Helper {
        public static int Pos_PrintRasterImageFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_PrintRasterImageFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Pos_PrintRasterImageFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int Pos_PrintRasterImageWithCompressFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method, int compress_method);
    int Pos_PrintRasterImageWithCompressFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method, int compress_method);

    int Pos_PrintRasterImageWithCompressFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method, int compress_method);
    class Pos_PrintRasterImageWithCompress_Helper {
        public static int Pos_PrintRasterImageWithCompressFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method, int compress_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_PrintRasterImageWithCompressFromData(handle, dstw, dsth, data, data.length, binaryzation_method, compress_method);
            }
            return result;
        }
    }

    int Pos_PrintRasterImageWithCompressFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method, int compress_method);

    int Pos_FillDotImageToCurrentLineFromFileA(Pointer handle, String pszFile, int binaryzation_method);
    int Pos_FillDotImageToCurrentLineFromFileW(Pointer handle, WString pszFile, int binaryzation_method);

    int Pos_FillDotImageToCurrentLineFromData(Pointer handle, byte[] data, int data_size, int binaryzation_method);
    class Pos_FillDotImageToCurrentLine_Helper {
        public static int Pos_FillDotImageToCurrentLineFromBitmap(Pointer handle, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_FillDotImageToCurrentLineFromData(handle, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Pos_FillDotImageToCurrentLineFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int Pos_PrintDotImageSpecifyHorizontalPositionFromFileA(Pointer handle, int nPosition, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Pos_PrintDotImageSpecifyHorizontalPositionFromFileW(Pointer handle, int nPosition, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Pos_PrintDotImageSpecifyHorizontalPositionFromData(Pointer handle, int nPosition, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Pos_PrintDotImageSpecifyHorizontalPosition_Helper {
        public static int Pos_PrintDotImageSpecifyHorizontalPositionFromBitmap(Pointer handle, int nPosition, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_PrintDotImageSpecifyHorizontalPositionFromData(handle, nPosition, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Pos_PrintDotImageSpecifyHorizontalPositionFromPixels(Pointer handle, int nPosition, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int Pos_SetNVImageFromFilesA(Pointer handle, int count, String[] pszFiles);
    int Pos_SetNVImageFromFilesW(Pointer handle, int count, WString[] pszFiles);

    //current not support pass byte[][]
    //public int Pos_SetNVImageFromDatas(Pointer handle, int count, byte[][] pdata, int[] pdata_size);

    int Pos_PrintNVImage(Pointer handle, int no);

    int Pos_ClearNVImage(Pointer handle);

    int Pos_SetRAMImageFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Pos_SetRAMImageFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Pos_SetRAMImageFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Pos_SetRAMImage_Helper {
        public static int Pos_SetRAMImageFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_SetRAMImageFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Pos_SetRAMImageFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int Pos_PrintRAMImage(Pointer handle);

    int Pos_PrintHorizontalLine(Pointer handle, int nLineStartPosition, int nLineEndPosition);

    int Pos_PrintHorizontalLineSpecifyThickness(Pointer handle, int nLineStartPosition, int nLineEndPosition, int nLineThickness);

    int Pos_PrintMultipleHorizontalLinesAtOneRow(Pointer handle, int nLineCount, int[] pLineStartPosition, int[] pLineEndPosition);

    int Pos_PrintBitRasterImageUseDC2StarCmdFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Pos_PrintBitRasterImageUseDC2StarCmdFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Pos_PrintBitRasterImageUseDC2StarCmdFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Pos_PrintBitRasterImageUseDC2StarCmd_Helper {
        public static int Pos_PrintBitRasterImageUseDC2StarCmdFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_PrintBitRasterImageUseDC2StarCmdFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Pos_PrintMsbRasterImageUseDC2VCmdFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Pos_PrintMsbRasterImageUseDC2VCmdFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Pos_PrintMsbRasterImageUseDC2VCmdFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Pos_PrintMsbRasterImageUseDC2VCmd_Helper {
        public static int Pos_PrintMsbRasterImageUseDC2VCmdFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_PrintMsbRasterImageUseDC2VCmdFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Pos_PrintLsbRasterImageUseDC2vCmdFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Pos_PrintLsbRasterImageUseDC2vCmdFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Pos_PrintLsbRasterImageUseDC2vCmdFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Pos_PrintLsbRasterImageUseDC2vCmd_Helper {
        public static int Pos_PrintLsbRasterImageUseDC2vCmdFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_PrintLsbRasterImageUseDC2vCmdFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Pos_PrintEpsonTM88IVImageUseGS8CmdFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Pos_PrintEpsonTM88IVImageUseGS8CmdFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Pos_PrintEpsonTM88IVImageUseGS8CmdFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Pos_PrintEpsonTM88IVImageUseGS8Cmd_Helper {
        public static int Pos_PrintEpsonTM88IVImageUseGS8CmdFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Pos_PrintEpsonTM88IVImageUseGS8CmdFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Pos_PrintEpsonTM88IVImageUseGS8CmdFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int Pos_EnableBlackMarkMode(Pointer handle);

    int Pos_DisableBlackMarkMode(Pointer handle);

    int Pos_SetBlackMarkMaxFindLength(Pointer handle, int maxFindLength);

    int Pos_SetBlackMarkMaxFindLengthRuntime(Pointer handle, int maxFindLength);

    int Pos_FindBlackMark(Pointer handle);

    int Pos_SetBlackMarkPaperPrintPosition(Pointer handle, int position);

    int Pos_SetBlackMarkPaperCutPosition(Pointer handle, int position);

    int Pos_FullCutBlackMarkPaper(Pointer handle);

    int Pos_HalfCutBlackMarkPaper(Pointer handle);

    int Page_DrawRect(Pointer handle, int x, int y, int width, int height, int color);

    int Page_DrawBox(Pointer handle, int x, int y, int width, int height, int borderwidth, int bordercolor);

    int Page_DrawTextSpecifyPositionA(Pointer handle, int x, int y, String str);

    int Page_DrawTextSpecifyPositionInUTF8W(Pointer handle, int x, int y, WString str);

    int Page_DrawTextSpecifyPositionInGBKW(Pointer handle, int x, int y, WString str);

    int Page_DrawTextSpecifyPositionInBIG5W(Pointer handle, int x, int y, WString str);

    int Page_DrawTextSpecifyPositionInShiftJISW(Pointer handle, int x, int y, WString str);

    int Page_DrawTextSpecifyPositionInEUCKRW(Pointer handle, int x, int y, WString str);

    int Page_DrawBarcodeSpecifyPositionA(Pointer handle, int x, int y, int nBarcodeType, String str);
    int Page_DrawBarcodeSpecifyPositionW(Pointer handle, int x, int y, int nBarcodeType, WString str);

    int Page_DrawQRCodeSpecifyPositionA(Pointer handle, int x, int y, int nVersion, int nECCLevel, String str);
    int Page_DrawQRCodeSpecifyPositionW(Pointer handle, int x, int y, int nVersion, int nECCLevel, WString str);

    int Page_DrawQRCodeUseEpsonCmdSpecifyPositionA(Pointer handle, int x, int y, int nQRCodeUnitWidth, int nECCLevel, String str);
    int Page_DrawQRCodeUseEpsonCmdSpecifyPositionW(Pointer handle, int x, int y, int nQRCodeUnitWidth, int nECCLevel, WString str);

    int Page_DrawImageSpecifyPositionFromFileA(Pointer handle, int x, int y, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Page_DrawImageSpecifyPositionFromFileW(Pointer handle, int x, int y, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Page_DrawImageSpecifyPositionFromData(Pointer handle, int x, int y, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Page_DrawImageSpecifyPosition_Helper {
        public static int Page_DrawImageSpecifyPositionFromBitmap(Pointer handle, int x, int y, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Page_DrawImageSpecifyPositionFromData(handle, x, y, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Page_DrawImageSpecifyPositionFromPixels(Pointer handle, int x, int y, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int Label_EnableLabelMode(Pointer handle);

    int Label_DisableLabelMode(Pointer handle);

    int Label_CalibrateLabel(Pointer handle);

    int Label_FeedLabel(Pointer handle);

    int Label_PageBegin(Pointer handle, int x, int y, int width, int height, int rotation);

    int Label_PageEnd(Pointer handle);

    int Label_PagePrint(Pointer handle, int copies);

    int Label_DrawTextA(Pointer handle, int x, int y, int font, int style, String str);

    int Label_DrawTextInUTF8W(Pointer handle, int x, int y, int font, int style, WString str);

    int Label_DrawTextInGBKW(Pointer handle, int x, int y, int font, int style, WString str);

    int Label_DrawTextInBIG5W(Pointer handle, int x, int y, int font, int style, WString str);

    int Label_DrawTextInShiftJISW(Pointer handle, int x, int y, int font, int style, WString str);

    int Label_DrawTextInEUCKRW(Pointer handle, int x, int y, int font, int style, WString str);

    int Label_DrawBarcodeA(Pointer handle, int x, int y, int nBarcodeType, int height, int unitwidth, int rotation, String str);
    int Label_DrawBarcodeW(Pointer handle, int x, int y, int nBarcodeType, int height, int unitwidth, int rotation, WString str);

    int Label_DrawQRCodeA(Pointer handle, int x, int y, int nVersion, int nECCLevel, int unitwidth, int rotation, String str);
    int Label_DrawQRCodeW(Pointer handle, int x, int y, int nVersion, int nECCLevel, int unitwidth, int rotation, WString str);

    int Label_DrawPDF417CodeA(Pointer handle, int x, int y, int column, int nAspectRatio, int nECCLevel, int unitwidth, int rotation, String str);
    int Label_DrawPDF417CodeW(Pointer handle, int x, int y, int column, int nAspectRatio, int nECCLevel, int unitwidth, int rotation, WString str);

    int Label_DrawImageFromFileA(Pointer handle, int x, int y, int dstw, int dsth, String pszFile, int binaryzation_method);
    int Label_DrawImageFromFileW(Pointer handle, int x, int y, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int Label_DrawImageFromData(Pointer handle, int x, int y, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class Label_DrawImage_Helper {
        public static int Label_DrawImageFromBitmap(Pointer handle, int x, int y, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.Label_DrawImageFromData(handle, x, y, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int Label_DrawImageFromPixels(Pointer handle, int x, int y, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int Label_DrawLine(Pointer handle, int startx, int starty, int endx, int endy, int linewidth, int linecolor);

    int Label_DrawRect(Pointer handle, int x, int y, int width, int height, int color);

    int Label_DrawBox(Pointer handle, int x, int y, int width, int height, int borderwidth, int bordercolor);

}

