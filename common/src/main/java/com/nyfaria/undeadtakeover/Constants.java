package com.nyfaria.undeadtakeover;

import net.minecraft.resources.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MODID = "undeadtakeover";
	public static final String MOD_NAME = "Undead Takeover";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static Identifier modLoc(String path){
        return Identifier.fromNamespaceAndPath(Constants.MODID, path);
    }
}