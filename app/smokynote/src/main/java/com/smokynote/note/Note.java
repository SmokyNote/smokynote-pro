package com.smokynote.note;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Represents Note entity.
 * Since there are a lot of non-null fields, you might find useful to construct
 * Note instances using {@link NoteBuilder}.
 *
 * @author Maksim Zakharov
 * @since 1.0
 */
@DatabaseTable(tableName = "note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(canBeNull = false)
    private DateTime schedule;

    @DatabaseField
    private boolean enabled;

    @DatabaseField(canBeNull = false)
    private String filename;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("enabled", isEnabled())
                .append("schedule", getSchedule())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        if (o == this) {
            return true;
        }

        return isEquals((Note) o);
    }

    public boolean isEquals(Note other) {
        return getFilename().equals(other.getFilename());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getFilename())
                .toHashCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(DateTime schedule) {
        this.schedule = schedule;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
