package com.notes.domain;

import com.notes.domain.enumeration.AssociatedResource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Notes.
 */
@Document(collection = "notes")
public class Notes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("associated_resource_id")
    private String associatedResourceId;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("associated_resource")
    private AssociatedResource associatedResource;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssociatedResourceId() {
        return associatedResourceId;
    }

    public void setAssociatedResourceId(String associatedResourceId) {
        this.associatedResourceId = associatedResourceId;
    }

    public Notes associatedResourceId(String associatedResourceId) {
        this.associatedResourceId = associatedResourceId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Notes title(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Notes content(String content) {
        this.content = content;
        return this;
    }

    public AssociatedResource getAssociatedResource() {
        return associatedResource;
    }

    public void setAssociatedResource(AssociatedResource associatedResource) {
        this.associatedResource = associatedResource;
    }

    public Notes associatedResource(AssociatedResource associatedResource) {
        this.associatedResource = associatedResource;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notes notes = (Notes) o;
        if (notes.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notes.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notes{" +
            "id=" + getId() +
            ", associatedResourceId='" + getAssociatedResourceId() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", associatedResource='" + getAssociatedResource() + "'" +
            "}";
    }
}
