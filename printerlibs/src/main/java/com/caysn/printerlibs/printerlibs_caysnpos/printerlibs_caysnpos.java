package com.caysn.printerlibs.printerlibs_caysnpos;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.*;

public interface printerlibs_caysnpos extends Library {

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
    printerlibs_caysnpos INSTANCE = Native.loadLibrary(GetLibraryPath_Helper.GetLibraryPath(), printerlibs_caysnpos.class);

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

    int PosAlignment_Left = 0;
    int PosAlignment_HCenter = 1;
    int PosAlignment_Right = 2;

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

    String CaysnPos_LibraryVersion();

    int CaysnPos_EnumComA(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    //public int CaysnPos_EnumComW(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    class CaysnPos_EnumCom_Helper {
        public static String[] CaysnPos_EnumComA() {
            IntByReference pcbNeeded = new IntByReference();
            INSTANCE.CaysnPos_EnumComA(null, 0, pcbNeeded);
            if (pcbNeeded.getValue() > 0) {
                byte[] pBuf = new byte[pcbNeeded.getValue()];
                if (pBuf != null) {
                    INSTANCE.CaysnPos_EnumComA(pBuf, pBuf.length, null);
                    String s = new String(pBuf);
                    String[] ss = s.split("\0");
                    return ss;
                }
            }
            return null;
        }
    }

