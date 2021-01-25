package com.king.graduation.backend_system.Utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author king
 * @date 2020/12/31 - 20:24
 */
public class VideoUtil {

//    public static String Base_Path = "E:\\idea\\projects\\bishe";
    public static String Base_Path = "/data/home/lian/MyApplication/develop/idea/projects/bishe";
    public static String VIDEO_BASE_PATH = Base_Path + "/video/";
    public static String ZERO_VIDEO_PATH = Base_Path + "/video/zeroVideo/";
    public static String FREE_VIDEO_PATH = Base_Path + "/video/freestyleVideo/";
    public static String BUTTER_VIDEO_PATH = Base_Path + "/video/butterflyStrokeVideo/";
    public static String BREAST_VIDEO_PATH = Base_Path + "/video/breaststrokeVideo/";

    public static String ZERO_VIDEO_IMG_PATH = ZERO_VIDEO_PATH + "img/";
    public static String FREE_VIDEO_IMG_PATH = FREE_VIDEO_PATH + "img/";
    public static String BUTTER_VIDEO_IMG_PATH = BUTTER_VIDEO_PATH + "img/";
    public static String BREAST_VIDEO_IMG_PATH = BREAST_VIDEO_PATH + "img/";

    public static final String SUFFIX = ".jpg";


    /**
     * @Describe 返回视屏路径
     * @Author king
     * @Date 2021/1/21 - 23:07
     * @Params [type]
     */
    public static String getVideoPath(int type) {
        switch (type) {
            case 1:
                return ZERO_VIDEO_PATH;
            case 2:
                return FREE_VIDEO_PATH;
            case 3:
                return BUTTER_VIDEO_PATH;
            case 4:
                return BREAST_VIDEO_PATH;
            default:
                return VIDEO_BASE_PATH;
        }
    }

    /**
     * @Describe 返回视屏封面路径
     * @Author king
     * @Date 2021/1/21 - 23:07
     * @Params [type]
     */
    public static String getVideoImgPath(int type) {
        switch (type) {
            case 1:
                return ZERO_VIDEO_IMG_PATH;
            case 2:
                return FREE_VIDEO_IMG_PATH;
            case 3:
                return BUTTER_VIDEO_IMG_PATH;
            case 4:
                return BREAST_VIDEO_IMG_PATH;
            default:
                return VIDEO_BASE_PATH;
        }
    }

    /**
     * 获取视频时长，单位为秒
     *
     * @param video 源视频文件
     * @return 时长（s）
     */
    public static long getVideoDuration(File video) {
        long duration = 0L;
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        try {
            ff.start();
            duration = ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

    /**
     * 截取视频获得指定帧的图片
     *
     * @param video   源视频文件
     * @param picPath 截图存放路径
     */
    public static void getVideoPic(File video, String picPath) {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        try {
            ff.start();

            // 截取中间帧图片(具体依实际情况而定)
            int i = 0;
            int length = ff.getLengthInFrames();
            int middleFrame = length / 2;
            Frame frame = null;
            while (i < length) {
                frame = ff.grabFrame();
                if ((i > middleFrame) && (frame.image != null)) {
                    break;
                }
                i++;
            }
            // 截取的帧图片
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage srcImage = converter.getBufferedImage(frame);
            int srcImageWidth = srcImage.getWidth();
            int srcImageHeight = srcImage.getHeight();

            // 对截图进行等比例缩放(缩略图)
            int width = 480;
            int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
            BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            File picFile = new File(picPath);
            ImageIO.write(thumbnailImage, "jpg", picFile);

            ff.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
