package aio.health2world.recyclerview;

import android.content.Context;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class DividerItemDecoration1 extends Y_DividerItemDecoration {

    public DividerItemDecoration1(Context context) {
        super(context);
    }

    @Override
    public Y_Divider getDivider(int itemPosition) {
        Y_Divider divider = null;
//        switch (itemPosition % 2) {
//            case 0:
//                //每一行第一个显示rignt和bottom
//                divider = new Y_DividerBuilder()
//                        .setRightSideLine(true, 0xff666666, 10, 0, 0)
//                        .setBottomSideLine(true, 0xff666666, 20, 0, 0)
//                        .create();
//                break;
//            case 1:
//                //第二个显示Left和bottom
//                divider = new Y_DividerBuilder()
//                        .setLeftSideLine(true, 0xff666666, 10, 0, 0)
//                        .setBottomSideLine(true, 0xff666666, 20, 0, 0)
//                        .create();
//                break;
//            default:
//                break;
//        }
        divider = new Y_DividerBuilder()
                .setLeftSideLine(true, 0xffeeeeee, 1, 0, 0)
                .setBottomSideLine(true, 0xffeeeeee, 1, 0, 0)
                .create();
        return divider;
    }
}