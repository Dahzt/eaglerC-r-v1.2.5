package net.minecraft.src;

import java.io.IOException;

class NetworkWriterThread extends Thread {
	final NetworkManager netManager;

	NetworkWriterThread(NetworkManager var1, String var2) {
		super(var2);
		this.netManager = var1;
	}

	public void run() {
		Object var1 = NetworkManager.threadSyncObject;
		synchronized(var1) {
			++NetworkManager.numWriteThreads;
		}

		while(true) {
			boolean var13 = false;

			try {
				var13 = true;
				if(!NetworkManager.isRunning(this.netManager)) {
					var13 = false;
					break;
				}

				while(NetworkManager.sendNetworkPacket(this.netManager)) {
				}

				try {
					if(NetworkManager.getOutputStream(this.netManager) != null) {
						NetworkManager.getOutputStream(this.netManager).flush();
					}
				} catch (IOException var18) {
					if(!NetworkManager.isTerminating(this.netManager)) {
						NetworkManager.sendError(this.netManager, var18);
					}

					var18.printStackTrace();
				}

				try {
					sleep(2L);
				} catch (InterruptedException var16) {
				}
			} finally {
				if(var13) {
					Object var5 = NetworkManager.threadSyncObject;
					synchronized(var5) {
						--NetworkManager.numWriteThreads;
					}
				}
			}
		}

		var1 = NetworkManager.threadSyncObject;
		synchronized(var1) {
			--NetworkManager.numWriteThreads;
		}
	}
}
