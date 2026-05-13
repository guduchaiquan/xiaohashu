package com.quanxiaoha.distributed.id.generator.biz.core.common;

public class NoKeyException extends RuntimeException {
    public NoKeyException() {
        super("No key provided");
    }
}

