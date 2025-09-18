package com.gtro;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.List;

@Config(name = "mcspa")
    public class MCSPAConfig implements ConfigData {
        @Comment("连接码")
        public String code="awaqwq123";
        @Comment("是否开启调试模式")
        public boolean debug = false;
        @Comment("加密类型")
        public boolean simple_mode=true;

        @Comment("需要敲门的服务器")
        public List<String> serverlist=List.of("127.0.0.1","localhost");
    }