    int CaysnPos_EnumUsbVidPidA(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    //public int CaysnPos_EnumUsbVidPidW(byte[] pBuf, int cbBuf, IntByReference pcbNeeded);
    class CaysnPos_EnumUsbVidPid_Helper {
        public static String[] CaysnPos_EnumUsbVidPidA() {
            IntByReference pcbNeeded = new IntByReference();
            INSTANCE.CaysnPos_EnumUsbVidPidA(null, 0, pcbNeeded);
            if (pcbNeeded.getValue() > 0) {
                byte[] pBuf = new byte[pcbNeeded.getValue()];
                if (pBuf != null) {
                    INSTANCE.CaysnPos_EnumUsbVidPidA(pBuf, pBuf.length, null);
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
    void CaysnPos_EnumNetPrinterA(int timeout, IntByReference cancel, on_netprinter_discovered_a_callback callback, Pointer data);

    interface on_btdevice_discovered_a_callback extends Callback {
        void on_btdevice_discovered_a(String device_name, String device_address, Pointer private_data);
    }
    void CaysnPos_EnumBtDeviceA(int timeout, IntByReference cancel, on_btdevice_discovered_a_callback callback, Pointer data);

    void CaysnPos_EnumBleDeviceA(int timeout, IntByReference cancel, on_btdevice_discovered_a_callback callback, Pointer data);

    Pointer CaysnPos_OpenComA(String name, int baudrate, int databits, int parity, int stopbits, int flowcontrol);
    Pointer CaysnPos_OpenComW(WString name, int baudrate, int databits, int parity, int stopbits, int flowcontrol);

    Pointer CaysnPos_OpenTcpA(String ip, short port, int timeout);
    Pointer CaysnPos_OpenTcpW(WString ip, short port, int timeout);

    Pointer CaysnPos_OpenTcpBindInterfaceA(String ip, short port, String bind_local_addr, int timeout);
    Pointer CaysnPos_OpenTcpBindInterfaceW(WString ip, short port, WString bind_local_addr, int timeout);

    Pointer CaysnPos_OpenUsbVidPid(short vid, short pid);
    Pointer CaysnPos_OpenUsbVidPidStringA(String name);
    Pointer CaysnPos_OpenUsbVidPidStringW(WString name);

    Pointer CaysnPos_OpenBT2ByConnectA(String address);
    Pointer CaysnPos_OpenBT2ByConnectW(WString address);

    Pointer CaysnPos_OpenBT2ByListenA(int timeout, byte[] address);

    Pointer CaysnPos_OpenBT4ByConnectA(String address);
    Pointer CaysnPos_OpenBT4ByConnectW(WString address);

    Pointer CaysnPos_OpenFileNewA(String name);
    Pointer CaysnPos_OpenFileNewW(WString name);

    Pointer CaysnPos_OpenFileAppendA(String name);
    Pointer CaysnPos_OpenFileAppendW(WString name);

    Pointer CaysnPos_OpenMemory(int nMemorySpaceSize);

    Pointer CaysnPos_MemoryData(Pointer handle);
    class CaysnPos_MemoryData_Helper {
        public static byte[] CaysnPos_MemoryByteArray(Pointer handle) {
            Pointer pdata = INSTANCE.CaysnPos_MemoryData(handle);
            int data_size = INSTANCE.CaysnPos_MemoryDataLength(handle);
            if (pdata != Pointer.NULL) {
                byte[] buffer = pdata.getByteArray(0, data_size);
                return buffer;
            }
            return null;
        }
    }

    int CaysnPos_MemoryDataLength(Pointer handle);

    void CaysnPos_ClearMemoryData(Pointer handle);

    interface on_bytes_writed_callback extends Callback {
        // callback not support byte[]
        void on_bytes_writed(Pointer buffer, int count, Pointer private_data);
    }
    int CaysnPos_SetWritedEvent(Pointer handle, on_bytes_writed_callback callback, Pointer private_data);

    interface on_bytes_readed_callback extends Callback {
        // callback not support byte[]
        void on_bytes_readed(Pointer buffer, int count, Pointer private_data);
    }
    int CaysnPos_SetReadedEvent(Pointer handle, on_bytes_readed_callback callback, Pointer private_data);

    interface on_port_closed_callback extends Callback {
        void on_port_closed(Pointer private_data);
    }
    int CaysnPos_SetClosedEvent(Pointer handle, on_port_closed_callback callback, Pointer private_data);

    int CaysnPos_Write(Pointer handle, byte[] buffer, int count, int timeout);

    int CaysnPos_Read(Pointer handle, byte[] buffer, int count, int timeout);

    int CaysnPos_ReadUntilByte(Pointer handle, byte[] buffer, int count, int timeout, byte breakByte);

    void CaysnPos_SkipAvailable(Pointer handle);

    void CaysnPos_FlushBuffer(Pointer handle);

    void CaysnPos_Close(Pointer handle);

    int CaysnPos_QueryPrinterBufferEmpty(Pointer handle, int timeout);

    int CaysnPos_QueryPrinterStatus(Pointer handle, int timeout);

    int CaysnPos_QueryPrintResult(Pointer handle, int timeout);

    int CaysnPos_KickOutDrawer(Pointer handle, int nDrawerIndex, int nHighLevelTime, int nLowLevelTime);

    int CaysnPos_Beep(Pointer handle, int nBeepCount, int nBeepMs);

    int CaysnPos_FeedAndHalfCutPaper(Pointer handle);

    int CaysnPos_FullCutPaper(Pointer handle);

    int CaysnPos_HalfCutPaper(Pointer handle);

    int CaysnPos_ResetPrinter(Pointer handle);

    int CaysnPos_SetPrinter(Pointer handle, int setType, byte[] buffer, int count);

    int CaysnPos_SetPrintSpeed(Pointer handle, int nSpeed);

    int CaysnPos_SetPrintDensity(Pointer handle, int nDensity);

    int CaysnPos_SetPrintHeatPara(Pointer handle, int nMaxHeatDots, int nHeatOnTime, int nHeatOffTime);

    int CaysnPos_PrintSelfTestPage(Pointer handle);

    int CaysnPos_SetMovementUnit(Pointer handle, int nHorizontalMovementUnit, int nVerticalMovementUnit);

    int CaysnPos_SetPrintAreaLeftMargin(Pointer handle, int nLeftMargin);

    int CaysnPos_SetPrintAreaWidth(Pointer handle, int nWidth);

    int CaysnPos_SetHorizontalAbsolutePrintPosition(Pointer handle, int nPosition);

    int CaysnPos_SetHorizontalRelativePrintPosition(Pointer handle, int nPosition);

    int CaysnPos_SetAlignment(Pointer handle, int nAlignment);

    int CaysnPos_FeedLine(Pointer handle, int numLines);

    int CaysnPos_FeedDot(Pointer handle, int numDots);

    int CaysnPos_PrintTextA(Pointer handle, String str);

    int CaysnPos_PrintTextInUTF8W(Pointer handle, WString str);

    int CaysnPos_PrintTextInGBKW(Pointer handle, WString str);

    int CaysnPos_PrintTextInBIG5W(Pointer handle, WString str);

    int CaysnPos_PrintTextInShiftJISW(Pointer handle, WString str);

    int CaysnPos_PrintTextInEUCKRW(Pointer handle, WString str);

    int CaysnPos_SetTextScale(Pointer handle, int nWidthScale, int nHeightScale);

    int CaysnPos_SetAsciiTextFontType(Pointer handle, int nFontType);

    int CaysnPos_SetTextBold(Pointer handle, int nBold);

    int CaysnPos_SetTextUnderline(Pointer handle, int nUnderline);

    int CaysnPos_SetTextUpsideDown(Pointer handle, int nUpsideDown);

    int CaysnPos_SetTextWhiteOnBlack(Pointer handle, int nWhiteOnBlack);

    int CaysnPos_SetTextRotate(Pointer handle, int nRotate);

    int CaysnPos_SetTextLineHeight(Pointer handle, int nLineHeight);

    int CaysnPos_SetAsciiTextCharRightSpacing(Pointer handle, int nSpacing);

    int CaysnPos_SetKanjiTextCharSpacing(Pointer handle, int nLeftSpacing, int nRightSpacing);

    int CaysnPos_SetSingleByteMode(Pointer handle);

    int CaysnPos_SetCharacterSet(Pointer handle, int nCharacterSet);

    int CaysnPos_SetCharacterCodepage(Pointer handle, int nCharacterCodepage);

    int CaysnPos_SetMultiByteMode(Pointer handle);

    int CaysnPos_SetMultiByteEncoding(Pointer handle, int nEncoding);

    int CaysnPos_SetUserCharacterPatternFromFileA(Pointer handle, byte ch, String pszFile);
    int CaysnPos_SetUserCharacterPatternFromFileW(Pointer handle, byte ch, WString pszFile);

    int CaysnPos_SetUserCharacterPatternFromData(Pointer handle, byte ch, byte[] data, int data_size);
    class CaysnPos_SetUserCharacterPattern_Helper {
        public static int CaysnPos_SetUserCharacterPatternFromBitmap(Pointer handle, byte ch, Bitmap bitmap) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPos_SetUserCharacterPatternFromData(handle, ch, data, data.length);
            }
            return result;
        }
    }

    int CaysnPos_SetUserCharacterPatternFromPixels(Pointer handle, byte ch, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format);

    int CaysnPos_ClearUserCharacterPattern(Pointer handle, byte ch);

    int CaysnPos_SetUserCharacterEnable(Pointer handle, int enable);

    int CaysnPos_SetUserKanjiPatternFromFileA(Pointer handle, byte c1, byte c2, String pszFile);
    int CaysnPos_SetUserKanjiPatternFromFileW(Pointer handle, byte c1, byte c2, WString pszFile);

    int CaysnPos_SetUserKanjiPatternFromData(Pointer handle, byte c1, byte c2, byte[] data, int data_size);
    class CaysnPos_SetUserKanjiPattern_Helper {
        public static int CaysnPos_SetUserKanjiPatternFromBitmap(Pointer handle, byte c1, byte c2, Bitmap bitmap) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPos_SetUserKanjiPatternFromData(handle, c1, c2, data, data.length);
            }
            return result;
        }
    }

    int CaysnPos_SetUserKanjiPatternFromPixels(Pointer handle, byte c1, byte c2, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format);

    int CaysnPos_PrintBarcodeA(Pointer handle, int nBarcodeType, String str);
    int CaysnPos_PrintBarcodeW(Pointer handle, int nBarcodeType, WString str);

    int CaysnPos_SetBarcodeUnitWidth(Pointer handle, int nBarcodeUnitWidth);

    int CaysnPos_SetBarcodeHeight(Pointer handle, int nBarcodeHeight);

    int CaysnPos_SetBarcodeReadableTextFontType(Pointer handle, int nFontType);

    int CaysnPos_SetBarcodeReadableTextPosition(Pointer handle, int nTextPosition);

    int CaysnPos_PrintQRCodeA(Pointer handle, int nVersion, int nECCLevel, String str);
    int CaysnPos_PrintQRCodeW(Pointer handle, int nVersion, int nECCLevel, WString str);

    int CaysnPos_PrintQRCodeUseEpsonCmdA(Pointer handle, int nQRCodeUnitWidth, int nECCLevel, String str);
    int CaysnPos_PrintQRCodeUseEpsonCmdW(Pointer handle, int nQRCodeUnitWidth, int nECCLevel, WString str);

    int CaysnPos_PrintDoubleQRCodeA(Pointer handle, int nQRCodeUnitWidth, int nQR1Position, int nQR1Version, int nQR1ECCLevel, String strQR1, int nQR2Position, int nQR2Version, int nQR2ECCLevel, String strQR2);
    int CaysnPos_PrintDoubleQRCodeW(Pointer handle, int nQRCodeUnitWidth, int nQR1Position, int nQR1Version, int nQR1ECCLevel, WString strQR1, int nQR2Position, int nQR2Version, int nQR2ECCLevel, WString strQR2);

    int CaysnPos_PrintPDF417BarcodeUseEpsonCmdA(Pointer handle, int columnCount, int rowCount, int unitWidth, int rowHeight, int nECCLevel, int dataProcessingMode, String str);
    int CaysnPos_PrintPDF417BarcodeUseEpsonCmdW(Pointer handle, int columnCount, int rowCount, int unitWidth, int rowHeight, int nECCLevel, int dataProcessingMode, WString str);

    int CaysnPos_GetImageSizeFromFileA(String pszFile, IntByReference depth, IntByReference width, IntByReference height);
    int CaysnPos_GetImageSizeFromFileW(WString pszFile, IntByReference depth, IntByReference width, IntByReference height);

    int CaysnPos_GetImageSizeFromData(byte[] data, int data_size, IntByReference depth, IntByReference width, IntByReference height);

    int CaysnPos_PrintRasterImageFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int CaysnPos_PrintRasterImageFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int CaysnPos_PrintRasterImageFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class CaysnPos_PrintRasterImage_Helper {
        public static int CaysnPos_PrintRasterImageFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPos_PrintRasterImageFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int CaysnPos_PrintRasterImageFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int CaysnPos_PrintRasterImageWithCompressFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method, int compress_method);
    int CaysnPos_PrintRasterImageWithCompressFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method, int compress_method);

