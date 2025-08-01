package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Login extends Packet {
	public int protocolVersion;
	public String username;
	public WorldType terrainType;
	public int serverMode;
	public int field_48170_e;
	public byte difficultySetting;
	public byte worldHeight;
	public byte maxPlayers;

	public Packet1Login() {
	}

	public Packet1Login(String var1, int var2) {
		this.username = var1;
		this.protocolVersion = var2;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.protocolVersion = var1.readInt();
		this.username = readString(var1, 16);
		String var2 = readString(var1, 16);
		this.terrainType = WorldType.parseWorldType(var2);
		if(this.terrainType == null) {
			this.terrainType = WorldType.DEFAULT;
		}

		this.serverMode = var1.readInt();
		this.field_48170_e = var1.readInt();
		this.difficultySetting = var1.readByte();
		this.worldHeight = var1.readByte();
		this.maxPlayers = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.protocolVersion);
		writeString(this.username, var1);
		if(this.terrainType == null) {
			writeString("", var1);
		} else {
			writeString(this.terrainType.func_48628_a(), var1);
		}

		var1.writeInt(this.serverMode);
		var1.writeInt(this.field_48170_e);
		var1.writeByte(this.difficultySetting);
		var1.writeByte(this.worldHeight);
		var1.writeByte(this.maxPlayers);
	}

	public void processPacket(NetHandler var1) {
		var1.handleLogin(this);
	}

	public int getPacketSize() {
		int var1 = 0;
		if(this.terrainType != null) {
			var1 = this.terrainType.func_48628_a().length();
		}

		return 4 + this.username.length() + 4 + 7 + 7 + var1;
	}
}
