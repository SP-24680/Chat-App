package com.raven.model;

public enum MessageType {
    TEXT(0),
    IMAGE(1),
    FILE(2),
    EMOJI(3),
    VOICE(4),
    VIDEO(5),
    GIF(6);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageType toMessageType(int value) {
        for (MessageType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return TEXT; // fallback
    }
}
