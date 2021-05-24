package org.trafficplatform.videoserver.service.video.capture;

public interface IVideoCapture {
		
	public double getWidth();
	public double getHeight();

	public void startThread();
	public void stopThread();
	
	public byte[] getCurrentImage();
	
	public boolean setEnableSaveVideo(boolean enableSaveVideo);
	public boolean isEnableSaveVideo();
	
}
