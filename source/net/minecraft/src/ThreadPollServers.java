package net.minecraft.src;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

class ThreadPollServers extends Thread {
	final ServerNBTStorage server;
	final GuiSlotServer serverSlotContainer;

	ThreadPollServers(GuiSlotServer var1, ServerNBTStorage var2) {
		this.serverSlotContainer = var1;
		this.server = var2;
	}

	public void run() {
		boolean var27 = false;

		Object var41;
		label183: {
			label184: {
				label185: {
					label186: {
						label187: {
							try {
								var27 = true;
								this.server.motd = "\u00a78Polling..";
								long var1 = System.nanoTime();
								GuiMultiplayer.pollServer(this.serverSlotContainer.parentGui, this.server);
								long var3 = System.nanoTime();
								this.server.lag = (var3 - var1) / 1000000L;
								var27 = false;
								break label183;
							} catch (UnknownHostException var35) {
								this.server.lag = -1L;
								this.server.motd = "\u00a74Can\'t resolve hostname";
								var27 = false;
							} catch (SocketTimeoutException var36) {
								this.server.lag = -1L;
								this.server.motd = "\u00a74Can\'t reach server";
								var27 = false;
								break label187;
							} catch (ConnectException var37) {
								this.server.lag = -1L;
								this.server.motd = "\u00a74Can\'t reach server";
								var27 = false;
								break label186;
							} catch (IOException var38) {
								this.server.lag = -1L;
								this.server.motd = "\u00a74Communication error";
								var27 = false;
								break label185;
							} catch (Exception var39) {
								this.server.lag = -1L;
								this.server.motd = "ERROR: " + var39.getClass();
								var27 = false;
								break label184;
							} finally {
								if(var27) {
									Object var12 = GuiMultiplayer.getLock();
									synchronized(var12) {
										GuiMultiplayer.decrementThreadsPending();
									}
								}
							}

							var41 = GuiMultiplayer.getLock();
							synchronized(var41) {
								GuiMultiplayer.decrementThreadsPending();
								return;
							}
						}

						var41 = GuiMultiplayer.getLock();
						synchronized(var41) {
							GuiMultiplayer.decrementThreadsPending();
							return;
						}
					}

					var41 = GuiMultiplayer.getLock();
					synchronized(var41) {
						GuiMultiplayer.decrementThreadsPending();
						return;
					}
				}

				var41 = GuiMultiplayer.getLock();
				synchronized(var41) {
					GuiMultiplayer.decrementThreadsPending();
					return;
				}
			}

			var41 = GuiMultiplayer.getLock();
			synchronized(var41) {
				GuiMultiplayer.decrementThreadsPending();
				return;
			}
		}

		var41 = GuiMultiplayer.getLock();
		synchronized(var41) {
			GuiMultiplayer.decrementThreadsPending();
		}

	}
}
