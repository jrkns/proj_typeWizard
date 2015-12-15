package entity;

import java.awt.event.KeyEvent;

import render.Resource;
import input.InputUtility;

public class BgmControl implements Runnable {

	private boolean isPause = false;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println(isPause + " " +InputUtility.getKeyPressed(KeyEvent.VK_5));
			if (isPause) {
				synchronized (this) {
					try {
						Resource.screenbgm.stop();
						this.wait();
						Resource.screenbgm.loop();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean x) {
		isPause = x;
	}

}
