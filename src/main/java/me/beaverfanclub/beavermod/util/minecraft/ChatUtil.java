/*
 * This file is apart of Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>
 * Copyright (C) 2024  Beaver Fan Club
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package me.beaverfanclub.beavermod.util.minecraft;

import me.beaverfanclub.beavermod.Beaver;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatUtil {

    public static final String CHAT_PREFIX = String.format("%s[%sBeaver%s]%s %s",
            EnumChatFormatting.DARK_GRAY,
            EnumChatFormatting.AQUA,
            EnumChatFormatting.DARK_GRAY,
            EnumChatFormatting.RESET,
            EnumChatFormatting.GRAY);

    public static final String SHORT_PREFIX = String.format("%s> %s",
            EnumChatFormatting.WHITE,
            EnumChatFormatting.GRAY);

    public static void send(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer
                .addChatMessage(new ChatComponentText(CHAT_PREFIX + String.format(fmt, args)));
    }

    public static void print(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer
                .addChatMessage(new ChatComponentText(SHORT_PREFIX + String.format(fmt, args)));
    }

    public static void error(String fmt, Object... args) {
        Beaver.INSTANCE.mc.thePlayer
                .addChatMessage(new ChatComponentText(CHAT_PREFIX + EnumChatFormatting.RED + String.format(fmt, args)));
    }

    /**
     * Utility class to easily build IChatComponents (Text).
     * @author Semx11
     * <https://gist.github.com/Semx11/e3c1a8df4d8667a6c30a6d01505418c5>
     */
    public static class Builder {

        private final IChatComponent parent;

        private final String text;
        private final ChatStyle style;

        private Builder(String text) {
            this(text, null, Inheritance.SHALLOW);
        }

        private Builder(String text, IChatComponent parent, Inheritance inheritance) {
            this.parent = parent;
            this.text = text;

            switch (inheritance) {
                case DEEP:
                    this.style = parent != null ? parent.getChatStyle() : new ChatStyle();
                    break;
                default:
                case SHALLOW:
                    this.style = new ChatStyle();
                    break;
                case NONE:
                    this.style = new ChatStyle().setColor(null).setBold(false).setItalic(false)
                            .setStrikethrough(false).setUnderlined(false).setObfuscated(false)
                            .setChatClickEvent(null).setChatHoverEvent(null).setInsertion(null);
                    break;
            }
        }

        public static Builder of(String text) {
            return new Builder(text);
        }

        public Builder setColor(EnumChatFormatting color) {
            style.setColor(color);
            return this;
        }

        public Builder setBold(boolean bold) {
            style.setBold(bold);
            return this;
        }

        public Builder setItalic(boolean italic) {
            style.setItalic(italic);
            return this;
        }

        public Builder setStrikethrough(boolean strikethrough) {
            style.setStrikethrough(strikethrough);
            return this;
        }

        public Builder setUnderlined(boolean underlined) {
            style.setUnderlined(underlined);
            return this;
        }

        public Builder setObfuscated(boolean obfuscated) {
            style.setObfuscated(obfuscated);
            return this;
        }

        public Builder setClickEvent(ClickEvent.Action action, String value) {
            style.setChatClickEvent(new ClickEvent(action, value));
            return this;
        }

        public Builder setHoverEvent(String value) {
            return this.setHoverEvent(new ChatComponentText(value));
        }

        public Builder setHoverEvent(IChatComponent value) {
            return this.setHoverEvent(HoverEvent.Action.SHOW_TEXT, value);
        }

        public Builder setHoverEvent(HoverEvent.Action action, IChatComponent value) {
            style.setChatHoverEvent(new HoverEvent(action, value));
            return this;
        }

        public Builder setInsertion(String insertion) {
            style.setInsertion(insertion);
            return this;
        }

        public Builder append(String text) {
            return this.append(text, Inheritance.SHALLOW);
        }

        public Builder append(String text, Inheritance inheritance) {
            return new Builder(text, this.build(), inheritance);
        }

        public IChatComponent build() {
            IChatComponent thisComponent = new ChatComponentText(text).setChatStyle(style);
            return parent != null ? parent.appendSibling(thisComponent) : thisComponent;
        }

        public void send() {
            Beaver.INSTANCE.mc.thePlayer.addChatMessage(build());
        }

        public enum Inheritance {
            DEEP, SHALLOW, NONE
        }

    }

}
