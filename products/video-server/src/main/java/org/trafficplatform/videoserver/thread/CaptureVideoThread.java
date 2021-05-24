package org.trafficplatform.videoserver.thread;

import org.trafficplatform.videoserver.service.video.capture.IVideoCapture;

public class CaptureVideoThread extends Thread {

	private IVideoCapture videoCapture;

	public CaptureVideoThread(IVideoCapture videoCapture) {

		this.videoCapture = videoCapture;
	}

	public IVideoCapture getVideoCapture() {

		return this.videoCapture;
	}

	@Override
	public void run() {

		this.videoCapture.startThread();
	}
}
