package aio.health2world.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import aio.health2world.banner.transformer.DepthPageTransformer;
import aio.health2world.banner.transformer.ScaleInOutTransformer;
import aio.health2world.banner.transformer.AccordionTransformer;
import aio.health2world.banner.transformer.BackgroundToForegroundTransformer;
import aio.health2world.banner.transformer.CubeInTransformer;
import aio.health2world.banner.transformer.CubeOutTransformer;
import aio.health2world.banner.transformer.DefaultTransformer;
import aio.health2world.banner.transformer.FlipHorizontalTransformer;
import aio.health2world.banner.transformer.FlipVerticalTransformer;
import aio.health2world.banner.transformer.ForegroundToBackgroundTransformer;
import aio.health2world.banner.transformer.RotateDownTransformer;
import aio.health2world.banner.transformer.RotateUpTransformer;
import aio.health2world.banner.transformer.StackTransformer;
import aio.health2world.banner.transformer.TabletTransformer;
import aio.health2world.banner.transformer.ZoomInTransformer;
import aio.health2world.banner.transformer.ZoomOutSlideTransformer;
import aio.health2world.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
