# Camera
Android拍照录像功能
project里只上传了两个module
录像功能步骤如下:
使用录像我们分以下几步：
1、打开相机—–调用 Camera.open()
2、设置预览——创建SurfaceView 并调用 Camera.setPreviewDisplay(). 设置SurfaceHoder
3、开始预览—–调用 Camera.startPreview()
4、开始录制视频
a、解锁Camera调用 Camera.unlock().
b、配置MediaRecord
1、setCamera()——–设置camera
2、setAudioSource()—使用 MediaRecorder.AudioSource.CAMCORDER.
3、setVideoSource() —- 使用MediaRecorder.VideoSource.CAMERA.
4、设置output format 在android2.2或者更高版，可以使用 MediaRecorder.setProfile（）方法和 CamcorderProfile.get() 设置。
①setOutputFormat() —–设置输出格式，可以是默认 或者MediaRecorder.OutputFormat.MPEG_4.
②setAudioEncoder() ——-设置声音编码，可以是默认或者 MediaRecorder.AudioEncoder.AMR_NB.
③、setVideoEncoder() —-可以是默认或者MediaRecorder.VideoEncoder.MPEG_4_SP.
5、setOutputFile() 设置输出文件路径
6、setPreviewDisplay() 这是SurfaceHolder
注意MediaRecorder 的设置应该按以上顺序执行，负责可能会crash,或者录制失败
c、调用MediaRecorder.prepare().
d、调用MediaRecorder.start().
5、停止录像
a、调用MediaRecorder.stop().
b、调用 MediaRecorder.reset(). 复位MediaRecorder的设置
c、调用MediaRecorder.release(). 释放MediaRecorder
d、调用Camera.lock().
6、停止预览 调用 Camera.stopPreview().
7、释放相机 调用 Camera.release()

详见代码注释
