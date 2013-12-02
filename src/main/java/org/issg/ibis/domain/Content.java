package org.issg.ibis.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public interface Content {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "content_type_id")
    public abstract ContentType getContentType();

    public abstract void setContentType(ContentType contentType);

    @NotNull
    @Column
    public abstract String getContent();

    public abstract void setContent(String content);

}