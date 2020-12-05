package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalTime;

@Data
public class LessonScheduleDto extends ObjectRef {

    private Long order;
    private LocalTime startTime;
    private LocalTime finishTime;
    private Shift shift;

    public enum Shift {
        FIRST("1"),
        SECOND("2"),
        ;

        private String value;

        Shift(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Shift find(String value) {
            for (Shift shift : Shift.values()) {
                if (shift.value.equals(value)) {
                    return shift;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LessonScheduleDto that = (LessonScheduleDto) o;

        boolean isIdEquals = false;
        if (getId() != null) {
            isIdEquals = getId().equals(that.getId());
        }

        return isIdEquals || new EqualsBuilder()
                .append(startTime, that.startTime)
                .append(finishTime, that.finishTime)
                .append(shift, that.shift)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(startTime)
                .append(finishTime)
                .append(shift)
                .toHashCode();
    }
}
