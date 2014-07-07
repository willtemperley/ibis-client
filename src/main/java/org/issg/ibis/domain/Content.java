package org.issg.ibis.domain;


public interface Content {

    public abstract ContentType getContentType();

    public abstract void setContentType(ContentType contentType);

    public abstract String getContent();

    public abstract void setContent(String content);

}