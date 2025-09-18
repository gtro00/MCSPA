package com.gtro.mixin.client;
import com.gtro.AesUtil;
import com.gtro.MCSPAConfig;
import com.gtro.McSPAClient;
import com.gtro.SecretKeyLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.handshake.ConnectionIntent;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;

@Mixin(HandshakeC2SPacket.class)
public class SPA_HandPacketMixin {

@Shadow @Final
@Mutable
private String address;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(int i, String string, int j, ConnectionIntent connectionIntent, CallbackInfo ci) {
        System.out.println("原始地址="+string);

        try {
            InetAddress resolved = InetAddress.getByName(string);
            String realIp = resolved.getHostAddress();
            System.out.println("解析地址="+realIp);
            if( McSPAClient.CONFIG.serverlist.contains(string) || McSPAClient.CONFIG.serverlist.contains(realIp))   //有的环境会存在登录使用主机名导致无法匹配敲门IP列表
            {
                if (McSPAClient.CONFIG.simple_mode)
                {
                    this.address=string+"##"+getcode();
                    System.out.println("简洁模式IP或地址:"+string+" 解析后:"+realIp);
                }
                else
                {
                    this.address=encryptaddr(string);
                    System.out.println("完整模式IP或地址:"+string+" 解析后:"+realIp);
                }
            }
        }catch (Exception e)
        {
            System.out.println("配置文件读取异常！不进行敲门");
            e.printStackTrace();
        }


    }
    private static String encryptaddr(String addr) {
        String sendaddr = addr + "##" + getcode() + "##" + Instant.now().getEpochSecond();
        System.out.println("连接的地址:" + addr);
        System.out.println("组合的字符串:" + sendaddr);
        String AESkey = McSPAClient.AESkey;
        System.out.println("原始密钥base64:"+AESkey);

        String cipher;
        try {
            cipher = AesUtil.encrypt(sendaddr, AESkey);
            System.out.println("加密密文:"+cipher);
        } catch (Exception e) {
            System.out.println("加密异常");
            throw new RuntimeException(e);

        }
        return cipher;
    }

    private static String getcode()
    {
        String code=McSPAClient.CONFIG.code;
        return code;
    }

}
