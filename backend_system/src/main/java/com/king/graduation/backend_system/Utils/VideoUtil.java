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

    public static final String VIDEO_BASE_PATH ="F:\\idea\\projects\\bishe\\video\\";
    public static final String ZERO_VIDEO_PATH ="F:\\idea\\projects\\bishe\\video\\zeroVideo\\";
    public static final String FREE_VIDEO_PATH ="F:\\idea\\projects\\bishe\\video\\freestyleVideo\\";
    public static final String BUTTER_VIDEO_PATH ="F:\\idea\\projects\\bishe\\video\\butterflyStrokeVideo\\";
    public static final String BREAST_VIDEO_PATH ="F:\\idea\\projects\\bishe\\video\\breaststrokeVideo\\";

    public static final String ZERO_VIDEO_IMG_PATH = ZERO_VIDEO_PATH +"img\\";
    public static final String FREE_VIDEO_IMG_PATH = FREE_VIDEO_PATH +"img\\";
    public static final String BUTTER_VIDEO_IMG_PATH = BUTTER_VIDEO_PATH +"img\\";
    public static final String BREAST_VIDEO_IMG_PATH = BREAST_VIDEO_PATH +"img\\";

    public static final String SUFFIX =".jpg";

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

    public static void main(String[] args) {
        String videoName = "初学游泳第一节课零基础学员，蹬池边漂浮练习要点_ 憋气、漂浮、滑行 标清(270P).qlv.mp4";
        String realVideo=VideoUtil.ZERO_VIDEO_PATH+videoName;
        File file = new File(realVideo);
        String fileName = file.getName().split("\\.")[0];

        System.out.println(fileName);

        String imgName = VideoUtil.ZERO_VIDEO_IMG_PATH + fileName + SUFFIX;
        VideoUtil.getVideoPic(file, imgName);
    }

}
