package com.smokynote.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
@DatabaseTable(tableName = "note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(canBeNull = false)
    private DateTime schedule;

    @DatabaseField
    private boolean enabled;

    @DatabaseField(canBeNull = false)
    private String filename;

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