    int CaysnPos_PrintRasterImageWithCompressFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method, int compress_method);
    class CaysnPos_PrintRasterImageWithCompress_Helper {
        public static int CaysnPos_PrintRasterImageWithCompressFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method, int compress_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPos_PrintRasterImageWithCompressFromData(handle, dstw, dsth, data, data.length, binaryzation_method, compress_method);
            }
            return result;
        }
    }

    int CaysnPos_PrintRasterImageWithCompressFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method, int compress_method);

    int CaysnPos_FillDotImageToCurrentLineFromFileA(Pointer handle, String pszFile, int binaryzation_method);
    int CaysnPos_FillDotImageToCurrentLineFromFileW(Pointer handle, WString pszFile, int binaryzation_method);

    int CaysnPos_FillDotImageToCurrentLineFromData(Pointer handle, byte[] data, int data_size, int binaryzation_method);
    class CaysnPos_FillDotImageToCurrentLine_Helper {
        public static int CaysnPos_FillDotImageToCurrentLineFromBitmap(Pointer handle, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPos_FillDotImageToCurrentLineFromData(handle, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int CaysnPos_FillDotImageToCurrentLineFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int CaysnPos_PrintDotImageSpecifyHorizontalPositionFromFileA(Pointer handle, int nPosition, int dstw, int dsth, String pszFile, int binaryzation_method);
    int CaysnPos_PrintDotImageSpecifyHorizontalPositionFromFileW(Pointer handle, int nPosition, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int CaysnPos_PrintDotImageSpecifyHorizontalPositionFromData(Pointer handle, int nPosition, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class CaysnPos_PrintDotImageSpecifyHorizontalPosition_Helper {
        public static int CaysnPos_PrintDotImageSpecifyHorizontalPositionFromBitmap(Pointer handle, int nPosition, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPos_PrintDotImageSpecifyHorizontalPositionFromData(handle, nPosition, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int CaysnPos_PrintDotImageSpecifyHorizontalPositionFromPixels(Pointer handle, int nPosition, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int CaysnPos_SetNVImageFromFilesA(Pointer handle, int count, String[] pszFiles);
    int CaysnPos_SetNVImageFromFilesW(Pointer handle, int count, WString[] pszFiles);

    //current not support pass byte[][]
    //public int CaysnPos_SetNVImageFromDatas(Pointer handle, int count, byte[][] pdata, int[] pdata_size);

    int CaysnPos_PrintNVImage(Pointer handle, int no);

    int CaysnPos_ClearNVImage(Pointer handle);

    int CaysnPos_SetRAMImageFromFileA(Pointer handle, int dstw, int dsth, String pszFile, int binaryzation_method);
    int CaysnPos_SetRAMImageFromFileW(Pointer handle, int dstw, int dsth, WString pszFile, int binaryzation_method);

    int CaysnPos_SetRAMImageFromData(Pointer handle, int dstw, int dsth, byte[] data, int data_size, int binaryzation_method);
    class CaysnPos_SetRAMImage_Helper {
        public static int CaysnPos_SetRAMImageFromBitmap(Pointer handle, int dstw, int dsth, Bitmap bitmap, int binaryzation_method) {
            int result = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                byte[] data = os.toByteArray();
                result = INSTANCE.CaysnPos_SetRAMImageFromData(handle, dstw, dsth, data, data.length, binaryzation_method);
            }
            return result;
        }
    }

    int CaysnPos_SetRAMImageFromPixels(Pointer handle, byte[] img_data, int img_datalen, int img_width, int img_height, int img_stride, int img_format, int binaryzation_method);

    int CaysnPos_PrintRAMImage(Pointer handle);

    int CaysnPos_PrintHorizontalLine(Pointer handle, int nLineStartPosition, int nLineEndPosition);

    int CaysnPos_PrintHorizontalLineSpecifyThickness(Pointer handle, int nLineStartPosition, int nLineEndPosition, int nLineThickness);

    int CaysnPos_PrintMultipleHorizontalLinesAtOneRow(Pointer handle, int nLineCount, int[] pLineStartPosition, int[] pLineEndPosition);

    int CaysnPos_EnableBlackMarkMode(Pointer handle);

    int CaysnPos_DisableBlackMarkMode(Pointer handle);

    int CaysnPos_SetBlackMarkMaxFindLength(Pointer handle, int maxFindLength);

    int CaysnPos_FindBlackMark(Pointer handle);

    int CaysnPos_SetBlackMarkPaperPrintPosition(Pointer handle, int position);

    int CaysnPos_SetBlackMarkPaperCutPosition(Pointer handle, int position);

    int CaysnPos_FullCutBlackMarkPaper(Pointer handle);

    int CaysnPos_HalfCutBlackMarkPaper(Pointer handle);

}

