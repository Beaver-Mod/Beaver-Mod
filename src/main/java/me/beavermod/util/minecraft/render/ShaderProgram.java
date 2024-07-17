/*
 * Some of the source in the file was originally created by Jinthium <https://github.com/Jinthium>
 *
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.minecraft.render;

import me.beavermod.Beaver;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programID;

    public ShaderProgram(ResourceLocation fragmentShader, ResourceLocation vertexShader) {

        this.programID = glCreateProgram();

        try {
            glAttachShader(programID, createShader(fragmentShader, GL_FRAGMENT_SHADER));
            glAttachShader(programID, createShader(vertexShader, GL_VERTEX_SHADER));
        } catch (IOException exception) {
            Beaver.LOGGER.error("Failed to create shader program", exception);
        }

        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
            throw new IllegalStateException("Failed to link shader.");
        }
    }

    public ShaderProgram(ResourceLocation fragmentShader) {
        this(fragmentShader, new ResourceLocation("beaver/shader/vertex/vertex.vert"));
    }

    public void load() {
        glUseProgram(this.programID);
    }

    public void unload() {
        glUseProgram(0);
    }

    public int getProgramID() {
        return this.programID;
    }

    public int getUniform(String name) {
        return glGetUniformLocation(this.programID, name);
    }

    public void setUniformf(String name, float... args) {
        int location = glGetUniformLocation(this.programID, name);

        // No enhanced switch :(
        switch (args.length) {
            case 1:
                glUniform1f(location, args[0]);
                break;

            case 2:
                glUniform2f(location, args[0], args[1]);
                break;

            case 3:
                glUniform3f(location, args[0], args[1], args[2]);
                break;

            case 4:
                glUniform4f(location, args[0], args[1], args[2], args[3]);
                break;
        }
    }

    public void setUniformi(String name, int... args) {
        int loc = glGetUniformLocation(this.programID, name);
        if (args.length > 1) {
            glUniform2i(loc, args[0], args[1]);
        } else {
            glUniform1i(loc, args[0]);
        }

    }

    public static int createShader(ResourceLocation location, int shaderType) throws IOException {
        String shaderSource = IOUtils.toString(Beaver.INSTANCE.mc.getResourceManager().getResource(location).getInputStream());
        int shader = glCreateShader(shaderType);
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            Beaver.LOGGER.info(glGetShaderInfoLog(shader, 0x1000));
            throw new IllegalStateException(String.format("Failed to compile shader: %d", shaderType));
        }

        return shader;
    }

}
